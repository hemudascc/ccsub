package net.mycomp.comviva.ooredo.oman;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import net.mycomp.mt2.uae.Mt2UAEConstant;
import net.mycomp.mt2.uae.Mt2UAEServiceConfig;
import net.util.MConstants;
import net.util.MUtility;

public interface OoredooOmanConstant {

	 static final Logger logger = Logger
				.getLogger(OoredooOmanConstant.class.getName());
		
	 public static SimpleDateFormat sdfDDMMyyyyHHmmss= new SimpleDateFormat("yyMMddHHmmss");
public static Map<Integer,OoredooOmanServiceConfig> mapCCServiceIdToOoreodoOmanServiceConfig
=new HashMap<Integer,OoredooOmanServiceConfig>();

public static Map<String,OoredooOmanServiceConfig> mapServiceIdToOoreodoOmanServiceConfig
=new HashMap<String,OoredooOmanServiceConfig>();


public AtomicInteger ooredooOmanOCSLogDetailId=new AtomicInteger(0);
public static final String OOREDO_OMAN_CACHE_TRANS_PREFIX_ID="OOREDO_OMAN_CACHE_TRANS_PREFIX_ID";

public static final String SNED_PIN="SNED_PIN";
public static final String PIN_VALIDATION="PIN_VALIDATION";
public static final String UNSUB="UNSUB";
public static final String SEND_MT="SEND_MT";

public static String getPortalUrl(String portalUrl,String msisdn,Integer subId){
	try{
		return portalUrl
				.replaceAll("<msisdn>", msisdn)
				.replaceAll("<subid>",""+subId);					
	}catch(Exception ex){
		logger.error("Exception ",ex);
	}
	return portalUrl;
}

 public static  String prepareMessage(String textTemplate, 
		 OoredooOmanServiceConfig ooredooOmanServiceConfig,Integer subId){
	 
	 String msg= textTemplate;
	 try{
	  msg= msg
			    
			     .replaceAll("<productname>",ooredooOmanServiceConfig.getServiceName())
				 .replaceAll("<amount>", ooredooOmanServiceConfig.getPricePointDesc())
				 .replaceAll("<days>", ooredooOmanServiceConfig.getValidityDesc())
				 .replaceAll("<unsubkeyword>", ooredooOmanServiceConfig.getUnsubKeyword())
				 .replaceAll("<shortcode>",ooredooOmanServiceConfig.getShortCode())
				 .replaceAll("<portalurl>", ooredooOmanServiceConfig.getPortalUrl())
				  .replaceAll("<subid>", ""+subId)
				 ;
	  

	 }catch(Exception ex){
		 logger.error("prepareMessage ",ex);
	 }
	 return msg;
 }
 
 public static String formatMsisdn(String msisdn){
	 try{
		 msisdn=msisdn.replaceAll("\\+", "");
		if(msisdn!=null&&"968"!=null&&!msisdn.startsWith("968")){
		msisdn="968"+msisdn;
		}
	 }catch(Exception ex){
		 logger.error("formatMsisdn ",ex);
	 }
		return msisdn;
	}
 
 public static boolean isValidMsisdn(String msisdn,String msisdnPrefix){
		boolean valid=true;
		try{
			
			if(msisdn==null){
				valid= false;
			}
			if(msisdn.equalsIgnoreCase(msisdnPrefix)){
				valid= false;
			}
			
			if(!msisdn.startsWith(msisdnPrefix)){
				valid= false;
			}
			//971544576350
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
 
 
 public static String findAction(String operationId){
		
		if(operationId==null){
			return "";
		}
		String action="";		
		switch(operationId){		
		
		case "SN":
		case "RN":
		//case "SP":
		{
			action=MConstants.ACT;
			break;
		}
		case "YR":
		case "GR":
		case "RR":
		case "SR":
		{
			action=MConstants.RENEW;
			break;
		}
		case "SS":
		{
			action=MConstants.DCT;
			break;
		}
		
		case "YG":
		case "SP":{			
			action=MConstants.GRACE;
			break;
		}
		
		case "YS":
		case "GS":
		case "RS":
		case "PS":	
		case "AS":	
		{
			action=MConstants.CHURN;
			break;
		}
		case "SH":
		{
			action=MConstants.BLOCKED;
			break;
		}
	}
	return action;	
	}
 
 public static void main(String arg[]) throws Exception{
	  Timestamp ts=new Timestamp(System.currentTimeMillis());
	  System.out.println("ts:: "+ts+" , formated time :: "+sdfDDMMyyyyHHmmss.format(ts));
	  System.out.println("url encoding: "+("FXRKZrIY UNd4NzUSSbNlA==".replaceAll(" ","+")));
 }

}
