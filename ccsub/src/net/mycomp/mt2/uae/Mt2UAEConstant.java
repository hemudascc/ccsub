package net.mycomp.mt2.uae;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import net.util.JsonMapper;
import net.util.MConstants;


public interface Mt2UAEConstant {

	 static final Logger logger = Logger.getLogger(Mt2UAEConstant.class.getName());
	
	 public static Random random = new Random();
	
	 public static  int DAY_MINUTE=60*24;
	 public static final String GET_SUBSCRIPTON_OPTIONS="GetSubscriptionOptions";
	 public static final String SEND_OTP="SendOTP";
	 public static final String SUBSCRIBE="Subscribe";
	 public static final String CANCEL_SUBSCRIBE="CancelSubscription";
	 public static final String SEND_SMS="SendSMS";
	 public final String MT2_UAE_OTP_PREFIX="MT2_UAE_OTP_PREFIX";
	 
	 public final String MT2_UAE_SUB_CAHCHE_PREFIX="MT2_UAE_SUB_CAHCHE_PREFIX";
	 public final String MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX="MT2_UAE_MSISDN_TOKEN_CAHCHE_PREFIX";
	 public static final String MT2_UAE_TOKEN_TRACKINGID_PREFIX="MT2_UAE_TOKEN_TRACKINGID_PREFIX";
	 
	 public final String MT2_UAE_SEND_OTP_COUNTER_PREFIX="MT2_UAE_SEND_OTP_COUNTER_PREFIX";
	 public final String MT2_UAE_OTP_VALIDATION_COUNTER_PREFIX="MT2_UAE_OTP_VALIDATION_COUNTER_PREFIX";
	 
	public static final  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	public static final  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static AtomicInteger mt2UAEServiceApiTransIdAtomicInteger=new AtomicInteger(0);
	
	public static Map<Integer,Mt2UAEServiceConfig> mapServiceIdToMt2UAEServiceConfig
	=new HashMap<Integer,Mt2UAEServiceConfig>();
	
	public static Map<String,Mt2UAEServiceConfig> mapMt2OperatorIdMt2UAEServiceConfig
	=new HashMap<String,Mt2UAEServiceConfig>();
	
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
	
	 public static  String prepareMessage(String textTemplate, 
			 Mt2UAEServiceConfig mt2UAEServiceConfig,Integer subId,String pwd){
		 
		 String msg= textTemplate;
		 try{
		  msg= msg
				     .replaceAll("<servicename>",mt2UAEServiceConfig.getServiceName())					
					 .replaceAll("<pricepoint>", mt2UAEServiceConfig.getPricePointDesc())
					 .replaceAll("<validitydesc>", mt2UAEServiceConfig.getValidityDesc())
					 .replaceAll("<portalurl>", mt2UAEServiceConfig.getPortalUrl())
					 .replaceAll("<subid>", ""+subId)
					 .replaceAll("<pwd>", ""+pwd)
					 ;
		  
	
		 }catch(Exception ex){
			 logger.error("prepareMessage ",ex);
		 }
		 return msg;
	 }
	
	 public static String getRedirectionUrl(String getSubscriptionResponse){
		 try{
				Map responseMap=(Map)JsonMapper.getJsonToObject(getSubscriptionResponse
						, Map.class);
				if(responseMap!=null){
					Map dataObjMap=(Map)responseMap.get("Data");
					Map subscriptionFlowObjMap=(Map)dataObjMap.get("subscriptionFlowObj");
				    if(subscriptionFlowObjMap!=null){
				    	Map webFlowObjMap=(Map)subscriptionFlowObjMap.get("webFlowObj");
				    	return (String)webFlowObjMap.get("URL");
				    }	
				}
			}catch(Exception ex){
				 logger.error("getRedirectionUrl ",ex);
			}
		 return null;
		 
	 }
	 
	 public static String formatMsisdn(String msisdn,String msisdnPrefix){
		 try{
			 msisdn=msisdn.replaceAll("\\+", "");
			 
			 if(msisdn.startsWith("0")){
			  	 msisdn=msisdn.substring(1);
			 }
			 if(msisdn.startsWith("0")){
			  	 msisdn=msisdn.substring(1);
			 }
			 if(msisdn.startsWith("0")){
			  	 msisdn=msisdn.substring(1);
			 }
			 
			if(msisdn!=null&&msisdnPrefix!=null&&!msisdn.startsWith(msisdnPrefix)){
			msisdn=msisdnPrefix+msisdn;
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
	 
	 public static String getPassword(){
		 
		 return String.format("%04d", random.nextInt(10000));
	 }
public static String getStatus(Integer status){
		 if(status!=null&&status==MConstants.SUBSCRIBED){
			 return "success";
		 }
		 return "fail";
	 }
	 
	public static void main(String atg[]) throws Exception{
//		 String content = new String(Files.readAllBytes(Paths.get( 
//				 "C:\\Users\\mobitize\\Desktop\\temp\\25022020\\mt2uae.json")));
//		 
//		System.out.println(getRedirectionUrl(content));
		String msisdn=formatMsisdn("0787827332","962");
		System.out.println(msisdn);
		System.out.println(isValidMsisdn(msisdn, "962"));
		 
	}
}
