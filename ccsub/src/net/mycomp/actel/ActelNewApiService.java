package net.mycomp.actel;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.Adnetworks;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.commons.net.util.Base64;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("actelNewApiService")
public class ActelNewApiService {

	
	private static final Logger logger = Logger
			.getLogger(ActelNewApiService.class.getName());
	
  //  private final String smsPushUrlTemplate;
	private final String smsPushUsername;
	private final String smsPushPassword;
//	private final String  otpUrl;
//	private final String otpValidateUrl;
//	
//	private final String  inappUsername;
//	private final String  inappPassword;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private RedisCacheService redisCacheService; 
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	public ActelNewApiService(
			//@Value("${actel.sms.push.url.template}")String smsPushUrlTemplate,
			@Value("${actel.sms.push.username}")String smsPushUsername,
			@Value("${actel.sms.push.password}")String smsPushPassword
			
			){
//		this.smsPushUrlTemplate=smsPushUrlTemplate;
			this.smsPushUsername=smsPushUsername;
			this.smsPushPassword=smsPushPassword;
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public static void main(String arg[]){
		ActelNewApiService actelNewApiService=new ActelNewApiService("","");
		 String keyData="123"+"32100"+"456"+"123";
			
		String encval=actelNewApiService.aesEncryption("971567816612449730878571", "ydsZ744gXX64CciKuO9qSw==");
		
		System.out.println("ecval:: "+encval);
		String value="I6EeNyeCxbKqVhRirAQjH6d0FJLTytGnRCBq4XPSyUs=\r\n".replace("\r\n", "");
		System.out.println("value:: "+value);
	}
	
	
	
	public String aesEncryption(String data, String apiSecret){
		 try{
		   IvParameterSpec iv = new IvParameterSpec(new byte[16]);
		   Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
		  //Key key = KeyGenerator.getInstance("DES").generateKey();
		 // KeyFactory kf = KeyFactory.getInstance("AES");
		 // PrivateKey prv_recovered = kf.generatePrivate(new PKCS8EncodedKeySpec(apiSecret.getBytes()));		   
		   SecretKeySpec secretKeySpec = new SecretKeySpec(apiSecret.getBytes(), "AES");		   
		   cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec); 		     
		    byte[] encryptedMessageInBytes = cipher.doFinal(data.getBytes()); 
		    String encriptedRequestParam=Base64.encodeBase64String(encryptedMessageInBytes);
		    return encriptedRequestParam.replace("\r\n", "");
		 }catch(Exception ex){
			logger.error("Exception ",ex); 			 
		 }
		 return null;
	}
	
	public boolean pushSms(ActelDlr actelDlr,ActelServiceConfig actelServiceConfig,String msg){
		
		 ActelSms actelSms=new ActelSms(true); 
		try{
		//http://clients.actelme.com/MainSMS/push_Content.aspx?
		//username=<username>&password=<password>&receiver=<receiver>&ContentBody=<content>&
		//originator=playit&countryname=uae&operatorname=etisalat&
		//Contentid=AA814898-0CDA-48F6-BD22-1D4CB8ED2C9B&ContentTypeDetails=text&
		//ContentSubType=smsreply&id_application=4497
			
			 actelSms.setMsisdn(actelDlr.getMsisdn());
			 if(!ActelConstant.isValidMsisdn(actelDlr.getMsisdn(),
					 actelServiceConfig.getMsisdnPrefix()
					 ,actelServiceConfig.getMsisdnLength())){
				 actelSms.setRequestUrl("Not valid msisdn"); 
				 return false;
			 }
			 
		String url=actelServiceConfig.getSmsUrl()
				.replaceAll("<username>",MUtility.urlEncoding(smsPushUsername))
				.replaceAll("<password>",MUtility.urlEncoding(smsPushPassword))
				.replaceAll("<receiver>",MUtility.urlEncoding(actelDlr.getMsisdn()))
				.replaceAll("<content>",MUtility.urlEncoding(msg))
				.replaceAll("<contentid>",MUtility.urlEncoding(""+System.currentTimeMillis()))
				;
		actelSms.setRequestUrl(url);
		HTTPResponse response=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
		actelSms.setResponse(response.getResponseCode()+":"+response.getResponseStr());
		return true;
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.saveObject(actelSms);
		 }
		return false;		
	}
	

	public ActelApiTrans checkSubscriptionStatus(ActelNewServiceConfig actelNewServiceConfig,
			String msisdn,String token){
		
		ActelApiTrans actelApiTrans=new ActelApiTrans(true,"CHECK_SUBCRIPTION_STATUS");
		try{
		    
			actelApiTrans.setToken(token);
			actelApiTrans.setMsisdn(msisdn);
			//actelApiTrans.setMode(mode);
			
			 if(!ActelConstant.isValidMsisdn(actelApiTrans.getMsisdn(),
					 actelNewServiceConfig.getMsisdnPrefix(),actelNewServiceConfig.getMsisdnLength())){
				 actelApiTrans.setRequest("Not valid msisdn"); 
				 return actelApiTrans;
			 }
			 Map<String,String> headerMap=new HashMap<String,String>();			 
			 Map<String,String> dataMap=new HashMap<String,String>();
			 dataMap.put("api_msisdn", msisdn);
			 dataMap.put("api_appid", actelNewServiceConfig.getApiAppid());
			 dataMap.put("api_opid",actelNewServiceConfig.getApiOpid());
			 dataMap.put("api_reqid", ""+actelApiTrans.getId());
			 dataMap.put("api_langid", actelNewServiceConfig.getApiLangid());
			 String keyData=msisdn+actelNewServiceConfig.getApiAppid()+actelNewServiceConfig.getApiOpid()+actelApiTrans.getId();
			
			 dataMap.put("api_sig",aesEncryption(keyData,actelNewServiceConfig.getApiSecretKey()));
			 dataMap.put("api_key", actelNewServiceConfig.getApiKey());			 
			// dataMap.put("api_clickid", token);
			// dataMap.put("api_traffic_source", );
			// dataMap.put("api_buychannel", mode);
			// dataMap.put("api_rate", );			 
		
			 String data=JsonMapper.getObjectToJson(dataMap);
		logger.info("checkSubscriptionStatus:: request url "+actelNewServiceConfig.getCheckSubscriptionStatusUrl());
		actelApiTrans.setRequest(actelNewServiceConfig.getCheckSubscriptionStatusUrl()+" : "+data +" : "+keyData);
		
		HTTPResponse httpResponse=httpURLConnectionUtil
				.makeHTTPPOSTRequest(actelNewServiceConfig.getCheckSubscriptionStatusUrl()
				, data, headerMap);
		logger.info("checkSubscriptionStatus:: httpResponse  "+httpResponse);
		actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
		     Map responseMap=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);//ActelConstant.parseXml(httpResponse.getResponseStr());
		     if(Objects.toString(responseMap.get("active")).equalsIgnoreCase("1")){
		    	 actelApiTrans.setSuccess(true);
		     }
		}
		
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(actelApiTrans);
		 }
		return actelApiTrans;		
	}
	
	public ActelApiTrans sendOTP(ActelNewServiceConfig actelNewServiceConfig,
			String msisdn,String token,String mode){
		
		ActelApiTrans actelApiTrans=new ActelApiTrans(true,"SEND_OTP");
		try{
			//http://clients.actelme.com/MainSMS/REST/Subscription_Management.aspx?
			//username=<username>&password=<password>
				//&requestid=<requestid>&operatorid=<operatorid>&msisdn=<msisdn>&id_application=<idapplication>
				//&langid=<langid>&signature=<signature>&mode=<mode>&method=OTP
			actelApiTrans.setToken(token);
			actelApiTrans.setMsisdn(msisdn);
			actelApiTrans.setMode(mode);
			
			if(ActelConstant.blockMsisdn.contains(actelApiTrans.getMsisdn())) {
				actelApiTrans.setRequest("Blocked Msisdn"); 
				 return actelApiTrans;
			}
			 if(!ActelConstant.isValidMsisdn(actelApiTrans.getMsisdn(),
					 actelNewServiceConfig.getMsisdnPrefix(),actelNewServiceConfig.getMsisdnLength())){
				 actelApiTrans.setRequest("Not valid msisdn"); 
				 return actelApiTrans;
			 }
			 Map<String,String> headerMap=new HashMap<String,String>();			 
			 Map<String,String> dataMap=new HashMap<String,String>();
			 dataMap.put("api_msisdn", msisdn);
			 dataMap.put("api_appid", actelNewServiceConfig.getApiAppid());
			 dataMap.put("api_opid",actelNewServiceConfig.getApiOpid());
			 dataMap.put("api_reqid", ""+actelApiTrans.getId());
			 dataMap.put("api_langid", actelNewServiceConfig.getApiLangid());
			 String keyData=msisdn+actelNewServiceConfig.getApiAppid()+actelNewServiceConfig.getApiOpid()+actelApiTrans.getId();
			 dataMap.put("api_sig",aesEncryption(keyData,actelNewServiceConfig.getApiSecretKey()) );
			 dataMap.put("api_key", actelNewServiceConfig.getApiKey());			 
			 dataMap.put("api_clickid", token);
			// dataMap.put("api_traffic_source", );
			 dataMap.put("api_buychannel", mode);
			// dataMap.put("api_rate", );			 
		
			 String data=JsonMapper.getObjectToJson(dataMap);
		logger.info("sendOTP:: request url "+actelNewServiceConfig.getPinApi());
		actelApiTrans.setRequest(actelNewServiceConfig.getPinApi()+" : "+data);
		
		HTTPResponse httpResponse=httpURLConnectionUtil
				.makeHTTPPOSTRequest(actelNewServiceConfig.getPinApi()
				, data, headerMap);
		logger.info("sendOTP:: httpResponse  "+httpResponse);
		actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
		     Map responseMap=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);//ActelConstant.parseXml(httpResponse.getResponseStr());
		     if(Objects.toString(responseMap.get("status")).equalsIgnoreCase("OK")){
		    	 actelApiTrans.setSuccess(true);
		     }
		}
		
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(actelApiTrans);
		 }
		return actelApiTrans;		
	}
	
	
public ActelApiTrans validateOTP(ActelNewServiceConfig actelNewServiceConfig,String msisdn,String token,String mode
		,String pin,String ip){
		
		ActelApiTrans actelApiTrans=new ActelApiTrans(true,"VALIDATE_OTP");
		try{
			//http://clients.actelme.com/MainSMS/REST/Subscription_Management.aspx?
			//username=<username>&password=<password>
				//&requestid=<requestid>&operatorid=<operatorid>&msisdn=<msisdn>&id_application=<idapplication>
				//&langid=<langid>&signature=<signature>&mode=<mode>&method=OTP
			actelApiTrans.setToken(token);
			actelApiTrans.setMsisdn(msisdn);
			actelApiTrans.setMode(mode);
			
			if(!ActelConstant.isValidMsisdn(actelApiTrans.getMsisdn(),
					actelNewServiceConfig.getMsisdnPrefix(),actelNewServiceConfig.getMsisdnLength())){
				 actelApiTrans.setRequest("Not valid msisdn"); 
				 return actelApiTrans;
			 }
			
			 Map<String,String> headerMap=new HashMap<String,String>();			 
			 Map<String,String> dataMap=new HashMap<String,String>();
			 dataMap.put("api_msisdn", msisdn);
			 dataMap.put("api_appid", actelNewServiceConfig.getApiAppid());
			 dataMap.put("api_opid",actelNewServiceConfig.getApiOpid());
			 dataMap.put("api_reqid", ""+actelApiTrans.getId());
			 dataMap.put("api_langid", actelNewServiceConfig.getApiLangid());
			 String keyData=msisdn+actelNewServiceConfig.getApiAppid()+actelNewServiceConfig.getApiOpid()+actelApiTrans.getId();
			 dataMap.put("api_sig",aesEncryption(keyData,actelNewServiceConfig.getApiSecretKey()) );
			 dataMap.put("api_key", actelNewServiceConfig.getApiKey());			 
			 dataMap.put("api_clickid", token);
			// dataMap.put("api_traffic_source", );
			 dataMap.put("api_buychannel", mode);
			// dataMap.put("api_rate", );			 
			 dataMap.put("api_pincode", pin);
			 String data=JsonMapper.getObjectToJson(dataMap);
			 
		logger.info("validateOTP:: request url "+actelNewServiceConfig.getPinValidationApi());
		actelApiTrans.setRequest(actelNewServiceConfig.getPinValidationApi()+" : "+data);
		
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(actelNewServiceConfig.getPinValidationApi()
				, data, headerMap);
		
		logger.info("validateOTP:: httpResponse  "+httpResponse);
		actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
		     Map responseMap=JsonMapper.getJsonToObject(httpResponse.getResponseStr(),Map.class);//sActelConstant.parseXml(httpResponse.getResponseStr());
		     if(Objects.toString(responseMap.get("status")).equalsIgnoreCase("OK")){
		    	 actelApiTrans.setSuccess(true);
		    	 redisCacheService.putObjectCacheValueByEvictionMinute(
		    			 ActelConstant.ACTEL_OTP_VALIDATION_CACHE+msisdn
		    			 , token, 60*23);
		     }
		}
		
		
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(actelApiTrans);
		 }
		return  actelApiTrans;		
	}


public ActelApiTrans unsubscription(ActelNewServiceConfig actelNewServiceConfig,
		String msisdn,String token,String mode){
	
	ActelApiTrans actelApiTrans=new ActelApiTrans(true,MConstants.DCT);
	try{
	    
		actelApiTrans.setToken(token);
		actelApiTrans.setMsisdn(msisdn);
		actelApiTrans.setMode(mode);
		
		 if(!ActelConstant.isValidMsisdn(actelApiTrans.getMsisdn(),
				 actelNewServiceConfig.getMsisdnPrefix(),actelNewServiceConfig.getMsisdnLength())){
			 actelApiTrans.setRequest("Not valid msisdn"); 
			 return actelApiTrans;
		 }
		 Map<String,String> headerMap=new HashMap<String,String>();			 
		 Map<String,String> dataMap=new HashMap<String,String>();
		 dataMap.put("api_msisdn", msisdn);
		 dataMap.put("api_appid", actelNewServiceConfig.getApiAppid());
		 dataMap.put("api_opid",actelNewServiceConfig.getApiOpid());
		 dataMap.put("api_reqid", ""+actelApiTrans.getId());
		 dataMap.put("api_langid", actelNewServiceConfig.getApiLangid());
		 String keyData=msisdn+actelNewServiceConfig.getApiAppid()+actelNewServiceConfig.getApiOpid()+actelApiTrans.getId();
		 dataMap.put("api_sig",aesEncryption(keyData,actelNewServiceConfig.getApiSecretKey()) );
		 dataMap.put("api_key", actelNewServiceConfig.getApiKey());			 
		// dataMap.put("api_clickid", token);
		// dataMap.put("api_traffic_source", );
		// dataMap.put("api_buychannel", mode);
		// dataMap.put("api_rate", );			 
	
		 String data=JsonMapper.getObjectToJson(dataMap);
	logger.info("unsubscription:: request url "+actelNewServiceConfig.getUnsubUrl());
	actelApiTrans.setRequest(actelNewServiceConfig.getUnsubUrl()+" : "+data);
	
	HTTPResponse httpResponse=httpURLConnectionUtil
			.makeHTTPPOSTRequest(actelNewServiceConfig.getUnsubUrl()
			, data, headerMap);
	logger.info("unsubscription:: httpResponse  "+httpResponse);
	actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
	if(httpResponse.getResponseCode()==200){
	     Map responseMap=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);//ActelConstant.parseXml(httpResponse.getResponseStr());
	     if(Objects.toString(responseMap.get("status")).equalsIgnoreCase("ok")){
	    	 actelApiTrans.setSuccess(true);
	     }
	}
	 }catch(Exception ex){
		 logger.error("exception ",ex);
	 }finally{
		 daoService.updateObject(actelApiTrans);
	 }
	return actelApiTrans;		
}

}


