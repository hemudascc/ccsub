package net.mycomp.mt2.ksa;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPAMT2KSAServiceApiTrans;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;
import net.util.MUtility;

@Controller
@RequestMapping("mt2ksa")
public class Mt2KSAController {

	
	private static final Logger logger = Logger
			.getLogger(Mt2KSAController.class.getName());
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private Mt2KSAServiceApi mt2KSAServiceApi;
	
	@Autowired
	private Mt2KSAService mt2KSAService;
	@Autowired
	private JMSMt2KSAService jmsMt2KSAService;
	
	@Autowired
	private JPAMT2KSAServiceApiTrans jpaMT2KSAServiceApiTrans;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@RequestMapping(value={"notification"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String notification(HttpServletRequest request,ModelAndView modelAndView){
		Mt2KSANotification mt2KSANotification=new Mt2KSANotification(true);
		try{
			logger.info("notificaation:: "+request.getQueryString());
			mt2KSANotification.setNotificationId(request.getParameter("ID"));
			mt2KSANotification.setMo(request.getParameter("MO"));
			mt2KSANotification.setMt(request.getParameter("MT"));
			mt2KSANotification.setData(request.getParameter("DATA"));
			mt2KSANotification.setNotifcationDate(request.getParameter("Date"));
			mt2KSANotification.setOp(request.getParameter("OP"));
			mt2KSANotification.setQueryStr(request.getQueryString());
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsMt2KSAService.processNotification(mt2KSANotification);
		}
		return "OK";
	}
	
	
	@RequestMapping(value={"web/send/otp"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView webSendOTP(HttpServletRequest request,ModelAndView modelAndView){
		
       try{
    	   modelAndView.setViewName("mt2ksa/msisdn_missing");
    	    
    	   logger.info("webSendOTP:::::::: 11 msisdn:: "+request.getParameter("msisdn"));
			String msisdn=request.getParameter("msisdn");
			String token=request.getParameter("token");	
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token);
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			
			Mt2KSAServiceConfig mt2ksaServiceConfig=Mt2KSAConstant
					.mapServiceIdToMt2KSAServiceConfig.get(vwserviceCampaignDetail.getServiceId());		
			
	    if(mt2KSAService.checkSub(vwserviceCampaignDetail.getProductId(), vwserviceCampaignDetail.getOpId(),
	    		msisdn)){
	    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					   vwserviceCampaignDetail.getProductId());
	    	
			   modelAndView.setView(new RedirectView(Mt2KSAConstant.getPortalUrl
					   (mt2ksaServiceConfig.getPortalUrl()
						  ,msisdn,subscriberReg.getSubscriberId()))); 
			   return modelAndView;
		}
	    
//			MT2KSAServiceApiTrans mt2KSAServiceApiTrans= mt2KSAServiceApi.subStatus(mt2ksaServiceConfig, msisdn);
//			if(mt2KSAServiceApiTrans.getResponseToCaller()){
//				LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
//						   new Timestamp(System.currentTimeMillis()),
//						   cgToken.getCampaignId()
//						   ,vwserviceCampaignDetail.getServiceId(),
//						   vwserviceCampaignDetail.getProductId());
//						   liveReport.setNoOfDays(mt2ksaServiceConfig.getValidity());	
//						   
//			    subscriberReg=  subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
//				   modelAndView.clear();
//				 modelAndView.setView(new RedirectView(Mt2KSAConstant.getPortalUrl
//						   (mt2ksaServiceConfig.getPortalUrl()
//							  ,msisdn,subscriberReg.getSubscriberId()))); 
//				 
//			}
			
				
		    
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("mt2KSAServiceConfig",mt2ksaServiceConfig);
				
				MT2KSAServiceApiTrans mt2KSAServiceApiTrans=mt2KSAServiceApi.sendOTP(mt2ksaServiceConfig
						, msisdn, token);//OTP(mt2ksaServiceConfig, msisdn,token, "WEB");
				if(mt2KSAServiceApiTrans.getSuccess()){
					modelAndView.setViewName("mt2ksa/wap_otp");
					modelAndView.addObject("otpinfo","We have sent you a PIN code on your phone number");
				}else{
					//modelAndView.setViewName("mt2ksa/msisdn_missing");
					modelAndView.addObject("otpinfo","Please enter valid mobile number");
				}				
					
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value={"web/send/otp/validation"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView sendOTPValidation(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){
		 try{
			    modelAndView.setViewName("mt2ksa/wap_otp");
			    modelAndView.addObject("l",lang);
				String msisdn=request.getParameter("msisdn");
				String token=request.getParameter("token");
				String pin=request.getParameter("pin"); 
				String ip=request.getRemoteAddr();
				logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token+", pin:: "+pin);
				CGToken cgToken=new CGToken(token);
				VWServiceCampaignDetail vwserviceCampaignDetail=MData
						.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
				
				Mt2KSAServiceConfig mt2KSAServiceConfig=Mt2KSAConstant
						.mapServiceIdToMt2KSAServiceConfig.get(vwserviceCampaignDetail.getServiceId());
				
				 if(mt2KSAService.checkSub(vwserviceCampaignDetail.getProductId(), 
						 vwserviceCampaignDetail.getOpId(),
				    		msisdn)){
				    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
								msisdn, 
								   vwserviceCampaignDetail.getProductId());
				    	
						   modelAndView.setView(new RedirectView(Mt2KSAConstant.getPortalUrl
								   (mt2KSAServiceConfig.getPortalUrl()
									  ,msisdn,subscriberReg.getSubscriberId()))); 
						   return modelAndView;
					}
		    
//				MT2KSAServiceApiTrans mt2KSAServiceApiTrans= mt2KSAServiceApi.subStatus(mt2KSAServiceConfig, msisdn);
//				if(mt2KSAServiceApiTrans.getResponseToCaller()){
//					LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
//							   new Timestamp(System.currentTimeMillis()),
//							   cgToken.getCampaignId()
//							   ,vwserviceCampaignDetail.getServiceId(),
//							   vwserviceCampaignDetail.getProductId());
//							   liveReport.setNoOfDays(mt2KSAServiceConfig.getValidity());	
//							   
//							   subscriberReg= subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
//					   modelAndView.clear();
//					 modelAndView.setView(new RedirectView(Mt2KSAConstant.getPortalUrl
//							   (mt2KSAServiceConfig.getPortalUrl()
//								  ,msisdn,subscriberReg.getSubscriberId()))); 
//					 
//				}
			
					modelAndView.addObject("token",token);
					modelAndView.addObject("msisdn",msisdn);
					modelAndView.addObject("mt2KSAServiceConfig",mt2KSAServiceConfig);
					modelAndView.setViewName("mt2ksa/wap_otp");
					MT2KSAServiceApiTrans mt2KSAServiceApiTrans=mt2KSAServiceApi
							.validateOTP(mt2KSAServiceConfig, msisdn, token,pin);
					
					if(mt2KSAServiceApiTrans.getSuccess()){
						//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
						LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
								   new Timestamp(System.currentTimeMillis()),
								   cgToken.getCampaignId()
								   ,vwserviceCampaignDetail.getServiceId(),
								   vwserviceCampaignDetail.getProductId());
								   liveReport.setNoOfDays(mt2KSAServiceConfig.getValidity());						   
								   SubscriberReg subscriberReg=  subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
						   modelAndView.addObject("portalurl",Mt2KSAConstant.getPortalUrl
								   (mt2KSAServiceConfig.getPortalUrl()
											  ,msisdn,subscriberReg.getSubscriberId()));
						   modelAndView.clear();
						   modelAndView.setView(new RedirectView(Mt2KSAConstant.getPortalUrl
								   (mt2KSAServiceConfig.getPortalUrl()
											  ,msisdn,subscriberReg.getSubscriberId())));
						   
						   //modelAndView.setViewName("mt2ksa/final");
						   redisCacheService
						   .putObjectCacheValueByEvictionDay(Mt2KSAConstant
								   .MT2_KSA_MSISDN_TOKEN_CACHE_PREFIX+msisdn, token, 2);
						 
					}else{
						modelAndView.addObject("otpinfo","Please enter valid pin");
					}
			}catch(Exception  ex){
				logger.error("Exception" ,ex);
			}
			return modelAndView;
		
	}
	
	@RequestMapping(value={"test/pinvalidation"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String testing(HttpServletRequest request,ModelAndView modelAndView){
		MT2KSAServiceApiTrans mt2KSAServiceApiTrans=jpaMT2KSAServiceApiTrans
				.findEnableMT2KSAServiceApiTrans(MUtility.toInt(request.getParameter("id")
				, 0));
	  jmsMt2KSAService.processPinValidation(mt2KSAServiceApiTrans);
		return "ok";
		 }
	
	
	
	@RequestMapping(value="/v2/cgcallback",method = RequestMethod.GET)
	public ModelAndView cgCallbackSdp(HttpServletRequest request,ModelAndView modelAndView){
		//token=collectcent&trxid=aaa2086a11f048adb81cabd162e2f4d2
		Mt2KSACGCallbackSdp mt2ksacgCallback = new Mt2KSACGCallbackSdp(true);
		Mt2KSAServiceConfig mt2KSAServiceConfig=null;
		CGToken cgToken=null;
		try {
			mt2ksacgCallback.setToken(request.getParameter("token"));
			mt2ksacgCallback.setTrackingId(request.getParameter("trxid"));
			mt2ksacgCallback.setQueryStr(request.getQueryString());
			cgToken = new CGToken(mt2ksacgCallback.getToken());
			VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			mt2KSAServiceConfig = Mt2KSAConstant.mapServiceIdToMt2KSAServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			if(Objects.nonNull(mt2KSAServiceConfig)) {
				modelAndView.addObject("mt2UAEServiceConfig",mt2KSAServiceConfig);
			}
		} catch (Exception e) {
			logger.error("exception cg callback mt2 uae"+e);
		}finally {
			jmsMt2KSAService.savemt2ksacgCallbackSdp(mt2ksacgCallback);
		}
		modelAndView.setViewName("/mt2uae/final");
		return modelAndView;
	}

	@RequestMapping(value = "/v2/tracking", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String tracking(HttpServletRequest request){
		//token=1234&msisdn6789
		Mt2KSATracking mt2ksaTracking = new Mt2KSATracking(true);
		try {	
			mt2ksaTracking.setToken(request.getParameter("token"));
			mt2ksaTracking.setMsisdn(request.getParameter("msisdn"));
			mt2ksaTracking.setQueryStr(request.getQueryString());
		} catch (Exception e) {
			logger.error("exception notification mt2 uae"+e);
		}finally {
			jmsMt2KSAService.saveMt2KSATrakingSdp(mt2ksaTracking);
		}
		return "OK";
	}

	@RequestMapping(value = "/v2/notification", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Map<String, String> notificationSdp(HttpServletRequest request){
		//Id=20122212303719741&Data=S,392,87dba3d7-fba1-40f2-9f6b-b9dacb374754&TrackingId=aaa2086a11f048adb81cabd162e2f4d2
		//&MSISDN=971523051932&ShortCode=9132&Date=20202212&Operator=Du&ValidityDays=0
		Mt2KSANotificationSdp mt2KSANotification = new Mt2KSANotificationSdp(true);
		Map<String, String> response = new HashMap<>();
		response.put("ResponseCode", "Success");
		response.put("Message", "Subscription info shared successfully");
		try {	
			mt2KSANotification.setSdpId(request.getParameter("Id"));
			mt2KSANotification.setData(request.getParameter("Data"));
			mt2KSANotification.setTrackingId(request.getParameter("TrackingId"));
			mt2KSANotification.setMsisdn(request.getParameter("MSISDN"));
			mt2KSANotification.setShortCode(request.getParameter("ShortCode"));
			mt2KSANotification.setSdpDate(request.getParameter("Date"));
			mt2KSANotification.setOperator(request.getParameter("Operator"));
			mt2KSANotification.setValidity(request.getParameter("ValidityDays"));
			mt2KSANotification.setQueryStr(request.getQueryString());
		} catch (Exception e) {
			logger.error("exception notification mt2 uae"+e);
		}finally {
			jmsMt2KSAService.saveMt2KSANotificationSdp(mt2KSANotification);
		}
		return response;
	}


	@RequestMapping(value={"/v2/dlr"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Map<String, String> dlrSdp(@RequestBody List<Mt2KSADeliveryNotificationSdp> dlrs){
		Map<String, String> response = new HashMap<>();
		response.put("ResponseCode", "Success");
		response.put("Message", "Subscription info shared successfully");
		try{
			logger.info("dlrs="+dlrs);
		}catch(Exception  ex){logger.error("Exception" ,ex);
		}finally{
			dlrs.parallelStream().forEach(dlr->jmsMt2KSAService.saveMt2KSADlrSdp(dlr));			
		}
		return response;
	}

	//http://192.241.253.234/ccsub/cnt/mt2ksa/check-by-refrenceid?op=du&id=<refrenceid>
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
	//http://192.241.253.234/ccsub/cnt/mt2ksa/check-by-msisdn?op=zani&msisdn=<msisdn>
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
	
	
	
}
