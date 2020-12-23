package net.mycomp.worldplay;


import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.Service;
import net.persist.bean.SubscriberReg;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSWorlplayCGCallbackListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSWorlplayCGCallbackListener.class);

	

	@Autowired
	private IDaoService daoService;
	
	

	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
  
	@Override
	public void onMessage(Message m) {

		WorldplayCGCallback worldplayCGCallback = null;
		LiveReport liveReport = null;
		boolean update = false;
		long time = System.currentTimeMillis();
		
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			worldplayCGCallback = (WorldplayCGCallback) objectMessage
					.getObject();			
			logger.debug("worldplayCGCallback::::::: "+worldplayCGCallback);
			
			WorldplayServiceConfig worldplayServiceConfig=
					 WorldplayConstant.getWorldplayServiceConfig(worldplayCGCallback.getOperator(),
							 worldplayCGCallback.getClient());
			
			Service service=MData.mapServiceIdToService.get(worldplayServiceConfig.getServiceId());
			 liveReport=new LiveReport(service.getOpId(),worldplayCGCallback.getCreateTime(),
		    			-1,worldplayServiceConfig.getServiceId(),service.getProductId()
							 );
			 
			 if(worldplayCGCallback.getAdTracking()!=null&&
					 worldplayCGCallback.getTelNo()!=null){
				 redisCacheService
				 .putObjectCacheValueByEvictionMinute(WorldplayConstant
						 .WORLDPLAY_CACHE_PREFIX_TOKEN
						 +worldplayCGCallback.getTelNo(),
						 worldplayCGCallback.getAdTracking(), 60*20);
			 }
			 
			 CGToken cgToken =new CGToken(worldplayCGCallback.getAdTracking());
			 liveReport.setAdnetworkCampaignId(cgToken.getCampaignId());
			 liveReport.setToken(cgToken.getCGToken());
			 liveReport.setTokenId(cgToken.getTokenId());			 
			 liveReport.setMsisdn(worldplayCGCallback.getTelNo());
			 SubscriberReg subscriberReg=jpaSubscriberReg
					 .findSubscriberRegByMsisdnAndProductId(worldplayCGCallback.getTelNo()
					 ,service.getProductId());
			 
			if(worldplayCGCallback.getMt().equalsIgnoreCase("tnSubscribe")&&(subscriberReg==null
					||subscriberReg.getStatus()!=MConstants.SUBSCRIBED)){//this is only subscription confirmation not charging	
			        	liveReport.setAction(MConstants.ACT);
						liveReport.setConversionCount(0);
						liveReport.setNoOfDays(1);
						liveReport.setAmount(0d);
						//liveReport.setParam1(WorldplayConstant.CHARGING_PENDING);
						worldplayCGCallback.setAction(MConstants.SUBSCRIBED_DESC);
						subscriberRegService.findOrCreateSubscriberByAct(worldplayCGCallback.getTelNo()
								, null, liveReport);
			}
			 
		} catch (Exception ex) {
			logger.error("onMessage::::: "+worldplayCGCallback, ex);
		} finally {
			try {
				if (liveReport!=null&&liveReport.getAction() != null) {				    
					//liveReport = liveReportFactoryService.process(liveReport);					
					//worldplayCGCallback.setAction(liveReport.getAction());
				}
				
			} catch (Exception ex) {
				logger.error(" fianlly  , : worldplayNotification:: "+ worldplayCGCallback,ex);
				
			} finally {
				update = daoService.saveObject(worldplayCGCallback);
			}
			logger.info("onMessage::::::::::::::::: :: update::live report "
					+ update + ", total time:: "
					+ (System.currentTimeMillis() - time));
		}
	}
}
