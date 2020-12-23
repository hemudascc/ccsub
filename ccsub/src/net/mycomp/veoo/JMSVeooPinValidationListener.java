package net.mycomp.veoo;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.CGToken;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSVeooPinValidationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSVeooPinValidationListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private VeooApiService veooApiService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
  
	@Override
	public void onMessage(Message m) {

		VeooPinValidation veooPinValidation = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		VeooServiceConfig veooServiceConfig=null;
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			veooPinValidation = (VeooPinValidation) objectMessage
					.getObject();
			
			logger.debug("veooPinValidation::::::: "+veooPinValidation);
			CGToken  cgToken=new CGToken(veooPinValidation.getToken());
			
			 veooServiceConfig=
					VeooConstant.mapServiceIdToVeooServiceConfig.get(veooPinValidation.getServiceId());
			 
			 liveReport=new LiveReport(veooServiceConfig.getOpId(),veooPinValidation.getCreateTime(),
					 cgToken.getCampaignId(),veooServiceConfig.getServiceId(),
					 veooServiceConfig.getCcProductId());
			 
			 liveReport.setMsisdn(veooPinValidation.getMsisdn());
			 
			 if(veooPinValidation.getPinValidate()){
				 liveReport.setAction(MConstants.GRACE);
				 liveReport.setGraceConversionCount(1);
				 liveReport.setNoOfDays(veooServiceConfig.getValidity());
			 }
			 
//		 if(veooPinValidation.getPinValidate()){
//				 
//				 SubscriberReg subscriberReg=
//						 jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(veooPinValidation.getMsisdn(),
//								 veooServiceConfig.getCcProductId());
//		  if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
//					 veooApiService.sendFreeMTMessage(MConstants.ALREADY_SUBSCRIBED
//							 , veooServiceConfig,
//							 veooPinValidation.getMsisdn(),veooServiceConfig.getShortCode()
//							 ,veooServiceConfig.getAlreadySubscribedMessage());
//				 }else{
//					 
//			veooApiService.sendFreeMTMessage("FREE",veooServiceConfig, veooPinValidation.getMsisdn()
//					,veooServiceConfig.getShortCode());			
//			veooApiService.sendPremiumMTMessage(MConstants.ACT,
//					veooServiceConfig,veooPinValidation.getMsisdn());			
//			 liveReport.setMsisdn(veooPinValidation.getMsisdn());
//			 liveReport.setAction(MConstants.ACT);
//			 liveReport.setProductId(veooServiceConfig.getCcProductId());	
//			 liveReport.setNoOfDays(veooServiceConfig.getValidity());
//			 liveReport.setConversionCount(1);
//				 }
//		   }
			
		} catch (Exception ex) {
			logger.error("onMessage::::: "+veooPinValidation+", veooServiceConfig:: "+veooServiceConfig, ex);
		} finally {
			try {
				
				if (liveReport!=null&&liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);					
				}
				
			} catch (Exception ex) {
				logger.error(" fianlly " 
						+ ", : veooMo:: "
						+ veooPinValidation);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.updateObject(veooPinValidation);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
