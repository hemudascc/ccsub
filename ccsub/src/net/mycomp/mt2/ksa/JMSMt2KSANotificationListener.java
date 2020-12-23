package net.mycomp.mt2.ksa;

import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMt2KSANotificationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMt2KSANotificationListener.class);

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Override
	public void onMessage(Message m) {

		Mt2KSANotification mt2KSANotification = null;
		
		boolean update = false;
		long time = System.currentTimeMillis();
		LiveReport liveReport=null;
		
		Mt2KSAServiceConfig mt2KSAServiceConfig=null;
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mt2KSANotification = (Mt2KSANotification) objectMessage
					.getObject();
			
			Mt2KSAConstant.parseNotificationData(mt2KSANotification);
			CGToken cgToken=new CGToken(Objects.toString(redisCacheService.getObjectCacheValue(Mt2KSAConstant
					   .MT2_KSA_MSISDN_TOKEN_CACHE_PREFIX+mt2KSANotification.getMo())));
			mt2KSANotification.setToken(cgToken.getCGToken());
			 mt2KSAServiceConfig=
						Mt2KSAConstant.mapMt2ServiceIdToMt2KSAServiceConfig.get(
								mt2KSANotification.getMt2ServiceId());
				Service service=MData.mapServiceIdToService.get(mt2KSAServiceConfig.getServiceId());
			 liveReport=new LiveReport(service.getOpId(),
						   new Timestamp(System.currentTimeMillis())
					 ,cgToken.getCampaignId(),mt2KSAServiceConfig.getServiceId(),service.getProductId()
					 ); 
				
					liveReport.setMsisdn(mt2KSANotification.getMo());
					liveReport.setCircleId(0);
					liveReport.setToken(cgToken.getCGToken());
					liveReport.setTokenId(cgToken.getTokenId());
					
					if(mt2KSANotification.getMt2Action().equalsIgnoreCase("S")){				
						 liveReport.setAction(MConstants.ACT);				
						 //Zain KSA Operator conversion increase by JMSMt2KSAPinValidationListener
						 liveReport.setConversionCount(
								 MConstants.MT2_KSA_ZAIN_OPERATOR_ID==liveReport.getOperatorId()?0:1);
						 liveReport.setAmount(mt2KSAServiceConfig.getPricePoint());
						 liveReport.setNoOfDays(mt2KSAServiceConfig.getValidity());	
						 
					}else if(mt2KSANotification.getMt2Action().equalsIgnoreCase("R")){				
						 liveReport.setAction(MConstants.RENEW);				
						 liveReport.setRenewalAmount(mt2KSAServiceConfig.getPricePoint());
						 liveReport.setRenewalCount(1);
						 liveReport.setNoOfDays(mt2KSAServiceConfig.getValidity());						
					}else if(mt2KSANotification.getMt2Action().equalsIgnoreCase("U")){				
						 liveReport.setAction(MConstants.DCT);				
						 liveReport.setDctCount(1);				
					}
			
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
			try {
				mt2KSANotification.setMyAction(liveReport.getAction());
				
				if(liveReport.getAction()!=null){
					liveReportFactoryService.process(liveReport);
				}
			} catch (Exception ex) {			
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				daoService.saveObject(mt2KSANotification);
			}
			logger.info("onMessage::::::::::::::::: ::  "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
			}
	}
}
