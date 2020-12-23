package net.mycomp.messagecloud.gateway.za;

import java.util.Map;
import java.util.Objects;

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

@Service("mcgZAApiService")
public class MCGZAApiService {

	private static final Logger logger = Logger.getLogger(JMSMCGZAService.class);
	
	

	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private IDaoService daoService;
	
	private final String obsWindowUrl;
	
	private final String successUrl;
	private final String errorUrl;
	private final String fallbackUrl;
	private final String contentPushUrl;
	
	private final  String portalUrl;
   
	@Autowired
	public MCGZAApiService(@Value("${message.cloud.za.obs.window.url}")String obsWindowUrl,
			@Value("${message.cloud.za.obs.window.success.url}")String successUrl,
			@Value("${message.cloud.za.obs.window.error.url}")String errorUrl,
			@Value("${message.cloud.za.obs.window.fallback.url}")String fallbackUrl,
			@Value("${message.cloud.za.content.push.url}")String contentPushUrl,
			@Value("${messagecloud.za.portal.url}") String portalUrl
			){
		this.obsWindowUrl=obsWindowUrl;
		this.successUrl=successUrl;
		this.errorUrl=errorUrl;
		this.fallbackUrl=fallbackUrl;
		this.contentPushUrl=contentPushUrl;
		this.portalUrl=portalUrl;
		httpURLConnectionUtil=new HttpURLConnectionUtil();	
	}
	
		
	
	
	public  MCGZAOBSWindowTrans sendOBSWindowRequest(String msisdn,
			MCGZAServiceConfig mcgZAServiceConfig,
			
			String token,Integer tokenId){
		
		MCGZAOBSWindowTrans mcgZAOBSWindowTrans=new MCGZAOBSWindowTrans(true);
		try{	
			mcgZAOBSWindowTrans.setMsisdn(msisdn);
			mcgZAOBSWindowTrans.setToken(token);
			mcgZAOBSWindowTrans.setTokenId(tokenId);
	//https://client.txtnation.com/za/?cc=<cc>&ekey=<ekey>&app_id=<app_id>
			//&success_url=<success_url>
			//&error_url=<error_url>&fallback_url=<fallback_url>&msisdn=<msisdn>
	  String url=	obsWindowUrl
		.replaceAll("<cc>",MUtility.urlEncoding(mcgZAServiceConfig.getCompanyCode()))
		.replaceAll("<ekey>",MUtility.urlEncoding( mcgZAServiceConfig.getEkey()))
		.replaceAll("<app_id>",MUtility.urlEncoding(mcgZAServiceConfig.getAppId()))
		.replaceAll("<success_url>",MUtility.urlEncoding(successUrl+token) )
		.replaceAll("<error_url>", MUtility.urlEncoding(errorUrl+token))
		.replaceAll("<fallback_url>", MUtility.urlEncoding(fallbackUrl+token))
		.replaceAll("<msisdn>", MUtility.urlEncoding(msisdn))
		;
	
	mcgZAOBSWindowTrans.setRequestStr(url);
	HTTPResponse httpResponse =httpURLConnectionUtil.sendHttpsGet(url,null);
	mcgZAOBSWindowTrans.setResponseStr(httpResponse.getResponseCode()+""+httpResponse.getResponseStr());
	Map map=JsonMapper.getJsonToObject(httpResponse.getResponseStr(),Map.class);
	mcgZAOBSWindowTrans.setResponseId(Objects.toString(map.get("id")));
	mcgZAOBSWindowTrans.setResponseRedirectUrl(Objects.toString(map.get("redirect")));
	mcgZAOBSWindowTrans.setResponseStatus(Objects.toString(map.get("status")));
	}catch(Exception ex){
			logger.error("Exception:: ",ex);
		}finally{
			daoService.saveObject(mcgZAOBSWindowTrans);
		}
		return  mcgZAOBSWindowTrans;
	}
	
	
	public  MCGZAMTMessage sendContentMessage(String msisdn,MCGZAServiceConfig mcgZAServiceConfig,
			String msg){
	
		MCGZAMTMessage mcgMTMessage=new MCGZAMTMessage(true);
		try{
			msg=MCGZAConstant.getMsg(msg, mcgZAServiceConfig,
					mcgZAServiceConfig.getPricePoint(),
					mcgZAServiceConfig.getValidity(),  msisdn,portalUrl);	
	    mcgMTMessage.setAction(MCGZAConstant.CONTENT_MSG_TYPE);
	    mcgMTMessage.setType(MCGZAConstant.CONTENT_MSG_TYPE);
		mcgMTMessage.setNumber(msisdn);
		mcgMTMessage.setMessage(msg);
		mcgMTMessage.setTitle(mcgZAServiceConfig.getServiceName());
		mcgMTMessage.setCc(mcgZAServiceConfig.getCompanyCode());
		mcgMTMessage.setEkey(mcgZAServiceConfig.getEkey());
		//https://client.txtnation.com/gateway.php?
		//number=<number>&message=<message>&title=<title>&cc=<cc>&ekey=<ekey>
	String url=	contentPushUrl
		.replaceAll("<number>",MUtility.urlEncoding(mcgMTMessage.getNumber()))
		.replaceAll("<message>",MUtility.urlEncoding( mcgMTMessage.getMessage()))
		.replaceAll("<title>",MUtility.urlEncoding( mcgMTMessage.getTitle()))
		.replaceAll("<cc>", MUtility.urlEncoding(mcgMTMessage.getCc()))
		.replaceAll("<ekey>", MUtility.urlEncoding(mcgMTMessage.getEkey()));
	mcgMTMessage.setRequestUrl(url);
	HTTPResponse httpResponse =httpURLConnectionUtil.sendHttpsGet(url,null);
	mcgMTMessage.setResponseCode(""+httpResponse.getResponseCode());
	mcgMTMessage.setResponse(httpResponse.getResponseStr());
	
		}catch(Exception ex){
			logger.error("Exception:: ",ex);
		}finally{
			jmsService.saveObject(mcgMTMessage);
		}
		return  mcgMTMessage;
	}
   

//	public  MCGMTMessage sendBilledMessage(String msessageId,String msisdn,
//			MCGZAServiceConfig mcgZAServiceConfig,
//			
//			String msg){
//		
//		MCGMTMessage mcgMTMessage=new MCGMTMessage(true);
//		try{	
//	    msg=MCGZAConstant.getMsg(msg, mcgZAServiceConfig,
//	    		mcgZAServiceConfig.getPricePoint(),
//	    		mcgZAServiceConfig.getValidity(),  msisdn);
//		mcgMTMessage.setAction(MConstants.ACT);
//		mcgMTMessage.setType(MCGConstant.BILLLED_MSG_TYPE);
//		mcgMTMessage.setNumber(msisdn);
//		mcgMTMessage.setServiceConfigId(mcgZAServiceConfig.getId());
//		//mcgMTMessage.setFallbackServiceConfigId(mcgFallbackPricePointConfig.getId());		
//		mcgMTMessage.setMessage(msg);
//		mcgMTMessage.setTitle(mcgZAServiceConfig.getServiceName());
//		mcgMTMessage.setCc(mcgZAServiceConfig.getCompanyCode());
//		mcgMTMessage.setEkey(mcgZAServiceConfig.getEkey());
//	
//		if(mcgZAServiceConfig.getReply()==1){
//			mcgMTMessage.setReply(""+mcgZAServiceConfig.getReply());		
//			mcgMTMessage.setMessageId(msessageId);
//		}else{
//			MCGCGToken mcgCGToken=new MCGCGToken("R",mcgMTMessage.getId(),mcgServiceConfig.getId(),
//					mcgFallbackPricePointConfig.getId());
//			mcgMTMessage.setReply(""+mcgFallbackPricePointConfig.getReply());		
//			mcgMTMessage.setMessageId(mcgCGToken.getCGToken());
//		}
//		
//		mcgMTMessage.setNetwork(mcgServiceConfig.getNetwork());
//		mcgMTMessage.setCurrency(mcgServiceConfig.getCurrency());
//		mcgMTMessage.setValue(""+mcgFallbackPricePointConfig.getPricePoint());
//		//https://client.txtnation.com/gateway.php?number=<number>&message=<message>&
//		//title=<title>&cc=<cc>&ekey=<ekey>&reply=<reply>&id=<messageid>&
//		//network=<network>&currency=<currency>&value=<value>
//	String url=	msg
//		.replaceAll("<number>",MUtility.urlEncoding(mcgMTMessage.getNumber()))
//		.replaceAll("<message>",MUtility.urlEncoding( mcgMTMessage.getMessage()))
//		.replaceAll("<title>",MUtility.urlEncoding( mcgMTMessage.getTitle()))
//		.replaceAll("<cc>", MUtility.urlEncoding(mcgMTMessage.getCc()))
//		.replaceAll("<ekey>", MUtility.urlEncoding(mcgMTMessage.getEkey()))
//		.replaceAll("<messageid>", MUtility.urlEncoding(mcgMTMessage.getMessageId()))
//		.replaceAll("<network>", MUtility.urlEncoding(mcgMTMessage.getNetwork()))
//		.replaceAll("<currency>", MUtility.urlEncoding(mcgMTMessage.getCurrency()))
//		.replaceAll("<value>", MUtility.urlEncoding(mcgMTMessage.getValue()))
//		;
//	
//	mcgMTMessage.setRequestUrl(url);
//	HTTPResponse httpResponse =httpURLConnectionUtil.sendHttpsGet(url,null);
//	mcgMTMessage.setResponseCode(""+httpResponse.getResponseCode());
//	mcgMTMessage.setResponse(httpResponse.getResponseStr());
//	
//	}catch(Exception ex){
//			logger.error("Exception:: ",ex);
//		}finally{
//			daoService.updateObject(mcgMTMessage);
//		}
//		return  mcgMTMessage;
//	}

}
