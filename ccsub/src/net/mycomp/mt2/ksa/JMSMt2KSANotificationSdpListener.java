package net.mycomp.mt2.ksa;

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
import net.mycomp.mt2.uae.Mt2UAEConstant;
import net.persist.bean.AdnetworkToken;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

public class JMSMt2KSANotificationSdpListener implements MessageListener {

	private static final Logger logger = Logger.getLogger(JMSMt2KSANotificationSdpListener.class);
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

		Mt2KSANotificationSdp mt2ksaNotificationSdp = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=new CGToken("");
		Mt2KSAServiceConfig mt2ksaServiceConfig =null;
		List<SubscriberReg> subscriberRegs;
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2ksaNotificationSdp = (Mt2KSANotificationSdp) objectMessage.getObject(); 
			logger.debug("received notification ="+mt2ksaNotificationSdp);
			String[] array = mt2ksaNotificationSdp.getData().split(",");
			if("S".equals(array[0])) {
				/*
				 * if("".equalsIgnoreCase(mt2ksaNotificationSdp.getOperator())) { cgToken= new
				 * CGToken(Objects.toString(redisCacheService.getObjectCacheValue(Mt2UAEConstant
				 * .MT2_UAE_TOKEN_TRACKINGID_PREFIX+mt2UAENotification.getTrackingId()))); }else
				 * { cgToken= new
				 * CGToken(Objects.toString(redisCacheService.getObjectCacheValue(Mt2UAEConstant
				 * .MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX+mt2UAENotification.getMsisdn()))); }
				 */

				//TODO need to fetch token here 
				mt2ksaNotificationSdp.setAction(MConstants.ACT);
				mt2ksaNotificationSdp.setSdpServiceId(array[1]);
				mt2ksaNotificationSdp.setReferenceId(array[2]);
				mt2ksaNotificationSdp.setToken(cgToken.getCGToken());
				redisCacheService.putObjectCacheValueByEvictionDay(Mt2UAEConstant.MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX+mt2ksaNotificationSdp.getMsisdn(), cgToken.getCGToken(), 1);
			}else if("U".equals(array[0])){
				mt2ksaNotificationSdp.setAction(MConstants.DCT);
				subscriberRegs = jpaSubscriberReg.findSubscriberRegByMsisdn(mt2ksaNotificationSdp.getMsisdn());
				if(Objects.nonNull(subscriberRegs) && subscriberRegs.size()!=0) {
					AdnetworkToken adnetworkToken = jpaAdnetworkToken.findEnableAdnetworkToken(subscriberRegs.get(0).getTokenId());
					if(Objects.nonNull(adnetworkToken)) {
						cgToken = new CGToken(adnetworkToken.getTokenToCg());
					}
				}
			}else {
				logger.error("unknown action mt2uae notification");
			}
			if(Objects.nonNull(mt2ksaNotificationSdp.getAction())){
				VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
				if(Objects.nonNull(vwServiceCampaignDetail)) {
					mt2ksaServiceConfig = Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(vwServiceCampaignDetail.getServiceId());
				}
				if(Objects.isNull(mt2ksaServiceConfig)) {
					if("Zain".equalsIgnoreCase(mt2ksaNotificationSdp.getOperator())) {
						mt2ksaServiceConfig = Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(81);
					}else {
						mt2ksaServiceConfig = Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(82);
					}
				}
				Service service = MData.mapServiceIdToService.get(mt2ksaServiceConfig.getServiceId());
				liveReport = new LiveReport(service.getOpId(), new Timestamp(System.currentTimeMillis()),
						cgToken.getCampaignId(),mt2ksaServiceConfig.getServiceId(),
						20); 
				liveReport.setTokenId(cgToken.getTokenId());
				liveReport.setToken(cgToken.getCGToken());
				liveReport.setMsisdn(mt2ksaNotificationSdp.getMsisdn());
				liveReport.setCircleId(0);
				liveReport.setParam1(mt2ksaNotificationSdp.getReferenceId());
				//liveReport.setMode(MT2UAENotificationMode.getMode(mt2UAENotification.getChannel()));
				liveReport.setMode("");
				if(mt2ksaNotificationSdp.getAction().equals(MConstants.DCT)) {
					liveReport.setAction(MConstants.DCT);			
					liveReport.setDctCount(1);
				}
			}
		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}finally {
			try {		
				liveReport.setAction(mt2ksaNotificationSdp.getAction());			
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2KSANotificationSdp:: "
						+ mt2ksaNotificationSdp);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2ksaNotificationSdp);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}