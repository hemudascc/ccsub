package net.mycomp.mcarokiosk.hongkong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.MConstants;

@Service("hutchisonMacroKioskService")
public class HutchisonMacroKioskService  implements IMacroKioskService{

	private static final Logger logger = Logger.getLogger(HutchisonMacroKioskService.class);

	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	protected HongkongSmsService smsService;

	@Value("${macrokiosk.hongkong.mt.url}")
	protected String mtUrl;
	
	@Autowired
	private RedisCacheService redisCacheService; 

	@Override
	public boolean handleSubscriptionMOMessage(HongkongMOMessage hongkongMOMessage) {
		
		logger.info("handleSubscriptionMOMessage::::::::: "+hongkongMOMessage);
		MKHongkongConfig selectedMKHongkongConfig=null;
		for(MKHongkongConfig mkHongkongConfig:MKHongkongConstant.listMKHongkongConfig){
			if(mkHongkongConfig.getTelcoId().intValue()==hongkongMOMessage.getTelcoid()&&
					hongkongMOMessage.getText().toUpperCase().contains(mkHongkongConfig.getKeyword())){
				selectedMKHongkongConfig=mkHongkongConfig;
				break;
			}
		}		
		
		      //MT Billing Messagge
	    		String text = (hongkongMOMessage.getIsFreeMt())?selectedMKHongkongConfig.getMtWelcomeMessageTemplate():selectedMKHongkongConfig.getMtBillingMessageTemplate()+hongkongMOMessage.getMsisdn()+"&idcmp="+hongkongMOMessage.getCampaignId();
				logger.info("text::::::   "+text+"handleSubscriptionhongkongMOMessage:: ::::::selectedMKHongkongConfig::  "+selectedMKHongkongConfig);
				String msg=MKHongkongConstant.convertToHexString(
						MKHongkongConstant.convertToDateTimeFormat())+text;		
			    HongkongMTMessage mtMessage =(hongkongMOMessage.getIsFreeMt())?createMTWelcomeMessage(selectedMKHongkongConfig,hongkongMOMessage,msg):createMTBillableMessage(selectedMKHongkongConfig,hongkongMOMessage,msg);
				 mtMessage.setServiceId(selectedMKHongkongConfig.getServiceId());
				 
				 logger.info("handleSubscriptionhongkongMOMessage:: create MT message::::::mtMessage "+mtMessage);
				 HTTPResponse  response=smsService.sendMTSMS(mtUrl, mtMessage);
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
				
				    hongkongDeliveryNotification.setCharged(true);	
				}
			
		
			if(hongkongDeliveryNotification!=null&&hongkongDeliveryNotification.getStatus()!=null){
//Yash				hongkongDeliveryNotification.setRetry(HongkongSmartoneStatusEnum.isRetry(hongkongDeliveryNotification.getStatus()));			
			}
		}
		}catch(Exception ex){
			logger.error("processhongkongDeliveryNotification::: ",ex);
		}
	}
	
	
protected  HongkongMTMessage createMTWelcomeMessage(MKHongkongConfig mkHongkongConfig,
		HongkongDeliveryNotification deliveryNotification,String msg){
		
	HongkongMTMessage hongkongMTMessage=new HongkongMTMessage(true);
		hongkongMTMessage.setMessageType(MKHongkongConstant.MT_WELCOME_MESSAGE);
		hongkongMTMessage.setMtActionType(MKHongkongConstant.MT_WELCOME_MESSAGE);
		hongkongMTMessage.setUser(mkHongkongConfig.getUser());
		hongkongMTMessage.setPass(mkHongkongConfig.getPassword());	
		hongkongMTMessage.setCat(HongkongMTCat.SUBCRIBE_TYPE.getCatId());
		hongkongMTMessage.setFromStr(deliveryNotification.getShortcode());
		hongkongMTMessage.setMsisdn(deliveryNotification.getMsisdn());
		hongkongMTMessage.setKeyword(mkHongkongConfig.getKeyword());		
		//hongkongMTMessage.setMsgId(deliveryNotification.getMtid());	
		hongkongMTMessage.setServiceId(mkHongkongConfig.getServiceId());
		hongkongMTMessage.setOpId(deliveryNotification.getOpId());
		hongkongMTMessage.setPrice(0d);		
		hongkongMTMessage.setTelcoId(deliveryNotification.getTelcoId());
		hongkongMTMessage.setTextMsg(msg);
		hongkongMTMessage.setType(MKHongkongConstant.MT_TEXT);
		hongkongMTMessage.setSenderid(deliveryNotification.getShortcode());
		return hongkongMTMessage;
	}


protected  HongkongMTMessage createMTWelcomeMessage(MKHongkongConfig mkHongkongConfig,HongkongMOMessage hongkongMOMessage,String msg){
	
	HongkongMTMessage mtMessage=new HongkongMTMessage(true);
	mtMessage.setMessageType(MKHongkongConstant.MT_WELCOME_MESSAGE);
	mtMessage.setMtActionType(MKHongkongConstant.MT_WELCOME_MESSAGE);
	mtMessage.setUser(mkHongkongConfig.getUser());
	mtMessage.setPass(mkHongkongConfig.getPassword());	
	mtMessage.setCat(HongkongMTCat.SUBCRIBE_TYPE.getCatId());
	mtMessage.setFromStr(mkHongkongConfig.getShortcode()); 
	mtMessage.setMsisdn(hongkongMOMessage.getMsisdn());
	mtMessage.setKeyword(mkHongkongConfig.getKeyword());		
	mtMessage.setMoMessageId(hongkongMOMessage.getId());		
	mtMessage.setMoMessageIdStr(hongkongMOMessage.getMoid());  
	mtMessage.setOpId(hongkongMOMessage.getOpId());
	mtMessage.setPrice(0d);		
	mtMessage.setTelcoId(hongkongMOMessage.getTelcoid());
	mtMessage.setTextMsg(msg);
	mtMessage.setType(MKHongkongConstant.MT_TEXT);
	mtMessage.setSenderid(hongkongMOMessage.getShortcode());
	mtMessage.setTokenId(hongkongMOMessage.getTokenId());
	mtMessage.setToken(hongkongMOMessage.getToken());
	mtMessage.setCampaignId(hongkongMOMessage.getCampaignId());
	mtMessage.setPlatform(mkHongkongConfig.getPlatform());
	mtMessage.setCharge(null);
	return mtMessage;
 }


protected  HongkongMTMessage createMTBillableMessage(MKHongkongConfig mkHongkongConfig,
		HongkongMOMessage hongkongMOMessage,String msg){
	
	HongkongMTMessage mtMessage=new HongkongMTMessage(true);
	mtMessage.setMessageType((hongkongMOMessage.getIsFreeMt())?MKHongkongConstant.MT_WELCOME_MESSAGE:MKHongkongConstant.MT_BIILABLE_MESSAGE);
	mtMessage.setMtActionType((hongkongMOMessage.getIsFreeMt())?MKHongkongConstant.MT_WELCOME_MESSAGE:MConstants.ACT);
	mtMessage.setUser(mkHongkongConfig.getUser());
	mtMessage.setPass(mkHongkongConfig.getPassword());	
	mtMessage.setCat(HongkongMTCat.SUBCRIBE_TYPE.getCatId());
	mtMessage.setFromStr(mkHongkongConfig.getShortcode()); 
	mtMessage.setMsisdn(hongkongMOMessage.getMsisdn());
	mtMessage.setKeyword(mkHongkongConfig.getKeyword());		
	mtMessage.setMoMessageId(hongkongMOMessage.getId());		
	mtMessage.setMoMessageIdStr(hongkongMOMessage.getMoid());  
	mtMessage.setOpId(hongkongMOMessage.getOpId());
	mtMessage.setPrice((hongkongMOMessage.getIsFreeMt())?0d:mkHongkongConfig.getPricePoint());		
	mtMessage.setTelcoId(hongkongMOMessage.getTelcoid());
	mtMessage.setTextMsg(msg);
	mtMessage.setType(MKHongkongConstant.MT_TEXT);
	mtMessage.setSenderid(hongkongMOMessage.getShortcode());
	mtMessage.setTokenId(hongkongMOMessage.getTokenId());
	mtMessage.setToken(hongkongMOMessage.getToken());
	mtMessage.setCampaignId(hongkongMOMessage.getCampaignId());
	mtMessage.setPlatform(mkHongkongConfig.getPlatform());
	mtMessage.setCharge((hongkongMOMessage.getIsFreeMt())?null:MKHongkongConstant.MT_CHARGE_SUBSCRIPTION);
	return mtMessage;
   }	
}