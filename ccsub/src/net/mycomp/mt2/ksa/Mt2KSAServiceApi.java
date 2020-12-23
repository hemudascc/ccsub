package net.mycomp.mt2.ksa;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service("mt2KSAServiceApi")
public class Mt2KSAServiceApi  {

	private static final Logger logger = Logger
			.getLogger(Mt2KSAServiceApi.class.getName());
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JMSMt2KSAService jmsMt2KSAService;
	
	@Autowired
	private RedisCacheService redisCacheService; 
	
	private final String publicKey;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	public Mt2KSAServiceApi(@Value("${mt2.ksa.pulic.key}")String publicKey){
		this.publicKey=publicKey;
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
//	private static String generateTimestampAsString(Timestamp time)
//	{
//	return time.ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ss.fffZ");
//	}
//	
	
//	private static String GenerateNonce(String MethodName)
//	{
//	   return "MT2PAYKSACHARGE";
//	}
	
	public static String getSha1Hash(String password){
	try{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(password.getBytes("UTF-8"));
		return DatatypeConverter.printBase64Binary(md.digest());
	}catch(Exception ex){
		logger.error("getSha1Hash ",ex);
	}
	return null;
	}
	
	
	public static String generatePaswordHash(String nonce, String timestamp, String hashpassword, String
			key){
		try{
				String data = nonce + timestamp + hashpassword;
				byte[] DataInBytes = data.getBytes("UTF-8");
				byte[] PublicKeyInBytes = key.getBytes("UTF-8");
				SecretKeySpec keySpec = new SecretKeySpec(PublicKeyInBytes, "HmacSHA1");
				Mac mac = Mac.getInstance("HmacSHA1");
				mac.init(keySpec);
				byte[] result = mac.doFinal(DataInBytes);
				String outHash = DatatypeConverter.printBase64Binary(result);
				return outHash;
		}catch(Exception ex){
			logger.error("generateHash  ",ex);
		}
		   return null;
			}
	
	
	public Map<String,String> getHeaderMap(String msisdn,Mt2KSAServiceConfig mt2KSAServiceConfig,Timestamp createTime){
		Map<String,String> headerMap=new HashMap<String,String>();
		headerMap.put("Connection","Keep-Alive");
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept-Encoding", "gzip,deflate");
		headerMap.put("Authorization", "WSSE realm=MT2,profile=UsernameToken");
		headerMap.put("Host", "mt2-sa.com:1000");
		headerMap.put("X-WSSE", "UsernameToken Username=\""+mt2KSAServiceConfig.getMerchantId()+"\","
				+ "PasswordDigest=\""+generatePaswordHash(Mt2KSAConstant.NOUNCE_PIN, 
						Mt2KSAConstant.getFormatUTCDate(createTime),
						getSha1Hash(mt2KSAServiceConfig.getClientPassword())
				, publicKey)+"\",Nonce=\""+Mt2KSAConstant.NOUNCE_PIN+"\","
				+ "Created=\""+ Mt2KSAConstant.getFormatUTCDate(createTime)+"\"");
		headerMap.put("x-user-id", msisdn);
		headerMap.put("description", mt2KSAServiceConfig.getServiceName());
		return headerMap;
		
	}
	
	
public MT2KSAServiceApiTrans sendOTP(Mt2KSAServiceConfig mt2KSAServiceConfig,String msisdn,String token){
		
	MT2KSAServiceApiTrans mt2KSAServiceApiTrans=new MT2KSAServiceApiTrans(true,"SEND_OTP");
		try{
			mt2KSAServiceApiTrans.setToken(token);
			mt2KSAServiceApiTrans.setMsisdn(msisdn);
			
			if(StringUtils.isEmpty(msisdn)){
				mt2KSAServiceApiTrans.setRequest("Invalid msisdn. not proccess for api");
				return mt2KSAServiceApiTrans;
			}
			//http://mt2-sa.com:1000/api/{controller}/CollectCent/ZAINSA/subpin/<token>
			
			//http://mt2-sa.com:1000/api/dob/clt_test/zainsa/subpin/TEST_20172201342_2118/
			
			//Authorization=WSSErealm=MT2,profile=UsernameToken&Host=mt2-sa.com:1000&
			//X-WSSE=UsernameTokenUsername="2500118",PasswordDigest="{Password Digest}"
			//,Nonce="MT2PAYKSASUBPIN",Created="{Dated UTC}"&x-user-id={MSISDN}
			
		String url=mt2KSAServiceConfig.getSendPinUrl()
				.replaceAll("<token>",MUtility.urlEncoding(""+mt2KSAServiceApiTrans.getId()));
		logger.info("sendOTP:: request url "+url);
		Map<String,String> headerMap=new HashMap<String,String>();
		headerMap.put("Connection","Keep-Alive");
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept-Encoding", "gzip,deflate");
		headerMap.put("Authorization", "WSSE realm=MT2,profile=UsernameToken");
		headerMap.put("Host", "mt2-sa.com:1000");
		headerMap.put("X-WSSE", "UsernameToken Username=\""+mt2KSAServiceConfig.getMerchantId()+"\","
				+ "PasswordDigest=\""+generatePaswordHash(Mt2KSAConstant.NOUNCE_PIN, Mt2KSAConstant.getFormatUTCDate(mt2KSAServiceApiTrans.getCreateTime()),
						getSha1Hash(mt2KSAServiceConfig.getClientPassword())
				, publicKey)+"\",Nonce=\""+Mt2KSAConstant.NOUNCE_PIN+"\","
				+ "Created=\""+ Mt2KSAConstant.getFormatUTCDate(mt2KSAServiceApiTrans.getCreateTime())+"\"");
		
		headerMap.put("x-user-id", msisdn);
		headerMap.put("amount", ""+mt2KSAServiceConfig.getPricePoint().intValue());
		headerMap.put("description", mt2KSAServiceConfig.getServiceName());
		
		mt2KSAServiceApiTrans.setRequest(url+" : "+headerMap);
		
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url,headerMap);
		logger.info("sendOTP:: httpResponse  "+httpResponse);
		mt2KSAServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2KSAServiceApiTrans.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			//{"ResponseResult":{"ID":"zEr1503PsXSErlJkOVo1882","Token":"TEST_20172201342_2118",
			//	"Status":"ERROR","ReasonCode":"1","Message":"DuplicateTokenID"}}
		     Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		     Map respoonseResultMap=(Map)map.get("ResponseResult");
		     if(Objects.toString(respoonseResultMap.get("ReasonCode")).equalsIgnoreCase("0")){
		    	 mt2KSAServiceApiTrans.setSuccess(true);	
		    	 mt2KSAServiceApiTrans.setResponseToCaller(true);
		    	 redisCacheService.putObjectCacheValueByEvictionMinute(Mt2KSAConstant.MT2KSA_OTP_TRXID_PREFIX+msisdn,
		    			 Objects.toString(respoonseResultMap.get("ID")),10);
		     }
		}	
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(mt2KSAServiceApiTrans);
		 }
		return mt2KSAServiceApiTrans;		
	}


public MT2KSAServiceApiTrans validateOTP(Mt2KSAServiceConfig mt2KSAServiceConfig,String msisdn,
		String token,String otp){
	
	MT2KSAServiceApiTrans mt2KSAServiceApiTrans=new MT2KSAServiceApiTrans(true,Mt2KSAConstant.VALIDATE_OTP);
		try{
			mt2KSAServiceApiTrans.setToken(token);
			mt2KSAServiceApiTrans.setMsisdn(msisdn);
			
			if(StringUtils.isEmpty(msisdn)){
				mt2KSAServiceApiTrans.setRequest("Invalid msisdn. not proccess for api");
				return mt2KSAServiceApiTrans;
			}
			
			//http://mt2-sa.com:1000/api/{controller}/CollectCent/ZAINSA/subpin/<token>
			
			//http://mt2-sa.com:1000/api/dob/clt_test/zainsa/subpin/TEST_20172201342_2118/
			
			//Authorization=WSSErealm=MT2,profile=UsernameToken&Host=mt2-sa.com:1000&
			//X-WSSE=UsernameTokenUsername="2500118",PasswordDigest="{Password Digest}"
			//,Nonce="MT2PAYKSASUBPIN",Created="{Dated UTC}"&x-user-id={MSISDN}
			
		String url=mt2KSAServiceConfig.getValidatePinUrl()
				.replaceAll("<token>",MUtility.urlEncoding(""+mt2KSAServiceApiTrans.getId()));
		logger.info("sendOTP:: request url "+url);
		Map<String,String> headerMap=new HashMap<String,String>();
		headerMap.put("Connection","Keep-Alive");
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept-Encoding", "gzip,deflate");
		headerMap.put("Authorization", "WSSE realm=MT2,profile=UsernameToken");
		headerMap.put("Host", "mt2-sa.com:1000");
		headerMap.put("X-WSSE", "UsernameToken Username=\""+mt2KSAServiceConfig.getMerchantId()+"\","
				+ "PasswordDigest=\""+generatePaswordHash(Mt2KSAConstant.NOUNCE_PIN, Mt2KSAConstant.getFormatUTCDate(mt2KSAServiceApiTrans.getCreateTime()),
				getSha1Hash(mt2KSAServiceConfig.getClientPassword())
				, publicKey)+"\",Nonce=\""+Mt2KSAConstant.NOUNCE_PIN+"\","
				+ "Created=\""+ Mt2KSAConstant.getFormatUTCDate(mt2KSAServiceApiTrans.getCreateTime())+"\"");
		headerMap.put("x-user-id", msisdn);
		headerMap.put("amount", ""+mt2KSAServiceConfig.getPricePoint().intValue());
		headerMap.put("description", mt2KSAServiceConfig.getServiceName());
		
		//headerMap.put("Pincode",otp);
		//headerMap.put("ReferenceID",Objects.toString(redisCacheService.getObjectCacheValue(Mt2KSAConstant.MT2KSA_OTP_TRXID_PREFIX+msisdn) ));
		
	
		//Map<String,Object> dataMap=new HashMap<String,Object>();
		Map<String,Object> dataMap=new HashMap<String,Object>();
		dataMap.put("PinCode",otp);
		dataMap.put("ReferenceID",Objects.toString(redisCacheService.getObjectCacheValue(Mt2KSAConstant.MT2KSA_OTP_TRXID_PREFIX+msisdn) ));
		dataMap.put("serviceID", mt2KSAServiceConfig.getMt2ServiceId());
		Map<String,Object> pinDataMap=new HashMap<String,Object>();		
		pinDataMap.put("PIN",dataMap);		
		Map<String,Object> subscribeRequestDataMap=new HashMap<String,Object>();		
		subscribeRequestDataMap.put("SubscribeRequest",pinDataMap);	
		
		Map<String,Object> transactionRequestDataMap=new HashMap<String,Object>();
		transactionRequestDataMap.put("TransactionRequest",subscribeRequestDataMap);
		//dataMap.putAll(subscribeRequestDataMap);
		
		String json=JsonMapper.getObjectToJson(transactionRequestDataMap);
		mt2KSAServiceApiTrans.setRequest(url+" : "+headerMap+" : "+json);
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url, json, headerMap);//HTTPGETRequest(url,null);
		logger.info("sendOTP:: httpResponse  "+httpResponse);
		mt2KSAServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2KSAServiceApiTrans.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			//{"ResponseResult":{"ID":"zEr1503PsXSErlJkOVo1882","Token":"TEST_20172201342_2118",
			//	"Status":"ERROR","ReasonCode":"1","Message":"DuplicateTokenID"}}
		     Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		     Map respoonseResultMap=(Map)map.get("ResponseResult");
		     if(Objects.toString(respoonseResultMap.get("ReasonCode")).equalsIgnoreCase("0")){
		    	 mt2KSAServiceApiTrans.setSuccess(true);			    	 
		    	 mt2KSAServiceApiTrans.setResponseToCaller(true);
		     }
		}	
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(mt2KSAServiceApiTrans);
			 jmsMt2KSAService.processPinValidation(mt2KSAServiceApiTrans);
		 }
		return mt2KSAServiceApiTrans;		
	}

public MT2KSAServiceApiTrans subStatus(Mt2KSAServiceConfig mt2KSAServiceConfig,String msisdn
		,String token){
	
	MT2KSAServiceApiTrans mt2KSAServiceApiTrans=new MT2KSAServiceApiTrans(true,"SUB_STATUS");
		try{
			
			mt2KSAServiceApiTrans.setMsisdn(msisdn);
			mt2KSAServiceApiTrans.setToken(token);
			if(StringUtils.isEmpty(msisdn)){
				mt2KSAServiceApiTrans.setRequest("Invalid msisdn. not proccess for api");
				return mt2KSAServiceApiTrans;
			}
			
		String url=mt2KSAServiceConfig.getSubStatusUrl()
				.replaceAll("<token>",MUtility.urlEncoding(""+mt2KSAServiceApiTrans.getId()));
		logger.info("unsubscribe:: request url "+url);
		
		Map<String,String> headerMap=getHeaderMap(msisdn, mt2KSAServiceConfig, mt2KSAServiceApiTrans.getCreateTime());
	         
		mt2KSAServiceApiTrans.setRequest(url+" : "+headerMap );
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url, headerMap);//TPPOSTRequest(url, null, headerMap);//HTTPGETRequest(url,null);
		logger.info("unsubscribe:: httpResponse  "+httpResponse);
		mt2KSAServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2KSAServiceApiTrans.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			//{"ResponseResult":{"ID":"zEr1503PsXSErlJkOVo1882","Token":"TEST_20172201342_2118",
			//	"Status":"ERROR","ReasonCode":"1","Message":"DuplicateTokenID"}}
			 mt2KSAServiceApiTrans.setSuccess(true);
		     Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		     Map respoonseResultMap=(Map)map.get("ResponseResult");
		     if(Objects.toString(respoonseResultMap.get("ReasonCode")).equalsIgnoreCase("0")
		    		 &&Objects.toString(respoonseResultMap.get("Message")).equalsIgnoreCase("Active")){		    				    	 
		    	 mt2KSAServiceApiTrans.setResponseToCaller(true);
		     }
		}	
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(mt2KSAServiceApiTrans);
		 }
		return mt2KSAServiceApiTrans;		
	}

public MT2KSAServiceApiTrans unsubscribe(Mt2KSAServiceConfig mt2KSAServiceConfig
		,String msisdn){
	
	MT2KSAServiceApiTrans mt2KSAServiceApiTrans=new MT2KSAServiceApiTrans(true,"UNSUBSCRIBED");
		try{
			//mt2KSAServiceApiTrans.setToken(token);
			mt2KSAServiceApiTrans.setMsisdn(msisdn);
			
			if(StringUtils.isEmpty(msisdn)){
				mt2KSAServiceApiTrans.setRequest("Invalid msisdn. not proccess for api");
				return mt2KSAServiceApiTrans;
			}
			
		String url=mt2KSAServiceConfig.getUnsubUrl()
				.replaceAll("<token>",MUtility.urlEncoding(""+mt2KSAServiceApiTrans.getId()));
		logger.info("unsubscribe:: request url "+url);
		
		Map<String,String> headerMap=getHeaderMap(msisdn, mt2KSAServiceConfig, mt2KSAServiceApiTrans.getCreateTime());
	         
		mt2KSAServiceApiTrans.setRequest(url+" : "+headerMap );
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPGETRequest(url, headerMap);//HTTPPOSTRequest(url, null, headerMap);//HTTPGETRequest(url,null);
		logger.info("unsubscribe:: httpResponse  "+httpResponse);
		mt2KSAServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2KSAServiceApiTrans.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			//{"ResponseResult":{"ID":"zEr1503PsXSErlJkOVo1882","Token":"TEST_20172201342_2118",
			//	"Status":"ERROR","ReasonCode":"1","Message":"DuplicateTokenID"}}
		     Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		     Map respoonseResultMap=(Map)map.get("ResponseResult");
		     if(Objects.toString(respoonseResultMap.get("ReasonCode")).equalsIgnoreCase("0")){
		    	 mt2KSAServiceApiTrans.setSuccess(true);			    	 
		    	 mt2KSAServiceApiTrans.setResponseToCaller(true);
		     }
		}	
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(mt2KSAServiceApiTrans);
		 }
		return mt2KSAServiceApiTrans;		
	}
	

public MT2KSAServiceApiTrans sendMTSMS(Mt2KSAServiceConfig mt2KSAServiceConfig,
		String msisdn,String token,String msg,String action){
	
	MT2KSAServiceApiTrans mt2KSAServiceApiTrans=new MT2KSAServiceApiTrans(true,action);
		try{
			mt2KSAServiceApiTrans.setToken(token);
			mt2KSAServiceApiTrans.setMsisdn(msisdn);
			
			if(StringUtils.isEmpty(msisdn)){
				mt2KSAServiceApiTrans.setRequest("Invalid msisdn. not proccess for api");
				return mt2KSAServiceApiTrans;
			}
			
		String url=mt2KSAServiceConfig.getSendSmsUrl()
				.replaceAll("<token>",MUtility.urlEncoding(""+mt2KSAServiceApiTrans.getId()));
		logger.info("unsubscribe:: request url "+url);
		
		Map<String,String> headerMap=getHeaderMap(msisdn, mt2KSAServiceConfig, mt2KSAServiceApiTrans.getCreateTime());
	         //{"TransactionRequest": {	" SendSMSRequest ": {"SMSMessage": "Content to be sent}	}}
		Map<String,String> sendSMSRequest=new HashMap<String,String>();
		sendSMSRequest.put("SMSMessage", msg);
		Map<String,Object> transactionRequest=new HashMap<String,Object>();
		transactionRequest.put("TransactionRequest", sendSMSRequest);
		String json=JsonMapper.getObjectToJson(transactionRequest);
		mt2KSAServiceApiTrans.setRequest(url+" : "+headerMap+" : "+json );
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url, json, headerMap);//HTTPGETRequest(url,null);
		logger.info("unsubscribe:: httpResponse  "+httpResponse);
		mt2KSAServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2KSAServiceApiTrans.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			//{"ResponseResult":{"ID":"zEr1503PsXSErlJkOVo1882","Token":"TEST_20172201342_2118",
			//	"Status":"ERROR","ReasonCode":"1","Message":"DuplicateTokenID"}}
		     Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		     Map respoonseResultMap=(Map)map.get("ResponseResult");
		     if(Objects.toString(respoonseResultMap.get("ReasonCode")).equalsIgnoreCase("0")){
		    	 mt2KSAServiceApiTrans.setSuccess(true);			
		    	 mt2KSAServiceApiTrans.setResponseToCaller(true);
		    	
		     }
		}	
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(mt2KSAServiceApiTrans);
		 }
		return mt2KSAServiceApiTrans;		
	}

public MT2KSAServiceApiTrans addContent(Mt2KSAServiceConfig mt2KSAServiceConfig,
		String msisdn,String token,String msg){
	
	MT2KSAServiceApiTrans mt2KSAServiceApiTrans=new MT2KSAServiceApiTrans(true,"ADD_CONTENT");
		try{
			mt2KSAServiceApiTrans.setToken(token);
			mt2KSAServiceApiTrans.setMsisdn(msisdn);
			
			if(StringUtils.isEmpty(msisdn)){
				mt2KSAServiceApiTrans.setRequest("Invalid msisdn. not proccess for api");
				return mt2KSAServiceApiTrans;
			}
			
		String url=mt2KSAServiceConfig.getAddContentUrl()
				.replaceAll("<token>",MUtility.urlEncoding(""+mt2KSAServiceApiTrans.getId()));
		logger.info("unsubscribe:: request url "+url);
		
		Map<String,String> headerMap=getHeaderMap(msisdn, mt2KSAServiceConfig, mt2KSAServiceApiTrans.getCreateTime());
	         //{"TransactionRequest": {"Content": {	"Message":
          //"Content to be sent","Date": "2017-01-01 12:01:01.001"}}}
		
		Map<String,String> contentMap=new HashMap<String,String>();
		contentMap.put("Message", msg);
		contentMap.put("Date", Mt2KSAConstant.getFormatUTCDate(mt2KSAServiceApiTrans.getCreateTime()));
		
		Map<String,Object> transactionRequest=new HashMap<String,Object>();
		transactionRequest.put("TransactionRequest", contentMap);
		String json=JsonMapper.getObjectToJson(transactionRequest);
		mt2KSAServiceApiTrans.setRequest(url+" : "+headerMap+" : "+json );
		HTTPResponse httpResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url, json, headerMap);//HTTPGETRequest(url,null);
		logger.info("unsubscribe:: httpResponse  "+httpResponse);
		mt2KSAServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
		mt2KSAServiceApiTrans.setResponse(httpResponse.getResponseStr());
		
		if(httpResponse.getResponseCode()==200){
			//{"ResponseResult":{"ID":"zEr1503PsXSErlJkOVo1882","Token":"TEST_20172201342_2118",
			//	"Status":"ERROR","ReasonCode":"1","Message":"DuplicateTokenID"}}
		     Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		     Map respoonseResultMap=(Map)map.get("ResponseResult");
		     if(Objects.toString(respoonseResultMap.get("ReasonCode")).equalsIgnoreCase("0")){
		    	 mt2KSAServiceApiTrans.setSuccess(true);			    	 
		    	 mt2KSAServiceApiTrans.setResponseToCaller(true);
		     }
		}	
	
		 }catch(Exception ex){
			 logger.error("exception ",ex);
		 }finally{
			 daoService.updateObject(mt2KSAServiceApiTrans);
		 }
		return mt2KSAServiceApiTrans;		
	}


}
