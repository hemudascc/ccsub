package net.mycomp.mt2.zain.iraq;

import java.sql.Timestamp;
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


public interface Mt2ZainIraqConstant {

	 static final Logger logger = Logger
			.getLogger(Mt2ZainIraqConstant.class.getName());
	
	 public static final List<Mt2ZainIraqIpPool> listIPPool=new ArrayList<Mt2ZainIraqIpPool>();
	 
	 public static final String MT2_ZAIN_IRAQ_SOURCE_CACHE_PREFIX="MT2_ZAIN_IRAQ_SOURCE_CACHE_PREFIX";
	 public static final String MT2_ZAIN_IRAQ_UNIQUEID_CACHE_PREFIX="MT2_ZAIN_IRAQ_UNIQUEID_CACHE_PREFIX";
	 public static final String MT2_ZAIN_IRAQ_SUB_CAHCHE_PREFIX="MT2_ZAIN_IRAQ_SUB_CAHCHE_PREFIX";
	 
	 public static final String MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX="MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX";
	
	 public static final String MT2_ZAIN_IRAQ_ACT_CACHE_PREFIX="MT2_ZAIN_IRAQ_MSISDN_TOKEN_CACHE_PREFIX";
	 
	 public static final  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	public static final  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static AtomicInteger mt2ZainIraqApiTranAtomicInteger=new AtomicInteger(0);
	
	public static Map<Integer,Mt2ZainIraqServiceConfig> mapServiceIdToMt2ZainIrqServiceConfig
	=new HashMap<Integer,Mt2ZainIraqServiceConfig>();
	
	public static Map<String,Mt2ZainIraqServiceConfig> mapZainIraqServiceIdToMt2ZainIrqServiceConfig
	=new HashMap<String,Mt2ZainIraqServiceConfig>();
	
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
	
	 public static  String prepareMessage(String textTemplate,Mt2ZainIraqServiceConfig 
			 mt2ZainIraqServiceConfig,String subId){
		 
		 String msg= textTemplate;
		 try{
		  msg= msg
				     .replaceAll("<servicename>",mt2ZainIraqServiceConfig.getServiceName())					
					 .replaceAll("<pricepoint>", mt2ZainIraqServiceConfig.getPricePointDesc())
					 .replaceAll("<validitydesc>", mt2ZainIraqServiceConfig.getValidityDesc())
					 .replaceAll("<portalurl>", mt2ZainIraqServiceConfig.getPortalUrl())
					 .replaceAll("<subid>", ""+subId)
					 ;
		  
	
		 }catch(Exception ex){
			 logger.error("prepareMessage ",ex);
		 }
		 return msg;
	 }
	 
	 public static Mt2ZainIraqIpPool findMt2ZainIraqOperatorByIp(String ip) {
		 Mt2ZainIraqIpPool ippool=null;
			try{
			 ippool=listIPPool.stream().filter(p->p!=null&&
					p.getSubnetUtils()!=null&&p.getSubnetUtils().getInfo().
	     			isInRange(ip)).findFirst().orElse(null);	
			}catch(Exception ex){
				logger.error("findMt2ZainIraqOperatorByIp:: ",ex);
			}
			return ippool;		
		}
	 
	 
	
	public static void main(String atg[]){
	//	System.out.println(formatDate(new Timestamp(System.currentTimeMillis())));
	//	System.out.println(getFormatUTC2Date());
	}
}
