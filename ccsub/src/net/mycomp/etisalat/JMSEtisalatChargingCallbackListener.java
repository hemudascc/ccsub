package net.mycomp.etisalat;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.persist.bean.LiveReport;
import net.process.bean.CGToken;
import net.util.HTTPResponse;
import net.util.MConstants;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSEtisalatChargingCallbackListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSEtisalatChargingCallbackListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private EtisalatSmsService etisalatSmsService;

	
  
	@Override
	public void onMessage(Message m) {

		EtisalatChargingCallback etisalatChargingCallback = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			etisalatChargingCallback = (EtisalatChargingCallback) objectMessage
					.getObject();
			
			logger.debug("etisalatChargingCallback::::::: "+etisalatChargingCallback);
			
			EtisalatServiceConfig etisalatServiceConfig=
					EtisalatConstant.mapPackageIdToEtisalatServiceConfig.get(
							etisalatChargingCallback.getPackageId());
//			liveReport = new LiveReport(MConstants.ETISALAT_OPERATOR_ID,
//					new Timestamp(System.currentTimeMillis()), null,etisalatServiceConfig.getServiceId());
			
			CGToken cgToken=new CGToken(etisalatChargingCallback.getTransactionId2()); 
			liveReport=new LiveReport(MConstants.ETISALAT_OPERATOR_ID,new Timestamp(System.currentTimeMillis())
					 ,cgToken.getCampaignId(),etisalatServiceConfig.getServiceId(),0); 
			
			
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(etisalatChargingCallback.getMsisdn());
			liveReport.setCircleId(0);
				   
			logger.debug("etisalatServiceConfig::::::: "+etisalatServiceConfig);
			//liveReport.setServiceId(etisalatServiceConfig.getServiceId());

			Long counter = redisCacheService.getAndIcrementIntValue(
					EtisalatChargingCallback.class.getName()
							+ etisalatChargingCallback.getTransactionId1(),0);
			if (counter != null && counter > 1) {
				logger.debug("onMessage:::::::::::::::::"
						+ " duplicate callback");
				etisalatChargingCallback.setDuplicate(true);
				liveReport.setDuplicateRequest(true);
			}
 
			logger.debug("cgToken::::::: "+cgToken);
			if (etisalatChargingCallback.getTransactionType().equalsIgnoreCase(EtisalatConstant.SUB)) {

				liveReport.setAction(MConstants.ACT);
				liveReport.setConversionCount(1);
				liveReport.setAmount(EtisalatConstant.convertToAED(
						MUtility.toDouble(etisalatChargingCallback.getAmount(),0)));
				liveReport.setNoOfDays(1);
				String portalUrl=etisalatServiceConfig.getPortalUrl()+"&msisdn="+etisalatChargingCallback.getMsisdn();
				logger.debug("portalUrl::::::: "+portalUrl);
				String msg=etisalatServiceConfig.getSubMessageTemplate().
						replaceAll("<portalurl>",portalUrl)
						.replaceAll("<servicename>", etisalatServiceConfig.getPackageName());
				logger.debug("message::::::: "+msg);
				
				etisalatSmsService.sendSubscriptionMTPush(etisalatChargingCallback,
						etisalatChargingCallback.getMsisdn(),
						etisalatServiceConfig.getSenderId(), msg);
				

			} else if (etisalatChargingCallback.getTransactionType().equalsIgnoreCase(EtisalatConstant.UNSUB)) {
				liveReport.setAction(MConstants.DCT);
				liveReport.setDctCount(1);
				logger.debug("DCT::::::: ");
//				String msg=etisalatServiceConfig.getUnsubMessageTemplate().
//						replaceAll("<portalurl>",etisalatServiceConfig.getPortalUrl())
//						.replaceAll("<servicename>", etisalatServiceConfig.getPackageName());
//				logger.debug("message::::::: "+msg);
//				etisalatSmsService.sendSubscriptionMTPush(etisalatChargingCallback.getMsisdn(),
//						etisalatServiceConfig.getSenderId(), msg);
				
			} else if (etisalatChargingCallback.getTransactionType().
					equalsIgnoreCase(EtisalatConstant.REN)) {
				logger.debug("renwal::::::: ");
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalCount(1);
				liveReport.setRenewalAmount(EtisalatConstant.convertToAED(
						MUtility.toDouble(etisalatChargingCallback.getAmount(),0)));
				liveReport.setNoOfDays(etisalatServiceConfig.getDuration());
				
			}
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				if (liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);
				}
				
				etisalatChargingCallback.setAction(liveReport.getAction());
				etisalatChargingCallback.setActKey(liveReport.getAction());
				etisalatChargingCallback.setCampaignId(liveReport.getAdnetworkCampaignId());
				etisalatChargingCallback.setSendToAdnetwork(liveReport.getSendConversionCount()>0?true:false);
				etisalatChargingCallback.setToken(liveReport.getToken());
				etisalatChargingCallback.setTokenId(liveReport.getTokenId());
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : etisalatChargingCallback:: "
						+ etisalatChargingCallback);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(etisalatChargingCallback);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
