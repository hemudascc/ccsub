package net.mycomp.beecel.jordon;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.common.service.BlockSeriesRedisCacheService;
import net.common.service.IDaoService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MUtility;

@Service("bCJordonSmsService")
public class BCJordonSmsService {


private static final Logger logger = Logger.getLogger(BCJordonSmsService.class);

@Autowired
private BlockSeriesRedisCacheService  blockSeriesRedisCacheService;

private HttpURLConnectionUtil httpURLConnectionUtil; 

@Autowired
private IDaoService daoService;

@PostConstruct
public void init(){
	httpURLConnectionUtil=new HttpURLConnectionUtil();
}




public HTTPResponse sendMTSMS(BCJordonConfig bcJordonConfig,String msisdn){
	BCOrangeMTMessage bcOrangeMTMessage = new BCOrangeMTMessage();
	try{

		if(blockSeriesRedisCacheService.isBlockSeries(Arrays.asList(msisdn))){
			bcOrangeMTMessage.setMsisdn(msisdn);
			bcOrangeMTMessage.setServiceId(bcJordonConfig.getServiceId());
			bcOrangeMTMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
			bcOrangeMTMessage.setRequestStr("Numberin block list..so not sent any message");
			return null;
		}
		bcOrangeMTMessage.setMsisdn(msisdn);
		bcOrangeMTMessage.setServiceId(bcJordonConfig.getServiceId());
		bcOrangeMTMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
		
	String msg = MUtility.urlEncoding(BCJordonConstant.CONTENT_MESSAGE
			.replaceAll("<portalurl>", bcJordonConfig.getPortalUrl()
					));	
		
	 String url=BCJordonConstant.MT_URL
		   .replaceAll("<msisdn>", msisdn)
		   .replaceAll("<shortcode>", bcJordonConfig.getShortCode())
		   .replaceAll("<msg>", msg)
		   .replaceAll("<opcode>", bcJordonConfig.getOpCode())
		   .replaceAll("<countrycode>",String.valueOf(bcJordonConfig.getCountryCode()))
		   .replaceAll("<service>", String.valueOf(bcJordonConfig.getBcServiceId()))
		   .replaceAll("<portalurl>", bcJordonConfig.getPortalUrl());
			 
	logger.info("sendMTSMS::::calling url:: "+url);
	Map<String,String> headerMap = new HashMap();
	headerMap.put("Accept-Encoding", "*");
	HTTPResponse response=httpURLConnectionUtil.sendHttpGet(url, headerMap); 
	bcOrangeMTMessage.setRequestStr(url);  
    logger.info("sendMTSMS::::calling url::response: "+url+", response: "+response.toString());
    bcOrangeMTMessage.setResponseStr(response.getResponseCode()+":" +response.getResponseStr());
	 if(response.getResponseStr()!=null){
		 bcOrangeMTMessage.setMtid(response.getResponseStr());
	 }		  
	}catch(Exception ex){
		logger.error("sendMTSMS:: ",ex);
	}finally {
		 daoService.saveObject(bcOrangeMTMessage);
	}
	return null;
}
}
