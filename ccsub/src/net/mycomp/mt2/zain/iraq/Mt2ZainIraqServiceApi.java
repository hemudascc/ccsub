package net.mycomp.mt2.zain.iraq;

import java.security.MessageDigest;
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


@Service("mt2ZainIraqServiceApi")
public class Mt2ZainIraqServiceApi  {

	private static final Logger logger = Logger
			.getLogger(Mt2ZainIraqServiceApi.class.getName());
	
	private final String smsUrlTemplate;
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService; 
	
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	

@Autowired
public Mt2ZainIraqServiceApi(@Value("${mt2.zain.iraq.sms.url.template}")String smsUrlTemplate){
		
		this.smsUrlTemplate=smsUrlTemplate;
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	

	
	public  String getSha1Hash(String password){
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
	
	public MT2ZainIraqServiceApiTrans getScriptSource(Mt2ZainIraqServiceConfig mt2ZainIraqServiceConfig
			,String token,
			Map<String,String> headerMap,String sourceIp,String url){
		
		MT2ZainIraqServiceApiTrans mt2ZainIraqServiceApiTrans=new MT2ZainIraqServiceApiTrans(true,"Source");
		try{
			mt2ZainIraqServiceApiTrans.setToken(token);
			String apiUrl=mt2ZainIraqServiceConfig.getApiUrl()
					.replaceAll("<servicekey>", mt2ZainIraqServiceConfig.getZainServiceKey())
						.replaceAll("<token>",token+mt2ZainIraqServiceApiTrans.getId());
			logger.info("getScriptSource:: apiUrl:: "+apiUrl);
			
			headerMap.remove("Upgrade-Insecure-Requests");
			String urlencodeHeaderjson=MUtility.urlEncoding(JsonMapper.getObjectToJson(headerMap));
			Map<String,String> requestHeaderMap=new HashMap<String,String>();
			//requestHeaderMap.put("user_agent", headerMap.get(""));
			requestHeaderMap.put("timeout","5");
			String userAgent=headerMap.get("user-agent");
			if(userAgent==null){
				 userAgent=headerMap.get("User_agent");
			}
			if(userAgent==null){
				 userAgent=headerMap.get("User-Agent");
			}
			//requestHeaderMap.put("user_agent", userAgent);
			requestHeaderMap.put("User-Agent", userAgent);
 			String queryString=
 					"lpu="+MUtility.urlEncoding(url)
 					+"&timestamp="+System.nanoTime()
 					+"&user_ip="+sourceIp+"&head="+urlencodeHeaderjson;
 			mt2ZainIraqServiceApiTrans.setRequest(apiUrl+"?"+queryString+" : "+requestHeaderMap);
 			
 			logger.info("getScriptSource:: requestHeaderMap:: "+requestHeaderMap+" , queryString:: "+queryString);
 			HTTPResponse httpResponse=	httpURLConnectionUtil.sendHttpsGet(apiUrl+"?"+queryString, requestHeaderMap);
 			logger.info("getScriptSource:: httpResponse:: "+httpResponse);
 			mt2ZainIraqServiceApiTrans.setResponse(httpResponse.getResponseStr());
 			mt2ZainIraqServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
 			if(httpResponse.getResponseCode()==200){
 				
 				Map map=JsonMapper.getJsonToObject(mt2ZainIraqServiceApiTrans.getResponse(), Map.class);
				mt2ZainIraqServiceApiTrans.setUniqueId(Objects.toString(map.get("uniqid")));	
				mt2ZainIraqServiceApiTrans.setSource(Objects.toString(map.get("source")));
 				mt2ZainIraqServiceApiTrans.setResponseToCaller(true);				
			}

		}catch(Exception ex){
			logger.error("getScriptSource:::::: ",ex);
		}finally{
		//	daoService.updateObject(mt2ZainIraqServiceApiTrans);
		}
		return mt2ZainIraqServiceApiTrans;		
	}
	
	
	public MT2ZainIraqServiceApiTrans sendContentSms(String msisdn
			,String msg,String token,String action){
	
	//	http://web.mt2.com.lb:2012/SendMT.aspx?login=<login>&pwd=<pwd>&orig=<orig>
	//		&coding=<coding>&smsid=<smsid>&data=<data>&recipient=<recipient>&u
	//		serid=<userid>
			
		MT2ZainIraqServiceApiTrans mt2ZainIraqServiceApiTrans=new MT2ZainIraqServiceApiTrans(true,action+"_SMS");
		
		try{
			
			mt2ZainIraqServiceApiTrans.setMsisdn(msisdn);	
			mt2ZainIraqServiceApiTrans.setToken(token);
		    String coding="txt";
			
			
	String url=smsUrlTemplate
			           //.replaceAll("<login>", MUtility.urlEncoding(numeroAsiacellConfig.getLogin()))
						//.replaceAll("<pwd>", MUtility.urlEncoding(numeroAsiacellConfig.getPwd()))
						//.replaceAll("<recipient>", MUtility.urlEncoding(numeroAsiacellConfig.getRecipient()))
						.replaceAll("<orig>", MUtility.urlEncoding(msisdn))
						.replaceAll("<coding>", MUtility.urlEncoding(coding))
						.replaceAll("<smsid>", MUtility.urlEncoding(""+mt2ZainIraqServiceApiTrans.getId()))
						.replaceAll("<data>", MUtility.urlEncoding(msg))
					//	.replaceAll("<userid>", MUtility.urlEncoding(numeroAsiacellConfig.getUserId()))
						//.replaceAll("<crweb>", MUtility.urlEncoding(numeroAsiacellConfig.getCrweb()))
						;
						
	mt2ZainIraqServiceApiTrans.setRequest(url);	
	
	
	 HTTPResponse httpResponse=httpURLConnectionUtil.invokeGetURL(url);
	 mt2ZainIraqServiceApiTrans.setResponseCode(httpResponse.getResponseCode());
	 mt2ZainIraqServiceApiTrans.setResponse(httpResponse.getResponseStr());
	 
	 if(httpResponse.getResponseCode()==200&&MUtility.toInt(httpResponse.getResponseStr(), -1)>0){
		 mt2ZainIraqServiceApiTrans.setResponseToCaller(true);
		 
	 }
	     }catch(Exception ex){
			logger.error("sendSms:: ",ex);
		}finally{
			daoService.updateObject(mt2ZainIraqServiceApiTrans);
		}
		return mt2ZainIraqServiceApiTrans;
	}
	
}
