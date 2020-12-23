package net.bizao;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.annotation.PostConstruct;

import net.bizao.json.bean.AmountTransactionWrapper;
import net.bizao.json.bean.Challenge;
import net.bizao.json.bean.ChallengeWrapper;
import net.bizao.json.bean.Challenge.Inputs;
import net.bizao.json.bean.OutboundSMSMessageRequestWrapper;
import net.common.jms.JMSService;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("bizaoApiService")
public class BizaoApiService {

	private static final Logger logger = Logger
			.getLogger(BizaoApiService.class.getName());
	
	
	
	@Value("${bizao.otp.message}")
	private String otpMessage;
	
	
	@Value("${bizao.authorization.token}")
	private String authorization;
	@Value("${bizao.challenge.url}")
	private String bizaoChallengeUrl;
	@Value("${bizao.charging.url}")
	private String bizaoChargingUrl;
	@Value("${bizao.sms.url}")
	private String bizaoSmsUrl;
	
	@Value("${bizao.sms.notify.url}")
	private String smsNotifyUrl;
	
	@Value("${bizao.msisdn.to.alias.url}")
	private String bizaoMsisdnToAliasUrl;
	
	
	@Value("${bizao.access.token.url}")
	private String bizaoAccessTokenUrl;
	
	//@Value("${bizao.security.mcp.block.api}")
	//private String bizaoSecurityMcpBlockApi;
	
	
	private String accessToken;
	
	private Random random = new Random();
	private BizaoHttpURLConnectionUtil bizaoHttpURLConnectionUtil;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	private static final Object lockObject=new Object();
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private JMSBizaoService jmsBizaoService;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private IDaoService daoService;
	
	
	
	
	@PostConstruct
	public void init(){
		try{
		bizaoHttpURLConnectionUtil=new BizaoHttpURLConnectionUtil();
		httpURLConnectionUtil=new HttpURLConnectionUtil();
		
		BizaoHTTPResponse bizaoHTTPResponse=bizaoHttpURLConnectionUtil.
				findAccessToken(bizaoAccessTokenUrl, authorization);
		logger.info("init::::: bizaoHTTPResponse::: "+bizaoHTTPResponse);
		Map map=JsonMapper.getJsonToObject(bizaoHTTPResponse.getResponseStr(), Map.class);
		logger.info("init::::: map::::::: "+map);
		accessToken=(String)map.get("access_token");
		logger.info("init::::: accessToken::::::: "+accessToken);
		}catch(Exception ex){
			logger.error("init:::: ",ex);
		}
	}
	
	public BizaoOtpTrans sendOtp(String msisdn,BizaoConfig bizaoConfig,String  token){
		BizaoOtpTrans bizaoOtp=null;
		try{
		bizaoOtp=new BizaoOtpTrans();
		bizaoOtp.setMsisdn(msisdn);
		bizaoOtp.setMsisdnPrefix(bizaoConfig.getMsisdnPrefix());
		bizaoOtp.setBizaoConfigId(bizaoConfig.getId());
		bizaoOtp.setServiceId(bizaoOtp.getServiceId());
		bizaoOtp.setOtp(String.format("%04d", random.nextInt(10000)));
		bizaoOtp.setToken(token);
		bizaoOtp.setOtpMessage(otpMessage.replaceAll("<otp>",bizaoOtp.getOtp()));
		Challenge challenge=new Challenge();
		challenge.setMethod("OTP-SMS-AUTH");
		challenge.setCountry(bizaoConfig.getCountryCode());
		challenge.setService("BIZAO");
		challenge.setPartnerId("PDKSUB");
		challenge.setMethod("OTP-SMS-AUTH");
		challenge.addInputs("MSISDN",bizaoOtp.getMsisdnPrefix()+bizaoOtp.getMsisdn());
		challenge.addInputs("confirmationCode", "");
		challenge.addInputs("message", bizaoOtp.getOtpMessage());
		challenge.addInputs("otpLength", ""+bizaoOtp.getOtp().length());
		challenge.addInputs("senderName",bizaoConfig.getSenderName());
		Map<String,Challenge> map=new HashMap<String,Challenge>();
		map.put("challenge", challenge);
		String otpRequestData=JsonMapper.getObjectToJson(map);
		//String authorization=Base64.getEncoder().encodeToString((bizaoConfig.getClientId()+":"+bizaoConfig.getClientSecret()).getBytes());
		
		BizaoHTTPResponse bizaoHTTPResponse=bizaoHttpURLConnectionUtil.
				sendOTPPostRequest(bizaoChallengeUrl,otpRequestData, accessToken);
		logger.info("sendOtp:: bizaoHTTPResponse::111111:: "+bizaoHTTPResponse);
		
		bizaoOtp.setResponseCode(bizaoHTTPResponse.getResponseCode());
		bizaoOtp.setResponse(bizaoHTTPResponse.getResponseStr());	
		String[] parts = bizaoOtp.getResponse().split("/");
		logger.info("sendOtp:: bizaoHTTPResponse::111111::ChallengeId:: "+parts[parts.length-1]);
		
		
		bizaoOtp.setChallengeId(parts[parts.length-1]);
		redisCacheService.putObjectCacheValueByEvictionMinute(BizaoConstant.BIZAO_CHALENGE_ID_CACHE_PREFIX+msisdn,
				bizaoOtp.getChallengeId(), 2000);
		
		}catch(Exception ex){
			logger.error("sendOtp :: ",ex);
		}finally{
			jmsService.saveObject(bizaoOtp);
		}
		return bizaoOtp;
	}
	
	public BizaoValidateOtpTrans validateOTP(String msisdn,BizaoConfig bizaoConfig,String cgToken,String otp ){
		BizaoValidateOtpTrans bizaoValidateOtp=null;
		
		try{
		
		bizaoValidateOtp=new BizaoValidateOtpTrans();
		bizaoValidateOtp.setMsisdnPrefix(bizaoConfig.getMsisdnPrefix());
		bizaoValidateOtp.setMsisdn(msisdn);
		bizaoValidateOtp.setOtp(otp);
		bizaoValidateOtp.setBizaoConfigId(bizaoConfig.getId());
		bizaoValidateOtp.setToken(cgToken);
		Challenge challenge=new Challenge();
		challenge.setMethod("OTP-SMS-AUTH");
		challenge.setCountry(bizaoConfig.getCountryCode());
		challenge.setService("BIZAO");
		challenge.setPartnerId("PDKSUB");
		challenge.setMethod("OTP-SMS-AUTH");
		challenge.addInputs("MSISDN",bizaoConfig.getMsisdnPrefix()+bizaoValidateOtp.getMsisdn());
		challenge.addInputs("confirmationCode",bizaoValidateOtp.getOtp());
		challenge.addInputs("info", "OrangeApiToken,ise2");
		Map<String,Challenge> map=new HashMap<String,Challenge>();
		map.put("challenge", challenge);
		String otpRequestData=JsonMapper.getObjectToJson(map);
		//String authorization=Base64.getEncoder().encodeToString((bizaoConfig.getClientId()+":"+bizaoConfig.getClientSecret()).getBytes());
		String challegeId=(String)redisCacheService.getObjectCacheValue(
				BizaoConstant.BIZAO_CHALENGE_ID_CACHE_PREFIX+msisdn);
		bizaoValidateOtp.setRequestUrl(bizaoChallengeUrl+"/"+challegeId);
		
		BizaoHTTPResponse bizaoHTTPResponse=bizaoHttpURLConnectionUtil.
				sendValidateOTPPostRequest(bizaoValidateOtp.getRequestUrl(),otpRequestData, accessToken);
		
		logger.info("validateOTP:: bizaoHTTPResponse:: "+bizaoHTTPResponse);
		
		bizaoValidateOtp.setResponseCode(bizaoHTTPResponse.getResponseCode());
		bizaoValidateOtp.setResponse(bizaoHTTPResponse.getResponseStr());
		ChallengeWrapper challengeWrapper=JsonMapper.getJsonToObject(bizaoValidateOtp.getResponse(),ChallengeWrapper.class);
		
		if((bizaoValidateOtp.getResponseCode()==200||bizaoValidateOtp.getResponseCode()==201)
				&&bizaoValidateOtp.getResponse()!=null){
			bizaoValidateOtp.setIsValid(true);
			Challenge respChallenge=challengeWrapper.getChallenge();
		for(Inputs in:respChallenge.getResult()){
				if(in.getType().equalsIgnoreCase("OrangeApiToken")||in.getType().equalsIgnoreCase("BIZAO_TOKEN")){
					bizaoValidateOtp.setBizaoToken(in.getValue());
				}else if(in.getType().equalsIgnoreCase("ise2")||in.getType().equalsIgnoreCase("BIZAO_ALIAS")){
					bizaoValidateOtp.setBizaoAalias(in.getValue());
				}			
		    }
		}
		
		}catch(Exception ex){
			logger.error("validateOTP:: ",ex);
		}finally{
			jmsService.saveObject(bizaoValidateOtp);
		}
		return bizaoValidateOtp;
	}
	
	
	
	public BizaoPayment makePayment(String action,String msisdn,BizaoConfig bizaoConfig,String token,
			String bizaoToken,String bizaoAlias ){
		
		BizaoPayment bizaoPayment=null;
		
		try{
			
			
		bizaoPayment=new BizaoPayment(true);
		bizaoPayment.setMsisdnPrefix(bizaoConfig.getMsisdnPrefix());
		bizaoPayment.setMsisdn(msisdn);
		bizaoPayment.setBizaoToken(bizaoToken);
		bizaoPayment.setBizaoAlias(bizaoAlias);
		bizaoPayment.setBizaoConfigId(bizaoConfig.getId());
		bizaoPayment.setAction(action);
		bizaoPayment.setToken(token);
		
		synchronized (lockObject) {			
		
		Long value=redisCacheService.getObjectCacheValueByEvictionSecond(BizaoConstant.BIZAO_PAYMENT_TRX
				+bizaoAlias
				, 1l, 5);
		logger.info("makePayment::::: cache value:: "+value);
		if(value!=null&&value>1l){
			bizaoPayment.setResponse("duplicate request within 15 second");
			bizaoPayment.setSuccess(false);
			return bizaoPayment;
		  }		
		}
		
		//String authorization=Base64.getEncoder().encodeToString((bizaoConfig.getClientId()+":"+bizaoConfig.getClientSecret()).getBytes());
		AmountTransactionWrapper amountTransactionWrapper=new 
				AmountTransactionWrapper(String.valueOf(bizaoConfig.getPricePoint()),
						bizaoConfig.getCurrency()
						,bizaoConfig.getServiceName(),
						bizaoConfig.getOnBehalfOf(),
						String.valueOf(System.currentTimeMillis()+token),
						String.valueOf(System.currentTimeMillis()+token));
		
		Map<String,String> headerMap=new HashMap<String,String>();
		headerMap.put("authorization", "Bearer "+accessToken);
		headerMap.put("cache-control", "no-cache");
		headerMap.put("content-type", "application/json");
		headerMap.put("bizao-token", bizaoPayment.getBizaoToken());
		headerMap.put("bizao-alias", bizaoPayment.getBizaoAlias());
		
		String requestJson=JsonMapper.getObjectToJson(amountTransactionWrapper);
		bizaoPayment.setRequest(requestJson+" header: "+headerMap);
		BizaoHTTPResponse bizaoHTTPResponse=bizaoHttpURLConnectionUtil.
				sendPostRequest(bizaoChargingUrl,requestJson,headerMap);
		logger.info("validateOTP:: bizaoHTTPResponse:: "+bizaoHTTPResponse);
		bizaoPayment.setResponse(bizaoHTTPResponse.getResponseCode()+": "+bizaoHTTPResponse.getResponseStr());
		AmountTransactionWrapper resAmountTransactionWrapper=JsonMapper.
				getJsonToObject(bizaoHTTPResponse.getResponseStr(),AmountTransactionWrapper.class);
		
		if(bizaoHTTPResponse.getResponseCode()==201&&resAmountTransactionWrapper!=null){
			bizaoPayment.setServerReferenceCode(resAmountTransactionWrapper.getAmountTransaction().
					getServerReferenceCode());
			bizaoPayment.setResourceUrl(resAmountTransactionWrapper.getAmountTransaction().
					getResourceURL());
			bizaoPayment.setChargedAmount(MUtility.toDouble(resAmountTransactionWrapper.getAmountTransaction().
					getPaymentAmount().getTotalAmountCharged(),0));
			bizaoPayment.setSuccess(true);
		 }
		}catch(Exception ex){
			logger.error("makePayment:: ",ex);
		}finally{
			jmsBizaoService.saveBizaoPayment(bizaoPayment);
			//daoService.updateObject(bizaoPayment);
		}
		return bizaoPayment;
	}

	
	public BizaoSms sendSms(String action,String msisdnPrefix,BizaoConfig bizaoConfig,String text,
			String bizaoToken,String bizaoAlias,String token){
		
		BizaoSms bizaoSms=null;
		try{
			
		if(text==null){
			logger.info("meessage is empty. could not send");
			return null;
		}	
		bizaoSms=new BizaoSms();
		//bizaoSms.setSenderAddress("tel:"+msisdnPrefix+msisdn);
		bizaoSms.setBizaoToken(bizaoToken);
		bizaoSms.setBizaoAlias(bizaoAlias);
		bizaoSms.setSenderName(bizaoConfig.getSenderName());
		bizaoSms.setTextMsg(text);
		bizaoSms.setAction(action);
		
		OutboundSMSMessageRequestWrapper outboundSMSMessageRequestWrapper=
				new OutboundSMSMessageRequestWrapper(bizaoSms.getTextMsg(),
						String.valueOf(bizaoSms.getId()),smsNotifyUrl
				,String.valueOf(bizaoSms.getId()),bizaoSms.getSenderName(),"tel:"+bizaoConfig.getSenderAddress());
		
		Map<String,String> headerMap=new HashMap<String,String>();
		
		headerMap.put("authorization", "Bearer "+accessToken);
		headerMap.put("cache-control", "no-cache");
		headerMap.put("content-type", "application/json");
		headerMap.put("bizao-token", bizaoToken);
		//headerMap.put("bizao-alias", bizaoToken);
		 
		headerMap.put("x-oapi-application-id", "BIZAO");
		headerMap.put("x-oapi-contact-id", "b2b-bizao-97b5878");
		headerMap.put("x-oapi-resource-type", "SMS_OSM");
		headerMap.put("bizao-alias", bizaoAlias);
		headerMap.put("x-orange-mco",bizaoConfig.getCircle());
		logger.info("send sms header map:: "+headerMap);
		String requestJson=JsonMapper.getObjectToJson(outboundSMSMessageRequestWrapper);
		logger.info("send sms request json:: "+requestJson);
		bizaoSms.setRequest(requestJson);
		
		String smsUrl=bizaoSmsUrl.replaceAll("<senderaddress>",MUtility.urlEncoding(bizaoConfig.getSenderAddress()));
		BizaoHTTPResponse bizaoHTTPResponse=bizaoHttpURLConnectionUtil.
				sendPostRequest(smsUrl,requestJson,headerMap);
		
		logger.info("send sms bizaoHTTPResponse:: "+bizaoHTTPResponse);
		
		bizaoSms.setResponse(bizaoHTTPResponse.getResponseStr());
		
		}catch(Exception ex){
			logger.error("sendSms:: ",ex);
		}finally{
			daoService.updateObject(bizaoSms);
		}
		return bizaoSms;
	}
	
	public BizaoMsisdnToAlias getAliasAndToken(String msisdn,BizaoConfig bizaoConfig){
		
		
		BizaoMsisdnToAlias bizaoMsisdnToAlias=null;
		try{
			Map<String,String> headerMap=new HashMap<String,String>();
			headerMap.put("authorization", "Bearer "+accessToken);
			headerMap.put("cache-control", "no-cache");
			String url=bizaoMsisdnToAliasUrl
					.replaceAll("<msisdn>",bizaoConfig.getMsisdnPrefix()+msisdn)
					.replaceAll("<country>",bizaoConfig.getCountryCode());
			  //'https://api.bizao.com/eligibility/v1/tel:<msisdn>/infos?country=<country>&configuration=SUPPORT' \
			logger.info("getAliasAndToken:: request url:: "+url);
			BizaoHTTPResponse bizaoHTTPResponse=bizaoHttpURLConnectionUtil.
					 sendGetRequest(url,"",headerMap);
			logger.info("getAliasAndToken:: bizaoHTTPResponse:: "+bizaoHTTPResponse);
			//{\"configuration\":\"SUPPORT\",\"country\":\"CIV\",\"accountId\":\"tel:+22530303030\",
			 //\"infos\":[{\"key\":\"user_federation_id\",\"value\":\"PDKSUB-200-csatbXtsfr5bZs4vuHlzpEnojI2yclpygD0Z7x7ciCw=\"}]}
			if(bizaoHTTPResponse.getResponseCode()==200){
				bizaoMsisdnToAlias=JsonMapper.getJsonToObject(bizaoHTTPResponse.getResponseStr(), BizaoMsisdnToAlias.class);
			}
		}catch(Exception ex){
			logger.error("Exception:: ",ex);
		}
		return bizaoMsisdnToAlias;		
	}
		
	
	public BizaoSecurityValidateTrans getShield(String token,String queryStr,Map<String,String> headerMap,
			String remoteAddress){
		BizaoSecurityValidateTrans bizaoSecurityValidateTrans=null;
		  String source="";
		    String uniqid="";
		    
		try{
			bizaoSecurityValidateTrans=new BizaoSecurityValidateTrans(true);
		
		
		    String serviceKey ="2Ne3OXEBUDzWVHEzZsFt";// "ckXHw2kBCWcIpgQqBRRK";
		    String transactionID=token;
            String APIURL = "https://sa.apiserver.shield.monitoringservice.co/"+serviceKey+"/"+transactionID+"/JS";
		    String apiSnippetUrl = "https://uk.api.shield.monitoringservice.co/";
             String url = "http://192.241.253.234/ccsub/cnt/cmp";//request.getRequestURL().toString();
		    url = url + "?" + queryStr;
		    StringBuilder urlBuilder = new StringBuilder();
		    urlBuilder.append("?lpu=" + MUtility.urlEncoding(url) + 
		    		"&timeStamp=" + System.nanoTime() + "&user_ip=" + remoteAddress 
		    		+ "&head=" + MUtility.urlEncoding(JsonMapper.getObjectToJson(headerMap)));		    
		   Map<String,String> header=new HashMap<String,String>();
		   header.put("User-Agent", headerMap.get("user-agent"));
		   bizaoSecurityValidateTrans.setRequestStr("url:: "+APIURL + urlBuilder.toString()
				   +" , header:: "+header);
		   HTTPResponse httpResponse= httpURLConnectionUtil.sendHttpsGet(APIURL + urlBuilder.toString(), header);	
		   bizaoSecurityValidateTrans.setResponseStr(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
		    if (!httpResponse.getResponseStr().equalsIgnoreCase("")) {		     
		      Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		      uniqid =Objects.toString(map.get("uniqid"));
		      source =Objects.toString(map.get("source"));
		    } else {
		        //Failover//
		        String uniqueId = transactionID + "-" + remoteAddress + "-" + System.nanoTime();
		        //Generate MD//
		        String plainText = uniqueId;
		        MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
		        mdAlgorithm.update(plainText.getBytes());
		        byte[] digest = mdAlgorithm.digest();
		        StringBuffer hexString = new StringBuffer();
		        for (int i = 0; i < digest.length; i++) {
		            plainText = Integer.toHexString(0xFF & digest[i]);
		            if (plainText.length() < 2) {
		                plainText = "0" + plainText;
		            }
		            hexString.append(plainText);
		        }
		        uniqid = hexString.toString();
		        StringBuilder failoverSource=new StringBuilder("");
		        failoverSource.append("(function(s, o, u, r, k){");
		        failoverSource.append("b = s.URL;");
		        failoverSource.append("v = (b.substr(b.indexOf(r)).replace(r + \"=\", \"\")).toString();");
		        failoverSource.append("r = (v.indexOf(\"&\") !== -1) ? v.split(\"&\")[0] : v;");
		        failoverSource.append("a = s.createElement(o),");
		        failoverSource.append("m = s.getElementsByTagName(o)[0];");
		        failoverSource.append("a.async = 1;");
		        failoverSource.append("a.setAttribute(\"crossorigin\", \"anonymous\");");
		        failoverSource.append("a.src = u+'script.js?ak='+k+'&lpi='+r+'&lpu='+encodeURIComponent(b);");
		        failoverSource.append("m.parentNode.insertBefore(a, m);");
		        failoverSource.append("})(document, 'script', '"+apiSnippetUrl+"', 'token', '"+serviceKey+"');");
		        source = failoverSource.toString();
		    }
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			bizaoSecurityValidateTrans.setSource(source);
			bizaoSecurityValidateTrans.setUniqueId(uniqid);
			logger.info("bizaoSecurityValidateTrans:::: "+bizaoSecurityValidateTrans);
			jmsService.saveObject(bizaoSecurityValidateTrans);
		}
		return bizaoSecurityValidateTrans;
		    
	} 
	
	public boolean isValidSecurity(
			String bizaoAlias,String uniqeId,String token){
		
		BizaoSecurityValidateTrans bizaoSecurityValidateTrans=null;
		boolean clear=false;
		try{
			
			bizaoSecurityValidateTrans=new BizaoSecurityValidateTrans(true);
			bizaoSecurityValidateTrans.setBizaoAlias(bizaoAlias);
			bizaoSecurityValidateTrans.setToken(token);			
		Map<String,String> headerMap=new HashMap<String,String>();		
		headerMap.put("Content-Type","application/x-www-form-urlencoded");
		headerMap.put("cache-control","no-cache");
		logger.info("isValidSecurity:: "+headerMap);
		Map<String,String> data=new HashMap<String,String>();
		data.put("uniqid",uniqeId);
		bizaoSecurityValidateTrans.setRequestStr("http://uk.block.shield.monitoringservice.co/appblock"+
			 data.toString()+headerMap);
		HTTPResponse  httpResponse=httpURLConnectionUtil.
				sendPostRequest("http://uk.block.shield.monitoringservice.co/appblock",data,headerMap);
		bizaoSecurityValidateTrans.setResponseStr(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
		Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
	    String action=(String)map.get("action");
	    bizaoSecurityValidateTrans.setAction(action);
	    logger.info("action::: "+action);
	    if(action.equalsIgnoreCase("Clear")||
	    		action.equalsIgnoreCase("Suspect")){
	    	clear=true;
	    }
		
		}catch(Exception ex){
			logger.error("isValidSecurity:: ",ex);
		}finally{
			logger.info("bizaoSecurityValidateTrans:::: "+bizaoSecurityValidateTrans);
			daoService.saveObject(bizaoSecurityValidateTrans);
		}
		return clear;
	}
	
}
