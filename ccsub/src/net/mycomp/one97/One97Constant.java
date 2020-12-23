package net.mycomp.one97;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import net.util.MUtility;

public interface One97Constant {

	 static final Logger logger = Logger
			.getLogger(One97Constant.class.getName());
	
	public static final String ONE97_OTP_PREFIX="ONE97_OTP_PREFIX";
	
	public static Map<Integer,One97Config> mapIdToOne97Config=new HashMap<Integer,One97Config>();	
	public static AtomicInteger otpPinSendIdAtomicInteger=new AtomicInteger(0);
	public static AtomicInteger otpValidationIdAtomicInteger=new AtomicInteger(0);
		
	public static String getUrl(String url,String transactionId,String msisdn,
			One97Config one97Config,String pin,String token
			){
		
		try{
			
			url= url
				.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
				.replaceAll("<transactionid>",MUtility.urlEncoding(transactionId))
				.replaceAll("<buychannel>",MUtility.urlEncoding(one97Config.getBuyChannel()))
				.replaceAll("<packname>",MUtility.urlEncoding(one97Config.getPackageName()))
				.replaceAll("<agency>",MUtility.urlEncoding(one97Config.getAgency()))
				.replaceAll("<packageid>",MUtility.urlEncoding(""+one97Config.getPackageId()))
				.replaceAll("<token>",token)
				.replaceAll("<pin>",MUtility.urlEncoding(pin))
				;
			
			
		}catch(Exception ex){
			logger.error("getMessage:: ",ex);
		}	
		return url;
	}
	
	
	public static String[] parse(String str){
		
		try{
			return str.split("\\|");
			
			
		}catch(Exception ex){
			logger.error("parse:: ",ex);
		}	
		return null;
	}
	
	public static void main(String arg[]){
		String str="0|306|You have successfully subscribed in gameland.You are in Pending State.|971565217461|mobetisalat|dubai||gameland|2|PENDING|2019-07-18 19:48:18|2019-07-25 19:48:18|2019-07-18 19:48:18|1100|0|unknown|";
		String arr[]=parse(str);
		System.out.println("arr "+arr[0]+", "+arr[1]);
	}
	
}
