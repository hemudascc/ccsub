package net.mycomp.mt2.uae;

import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

public class JMSMt2UAEDLRSdpListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2UAEDLRSdpListener.class);

	@Autowired
	private IDaoService daoService;

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;

	@Override
	public void onMessage(Message m) {

		Mt2UAEDeliveryNotificationSdp mt2uaeDeliveryNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		CGToken cgToken=null;
		Mt2UAEServiceConfig mt2UAEServiceConfig=null;
		try {
			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2uaeDeliveryNotification = (Mt2UAEDeliveryNotificationSdp) objectMessage.getObject();
			logger.info("mt2UAEDeliveryNotification::::: "+mt2uaeDeliveryNotification);
			cgToken = new CGToken(Objects.toString(redisCacheService.getObjectCacheValue(Mt2UAEConstant.MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX+mt2uaeDeliveryNotification.getMsisdn())));
			mt2uaeDeliveryNotification.setToken(cgToken.getCGToken());
			//mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(mt2uaeDeliveryNotification.getOperator().equals("Etisalat")?88:89);
			if(Objects.isNull(mt2UAEServiceConfig)) {
				if("Du".equalsIgnoreCase(mt2uaeDeliveryNotification.getOperator())) {
					mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(89);
				}else if("Etisalat".equalsIgnoreCase(mt2uaeDeliveryNotification.getOperator())){
					mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(88);
				}else {
					mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(90);
				}
			}
			
			Service service = MData.mapServiceIdToService.get(mt2UAEServiceConfig.getServiceId());
			liveReport = new LiveReport(service.getOpId(), new Timestamp(System.currentTimeMillis()), 
					cgToken.getCampaignId(),mt2UAEServiceConfig.getServiceId(),mt2UAEServiceConfig.getProductId()); 
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(mt2uaeDeliveryNotification.getMsisdn());
			liveReport.setCircleId(0);
			liveReport.setMode("");
			liveReport.setResponse(mt2uaeDeliveryNotification.toString());
			if(mt2uaeDeliveryNotification.getSdpStatus().equals("Success") && cgToken.getCampaignId()>0) {
				//act
				liveReport.setAction(MConstants.ACT);
				liveReport.setConversionCount(1);
				liveReport.setAmount(MUtility.toDouble(mt2uaeDeliveryNotification.getPrice().toString(), mt2UAEServiceConfig.getPricePoint()));
				liveReport.setNoOfDays(MUtility.toInt(mt2uaeDeliveryNotification.getValidity().toString(),0));
			}else if(mt2uaeDeliveryNotification.getSdpStatus().equals("Success") && cgToken.getCampaignId()<0){
				//renew
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);
				liveReport.setRenewalAmount(MUtility.toDouble(mt2uaeDeliveryNotification.getPrice().toString(), mt2UAEServiceConfig.getPricePoint()));
				liveReport.setNoOfDays(MUtility.toInt(mt2uaeDeliveryNotification.getValidity().toString(),0));
			}else {
				//grace
				liveReport.setAction(MConstants.GRACE);
				liveReport.setGraceConversionCount(1);
			}

		} catch (Exception e) {
			logger.error("onMessage::::: ", e);
		}
		finally {
			try {
				mt2uaeDeliveryNotification.setAction(liveReport.getAction());
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mt2uaeDeliveryNotification:: "
						+ mt2uaeDeliveryNotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mt2uaeDeliveryNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));	
		}	
	}
}
