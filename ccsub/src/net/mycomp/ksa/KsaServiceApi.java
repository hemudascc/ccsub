package net.mycomp.ksa;

import java.util.Map;
import java.util.Objects;

import net.common.jms.JMSService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;

import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("ksaServiceApi")
public class KsaServiceApi {

	private static final Logger logger = Logger
			.getLogger(KsaServiceApi.class.getName());
	
	
	//private final String profileCheckUrl;
	//private final  String subscriptionPinUrl;
//	private final String  subscriptionPinValidationUrl;
//	private final String smsPushUrl;
//	private final String smsBulkPushUrl;	
//	private final String unsubscriptionUrl;
	
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private JMSKSAService jmsKSAService;
	
	///@Autowired
	public KsaServiceApi(
			//@Value("${ksa.profile.check.url}")String profileCheckUrl,
			//@Value("${ksa.subscription.pin.url}")String subscriptionPinUrl,
			//@Value("${ksa.subscription.pin.validation.url}")String subscriptionPinValidationUrl,
			//@Value("${ksa.sms.push.url}")String smsPushUrl,
			//@Value("${ksa.unsubscription.url}")String unsubscriptionUrl
			){
		//this.profileCheckUrl=profileCheckUrl;
		//this.subscriptionPinUrl=subscriptionPinUrl;
		//this.subscriptionPinValidationUrl=subscriptionPinValidationUrl;
		//this.smsPushUrl=smsPushUrl;
		//this.unsubscriptionUrl=unsubscriptionUrl;
		this.httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public boolean checkProfile(String msisdn,KsaServiceConfig ksaServiceConfig,String token){
		//http://<IP>:<Port>/<Context>/en/profileCheck?msisdn=999999999&serviceId=1001&operatorId=xyz
		KsaApiTrans ksaProfileCheckTrans=new KsaApiTrans(true,KsaConstant.CHECK_PROFILE);
		try{
			ksaProfileCheckTrans.setMsisdn(msisdn);
			ksaProfileCheckTrans.setToken(token);
			
			if(!KsaConstant.isValidMsisdn(msisdn)){
				ksaProfileCheckTrans.setRequestUrl("Not valid msisdn");
				return false;
			}
			
		String url=ksaServiceConfig.getProfileCheckUrl().replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
				.replaceAll("<serviceid>",MUtility.urlEncoding(ksaServiceConfig.getKsaServiceId()))
				.replaceAll("<operatorid>",MUtility.urlEncoding(ksaServiceConfig.getKsaOperatorId()));
		
		ksaProfileCheckTrans.setRequestUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpGet(url,null);
		ksaProfileCheckTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			if(Objects.toString(map.get("is_subscribed")).equalsIgnoreCase("True")){
				ksaProfileCheckTrans.setResponseToCaller(true);
			}
		}
		}catch(Exception ex){
			logger.error("checkProfile:: ",ex);
		}finally{
			logger.info("checkProfile:::ksaProfileCheckTrans:: "+ksaProfileCheckTrans);
			jmsService.saveObject(ksaProfileCheckTrans);
		}
		return ksaProfileCheckTrans.getResponseToCaller();
	}
	
	
	public boolean sendSubscriptionPinRequest(String msisdn,KsaServiceConfig ksaServiceConfig,String token){
		//http://<IP>:<Port>/<Context>/en/profileCheck?msisdn=999999999&serviceId=1001&operatorId=xyz
		KsaApiTrans ksaProfileCheckTrans=new KsaApiTrans(true,KsaConstant.SEND_PIN);
		try{
			ksaProfileCheckTrans.setMsisdn(msisdn);
			ksaProfileCheckTrans.setToken(token);
			if(!KsaConstant.isValidMsisdn(msisdn)){
				ksaProfileCheckTrans.setRequestUrl("Not valid msisdn");
				return false;
			}
		String url=ksaServiceConfig.getSubscriptionPinUrl().replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
				.replaceAll("<serviceid>",MUtility.urlEncoding(ksaServiceConfig.getKsaServiceId()))
				.replaceAll("<operatorid>",MUtility.urlEncoding(ksaServiceConfig.getKsaOperatorId()));
		ksaProfileCheckTrans.setRequestUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpGet(url,null);
		ksaProfileCheckTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			if(Objects.toString(map.get("sent")).equalsIgnoreCase("1")){
				ksaProfileCheckTrans.setResponseToCaller(true);
			}
		}
		}catch(Exception ex){
			logger.error("sendSubscriptionPinRequest:: ",ex);
		}finally{
			logger.info("sendSubscriptionPinRequest:::sendSubscriptionRequestrans:: "+ksaProfileCheckTrans);
			jmsService.saveObject(ksaProfileCheckTrans);
		}
		return ksaProfileCheckTrans.getResponseToCaller();
	}
	
	public boolean sendSubscriptionPinValidationRequest(String msisdn,
			KsaServiceConfig ksaServiceConfig,String pin,String token){
		//http://<IP>:<Port>/<Context>/en/profileCheck?msisdn=999999999&serviceId=1001&operatorId=xyz
		KsaApiTrans ksaApiTrans=new KsaApiTrans(true,KsaConstant.VALIDATE_PIN);
		try{
			
			ksaApiTrans.setMsisdn(msisdn);
			ksaApiTrans.setToken(token);
			if(!KsaConstant.isValidMsisdn(msisdn)){
				ksaApiTrans.setRequestUrl("Not valid msisdn");
				return false;
			}
			logger.info("sendSubscriptionPinValidationRequest:::ksaApiTrans::msisdn valid ");
			
		String url=ksaServiceConfig.getSubscriptionPinValidationUrl().replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
				.replaceAll("<serviceid>",MUtility.urlEncoding(ksaServiceConfig.getKsaServiceId()))
				.replaceAll("<operatorid>",MUtility.urlEncoding(ksaServiceConfig.getKsaOperatorId()))
				.replaceAll("<pin>",MUtility.urlEncoding(pin))
				;
		ksaApiTrans.setRequestUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpGet(url,null);
		ksaApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			if(Objects.toString(map.get("subscribe")).equalsIgnoreCase("True")){
				ksaApiTrans.setResponseToCaller(true);
			}
		}
		}catch(Exception ex){
			logger.error("sendSubscriptionPinValidationRequest:: ",ex);
		}finally{
			logger.info("sendSubscriptionPinValidationRequest:::ksaApiTrans:: "+ksaApiTrans);
			jmsKSAService.saveKsaApiTrans(ksaApiTrans);
		}
		return ksaApiTrans.getResponseToCaller();
	}
	
	public boolean sendSms(String msisdn,KsaServiceConfig ksaServiceConfig,String msgTemplate,String action){
		//http://<IP>:<Port>/<Context>/en/profileCheck?msisdn=999999999&serviceId=1001&operatorId=xyz
		KsaApiTrans ksaApiTrans=new KsaApiTrans(true,action);//KsaConstant.SMS_PUSH);
		try{
		
		ksaApiTrans.setMsisdn(msisdn);
		if(!KsaConstant.isValidMsisdn(msisdn)){
			ksaApiTrans.setRequestUrl("Not valid msisdn");
			return false;
		}
		String msg=	KsaConstant.getMsg(msgTemplate, ksaServiceConfig, msisdn);			
		String url=ksaServiceConfig.getSmsPushUrl().replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
				.replaceAll("<serviceid>",MUtility.urlEncoding(ksaServiceConfig.getKsaServiceId()))
				.replaceAll("<operatorid>",MUtility.urlEncoding(ksaServiceConfig.getKsaOperatorId()))
				.replaceAll("<msg>",MUtility.urlEncoding(msg))
				;
		ksaApiTrans.setRequestUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpGet(url,null);
		ksaApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			if(map.get("mt_id")!=null){
				ksaApiTrans.setResponseToCaller(true);
			}
		}
		}catch(Exception ex){
			logger.error("sendSms:: ",ex);
		}finally{
			logger.info("sendSms:::ksaApiTrans:: "+ksaApiTrans);
			jmsService.saveObject(ksaApiTrans);
		}
		return ksaApiTrans.getResponseToCaller();
	}
	
	
	public boolean unsubscription(String msisdn,KsaServiceConfig ksaServiceConfig){
		//http://<IP>:<Port>/<Context>/en/profileCheck?msisdn=999999999&serviceId=1001&operatorId=xyz
		KsaApiTrans ksaApiTrans=new KsaApiTrans(true,KsaConstant.DCT);
		try{
			ksaApiTrans.setMsisdn(msisdn);
			if(!KsaConstant.isValidMsisdn(msisdn)){
				ksaApiTrans.setRequestUrl("Not valid msisdn");
				return false;
			}
		String url=ksaServiceConfig.getUnsubscriptionUrl().replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
				.replaceAll("<serviceid>",MUtility.urlEncoding(ksaServiceConfig.getKsaServiceId()))
				.replaceAll("<operatorid>",MUtility.urlEncoding(ksaServiceConfig.getKsaOperatorId()))
				
				;
		ksaApiTrans.setRequestUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpGet(url,null);
		ksaApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			if(map.get("unsubscribe")!=null&&Objects.toString(map.get("unsubscribe"))
					.equalsIgnoreCase("True")){
				ksaApiTrans.setResponseToCaller(true);
			}
		}
		}catch(Exception ex){
			logger.error("unsubscription:: ",ex);
		}finally{
			logger.info("unsubscription:::ksaApiTrans:: "+ksaApiTrans);
			jmsService.saveObject(ksaApiTrans);
		}
		return ksaApiTrans.getResponseToCaller();
	}
	
	
	
	public boolean sendBulkSms(String msisdn,KsaServiceConfig ksaServiceConfig,String msgTemplate){
		//http://<IP>:<Port>/<Context>/bulkPush?serviceId=1001&operatorId=xyz&content=hello%20there
		KsaApiTrans ksaApiTrans=new KsaApiTrans(true,KsaConstant.BULK_PUSH);
		try{
			
		ksaApiTrans.setMsisdn(msisdn);
		String msg=	KsaConstant.getMsg(msgTemplate, ksaServiceConfig, msisdn);			
		String url=ksaServiceConfig.getBulkSmsUrl()
				.replaceAll("<serviceid>",MUtility.urlEncoding(ksaServiceConfig.getKsaServiceId()))
				.replaceAll("<operatorid>",MUtility.urlEncoding(ksaServiceConfig.getKsaOperatorId()))
				.replaceAll("<msg>",MUtility.urlEncoding(msg))
				;
		ksaApiTrans.setRequestUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpGet(url,null);
		ksaApiTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			if(map.get("mt_id")!=null){
				ksaApiTrans.setResponseToCaller(true);
			}
		}
		}catch(Exception ex){
			logger.error("sendSms:: ",ex);
		}finally{
			logger.info("sendSms:::ksaApiTrans:: "+ksaApiTrans);
			jmsService.saveObject(ksaApiTrans);
		}
		return ksaApiTrans.getResponseToCaller();
	}
}
