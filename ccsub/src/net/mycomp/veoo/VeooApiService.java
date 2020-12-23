package net.mycomp.veoo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.common.jms.JMSService;
import net.common.service.IDaoService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MUtility;

@Service("veooApiService")
public class VeooApiService {

	private static final Logger logger = Logger
			.getLogger(VeooApiService.class.getName());
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	//@Value("${veoo.username}")
	private final String userName;
	//@Value("${veoo.password}")
	private final String password;
	//@Value("${veoo.premium.message.url}")
	private final String premiumMessageUrl;
	
	private final String  pinValidateApiUrl;
	private final  String pinSendApiUrl;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	public VeooApiService(@Value("${veoo.username}")String userName,
			@Value("${veoo.password}")String password,
			@Value("${veoo.premium.message.url}")String premiumMessageUrl,
			@Value("${veoo.pin.send.api}")String  pinSendApiUrl,
			@Value("${veoo.pin.validaion.api}")String  pinValidateApiUrl
			){
		this.userName=userName;
		this.password=password;
		this.premiumMessageUrl=premiumMessageUrl;
		this.pinSendApiUrl=pinSendApiUrl;
		this.pinValidateApiUrl=pinValidateApiUrl;
		
		
	}
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public VeooClickFlowUrlResponse callClickFlowUrl(VeooServiceConfig veooServiceConfig,
			String token){
		
		VeooClickFlowUrlResponseTrans veooClickFlowUrlResponseTrans=new VeooClickFlowUrlResponseTrans(true);
		VeooClickFlowUrlResponse veooClickFlowUrlResponse=null;
		try{
			
			veooClickFlowUrlResponseTrans.setTokenToCg(token);
		String url=veooServiceConfig.getClickFlowUrl()
				.replaceAll("<username>", MUtility.urlEncoding(userName))
				.replaceAll("<password>", MUtility.urlEncoding(password));
		
//		/https://sapi.veoo.com/lists/7da2c0ab-7f0b-11e8-b0a0-22000ac4c542/
		//subscribers?username=<username>&password=<password>
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("tigo_clickflow", Boolean.TRUE);
		map.put("network", veooServiceConfig.getCgNetwork());
		map.put("token", token);//change after 06-04-2019
		String json=JsonMapper.getObjectToJson(map);
		veooClickFlowUrlResponseTrans.setRequest(url+": "+json);
		HTTPResponse httpPResponse=httpURLConnectionUtil.sendHttpsPost(url, json);
		logger.info("sendSubscriberList:: request url: "+url+", data: "+json+" , response:: "+httpPResponse);
		veooClickFlowUrlResponseTrans.setResponse(httpPResponse.getResponseCode()+":"+httpPResponse.getResponseStr());
		if(httpPResponse.getResponseCode()==200){
			veooClickFlowUrlResponse=JsonMapper.getJsonToObject(httpPResponse.getResponseStr(),
					VeooClickFlowUrlResponse.class);
			veooClickFlowUrlResponseTrans.setResult(veooClickFlowUrlResponse.getResult());
			veooClickFlowUrlResponseTrans.setClickFlowResponseStatus(veooClickFlowUrlResponse.getStatus());
			veooClickFlowUrlResponseTrans.setRedirectUrl(veooClickFlowUrlResponse.getSpecial().getRedirectUrl());
		}
		}catch(Exception ex){
			logger.error("sendSubscriberList:: ",ex);			
		}finally{
			jmsService.saveObject(veooClickFlowUrlResponseTrans);
		}
		return veooClickFlowUrlResponse;
	}
	
	
public VeooClickFlowUrlResponse callClickFlowUrlTmp(VeooServiceConfig veooServiceConfig,
		String token,String msisdn){
		
		VeooClickFlowUrlResponse veooClickFlowUrlResponse=null;
		try{
		//String url=	"https://sapi.veoo.com/lists/7da2c0ab-7f0b-11e8-b0a0-22000ac4c542/subscribers/"+msisdn+"?username=<username>&password=<password>";
			String url=veooServiceConfig.getClickFlowUrl()
				.replaceAll("<username>", MUtility.urlEncoding(userName))
				.replaceAll("<password>", MUtility.urlEncoding(password));
		
//		/https://sapi.veoo.com/lists/7da2c0ab-7f0b-11e8-b0a0-22000ac4c542/
		//subscribers?username=<username>&password=<password>
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("tigo_clickflow", Boolean.TRUE);
		map.put("network", veooServiceConfig.getCgNetwork());
		map.put("token", token);//change after 06-04-2019
		String json=JsonMapper.getObjectToJson(map);
		HTTPResponse httpPResponse=httpURLConnectionUtil.sendHttpsPost(url, json);
		logger.info("callClickFlowUrlTmp2:: sendSubscriberList:: request url: "+url+", data: "+json+" , response:: "+httpPResponse);
		if(httpPResponse.getResponseCode()==200){
			veooClickFlowUrlResponse=JsonMapper.getJsonToObject(httpPResponse.getResponseStr(),
					VeooClickFlowUrlResponse.class);
		}
		}catch(Exception ex){
			logger.error("callClickFlowUrlTmp2:: sendSubscriberList:: ",ex);
			
		}
		return veooClickFlowUrlResponse;
	}
	
public VeooMtMessage sendFreeMTMessage(String action,VeooServiceConfig veooServiceConfig,
		String msisdn,String shortCode,String msg){
	
	VeooMtMessage veooMtMessage=null;
		try{
		
			if(msg==null){
				logger.info(" mt message is not configured for "+msisdn+" "+veooServiceConfig+" ,shortCode:  "+shortCode);
				return null;
			}
					
			veooMtMessage=new VeooMtMessage(true);
			veooMtMessage.setAction(action);
			veooMtMessage.setUserName(userName);
			veooMtMessage.setPassword(password);
			veooMtMessage.setMsisdn(msisdn);
			veooMtMessage.setSource(shortCode);
			veooMtMessage.setMessage(msg);
			veooMtMessage.setNetwork(veooServiceConfig.getCgNetwork());			
			veooMtMessage.setVeooServiceId(veooServiceConfig.getVeooServiceId());
			veooMtMessage.setType("free");
			
			String url=premiumMessageUrl+
			"?username="+MUtility.urlEncoding(userName)+
			"&password="+MUtility.urlEncoding(password)+
			"&source="+MUtility.urlEncoding(veooMtMessage.getSource())+
			"&msisdn="+MUtility.urlEncoding(veooMtMessage.getMsisdn())+
			"&message="+MUtility.urlEncoding(veooMtMessage.getMessage())+
			"&network="+MUtility.urlEncoding(veooMtMessage.getNetwork())+
			"&service_id="+MUtility.urlEncoding(veooMtMessage.getVeooServiceId())+
			"&type="+MUtility.urlEncoding(veooMtMessage.getType())+
			"&id="+MUtility.urlEncoding(String.valueOf(veooMtMessage.getId()));
			veooMtMessage.setRequestStr(url);
			
		HTTPResponse httpPResponse=httpURLConnectionUtil.sendHttpsGet(url);	
		logger.info("sendFreeMTMessage:: request url: "+url+" , response:: "+httpPResponse);
		veooMtMessage.setResponse(httpPResponse.getResponseCode()+":"+httpPResponse.getResponseStr());
		
		}catch(Exception ex){
			logger.error("sendSubscriberList:: ",ex);
			
		}finally{
			daoService.updateObject(veooMtMessage);
		}
		return veooMtMessage;
	}

public VeooMtMessage sendFreeMTMessage(String action,VeooServiceConfig veooServiceConfig,
		String msisdn,String shortCode){
		
	VeooMtMessage veooMtMessage=null;
		try{
		
			if(veooServiceConfig.getWelcomeMessageTemplate()==null){
				logger.info("welcome mt message is not configured for "+veooServiceConfig+" ,msisdn:  "+msisdn+", shortCode:: "+shortCode);
				return null;
			}
					
			veooMtMessage=new VeooMtMessage(true);
			veooMtMessage.setAction(action);
			veooMtMessage.setUserName(userName);
			veooMtMessage.setPassword(password);
			veooMtMessage.setMsisdn(msisdn);
			veooMtMessage.setSource(shortCode);
			veooMtMessage.setMessage(veooServiceConfig.getWelcomeMessageTemplate());
			veooMtMessage.setNetwork(veooServiceConfig.getCgNetwork());			
			veooMtMessage.setVeooServiceId(veooServiceConfig.getVeooServiceId());
			veooMtMessage.setType("free");
			
			String url=premiumMessageUrl+
			"?username="+MUtility.urlEncoding(userName)+
			"&password="+MUtility.urlEncoding(password)+
			"&source="+MUtility.urlEncoding(veooMtMessage.getSource())+
			"&msisdn="+MUtility.urlEncoding(veooMtMessage.getMsisdn())+
			"&message="+MUtility.urlEncoding(veooMtMessage.getMessage())+
			"&network="+MUtility.urlEncoding(veooMtMessage.getNetwork())+
			"&service_id="+MUtility.urlEncoding(veooMtMessage.getVeooServiceId())+
			"&type="+MUtility.urlEncoding(veooMtMessage.getType())+
			"&id="+MUtility.urlEncoding(String.valueOf(veooMtMessage.getId()));
			veooMtMessage.setRequestStr(url);
			
		HTTPResponse httpPResponse=httpURLConnectionUtil.sendHttpsGet(url);	
		logger.info("sendFreeMTMessage:: request url: "+url+" , response:: "+httpPResponse);
		veooMtMessage.setResponse(httpPResponse.getResponseCode()+":"+httpPResponse.getResponseStr());
		
		}catch(Exception ex){
			logger.error("sendSubscriberList:: ",ex);
			
		}finally{
			daoService.updateObject(veooMtMessage);
		}
		return veooMtMessage;
	}




public VeooMtMessage sendPremiumMTMessage(String action,VeooServiceConfig veooServiceConfig,
		String msisdn){
		
	VeooMtMessage veooMtMessage=null;
		try{
			String msg=veooServiceConfig.getContentMessageTemplate().
					replaceAll("<msisdn>", msisdn);
			
			veooMtMessage=new VeooMtMessage(true);
			veooMtMessage.setAction(action);			
			veooMtMessage.setUserName(userName);
			veooMtMessage.setPassword(password);
			veooMtMessage.setMsisdn(msisdn);
			veooMtMessage.setSource(veooServiceConfig.getShortCode());
			veooMtMessage.setMessage(msg);
			veooMtMessage.setNetwork(veooServiceConfig.getCgNetwork());
			veooMtMessage.setNetworkId(veooServiceConfig.getCgNetwork());
			veooMtMessage.setVeooServiceId(veooServiceConfig.getVeooServiceId());
			veooMtMessage.setType("paid");
			String url=premiumMessageUrl+
			"?username="+MUtility.urlEncoding(userName)+
			"&password="+MUtility.urlEncoding(password)+
			"&source="+MUtility.urlEncoding(veooMtMessage.getSource())+
			"&msisdn="+MUtility.urlEncoding(veooMtMessage.getMsisdn())+
			"&message="+MUtility.urlEncoding(veooMtMessage.getMessage())+
			"&network="+MUtility.urlEncoding(veooMtMessage.getNetwork())+
			"&service_id="+MUtility.urlEncoding(veooMtMessage.getVeooServiceId())+
			"&type="+MUtility.urlEncoding(veooMtMessage.getType())+
			"&id="+MUtility.urlEncoding(String.valueOf(veooMtMessage.getId()));
			veooMtMessage.setRequestStr(url);			
		HTTPResponse httpPResponse=httpURLConnectionUtil.makeHTTPSRequest(url);	
		logger.info("sendPremiumMTMessage:: request url: "+url+" , response:: "+httpPResponse);
		veooMtMessage.setResponse(httpPResponse.getResponseCode()+":"+httpPResponse.getResponseStr());
		
		}catch(Exception ex){
			logger.error("sendPremiumMTMessage:: ",ex);
			
		}finally{
			daoService.updateObject(veooMtMessage);
		}
		return veooMtMessage;
	}


public VeooMtMessage sendFreeDCTMessage(String action,VeooServiceConfig veooServiceConfig,String msisdn){
	
	VeooMtMessage veooMtMessage=null;
		try{
		
			if(veooServiceConfig.getWelcomeMessageTemplate()==null){
				logger.info("welcome mt message is not configured for "+veooServiceConfig+" ,msisdn:  "+msisdn);
				return null;
			}
			
			
					
			veooMtMessage=new VeooMtMessage(true);
			veooMtMessage.setAction(action);
			veooMtMessage.setUserName(userName);
			veooMtMessage.setPassword(password);
			veooMtMessage.setMsisdn(msisdn);
			veooMtMessage.setSource(veooServiceConfig.getShortCode());
			veooMtMessage.setMessage(veooServiceConfig.getCacellationMessageTemplate());
			veooMtMessage.setNetwork(veooServiceConfig.getCgNetwork());			
			veooMtMessage.setVeooServiceId(veooServiceConfig.getVeooServiceId());
			veooMtMessage.setType("free");
			
			String url=premiumMessageUrl+
			"?username="+MUtility.urlEncoding(userName)+
			"&password="+MUtility.urlEncoding(password)+
			"&source="+MUtility.urlEncoding(veooMtMessage.getSource())+
			"&msisdn="+MUtility.urlEncoding(veooMtMessage.getMsisdn())+
			"&message="+MUtility.urlEncoding(veooMtMessage.getMessage())+
			"&network="+MUtility.urlEncoding(veooMtMessage.getNetwork())+
			"&service_id="+MUtility.urlEncoding(veooMtMessage.getVeooServiceId())+
			"&type="+MUtility.urlEncoding(veooMtMessage.getType())+
			"&id="+MUtility.urlEncoding(String.valueOf(veooMtMessage.getId()));
			veooMtMessage.setRequestStr(url);
			Map<String,String> headerMap=new HashMap<String,String>();
			headerMap.put("Content-Type", "application/xml");
		HTTPResponse httpPResponse=httpURLConnectionUtil.sendHttpsGet(url);
		logger.info("sendFreeMTMessage:: request url length:"+url.length()+" , response:: "+httpPResponse);
		logger.info("sendFreeMTMessage:: request url:"+url+" , response:: "+httpPResponse);
		veooMtMessage.setResponse(httpPResponse.getResponseCode()+":"+httpPResponse.getResponseStr());
		
		}catch(Exception ex){
			logger.error("sendSubscriberList:: ",ex);
			
		}finally{
			daoService.updateObject(veooMtMessage);
		}
		return veooMtMessage;
	}



public VeooPinSend sendPin(VeooServiceConfig veooServiceConfig,String msisdn){
	
	VeooPinSend veooPinSend=null;
		try{
		
			
			veooPinSend=new VeooPinSend(true);
			
			veooPinSend.setMsisdn(msisdn);
			///lists/<serviceid>/subscribers/<msisdn>
			//String pinSendApiUrl="https://sapi.veoo.com/lists/<serviceid>/subscribers/<msisdn>";
			
			String url=this.pinSendApiUrl.replaceAll("<serviceid>",MUtility.urlEncoding(
					veooServiceConfig.getVeooServiceId()))
					.replaceAll("<msisdn>", MUtility.urlEncoding(msisdn));
			url=url+"?username="+MUtility.urlEncoding(userName)+
			"&password="+MUtility.urlEncoding(password);
			
			Map<String,String> headerMap=new HashMap<String,String>();
			headerMap.put("Content-Type", "application/json");
			Map<String,Object> data=new HashMap<String,Object>();
			data.put("network", veooServiceConfig.getCgNetwork());
			String json=JsonMapper.getObjectToJson(data);
			veooPinSend.setRequestStr(url);
			
		HTTPResponse httpPResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url, json, headerMap);
		veooPinSend.setResponseStr(httpPResponse.getResponseCode()+":"+httpPResponse.getResponseStr());
		logger.info("sendPin:: request url length:"+url.length()+" , response:: "+httpPResponse);
		if(httpPResponse.getResponseCode()==200){
			Map responseMap= JsonMapper.getJsonToObject(httpPResponse.getResponseStr(), Map.class);
			veooPinSend.setPinStatus(Objects.toString(responseMap.get("status")));
			veooPinSend.setMessage(Objects.toString(responseMap.get("message")));
			veooPinSend.setResult(Objects.toString(responseMap.get("result")));
			veooPinSend.setType(Objects.toString(responseMap.get("type")));
			if(veooPinSend.getPinStatus().equalsIgnoreCase("success")){
				veooPinSend.setPinSend(true);
			}				
		}		
		}catch(Exception ex){
			logger.error("sendSubscriberList:: ",ex);
			
		}finally{
			daoService.saveObject(veooPinSend);
		}
		return veooPinSend;
	}


public VeooPinValidation pinValidation(VeooServiceConfig veooServiceConfig,String msisdn,String pin){
	
	VeooPinValidation veooPinValidation=null;
		try{
		
			
			veooPinValidation=new VeooPinValidation(true);
			
			veooPinValidation.setMsisdn(msisdn);
			veooPinValidation.setServiceId(veooServiceConfig.getServiceId());
			///lists/<serviceid>/subscribers/<msisdn>
			//String pinValidateApiUrl="https://sapi.veoo.com/lists/<serviceid>/subscribers/<msisdn>/verify/<pin>";
			
			String url=this.pinValidateApiUrl.replaceAll("<serviceid>",MUtility.urlEncoding(
					veooServiceConfig.getVeooServiceId()))
					.replaceAll("<msisdn>", MUtility.urlEncoding(msisdn))
					.replaceAll("<pin>", MUtility.urlEncoding(pin));
			url=url+"?username="+MUtility.urlEncoding(userName)+
			"&password="+MUtility.urlEncoding(password);
			
			Map<String,String> headerMap=new HashMap<String,String>();
			headerMap.put("Content-Type", "application/json");
			Map<String,Object> data=new HashMap<String,Object>();
			data.put("network", veooServiceConfig.getCgNetwork());
			String json=JsonMapper.getObjectToJson(data);
			veooPinValidation.setRequest(url);
			
		HTTPResponse httpPResponse=httpURLConnectionUtil.makeHTTPPOSTRequest(url, json, headerMap);
		veooPinValidation.setResponse(httpPResponse.getResponseCode()+":"+httpPResponse.getResponseStr());
		logger.info("sendPin:: request url length:"+url.length()+" , response:: "+httpPResponse);
		if(httpPResponse.getResponseCode()==200){
			Map responseMap= JsonMapper.getJsonToObject(httpPResponse.getResponseStr(), Map.class);
			veooPinValidation.setPinValidationStatus(Objects.toString(responseMap.get("status")));
			veooPinValidation.setPinValidationMessage(Objects.toString(responseMap.get("message")));
			
			if(veooPinValidation.getPinValidationStatus().equalsIgnoreCase("success")){
				veooPinValidation.setPinValidate(true);
			}				
		}		
		}catch(Exception ex){
			logger.error("pinValidation:: ",ex);
			
		}finally{
			daoService.saveObject(veooPinValidation);
		}
		return veooPinValidation;
	}



public HTTPResponse sendHttpsPost(String url) {

	HTTPResponse httpResponse = new HTTPResponse();
	int responseCode = -1;
	StringBuffer response = new StringBuffer();
	String error = "";
	HttpClient client = HttpClientBuilder.create().build();
	HttpPost post = new HttpPost(url);
	BufferedReader in =null;
	try {
		
		
		post.addHeader("Accept","application/xml");
		post.addHeader("Content-Type","application/xml");
	
		
		HttpResponse res = client.execute(post);
		logger.debug("HttpResponse:::::::::::"+res.getStatusLine());
		responseCode=res.getStatusLine().getStatusCode();
		 if (res != null) {
                InputStream inputStream = res.getEntity().getContent(); //Get the data in the entity
                 in = new BufferedReader(new InputStreamReader(inputStream));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				
				}
           }

	} catch (Exception e) {
		error=e.toString();
		logger.error("\nSending 'GET' request to URL : " + url
				+ ", Response Code : " + responseCode + ", response: "
				+ response.toString() + ", Excption: " + e);
	} finally {

		try {
			if (in != null)
				in.close();
			if (post != null)
				post.releaseConnection();
		} catch (Exception e) {
			
			logger.error("Error in closing http connection for url:" + url
					+ " " + e);
		}
	}

	httpResponse.setError(error);
	httpResponse.setResponseCode(responseCode);
	httpResponse.setResponseStr(response.toString());
	return httpResponse;
}

}
