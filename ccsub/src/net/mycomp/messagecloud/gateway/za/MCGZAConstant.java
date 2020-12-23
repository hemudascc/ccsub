package net.mycomp.messagecloud.gateway.za;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

import net.util.MConstants;

public interface MCGZAConstant {

	 static final Logger logger = Logger
			.getLogger(MCGZAConstant.class);

	public static final int PRODUCT_ID=11;
	public static final String  CHECK_SUB="checksub";
	
	public static final int SERVICE_ID=26;
	
	public static final String INVALID_KEY="INVALID";
	public static final String SUB_KEY="SUB";
	public static final String UNSUB_KEY="UNSUB";
	
	public final static String CONTENT_MSG_TYPE="CONTENT";
	public final static String BILLLED_MSG_TYPE="BILLED";
	
	public static List<MCGZAServiceConfig> listMCGZAServiceConfig=new ArrayList<MCGZAServiceConfig>();
	
	public static Map<Integer,MCGZAServiceConfig> mapServiceIdToMCGZAServiceConfig=new HashMap<Integer,MCGZAServiceConfig>();
	
	public static String findAction(String text){
		try{
		   String msg[]=text.split(" ");
		   if(text.toLowerCase().contains(UNSUB_KEY)){
			   return MConstants.DCT;
		   }
		   if(text.toLowerCase().equalsIgnoreCase(SUB_KEY)){
			   return MConstants.ACT;
		   }
		}catch(Exception ex){
		}
		return INVALID_KEY;
	}
	
	
	
	
		
	public static String getMsg(String msgTemplate,MCGZAServiceConfig mcgServiceConfig,
			Double amount,Integer validity,String  msisdn,String portalUrl){
		
		try{
		
	   if(msgTemplate==null){
			return null;
		}
		return msgTemplate.replaceAll("<currency>", Objects.toString(mcgServiceConfig.getCurrency()))
		 .replaceAll("<servicename>", Objects.toString(mcgServiceConfig.getServiceName()))
		 .replaceAll("<amount>", Objects.toString(amount))
		 .replaceAll("<validity>", Objects.toString(validity))
		 .replaceAll("<shortcode>", Objects.toString(mcgServiceConfig.getShortCode()))
		 .replaceAll("<portalurl>",Objects.toString(portalUrl))
         .replaceAll("<msisdn>",Objects.toString(msisdn));
		
		}catch(Exception ex){
			logger.error("getMsg::: ",ex);
		}
		return msgTemplate;
	}

}
