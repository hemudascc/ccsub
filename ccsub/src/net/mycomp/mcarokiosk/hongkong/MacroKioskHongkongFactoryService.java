package net.mycomp.mcarokiosk.hongkong;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.util.MConstants;

@Service("macroKioskHongkongFactoryService")
public class MacroKioskHongkongFactoryService extends  AbstractMacroKioskMTMessage{

	private static final Logger logger = Logger.getLogger(MacroKioskHongkongFactoryService.class);
	
	@Autowired
	private HongkongSmatoneMacroKioskService hongkongSmartoneMacroKioskService;
	
	@Autowired
	private HutchisonMacroKioskService hongkongHutchisonMacroKioskService;
	
	
	@PostConstruct
	public void init(){
		
	
	}
	
	public IMacroKioskService findMacroKioskService(int opId){
		
		IMacroKioskService macroKioskService=null;
		switch(opId){
		case MConstants.MK_HONGKONG_SMARTONE_OPERATOR_ID:
			macroKioskService=hongkongSmartoneMacroKioskService;	
		break;
		case MConstants.MK_HONGKONG_HUTCHISON_OPERATOR_ID:
			macroKioskService=hongkongHutchisonMacroKioskService;	
		break;
		default:{
			
			}
		}
		return macroKioskService;
	}
	
	
	
	@Override
	public boolean handleSubscriptionMOMessage(HongkongMOMessage hongkongMOMessage) {
		
		return findMacroKioskService(hongkongMOMessage.getOpId()).handleSubscriptionMOMessage(hongkongMOMessage);
	}


	@Override
	public void processDeliveryNotification(
			HongkongDeliveryNotification deliveryNotification) {
		  findMacroKioskService(deliveryNotification.getOpId()).
				processDeliveryNotification(deliveryNotification);
	}
	
	public static MKHongkongConfig findConfigByKeyWordAndTelcoId(String keyword ,int telcoId){	
		MKHongkongConfig mkMalaysiaConfig=null;
		try{
		 for(MKHongkongConfig config:MKHongkongConstant.listMKHongkongConfig){
				if(config.getTelcoId().intValue()==telcoId&&
						config.getKeyword().toUpperCase().contains(keyword.toUpperCase())){
					mkMalaysiaConfig=config;
					break;
				}
			}		
		}catch(Exception ex){
			logger.error("Exception:: ",ex);
		}
		 return mkMalaysiaConfig;
		 
	}
}
