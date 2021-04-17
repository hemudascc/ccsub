package net.mycomp.mcarokiosk.hongkong;

import java.net.URLEncoder;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.service.BlockSeriesRedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

@Service("hongkongSmsService")
public class HongkongSmsService {

private static final Logger logger = Logger.getLogger(HongkongSmsService.class);

@Autowired
private BlockSeriesRedisCacheService  blockSeriesRedisCacheService;

private HttpURLConnectionUtil httpURLConnectionUtil; 

@PostConstruct
public void init(){
	httpURLConnectionUtil=new HttpURLConnectionUtil();
}


 

public HTTPResponse sendMTSMS(String url,HongkongMTMessage hongkongMTMessage){
	try{

		if(blockSeriesRedisCacheService.isBlockSeries(Arrays.asList(hongkongMTMessage.getMsisdn()))){
			hongkongMTMessage.setRequestUrl("Numberin block list..so not sent any message");
			return null;
		}
	
	url=url+"?User="+MUtility.urlEncoding(hongkongMTMessage.getUser())
			+"&Pass="+MUtility.urlEncoding(hongkongMTMessage.getPass())
			+"&Type="+MUtility.urlEncoding(hongkongMTMessage.getType())
			+"&To="+MUtility.urlEncoding(hongkongMTMessage.getMsisdn())
			+"&From="+MUtility.urlEncoding(hongkongMTMessage.getFromStr())
			+"&Text="+MUtility.urlEncoding(hongkongMTMessage.getTextMsg())
			+"&Telcoid="+MUtility.urlEncoding(""+hongkongMTMessage.getTelcoId())
			+"&Platform="+MUtility.urlEncoding(hongkongMTMessage.getPlatform())
			+"&Price="+MUtility.urlEncoding(""+hongkongMTMessage.getPrice().intValue())
			;
			 
	logger.info("sendMTSMS::::calling url:: "+url);
	HTTPResponse response=httpURLConnectionUtil.sendGet(url); 
	hongkongMTMessage.setRequestUrl(url);
    logger.info("sendMTSMS::::calling url::response: "+url+", response: "+response);
    hongkongMTMessage.setResponse(response.getResponseCode()+":" +response.getResponseStr());
	 if(response.getResponseStr()!=null){
		 String arr[]=response.getResponseStr().split(",");
		 if(arr.length==3&&arr[2].equalsIgnoreCase("200")){
			 hongkongMTMessage.setMsgId(arr[1]);
		 }
	 }		  
	}catch(Exception ex){
		logger.error("sendMTSMS:: ",ex);
	}
	return null;
}
}
