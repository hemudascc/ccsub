package net.mycomp.mcarokiosk.hongkong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.MConstants;

@Service("hongkongSmartoneMacroKioskService")
public class HongkongSmatoneMacroKioskService extends  AbstractMacroKioskMTMessage{

	private static final Logger logger = Logger.getLogger(HongkongSmatoneMacroKioskService.class);

	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private MKHongkongService mkHongkongService;
	
	@Autowired
	private RedisCacheService redisCacheService; 

	@Override
	public boolean handleSubscriptionMOMessage(HongkongMOMessage hongkongMOMessage) {
		
		logger.info("handleSubscriptionMOMessage::::::::: "+hongkongMOMessage);
		MKHongkongConfig selectedMKHongkongConfig=null;
		for(MKHongkongConfig mkHongkongConfig:MKHongkongConstant.listMKHongkongConfig){

			logger.info("telcoid:::::::: "+mkHongkongConfig.getTelcoId().intValue()+"  hk telcoid "+hongkongMOMessage.getTelcoid()+" text : "+hongkongMOMessage.getText().toUpperCase()+" keywork: "+mkHongkongConfig.getKeyword());
			if(mkHongkongConfig.getTelcoId().intValue()==hongkongMOMessage.getTelcoid()&&
					hongkongMOMessage.getText().toUpperCase().contains(mkHongkongConfig.getKeyword())){
				selectedMKHongkongConfig=mkHongkongConfig;
				break;
			}  
		}		
		
		
		
		
		//MT Billing Messagge
		String text = (hongkongMOMessage.getIsFreeMt())?selectedMKHongkongConfig.getMtWelcomeMessageTemplate():selectedMKHongkongConfig.getMtBillingMessageTemplate()+hongkongMOMessage.getMsisdn();
		logger.info("text::::::   "+text+"handleSubscriptionhongkongMOMessage:: ::::::selectedMKHongkongConfig::  "+selectedMKHongkongConfig);
			String  msg=MKHongkongConstant.convertToHexString(
				MKHongkongConstant.convertToDateTimeFormat())+text;		
	   HongkongMTMessage mtMessage =createMTBillableMessage(selectedMKHongkongConfig,hongkongMOMessage,msg);
		// mtMessage.setAction(MConstants.ACT);
		 mtMessage.setServiceId(selectedMKHongkongConfig.getServiceId());
		 
		 logger.info("handleSubscriptionhongkongMOMessage:: create MT message::::::mtMessage "+mtMessage);
		 HTTPResponse response=smsService.sendMTSMS(mtUrl, mtMessage);
		 redisCacheService.putObjectCacheValueByEvictionMinute(MKHongkongConstant.MT_MESSAGE_CAHCHE_PREFIX
				 +mtMessage.getMsgId(),
				 mtMessage.getId(), 10*60);
		 
		logger.info("handleSubscriptionhongkongMOMessage:: sendMTSMS::::::response "+response);
		daoService.updateObject(mtMessage);
		return true;
	}
	 

	@Override
	public void processDeliveryNotification(
			HongkongDeliveryNotification hongkongDeliveryNotification) {
	    
		try{
			
		if(hongkongDeliveryNotification.getNotificationType()
				.equalsIgnoreCase(MKHongkongConstant.MT_BIILABLE_MESSAGE)){	
			if(hongkongDeliveryNotification!=null&&hongkongDeliveryNotification.getStatus()!=null&&
					(hongkongDeliveryNotification.getStatus().equalsIgnoreCase("1"))){
				  Long dnCounter=redisCacheService.getAndIcrementIntValue(
				    		MKHongkongConstant.HONGKONG_DN_CAHCHE_PREFIX+
				    		hongkongDeliveryNotification.getMtid(), 1,240);				    
					if(dnCounter!=null){
						hongkongDeliveryNotification.setDnCounter(dnCounter.intValue());
					}
					
				if(hongkongDeliveryNotification.getDnCounter()!=2){
					hongkongDeliveryNotification.setAction("CONFIRMATION");
				}
				
				if(hongkongDeliveryNotification.getDnCounter()==2){	
				    hongkongDeliveryNotification.setCharged(true);	
				}
			}
		
			if(hongkongDeliveryNotification!=null&&hongkongDeliveryNotification.getStatus()!=null){
				hongkongDeliveryNotification.setRetry(HongkongSmartoneStatusEnum.isRetry(hongkongDeliveryNotification.getStatus()));			
			}
		}
		}catch(Exception ex){
			logger.error("processhongkongDeliveryNotification::: ",ex);
		}
	}
	
	
protected  HongkongMTMessage
createMTWelcomeMessage(MKHongkongConfig mkHongkongConfig,
		HongkongDeliveryNotification hongkongDeliveryNotification,String msg){
		
		HongkongMTMessage mtMessage=new HongkongMTMessage(true);
		mtMessage.setMessageType(MKHongkongConstant.MT_WELCOME_MESSAGE);
		mtMessage.setMtActionType(MKHongkongConstant.MT_WELCOME_MESSAGE);
		mtMessage.setUser(mkHongkongConfig.getUser());
		mtMessage.setPass(mkHongkongConfig.getPassword());	
		mtMessage.setCat(HongkongMTCat.SUBCRIBE_TYPE.getCatId());
		mtMessage.setFromStr(hongkongDeliveryNotification.getShortcode());
		mtMessage.setMsisdn(hongkongDeliveryNotification.getMsisdn());
		mtMessage.setKeyword(mkHongkongConfig.getKeyword());		
		mtMessage.setServiceId(mkHongkongConfig.getServiceId());
		mtMessage.setOpId(hongkongDeliveryNotification.getOpId());
		mtMessage.setPrice(0d);		
		mtMessage.setTelcoId(hongkongDeliveryNotification.getTelcoId());
		mtMessage.setTextMsg(msg);
		mtMessage.setType(MKHongkongConstant.MT_TEXT);
		mtMessage.setSenderid(hongkongDeliveryNotification.getShortcode());
		return mtMessage;
	}


protected  HongkongMTMessage createMTWelcomeMessage(MKHongkongConfig mkHongkongConfig,
		HongkongMOMessage hongkongMOMessage,String msg){
	
	HongkongMTMessage mtMessage=new HongkongMTMessage(true);
	mtMessage.setMessageType(MKHongkongConstant.MT_WELCOME_MESSAGE);
	mtMessage.setMtActionType(MKHongkongConstant.MT_WELCOME_MESSAGE);
	mtMessage.setUser(mkHongkongConfig.getUser());
	mtMessage.setPass(mkHongkongConfig.getPassword());	
	mtMessage.setCat(HongkongMTCat.SUBCRIBE_TYPE.getCatId());
	mtMessage.setFromStr(hongkongMOMessage.getShortcode());
	mtMessage.setMsisdn(hongkongMOMessage.getMsisdn());
	mtMessage.setKeyword(mkHongkongConfig.getKeyword());		
	mtMessage.setMoMessageId(hongkongMOMessage.getId());		
	mtMessage.setOpId(hongkongMOMessage.getOpId());
	mtMessage.setPrice(0d);		
	mtMessage.setTelcoId(hongkongMOMessage.getTelcoid());
	mtMessage.setTextMsg(msg);
	mtMessage.setType(MKHongkongConstant.MT_TEXT);
	mtMessage.setSenderid(hongkongMOMessage.getShortcode());
	mtMessage.setLinkId(hongkongMOMessage.getMoid());
	return mtMessage;
 }


protected  HongkongMTMessage createMTBillableMessage(MKHongkongConfig mkHongkongConfig,
		HongkongMOMessage hongkongMOMessage,
		String msg){
	HongkongMTMessage mtMessage=new HongkongMTMessage(true);
	mtMessage.setMessageType(MKHongkongConstant.MT_BIILABLE_MESSAGE);
	mtMessage.setMtActionType(MConstants.ACT);
	mtMessage.setUser(mkHongkongConfig.getUser());
	mtMessage.setPass(mkHongkongConfig.getPassword());	
	mtMessage.setCat(HongkongMTCat.CONTENT_BRODCAST.getCatId());
	mtMessage.setFromStr(mkHongkongConfig.getShortcode());
	mtMessage.setMsisdn(hongkongMOMessage.getMsisdn());
	mtMessage.setKeyword(mkHongkongConfig.getKeyword());		
	mtMessage.setMoMessageId(hongkongMOMessage.getId());		
	mtMessage.setMoMessageIdStr(hongkongMOMessage.getMoid());	
	mtMessage.setOpId(hongkongMOMessage.getOpId());
	mtMessage.setPrice(mkHongkongConfig.getPricePoint());		
	mtMessage.setTelcoId(hongkongMOMessage.getTelcoid());
	mtMessage.setTextMsg(msg);
	mtMessage.setType(MKHongkongConstant.MT_TEXT);
	mtMessage.setSenderid(hongkongMOMessage.getShortcode());
	mtMessage.setTokenId(hongkongMOMessage.getTokenId());
	mtMessage.setToken(hongkongMOMessage.getToken());
	mtMessage.setCampaignId(hongkongMOMessage.getCampaignId());
	mtMessage.setCharge(MKHongkongConstant.MT_CHARGE_SUBSCRIPTION);
	mtMessage.setPlatform(mkHongkongConfig.getPlatform());
	return mtMessage;
}	

}
