package net.mycomp.mcarokiosk.malaysia;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.common.service.CommonService;
import net.common.service.IDaoService;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("macroKioskMalaysiaFactoryService")
public class MacroKioskMalaysiaFactoryService extends  AbstractMacroKioskMTMessage{

	private static final Logger logger = Logger.getLogger(MacroKioskMalaysiaFactoryService.class);

	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private MalaysiaDIGIMacroKioskService malaysiaDIGIMacroKioskService;
	
	@Autowired
	private UmobileMacroKioskService umobileMacroKioskService;
	
	
	@PostConstruct
	public void init(){
		
	
	}
	
	public IMacroKioskService findMacroKioskService(int opId){
		
		IMacroKioskService macroKioskService=null;
		switch(opId){
		case MConstants.MK_MALAYSIA_DIGI_OPERATOR_ID:
			macroKioskService=malaysiaDIGIMacroKioskService;	
		break;
		case MConstants.MK_MALAYSIA_UMOBILE_OPERATOR_ID:
			macroKioskService=umobileMacroKioskService;	
		break;
		default:{
			
			}
		}
//		case MConstants.DTAC_OPERATOR_ID:
//		  macroKioskService=dtacMacroKioskService;	
//		break;
//		case MConstants.TRUEMOVE_HUTCHISON_OPERATOR_ID:
//		//	macroKioskService=truemoveHutchisonMacroKioskService;	
//		break;
//		}
		return macroKioskService;
	}
	
	
	
	@Override
	public boolean handleSubscriptionMOMessage(MalasiyaMOMessage malasiyaMOMessage) {
		
		return findMacroKioskService(malasiyaMOMessage.getOpId()).handleSubscriptionMOMessage(malasiyaMOMessage);
	}


	@Override
	public boolean sendSubscriptionRenewalRequest(SubscriberReg subscriberReg) {

		return findMacroKioskService(subscriberReg.getOperatorId()).
				sendSubscriptionRenewalRequest(subscriberReg);
	}

	@Override
	public void processDeliveryNotification(
			MalaysiaDeliveryNotification deliveryNotification) {
		  findMacroKioskService(deliveryNotification.getOpId()).
				processDeliveryNotification(deliveryNotification);
	}
	
	public static MKMalaysiaConfig findConfigByKeyWordAndTelcoId(String keyword ,int telcoId){	
		MKMalaysiaConfig mkMalaysiaConfig=null;
		try{
			
//		 mkMalaysiaConfig=MKMalaysiaConstant
//				.mapTelcoIdToMKMalaysiaConfig
//				.get(telcoId);
		 for(MKMalaysiaConfig config:MKMalaysiaConstant.listMKMalaysiaConfig){
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
