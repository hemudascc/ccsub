package net.mycomp.mcarokiosk.malaysia;

import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.common.service.RedisCacheService;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;


import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("mkml")
public class MKMalaysiaController {

	private static final Logger logger = Logger
			.getLogger(MKMalaysiaController.class.getName());
	
	@Autowired
	private MKMalaysiaService mkMalaysiaService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private MKMalaysiaServiceApi mkmalaysiaServiceApi;
	
	@Value("${macrokiosk.malaysia.mo.send.url}")
	private String moUrl;
	
	//@Value("${macrokiosk.malaysia.cg.callback.url}")
	//private String cgCallbackUrl;
	
	@Autowired
	private JMSMalaysiaService jmsMalaysiaService;
	
	@RequestMapping("/hecallback/{token}/{authtoken}")
	public ModelAndView callBackUrl(@PathVariable(value="token") String  token,
			@PathVariable(value="authtoken") String  authToken
			,HttpServletRequest request,ModelAndView modelAndView) {
	
	
	   String msisdn = null;
		try{
		  //http://mk.com/Test.aspx?Status=Success&TelcoID=4&Msisdn=60191234567
			
			msisdn=request.getParameter("Msisdn");			
			Enumeration<String> en = request.getHeaderNames();
			int telcoId=MUtility.toInt(request.getParameter("TelcoID"),0);
			String status=request.getParameter("Status");
			//int opId=MalaysiaOperatorTelcoidMap.getOperatorId(telcoId);
			MKMalaysiaCGToken mkMalaysiaCGToken=new MKMalaysiaCGToken(token);//neeed to changebase on 
			VWServiceCampaignDetail vwServiceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(mkMalaysiaCGToken.getCampaignId());
			
		MKMalaysiaConfig mkMalaysiaConfig=MKMalaysiaConstant
				.mapServiceIdToMKMalaysiaConfig
				.get(vwServiceCampaignDetail.getServiceId());
		logger.info("  callBackUrl::token:"+token+", authToken::"+authToken+" query String :: "+request.getQueryString());	
		modelAndView.addObject("token",token);
		modelAndView.addObject("msisdn",msisdn);
		
		if(status.equalsIgnoreCase("Success")){
			
	    	SubscriberReg subscriberReg=mkMalaysiaService.searchSubscriber(0,
	    			msisdn, 0, mkMalaysiaConfig.getProductId());
	    	
			if(subscriberReg!=null){				
				modelAndView.setView(new RedirectView(
						mkMalaysiaConfig.getPortalUrl()+"?msisdn="+msisdn)
				);
			    return modelAndView;
			}else{
				
				//String cgUrl=MKMalaysiaConstant.buildPurchaseUrl(msisdn, purchaseUrl, mkMalaysiaConfig,
				//		token, cgCallbackUrl, authToken);		
				//modelAndView.addObject("cgUrl",cgUrl);				
				modelAndView.addObject("mkMalaysiaConfig",mkMalaysiaConfig);				
				modelAndView.setViewName("mkmalaysia/lp");
				return modelAndView;
			}
		}else{			
			//modelAndView.setView(new RedirectView(mkMalaysiaConfig.getPortalUrl()+"?msisdn="+msisdn));
			modelAndView.addObject("mkMalaysiaConfig",mkMalaysiaConfig);				
			modelAndView.setViewName("mkmalaysia/lp");
			return modelAndView;
		}		
		
		}catch(Exception ex){
			logger.error("callBackUrl:::: ",ex);
		}finally{

		}		
		return 	 modelAndView;
	}
	
	
	@RequestMapping("cgcallback")
	@ResponseBody
	public String cgCallBackUrl(HttpServletRequest request,ModelAndView modelAndView) {
		logger.info("cgCallBackUrl:::::::::: "+request.getQueryString());
		return "OK";
		
	}
	
	@RequestMapping("mo")
	@ResponseBody
	public String mo(HttpServletRequest request,ModelAndView modelAndView) {
		logger.info("mo:::::::::: "+request.getQueryString());
		MalasiyaMOMessage malasiyaMOMessage=null;
		try{
		logger.info("mo:::::::::::::::::::::::::::::::: "+request.getQueryString());		
		 malasiyaMOMessage=new MalasiyaMOMessage(true);		
		malasiyaMOMessage.setMsisdn(request.getParameter("from"));		
		malasiyaMOMessage.setTime(request.getParameter("time"));
		malasiyaMOMessage.setReqTime(MKMalaysiaConstant.convertStringToTimestamp(malasiyaMOMessage.getTime()));
		malasiyaMOMessage.setShortcode(request.getParameter("shortcode"));
		malasiyaMOMessage.setMoid(request.getParameter("moid"));
		malasiyaMOMessage.setMsgid(request.getParameter("msgid"));
		malasiyaMOMessage.setTelcoid(MUtility.toInt(request.getParameter("telcoid"),0));
		malasiyaMOMessage.setChannel(request.getParameter("channel"));
		malasiyaMOMessage.setRefId(request.getParameter("refid"));
		malasiyaMOMessage.setQueryStr(request.getQueryString());
		malasiyaMOMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
		malasiyaMOMessage.setOpId(MalaysiaOperatorTelcoidMap.getOperatorId(malasiyaMOMessage.getTelcoid()));
		malasiyaMOMessage.setText(request.getParameter("text"));
		if(malasiyaMOMessage.getText()!=null&&malasiyaMOMessage.getText().
				toUpperCase().contains("STOP")){
			malasiyaMOMessage.setKeyword(malasiyaMOMessage.getText().split(" ")[1]);
		}else{
			malasiyaMOMessage.setKeyword(malasiyaMOMessage.getText());
		}
		}catch(Exception ex){
			logger.error("Exception",ex);
		}finally{
			jmsMalaysiaService.saveMOMessage(malasiyaMOMessage);	
		}
		return "OK";		
	}
	
	@RequestMapping("dn")
	@ResponseBody
	public String dn(HttpServletRequest request,ModelAndView modelAndView) {
		logger.info("dn:::::::::: "+request.getQueryString());
		
		MalaysiaDeliveryNotification malaysiaDeliveryNotification=null;
		try{
		logger.info("deliveryNotification:::::::::::::::::::::::::::::::: "+request.getQueryString());
		//// http://www.yourdomainDNurl/receive.aspx?mtid=123296707&moid=1234567&
		// msisdn=66874111222&shortcode=4541889&telcoid=1&countryid=3&datetime=2010-06-15
		// 10:10:10&status=OK
		 malaysiaDeliveryNotification=new MalaysiaDeliveryNotification(true);
		malaysiaDeliveryNotification.setMtid(request.getParameter("mtid"));
		malaysiaDeliveryNotification.setMoid(request.getParameter("moid"));		
		malaysiaDeliveryNotification.setMsisdn(request.getParameter("msisdn"));
		malaysiaDeliveryNotification.setDatetime(MKMalaysiaConstant.convertStringToTimestamp(request.getParameter("datetime")));
		malaysiaDeliveryNotification.setShortcode(request.getParameter("shortcode"));	
		malaysiaDeliveryNotification.setTelcoId(MUtility.toInt(request.getParameter("telcoid"),0));
		malaysiaDeliveryNotification.setCountryId(MUtility.toInt(request.getParameter("countryid"),0));		
		malaysiaDeliveryNotification.setQueryStr(request.getQueryString());
		malaysiaDeliveryNotification.setCreateTime(new Timestamp(System.currentTimeMillis()));
		malaysiaDeliveryNotification.setStatus(request.getParameter("status"));		
		malaysiaDeliveryNotification
		.setOpId(MalaysiaOperatorTelcoidMap.getOperatorId(malaysiaDeliveryNotification.getTelcoId()));
		
		}catch(Exception ex){
			logger.error("exception",ex);
		}finally{
			jmsMalaysiaService.saveMalaysiaDeliveryNotification(malaysiaDeliveryNotification);
		}		
		return "ok";
	}
	
	
	@RequestMapping("notification")
	@ResponseBody
	public String notification(HttpServletRequest request,ModelAndView modelAndView) {
		
		logger.info("notification:::::::::: "+request.getQueryString());
		return "OK";		
	}
	
	@RequestMapping("dct")	
	public ModelAndView dct(HttpServletRequest request,ModelAndView modelAndView) {		
		logger.info("dct:::::::::: "+request.getQueryString());
		modelAndView.setViewName("mkmalaysia/unsub");
		return modelAndView;		
	}
	
	@RequestMapping("unsub/process")	
	
	public ModelAndView unsubscriptionProcess(HttpServletRequest request,ModelAndView modelAndView) {		
		logger.info("unsubscriptionProcess:::::::::: "+request.getQueryString());
		modelAndView.setViewName("mkmalaysia/unsub_finall");
		return modelAndView;		
	}
	
	@RequestMapping("tocg")
	public ModelAndView toCG(ModelAndView modelAndView,HttpServletRequest  request){
		
		String token=request.getParameter("token");
		MKMalaysiaCGToken mkMalaysiaCGToken=new MKMalaysiaCGToken(token);//neeed to changebase on 
		VWServiceCampaignDetail vwServiceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(mkMalaysiaCGToken.getCampaignId());
		
	MKMalaysiaConfig mkMalaysiaConfig=MKMalaysiaConstant
			.mapServiceIdToMKMalaysiaConfig
			.get(vwServiceCampaignDetail.getServiceId());
	
		
		String authToken=mkmalaysiaServiceApi.getToken(mkMalaysiaConfig, token);		
		String url=moUrl+"?Telcoid="+mkMalaysiaConfig.getTelcoId()+"&Shortcode="+mkMalaysiaConfig.getShortcode()
		+"&Keyword="+mkMalaysiaConfig.getKeyword()+"&refid="+token+"&AuthToken="+authToken;		
		logger.info("toMo:: url: "+url);
		modelAndView.setView(new RedirectView(url));
		return modelAndView;	
	}
	
	@RequestMapping("gettime")
	@ResponseBody
	public String gettime(ModelAndView modelAndView,HttpServletRequest  request){
		String info="info: "+new Timestamp(System.currentTimeMillis());
		 info=" , getFormatUTC8Date:: "+MKMalaysiaConstant.getFormatUTC8Date();			
		info+=", dattime:: "+MKMalaysiaConstant.yyyyMMddHHmmssAccessToken.
				format(MKMalaysiaConstant.getFormatUTC8Date());
		info+=", format toke time: "+MKMalaysiaConstant.getFormatUTC8TokenTime();
		//System.out.println("dateTime:: "+dateTime);
		return info;
	}
	
	
	@RequestMapping("cache/value")
	@ResponseBody
	public Object getCacheValue(HttpServletRequest request){
		  Long dnCounter=redisCacheService.getAndIcrementIntValue(
		    		MKMalaysiaConstant.MALASIYA_DN_CAHCHE_PREFIX+
		    		request.getParameter("mtid"), 1,240);
		return dnCounter;
	}
	  		
}
