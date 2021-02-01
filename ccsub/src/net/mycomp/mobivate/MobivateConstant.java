package net.mycomp.mobivate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import net.mycomp.oredoo.kuwait.OredooKuwaitServiceConfig;
import net.util.MConstants;

import org.apache.log4j.Logger;

public interface MobivateConstant {

	 static final Logger logger = Logger
			.getLogger(MobivateConstant.class.getName());
	
	 public static AtomicInteger smsTransAtomicInteger=new AtomicInteger(0);
	 public static final String CG_CALLBACK_PREFIX="MOBVIATE_CG_CALLBACK_PREFIX";
	 public static final String TOKEN_MSISDN_CHACHE_PREFIX="MOBIVATE_TOKEN_MSISDN_CHACHE_PREFIX";
	 
		public final  List<String> unsubKeywordList=Arrays.asList("STOP","END","CANCEL","UNSUBSCRIBE");
		public final  List<String> subKeywordList=Arrays.asList("START");
		
	 public final  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 
	public static Map<Integer,MobivateServiceConfig> mapServiceIdToMobivateServiceConfig
	=new HashMap<Integer,MobivateServiceConfig>();
	
	public static final Map<String,MobivateServiceConfig> mapProductIdToMobivateCellcConfig=
			new HashMap<String,MobivateServiceConfig>();
	
	//public final String CG_STTAUS_SUCCESS="success";
	public final String CG_STTAUS_SUCCESS="1";
	public final String CG_STTAUS_FAILED="failed";
	public static final String  MT_BILLED_MSG="MT_BILLED_MSG";
	public final String BILLED_SMS="BILLED_SMS";
	public final String FREE_SMS="FREE_SMS";
	public static final String SUCCESS="success";
	public static final String MSG="success";
	public final static SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final AtomicInteger mobivateApiTransId=new AtomicInteger(1);
	
	 public static  Timestamp formatTimestamp(String dtStr){
		 try{
			return  new Timestamp(simpleDateFormat.parse(dtStr).getTime());
		 }catch(Exception ex){
				logger.error("formatServiceOfferDate:: ",ex);
			}
			return new Timestamp(System.currentTimeMillis());
	 }
	 
	 public static  String getFormatedTime(Timestamp timestamp){		
			try{		
				//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");				
				return simpleDateFormat.format(timestamp);
				
			}catch(Exception ex){}
			return null;
		}
	 
	 public static String formatedDate(Timestamp ts) {
			//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-DD HH:mm:SS");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
			return sdf.format(ts);
			
		}
	 
	 public static  Timestamp getTimeByZone(String timeZone){		
			try{
				//return Timestamp.valueOf(LocalDateTime.now(ZoneId.of(timeZone)));
				
			}catch(Exception ex){}
			return new Timestamp(System.currentTimeMillis());
		}
	 
	 
	 public static String findAction(String textMsg){
			boolean isUnsubExit=MobivateConstant.unsubKeywordList.stream()
					.anyMatch(a->textMsg.toLowerCase().contains(a.toLowerCase()));
			if(isUnsubExit){
				return MConstants.DCT;
			}
			
			boolean isSUBExit=MobivateConstant.subKeywordList.stream()
					.anyMatch(a->textMsg.toLowerCase().contains(a.toLowerCase()));
			if(isSUBExit){
				return MConstants.ACT;
			}
			return "";
			
		}
	 
	 public static String getPortal(String portalUrl,String msisdn,Integer subId){
			try{
				return portalUrl
						.replaceAll("<msisdn>", msisdn)
						.replaceAll("<subid>",""+ subId)
					;
			}catch(Exception ex){
				logger.error("getPortal ",ex);
			}
			return portalUrl;
		}
	 
}
