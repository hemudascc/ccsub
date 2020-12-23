package net.mycomp.messagecloud;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import net.common.jms.JMSService;
import net.common.service.IDaoService;
import net.common.service.SubscriberRegService;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping("mc")
public class MessageCloudController {

	private static final Logger logger = Logger
			.getLogger(MessageCloudController.class.getName());
	
@Autowired
private IDaoService daoService;

@Autowired
private JMSService jmsService;

@Autowired
private SubscriberRegService subscriberRegService;

@Autowired
private MessageCloudService messageCloudService;

private HttpURLConnectionUtil httpURLConnectionUtil; 

@Value("${message.cloud.portal.url}")
private String portalUrl;

@Autowired
private JMSMessageCloudService jmsMessageCloudService;

@PostConstruct
public void init(){
	httpURLConnectionUtil=new HttpURLConnectionUtil();
}

	@RequestMapping("tc")
	public ModelAndView termAndCondition(ModelAndView modelAndView ){
		modelAndView.setViewName("messagecloud/term_condtion");
		return modelAndView;
	} 
	
	@RequestMapping("tocg")
	public ModelAndView toCG(ModelAndView modelAndView,HttpServletRequest request){
		MessageCloudSub messageCloudSub=null;
		try{
		String token=request.getParameter("token");
		CGToken cgToken=new CGToken(token); 
		VWServiceCampaignDetail vwServiceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		MessageCloudServiceConfig messageCloudServiceConfig=
				MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.
				get(vwServiceCampaignDetail.getServiceId());
		//https://payforit.txtnation.com/api/?campaign_name=<campaign_name>
		//&api_key=<api_key>&value=<value>&
		//cy=<cy>&name=<name>&description=<description>
		//&window=large&success_url=<success_url>&
		//cancel_url=<cancel_url>&callback_url=<callback_url>&id=<id>

		 messageCloudSub=new MessageCloudSub(true);
		messageCloudSub.setToken(token);
		
		messageCloudSub.setTokenId(cgToken.getTokenId());
		//https://payforit.txtnation.com/api/?campaign_name=<campaign_name>&api_key=<api_key>
		//&value=<value>&currency=<currency>&name=<name>&description=<description>&window=large
		//&success_url=<success_url>&cancel_url=<cancel_url>&callback_url=<callback_url>&id=<id>
		//&sub_repeat=<sub_repeat>&sub_period=<sub_period>&sub_period_units=<sub_period_units>
		
		String url=messageCloudServiceConfig.getCgUrlTemplate()
				.replaceAll("<campaign_name>", MUtility.urlEncoding(messageCloudServiceConfig.getCampaignName()))
				.replaceAll("<api_key>", MUtility.urlEncoding(messageCloudServiceConfig.getApiKey()))
				.replaceAll("<value>",MUtility.urlEncoding(String.format("%.2f",(messageCloudServiceConfig.getValue()))))
				.replaceAll("<currency>", MUtility.urlEncoding(messageCloudServiceConfig.getCurrency()))
				.replaceAll("<name>",  MUtility.urlEncoding(messageCloudServiceConfig.getName()))
				.replaceAll("<id>",  MUtility.urlEncoding(token))
				.replaceAll("<description>",  MUtility.urlEncoding(messageCloudServiceConfig.getDescription()))
				.replaceAll("<success_url>",MUtility.urlEncoding( messageCloudServiceConfig.getSuccessUrl()+"/"+token))
				.replaceAll("<cancel_url>", MUtility.urlEncoding(messageCloudServiceConfig.getCancelUrl()+"/"+token))
				.replaceAll("<callback_url>", MUtility.urlEncoding(messageCloudServiceConfig.getCallbackUrl()+"/"+token))
				.replaceAll("<sub_repeat>", MUtility.urlEncoding(""+messageCloudServiceConfig.getSubRepeat()))
				.replaceAll("<sub_period>", MUtility.urlEncoding(""+messageCloudServiceConfig.getSubPeriod()))
				.replaceAll("<sub_period_units>", MUtility.urlEncoding(messageCloudServiceConfig.getSubPeriodUnits()))
				;
		messageCloudSub.setRequestStr(url);
		logger.debug("toCG::: url: "+url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendHttpsGet(url, null);
		logger.debug("toCG::: httpResponse: "+httpResponse);
		messageCloudSub.setResponseStr(httpResponse.getResponseCode()+":"+httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200){//OK|1625505695696337|http://pfi.me/0/d2940098b87e69d1356755eec2c130c5
			String str[]=httpResponse.getResponseStr().split("\\|");
			logger.debug("toCG::: httpResponse: "+str[2]);
			if(str!=null&&str.length>=3){
				messageCloudSub.setRedirectTo(str[2].trim());	
			}
		}
		
		modelAndView.setView(new RedirectView(messageCloudSub.getRedirectTo()));
		}catch(Exception ex){
			logger.error("toCG::::: ",ex);
			  modelAndView.setView(new RedirectView(portalUrl));
		}finally{
			jmsService.saveObject(messageCloudSub);
			
		}
		return modelAndView;
	} 
	
	@RequestMapping(value="callback/{token}",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ModelAndView callbackUrl(ModelAndView modelAndView,HttpServletRequest request,
			@PathVariable("token") String token){
	    
		logger.info("callbackUrl:: : "+request.getQueryString());
	    
	    MessagecloudNotification messagecloudNotification=null;
	    try{
	    	//{"key":"7ec87c10a2d126ab3b8f25b772f70e5703f219ee",
	    	//"billed":1,"transactionId":"1619180117363935","status":"OK",
	    	//"msisdn":"447342190934","network":"VODAFONEUK"}
	    	
	    getAllParam("callbackUrl", request);
	    messagecloudNotification =new MessagecloudNotification(true);
	    messagecloudNotification.setKeyValue(request.getParameter("key"));
	    messagecloudNotification.setBilled(request.getParameter("billed"));
	    messagecloudNotification.setCgStatus(request.getParameter("status"));
	    messagecloudNotification.setCgTransactionId(request.getParameter("transactionId"));
	    messagecloudNotification.setMsisdn(request.getParameter("msisdn"));
	    messagecloudNotification.setNetwork(request.getParameter("network"));
	    messagecloudNotification.setStop(request.getParameter("stop"));
	    messagecloudNotification.setToken(token);
	    messagecloudNotification.setQueryStr(request.getQueryString());
	   String json= MUtility.getStringFromRequest(request.getInputStream());
	   logger.info("callbackUrl::: json:: "+json);;
	   
	    //  Map map=JsonMapper.getJsonToObject(json,Map.class);
	   // String key=Objects.toString(map.get("key"));
	    
	    
	    CGToken cgToken=new CGToken(token);
	    VWServiceCampaignDetail vwServiceCampaignDetail=MData
	    		.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
	    MessageCloudServiceConfig messageCloudServiceConfig=
	    		MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.
	    		get(vwServiceCampaignDetail.getServiceId());
	    LiveReport liveReport=new LiveReport(vwServiceCampaignDetail.getOpId(),new Timestamp(System.currentTimeMillis())
		 ,cgToken.getCampaignId(),vwServiceCampaignDetail.getServiceId(),0); 
	    liveReport.setAction(MConstants.ACT);
	    liveReport.setProductId(vwServiceCampaignDetail.getProductId());
	    liveReport.setConversionCount(1);
	    liveReport.setNoOfDays(messageCloudServiceConfig.getSubPeriod());
	    liveReport.setAmount(messageCloudServiceConfig.getValue());	    
	    subscriberRegService.findOrCreateSubscriberByAct(messagecloudNotification.getMsisdn(), null, liveReport);
	    // modelAndView.setView(new RedirectView(messageCloudServiceConfig.getPortalUrl()));
	    logger.info("callbackUrl:::messagecloudNotification::  "+messagecloudNotification);
	    
	   }catch(Exception ex){
		   logger.error("callbackUrl "+messagecloudNotification,ex);
		   modelAndView.setView(new RedirectView(portalUrl));
	   }finally{
		   jmsMessageCloudService.saveMessagecloudNotification(messagecloudNotification);
	   }
	    modelAndView.setView(new RedirectView(portalUrl+"&msisdn="+messagecloudNotification.getMsisdn()));
	    return modelAndView;
	}
	
	
	@RequestMapping(value="renew",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String renew(ModelAndView modelAndView,HttpServletRequest request
			){
	    
		logger.info("renew:: : "+request.getQueryString());
	    
	    MessagecloudNotification messagecloudNotification=null;
	    try{
	    getAllParam("renew", request);
	    messagecloudNotification =new MessagecloudNotification(true);
	    messagecloudNotification.setKeyValue(request.getParameter("key"));
	    messagecloudNotification.setBilled(request.getParameter("billed"));
	    messagecloudNotification.setCgStatus(request.getParameter("status"));
	    messagecloudNotification.setCgTransactionId(request.getParameter("transactionId"));
	    messagecloudNotification.setMsisdn(request.getParameter("msisdn"));
	    messagecloudNotification.setNetwork(request.getParameter("network"));
	    messagecloudNotification.setStop(request.getParameter("stop"));
	    messagecloudNotification.setToken(request.getParameter("id"));	    
	    messagecloudNotification.setAction(MConstants.RENEW);
	    messagecloudNotification.setQueryStr(request.getQueryString());
	    messagecloudNotification.setQueryStr(request.getQueryString());
	    
	    String json= MUtility.getStringFromRequest(request.getInputStream());
		   logger.info("callbackUrl::: json:: "+json);;
		 
		   
	    // modelAndView.setView(new RedirectView(messageCloudServiceConfig.getPortalUrl()));
	    logger.info("renew:::messagecloudNotification::  "+messagecloudNotification);
	    
	   }catch(Exception ex){
		   logger.error("renew "+messagecloudNotification,ex);
		  
	   }finally{
		   jmsMessageCloudService.saveMessagecloudNotification(messagecloudNotification);
	   }
	    return "ok";
	}
	
	@RequestMapping(value="success/{token}",method={RequestMethod.GET,RequestMethod.POST})
	
	public ModelAndView success(ModelAndView modelAndView,HttpServletRequest request,
			@PathVariable("token") String token){
		logger.info("success:: : "+request.getQueryString());	
		MessagecloudNotification messagecloudNotification=null;
		try{
		getAllParam("success ",request);
	    messagecloudNotification =new MessagecloudNotification(true);
	    messagecloudNotification.setKeyValue(request.getParameter("key"));
	    messagecloudNotification.setBilled(request.getParameter("billed"));
	    messagecloudNotification.setCgStatus(request.getParameter("status"));
	    messagecloudNotification.setCgTransactionId(request.getParameter("transactionId"));
	    messagecloudNotification.setMsisdn(request.getParameter("msisdn"));
	    messagecloudNotification.setNetwork(request.getParameter("network"));
	    messagecloudNotification.setStop(request.getParameter("stop"));
	    messagecloudNotification.setToken(token);	    
	    messagecloudNotification.setAction(MessageCloudConstant.SUCCESS);
	    messagecloudNotification.setQueryStr(request.getQueryString());
	    
	    String json= MUtility.getStringFromRequest(request.getInputStream());
		   logger.info("callbackUrl::: json:: "+json);;
		 
		   
	    CGToken cgToken=new CGToken(token);
	    VWServiceCampaignDetail vwServiceCampaignDetail=MData
	    		.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
	    MessageCloudServiceConfig messageCloudServiceConfig=
	    		MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.
	    		get(vwServiceCampaignDetail.getServiceId());
	    modelAndView.setView(new RedirectView(messageCloudServiceConfig.getPortalUrl()));
	   
	    LiveReport liveReport=new LiveReport(vwServiceCampaignDetail.getOpId(),new Timestamp(System.currentTimeMillis())
		 ,cgToken.getCampaignId(),vwServiceCampaignDetail.getServiceId(),0); 
	    liveReport.setAction(MConstants.ACT);
	    liveReport.setConversionCount(1);
	    liveReport.setProductId(vwServiceCampaignDetail.getProductId());
	    liveReport.setNoOfDays(messageCloudServiceConfig.getSubPeriod());
	    liveReport.setAmount(messageCloudServiceConfig.getValue());	    
	    subscriberRegService.findOrCreateSubscriberByAct(messagecloudNotification.getMsisdn(), null, liveReport);
	    
	    logger.info("success:::messagecloudNotification::  "+messagecloudNotification);
		}catch(Exception ex){
			  modelAndView.setView(new RedirectView(portalUrl));
			logger.error("success "+messagecloudNotification,ex);
		}finally{
			logger.info("success "+messagecloudNotification);	
			jmsMessageCloudService.saveMessagecloudNotification(messagecloudNotification);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="cancel/{token}",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView cancel(ModelAndView modelAndView,HttpServletRequest request,
			@PathVariable("token") String token){
		logger.info("cancel:: : "+request.getQueryString());
		getAllParam("cancel ",request);
	    MessagecloudNotification messagecloudNotification=null;
	    try{
	    messagecloudNotification =new MessagecloudNotification(true);
	    messagecloudNotification.setKeyValue(request.getParameter("key"));
	    messagecloudNotification.setBilled(request.getParameter("billed"));
	    messagecloudNotification.setCgStatus(request.getParameter("status"));
	    messagecloudNotification.setCgTransactionId(request.getParameter("transactionId"));
	    messagecloudNotification.setMsisdn(request.getParameter("msisdn"));
	    messagecloudNotification.setNetwork(request.getParameter("network"));
	    messagecloudNotification.setStop(request.getParameter("stop"));
	    messagecloudNotification.setToken(token);	    
	    messagecloudNotification.setAction(MessageCloudConstant.CANCEL);
	    messagecloudNotification.setQueryStr(request.getQueryString());
	    String json= MUtility.getStringFromRequest(request.getInputStream());
		   logger.info("callbackUrl::: json:: "+json);;
		 
		   
	    CGToken cgToken=new CGToken(token);
	    VWServiceCampaignDetail vwServiceCampaignDetail=MData
	    		.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
	    MessageCloudServiceConfig messageCloudServiceConfig=
	    		MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.
	    		get(vwServiceCampaignDetail.getServiceId());
	    modelAndView.setView(new RedirectView(messageCloudServiceConfig.getPortalUrl()));
	    
	    logger.info("cancel:::messagecloudNotification::  "+messagecloudNotification);
	}catch(Exception ex){
		  modelAndView.setView(new RedirectView(portalUrl));
		logger.error("cancel::  "+messagecloudNotification,ex);
	}finally{
		jmsMessageCloudService.saveMessagecloudNotification(messagecloudNotification);
	}
		return modelAndView;
  }
	
	@RequestMapping("checksub")
	public ModelAndView checkSub(ModelAndView modelAndView,
			HttpServletRequest request ){
	
		String msisdn=request.getParameter("msisdn");
		String token=request.getParameter("token");
		 CGToken cgToken=new CGToken(token);
		  VWServiceCampaignDetail vwServiceCampaignDetail=MData
		    		.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		MessageCloudServiceConfig messageCloudServiceConfig=
		    		MessageCloudConstant.mapServiceIdMessageCloudServiceConfig.
		    		get(vwServiceCampaignDetail.getServiceId());
		if(messageCloudService.checkSub(5, vwServiceCampaignDetail.getOpId(), msisdn)){
			modelAndView.setView(new RedirectView(messageCloudServiceConfig.getPortalUrl()));
		}else{
			modelAndView.setView(new RedirectView("http://life.limemoments.com"));	
		}
		return modelAndView;
	}
	
	@RequestMapping("alreadsubscribed")
	public ModelAndView msisdnPage(ModelAndView modelAndView,HttpServletRequest request ){
		modelAndView.addObject("token",request.getParameter("token"));
		modelAndView.setViewName("messagecloud/alreadsubscribed");
		return modelAndView;
	} 
	
	
	
	public void getAllParam(String method,HttpServletRequest request ){
		Enumeration<String> e=request.getParameterNames();
		while(e.hasMoreElements()){
			String key=e.nextElement();
			logger.info(method+"  key:: "+key+", param: "+request.getParameter(key));
		}
	}
}
