package net.mycomp.actel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.DatatypeConverter;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.Adnetworks;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.MData;
import net.util.MUtility;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("actelApiService")
public class ActelApiService {

	
	private static final Logger logger = Logger
			.getLogger(ActelApiService.class.getName());
	
	//private final String smsPushUrlTemplate;
	private final String smsPushUsername;
	private final String smsPushPassword;
	private final String  otpUrl;
	private final String otpValidateUrl;
	
	private final String  inappUsername;
	private final String  inappPassword;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private RedisCacheService redisCacheService; 
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	public ActelApiService(//@Value("${actel.sms.push.url.template}")String smsPushUrlTemplate,
			@Value("${actel.sms.push.username}")String smsPushUsername,
			@Value("${actel.sms.push.password}")String smsPushPassword,
			@Value("${actel.inapp.username}")String inappUsername,
			@Value("${actel.inapp.password}")String inappPassword,
			@Value("${actel.send.otp.url.template}")String otpUrl,
			@Value("${actel.otp.validate.url.template}")String otpValidateUrl
			){
	//	this.smsPushUrlTemplate=smsPushUrlTemplate;
		this.smsPushUsername=smsPushUsername;
		this.smsPushPassword=smsPushPassword;
		this.otpUrl=otpUrl;
		this.otpValidateUrl=otpValidateUrl;
		this.inappUsername=inappUsername;
		this.inappPassword=inappPassword;
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public boolean pushSms(ActelDlr actelDlr,ActelNewServiceConfig actelServiceConfig,String msg){
		
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
	
	private String calculateMD5(Integer id){
		
		 try { 
			 String token=smsPushUsername+"@"+smsPushPassword+"@"+id;
	            // Static getInstance method is called with hashing MD5 
	            MessageDigest md = MessageDigest.getInstance("MD5"); 
	  
	            // digest() method is called to calculate message digest 
	            //  of an input digest() return array of byte 
	            byte[] messageDigest = md.digest(token.getBytes()); 
	  
	            return   DatatypeConverter
	            	      .printHexBinary(messageDigest);
	            
	           
	        }catch(Exception ex){
	        	logger.error("calculateMD5 ",ex);
	        } 
		 return null;
	}
	
	
	public ActelApiTrans sendOTP(ActelServiceConfig actelServiceConfig,String msisdn,String token,String mode){
		
		ActelApiTrans actelApiTrans=new ActelApiTrans(true,"SEND_OTP");
		try{
			//http://clients.actelme.com/MainSMS/REST/Subscription_Management.aspx?
			//username=<username>&password=<password>
				//&requestid=<requestid>&operatorid=<operatorid>&msisdn=<msisdn>&id_application=<idapplication>
				//&langid=<langid>&signature=<signature>&mode=<mode>&method=OTP
			actelApiTrans.setToken(token);
			actelApiTrans.setMsisdn(msisdn);
			actelApiTrans.setMode(mode);
			
			 if(!ActelConstant.isValidMsisdn(actelApiTrans.getMsisdn(),
					 actelServiceConfig.getMsisdnPrefix(),actelServiceConfig.getMsisdnLength())){
				 actelApiTrans.setRequest("Not valid msisdn"); 
				 return actelApiTrans;
			 }
			 
		String url=otpUrl
				.replaceAll("<username>",MUtility.urlEncoding(inappUsername))
				.replaceAll("<password>",MUtility.urlEncoding(inappPassword))
				.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))				
				.replaceAll("<operatorid>",MUtility.urlEncoding(""+actelServiceConfig.getOperatorId()))
				.replaceAll("<idapplication>",MUtility.urlEncoding(actelServiceConfig.getIdApplication()))
				.replaceAll("<langid>",MUtility.urlEncoding(""+actelServiceConfig.getLangId()))
				.replaceAll("<signature>",calculateMD5(actelApiTrans.getId()))
				.replaceAll("<mode>",MUtility.urlEncoding(mode))
				.replaceAll("<requestid>",MUtility.urlEncoding(""+actelApiTrans.getId()))
				;
		logger.info("sendOTP:: request url "+url);
		actelApiTrans.setRequest(url);
		
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
		logger.info("sendOTP:: httpResponse  "+httpResponse);
		actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
		     Map<String,String> map=ActelConstant.parseXml(httpResponse.getResponseStr());
		     if(map.get("status").equalsIgnoreCase("OK")){
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
	
	
public ActelApiTrans validateOTP(ActelServiceConfig actelServiceConfig,String msisdn,String token,String mode
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
			
			if(!ActelConstant.isValidMsisdn(actelApiTrans.getMsisdn(),actelServiceConfig.getMsisdnPrefix(),actelServiceConfig.getMsisdnLength())){
				 actelApiTrans.setRequest("Not valid msisdn"); 
				 return actelApiTrans;
			 }
			
		String url=otpValidateUrl
				.replaceAll("<username>",MUtility.urlEncoding(inappUsername))
				.replaceAll("<password>",MUtility.urlEncoding(inappPassword))
				.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))				
				.replaceAll("<operatorid>",MUtility.urlEncoding(""+actelServiceConfig.getOperatorId()))
				.replaceAll("<idapplication>",MUtility.urlEncoding(actelServiceConfig.getIdApplication()))
				.replaceAll("<langid>",MUtility.urlEncoding(""+actelServiceConfig.getLangId()))
				.replaceAll("<signature>",calculateMD5(actelApiTrans.getId()))
				.replaceAll("<mode>",MUtility.urlEncoding(mode))
				.replaceAll("<requestid>",MUtility.urlEncoding(""+actelApiTrans.getId()))
				.replaceAll("<pin>",MUtility.urlEncoding(pin))
				.replaceAll("<ip>",MUtility.urlEncoding(ip))
				;
		logger.info("validateOTP:: request url "+url);
		actelApiTrans.setRequest(url);
		
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
		logger.info("validateOTP:: httpResponse  "+httpResponse);
		actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
		     Map<String,String> map=ActelConstant.parseXml(httpResponse.getResponseStr());
		     if(map.get("status").equalsIgnoreCase("OK")){
		    	 actelApiTrans.setSuccess(true);
		    	 redisCacheService.putObjectCacheValueByEvictionMinute(
		    			 ActelConstant.ACTEL_OTP_VALIDATION_CACHE+msisdn
		    			 , token, 60*10);
		     }
		}
		
		
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(actelApiTrans);
		 }
		return  actelApiTrans;		
	}
	
public boolean sendCallback(ActelServiceConfig actelServiceConfig,ActelDlr actelDlr){
		
		
		try{
			Adnetworks adnetworks=  MData.mapAdnetworks.get(2);
			String url=adnetworks.getCallbackUrlTemplate().replace("<token>", MUtility.urlEncoding(
					Objects.toString(actelDlr.getClickId())));
	        HTTPResponse htpResponse=httpURLConnectionUtil.sendGet(url);
	        actelDlr.setCallbackUrl(url);
	        actelDlr.setResponse(htpResponse.getResponseCode()+" : "+htpResponse.getResponseStr());
	        return true;
		}catch(Exception ex){
			logger.error("sendCallback    ",ex);
		}
		return false;
}
/////////////OOredo Qatar Start////////////////////////////////////////////////
public ActelApiTrans ooredoSendOTP(ActelServiceConfig actelServiceConfig,String msisdn,String token,String mode){
	
	ActelApiTrans actelApiTrans=new ActelApiTrans(true,"SEND_OTP");
	try{
		//http://clients.actelme.com/MainSMS/REST/Subscription_Management.aspx?
		//username=<username>&password=<password>
			//&requestid=<requestid>&operatorid=<operatorid>&msisdn=<msisdn>&id_application=<idapplication>
			//&langid=<langid>&signature=<signature>&mode=<mode>&method=OTP
		actelApiTrans.setToken(token);
		actelApiTrans.setMsisdn(msisdn);
		actelApiTrans.setMode(mode);
		
		 if(!ActelConstant.isOoredoValidMsisdn(actelApiTrans.getMsisdn(),actelServiceConfig.getMsisdnPrefix(),actelServiceConfig.getMsisdnLength())){
			 actelApiTrans.setRequest("Not valid msisdn"); 
			 return actelApiTrans;
		 }
		 
	String url=otpUrl
			.replaceAll("<username>",MUtility.urlEncoding(inappUsername))
			.replaceAll("<password>",MUtility.urlEncoding(inappPassword))
			.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))				
			.replaceAll("<operatorid>",MUtility.urlEncoding(""+actelServiceConfig.getOperatorId()))
			.replaceAll("<idapplication>",MUtility.urlEncoding(actelServiceConfig.getIdApplication()))
			.replaceAll("<langid>",MUtility.urlEncoding(""+actelServiceConfig.getLangId()))
			.replaceAll("<signature>",calculateMD5(actelApiTrans.getId()))
			.replaceAll("<mode>",MUtility.urlEncoding(mode))
			.replaceAll("<requestid>",MUtility.urlEncoding(""+actelApiTrans.getId()))
			;
	logger.info("sendOTP:: request url "+url);
	actelApiTrans.setRequest(url);
	
	HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
	logger.info("sendOTP:: httpResponse  "+httpResponse);
	actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
	if(httpResponse.getResponseCode()==200){
	     Map<String,String> map=ActelConstant.parseXml(httpResponse.getResponseStr());
	     if(map.get("status").equalsIgnoreCase("OK")){
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


public ActelApiTrans ooredooValidateOTP(ActelServiceConfig actelServiceConfig,String msisdn,String token,String mode
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
		
		if(!ActelConstant.isOoredoValidMsisdn(msisdn, actelServiceConfig.getMsisdnPrefix(),actelServiceConfig.getMsisdnLength())){
			 actelApiTrans.setRequest("Not valid msisdn"); 
			 return actelApiTrans;
		 }
		
	String url=otpValidateUrl
			.replaceAll("<username>",MUtility.urlEncoding(inappUsername))
			.replaceAll("<password>",MUtility.urlEncoding(inappPassword))
			.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))				
			.replaceAll("<operatorid>",MUtility.urlEncoding(""+actelServiceConfig.getOperatorId()))
			.replaceAll("<idapplication>",MUtility.urlEncoding(actelServiceConfig.getIdApplication()))
			.replaceAll("<langid>",MUtility.urlEncoding(""+actelServiceConfig.getLangId()))
			.replaceAll("<signature>",calculateMD5(actelApiTrans.getId()))
			.replaceAll("<mode>",MUtility.urlEncoding(mode))
			.replaceAll("<requestid>",MUtility.urlEncoding(""+actelApiTrans.getId()))
			.replaceAll("<pin>",MUtility.urlEncoding(pin))
			.replaceAll("<ip>",MUtility.urlEncoding(ip))
			;
	logger.info("validateOTP:: request url "+url);
	actelApiTrans.setRequest(url);
	
	HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,null);
	logger.info("validateOTP:: httpResponse  "+httpResponse);
	actelApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
	if(httpResponse.getResponseCode()==200){
	     Map<String,String> map=ActelConstant.parseXml(httpResponse.getResponseStr());
	     if(map.get("status").equalsIgnoreCase("OK")){
	    	 actelApiTrans.setSuccess(true);
	    	 redisCacheService.putObjectCacheValueByEvictionMinute(
	    			 ActelConstant.ACTEL_OTP_VALIDATION_CACHE+msisdn
	    			 , token, 60*10);
	     }
	}
	
	
	 }catch(Exception ex){
		 logger.error("exception ",ex);
	 }finally{
		 daoService.updateObject(actelApiTrans);
	 }
	return  actelApiTrans;		
}
////////////Oooredo Qatar End///////////////////////////////////////////////
}


