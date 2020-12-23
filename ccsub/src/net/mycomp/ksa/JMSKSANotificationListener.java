package net.mycomp.ksa;

import java.sql.Timestamp;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSKSANotificationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSKSANotificationListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private SubscriberRegService subscriberRegService; 

	
	@Autowired
	private KsaServiceApi ksaServiceApi;
	
	
	@Override
	public void onMessage(Message m) {

		KsaNotification ksaNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			ksaNotification = (KsaNotification) objectMessage
					.getObject();			
			logger.info("ksaNotification::::: "+ksaNotification);		
			KsaServiceConfig ksaServiceConfig=KsaConstant.mapKsaServiceIdToKsaServiceConfig.get(ksaNotification.getMaServiceId());
			Service service=MData.mapServiceIdToService.get(ksaServiceConfig.getServiceId());
			String action=KSAServiceEnum.getMyAction(ksaNotification.getOperationId());
		     liveReport=new LiveReport(service.getOpId(), ksaNotification.getCreateTime()
		    		, -1,
		    		ksaServiceConfig.getServiceId(), service.getProductId());
		    liveReport.setMsisdn(ksaNotification.getCallingParty());
		    liveReport.setMode(KSANotificationMode.getMode(ksaNotification.getBearerId()));
		    
			if(action.equalsIgnoreCase(MConstants.ACT)){
				liveReport.setConversionCount(1);
				liveReport.setAction(MConstants.ACT);
				liveReport.setAmount(MUtility.toDouble(ksaNotification.getPrice(),0));
				liveReport.setNoOfDays(ksaServiceConfig.getValidity());
				ksaServiceApi.sendSms(ksaNotification.getCallingParty(),
						ksaServiceConfig, ksaServiceConfig.getSubscriptionMsgTemplate()
						,KsaConstant.NOTIFICATION_SMS_PUSH);
				
		  }else if(action.equalsIgnoreCase(MConstants.DCT)){
				liveReport.setAction(MConstants.DCT);
				liveReport.setDctCount(1);
			}else if(action.equalsIgnoreCase(MConstants.RENEW)){
				liveReport.setAction(MConstants.RENEW);
				liveReport.setRenewalAmount(MUtility.toDouble(ksaNotification.getPrice(),0));
				liveReport.setNoOfDays(ksaServiceConfig.getValidity());				
			}
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
		
			try {
				
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
				
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : ksaNotification:: "
						+ ksaNotification);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(ksaNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
