package net.mycomp.messagecloud.gateway.za;

import java.sql.Timestamp;
import java.util.Objects;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAIntargetUssdTrans;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMCGZAMOMessageListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMCGZAMOMessageListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private MCGZAApiService mcgZAApiService;
	
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private SubscriberRegService subscriberRegService; 

	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private JPAIntargetUssdTrans jpaIntargetUssdTrans;
	
  
	@Override
	public void onMessage(Message m) {

		MCGZAMoMessage mcgZAMoMessage = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			mcgZAMoMessage = (MCGZAMoMessage) objectMessage
					.getObject();
			logger.debug("mcgZAMoMessage::::::: "+mcgZAMoMessage);
			String msg=mcgZAMoMessage.getMessage();
			String action=MCGZAConstant.findAction(msg);
			MCGZAServiceConfig mcgZAServiceConfig=MCGZAConstant.mapServiceIdToMCGZAServiceConfig.
					get(MCGZAConstant.SERVICE_ID);
			   
			Service service=MData.mapServiceIdToService.get(mcgZAServiceConfig.getServiceId());
			
			liveReport=new LiveReport(MConstants.MESSAGE_CLOUD_GATWAY_ZA_OPERATOR,
					new Timestamp(System.currentTimeMillis())
			 ,null,mcgZAServiceConfig.getServiceId(),0);
			
			liveReport.setMsisdn(mcgZAMoMessage.getNumber());
			liveReport.setType(""+mcgZAServiceConfig.getServiceId());
			
			if(action.equalsIgnoreCase(MConstants.ACT)){
				
				SubscriberReg subscriberReg= jpaSubscriberReg.
						findSubscriberRegByMsisdnAndProductId(mcgZAMoMessage.getNumber(), 
						service.getProductId());
				if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
					String mtMsg=mcgZAServiceConfig.getAlreadySubscribedMessage();
					mcgZAApiService.sendContentMessage(mcgZAMoMessage.getNumber(), mcgZAServiceConfig
							, mtMsg);
				}else{
					
					String mtMsg=mcgZAServiceConfig.getBillingMessage();	
					logger.info("yet Billing message not set ");
//					mcgZAApiService.sendBilledMessage(mcgZAMoMessage.getMessageId(),
//							mcgZAMoMessage.getNumber()
//							, mcgZAServiceConfig ,mtMsg);
				}
			}else if(action.equalsIgnoreCase(MConstants.DCT)){
				
				String mtMsg=mcgZAServiceConfig.getUnsubMessage();
				liveReport.setAction(MConstants.DCT);
				liveReport.setDctCount(1);
				mcgZAApiService.sendContentMessage(mcgZAMoMessage.getNumber(), 
						mcgZAServiceConfig, mtMsg);
				
			}else if(action.equalsIgnoreCase(MCGZAConstant.INVALID_KEY)){
				
				String mtMsg=mcgZAServiceConfig.getInvalidmessage();
				mcgZAApiService.sendContentMessage(mcgZAMoMessage.getNumber(),
						mcgZAServiceConfig,
						mtMsg);
			}
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
			try{
				if (liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);
				}
				
			}catch(Exception e){
				logger.error("onMessage::::: second:: ", e);
			}
			
		} finally {
			try {
			} catch (Exception ex) {
				logger.error(" fianlly liveReport:: " + liveReport
						+ ", : mcgMoMessage:: "
						+ mcgZAMoMessage);
				logger.error("onMessage::::::::::finally " ,ex);
			} finally {
				update = daoService.saveObject(mcgZAMoMessage);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
