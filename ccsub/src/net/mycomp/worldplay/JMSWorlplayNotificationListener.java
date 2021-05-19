package net.mycomp.worldplay;


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
import net.persist.bean.SubscriberReg;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSWorlplayNotificationListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSWorlplayNotificationListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;

	@Autowired
	private RedisCacheService redisCacheService;
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
  
	@Override
	public void onMessage(Message m) {

		WorldplayNotification worldplayNotification = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			worldplayNotification = (WorldplayNotification) objectMessage
					.getObject();			
			logger.debug("worldplayNotification::::::: "+worldplayNotification);
			
			WorldplayServiceConfig worldplayServiceConfig=
					 WorldplayConstant.getWorldplayServiceConfig(worldplayNotification.getOperatorName(),
							 worldplayNotification.getClient());
			worldplayNotification.setWorldplayServiceConfigId(worldplayServiceConfig.getId());
			
			Service service=MData.mapServiceIdToService.get(worldplayServiceConfig.getServiceId());
			 liveReport=new LiveReport(service.getOpId(),worldplayNotification.getCreateTime(),
		    			-1,worldplayServiceConfig.getServiceId(),service.getProductId()
							 );
			 String token=worldplayNotification.getAdTracking();
			 if(token==null){
				 token=Objects.toString(redisCacheService.getObjectCacheValue(WorldplayConstant
						 .WORLDPLAY_CACHE_PREFIX_TOKEN+worldplayNotification.getTelNo()));
			 }
			 
			 CGToken cgToken =new CGToken(token);
			 liveReport.setAdnetworkCampaignId(cgToken.getCampaignId());
			 liveReport.setToken(cgToken.getCGToken());
			 liveReport.setTokenId(cgToken.getTokenId());			 
			 liveReport.setMsisdn(worldplayNotification.getTelNo());
			
//			 SubscriberReg subscriberReg=jpaSubscriberReg
//					 .findSubscriberRegByMsisdnAndProductId(worldplayNotification.getTelNo()
//					 ,service.getProductId());
//			 
//			int subDayDiff=0; 
//			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){ 
//				subDayDiff=MUtility.noOfDaysDiffrence(subscriberReg.getSubDate()
//						,worldplayNotification.getCreateTime());
//			}
			
			 liveReport.setResponse(worldplayNotification.toString());
			if(worldplayNotification.getMt().equalsIgnoreCase("sdResult")
					&&worldplayNotification.getStatusId().equals("0")
					&&liveReport.getTokenId()>0
					//(subscriberReg==null
					//||subscriberReg.getStatus()!=MConstants.SUBSCRIBED||
					//(subscriberReg.getParam1()!=null
					//&&subscriberReg.getParam1()
					//.equalsIgnoreCase(WorldplayConstant.CHARGING_PENDING)))
					){//charging cnfirmation
				
						liveReport.setAction(MConstants.ACT);
						worldplayNotification.setActionType(MConstants.ACT);
						liveReport.setConversionCount(1);
						liveReport.setNoOfDays(worldplayServiceConfig.getValidity());
						liveReport.setAmount(MUtility.toDouble(worldplayNotification.getAmount(),0));
						liveReport.setParam1("");
						
			}else if(worldplayNotification.getMt().equalsIgnoreCase("sdResult")
					&&worldplayNotification.getStatusId().equals("0")){//charging cnfirmation
				liveReport.setAction(MConstants.RENEW);
				worldplayNotification.setActionType(MConstants.RENEW);
				liveReport.setRenewalCount(1);
				liveReport.setNoOfDays(worldplayServiceConfig.getValidity());
				liveReport.setRenewalAmount(MUtility.toDouble(worldplayNotification.getAmount(),0));				
			}else if(worldplayNotification.getMt().equalsIgnoreCase("tnUnsubscribe")){
				liveReport.setAction(MConstants.DCT);
				worldplayNotification.setActionType(MConstants.DCT);
				liveReport.setDctCount(1);
			}else if(worldplayNotification.getMt().equalsIgnoreCase("tnSubscribe")){				
				worldplayNotification.setAction("CHARGING PENDING");				
			}else if(worldplayNotification.getMt().equalsIgnoreCase("sdResult")
					&&worldplayNotification.getStatusId().equals("106")){
				
				//worldplayNotification.setAction("PARKING");		
				liveReport.setAction(MConstants.GRACE);
				worldplayNotification.setActionType(WorldplayConstant.RENEWAL_GRACE);
				if(liveReport.getTokenId()>0){//same day sub as grace count only 
				    liveReport.setGraceConversionCount(1);//RenewalCount(1);
				    worldplayNotification.setActionType(WorldplayConstant.ACT_GRACE);
				}
			}
			 
		} catch (Exception ex) {
			logger.error("onMessage::::: "+worldplayNotification, ex);
		} finally {
			try {
				worldplayNotification.setToken(liveReport.getToken());				
				
				if (liveReport!=null&&liveReport.getAction() != null) {				    
					liveReport = liveReportFactoryService.process(liveReport);					
					worldplayNotification.setAction(liveReport.getAction());
				}
				worldplayNotification.setSendToAdnetwork(liveReport.getSendConversionCount()>0?true:false);
			} catch (Exception ex) {
				logger.error(" fianlly  , : worldplayNotification:: "+ worldplayNotification,ex);
				
			} finally {
				update = daoService.saveObject(worldplayNotification);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
