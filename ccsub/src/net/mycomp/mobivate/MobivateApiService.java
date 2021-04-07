package net.mycomp.mobivate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import net.common.service.IDaoService;
import net.persist.bean.SubscriberReg;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mobivateApiService")
public class MobivateApiService {

	private static final Logger logger = Logger
			.getLogger(MobivateApiService.class.getName());
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private IDaoService daoService;
	
	@PostConstruct
	public void init(){
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	public MobivateApiTrans sendBilledMessage(String msisdn,String action,String token
			,MobivateServiceConfig mobivateServiceConfig
			,String msg,String msgClass,SubscriberReg subscriberReg){
		
		MobivateApiTrans mobivateApiTrans=new MobivateApiTrans(true,mobivateServiceConfig.getTimeZone());
		try{
			
			mobivateApiTrans.setMsisdn(msisdn);
			mobivateApiTrans.setAction(action);
			mobivateApiTrans.setMsgType(msgClass);
			mobivateApiTrans.setToken(token);
			
			// /srs/api/sendsms?
			//USER_NAME=<account_id>&PASSWORD=<api_key>&ORIGINATOR=1987654&RECIPIENT=61412345678&PR
			//		OVIDER=telstra&MESSAGE_TEXT=Hello%20There!
			
			String url=mobivateServiceConfig.getMtBilledUrl()
					.replaceAll("<originator>",MUtility.urlEncoding(mobivateServiceConfig.getShortcode()))
					.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn))
					//.replaceAll("<provider>",mobivateServiceConfig.getSortcode())
					.replaceAll("<text>",MUtility.urlEncoding(msg))
					.replaceAll("<value>",Objects.toString(
							mobivateServiceConfig.getBillingAmount().intValue()))
					.replaceAll("<refrence>",MUtility.urlEncoding(Objects.toString(mobivateApiTrans.getId())))
					.replaceAll("<url>",MUtility.urlEncoding(mobivateServiceConfig.getPortalUrl()+msisdn))
					.replaceAll("<started>",MUtility.urlEncoding(
							MobivateConstant.getFormatedTime(subscriberReg.getSubDate())))
					.replaceAll("<keyword>",MUtility.urlEncoding(mobivateServiceConfig.getKeyword()))
					.replaceAll("<messageclass>",MUtility.urlEncoding(msgClass))
					;
			mobivateApiTrans.setRequest(url);
			HTTPResponse httpResponse=httpURLConnectionUtil.sendPostRequest(url, null);
			mobivateApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			
			if(httpResponse.getResponseCode()==200&&httpResponse.getResponseStr().equalsIgnoreCase("0")){
				mobivateApiTrans.setSuccess(true);
			}
			
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			daoService.saveObject(mobivateApiTrans);
		}
		return mobivateApiTrans;		
	}
	
	

	public MobivateApiTrans subApi(MobivateCGCallback mobivateCGCallback,
			MobivateServiceConfig mobivateServiceConfig){
		
		MobivateApiTrans mobivateApiTrans=new MobivateApiTrans(true,mobivateServiceConfig.getTimeZone());
		try{
			
			mobivateApiTrans.setMsisdn(mobivateCGCallback.getUserId());
			mobivateApiTrans.setAction("SUB");			
			mobivateApiTrans.setToken(mobivateCGCallback.getToken());
		 //https://<content portal url>/lookup/subscribe/mtnng/?userid=<msisdn>&keyword=<productid>
			String url=mobivateServiceConfig.getSubApiUrl()
					.replaceAll("<productid>",MUtility.urlEncoding(mobivateServiceConfig.getProductId()))
					.replaceAll("<msisdn>",MUtility.urlEncoding(mobivateCGCallback.getUserId()));
			mobivateApiTrans.setRequest(url);
			HTTPResponse httpResponse=httpURLConnectionUtil.sendPostRequest(url, null);
			mobivateApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			
			if(httpResponse.getResponseCode()==200){
				Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(), Map.class);
				if(Objects.toString(map.get("subscribed")).equalsIgnoreCase("true")){
					mobivateApiTrans.setSuccess(true);
				}
			}
			
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			daoService.saveObject(mobivateApiTrans);
		}
		return mobivateApiTrans;		
	}
	
	public MobivateApiTrans unsubApi(String msisdn,
			MobivateServiceConfig mobivateServiceConfig
		){
		
		MobivateApiTrans mobivateApiTrans=new MobivateApiTrans(true,mobivateServiceConfig.getTimeZone());
		try{
			
			mobivateApiTrans.setMsisdn(msisdn);
			mobivateApiTrans.setAction("SUB");			
			//mobivateApiTrans.setToken(mobivateCGCallback.getToken());
		 //https://<content portal url>/lookup/subscribe/mtnng/?userid=<msisdn>&keyword=<productid>
			String url=mobivateServiceConfig.getUnsubApiUrl()
					.replaceAll("<productid>",MUtility.urlEncoding(mobivateServiceConfig.getProductId()))
					.replaceAll("<msisdn>",MUtility.urlEncoding(msisdn));
			mobivateApiTrans.setRequest(url);
			HTTPResponse httpResponse=httpURLConnectionUtil.sendPostRequest(url, null);
			mobivateApiTrans.setResponse(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
			
			if(httpResponse.getResponseCode()==200||httpResponse.getResponseCode()==202){
			
					mobivateApiTrans.setSuccess(true);
				
			}
			
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			daoService.saveObject(mobivateApiTrans);
		}
		return mobivateApiTrans;		
	}
	
	public MobivateApiTrans getUKVodaofoneCGUrl(
			MobivateServiceConfig mobivateServiceConfig,String token
		){
		
		MobivateApiTrans mobivateApiTrans=new MobivateApiTrans(true,mobivateServiceConfig.getTimeZone());
		try{
			
			//mobivateApiTrans.setMsisdn(msisdn);
			mobivateApiTrans.setToken(token);
			
			mobivateApiTrans.setAction("VODAFONE_CG_URL");			
			//mobivateApiTrans.setToken(mobivateCGCallback.getToken());
		 //https://<content portal url>/lookup/subscribe/mtnng/?userid=<msisdn>&keyword=<productid>
			String data=mobivateServiceConfig.getCgUrl()
					.replaceAll("<shortcode>",MUtility.urlEncoding(mobivateServiceConfig.getShortcode()))
					.replaceAll("<servicename>",MUtility.urlEncoding(mobivateServiceConfig.getServiceName()))
					.replaceAll("<keyword>",MUtility.urlEncoding( mobivateServiceConfig.getKeyword()))
					.replaceAll("<productid>", MUtility.urlEncoding(mobivateServiceConfig.getProductId()))
					.replaceAll("<validity>", MUtility.urlEncoding(Objects.toString(mobivateServiceConfig.getValidity())))
					.replaceAll("<campaign>", MUtility.urlEncoding(mobivateServiceConfig.getCampaignName()))
					.replaceAll("<amount>", MUtility.urlEncoding(Objects.toString(mobivateServiceConfig.getBillingAmount())))
					.replaceAll("<brandlogo>",MUtility.urlEncoding( mobivateServiceConfig.getBrandLogo()))
					.replaceAll("<backgroundcolor>",MUtility.urlEncoding( mobivateServiceConfig.getBackgroundColor()))
					.replaceAll("<textcolour>", MUtility.urlEncoding(mobivateServiceConfig.getTextColour()))
					.replaceAll("<callbackurl>",MUtility.urlEncoding( mobivateServiceConfig.getCallbackUrl()+token))
					.replaceAll("<notification>",MUtility.urlEncoding( "http://192.241.253.234/ccsub/cnt/mobic/noti"))
				    .replaceAll("<token>",MUtility.urlEncoding(token))
				    ;
			
			Map<String,String> headerMap=new HashMap<String,String>();
			headerMap.put("User-Agent", "");
			mobivateApiTrans.setRequest("https://gateway.mobivate.com/api/dcb/init"+" "+data);
//			HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpsPost("https://gateway.mobivate.com/api/dcb/init"
//					, data,null);
			HTTPResponse httpResponse= httpURLConnectionUtil
					.sendHttpsPost("https://gateway.mobivate.com/api/dcb/init",data,headerMap);
			
			mobivateApiTrans.setResponseCode(""+httpResponse.getResponseCode());
			mobivateApiTrans.setResponse(httpResponse.getResponseStr());
			
			if(httpResponse.getResponseCode()==200||httpResponse.getResponseCode()==202){			
					mobivateApiTrans.setSuccess(true);				
			}
			
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			daoService.saveObject(mobivateApiTrans);
		}
		return mobivateApiTrans;		
	}
	
	///srs/api/sendsms?
		//USER_NAME=<username>&PASSWORD=<password>&ORIGINATOR=<shortcode>&RECIPIENT=<msisdn>&PROVIDER=<provider>&MESSAGE_TEXT=<msg>
		public void sendFreeMessage(String msisdn,Integer subId,String msgTemplate,
				MobivateServiceConfig mobivateServiceConfig,String action,String token){
			//Thank you for subscribing <servicename> for <billingfrequency>/<validitydesc>.
			MobivateApiTrans mobivateApiTrans=new MobivateApiTrans(true,mobivateServiceConfig.getTimeZone());
			try{
				mobivateApiTrans.setAction("FREE_SMS");
				mobivateApiTrans.setMsisdn(msisdn);
				mobivateApiTrans.setAction(action);
				
				mobivateApiTrans.setToken(token);
				
			mobivateApiTrans.setAction(action);
			mobivateApiTrans.setMobivateServiceConfigId(mobivateServiceConfig.getServiceId());
			String msg=msgTemplate.replaceAll("<servicename>", mobivateServiceConfig.getServiceName())
					     .replaceAll("<validitydesc>", mobivateServiceConfig.getValidityDesc())
					     .replaceAll("<shortcode>", mobivateServiceConfig.getShortcode())
					     .replaceAll("<keyword>", mobivateServiceConfig.getKeyword())
					     .replaceAll("<portalurl>", mobivateServiceConfig.getPortalUrl())
					     .replaceAll("<msisdn>", msisdn)
					      .replaceAll("<subid>", ""+subId)
					     ;
			//https://gateway.mobivate.com/srs/api/sendsms?USER_NAME=<username>&
			//PASSWORD=<password>&ORIGINATOR=<shortcode>&RECIPIENT=<msisdn>&
			//PROVIDER=default&MESSAGE_TEXT=<msg>&VALUE=0
			String url=mobivateServiceConfig.getSmsUrl().replaceAll("<username>", MUtility.urlEncoding(mobivateServiceConfig.getSmsUserName()))
			              .replaceAll("<password>",MUtility.urlEncoding(mobivateServiceConfig.getSmsPassword()))
			              .replaceAll("<shortcode>", MUtility.urlEncoding(mobivateServiceConfig.getShortcode()))
			              .replaceAll("<msisdn>", MUtility.urlEncoding(msisdn))
			              .replaceAll("<keyword>", MUtility.urlEncoding(mobivateServiceConfig.getSmsKeyword()))
			              //.replaceAll("<started>",MUtility.urlEncoding(formateMoDate))
			              .replaceAll("<msg>",MUtility.urlEncoding(msg))
			              .replaceAll("<msgclass>",MUtility.urlEncoding("WELCOME MESSAGE"))
			              ;
			
			logger.info("sendMessage::: url:: "+url);
			mobivateApiTrans.setRequest(url);
			
			Map<String,String> headerMap=new HashMap<String,String>();
			headerMap.put("User-Agent", "");
			HTTPResponse response= httpURLConnectionUtil
					.sendHttpsGet( url, null, headerMap);
			
			mobivateApiTrans.setResponse(response.getResponseCode()+":"+response.getResponseStr());
			logger.info("sendMessage::: mobivateApiTrans:: "+mobivateApiTrans);
			}catch(Exception ex){
				logger.error("sendWelcomeMessage::: ",ex);
			}finally{
				daoService.updateObject(mobivateApiTrans);
			}
		}
		
		public static void main(String arg[]){
			HttpURLConnectionUtil httpURLConnectionUtil=new HttpURLConnectionUtil();
			String url="https://gateway.mobivate.com/api/sendsms/2a74245f27da4377b30e1d7954026cd8/?api_key=08a3958640f64a7db4e1a12f4bcb367b&source=65065&destination=447391984938&message=You+have+been+successfully+subscribed+to+Way2Life+for+week++access+content+by+click+on+%3Cportalurl%3E+.";
			Map<String,String> headerMap=new HashMap<String,String>();
			headerMap.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.5 Mobile/15E148 Safari/604.1");
			HTTPResponse response= httpURLConnectionUtil
					.sendHttpsGet( url, null, headerMap);
			System.out.println(response.toString());
		}
	
}
