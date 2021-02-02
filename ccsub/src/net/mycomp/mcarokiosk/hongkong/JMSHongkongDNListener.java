package net.mycomp.mcarokiosk.hongkong;

import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.jpa.repository.JPAMKHongkongMTMessage;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.util.MConstants;

public class JMSHongkongDNListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(JMSHongkongDNListener.class);


	@Autowired
	private MacroKioskHongkongFactoryService macroKioskHongkongFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPAMKHongkongMTMessage jpaMKHongkongMTMessage;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	

	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Override
	public void onMessage(Message m) {
		
		long time = System.currentTimeMillis();
		logger.info("onMessage::::::::::::::::: " + m);
		ObjectMessage objectMessage = (ObjectMessage) m;
		boolean update = false;		
		HongkongDeliveryNotification hongkongDeliveryNotification=null;
		LiveReport liveReport=null;
		try {		
			hongkongDeliveryNotification=(HongkongDeliveryNotification)objectMessage.getObject();
		    logger.info("onMessage::deliveryNotification:  "+hongkongDeliveryNotification);
		    		
		    HongkongMTMessage hongkongMTMessage=null;
		    List<HongkongMTMessage> list=jpaMKHongkongMTMessage
		    		.findMTMessageByMessageId(hongkongDeliveryNotification.getMtid());
		    
		    logger.info("list:: HongkongMTMessage : "+list);
		    if(list!=null&&list.size()>0){
		    	hongkongMTMessage=list.get(0);
		    }
		 
		    Integer cmapignId=MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID;
		    
		    if(hongkongMTMessage!=null){
		    	hongkongDeliveryNotification.setNotificationType(hongkongMTMessage.getMessageType());
		    	hongkongDeliveryNotification.setKeyword(hongkongMTMessage.getKeyword());		    	
		    	hongkongDeliveryNotification.setTokenId(hongkongMTMessage.getTokenId());	
		    	hongkongDeliveryNotification.setToken(hongkongMTMessage.getToken());
		    	cmapignId=hongkongMTMessage.getCampaignId();
		    }
		    
		    macroKioskHongkongFactoryService.processDeliveryNotification(hongkongDeliveryNotification);
			  
		    
		    MKHongkongConfig mkHongkongConfig=MKHongkongConstant
		    		.mapServiceIdToMKHongkongConfig.get(hongkongMTMessage.getServiceId());
			 liveReport=new LiveReport(hongkongDeliveryNotification.getOpId()
		    		,hongkongDeliveryNotification.getCreateTime(),
	    			cmapignId,mkHongkongConfig.getServiceId(),mkHongkongConfig.getProductId()
						 );
		    liveReport.setMsisdn(hongkongDeliveryNotification.getMsisdn());
		    
	    	
	    if(hongkongMTMessage!=null&&hongkongMTMessage.getMessageType()!=null
	    		&&hongkongMTMessage.getMessageType().equalsIgnoreCase(MKHongkongConstant.MT_BIILABLE_MESSAGE)){
	    	
	    	liveReport.setTokenId(hongkongDeliveryNotification.getTokenId());
	    	liveReport.setToken(hongkongDeliveryNotification.getToken());
	    	
	    	SubscriberReg subscriberReg=jpaSubscriberReg
	    			.findSubscriberRegByMsisdnAndProductId(hongkongDeliveryNotification.getMsisdn()
					, mkHongkongConfig.getProductId());
	    	
	    	if(subscriberReg!=null){
	    		liveReport.setMode(subscriberReg.getMode());
	    	}
	    		
		     if(hongkongDeliveryNotification.isCharged()){	
		    	
		    if(hongkongMTMessage.getMtActionType().equalsIgnoreCase(MConstants.ACT)){
		    	
			    	liveReport.setAction(MConstants.ACT);	    	
			    	logger.info("onMessage::mkHongkongConfig:  "+mkHongkongConfig);
			    	liveReport.setAmount(mkHongkongConfig.getPrice());
			    	liveReport.setConversionCount(0);//Receiived MO define Activation			    	
			    	liveReport.setNoOfDays(mkHongkongConfig.getValidityForCharge());
			    	hongkongDeliveryNotification.setAmount(mkHongkongConfig.getPrice());			    	
			    }else if(hongkongMTMessage.getMtActionType().equalsIgnoreCase(MConstants.RENEW)){
			    	
			    	liveReport.setAction(MConstants.RENEW);	    	
			    	logger.info("onMessage::mkHongkongConfig:  "+mkHongkongConfig);
			    	liveReport.setRenewalAmount(mkHongkongConfig.getPrice());
			    	liveReport.setRenewalCount(1);
			    	liveReport.setNoOfDays(mkHongkongConfig.getValidityForCharge());
			    	hongkongDeliveryNotification.setAmount(mkHongkongConfig.getPrice());
			    }		    		    	
		     }		     
  
		     }
			logger.info("update:: "+hongkongDeliveryNotification+", update:: "+update);
			hongkongDeliveryNotification.setProcessStatus(true);
		} catch (Exception e) {
			logger.error("onMessage:::::::::::::::::"  + " , Exception  " , e);
			hongkongDeliveryNotification.setProcessStatus(false);
		}finally{
			try{
				
				hongkongDeliveryNotification.setCcMode(liveReport.getMode());
				if(liveReport.getAction()!=null){
					hongkongDeliveryNotification.setAction(liveReport.getAction());
					liveReportFactoryService.process(liveReport);
				}
				
			}catch(Exception ex){
				logger.error("onMessage:::::::::::::::::"  + " , Exception  " , ex);
			}finally{
			    update=daoService.saveObject(hongkongDeliveryNotification);	
			}
		}	
		
		logger.info("onMessage::::::::::::::::: :: update:: " + update+ ", total time:: " + (System.currentTimeMillis() - time));
	}	
}
