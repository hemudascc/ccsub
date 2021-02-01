package net.mycomp.mobivate;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class JMSMobivateCGCallbackListener implements MessageListener {

	private static final Logger logger = Logger
			.getLogger(JMSMobivateCGCallbackListener.class);

	

	@Autowired
	private IDaoService daoService;
	

	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 

	@Autowired
	private MobivateApiService mobivateApiService;
	
	@Autowired
	private RedisCacheService  redisCacheService;
	
	@Override
	public void onMessage(Message m) {

		MobivateCGCallback mobivateCGCallback = null;
		
		CGToken cgToken=new CGToken("");
		try {

			ObjectMessage objectMessage = (ObjectMessage) m;
			 mobivateCGCallback = (MobivateCGCallback) objectMessage
					.getObject();
			
			logger.info("mobivateCGCallback::::: "+mobivateCGCallback);
			redisCacheService.putObjectCacheValueByEvictionMinute(MobivateConstant.TOKEN_MSISDN_CHACHE_PREFIX+mobivateCGCallback.getUserId(), mobivateCGCallback.getToken(), 1);
			 cgToken=new CGToken(mobivateCGCallback.getToken());
			mobivateCGCallback.setCampaignId(cgToken.getCampaignId());
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			MobivateServiceConfig mobivateServiceConfig=
					MobivateConstant.mapServiceIdToMobivateServiceConfig
					.get(vwServiceCampaignDetail.getServiceId());
		
			
			SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(mobivateCGCallback.getUserId(), 
					vwServiceCampaignDetail.getProductId());
			 
			if(mobivateServiceConfig.getCcOpId()==MConstants.MOBIVATE_SOUTH_AFRICA_CELLC_OPERATOR_ID){
			if(mobivateCGCallback.getCgStatus().equalsIgnoreCase(MobivateConstant.SUCCESS)){
				String msg=mobivateServiceConfig.getWelcomeMessageTemplate();				
				mobivateApiService.sendBilledMessage(mobivateCGCallback.getUserId(),
						MConstants.ACT,mobivateCGCallback.getToken(), mobivateServiceConfig,
						msg, MobivateMessageClass.WELCOME_MESSAGE.getValue(), subscriberReg);			 
			}
			}else if(mobivateServiceConfig.getCcOpId()==MConstants.MOBIVATE_SOUTH_AFRICA_CELLC_OPERATOR_ID){
				mobivateApiService.subApi(mobivateCGCallback, mobivateServiceConfig);
			}else if(mobivateServiceConfig.getCcOpId()==MConstants.MOBIVATE_UK_VODAFONE_OPERATOR_ID){
				if(mobivateCGCallback.getCgStatus().equalsIgnoreCase(MobivateConstant.SUCCESS)){
				String msg=mobivateServiceConfig.getWelcomeMessageTemplate();
				int subId=0;
				if(subscriberReg!=null){
					subId=subscriberReg.getSubscriberId();
				}
				mobivateApiService.sendFreeMessage(mobivateCGCallback.getUserId(),subId, msg,
						mobivateServiceConfig,"FREE_SMS", cgToken.getCGToken());
				
				}
			}
			
		} catch (Exception ex) {
			logger.error("onMessage::::: ", ex);
		} finally {
				 daoService.saveObject(mobivateCGCallback);
			
		}
	}
}
