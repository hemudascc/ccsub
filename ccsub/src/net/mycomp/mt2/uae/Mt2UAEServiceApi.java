package net.mycomp.mt2.uae;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.jpa.repository.JPASubscriberReg;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MUtility;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("mt2UAEServiceApi")
public class Mt2UAEServiceApi  {

	private static final Logger logger = Logger
			.getLogger(Mt2UAEServiceApi.class.getName());
	

	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService; 
	
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	private static String getSha1Hash(String data){
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes("UTF-8"));
			return Hex.encodeHexString(md.digest());
		}catch(Exception ex){
			logger.error("getSha1Hash ",ex);
		}
		return null;
		}
	
	private String getSignature(String apiName,SortedMap<String,String> headerMap,String secretKey){
		String data=apiName;
		Iterator<String> itr=headerMap.keySet().iterator();
		while(itr.hasNext()){
			data+=headerMap.get(itr.next());
		}
		data+=secretKey;
		data=data.toUpperCase();
		return getSha1Hash(data);
	}
	
	public MT2UAEServiceApiTrans getSubscription(Mt2UAEServiceConfig mt2UAEServiceConfig,
			String ip,String token){
		
		MT2UAEServiceApiTrans mt2UAEServiceApiTrans=new MT2UAEServiceApiTrans(true, Mt2UAEConstant.GET_SUBSCRIPTON_OPTIONS);
		try{
			mt2UAEServiceApiTrans.setServiceId(mt2UAEServiceConfig.getServiceId());
			SortedMap<String,String> headerMap=new TreeMap<String,String>();
			mt2UAEServiceApiTrans.setToken(token);
			headerMap.put("ClientId",mt2UAEServiceConfig.getClientId());
			headerMap.put("LanguageCode", "en");
			headerMap.put("MNC", mt2UAEServiceConfig.getMnc());
			headerMap.put("MCC", mt2UAEServiceConfig.getMcc());
			headerMap.put("AppVersion", "1.2");
			headerMap.put("PlatformTag", "1");
			headerMap.put("OSVersion", "");
			headerMap.put("ChannelTag", "WAP");
			headerMap.put("LocationCoordinates", ip);
			headerMap.put("DeviceIdentifier", "2");
			headerMap.put("DeviceName", "");
			headerMap.put("RequestId", ""+mt2UAEServiceApiTrans.getId());		
			headerMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
			headerMap.put("Context", "");
			headerMap.put("Signature", getSignature(Mt2UAEConstant.GET_SUBSCRIPTON_OPTIONS, 
					headerMap, mt2UAEServiceConfig.getSecretKey()));
			
			//SENDOTP1.2WAP91122S9EN11,22430029715689672342.201.311223323KK!NGD0M
			headerMap.put("Content-Type", "application/json");
			headerMap.put("Accept", "application/json");
			
			SortedMap<String,String> dataMap=new TreeMap<String,String>();
			String dataJson=JsonMapper.getObjectToJson(dataMap);
			
			mt2UAEServiceApiTrans.setRequest(mt2UAEServiceConfig.getGetSubscriptionUrl()
					+",data: "+dataJson+" : header: "+headerMap.toString());
			HTTPResponse httpResponse=httpURLConnectionUtil
					.makeHTTPPOSTRequest(mt2UAEServiceConfig.getGetSubscriptionUrl(), dataJson, headerMap);
			mt2UAEServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
			mt2UAEServiceApiTrans.setResponse(httpResponse.getResponseStr());
			if(mt2UAEServiceApiTrans.getResponseCode()==200){
				mt2UAEServiceApiTrans.setResponseToCaller(true);
			}
			
		}catch(Exception ex){
			logger.error("getSubscription ",ex);
		}finally{
			daoService.updateObject(mt2UAEServiceApiTrans);
		}
		return mt2UAEServiceApiTrans;
		
	}
	
	

	
public MT2UAEServiceApiTrans sendOTP(Mt2UAEServiceConfig mt2UAEServiceConfig,String msisdn,String ip,String token){
		
		MT2UAEServiceApiTrans mt2UAEServiceApiTrans=new MT2UAEServiceApiTrans(true, Mt2UAEConstant.SEND_OTP);
		try{
			mt2UAEServiceApiTrans.setToken(token);
			mt2UAEServiceApiTrans.setServiceId(mt2UAEServiceConfig.getServiceId());
			 msisdn=Mt2UAEConstant.formatMsisdn(msisdn
					,mt2UAEServiceConfig.getMsisdnPrefix());
			mt2UAEServiceApiTrans.setMsisdn(msisdn);
			if(!Mt2UAEConstant.isValidMsisdn(msisdn, mt2UAEServiceConfig.getMsisdnPrefix())){
				mt2UAEServiceApiTrans.setRequest("Invalid Msisdn");
				return mt2UAEServiceApiTrans;
			}
			long counter=redisCacheService.getAndIcrementIntValue(
					Mt2UAEConstant.MT2_UAE_SEND_OTP_COUNTER_PREFIX+msisdn, 0,Mt2UAEConstant.DAY_MINUTE);
			if(counter>2){
				mt2UAEServiceApiTrans.setRequest("Send OTP counter to max "+counter);
				return mt2UAEServiceApiTrans;
			}
			
			SortedMap<String,String> headerMap=new TreeMap<String,String>();
			headerMap.put("AppVersion", "1.2");
			headerMap.put("ChannelTag", "WAP");
			headerMap.put("ClientId",mt2UAEServiceConfig.getClientId());
			headerMap.put("DeviceIdentifier", "2");
			headerMap.put("DeviceName", "");
			headerMap.put("LanguageCode", "en");
			headerMap.put("MNC", mt2UAEServiceConfig.getMnc());
			headerMap.put("MCC", mt2UAEServiceConfig.getMcc());
			
			headerMap.put("OSVersion", "");
			
			headerMap.put("Content-Type", "application/json");
			
			
			headerMap.put("PlatformTag", "1.3");
			
			headerMap.put("LocationCoordinates", ip);
			headerMap.put("RequestId", ""+mt2UAEServiceApiTrans.getId());
		
			headerMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
			headerMap.put("Context", "");
			
			
			Map<String,String> dataMap=new HashMap<String,String>();
			dataMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
			dataMap.put("MSISDN", msisdn);
			dataMap.put("OTP", "");
			String dataJson=JsonMapper.getObjectToJson(dataMap);
			headerMap.put("Signature", getSignature(Mt2UAEConstant.SEND_OTP, 
					headerMap, mt2UAEServiceConfig.getSecretKey()));
			
			mt2UAEServiceApiTrans.setRequest(mt2UAEServiceConfig.getSendOtpUrl()
					+":"+dataJson+" : "+headerMap.toString());
			
			HTTPResponse httpResponse=httpURLConnectionUtil
					.makeHTTPPOSTRequest(mt2UAEServiceConfig.getSendOtpUrl(), dataJson, headerMap);
			
			mt2UAEServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
			mt2UAEServiceApiTrans.setResponse(httpResponse.getResponseStr());
			//{"Data":{"RequestId":168},"Result":{"Action":"OK","Message":"Success","ResponseCode":1}}
			if(mt2UAEServiceApiTrans.getResponseCode()==200){
				mt2UAEServiceApiTrans.setResponseToCaller(true);
				Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				Map resultMap=(Map)map.get("Result");
				if(Objects.toString(resultMap.get("Action")).equalsIgnoreCase("OK")
						&&Objects.toString(resultMap.get("Message")).equalsIgnoreCase("Success")){
					mt2UAEServiceApiTrans.setSuccess(true);
					redisCacheService.putObjectCacheValueByEvictionMinute(Mt2UAEConstant.
							MT2_UAE_OTP_PREFIX+msisdn, 
							Objects.toString(((Map)map.get("Data")).get("RequestId")), 
							10);
				}
			}
			
		}catch(Exception ex){
			logger.error("getSubscription ",ex);
		}finally{
			daoService.updateObject(mt2UAEServiceApiTrans);
		}
		return mt2UAEServiceApiTrans;
	}
	
public MT2UAEServiceApiTrans subscribe(Mt2UAEServiceConfig mt2UAEServiceConfig,String msisdn,String otp,String token){
	
	MT2UAEServiceApiTrans mt2UAEServiceApiTrans=new MT2UAEServiceApiTrans(true, Mt2UAEConstant.SUBSCRIBE);
	
	try{
		mt2UAEServiceApiTrans.setToken(token);
		mt2UAEServiceApiTrans.setServiceId(mt2UAEServiceConfig.getServiceId());
		 msisdn=Mt2UAEConstant.formatMsisdn(msisdn
					,mt2UAEServiceConfig.getMsisdnPrefix());
		mt2UAEServiceApiTrans.setMsisdn(msisdn);
		if(!Mt2UAEConstant.isValidMsisdn(msisdn, mt2UAEServiceConfig.getMsisdnPrefix())){
			mt2UAEServiceApiTrans.setRequest("Invalid Msisdn");
			return mt2UAEServiceApiTrans;
		}
		long counter=redisCacheService.getAndIcrementIntValue(
				Mt2UAEConstant.MT2_UAE_OTP_VALIDATION_COUNTER_PREFIX+msisdn, 0,Mt2UAEConstant.DAY_MINUTE);
		if(counter>2){
			mt2UAEServiceApiTrans.setRequest("subscribe counter to max "+counter);
			return mt2UAEServiceApiTrans;
		}
		
		SortedMap<String,String> headerMap=new TreeMap<String,String>();
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("ClientId",mt2UAEServiceConfig.getClientId());
		headerMap.put("LanguageCode", "en");
		headerMap.put("MNC", mt2UAEServiceConfig.getMnc());
		headerMap.put("MCC", mt2UAEServiceConfig.getMcc());
		headerMap.put("AppVersion", "1.2");
		headerMap.put("PlatformTag", "2");
		//headerMap.put("OSVersion", "1.4");
		headerMap.put("ChannelTag", "WAP");
		//headerMap.put("LocationCoordinates", value);
		headerMap.put("DeviceIdentifier", "2");
		//headerMap.put("DeviceName", value);
		headerMap.put("RequestId", ""+mt2UAEServiceApiTrans.getId());
		
		headerMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
		//headerMap.put("Context", value);
		
		Map<String,String> dataMap=new HashMap<String,String>();
		dataMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
		dataMap.put("MSISDN", msisdn);
		dataMap.put("UserIdentifier", Objects.toString(redisCacheService
				.getObjectCacheValue(Mt2UAEConstant.MT2_UAE_OTP_PREFIX+msisdn)));//otp identifier
		
		dataMap.put("SubscriptionOptionTag", "111");
		dataMap.put("OTP", otp);
		
		String dataJson=JsonMapper.getObjectToJson(dataMap);
		headerMap.put("Signature", getSignature(Mt2UAEConstant.SUBSCRIBE, 
				headerMap, mt2UAEServiceConfig.getSecretKey()));
		
		mt2UAEServiceApiTrans.setRequest(mt2UAEServiceConfig.getSubscribeUrl()
				+":"+dataJson+" : "+headerMap.toString());
		
		HTTPResponse httpResponse=httpURLConnectionUtil
				.makeHTTPPOSTRequest(mt2UAEServiceConfig.getSubscribeUrl(), dataJson, headerMap);
		mt2UAEServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2UAEServiceApiTrans.setResponse(httpResponse.getResponseStr());
		
		if(mt2UAEServiceApiTrans.getResponseCode()==200){
			mt2UAEServiceApiTrans.setResponseToCaller(true);
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			Map resultMap=(Map)map.get("Result");
			if(Objects.toString(resultMap.get("Action")).equalsIgnoreCase("OK")
					&&(Objects.toString(resultMap.get("Message")).equalsIgnoreCase("Success")
							||Objects.toString(resultMap.get("Message")).equalsIgnoreCase("Verified"))){
				mt2UAEServiceApiTrans.setSuccess(true);		
				redisCacheService.putObjectCacheValueByEvictionMinute(Mt2UAEConstant.MT2_UAE_SUB_CAHCHE_PREFIX+msisdn,
						token, 60*24*2);
				
			}
		}
		
	}catch(Exception ex){
		logger.error("subscribe ",ex);
	}finally{
		daoService.updateObject(mt2UAEServiceApiTrans);
	}
	return mt2UAEServiceApiTrans;
}

public MT2UAEServiceApiTrans cancelSubscribe(Mt2UAEServiceConfig mt2UAEServiceConfig,
		String msisdn,String subscriptionRef){
	
	MT2UAEServiceApiTrans mt2UAEServiceApiTrans=new MT2UAEServiceApiTrans(true, Mt2UAEConstant.CANCEL_SUBSCRIBE);
	
	try{
		mt2UAEServiceApiTrans.setServiceId(mt2UAEServiceConfig.getServiceId());
		 msisdn=Mt2UAEConstant.formatMsisdn(msisdn
					,mt2UAEServiceConfig.getMsisdnPrefix());
		mt2UAEServiceApiTrans.setMsisdn(msisdn);
		SortedMap<String,String> headerMap=new TreeMap<String,String>();
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("ClientId",mt2UAEServiceConfig.getClientId());
		headerMap.put("LanguageCode", "en");
		headerMap.put("MNC", mt2UAEServiceConfig.getMnc());
		headerMap.put("MCC", mt2UAEServiceConfig.getMcc());
		headerMap.put("AppVersion", "1.2");
		headerMap.put("PlatformTag", "2");
		//headerMap.put("OSVersion", "1.4");
		headerMap.put("ChannelTag", "WAP");
		//headerMap.put("LocationCoordinates", value);
		headerMap.put("DeviceIdentifier", "2");
		//headerMap.put("DeviceName", value);
		headerMap.put("RequestId", ""+mt2UAEServiceApiTrans.getId());
		
		headerMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
		//headerMap.put("Context", value);
		
		Map<String,String> dataMap=new HashMap<String,String>();
		dataMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
		dataMap.put("MSISDN", msisdn);
		dataMap.put("SubscriptionRef",subscriptionRef );//Subscription identifier
		
		String dataJson=JsonMapper.getObjectToJson(dataMap);
		headerMap.put("Signature", getSignature(Mt2UAEConstant.CANCEL_SUBSCRIBE, 
				headerMap, mt2UAEServiceConfig.getSecretKey()));
		
		mt2UAEServiceApiTrans.setRequest(mt2UAEServiceConfig.getSubscribeUrl()
				+":"+dataJson+" : "+headerMap.toString());
		
		HTTPResponse httpResponse=httpURLConnectionUtil
				.makeHTTPPOSTRequest(mt2UAEServiceConfig.getCancelUrl(), dataJson, headerMap);
		mt2UAEServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2UAEServiceApiTrans.setResponse(httpResponse.getResponseStr());
		if(mt2UAEServiceApiTrans.getResponseCode()==200){
			mt2UAEServiceApiTrans.setResponseToCaller(true);
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			Map resultMap=(Map)map.get("Result");
			if(Objects.toString(resultMap.get("Action")).equalsIgnoreCase("OK")
					&&Objects.toString(resultMap.get("Message")).equalsIgnoreCase("Success")){
				mt2UAEServiceApiTrans.setSuccess(true);				
			}
		}
		
	}catch(Exception ex){
		logger.error("subscribe ",ex);
	}finally{
		daoService.updateObject(mt2UAEServiceApiTrans);
	}
	return mt2UAEServiceApiTrans;
}


public MT2UAEServiceApiTrans sendSMS(Mt2UAEServiceConfig mt2UAEServiceConfig,String msisdn,String msg,
		String action){
	
	MT2UAEServiceApiTrans mt2UAEServiceApiTrans=new MT2UAEServiceApiTrans(true, action+"_SMS");
	try{
		mt2UAEServiceApiTrans.setServiceId(mt2UAEServiceConfig.getServiceId());
		 msisdn=Mt2UAEConstant.formatMsisdn(msisdn
					,mt2UAEServiceConfig.getMsisdnPrefix());
		mt2UAEServiceApiTrans.setMsisdn(msisdn);
		SortedMap<String,String> headerMap=new TreeMap<String,String>();
		
		headerMap.put("Content-Type", "application/json");
		headerMap.put("ClientId",mt2UAEServiceConfig.getClientId());
		headerMap.put("LanguageCode", "en");
        headerMap.put("MNC", mt2UAEServiceConfig.getMnc());
        headerMap.put("MCC", mt2UAEServiceConfig.getMcc());
		//headerMap.put("AppVersion", "1.2");
		//headerMap.put("PlatformTag", "2");
		//headerMap.put("OSVersion", "1.4");
		headerMap.put("ChannelTag", "WAP");
		//headerMap.put("LocationCoordinates", value);
		headerMap.put("DeviceIdentifier", "2");
		//headerMap.put("DeviceName", value);
		headerMap.put("username", mt2UAEServiceConfig.getSmsUser());
		headerMap.put("password",mt2UAEServiceConfig.getSmsPassword());
		
		Map<String,String> dataMap=new HashMap<String,String>();
		dataMap.put("ServiceTag", mt2UAEServiceConfig.getServiceTag());
		dataMap.put("MSISDN", msisdn);
		dataMap.put("Data", msg);
		dataMap.put("OperatorID", mt2UAEServiceConfig.getMt2OpId());
		
		headerMap.put("Signature", getSignature(Mt2UAEConstant.SEND_SMS, 
				headerMap, mt2UAEServiceConfig.getSecretKey()));
		
		String dataJson=JsonMapper.getObjectToJson(dataMap);
		
		mt2UAEServiceApiTrans.setRequest(mt2UAEServiceConfig.getSmsUrl()
				+":"+dataJson+" : "+headerMap.toString());
		
		HTTPResponse httpResponse=httpURLConnectionUtil
				.makeHTTPPOSTRequest(mt2UAEServiceConfig.getSmsUrl(), dataJson, headerMap);
		
		mt2UAEServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2UAEServiceApiTrans.setResponse(httpResponse.getResponseStr());
		if(mt2UAEServiceApiTrans.getResponseCode()==200){
			mt2UAEServiceApiTrans.setResponseToCaller(true);
		}
		
	}catch(Exception ex){
		logger.error("sendSMS ",ex);
	}finally{
		daoService.updateObject(mt2UAEServiceApiTrans);
	}
	return mt2UAEServiceApiTrans;
}


}
