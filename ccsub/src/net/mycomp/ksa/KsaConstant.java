package net.mycomp.ksa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;


public interface KsaConstant {

	 static final Logger logger = Logger
			.getLogger(KsaConstant.class.getName());
	
	
	 public static final List<KsaServiceConfig>
		listKsaServiceConfig=new ArrayList<KsaServiceConfig>();
	
	public static final Map<Integer,KsaServiceConfig>
	mapServiceIdToKsaServiceConfig=new HashMap<Integer,KsaServiceConfig>();
	
	public static final Map<String,KsaServiceConfig>
	mapKsaServiceIdToKsaServiceConfig=new HashMap<String,KsaServiceConfig>();
	
	public static final String CHECK_PROFILE="CHECK_PROFILE";
	public static final String SEND_PIN="SEND_PIN";
	public static final String VALIDATE_PIN="VALIDATE_PIN";
	public static final String ACTIVATION_SMS_PUSH="ACT_SMS_PUSH";
	public static final String DCT_SMS_PUSH="DCT_SMS_PUSH";
	public static final String NOTIFICATION_SMS_PUSH="NOTI_SMS_PUSH";
	
	public static final String BULK_PUSH="BULK_PUSH";
	public static final String DCT="DCT";
	
	
	public static String getMsg(String msgTemplate,KsaServiceConfig ksaServiceConfig,String msisdn){
		try{
			return msgTemplate
					.replaceAll("<servicename>", ksaServiceConfig.getServiceName())
					.replaceAll("<portalurl>", ksaServiceConfig.getPortalUrl())
					.replaceAll("<msisdn>", msisdn);
					
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}
		return msgTemplate;
	}
	
	
	public static String getPortalUrl(String portalUrl,String msisdn){
		try{
			return portalUrl
				
					.replaceAll("<msisdn>", msisdn);
					
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}
		return portalUrl;
	}
	
	
	public static String formatMsisdn(String msisdn){
		try{
			
			if(msisdn.startsWith("+")){
				msisdn=msisdn.substring(1);
			}
			for(int i=0;i<=5;i++){
			if(msisdn.startsWith("0")){
				msisdn=msisdn.substring(1);
			}			
			}
			msisdn= msisdn.startsWith("966")?msisdn:"966"+msisdn;
			
		}catch(Exception ex){
			
		}
		return msisdn;
	}
	
	public static boolean isValidMsisdn(String msisdn){
		
		boolean valid=true;
		try{
			
			if(msisdn==null){
				valid= false;
			}
			if(msisdn.equalsIgnoreCase("966")){
				valid= false;
			}
			if(msisdn.length()!=12){
				valid= false;
			}
			if(!valid){
				return valid;
			}
			
		}catch(Exception ex){
			return false;
		}
		logger.info("isValidMsisdn::: msisdn: "+msisdn+" , valid: "+valid);
		return true;
	}
	
	
	public static int randomPIN() {
		Random r = new Random(System.currentTimeMillis());
		return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}
	
	public static void main(String arg[]){
		System.out.println(isValidMsisdn("0966880182933"));
	}
	
}
