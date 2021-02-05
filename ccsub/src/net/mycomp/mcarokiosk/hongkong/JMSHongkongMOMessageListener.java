package net.mycomp.mcarokiosk.hongkong;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.util.MConstants;
import net.util.MData;

public class JMSHongkongMOMessageListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(JMSHongkongMOMessageListener.class);

	@Autowired
	private MacroKioskHongkongFactoryService macroKioskHongkongFactoryService;
	
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
		HongkongMOMessage hongkongMOMessage=null;
		LiveReport liveReport=null;
		MKHongkongConfig mkHongkongConfig=null;
		try {	
			hongkongMOMessage=(HongkongMOMessage)objectMessage.getObject();
			logger.info("onMessage:::::::: "+hongkongMOMessage);
		
			String refId=hongkongMOMessage.getRefId();
			if(refId==null){
			refId=(String)redisCacheService.
			getObjectCacheValue(MKHongkongConstant.MO_MESSAGE_CAHCHE_PREFIX+hongkongMOMessage.getMsisdn());
			hongkongMOMessage.setRefId(refId);
			}
			logger.info("onMessage 222:::::::: "+hongkongMOMessage);
			
			MKHongkongCGToken mkHongkongCGToken=new MKHongkongCGToken(refId);
			logger.info("onMessage campid:::::::: "+mkHongkongCGToken.getCampaignId());
			if(mkHongkongCGToken.getCampaignId()==MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID){
				mkHongkongCGToken=new MKHongkongCGToken(MKHongkongConstant.findToken(hongkongMOMessage.getText()));
			}
			
			
			if(mkHongkongCGToken.getCampaignId()==MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID){
				mkHongkongConfig=MacroKioskHongkongFactoryService.findConfigByKeyWordAndTelcoId(hongkongMOMessage.getKeyword().toUpperCase(),
						  hongkongMOMessage.getOpId());
				
				liveReport=new LiveReport(hongkongMOMessage.getOpId(),
						hongkongMOMessage.getCreateTime(), 0,
						mkHongkongConfig.getServiceId(),0);
			}else{    
				logger.info("onMessage::yash:::::: "+hongkongMOMessage);
				VWServiceCampaignDetail vwServiceCampaignDetail=MData.
						mapCamapignIdToVWServiceCampaignDetail.get(mkHongkongCGToken.getCampaignId());
				logger.info("onMessage::vwServiceCampaignDetail:::::: "+vwServiceCampaignDetail);
				mkHongkongConfig=	MKHongkongConstant
						.mapServiceIdToMKHongkongConfig.get(vwServiceCampaignDetail.getServiceId());
				logger.info("onMessage::mkHongkongConfig:::::: "+mkHongkongConfig);
				
				liveReport=new LiveReport(hongkongMOMessage.getOpId(),
						hongkongMOMessage.getCreateTime(), 0,  
						mkHongkongConfig.getServiceId(),0);
			}
			
			logger.info("onMessage::333:::::: "+hongkongMOMessage);
			liveReport.setAdnetworkCampaignId(mkHongkongCGToken.getCampaignId());
			liveReport.setToken(mkHongkongCGToken.getCGToken());
			liveReport.setTokenId(mkHongkongCGToken.getTokenId());
			liveReport.setProductId(mkHongkongConfig.getProductId());
			liveReport.setMsisdn(hongkongMOMessage.getMsisdn());
			hongkongMOMessage.setTokenId(liveReport.getTokenId());
			hongkongMOMessage.setToken(liveReport.getToken());
	    	hongkongMOMessage.setCampaignId(liveReport.getAdnetworkCampaignId());
	    	logger.info("onMessage::444:::::: "+hongkongMOMessage);
	    	if(hongkongMOMessage.getChannel()!=null){
	    		
	    	if(hongkongMOMessage.getChannel().equalsIgnoreCase("0")){	    		
	    		liveReport.setMode("SMS");
	    	}else if(hongkongMOMessage.getChannel().equalsIgnoreCase("1")){
	    		liveReport.setMode("IVR");
	    	}else if(hongkongMOMessage.getChannel().equalsIgnoreCase("2")){
	    		liveReport.setMode("WAP");	
	    	}
	    	}
	    	logger.info("onMessage::555:::::: "+hongkongMOMessage);
	    	
	    	if(hongkongMOMessage.getText().toUpperCase().contains("OFF")){
	    		
	    		logger.info("onMessage::666:::::: "+hongkongMOMessage);
				liveReport.setAction(MConstants.DCT);	
				liveReport.setDctCount(1);
			}else{
				  logger.info("onMessage::777:::::: "+hongkongMOMessage);
				  liveReport.setNoOfDays(mkHongkongConfig.getValidityForCharge());
				  liveReport.setAction(MConstants.ACT);
				  liveReport.setConversionCount(1);
				  liveReport.setAmount(0d);	
				  liveReport.setAddToCapping(true);
				  SubscriberReg subscriberReg = subscriberRegService.findOrCreateSubscriberByAct(
						  liveReport.getMsisdn(),null, liveReport);					  
				boolean response=  macroKioskHongkongFactoryService
					.handleSubscriptionMOMessage(hongkongMOMessage);
				
			  logger.info("onMessage:::::::: "+hongkongMOMessage+" ,handleSubscriptionhongkongMOMessage::  "+response);
			
			 // liveReport.setAction(MConstants.GRACE);
			  }
			
		} catch (Exception e) {
			logger.error("onMessage:::::::::::::::::"  +hongkongMOMessage+ " , Exception  " , e);
		}finally{
			try{				
				if (liveReport!=null&&liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);					
				}
		    	hongkongMOMessage.setAction(liveReport.getAction());		    	
			}catch(Exception ex){
				logger.error("onMessage:::::::::::::::::"  +hongkongMOMessage+ " , Exception  " , ex);
			}finally{
			update=daoService.updateObject(hongkongMOMessage);
			}
		}	
		logger.info("onMessage::::::::::::::::: :: update:: " + update+ ", total time:: " + (System.currentTimeMillis() - time));
	}	

}
