package net.mycom.nxt.vas;

import java.util.HashMap;
import java.util.Map;

import net.common.jms.JMSService;
import net.common.service.IDaoService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("nxtVasServiceApi")
public class NxtVasServiceApi {

	private static final Logger logger = Logger
			.getLogger(NxtVasServiceApi.class.getName());
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private IDaoService daoService;
	
	
	private final String freeSmsUrl;
	private final String apiKey;
	
	@Autowired	
 	public NxtVasServiceApi(@Value("${nxt.vas.free.sms.url}") String freeSmsUrl,
 			@Value("${nxt.vas.api.key}")String apiKey){
		
		httpURLConnectionUtil=new HttpURLConnectionUtil();
		this.freeSmsUrl=freeSmsUrl;
		this.apiKey=apiKey;
	}
	
	public NxtVasUnsubTrans callUnsubApi(NxtVasConfig nxtVasConfig,String msisdn,String subscriberId){
		NxtVasUnsubTrans nxtVasUnsubTrans=new NxtVasUnsubTrans();
		try{
			nxtVasUnsubTrans.setMsisdn(msisdn);
			nxtVasUnsubTrans.setSubscriberId(subscriberId);
			
		String url=nxtVasConfig.getUnsubUrlTemplate()
		.replaceAll("<subscriberid>", subscriberId)
		.replaceAll("<apiKey>", MUtility.urlEncoding(nxtVasConfig.getApiKey()))
		.replaceAll("<productid>", ""+nxtVasConfig.getNxtVasProduct_id());
		nxtVasUnsubTrans.setRequest(url);
		logger.info("callUnsubApi:: url:: "+url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpsPost(url, null);
		nxtVasUnsubTrans.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		logger.info("callUnsubApi:: httpResponse:: "+httpResponse);
		Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
		int status=MUtility.toInt(String.valueOf(map.get("status")),0);
		if(status==1){
			nxtVasUnsubTrans.setSuccess(true);
		}
		}catch(Exception ex){
			logger.error("callUnsubApi:: ",ex);
		}finally{
			jmsService.saveObject(nxtVasUnsubTrans);
		}
		return nxtVasUnsubTrans;
		}
	
	
	public void sendFreeSms(NxtVasConfig nxtVasConfig,String msisdn,
			String subscriberId,NxtVasSubscriberStatusEnum nxtVasSubscriberStatusEnum){
		
		NxtVasSms nxtVasSms=new NxtVasSms(true);		
		try{
		
		nxtVasSms.setMsisdn(msisdn);
		
		Map<String,String> data=new HashMap<String,String>();
		data.put("type","2");
		data.put("api_key",apiKey);
		data.put("subscriber_id", subscriberId);
		data.put("subscriber_condition", nxtVasSubscriberStatusEnum.statusDescp);
		//data.put("username", value);
		//data.put("password", value);
		data.put("short_url", nxtVasConfig.getPortalUrl());
		Map<String,String> headerMap=new HashMap<String,String>();
		
		headerMap.put("Accept", "application/json");
		nxtVasSms.setRequest(freeSmsUrl+", data: "+data+", headerMap:: "+headerMap);
		logger.info("sendFreeSms:: data: "+data+" , headerMap:: "+headerMap+" ,freeSmsUrl:: "+freeSmsUrl);
		HTTPResponse httpResponse =httpURLConnectionUtil.sendPostRequest(freeSmsUrl, data, headerMap);
		logger.info("sendFreeSms:: httpResponse::::: "+httpResponse);
		nxtVasSms.setResponse(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		
		}catch(Exception ex){
			logger.error("sendFreeSms:: ",ex);	
		}finally{
			daoService.saveObject(nxtVasSms);
		}
	}
	
}
