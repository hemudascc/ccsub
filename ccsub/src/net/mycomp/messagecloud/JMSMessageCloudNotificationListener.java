package net.mycomp.messagecloud;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.AdnetworkToken;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMessageCloudNotificationListener implements MessageListener{

	private static final Logger logger = Logger.getLogger(JMSMessageCloudNotificationListener.class);

	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	
	@Override
	public void onMessage(Message m) {
		
		MessagecloudNotification messagecloudNotification=null;
		LiveReport liveReport=null;
		String action=null;
		try{
		ObjectMessage objectMessage = (ObjectMessage) m;
		messagecloudNotification=(MessagecloudNotification)objectMessage.getObject();
		
		  CGToken cgToken=new CGToken(messagecloudNotification.getToken());
		  
		  VWServiceCampaignDetail vwServiceCampaignDetail=MData
		    		.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		  
		    MessageCloudServiceConfig messageCloudServiceConfig=
		    		MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.
		    		get(vwServiceCampaignDetail.getServiceId());
		    
		    
		     liveReport=new LiveReport(messageCloudServiceConfig.getOperatorId(),
		    		 new Timestamp(System.currentTimeMillis())
			 ,cgToken.getCampaignId(),messageCloudServiceConfig.getServiceId(),
			 messageCloudServiceConfig.getProductId()); 
		     
		    liveReport.setMsisdn(messagecloudNotification.getMsisdn());
		    liveReport.setProductId(messageCloudServiceConfig.getProductId());
		    liveReport.setToken(cgToken.getCGToken());
		    liveReport.setTokenId(cgToken.getTokenId());
		    liveReport.setParam1(cgToken.getCGToken());
		    
		    /////////////////Patch
		    SubscriberReg subscriberReg=null;
		    if(cgToken.getCGToken()!=null){
		    List<SubscriberReg> list=jpaSubscriberReg.findSubscriberRegByParam1(
		    		cgToken.getCGToken());
		    
		    
		    if(list!=null&&list.size()>0){
		    	subscriberReg=list.get(0);
		    	if(liveReport.getMsisdn()==null){
		    		liveReport.setMsisdn(subscriberReg.getMsisdn());
		    	}
		     }
              if(subscriberReg==null){
            	  subscriberReg=jpaSubscriberReg.
            			  findSubscriberRegByMsisdnAndProductId(messagecloudNotification.getMsisdn(),
            					  messageCloudServiceConfig.getProductId());
		     }	     
		    
		    }
		    liveReport.setResponse(messagecloudNotification.toString());
		    //////////////////////End Patch
		    action= MessageCloudConstant.getAction(messagecloudNotification,subscriberReg);
		    messagecloudNotification.setAction(action);
		    
		    if(action.equalsIgnoreCase(MConstants.ACT)){
			  
			  liveReport.setAction(MConstants.ACT);	
			  liveReport.setConversionCount(1);
			  liveReport.setNoOfDays(messageCloudServiceConfig.getSubPeriod());
			  liveReport.setAmount(messageCloudServiceConfig.getValue());	    
			    
		}else if(action.equalsIgnoreCase(MConstants.DCT)){
			liveReport.setDctCount(1);
			liveReport.setAction(MConstants.DCT);
		}else if(action.equalsIgnoreCase(MConstants.RENEW)){
			liveReport.setRenewalCount(1);
			liveReport.setRenewalAmount(messageCloudServiceConfig.getValue());
			liveReport.setNoOfDays(messageCloudServiceConfig.getSubPeriod());
			liveReport.setAction(MConstants.RENEW);
		}
		
		}catch(Exception ex){
			
			logger.error("onMessage::::: ",ex);
			
		}finally{		
			
			try {
				
				liveReportFactoryService.process(liveReport);
				messagecloudNotification.setSendToAdnetwork(
						liveReport.getSendConversionCount() > 0 ? true : false);
			} catch (Exception e) {	
				logger.error("finally::: ",e);
			}
			daoService.saveObject(messagecloudNotification);
		  }
		}
	}

