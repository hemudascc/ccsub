package net.mycomp.mcarokiosk.malaysia;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;

import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMalaysiaMOMessageListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(JMSMalaysiaMOMessageListener.class);

	@Autowired
	private MacroKioskMalaysiaFactoryService macroKioskMalaysiaFactoryService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@Override
	public void onMessage(Message m) {
		
		long time = System.currentTimeMillis();
		logger.info("onMessage::::::::::::::::: " + m);
		ObjectMessage objectMessage = (ObjectMessage) m;
		boolean update = false;		
		MalasiyaMOMessage malasiyaMOMessage=null;
		LiveReport liveReport=null;
		MKMalaysiaConfig mkMalaysiaConfig=null;
		try {	
			malasiyaMOMessage=(MalasiyaMOMessage)objectMessage.getObject();
			logger.info("onMessage:::::::: "+malasiyaMOMessage);
		
			String refId=malasiyaMOMessage.getRefId();
			if(refId==null){
			refId=(String)redisCacheService.
			getObjectCacheValue(MKMalaysiaConstant.MO_MESSAGE_CAHCHE_PREFIX+malasiyaMOMessage.getMsisdn());
			malasiyaMOMessage.setRefId(refId);
			}
			logger.info("onMessage 222:::::::: "+malasiyaMOMessage);
			MKMalaysiaCGToken mkMalaysiaCGToken=new MKMalaysiaCGToken(refId);
			if(mkMalaysiaCGToken.getCampaignId()==MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID){
				mkMalaysiaCGToken=new MKMalaysiaCGToken(MKMalaysiaConstant.findToken(malasiyaMOMessage.getText()));
			}
			
			
			if(mkMalaysiaCGToken.getCampaignId()==MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID){
				mkMalaysiaConfig=MacroKioskMalaysiaFactoryService.
						findConfigByKeyWordAndTelcoId(malasiyaMOMessage.getKeyword().toUpperCase(),
						  malasiyaMOMessage.getTelcoid());
				
				liveReport=new LiveReport(malasiyaMOMessage.getOpId(),
						malasiyaMOMessage.getCreateTime(), 0,
						mkMalaysiaConfig.getServiceId(),0);
			}else{
				VWServiceCampaignDetail vwServiceCampaignDetail=MData.
						mapCamapignIdToVWServiceCampaignDetail.get(mkMalaysiaCGToken.getCampaignId());
				mkMalaysiaConfig=	MKMalaysiaConstant
						.mapServiceIdToMKMalaysiaConfig.get(vwServiceCampaignDetail.getServiceId());
				
				liveReport=new LiveReport(malasiyaMOMessage.getOpId(),
						malasiyaMOMessage.getCreateTime(), 0,
						mkMalaysiaConfig.getServiceId(),0);
			}
			
			logger.info("onMessage::333:::::: "+malasiyaMOMessage);
			liveReport.setAdnetworkCampaignId(mkMalaysiaCGToken.getCampaignId());
			liveReport.setToken(mkMalaysiaCGToken.getCGToken());
			liveReport.setTokenId(mkMalaysiaCGToken.getTokenId());
			liveReport.setProductId(mkMalaysiaConfig.getProductId());
			liveReport.setMsisdn(malasiyaMOMessage.getMsisdn());
			malasiyaMOMessage.setTokenId(liveReport.getTokenId());
			malasiyaMOMessage.setToken(liveReport.getToken());
	    	malasiyaMOMessage.setCampaignId(liveReport.getAdnetworkCampaignId());
	    	logger.info("onMessage::444:::::: "+malasiyaMOMessage);
	    	if(malasiyaMOMessage.getChannel()!=null){
	    		
	    	if(malasiyaMOMessage.getChannel().equalsIgnoreCase("0")){	    		
	    		liveReport.setMode("SMS");
	    	}else if(malasiyaMOMessage.getChannel().equalsIgnoreCase("1")){
	    		liveReport.setMode("IVR");
	    	}else if(malasiyaMOMessage.getChannel().equalsIgnoreCase("2")){
	    		liveReport.setMode("WAP");	
	    	}
	    	}
	    	logger.info("onMessage::555:::::: "+malasiyaMOMessage);
	    	
	    	if(malasiyaMOMessage.getText().toUpperCase().contains("STOP")){
	    		
	    		logger.info("onMessage::666:::::: "+malasiyaMOMessage);
				liveReport.setAction(MConstants.DCT);	
				liveReport.setDctCount(1);
			}else{
				  logger.info("onMessage::777:::::: "+malasiyaMOMessage);
				  liveReport.setNoOfDays(mkMalaysiaConfig.getValidityForCharge());
				  liveReport.setAction(MConstants.ACT);
				  liveReport.setConversionCount(1);
				  liveReport.setAmount(0d);	
				  liveReport.setAddToCapping(true);
				  SubscriberReg subscriberReg = subscriberRegService.findOrCreateSubscriberByAct(
						  liveReport.getMsisdn(),null, liveReport);					  
				boolean response=  macroKioskMalaysiaFactoryService
					.handleSubscriptionMOMessage(malasiyaMOMessage);
				
			  logger.info("onMessage:::::::: "+malasiyaMOMessage+" ,handleSubscriptionmalasiyaMOMessage::  "+response);
			
			 // liveReport.setAction(MConstants.GRACE);
			  }
			
		} catch (Exception e) {
			logger.error("onMessage:::::::::::::::::"  +malasiyaMOMessage+ " , Exception  " , e);
		}finally{
			try{				
				if (liveReport!=null&&liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);					
				}
		    	malasiyaMOMessage.setAction(liveReport.getAction());		    	
			}catch(Exception ex){
				logger.error("onMessage:::::::::::::::::"  +malasiyaMOMessage+ " , Exception  " , ex);
			}finally{
			update=daoService.updateObject(malasiyaMOMessage);
			}
		}	
		logger.info("onMessage::::::::::::::::: :: update:: " + update+ ", total time:: " + (System.currentTimeMillis() - time));
	}	
}
