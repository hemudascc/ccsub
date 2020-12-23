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
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSKSAPinValidationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSKSAPinValidationListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private SubscriberRegService subscriberRegService; 

	
	
	@Override
	public void onMessage(Message m) {

		KsaApiTrans ksaApiTrans = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			ksaApiTrans = (KsaApiTrans) objectMessage
					.getObject();
			
			logger.info("ksaApiTrans::::: "+ksaApiTrans);
			CGToken cgToken=new CGToken(ksaApiTrans.getToken());
			
			VWServiceCampaignDetail vwServiceCampaignDetail
			=MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			
			KsaServiceConfig ksaServiceConfig=KsaConstant.mapServiceIdToKsaServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			
		   liveReport=new LiveReport(vwServiceCampaignDetail.getOpId(),
				   new Timestamp(System.currentTimeMillis())
			 ,cgToken.getCampaignId(),vwServiceCampaignDetail.getServiceId(),vwServiceCampaignDetail.getProductId()); 
	
		    
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			liveReport.setMsisdn(ksaApiTrans.getMsisdn());
			liveReport.setCircleId(0);
			
			if(ksaApiTrans.getResponseToCaller()
					&&ksaApiTrans.getRequestType().equalsIgnoreCase(KsaConstant.VALIDATE_PIN)){	
				
				 liveReport.setAction(MConstants.ACT);				
				 liveReport.setConversionCount(0);
				 liveReport.setAmount(0d);
				 liveReport.setNoOfDays(1);		
				 liveReport.setPinValidationCount(1);
				 
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
						+ ", : ksaApiTrans:: "
						+ ksaApiTrans);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(ksaApiTrans);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
