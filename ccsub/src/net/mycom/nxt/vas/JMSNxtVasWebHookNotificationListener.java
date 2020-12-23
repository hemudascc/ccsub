package net.mycom.nxt.vas;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.persist.bean.AdnetworkToken;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSNxtVasWebHookNotificationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSNxtVasWebHookNotificationListener.class);

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private IDaoService daoService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private NxtVasServiceApi nxtVasServiceApi;
	
	@Override
	public void onMessage(Message m) {

		NxtWebhookNotification nxtWebhookNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			nxtWebhookNotification = (NxtWebhookNotification) objectMessage
					.getObject();
			
			NxtVasConfig nxtVasConfig=NxtVasConstant.mapMccMncToNxtVasConfig
					.get(nxtWebhookNotification.getMcc()+nxtWebhookNotification.getMnc());
			
			liveReport = new LiveReport(nxtVasConfig.getOperatorId(),
					new Timestamp(System.currentTimeMillis()), null,nxtVasConfig.getServiceId(),0
					);
			
			
			
//			Long counter = redisCacheService.getAndIcementIntValue(
//					NxtWebhookNotification.class.getName()
//							+ nxtWebhookNotification.getTransactonId(),1);
			
//			if (counter != null && counter > 1) {
//				logger.debug("onMessage:::::::::::::::::"
//						+ " duplicate callback");
//				nxtWebhookNotification.setDuplicate(true);
//				liveReport.setDuplicateRequest(true);
//			}
			
			AdnetworkToken adnetworkToken=daoService.findAdnetworkTokenById(
					MUtility.toInt(nxtWebhookNotification.getTransactonId(),0));
			String token="";
			if(adnetworkToken!=null){
				token=adnetworkToken.getTokenToCg();
			}
			
			CGToken cgToken=new CGToken(token); 
			
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(nxtWebhookNotification.getMsisdn());
			liveReport.setCircleId(0);
			//liveReport.setType(""+nxtWebhookNotification.getServiceId());
			liveReport.setParam1(nxtWebhookNotification.getSubscriberId());
			nxtWebhookNotification.setToken(cgToken.getCGToken());
			NxtVasSubscriberStatusEnum nxtVasSubscriberStatusEnum=NxtVasSubscriberStatusEnum
					.getNxtVasSubscriberStatusEnum(MUtility.toInt(
					nxtWebhookNotification.getSubscriberStatus(),0));
			if (nxtWebhookNotification.getAction().equals(NxtVasConstant.ACTIVE)) {

				liveReport.setAction(MConstants.ACT);
				liveReport.setConversionCount(1);
				liveReport.setAmount(MUtility.toDouble(nxtWebhookNotification.getAmountCharge(),0));
				liveReport.setNoOfDays(nxtVasConfig.getValidity());
				liveReport.setParam1(nxtWebhookNotification.getSubscriberId());

			}else if (nxtWebhookNotification.getAction().equals(NxtVasConstant.BILL)
					&&nxtVasSubscriberStatusEnum.status==
					NxtVasSubscriberStatusEnum.ALREADY_EXIST_SUBSCRIBER.status) {
			
				//nxtVasServiceApi.sendFreeSms(nxtVasConfig, nxtWebhookNotification.getMsisdn(), 
				//		nxtWebhookNotification.getSubscriberId(),nxtVasSubscriberStatusEnum);
				
			 	liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);
				liveReport.setRenewalAmount(MUtility.toDouble(nxtWebhookNotification.getAmountCharge(),0));
				liveReport.setNoOfDays(nxtVasConfig.getValidity());
				
			}else if (nxtWebhookNotification.getAction().equals(NxtVasConstant.BILL)) {
				nxtVasServiceApi.sendFreeSms(nxtVasConfig, nxtWebhookNotification.getMsisdn(), 
						nxtWebhookNotification.getSubscriberId(),NxtVasSubscriberStatusEnum.NEW_SUBSCRIBER);
				
			//	liveReport.setAction(MConstants.RENEW);
			//	liveReport.setRenewalCount(1);
			//	liveReport.setRenewalAmount(MUtility.toDouble(nxtWebhookNotification.getAmountCharge(),0));
			//	liveReport.setNoOfDays(nxtVasConfig.getValidity());
				
			}else if (nxtWebhookNotification.getAction().equals(NxtVasConstant.CANCELED)) {
				liveReport.setAction(MConstants.DCT);
				liveReport.setDctCount(1);
			} 
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				if (liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);
				}
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : nxtWebhookNotification:: "
						+ nxtWebhookNotification);
				logger.error("onMessage::::::::::finally " , ex);
			} finally {
				update = daoService.saveObject(nxtWebhookNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
