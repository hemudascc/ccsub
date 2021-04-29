package net.mycomp.cornet.sudan;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import net.common.service.IDaoService;
import net.common.service.LiveReportFactoryService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;

@Service("cornetServiceApi")
public class CornetServiceApi {

	private static final Logger logger = Logger.getLogger(CornetServiceApi.class);
	

	private HttpURLConnectionUtil httpURLConnectionUtil;
	@Autowired
	private IDaoService daoService;
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	@Autowired
	RedisCacheService redisCacheService;
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private LiveReportFactoryService liveReportFactoryService;
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();

	}

	public String pinSend(String token,String accessToken, String msisdn,String channel) {
		Map<String,String> headerMap=new HashMap<>();
		Map<String, String> requestMap = new HashMap<>();
		CGToken cgToken = new CGToken(token);
		CornetTrans cornetTrans = new CornetTrans(true);
//		String accessToken = Objects.toString(redisCacheService.getObjectCacheValue(CornetConstant.CORNET_UNIQUE_ACCESS_TOKEN_PREFIX));
		VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		CornetConfig cornetConfig = CornetConstant.mapServiceIdToCornetConfig.get(vwServiceCampaignDetail.getServiceId());
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Authorization", "Bearer "+accessToken);
		try {
			cornetTrans.setMsisdn(msisdn);
			cornetTrans.setRequestType(CornetConstant.PIN_SEND);
			cornetTrans.setToken(token);
			cornetTrans.setTokenId(cgToken.getTokenId());
			requestMap.put("msisdn", msisdn);
			requestMap.put("product_code", cornetConfig.getProductCode());
			String requestJson = JsonMapper.getObjectToJson(requestMap);
			cornetTrans.setRequest("request url: "+CornetConstant.INITIATE_SUBSCRIPTION_API_URL+" requestMap: "+requestJson+"headers"+headerMap);
			logger.info("requestMap:  "+requestMap);
			HTTPResponse  httpResponse = httpURLConnectionUtil.makeHTTPPOSTRequest(CornetConstant.INITIATE_SUBSCRIPTION_API_URL,requestJson,headerMap);
			logger.info("getResponseStr:  "+httpResponse.getResponseStr());
			cornetTrans.setResponseCode(httpResponse.getResponseCode());
			cornetTrans.setResposne(httpResponse.getResponseStr());
			Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
			boolean status = (boolean)map.get("success");
			if(status) {
				int subscriptionRequestId = (int)map.get("subscribe_request_id");
					redisCacheService.putObjectCacheValueByEvictionDay(CornetConstant.CORNET_SUBSCRIPTION_ID_PREFIX+msisdn, subscriptionRequestId, 1);
					return "0";
				}else {
					return map.get("error_code").toString();
				}

		} catch (Exception e) {
			logger.error("error while sending pin to msisdn="+msisdn+" token="+token);
		}finally {
			daoService.saveObject(cornetTrans);
		}
		return "0";
	}

	public String verifyPin(String token,String accessToken, String msisdn,int otp, String channel) {
		Map<String,String> headerMap=new HashMap<>();
		Map<String, Integer> requestMap = new HashMap<>();
		CGToken cgToken = new CGToken(token);
		CornetTrans cornetTrans = new CornetTrans(true);
		
//		String accessToken = Objects.toString(redisCacheService.getObjectCacheValue(CornetConstant.CORNET_UNIQUE_TOKEN_PREFIX+msisdn));
		VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		CornetConfig cornetConfig = CornetConstant.mapServiceIdToCornetConfig.get(vwServiceCampaignDetail.getServiceId());
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Authorization", "Bearer "+accessToken);
		LiveReport liveReport=null;
		try {
			liveReport = new LiveReport(MConstants.CORNET_SUDAN_ZAIN_OPERATOR_ID, new Timestamp(System.currentTimeMillis()),
					cgToken.getCampaignId(), cornetConfig.getServiceId(), 4);
			cornetTrans.setMsisdn(msisdn);
			cornetTrans.setRequestType(CornetConstant.PIN_VERIFY);
			cornetTrans.setToken(token);
			cornetTrans.setTokenId(cgToken.getTokenId());
			cornetTrans.setPin(otp);
			requestMap.put("subscribe_request_id", (int)(redisCacheService.getObjectCacheValue(CornetConstant.CORNET_SUBSCRIPTION_ID_PREFIX+msisdn)));
			requestMap.put("otp", otp);
			String requestJson = JsonMapper.getObjectToJson(requestMap);
			cornetTrans.setRequest("request url: "+CornetConstant.PAYMENT_PROCESS_API_URL+" requestMap: "+requestJson+"headers"+headerMap);
			HTTPResponse  httpResponse = httpURLConnectionUtil.makeHTTPPOSTRequest(CornetConstant.PAYMENT_PROCESS_API_URL,requestJson,headerMap);
			cornetTrans.setResponseCode(httpResponse.getResponseCode());
			cornetTrans.setResposne(httpResponse.getResponseStr());	
			if(Objects.nonNull(httpResponse.getResponseStr())) {
				Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				boolean status = (boolean)map.get("success");				
				if(status) {
						subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
			
						Map sub = (Map)map.get("subscription_data");
						int isActive = (int)sub.get("is_active");
						long subDateUnix = (long)sub.get("subdate_unix");
						long unSubDateUnix = (long)sub.get("unsubdate_unix");
						long retryDateUnix = (long)sub.get("retry_unix");
						double price = (double)sub.get("price");  
						liveReport.setMsisdn(msisdn);
						liveReport.setTokenId(cgToken.getTokenId());
						liveReport.setToken(token);
						liveReport.setAction(MConstants.ACT);
						liveReport.setAdnetworkCampaignId(cgToken.getCampaignId());
						liveReport.setToken(token);
						liveReport.setTokenId(cgToken.getTokenId());
						liveReport.setProductId(cornetConfig.getProductId());
						liveReport.setConversionCount(1);
						liveReport.setAmount(cornetConfig.getPrice());	
						liveReport.setAddToCapping(true);
						cornetTrans.setIsActive(isActive);
						cornetTrans.setSubDateUnix(subDateUnix);
						cornetTrans.setUnSubDateUnix(unSubDateUnix);
						cornetTrans.setRetryDateUnix(retryDateUnix);
						cornetTrans.setPrice(price);
					return "0";
				}else{  
					return map.get("error_code").toString();
//					return "1";
				}
			}
		} catch (Exception e) {
			logger.error("error while verifying pin to msisdn="+msisdn+" token="+token+",  "+e);
		}finally{
			try{				
				if (liveReport!=null&&liveReport.getAction() != null) {
					liveReport = liveReportFactoryService.process(liveReport);					
				}
			}catch(Exception ex){
				logger.error("onMessage:::::::::::::::::"  +cornetTrans+ " , Exception  " , ex);
			}finally{
				daoService.saveObject(cornetTrans);
			}
		}
			
		return "0";
	}

	public String unsubscribe(String msisdn, Integer productId) {
		Map<String,String> headerMap=new HashMap<>();
		Map<String, String> requestMap = new HashMap<>();
		CornetTrans cornetTrans = new CornetTrans(true);
		LiveReport liveReport=null;
		CGToken cgToken = new CGToken("");
		try {
			cornetTrans.setMsisdn(msisdn);
			cornetTrans.setRequestType(CornetConstant.UNSUBSCRIBE);
//			String accessToken  ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjgiLCJmdWxsX25hbWUiOiJDb3JlTmV0IENsaWVudCIsInVzZXJuYW1lIjoiY29yZW5ldF9hcGkiLCJ2ZW5kb3JfaWQiOiI1IiwiaXNfYWRtaW4iOiIwIiwiaWF0IjoxNjE5MTYzMzg3LCJleHAiOjE2MjE3NTUzODd9.xZpEojl6jRSGLqBajKEmdJVAKbVgVZiL1pMHLbxdclY";
			String accessToken = Objects.toString(redisCacheService.getObjectCacheValue(CornetConstant.CORNET_UNIQUE_TOKEN_PREFIX+msisdn));
			SubscriberReg subscriberReg = jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(msisdn, productId);
			if(Objects.nonNull(subscriberReg) && subscriberReg.getStatus()==1) {
				cgToken = new CGToken(subscriberReg.getToken());
			}else {
				cornetTrans.setResposne("Not Subscribed User");
				return "2";
			}
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			CornetConfig cornetConfig = CornetConstant.mapServiceIdToCornetConfig.get(vwServiceCampaignDetail.getServiceId());
			headerMap.put("Content-Type", "application/json");
			headerMap.put("Authorization", "Bearer "+accessToken);
			requestMap.put("msisdn", msisdn);
			requestMap.put("product_code", cornetConfig.getProductCode());
			String requestJson = JsonMapper.getObjectToJson(requestMap);
			liveReport = new LiveReport(MConstants.CORNET_SUDAN_ZAIN_OPERATOR_ID, new Timestamp(System.currentTimeMillis()),
					cgToken.getCampaignId(), cornetConfig.getServiceId(), 8);
			liveReport.setTokenId(cgToken.getTokenId());
			liveReport.setToken(cgToken.getCGToken());
			HTTPResponse  httpResponse = httpURLConnectionUtil.makeHTTPPOSTRequest(CornetConstant.UNSUBSCRIBE_API_URL,requestJson,headerMap);
			cornetTrans.setResponseCode(httpResponse.getResponseCode());
			cornetTrans.setResposne(httpResponse.getResponseStr());	
			if(Objects.nonNull(httpResponse.getResponseStr())) {
				Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				boolean status = (boolean)map.get("success");

				liveReport.setMsisdn(cornetTrans.getMsisdn());
				liveReport.setAction(MConstants.DCT);
				liveReport.setToken(cgToken.getCGToken());
				liveReport.setTokenId(cgToken.getTokenId());
				liveReport.setDctCount(1);
				if(status) {
					return "1";
				}else if(httpResponse.getResponseStr().contains("Not subscribed")){
					return "2";
				}else {
					return "0";
				}
			}
		} catch (Exception e) {
			logger.error("error while unsubscribing to msisdn="+msisdn+", error: "+e);
		}
		finally {
		
		try {
			logger.debug("live report:::::"+liveReport);
			if(liveReport.getAction()!=null){
				liveReportFactoryService.process(liveReport);
				logger.info("processed");
				cornetTrans.setRequestType(liveReport.getAction());
			}
		}catch (Exception e) {
			logger.error(" fianlly liveReport:: " + liveReport
					+ ", : altruistCallback:: "
					+ cornetTrans);
			logger.error("onMessage::::::::::finally " ,e);
		}finally {
			daoService.saveObject(cornetTrans);
		}	
		}
		return "0";
	}



}
