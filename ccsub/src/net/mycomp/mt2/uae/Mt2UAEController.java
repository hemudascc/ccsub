package net.mycomp.mt2.uae;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Controller
@RequestMapping("mt2uae")
public class Mt2UAEController {


	private static final Logger logger = Logger
			.getLogger(Mt2UAEController.class.getName());

	@Autowired
	private RedisCacheService redisCacheService;

	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Autowired
	private SubscriberRegService subscriberRegService;


	@Autowired
	private JMSMt2UAEService jmsMt2UAEService;

	@Autowired
	private Mt2UAEServiceApi mt2UAEServiceApi;
	
	@RequestMapping(value={"notification"},method={RequestMethod.GET,RequestMethod.POST})	
	@ResponseBody
	public Map<String,String>  notification(HttpServletRequest request,ModelAndView modelAndView){
		
		Mt2UAENotification mt2UAENotification=new Mt2UAENotification(true);
		Map<String,String> responseMap=new HashMap<String,String>();
		try{
			//?Username=&Password=&SubscriptionRef=&MSISDN=&OperatorID=
			//&ServiceTag=&Amount=&CurrencyCode=&CurrencyISOCode=&CurrencySymbol=&CurrencyDescription=&
			//NextRenewalDate=&ChargeStatus=&SubscriptionStatus=&channel=&ServiceId=&operatorId=
			//{"ResponseCode":"Success","Message":"Subscription info shared successfully"}
			responseMap.put("ResponseCode","Success");
			responseMap.put("Message","Subscription info shared successfully");			
			logger.info("notification:: "+request.getQueryString());
			mt2UAENotification.setQueryStr(request.getQueryString());			
			mt2UAENotification.setUserName(request.getParameter("Username"));
			mt2UAENotification.setPassword(request.getParameter("Password"));
			mt2UAENotification.setSubscriptionRef(request.getParameter("SubscriptionRef"));
			mt2UAENotification.setMsisdn(request.getParameter("MSISDN"));
			mt2UAENotification.setOperatorID(request.getParameter("OperatorID"));
			mt2UAENotification.setServiceTag(request.getParameter("ServiceTag"));
			mt2UAENotification.setAmount(request.getParameter("Amount"));
			mt2UAENotification.setCurrencyCode(request.getParameter("CurrencyCode"));
			mt2UAENotification.setCurrencyISOCode(request.getParameter("CurrencyISOCode"));
			mt2UAENotification.setCurrencySymbol(request.getParameter("CurrencySymbol"));
			mt2UAENotification.setCurrencyDescription(request.getParameter("CurrencyDescription"));
			mt2UAENotification.setNextRenewalDate(request.getParameter("NextRenewalDate"));
			mt2UAENotification.setChargeStatus(request.getParameter("ChargeStatus"));
			mt2UAENotification.setSubscriptionStatus(request.getParameter("SubscriptionStatus"));
			mt2UAENotification.setChannel(request.getParameter("channel"));
			mt2UAENotification.setServiceId(request.getParameter("ServiceId"));
			mt2UAENotification.setTrxid(request.getParameter("trxid"));
			
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			//if(!mt2UAENotification.getCurrencyISOCode().equalsIgnoreCase("JOD")) {
				jmsMt2UAEService.saveMt2UAENotification(mt2UAENotification);
			//}
		}
		return responseMap;
	}
	
	@RequestMapping(value={"cgcallback/{token}"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView cgCallback(HttpServletRequest request,ModelAndView modelAndView, @PathVariable(value="token")String token){
		
		Mt2UAECGCallback mt2UAECGCallback=new Mt2UAECGCallback(true);
		try{
			logger.info("cgcallback:: "+request.getQueryString());
			mt2UAECGCallback.setQueryStr(request.getQueryString());
			mt2UAECGCallback.setToken(token);
			mt2UAECGCallback.setMsisdn(request.getParameter("msisdn"));
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			
			Mt2UAEServiceConfig mt2UAEServiceConfig=Mt2UAEConstant
					.mapServiceIdToMt2UAEServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			
			LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
					   new Timestamp(System.currentTimeMillis()),
					   cgToken.getCampaignId()
					   ,vwserviceCampaignDetail.getServiceId(),
					   vwserviceCampaignDetail.getProductId());
					   liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());						   
					   SubscriberReg subscriberReg=  subscriberRegService.findOrCreateSubscriberByAct(
							   mt2UAECGCallback.getMsisdn(),null, liveReport);
			  
			   modelAndView.clear();
			   modelAndView.setView(new RedirectView(Mt2UAEConstant.getPortalUrl
					   (mt2UAEServiceConfig.getPortalUrl()
								  , mt2UAECGCallback.getMsisdn(),subscriberReg.getSubscriberId())));
			   
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsMt2UAEService.saveMt2UAECGCallback(mt2UAECGCallback);
		}
		return modelAndView;
	}


@RequestMapping(value="/v2/cgcallback",method = RequestMethod.GET)
public ModelAndView cgCallbackSdp(HttpServletRequest request,ModelAndView modelAndView){
	//token=collectcent&trxid=aaa2086a11f048adb81cabd162e2f4d2
	Mt2UAECGCallbackSdp mt2uaecgCallback = new Mt2UAECGCallbackSdp(true);
	Mt2UAEServiceConfig mt2UAEServiceConfig=null;
	CGToken cgToken=null;
	try {
		mt2uaecgCallback.setToken(request.getParameter("token"));
		mt2uaecgCallback.setTrackingId(request.getParameter("trxid"));
		mt2uaecgCallback.setQueryStr(request.getQueryString());
		cgToken = new CGToken(mt2uaecgCallback.getToken());
		VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		mt2UAEServiceConfig = Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig.get(vwServiceCampaignDetail.getServiceId());
		if(Objects.nonNull(mt2UAEServiceConfig)) {
			modelAndView.addObject("mt2UAEServiceConfig",mt2UAEServiceConfig);
		}
	} catch (Exception e) {
		logger.error("exception cg callback mt2 uae"+e);
	}finally {
		jmsMt2UAEService.saveMt2UAECGCallbackSdp(mt2uaecgCallback);
	}
	modelAndView.setViewName("/mt2uae/final");
	return modelAndView;
}

@RequestMapping(value = "/v2/tracking", method = {RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public String tracking(HttpServletRequest request){
	//token=1234&msisdn6789
	Mt2UAETracking mt2uaeTracking = new Mt2UAETracking(true);
	try {	
		mt2uaeTracking.setToken(request.getParameter("token"));
		mt2uaeTracking.setMsisdn(request.getParameter("msisdn"));
		mt2uaeTracking.setQueryStr(request.getQueryString());
	} catch (Exception e) {
		logger.error("exception notification mt2 uae"+e);
	}finally {
		jmsMt2UAEService.saveMt2UAETrakingSdp(mt2uaeTracking);
	}
	return "OK";
}

@RequestMapping(value = "/v2/notification", method = {RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public Map<String, String> notificationSdp(HttpServletRequest request){
	//Id=20122212303719741&Data=S,392,87dba3d7-fba1-40f2-9f6b-b9dacb374754&TrackingId=aaa2086a11f048adb81cabd162e2f4d2
	//&MSISDN=971523051932&ShortCode=9132&Date=20202212&Operator=Du&ValidityDays=0
	Mt2UAENotificationSdp mt2UAENotification = new Mt2UAENotificationSdp(true);
	Map<String, String> response = new HashMap<>();
	response.put("ResponseCode", "Success");
	response.put("Message", "Subscription info shared successfully");
	try {	
		mt2UAENotification.setSdpId(request.getParameter("Id"));
		mt2UAENotification.setData(request.getParameter("Data"));
		mt2UAENotification.setTrackingId(request.getParameter("TrackingId"));
		mt2UAENotification.setMsisdn(request.getParameter("MSISDN"));
		mt2UAENotification.setShortCode(request.getParameter("ShortCode"));
		mt2UAENotification.setSdpDate(request.getParameter("Date"));
		mt2UAENotification.setOperator(request.getParameter("Operator"));
		mt2UAENotification.setValidity(request.getParameter("ValidityDays"));
		mt2UAENotification.setQueryStr(request.getQueryString());
	} catch (Exception e) {
		logger.error("exception notification mt2 uae"+e);
	}finally {
		jmsMt2UAEService.saveMt2UAENotificationSdp(mt2UAENotification);
	}
	return response;
}


@RequestMapping(value={"/v2/dlr"},method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public Map<String, String> dlrSdp(@RequestBody List<Mt2UAEDeliveryNotificationSdp> dlrs){
	Map<String, String> response = new HashMap<>();
	response.put("ResponseCode", "Success");
	response.put("Message", "Subscription info shared successfully");
	try{
		logger.info("dlrs="+dlrs);
	}catch(Exception  ex){logger.error("Exception" ,ex);}finally{
		jmsMt2UAEService.saveMt2UAEDlrSdp(dlrs);
	}
	return response;
}

//http://192.241.253.234/ccsub/cnt/mt2uae/check-by-refrenceid?op=du&id=<refrenceid>
@RequestMapping(value={"check-by-refrenceid"},method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public Map<String, String> subInfoByReferenceId(@RequestParam("id") String id,@RequestParam("op") String op){
	Map<String, String> response = new HashMap<>();
	response.put("subid", "");
	response.put("status", "");
	response.put("opid", "");
	SubscriberReg subscriberReg=null;
	try {
		List<SubscriberReg> subscriberRegs = jpaSubscriberReg.findSubscriberRegByParam1(id);
		if(Objects.nonNull(subscriberRegs) && subscriberRegs.size()>0) {
			subscriberReg = subscriberRegs.get(0);
			response.put("subid", subscriberReg.getSubscriberId().toString());
			response.put("status", subscriberReg.getStatus()==1?"ACTIVE":"INACTIVE");
			response.put("opid", op);
		}
	} catch (Exception e) {
		logger.error("error while fetching subs details by reference id");
	}
	return response;
}
//http://192.241.253.234/ccsub/cnt/mt2uae/check-by-msisdn?op=du&msisdn=<msisdn>
@RequestMapping(value={"check-by-msisdn"},method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public Map<String, String> subInfoByMsisdn(@RequestParam("msisdn") String msisdn, @RequestParam("op") String op){
	Map<String, String> response = new HashMap<>();
	response.put("subid", "");
	response.put("status", "");
	response.put("opid", "");
	SubscriberReg subscriberReg=null;
	try {
		List<SubscriberReg> subscriberRegs = jpaSubscriberReg.findSubscriberRegByMsisdn(msisdn);
		if(Objects.nonNull(subscriberRegs) && subscriberRegs.size()>0) {
			subscriberReg = subscriberRegs.get(0);
			response.put("subid", subscriberReg.getSubscriberId().toString());
			response.put("status", subscriberReg.getStatus()==1?"ACTIVE":"INACTIVE");
			response.put("opid", op);
		}
	} catch (Exception e) {
		logger.error("error while fetching subs details by reference id");
	}
	return response;
}

@RequestMapping(value={"web/send/otp"},method={RequestMethod.GET,RequestMethod.POST})	
public ModelAndView webSendOTP(HttpServletRequest request,ModelAndView modelAndView,
		@RequestParam(name="l",defaultValue="0")Integer lang){

	try{

		modelAndView.addObject("l",lang);
		logger.info("webSendOTP:::::::: 11 msisdn:: "+request.getParameter("msisdn"));

		String token=request.getParameter("token");	
		String ip=request.getRemoteAddr();	


		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwserviceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());			
		Mt2UAEServiceConfig mt2UAEServiceConfig =Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
				.get(vwserviceCampaignDetail.getServiceId());
		modelAndView.setViewName(mt2UAEServiceConfig.getLpPage());
		//			String msisdn=Mt2UAEConstant.formatMsisdn(request.getParameter("msisdn")
		//					,mt2UAEServiceConfig.getMsisdnPrefix());
		String msisdn=request.getParameter("msisdn");
		logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token);
		modelAndView.addObject("token",token);
		modelAndView.addObject("msisdn",msisdn);
		modelAndView.addObject("mt2UAEServiceConfig",mt2UAEServiceConfig);
		//			MT2UAEServiceApiTrans mt2UAEServiceApiTrans=mt2UAEServiceApi.getSubscription(mt2UAEServiceConfig, ip);	 
		//			if(mt2UAEServiceApiTrans.getSuccess()){
		//	    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
		//					msisdn, 
		//					   vwserviceCampaignDetail.getProductId());
		//	    	
		//			   modelAndView.setView(new RedirectView(Mt2UAEConstant.getPortalUrl
		//					   (mt2UAEServiceConfig.getPortalUrl()
		//						  ,msisdn,subscriberReg.getSubscriberId()))); 
		//			   return modelAndView;
		//		}



		MT2UAEServiceApiTrans mt2UAEServiceApiTransendOtp=mt2UAEServiceApi
				.sendOTP(mt2UAEServiceConfig, msisdn, ip,token);
		if(mt2UAEServiceApiTransendOtp.getSuccess()){
			//modelAndView.setViewName("mt2uae/wap_otp");
			modelAndView.setViewName(mt2UAEServiceConfig.getLpPage2());
			modelAndView.addObject("otpinfo","We have sent you a PIN code on your phone number");
		}else{

			modelAndView.addObject("otpinfo","Please enter valid mobile number");
		}				

	}catch(Exception  ex){
		logger.error("Exception" ,ex);
	}

	return modelAndView;
}

@RequestMapping(value={"web/send/otp/validation"},method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView sendOTPValidation(HttpServletRequest request,ModelAndView modelAndView
		,@RequestParam(name="l",defaultValue="0")Integer lang){
	try{
		modelAndView.setViewName("mt2uae/wap_otp");

		modelAndView.addObject("l",lang);

		String token=request.getParameter("token");
		String pin=request.getParameter("pin"); 
		String ip=request.getRemoteAddr();

		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwserviceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

		Mt2UAEServiceConfig mt2UAEServiceConfig =Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
				.get(vwserviceCampaignDetail.getServiceId());
		modelAndView.setViewName(mt2UAEServiceConfig.getLpPage2());
		//				String msisdn=Mt2UAEConstant.formatMsisdn(request.getParameter("msisdn")
		//						,mt2UAEServiceConfig.getMsisdnPrefix());
		String msisdn=request.getParameter("msisdn");
		logger.info("sendOTPValidation:::::::: msisdn:: "+msisdn+", token:: "+token+", pin:: "+pin);
		modelAndView.addObject("token",token);
		modelAndView.addObject("msisdn",msisdn);
		modelAndView.addObject("mt2UAEServiceConfig",mt2UAEServiceConfig);

		//				MT2UAEServiceApiTrans mt2UAEServiceApiTrans=mt2UAEServiceApi.getSubscription(mt2UAEServiceConfig, ip);
		//				 if(mt2UAEServiceApiTrans.getSuccess()){
		//				    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
		//								msisdn, 
		//								   vwserviceCampaignDetail.getProductId());
		//				    	
		//						   modelAndView.setView(new RedirectView(Mt2UAEConstant.getPortalUrl
		//								   (mt2UAEServiceConfig.getPortalUrl()
		//									  ,msisdn,subscriberReg.getSubscriberId()))); 
		//						   return modelAndView;
		//					}




		modelAndView.setViewName(mt2UAEServiceConfig.getLpPage2());
		MT2UAEServiceApiTrans mt2UAEServiceApiTransSubscribe=mt2UAEServiceApi
				.subscribe(mt2UAEServiceConfig, msisdn, pin,token);

		if(mt2UAEServiceApiTransSubscribe.getSuccess()){
			//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
			LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
					new Timestamp(System.currentTimeMillis()),
					cgToken.getCampaignId()
					,vwserviceCampaignDetail.getServiceId(),
					vwserviceCampaignDetail.getProductId());
			liveReport.setNoOfDays(mt2UAEServiceConfig.getValidity());						   
			SubscriberReg subscriberReg=  subscriberRegService
					.findOrCreateSubscriberByAct(msisdn,null, liveReport);						 
			modelAndView.clear();
			modelAndView.setView(new RedirectView
					(Mt2UAEConstant.getPortalUrl(mt2UAEServiceConfig.getPortalUrl(), msisdn
							, subscriberReg.getSubscriberId())));


		}else{
			modelAndView.addObject("otpinfo","Please enter valid pin");
		}
	}catch(Exception  ex){
		logger.error("Exception" ,ex);
	}
	return modelAndView;

}

@RequestMapping(value={"chnage/lang"},method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView changeLang(HttpServletRequest request,ModelAndView modelAndView,
		@RequestParam(name="l",defaultValue="0")Integer lang){
	try{
		String page=request.getParameter("page");

		modelAndView.setViewName("mt2uae/"+page);
		modelAndView.addObject("l",lang);
		String msisdn=request.getParameter("msisdn");
		String token=request.getParameter("token");
		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwserviceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

		Mt2UAEServiceConfig mt2UAEServiceConfig =Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
				.get(vwserviceCampaignDetail.getServiceId());

		String pin=request.getParameter("pin"); 
		modelAndView.addObject("token",token);
		modelAndView.addObject("msisdn",msisdn);
		modelAndView.addObject("mt2UAEServiceConfig",mt2UAEServiceConfig);

	}catch(Exception  ex){
		logger.error("Exception" ,ex);
	}
	return modelAndView;

}

@RequestMapping(value={"jod/login"},method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public String jodlogin(HttpServletRequest request,ModelAndView modelAndView
		){
	Map<String,Object> map=new HashMap<String,Object>();
	map.put("status",Mt2UAEConstant.getStatus(0));
	map.put("op", "na");
	map.put("subid", "");
	try{

		List<SubscriberReg> subscriberRegList=jpaSubscriberReg.findSubscriberRegByMsisdn(
				Mt2UAEConstant.formatMsisdn(request.getParameter("msisdn"),"962"));
		for(SubscriberReg tmpsubscriberReg:subscriberRegList){
			if(tmpsubscriberReg.getOperatorId()==MConstants.MT2_JORDAN_OPERATOR_ID){
				map.put("status",Mt2UAEConstant.getStatus(tmpsubscriberReg.getStatus()));
				map.put("op", "jod");
				map.put("subid", tmpsubscriberReg.getSubscriberId());
				break;
			}		
		}
	}catch(Exception  ex){
		logger.error("Exception" ,ex);
	}
	return JsonMapper.getObjectToJson(map);		
}


@RequestMapping(value={"login","du/login"},method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public String login(HttpServletRequest request,ModelAndView modelAndView
		){
	Map<String,Object> map=new HashMap<String,Object>();
	map.put("status",Mt2UAEConstant.getStatus(0));
	map.put("op", "na");
	map.put("subid", "");
	try{

		List<SubscriberReg> subscriberRegList=jpaSubscriberReg.findSubscriberRegByMsisdn(request.getParameter("msisdn"));
		for(SubscriberReg tmpsubscriberReg:subscriberRegList){
			if(tmpsubscriberReg.getOperatorId()==MConstants.MT2_UAE_DU_OPERATOR_ID){
				map.put("status",Mt2UAEConstant.getStatus(tmpsubscriberReg.getStatus()));
				map.put("op", "du");
				map.put("subid", tmpsubscriberReg.getSubscriberId());
				break;
			}else if(tmpsubscriberReg.getOperatorId()==MConstants.MT2_UAE_ETISALAT_OPERATOR_ID){
				map.put("status",Mt2UAEConstant.getStatus(tmpsubscriberReg.getStatus()));
				map.put("op", "eti");
				map.put("subid", tmpsubscriberReg.getSubscriberId());
				break;
			}else if(tmpsubscriberReg.getOperatorId()==MConstants.MT2_JORDAN_OPERATOR_ID){
				map.put("status",Mt2UAEConstant.getStatus(tmpsubscriberReg.getStatus()));
				map.put("op", "jod");
				map.put("subid", tmpsubscriberReg.getSubscriberId());
				break;
			}

		}
	}catch(Exception  ex){
		logger.error("Exception" ,ex);
	}
	return JsonMapper.getObjectToJson(map);		
}

@RequestMapping(value={"eti/login"},method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public String etilogin(HttpServletRequest request,ModelAndView modelAndView
		){

	Map<String,Object> map=new HashMap<String,Object>();	
	map.put("status", "fail");
	map.put("op", "na");
	map.put("subid","");

	try{

		List<SubscriberReg> subscriberRegList=jpaSubscriberReg
				.findSubscriberRegByMsisdn(request.getParameter("msisdn"));
		String pwd=request.getParameter("pwd");

		for(SubscriberReg tmpsubscriberReg:subscriberRegList){
			if(pwd.equalsIgnoreCase(tmpsubscriberReg.getParam1())){

				if(tmpsubscriberReg.getOperatorId()==MConstants.MT2_UAE_ETISALAT_OPERATOR_ID){
					map.put("status",Mt2UAEConstant.getStatus(tmpsubscriberReg.getStatus()));
					map.put("status", pwd.equalsIgnoreCase(tmpsubscriberReg.getParam1())&&
							Mt2UAEConstant.getStatus(tmpsubscriberReg.getStatus())
							.equalsIgnoreCase("success")
							?"success":"fail");

					map.put("op", "eti");
					map.put("subid", tmpsubscriberReg.getSubscriberId());
					break;
				}
			}
		}

	}catch(Exception  ex){
		logger.error("Exception" ,ex);
	}
	return JsonMapper.getObjectToJson(map);		
}

@RequestMapping(value={"putcahce"},method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public String putcahce(HttpServletRequest request,ModelAndView modelAndView
		){
	String msisdn=request.getParameter("msisdn");
	String token=request.getParameter("token");
	redisCacheService.putObjectCacheValueByEvictionDay(Mt2UAEConstant.MT2_UAE_SUB_CAHCHE_PREFIX+msisdn,
			token, 1);
	return "ok";
}


@RequestMapping("tc")	
public ModelAndView termAndCondition(HttpServletRequest request,ModelAndView modelAndView){

	CGToken cgToken=new CGToken
			(request.getParameter("token"));
	String l=request.getParameter("l");
	if(l==null){
		l="0";
	}
	VWServiceCampaignDetail vwServiceCampaignDetail=MData.
			mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

	Mt2UAEServiceConfig mt2UAEServiceConfig=
			Mt2UAEConstant.mapServiceIdToMt2UAEServiceConfig
			.get(vwServiceCampaignDetail.getServiceId());

	modelAndView.addObject("mt2UAEServiceConfig", mt2UAEServiceConfig);
	modelAndView.addObject("l", l);
	modelAndView.setViewName("mt2uae/termscond");
	return modelAndView;
}

@RequestMapping(value={"portal"},method={RequestMethod.GET,RequestMethod.POST})	
public ModelAndView portal(HttpServletRequest request,ModelAndView modelAndView
		){

	String portalUrl="";
	try{
		Integer serviceId=MUtility.toInt(request.getParameter("serviceid"),0);
		Mt2UAEServiceConfig mt2UAEServiceConfig=Mt2UAEConstant
				.mapServiceIdToMt2UAEServiceConfig.get(serviceId);
		portalUrl=mt2UAEServiceConfig.getPortalUrl();

		SubscriberReg subscriberReg=jpaSubscriberReg
				.findSubscriberRegByMsisdnAndProductId(request.getParameter("msisdn"), mt2UAEServiceConfig.getProductId());
		portalUrl=portalUrl.replaceAll("<subid>", ""+subscriberReg.getSubscriberId());

	}catch(Exception  ex){
		logger.error("Exception" ,ex);
	}finally{
		modelAndView.setView(new RedirectView(portalUrl));
	}
	return modelAndView;		
}
}
