package net.mycomp.mcarokiosk.hongkong;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.web.util.UriUtils;

import net.util.MUtility;

public interface MKHongkongConstant {


	 static final Logger logger = Logger
			.getLogger(MKHongkongConstant.class.getName());
	
	
	 
	 public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		public static final DateFormat ddMMYYYYMA=new SimpleDateFormat("[dd/MM/YYYY: HH:mma]");
		public static final DateFormat dfYYYYMMDDhhmm=new SimpleDateFormat("yyyy-MM-ddhh:mm:ss");
		//public static final DateTimeFormatter ddMMYYYYMA = DateTimeFormatter.ofPattern("dd/MM/YYYY: H:ma");
		public static final String MT_BIILABLE_MESSAGE="MT_BILLABLE";
		public static final String MT_WELCOME_MESSAGE="MT_WELCOME";
		public static final String MT_UNSUB_MESSAGE="MT_UNSUBSCRIBE";
		public static final String MT_RENEWAL="HOT";
		public static final String MT_CHARGE_SUBSCRIPTION="1";
		public static final String MT_CHARGE_RENEWAL="3";
		
		public static final String SMARTONE_SUBSCRIBER_RENEWAL_CAHCE_BLOCK="SMARTONE_SUBSCRIBER_RENEWAL_CAHCE_BLOCK";
		public static final DateFormat hongkongRetryDateFormat=new SimpleDateFormat("yyyyMMdd");
		public static StringBuilder retryBillingDate=new StringBuilder(hongkongRetryDateFormat.format(
				new Timestamp(System.currentTimeMillis())));
		
		public static final String  MT_TEXT="0";
		public static final String MT_BINARY="1";
		//public static final String MT_TEXT="5";
		
	 public static AtomicInteger moMessageIdAtomicInteger=new AtomicInteger(0);
	 public static AtomicInteger mtMessageIdAtomicInteger=new AtomicInteger(0);
		
	 public final String HONGKONG_DN_CAHCHE_PREFIX="HONGKONG_DN_CAHCHE_PREFIX";		
	 public final String MT_MESSAGE_CAHCHE_PREFIX="HONGKONG_MT_MESSAGE_CAHCHE_PREFIX";
	 public final String MT_MESSAGE_COUNT_CAHCHE_PREFIX="HONGKONG_MT_MESSAGE_COUNT_CAHCHE_PREFIX";
	 public final String MT_MESSAGE_PRICE_CAHCHE_PREFIX="HONGKONG_MT_MESSAGE_PRICE_CAHCHE_PREFIX";
	 public final String MO_MESSAGE_CAHCHE_PREFIX="HONGKONG_MO_MESSAGE_CAHCHE_PREFIX";		
	 public final String HONGKONG_RETRY_BILLING_PREFIX="HONGKONG_RETRY_BILLING_PREFIX";
	 public final String HONGKONG_AD_NETWORK_CAHCHE_PREFIX="HONGKONG_AD_NETWORK_CAHCHE_PREFIX";	
	 
	 public static final String TEST_NUMBER_1="85298685648";
	 public static final String TEST_NUMBER_2="85259789813";
	 public static final String TEST_NUMBER_3="";  
	 
		
	public static final DateFormat yyyyMMddHHmmssAccessToken=new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static final List<MKHongkongConfig> listMKHongkongConfig=new ArrayList<MKHongkongConfig>();
	public static Map<Integer,MKHongkongConfig> mapIdToMKHongkongConfig=new HashMap<Integer,MKHongkongConfig>();
	 
   public static Map<Integer,MKHongkongConfig> mapServiceIdToMKHongkongConfig=new HashMap<Integer,MKHongkongConfig>();

   //public static Map<Integer,MKMalaysiaConfig> mapTelcoIdToMKMalaysiaConfig=new HashMap<Integer,MKMalaysiaConfig>();
	
	
	
	public static String md5(String str){
		try {
			 MessageDigest md = MessageDigest.getInstance("MD5");
			 md.update(str.getBytes());
			 byte digest[]=md.digest();
			 StringBuffer s = new StringBuffer();
		    	for (int i=0;i<digest.length;i++) {
		    		String hex=Integer.toHexString(0xff & digest[i]);
		   	     	if(hex.length()==1) s.append('0');
		   	          s.append(hex);
		    	}   
		    	return s.toString();
		} catch (Exception e) {			
			logger.error("Exception ",e);
		}
		return null;
	}
	
	public static String buildPurchaseUrl(String msisdn,String purchaseUrl,
			MKHongkongConfig mkhongkongConfig,String token,
			String callbackUrl,String authToken){
		
		try{
			purchaseUrl=purchaseUrl
					.replaceAll("<servicename>",MUtility.urlEncoding(mkhongkongConfig.getOpServiceName()))
					.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
					.replaceAll("<telcoid>",MUtility.urlEncoding(""+mkhongkongConfig.getTelcoId()))
					.replaceAll("<price>",MUtility.urlEncoding(""+mkhongkongConfig.getPrice()))
					.replaceAll("<subscriptioncycle>",MUtility.urlEncoding(mkhongkongConfig.getSubscriptionCycle()))
					.replaceAll("<refid>",MUtility.urlEncoding(token))
					.replaceAll("<callbackurl>",MUtility.urlEncoding(callbackUrl))
					.replaceAll("<contenturl>",MUtility.urlEncoding(mkhongkongConfig.getPortalUrl()))
					.replaceAll("<authtoken>",MUtility.urlEncoding(authToken))
					;
					
		}catch(Exception ex){
			logger.error("buildPurchaseUrl ",ex);
		}
		return purchaseUrl;
	}
	 
		
		public static String convertToHexString(String str){
			String hex=""; 
			try{
			char[]arr= str.toCharArray();
				for(char ch:arr){
					hex+=Integer.toHexString(ch | 0x10000).substring(1);
				}  
			}catch(Exception ex){
				hex=null;
			}
			return hex;
		}
		
		
		public static String convertToDateTimeFormat(){
			
			try{
			//LocalDateTime local=LocalDateTime.now();
			//TimeZone timeZone=TimeZone.getTimeZone("GMT+7");  
			//ZoneId thiaZoneId = ZoneId.of(timeZone);
			  
			// ZonedDateTime zonedDateTime = local.atZone(timeZone);
			//return "["+ddMMYYYYMA.format(zonedDateTime)+"]";
			//below cooment in 19-04-2019
			//ddMMYYYYMA.setTimeZone(TimeZone.getTimeZone("GMT+7"));
			//return ddMMYYYYMA.format(new Timestamp(System.currentTimeMillis()));
			}catch(Exception ex){
			System.out.println("error:: "+ex);	
			}
			return "";
		}
		
		public static Timestamp convertStringToTimestamp(String time){
			try {
				return new Timestamp(dfYYYYMMDDhhmm.parse(time).getTime());
			} catch (Exception e) {			
				
			}
			return null;
		}
		
		public static Timestamp getFormatUTC8Date(){			
			ZonedDateTime now = ZonedDateTime.now(ZoneOffset.of("+08:00"));		
			return Timestamp.valueOf(now.toLocalDateTime());
		}
		
	public static String getFormatUTC8TokenTime(){
			
			ZonedDateTime now = ZonedDateTime.now(ZoneOffset.of("+08:00"));	
			String dateTime=now.format(formatter);					
			return dateTime;
		}
		
	public static String findToken(String mo){
		try{
			return mo.split(" ")[2];
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}
		return mo;
	}
	
	public static String encode(String msg) {
		try {
//			return UriUtils.encode(msg.toUpperCase(),"UTf-8");
			return URLEncoder.encode( msg, "UTF-8" );
			
		}catch(Exception ex) {
			logger.error("Exception ",ex);
		}
		return msg;
	}	
		public static void main(String arg[]){
			System.out.println("getFormatUTC8Date:: "+getFormatUTC8Date());			
			String dateTime=getFormatUTC8TokenTime();
			System.out.println("dateTime:: "+dateTime);
			System.out.println("mo token :: "+findToken("ON GA "));
		}
	
}
