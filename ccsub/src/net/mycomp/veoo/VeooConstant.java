package net.mycomp.veoo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.persist.bean.SubscriberReg;
import net.util.MConstants;

public interface VeooConstant {

	public static Map<Integer,VeooServiceConfig> mapIdToVeooServiceConfig=
			new HashMap<Integer,VeooServiceConfig>();
	
	public static Map<Integer,VeooServiceConfig> mapServiceIdToVeooServiceConfig=
			new HashMap<Integer,VeooServiceConfig>();
	
	public static Map<String,VeooServiceConfig> mapVeooServiceIdToVeooServiceConfig=
			new HashMap<String,VeooServiceConfig>();
	
	public static AtomicInteger automicIdVeooMTMessage=new AtomicInteger(0);
	
	public static String SUCCESS="success"; 
	public static String PIN_VALIDATION_CACHE_PREFIX="PIN_VALIDATION_CACHE_PREFIX";
	
	public static String findAction(
			VeooDeliveryReceipt veooDeliveryReceipt,VeooMtMessage veooMtMessage){
		try{
			if(veooDeliveryReceipt.getStatusText().equalsIgnoreCase("VEOO_DELIVERED")){
				if(veooMtMessage.getAction().equalsIgnoreCase(MConstants.ACT)){
					return MConstants.ACT;
				}else if(veooMtMessage.getAction().equalsIgnoreCase(MConstants.RENEW)){
					return MConstants.RENEW;
				}
			}			
		}
		catch(Exception ex){
			
		}
		return "";
	}
	
	public static String formatMsisdn(String msisdn,String countryCode){
		try{
			//07717607998
//			if(msisdn.startsWith("+")){
//				msisdn=msisdn.substring(1);
//			}
			msisdn=msisdn.replaceAll("\\+", "");
			if(msisdn.startsWith("0")){
				msisdn=msisdn.substring(1);
			}
				msisdn=msisdn.replaceAll(" ", "");
		
			if(countryCode!=null){
			msisdn= msisdn.startsWith(countryCode)?msisdn:countryCode+msisdn;
			}
		}catch(Exception ex){
			
		}
		return msisdn;
	}
	
	public static void main(String arg[]){
		System.out.println(formatMsisdn("++97915   200","504"));
	}
	
}
