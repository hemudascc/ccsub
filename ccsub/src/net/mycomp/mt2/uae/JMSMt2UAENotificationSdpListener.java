package net.mycomp.mt2.uae;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPAAdnetworkToken;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.AdnetworkToken;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

public class JMSMt2UAENotificationSdpListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2UAENotificationSdpListener.class);



	@Autowired
	private IDaoService daoService;

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private JPAAdnetworkToken jpaAdnetworkToken;

	@Override
	public void onMessage(Message m) {

		Mt2UAENotificationSdp mt2UAENotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		Mt2UAEServiceConfig mt2UAEServiceConfig=null;
		List<SubscriberReg> subscriberRegs;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2UAENotification = (Mt2UAENotificationSdp) objectMessage.getObject();
			logger.info("mt2UAENotification::::: "+mt2UAENotification);

			String[] array = mt2UAENotification.getData().split(",");
			if("S".equals(array[0])) {
				if("Du".equalsIgnoreCase(mt2UAENotification.getOperator())) {
					cgToken= new CGToken(Objects.toString(redisCacheService.getObjectCacheValue(Mt2UAEConstant.MT2_UAE_TOKEN_TRACKINGID_PREFIX+mt2UAENotification.getTrackingId())));
				}else {
					cgToken= new CGToken(Objects.toString(redisCacheService.getObjectCacheValue(Mt2UAEConstant.MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX+mt2UAENotification.getMsisdn())));
				}
				mt2UAENotification.setAction(MConstants.ACT);
				mt2UAENotification.setSdpServiceId(array[1]);
				mt2UAENotification.setReferenceId(array[2]);
				mt2UAENotification.setToken(cgToken.getCGToken());
				redisCacheService.putObjectCacheValueByEvictionDay(Mt2UAEConstant.MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX+mt2UAENotification.getMsisdn(), cgToken.getCGToken(), 1);
			}else if("U".equals(array[0])){
				mt2UAENotification.setAction(MConstants.DCT);
				subscriberRegs = jpaSubscriberReg.findSubscriberRegByMsisdn(mt2UAENotification.getMsisdn());
				if(Objects.nonNull(subscriberRegs) && subscriberRegs.size()!=0) {
					AdnetworkToken adnetworkToken = jpaAdnetworkToken.findEnableAdnetworkToken(subscriberRegs.get(0).getTokenId());
					if(Objects.nonNull(adnetworkToken)) {
						cgToken = new CGToken(adnetworkToken.getTokenToCg());
					}
				}
			}else {
				logger.error("unknown action mt2uae notification");
			}
			if(Objects.nonNull(mt2UAENotification.getAction())){
				VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
				if(Objects.nonNull(vwServiceCampaignDetail)) {
					mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(vwServiceCampaignDetail.getServiceId());
				}
				if(Objects.isNull(mt2UAEServiceConfig)) {
					if("Du".equalsIgnoreCase(mt2UAENotification.getOperator())) {
						mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(89);
					}else if("Etisalat".equalsIgnoreCase(mt2UAENotification.getOperator())){
						mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(88);
					}else {
						mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(90);
					}
				}
				Service service = MData.mapServiceIdToService.get(mt2UAEServiceConfig.getServiceId());
				liveReport = new LiveReport(service.getOpId(), new Timestamp(System.currentTimeMillis()),
						cgToken.getCampaignId(),mt2UAEServiceConfig.getServiceId(),
						mt2UAEServiceConfig.getProductId()); 
				liveReport.setTokenId(cgToken.getTokenId());
				liveReport.setToken(cgToken.getCGToken());
				liveReport.setMsisdn(mt2UAENotification.getMsisdn());
				liveReport.setCircleId(0);
				liveReport.setParam1(mt2UAENotification.getReferenceId());
				//liveReport.setMode(MT2UAENotificationMode.getMode(mt2UAENotification.getChannel()));
				liveReport.setMode("");
				if(mt2UAENotification.getAction().equals(MConstants.DCT)) {
					liveReport.setAction(MConstants.DCT);			
					liveReport.setDctCount(1);
				}else if(mt2UAENotification.getAction().equals(MConstants.ACT)) {
					liveReport.setAction(MConstants.ACT);			
					liveReport.setConversionCount(1);
					liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());
				}
			}
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {		
				liveReport.setAction(mt2UAENotification.getAction());			
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2UAENotification:: "
						+ mt2UAENotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2UAENotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
