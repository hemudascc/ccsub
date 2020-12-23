package net.mycomp.mcarokiosk.malaysia;

import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPAMKMalaysiaMTMessage;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.process.bean.SuscriberIdMsg;


import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMalaysiaDNListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(JMSMalaysiaDNListener.class);


	@Autowired
	private MacroKioskMalaysiaFactoryService macroKioskMalaysiaFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private JPAMKMalaysiaMTMessage jpaMKMalaysiaMTMessage;
	
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
		MalaysiaDeliveryNotification malaysiaDeliveryNotification=null;
		LiveReport liveReport=null;
		try {		
			malaysiaDeliveryNotification=(MalaysiaDeliveryNotification)objectMessage.getObject();
		    logger.info("onMessage::deliveryNotification:  "+malaysiaDeliveryNotification);
		    		
		    MalasiyaMTMessage malasiyaMTMessage=null;
		    List<MalasiyaMTMessage> list=jpaMKMalaysiaMTMessage
		    		.findMTMessageByMessageId(malaysiaDeliveryNotification.getMtid());
		    
		    logger.info("list:: MalasiyaMTMessage : "+list);
		    if(list!=null&&list.size()>0){
		    	malasiyaMTMessage=list.get(0);
		    }
		 
		    Integer cmapignId=MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID;
		   // Integer moId=0;
		    
		    if(malasiyaMTMessage!=null){
		        malaysiaDeliveryNotification.setNotificationType(malasiyaMTMessage.getMessageType());
		    	malaysiaDeliveryNotification.setKeyword(malasiyaMTMessage.getKeyword());		    	
		    	malaysiaDeliveryNotification.setTokenId(malasiyaMTMessage.getTokenId());	
		    	malaysiaDeliveryNotification.setToken(malasiyaMTMessage.getToken());
		    	cmapignId=malasiyaMTMessage.getCampaignId();
		    //	moId=malasiyaMTMessage.getMoMessageId();
		    }
		    
		    macroKioskMalaysiaFactoryService.processDeliveryNotification(malaysiaDeliveryNotification);
			  
		    
		    MKMalaysiaConfig mkMalaysiaConfig=MKMalaysiaConstant
		    		.mapServiceIdToMKMalaysiaConfig.get(malasiyaMTMessage.getServiceId());
			//LiveReport(int operatorId, Timestamp timestamp, Integer adnetworkCampaignId,int serviceId)
		    liveReport=new LiveReport(malaysiaDeliveryNotification.getOpId()
		    		,malaysiaDeliveryNotification.getCreateTime(),
	    			cmapignId,mkMalaysiaConfig.getServiceId(),mkMalaysiaConfig.getProductId()
						 );
		    liveReport.setMsisdn(malaysiaDeliveryNotification.getMsisdn());
		    
	    	
	    if(malasiyaMTMessage!=null&&malasiyaMTMessage.getMessageType()!=null
	    		&&malasiyaMTMessage.getMessageType().equalsIgnoreCase(MKMalaysiaConstant.MT_BIILABLE_MESSAGE)){
	    	
	    	liveReport.setTokenId(malaysiaDeliveryNotification.getTokenId());
	    	liveReport.setToken(malaysiaDeliveryNotification.getToken());
	    	
	    	SubscriberReg subscriberReg=jpaSubscriberReg
	    			.findSubscriberRegByMsisdnAndProductId(malaysiaDeliveryNotification.getMsisdn()
					, mkMalaysiaConfig.getProductId());
	    	
	    	if(subscriberReg!=null){
	    		liveReport.setMode(subscriberReg.getMode());
	    	}
	    		
		     if(malaysiaDeliveryNotification.isCharged()){	
		    	
		    if(malasiyaMTMessage.getMtActionType().equalsIgnoreCase(MConstants.ACT)){
		    	
			    	liveReport.setAction(MConstants.ACT);	    	
			    	logger.info("onMessage::mkMalaysiaConfig:  "+mkMalaysiaConfig);
			    	liveReport.setAmount(mkMalaysiaConfig.getPrice());
			    	liveReport.setConversionCount(0);//Receiived MO define Activation			    	
			    	liveReport.setNoOfDays(mkMalaysiaConfig.getValidityForCharge());
			    	malaysiaDeliveryNotification.setAmount(mkMalaysiaConfig.getPrice());			    	
			    }else if(malasiyaMTMessage.getMtActionType().equalsIgnoreCase(MConstants.RENEW)){
			    	
			    	liveReport.setAction(MConstants.RENEW);	    	
			    	logger.info("onMessage::mkMalaysiaConfig:  "+mkMalaysiaConfig);
			    	liveReport.setRenewalAmount(mkMalaysiaConfig.getPrice());
			    	liveReport.setRenewalCount(1);
			    	liveReport.setNoOfDays(mkMalaysiaConfig.getValidityForCharge());
			    	malaysiaDeliveryNotification.setAmount(mkMalaysiaConfig.getPrice());
			    }		    		    	
		     }		     
  
		     }
			logger.info("update:: "+malaysiaDeliveryNotification+", update:: "+update);
			malaysiaDeliveryNotification.setProcessStatus(true);
		} catch (Exception e) {
			logger.error("onMessage:::::::::::::::::"  + " , Exception  " , e);
			malaysiaDeliveryNotification.setProcessStatus(false);
		}finally{
			try{
				
				malaysiaDeliveryNotification.setCcMode(liveReport.getMode());
				if(liveReport.getAction()!=null){
					malaysiaDeliveryNotification.setAction(liveReport.getAction());
					liveReportFactoryService.process(liveReport);
				}
				
			}catch(Exception ex){
				logger.error("onMessage:::::::::::::::::"  + " , Exception  " , ex);
			}finally{
			    update=daoService.saveObject(malaysiaDeliveryNotification);	
			}
		}	
		
		logger.info("onMessage::::::::::::::::: :: update:: " + update+ ", total time:: " + (System.currentTimeMillis() - time));
	}	
}



