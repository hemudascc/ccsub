package net.mycomp.veoo;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import net.common.service.RedisCacheService;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.hibernate.type.descriptor.java.MutabilityPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("veoo")
public class VeooController {

	
	private static final Logger logger = Logger
			.getLogger(VeooController.class.getName());
	
	private final  String veooHeCallback;
	private final String veooHeUrl;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private VeooService veooService;
	
	@Autowired
	private VeooApiService veooApiService;
	
	@Autowired
	private JMSVeooService jmsVeooService;
	
	@Autowired
	private VeooScheduler veooScheduler;
	private final String pinSendMessage;
	private final String pinSendErrorMessage;
	private final String pinValidationSuccessMessage;
	private final String pinValidationErrorMessage;
	
	@Autowired
	   public VeooController(@Value("${veoo.he.callback}") String  veooHeCallback,
			   @Value("${veoo.he.url}") String  veooHeUrl,
			   @Value("${veoo.pin.send.message}") String  pinSendMessage,
			   @Value("${veoo.pin.send.error.message}") String  pinSendErrorMessage,
			   @Value("${veoo.pin.validation.success.message}") String  pinValidationSuccessMessage,
			   @Value("${veoo.pin.validation.error.message}") String  pinValidationErrorMessage){
		
			this.veooHeCallback=veooHeCallback;
			this.veooHeUrl=veooHeUrl;
			this.pinSendMessage=pinSendMessage;
			this.pinSendErrorMessage=pinSendErrorMessage;
			this.pinValidationSuccessMessage=pinValidationSuccessMessage;
			this.pinValidationErrorMessage=pinValidationErrorMessage;
			
		}
	
	
	@RequestMapping("cmp")	
	public ModelAndView cmp(HttpServletRequest request,ModelAndView modelAndView){
		
		String encodedQueryString=MUtility.getBase64EncodedString(request.getQueryString());	
		String headerCallbackUrl=veooHeCallback.replaceAll("<query>", encodedQueryString);		
		//modelAndView.addObject("msisdnHeaderUrl", headerUrl);	
		String url=veooHeUrl.replaceAll("<callback>",MUtility.urlEncoding(headerCallbackUrl));
		logger.info("veoocmp::: "+request.getQueryString()+", redirect to :: "+url);
		modelAndView.setView(new RedirectView(url));
		return modelAndView;
	}
	
	
	@RequestMapping(value="lpform",method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView lpform(HttpServletRequest request,ModelAndView modelAndView){
		VeooServiceConfig veooServiceConfig=null;
		try{
		logger.info("lpform::: "+request.getQueryString());		
		String token=request.getParameter("token");
		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=MData.
				mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		 veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.
				 get(vwServiceCampaignDetail.getServiceId());
		 VeooCGToken veooCGToken=new VeooCGToken(cgToken.getTokenId());
		VeooClickFlowUrlResponse veooClickFlowUrlResponse=
				veooApiService.callClickFlowUrl(veooServiceConfig,veooCGToken.getCGToken());
		logger.info("lpform:::: veooClickFlowUrlResponse:: "+veooClickFlowUrlResponse);
		
		if(veooClickFlowUrlResponse!=null&&((veooClickFlowUrlResponse.getResult()!=null&&(
				veooClickFlowUrlResponse.getResult().equalsIgnoreCase(VeooConstant.SUCCESS)))||
				(veooClickFlowUrlResponse.getStatus()!=null&&
				veooClickFlowUrlResponse.getStatus().equalsIgnoreCase(VeooConstant.SUCCESS)))){			
			modelAndView.setView(new RedirectView(veooClickFlowUrlResponse.getSpecial().getRedirectUrl()));	
		}else{
			modelAndView.setViewName(veooServiceConfig.getErrorUrl());		
		}
		
		}catch(Exception ex){
			logger.error("lpform:: ",ex);
			if(veooServiceConfig!=null){
				modelAndView.setViewName(veooServiceConfig.getErrorUrl());	
			}
			modelAndView.setViewName("veoo/error");
		}
		return modelAndView;
	}
	
	@RequestMapping("mo")	
	@ResponseBody
	public String mo(HttpServletRequest request,ModelAndView modelAndView){
		//timestamp=2019-04-06T03:24:51&username=&password=&msisdn=50499634712&
		//shortcode=2511&time=2019-04-06T03%3A24%3A51&id=9855914a-a723-407b-afd1-1a800e5b2fb3&
		//keyword=salir&network=708002&networkid=708002&message=salir%20idea%20-%20imobile&
		//service_id=12c415c9-7ef2-11e8-b0a0-22000ac4c542
		
		VeooMo veooMo=new VeooMo();
		veooMo.setUserName(request.getParameter("username"));
		veooMo.setPassword(request.getParameter("password"));
		veooMo.setMsisdn(request.getParameter("msisdn"));
		veooMo.setShortcode(request.getParameter("shortcode"));
		veooMo.setTimestamp(request.getParameter("timestamp"));
		veooMo.setVeooId(request.getParameter("id"));
		veooMo.setKeyword(request.getParameter("keyword"));
		veooMo.setNetwork(request.getParameter("network"));
		veooMo.setNetworkId(request.getParameter("networkid"));
		veooMo.setMessage(request.getParameter("message"));
		veooMo.setServiceId(request.getParameter("service_id"));
		veooMo.setQueryStr(request.getQueryString());
		jmsVeooService.saveMO(veooMo);
		return "OK";
	}
	
	
	
	@RequestMapping("receipt")	
	@ResponseBody
	public String receipt(HttpServletRequest request,ModelAndView modelAndView){
		// status=1%3A%20delivery%20success&id=REN19-1554563704120-43175&
		//msisdn=50495271747&origin=2511&timestamp=2019-04-06T15%3A49%3A02&statusCode=000&
		//statusText=VEOO_DELIVERED&type=premium&service_id=12c415c9-7ef2-11e8-b0a0-22000ac4c542
		
		VeooDeliveryReceipt veooDeliveryReceipt=new VeooDeliveryReceipt(true);
		veooDeliveryReceipt.setUserName(request.getParameter("username"));
		veooDeliveryReceipt.setPassword(request.getParameter("password"));
		veooDeliveryReceipt.setMsisdn(request.getParameter("msisdn"));
		veooDeliveryReceipt.setOrigin(request.getParameter("origin"));
		veooDeliveryReceipt.setType(request.getParameter("type"));
		veooDeliveryReceipt.setDeliveryStatus(request.getParameter("status"));
		veooDeliveryReceipt.setStatusCode(request.getParameter("statusCode"));
		veooDeliveryReceipt.setStatusText(request.getParameter("statusText"));
		veooDeliveryReceipt.setMtid(request.getParameter("id"));
		veooDeliveryReceipt.setVeooTimestamp(request.getParameter("timestamp"));
		veooDeliveryReceipt.setVeooServiceId(request.getParameter("service_id"));
		veooDeliveryReceipt.setQueryStr(request.getQueryString());
		jmsVeooService.saveDeliveryReceipt(veooDeliveryReceipt);
		return "OK";
	}
	
	@RequestMapping("termcondtion")	
	public ModelAndView tigoGamePadTermCondition(HttpServletRequest request,ModelAndView modelAndView){
		String id	=request.getParameter("id");
		try{
		
		logger.info("tigoGamePadTermCondition::::::::: id:: "+id);
		VeooServiceConfig veooServiceConfig=VeooConstant.mapIdToVeooServiceConfig.get(MUtility.toInt(id, 0));
		modelAndView.setViewName(veooServiceConfig.getTermConditionPage());
		}catch(Exception ex){
			logger.error("tigoGamePadTermCondition::::::::: id "+id,ex);
			modelAndView.setViewName("veoo/error");
		}
		return modelAndView;
	}
	
	
	@RequestMapping("testclickflow")
	@ResponseBody
	public String testCallFlowApi(HttpServletRequest request,ModelAndView modelAndView){
		VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.
				 get(2);
		String msisdn=request.getParameter("msisdn");
		String token=request.getParameter("token");
		
		VeooClickFlowUrlResponse veooClickFlowUrlResponse=
				veooApiService.callClickFlowUrlTmp(veooServiceConfig,token,msisdn);
		return Objects.toString(veooClickFlowUrlResponse.toString());
	}
	
	@RequestMapping(value = {"send/renewal"}, method = RequestMethod.GET)	
	@ResponseBody
	public String sendRenewalRequest(){
		veooScheduler.sendRenewalBilled();
		return "OK:"+System.currentTimeMillis();
	}
	
	

	@RequestMapping(value="riacostalp2",method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView riacostalp2(HttpServletRequest request,ModelAndView modelAndView){
		VeooServiceConfig veooServiceConfig=null;
		try{
		logger.info("lpform::: "+request.getQueryString());		
		String token=request.getParameter("token");
		String msisdn=request.getParameter("msisdn");
		
		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=MData.
				mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		 veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.
				 get(vwServiceCampaignDetail.getServiceId());
		  modelAndView.addObject("veooServiceConfig", veooServiceConfig);
		 modelAndView.addObject("token", token);
		 modelAndView.addObject("msisdn",msisdn);
		 modelAndView.setViewName("veoo/costarica/gamepad_lp2");
		}catch(Exception ex){
			logger.error("lpform:: ",ex);
			
			modelAndView.setViewName("veoo/error");
		}
		return modelAndView;
	}
	
	
	
	
	@RequestMapping(value = {"send/pin"}, method = RequestMethod.GET)		
	public ModelAndView sendPin(ModelAndView modelAndView,HttpServletRequest request){
		try{
			
		
		String token=request.getParameter("token");
		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=
				MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		
		  VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.get(
				  vwServiceCampaignDetail.getServiceId());
		  String msisdn=VeooConstant.formatMsisdn(request.getParameter("msisdn"),veooServiceConfig
				  .getCountryIsdCode());
		  
		   modelAndView.addObject("veooServiceConfig", veooServiceConfig);
		   modelAndView.addObject("token", token);
		   modelAndView.addObject("msisdn",msisdn);
		   modelAndView.setViewName(veooServiceConfig.getPinLandingPage());	 
		   
		  if(veooService.checkSub(veooServiceConfig.getCcProductId(), veooServiceConfig.getOpId()
				  , msisdn)){			
			  modelAndView.addObject("sendinfo", veooServiceConfig.getAlreadySubscribedMessage());
			  return modelAndView;
		  }
		  
		  VeooPinSend veooPinSend=veooApiService.sendPin(veooServiceConfig, msisdn);
		   logger.info("processBilling:: veooServiceConfig:: "+veooServiceConfig);
		   
			   if(veooPinSend.getPinSend()){
				   modelAndView.addObject("sendinfo", pinSendMessage);
				 // modelAndView.setViewName("veoo/tn/gamepad_pin2");
				   modelAndView.setViewName(veooServiceConfig.getPinLandingPage2());	
				   
			   }else{
				   modelAndView.addObject("sendinfo", pinSendErrorMessage);
			   }
			   logger.info("processBilling:: veooServiceConfig:: "+veooServiceConfig);
			   
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}
		return modelAndView;
	}
	
	
	@RequestMapping(value = {"pin/validation"}, method = RequestMethod.GET)		
	public ModelAndView pinValidation(ModelAndView modelAndView,HttpServletRequest request){
		try{
			
		//String msisdn=request.getParameter("msisdn");
		String pin=request.getParameter("pin");
		String token=request.getParameter("token");
		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=
				MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		
		  VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.get(
				  vwServiceCampaignDetail.getServiceId());
		  String msisdn=VeooConstant.formatMsisdn(request.getParameter("msisdn"),veooServiceConfig
				  .getCountryIsdCode());
		  
		  modelAndView.setViewName(veooServiceConfig.getPinLandingPage());
		  
		  VeooPinValidation veooPinValidation=veooApiService.
				  pinValidation(veooServiceConfig, msisdn, pin);
		  
		  if(veooPinValidation.getPinValidate()){				  
			  modelAndView.addObject("validateinfo", pinValidationSuccessMessage);
			  jmsVeooService.processPinValidation(veooPinValidation);
		  }else{
			  modelAndView.addObject("validateinfo", pinValidationErrorMessage);  
		  }
			   logger.info("processBilling:: veooServiceConfig:: "+veooServiceConfig);
			   modelAndView.addObject("veooServiceConfig", veooServiceConfig);
			   modelAndView.addObject("token", token);
			   modelAndView.addObject("msisdn",msisdn);
			  	 
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}
		return modelAndView;
	}
	
@RequestMapping(value = {"manually/send/pin"}, method = RequestMethod.GET)	
	@ResponseBody
	public int sendManuallyPin(ModelAndView modelAndView,HttpServletRequest request){
	
	VeooPinSend veooPinSend=null;
	int send=0;
		try{
		String msisdn=request.getParameter("msisdn");
		 Integer cmpId=MUtility.toInt(request.getParameter("cmpid"),0);
		 VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cmpId);			
		
		  VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.get(
				  vwServiceCampaignDetail.getServiceId());
		  
		//  VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.get(2);
		  veooPinSend=veooApiService.sendPin(veooServiceConfig, msisdn);
			   logger.info("processBilling:: veooServiceConfig:: "+veooServiceConfig);
			  if(veooPinSend.getPinSend()){
				  send=1;
			   }
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}
		return send;
	}


@RequestMapping(value = {"manually/pin/validate"}, method = RequestMethod.GET)	
@ResponseBody
public int manuallyPinValidate(ModelAndView modelAndView,HttpServletRequest request){

	VeooPinValidation veooPinValidation=null;
	int pinValidate=0;
	try{
		
		 String msisdn=request.getParameter("msisdn");
		 String pin=request.getParameter("pin");
		 Integer cmpId=MUtility.toInt(request.getParameter("cmpid"),0);
		 VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cmpId);			
		 VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.get(
					  vwServiceCampaignDetail.getServiceId());			  
		  //VeooServiceConfig veooServiceConfig=VeooConstant.mapServiceIdToVeooServiceConfig.get(2);
		  veooPinValidation=veooApiService.pinValidation(veooServiceConfig, msisdn, pin);
		  logger.info("manuallyPinValidate:: veooPinValidation:: "+veooPinValidation);	
		  if(veooPinValidation.getPinValidate()){
			  CGToken cgToken=new CGToken(System.currentTimeMillis(),cmpId,0);
			  redisCacheService
			  .putObjectCacheValueByEvictionMinute(VeooConstant.PIN_VALIDATION_CACHE_PREFIX+msisdn, cgToken.getCGToken()
					  , 10*60);
			  
			  pinValidate=1;
		  }
	}catch(Exception ex){
		logger.error("manuallyPinValidate ",ex);
	}finally{
		jmsVeooService.processPinValidation(veooPinValidation);
	}
	return pinValidate;
}
}
