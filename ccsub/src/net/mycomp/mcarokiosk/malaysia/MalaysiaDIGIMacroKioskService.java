package net.mycomp.mcarokiosk.malaysia;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.MConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("malaysiaDIGIMacroKioskService")
public class MalaysiaDIGIMacroKioskService extends  AbstractMacroKioskMTMessage{

	private static final Logger logger = Logger.getLogger(MalaysiaDIGIMacroKioskService.class);

	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private MKMalaysiaService mkMalaysiaService;
	
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
	   String  msg=MKMalaysiaConstant.convertToHexString(
				MKMalaysiaConstant.convertToDateTimeFormat())+
				selectedMKMalaysiaConfig.getMtBillingMessageTemplate();		
	   MalasiyaMTMessage mtMessage =createMTBillableMessage(selectedMKMalaysiaConfig,malasiyaMOMessage,msg);
		// mtMessage.setAction(MConstants.ACT);
		 mtMessage.setServiceId(selectedMKMalaysiaConfig.getServiceId());
		 
		 logger.info("handleSubscriptionmalasiyaMOMessage:: create MT message::::::mtMessage "+mtMessage);
		 HTTPResponse response=smsService.sendMTSMS(mtUrl, mtMessage);
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
		
		MalasiyaMTMessage mtMessage=new MalasiyaMTMessage(true);
		mtMessage.setServiceId(mkMalaysiaConfig.getServiceId());
		mtMessage.setMessageType(MKMalaysiaConstant.MT_BIILABLE_MESSAGE);
		mtMessage.setMtActionType(MConstants.RENEW);
		mtMessage.setUser(mkMalaysiaConfig.getUser());
		mtMessage.setPass(mkMalaysiaConfig.getPassword());	
		
		mtMessage.setCat(MalaysiaMTCat.RENEWAL_SUBSCRIPTION.getCatId());
		mtMessage.setFromStr(mkMalaysiaConfig.getShortcode());
		mtMessage.setMsisdn(subscriberReg.getMsisdn());
		mtMessage.setKeyword(mkMalaysiaConfig.getKeyword());		
		mtMessage.setOpId(subscriberReg.getOperatorId());
		mtMessage.setPrice(mkMalaysiaConfig.getPricePoint());		
		mtMessage.setTelcoId(mkMalaysiaConfig.getTelcoId());
		String msg=MKMalaysiaConstant.convertToHexString(
				MKMalaysiaConstant.convertToDateTimeFormat())+
				mkMalaysiaConfig.getMtRenewalMessageTemplate();//BillingMessageTemplate();		
		mtMessage.setTextMsg(msg);
		mtMessage.setType(MKMalaysiaConstant.MT_TEXT);
		mtMessage.setSenderid(mkMalaysiaConfig.getShortcode());	
		mtMessage.setCharge(MKMalaysiaConstant.MT_CHARGE_SUBSCRIPTION);	
		//mtMessage.setAction(MConstants.RENEW);
		HTTPResponse response=smsService.sendMTSMS(mtUrl, mtMessage);
		redisCacheService.putObjectCacheValueByEvictionMinute(MKMalaysiaConstant
				.MT_MESSAGE_CAHCHE_PREFIX+mtMessage.getMsgId(),
				 mtMessage.getId(), 10*60);
		logger.info("sendSubscriptionRenewalRequest:: sendMTSMS::::::mtMessage:: "+mtMessage+" ,response "+response);
		daoService.updateObject(mtMessage);
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
				  Long dnCounter=redisCacheService.getAndIcrementIntValue(
				    		MKMalaysiaConstant.MALASIYA_DN_CAHCHE_PREFIX+
				    		malaysiaDeliveryNotification.getMtid(), 1,240);				    
					if(dnCounter!=null){
						malaysiaDeliveryNotification.setDnCounter(dnCounter.intValue());
					}
					
				if(malaysiaDeliveryNotification.getDnCounter()!=2){
					malaysiaDeliveryNotification.setAction("CONFIRMATION");
				}
				
				if(malaysiaDeliveryNotification.getDnCounter()==2){	
				    malaysiaDeliveryNotification.setCharged(true);	
				}
			}
		
			if(malaysiaDeliveryNotification!=null&&malaysiaDeliveryNotification.getStatus()!=null){
				malaysiaDeliveryNotification.setRetry(MalaysiaDIGIStatusEnum.isRetry(malaysiaDeliveryNotification.getStatus()));			
			}
		}
		}catch(Exception ex){
			logger.error("processmalaysiaDeliveryNotification::: ",ex);
		}
	}
	
	
protected  MalasiyaMTMessage
createMTWelcomeMessage(MKMalaysiaConfig mkMalaysiaConfig,
		MalaysiaDeliveryNotification malaysiaDeliveryNotification,String msg){
		
		MalasiyaMTMessage mtMessage=new MalasiyaMTMessage(true);
		mtMessage.setMessageType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
		mtMessage.setMtActionType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
		mtMessage.setUser(mkMalaysiaConfig.getUser());
		mtMessage.setPass(mkMalaysiaConfig.getPassword());	
		mtMessage.setCat(MalaysiaMTCat.SUBCRIBE_TYPE.getCatId());
		mtMessage.setFromStr(malaysiaDeliveryNotification.getShortcode());
		mtMessage.setMsisdn(malaysiaDeliveryNotification.getMsisdn());
		mtMessage.setKeyword(mkMalaysiaConfig.getKeyword());		
		//mtMessage.setMsgId(malaysiaDeliveryNotification.getMtid());	
		mtMessage.setServiceId(mkMalaysiaConfig.getServiceId());
		mtMessage.setOpId(malaysiaDeliveryNotification.getOpId());
		mtMessage.setPrice(0d);		
		mtMessage.setTelcoId(malaysiaDeliveryNotification.getTelcoId());
		mtMessage.setTextMsg(msg);
		mtMessage.setType(MKMalaysiaConstant.MT_TEXT);
		mtMessage.setSenderid(malaysiaDeliveryNotification.getShortcode());
		return mtMessage;
	}


protected  MalasiyaMTMessage createMTWelcomeMessage(MKMalaysiaConfig mkMalaysiaConfig,
		MalasiyaMOMessage malasiyaMOMessage,String msg){
	
	MalasiyaMTMessage mtMessage=new MalasiyaMTMessage(true);
	mtMessage.setMessageType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
	mtMessage.setMtActionType(MKMalaysiaConstant.MT_WELCOME_MESSAGE);
	mtMessage.setUser(mkMalaysiaConfig.getUser());
	mtMessage.setPass(mkMalaysiaConfig.getPassword());	
	mtMessage.setCat(MalaysiaMTCat.SUBCRIBE_TYPE.getCatId());
	mtMessage.setFromStr(malasiyaMOMessage.getShortcode());
	mtMessage.setMsisdn(malasiyaMOMessage.getMsisdn());
	mtMessage.setKeyword(mkMalaysiaConfig.getKeyword());		
	mtMessage.setMoMessageId(malasiyaMOMessage.getId());		
	mtMessage.setOpId(malasiyaMOMessage.getOpId());
	mtMessage.setPrice(0d);		
	mtMessage.setTelcoId(malasiyaMOMessage.getTelcoid());
	mtMessage.setTextMsg(msg);
	mtMessage.setType(MKMalaysiaConstant.MT_TEXT);
	mtMessage.setSenderid(malasiyaMOMessage.getShortcode());
	mtMessage.setLinkId(malasiyaMOMessage.getMoid());
	return mtMessage;
 }


protected  MalasiyaMTMessage createMTBillableMessage(MKMalaysiaConfig mkMalaysiaConfig,
		MalasiyaMOMessage malasiyaMOMessage,
		String msg){
	//http://mis.etracker.cc/THPush/THpush.aspx?
	//user=creativeantenna&pass=creativeantenna123&type=5&to=1&
	//from=4541538&text=0E140E320E270E190E4C0E420E2B0E250E1400200076006900640065006F00200E440E140E490E440E210E480E080E330E010E310E140E170E380E010E270E310E190E170E350E480020&
	//price=10&telcoid=1&cat=6&keyword=hot&senderid=4541538
	MalasiyaMTMessage mtMessage=new MalasiyaMTMessage(true);
	mtMessage.setMessageType(MKMalaysiaConstant.MT_BIILABLE_MESSAGE);
	mtMessage.setMtActionType(MConstants.ACT);
	mtMessage.setUser(mkMalaysiaConfig.getUser());
	mtMessage.setPass(mkMalaysiaConfig.getPassword());	
	mtMessage.setCat(MalaysiaMTCat.CONTENT_BRODCAST.getCatId());
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


