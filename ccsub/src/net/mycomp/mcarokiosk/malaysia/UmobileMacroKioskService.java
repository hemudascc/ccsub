package net.mycomp.mcarokiosk.malaysia;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("umobileMacroKioskService")
public class UmobileMacroKioskService extends  AbstractMacroKioskMTMessage{

	private static final Logger logger = Logger.getLogger(UmobileMacroKioskService.class);

	
	@Autowired
	private IDaoService daoService;
	
	
	
	@Autowired
	private RedisCacheService redisCacheService; 

	@Override
	public boolean handleSubscriptionMOMessage(MalasiyaMOMessage malasiyaMOMessage) {
		
		logger.info("handleSubscriptionMOMessage::::::::: "+malasiyaMOMessage);
		MKMalaysiaConfig selectedMKMalaysiaConfig=null;
		for(MKMalaysiaConfig mkMalaysiaConfig:MKMalaysiaConstant.listMKMalaysiaConfig){
			if(mkMalaysiaConfig.getTelcoId().intValue()==malasiyaMOMessage.getTelcoid()&&
					malasiyaMOMessage.getText().toUpperCase().contains(mkMalaysiaConfig.getKeyword())){
				selectedMKMalaysiaConfig=mkMalaysiaConfig;
				break;
			}
		}		
		
		      //MT Billing Messagge
				logger.info("handleSubscriptionmalasiyaMOMessage:: ::::::selectedMKMalaysiaConfig::  "+selectedMKMalaysiaConfig);
			     String msg=MKMalaysiaConstant.convertToHexString(
						MKMalaysiaConstant.convertToDateTimeFormat())+
						selectedMKMalaysiaConfig.getMtBillingMessageTemplate();		
			     MalasiyaMTMessage mtMessage =createMTBillableMessage(selectedMKMalaysiaConfig,malasiyaMOMessage,msg);
				 mtMessage.setServiceId(selectedMKMalaysiaConfig.getServiceId());
				 
				 logger.info("handleSubscriptionmalasiyaMOMessage:: create MT message::::::mtMessage "+mtMessage);
				 HTTPResponse  response=smsService.sendMTSMS(mtUrl, mtMessage);
				 redisCacheService.putObjectCacheValueByEvictionMinute(MKMalaysiaConstant.MT_MESSAGE_CAHCHE_PREFIX
						 +mtMessage.getMsgId(),
						 mtMessage.getId(), 10*60);				 
				logger.info("handleSubscriptionmalasiyaMOMessage:: sendMTSMS::::::response "+response);
				daoService.updateObject(mtMessage);
				
	     return true;
	}
	
	

	@Override
	public boolean sendSubscriptionRenewalRequest(SubscriberReg subscriberReg) {
		
		MKMalaysiaConfig mkMalaysiaConfig =MKMalaysiaConstant
				.mapServiceIdToMKMalaysiaConfig.get(subscriberReg.getServiceId());//MacroKioskFactoryService.findThConfigByServiceIdAndTelcoId(subscriberReg.getServiceId(), telcoId);
		
		MalasiyaMTMessage malasiyaMTMessage=new MalasiyaMTMessage(true);
		malasiyaMTMessage.setServiceId(mkMalaysiaConfig.getServiceId());
		malasiyaMTMessage.setMessageType(MKMalaysiaConstant.MT_BIILABLE_MESSAGE);
		malasiyaMTMessage.setMtActionType(MConstants.RENEW);
		malasiyaMTMessage.setUser(mkMalaysiaConfig.getUser());
		malasiyaMTMessage.setPass(mkMalaysiaConfig.getPassword());	
		malasiyaMTMessage.setCat(MalaysiaMTCat.RENEWAL_SUBSCRIPTION.getCatId());
		malasiyaMTMessage.setFromStr(mkMalaysiaConfig.getShortcode());
		malasiyaMTMessage.setMsisdn(subscriberReg.getMsisdn());
		malasiyaMTMessage.setKeyword(mkMalaysiaConfig.getKeyword());		
		malasiyaMTMessage.setOpId(subscriberReg.getOperatorId());
		malasiyaMTMessage.setPrice(mkMalaysiaConfig.getPricePoint());		
		malasiyaMTMessage.setTelcoId(mkMalaysiaConfig.getTelcoId());
		String msg=MKMalaysiaConstant.convertToHexString(
				MKMalaysiaConstant.convertToDateTimeFormat())+
				mkMalaysiaConfig.getMtRenewalMessageTemplate();//BillingMessageTemplate();		
		malasiyaMTMessage.setTextMsg(msg);
		malasiyaMTMessage.setType(MKMalaysiaConstant.MT_TEXT);
		malasiyaMTMessage.setSenderid(mkMalaysiaConfig.getShortcode());	
		malasiyaMTMessage.setCharge(MKMalaysiaConstant.MT_CHARGE_SUBSCRIPTION);
		
		HTTPResponse response=smsService.sendMTSMS(mtUrl, malasiyaMTMessage);
		redisCacheService.putObjectCacheValueByEvictionMinute(MKMalaysiaConstant.MT_MESSAGE_CAHCHE_PREFIX+malasiyaMTMessage.getMsgId(),
				 malasiyaMTMessage.getId(), 10*60);
		logger.info("sendSubscriptionRenewalRequest:: sendMTSMS::::::malasiyaMTMessage:: "+malasiyaMTMessage+" ,response "+response);
		daoService.updateObject(malasiyaMTMessage);	
		return true;
	}
	
	

	@Override
	public void processDeliveryNotification(
			MalaysiaDeliveryNotification malaysiaDeliveryNotification) {
	    
		try{
			
		if(malaysiaDeliveryNotification.getNotificationType()
				.equalsIgnoreCase(MKMalaysiaConstant.MT_BIILABLE_MESSAGE)){	
			if(malaysiaDeliveryNotification!=null&&malaysiaDeliveryNotification.getStatus()!=null&&
					(malaysiaDeliveryNotification.getStatus().equalsIgnoreCase("1"))){
				
				    malaysiaDeliveryNotification.setCharged(true);	
				}
			
		
			if(malaysiaDeliveryNotification!=null&&malaysiaDeliveryNotification.getStatus()!=null){
				malaysiaDeliveryNotification.setRetry(MalaysiaDIGIStatusEnum.isRetry(malaysiaDeliveryNotification.getStatus()));			
			}
		}
		}catch(Exception ex){
			logger.error("processmalaysiaDeliveryNotification::: ",ex);
		}
	}
	
	
protected  MalasiyaMTMessage createMTWelcomeMessage(MKMalaysiaConfig mkMalaysiaConfig,
		MalaysiaDeliveryNotification deliveryNotification,String msg){
		
	MalasiyaMTMessage malasiyaMTMessage=new MalasiyaMTMessage(true);
		malasiyaMTMessage.setMessageType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
		malasiyaMTMessage.setMtActionType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
		malasiyaMTMessage.setUser(mkMalaysiaConfig.getUser());
		malasiyaMTMessage.setPass(mkMalaysiaConfig.getPassword());	
		malasiyaMTMessage.setCat(MalaysiaMTCat.SUBCRIBE_TYPE.getCatId());
		malasiyaMTMessage.setFromStr(deliveryNotification.getShortcode());
		malasiyaMTMessage.setMsisdn(deliveryNotification.getMsisdn());
		malasiyaMTMessage.setKeyword(mkMalaysiaConfig.getKeyword());		
		//malasiyaMTMessage.setMsgId(deliveryNotification.getMtid());	
		malasiyaMTMessage.setServiceId(mkMalaysiaConfig.getServiceId());
		malasiyaMTMessage.setOpId(deliveryNotification.getOpId());
		malasiyaMTMessage.setPrice(0d);		
		malasiyaMTMessage.setTelcoId(deliveryNotification.getTelcoId());
		malasiyaMTMessage.setTextMsg(msg);
		malasiyaMTMessage.setType(MKMalaysiaConstant.MT_TEXT);
		malasiyaMTMessage.setSenderid(deliveryNotification.getShortcode());
		return malasiyaMTMessage;
	}


protected  MalasiyaMTMessage createMTWelcomeMessage(MKMalaysiaConfig mkMalaysiaConfig,MalasiyaMOMessage malasiyaMOMessage,String msg){
	
	MalasiyaMTMessage malasiyamalasiyaMTMessage=new MalasiyaMTMessage(true);
	malasiyamalasiyaMTMessage.setMessageType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
	malasiyamalasiyaMTMessage.setMtActionType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
	malasiyamalasiyaMTMessage.setUser(mkMalaysiaConfig.getUser());
	malasiyamalasiyaMTMessage.setPass(mkMalaysiaConfig.getPassword());	
	malasiyamalasiyaMTMessage.setCat(MalaysiaMTCat.SUBCRIBE_TYPE.getCatId());
	malasiyamalasiyaMTMessage.setFromStr(malasiyaMOMessage.getShortcode());
	malasiyamalasiyaMTMessage.setMsisdn(malasiyaMOMessage.getMsisdn());
	malasiyamalasiyaMTMessage.setKeyword(mkMalaysiaConfig.getKeyword());		
	malasiyamalasiyaMTMessage.setMoMessageId(malasiyaMOMessage.getId());		
	malasiyamalasiyaMTMessage.setOpId(malasiyaMOMessage.getOpId());
	malasiyamalasiyaMTMessage.setPrice(0d);		
	malasiyamalasiyaMTMessage.setTelcoId(malasiyaMOMessage.getTelcoid());
	malasiyamalasiyaMTMessage.setTextMsg(msg);
	malasiyamalasiyaMTMessage.setType(MKMalaysiaConstant.MT_TEXT);
	malasiyamalasiyaMTMessage.setSenderid(malasiyaMOMessage.getShortcode());
	malasiyamalasiyaMTMessage.setLinkId(malasiyaMOMessage.getMoid());
	return malasiyamalasiyaMTMessage;
 }


protected  MalasiyaMTMessage createMTBillableMessage(MKMalaysiaConfig mkMalaysiaConfig,
		MalasiyaMOMessage malasiyaMOMessage,String msg){
	
	MalasiyaMTMessage mtMessage=new MalasiyaMTMessage(true);
	mtMessage.setMessageType(MKMalaysiaConstant.MT_BIILABLE_MESSAGE);
	mtMessage.setMtActionType(MConstants.ACT);
	mtMessage.setUser(mkMalaysiaConfig.getUser());
	mtMessage.setPass(mkMalaysiaConfig.getPassword());	
	mtMessage.setCat(MalaysiaMTCat.SUBCRIBE_TYPE.getCatId());
	mtMessage.setFromStr(malasiyaMOMessage.getShortcode());
	mtMessage.setMsisdn(malasiyaMOMessage.getMsisdn());
	mtMessage.setKeyword(mkMalaysiaConfig.getKeyword());		
	mtMessage.setMoMessageId(malasiyaMOMessage.getId());		
	mtMessage.setMoMessageIdStr(malasiyaMOMessage.getMoid());
	mtMessage.setOpId(malasiyaMOMessage.getOpId());
	mtMessage.setPrice(mkMalaysiaConfig.getPricePoint());		
	mtMessage.setTelcoId(malasiyaMOMessage.getTelcoid());
	mtMessage.setTextMsg(msg);
	mtMessage.setType(MKMalaysiaConstant.MT_TEXT);
	mtMessage.setSenderid(malasiyaMOMessage.getShortcode());
	mtMessage.setTokenId(malasiyaMOMessage.getTokenId());
	mtMessage.setToken(malasiyaMOMessage.getToken());
	mtMessage.setCampaignId(malasiyaMOMessage.getCampaignId());
	mtMessage.setCharge(MKMalaysiaConstant.MT_CHARGE_SUBSCRIPTION);
	return mtMessage;
   }	
}


