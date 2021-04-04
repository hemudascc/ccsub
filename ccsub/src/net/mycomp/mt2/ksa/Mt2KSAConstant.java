package net.mycomp.mt2.ksa;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;


public interface Mt2KSAConstant {

	 static final Logger logger = Logger
			.getLogger(Mt2KSAConstant.class.getName());
	
	 public static final String MT2_KSA_MSISDN_TOKEN_CACHE_PREFIX="MT2_KSA_MSISDN_TOKEN_CACHE_PREFIX";
	 public static final String MT2_KSA_TOKEN_TRACKINGID_PREFIX="MT2_KSA_TOKEN_TRACKINGID_PREFIX";
	 
	public static final String SMS_CONTENT_ALERT="SMS_CONTENT_ALERT";
	public static final String VALIDATE_OTP="VALIDATE_OTP";
	public static final String MT2_KSA_MSISDN_TOKEN_CAHCHE_PREFIX="MT2_KSA_MSISDN_TOKEN_CAHCHE_PREFIX";
	 public static final  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	public static final  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static AtomicInteger mt2KSAServiceApiTransIdAtomicInteger=new AtomicInteger(0);
	
	public static final String MT2KSA_OTP_TRXID_PREFIX="MT2KSA_OTP_TRXID_PREFIX";
	public static final String NOUNCE_PIN="MT2PAYKSASUBPIN";
	public static final String NOUNCE_CHARGE="MT2PAYKSACHARGE";
	public static Map<Integer,Mt2KSAServiceConfig> mapServiceIdToMt2KSAServiceConfig
	=new HashMap<Integer,Mt2KSAServiceConfig>();
	
	public static Map<String,Mt2KSAServiceConfig> mapMt2ServiceIdToMt2KSAServiceConfig
	=new HashMap<String,Mt2KSAServiceConfig>();
	
	public static String getFormatUTCDate(Timestamp ts){
		ZonedDateTime now = ZonedDateTime.of(ts.toLocalDateTime(), ZoneOffset.UTC);		
		return simpleDateFormat.format(Timestamp.valueOf(now.toLocalDateTime()));
	}
	
	
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
	
	 public static  String prepareMessage(String textTemplate,Mt2KSAServiceConfig 
			 mt2KSAServiceConfig,Integer subId){
		 
		 String msg= textTemplate;
		 try{
		  msg= msg
				     .replaceAll("<servicename>",mt2KSAServiceConfig.getServiceName())					
					 .replaceAll("<pricepoint>", mt2KSAServiceConfig.getPricePointDesc())
					 .replaceAll("<validitydesc>", mt2KSAServiceConfig.getValidityDesc())
					 .replaceAll("<portalurl>", mt2KSAServiceConfig.getPortalUrl())
					 .replaceAll("<subid>", ""+subId)
					 ;
		  
	
		 }catch(Exception ex){
			 logger.error("prepareMessage ",ex);
		 }
		 return msg;
	 }
	 
	 
	 public static void parseNotificationData(Mt2KSANotification mt2KSANotification){
			try{
				String str[]=mt2KSANotification.getData().split(",");
				mt2KSANotification.setMt2Action(str[0]);
				mt2KSANotification.setMt2ServiceId(str[1]);
				if(str.length>=3){
				  mt2KSANotification.setMt2Period(str[2]);
				}
				
			}catch(Exception ex){
				logger.error("Exception ",ex);
			}
			
		}
	public static void main(String atg[]){
	//	System.out.println(formatDate(new Timestamp(System.currentTimeMillis())));
	//	System.out.println(getFormatUTC2Date());
	}
}
