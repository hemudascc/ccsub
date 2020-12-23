package net.mycomp.ksa;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
@Controller
@RequestMapping("ksa")
public class KsaController {

	private static final Logger logger = Logger
			.getLogger(KsaController.class.getName());
	
	
	@Autowired
	private KsaServiceApi ksaServiceApi;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private KsaScheduler ksaScheduler;
	
	@Autowired
	private JMSKSAService jmsKSAService;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	
	@RequestMapping("notification")
	@ResponseBody
	public String notification(HttpServletRequest request,ModelAndView modelAndView){
		//http://IP:PORT/<SP Context>?callingParty=<MSISDN>&maServiceId=xyz&bearerId=1
//		&operationId=SN&appliedPlan=ABC&price=100&validity=30&spTransactionId=178687987989
		KsaNotification ksaNotification=new KsaNotification(true);
		try{
			ksaNotification.setCallingParty(request.getParameter("callingParty"));
			ksaNotification.setMaServiceId(request.getParameter("maServiceId"));
			ksaNotification.setBearerId(request.getParameter("bearerId"));
			ksaNotification.setOperationId(request.getParameter("operationId"));
			ksaNotification.setAppliedPlan(request.getParameter("appliedPlan"));
			ksaNotification.setPrice(request.getParameter("price"));
			ksaNotification.setValidity(request.getParameter("validity"));
			ksaNotification.setSpTransactionId(request.getParameter("spTransactionId"));
			ksaNotification.setQueryStr(request.getQueryString());
			
			logger.info("KSA notification:: "+request.getQueryString());
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			jmsKSAService.saveKsaNotification(ksaNotification);
		}
		return "OK";
	}
	
	@RequestMapping("send/otp")
	public ModelAndView sendOTP(HttpServletRequest request,ModelAndView modelAndView){
		
       try{
    	   
			String msisdn=KsaConstant.formatMsisdn(request.getParameter("msisdn"));
			String token=request.getParameter("token");
			  
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			KsaServiceConfig ksaServiceConfig=
					KsaConstant.mapServiceIdToKsaServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			
		if(KsaConstant.isValidMsisdn(msisdn)&&ksaServiceApi.checkProfile(msisdn, ksaServiceConfig,
				token)){
				SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
						msisdn, 
						   vwserviceCampaignDetail.getProductId());
				   if(subscriberReg==null||subscriberReg.getStatus()!=MConstants.SUBSCRIBED){
					   LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
							   new Timestamp(System.currentTimeMillis()),
							   cgToken.getCampaignId()
							   ,vwserviceCampaignDetail.getServiceId(),
							   vwserviceCampaignDetail.getProductId());
							   liveReport.setNoOfDays(ksaServiceConfig.getValidity());						   
					   subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);					   
				   }
				   modelAndView.setView(new RedirectView(KsaConstant.getPortalUrl
						   (ksaServiceConfig.getPortalUrl()
							  ,msisdn))); 
			}else{
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("ksaServiceConfig",ksaServiceConfig);
				modelAndView.setViewName("ksa/msisdn_missing");
				
				if(KsaConstant.isValidMsisdn(msisdn)&&ksaServiceApi.sendSubscriptionPinRequest(msisdn, ksaServiceConfig,token)){
					modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
				}else{
					modelAndView.addObject("otpinfo","Please enter valid mobile number");
				}
				
			}
			
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		
		return modelAndView;
	}
	
	@RequestMapping("send/otp/validation")
	public ModelAndView sendOTPValidation(HttpServletRequest request,ModelAndView modelAndView){
		 try{
				
				String msisdn=KsaConstant.formatMsisdn(request.getParameter("msisdn"));
				String token=request.getParameter("token");
				String pin=request.getParameter("pin"); 
				CGToken cgToken=new CGToken(token);
				VWServiceCampaignDetail vwserviceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
				KsaServiceConfig ksaServiceConfig=
						KsaConstant.mapServiceIdToKsaServiceConfig.get(vwserviceCampaignDetail.getServiceId());
				
			if(KsaConstant.isValidMsisdn(msisdn)&&ksaServiceApi.checkProfile(msisdn, ksaServiceConfig,token)){
					SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
							msisdn, 
							   vwserviceCampaignDetail.getProductId());
					   if(subscriberReg==null||subscriberReg.getStatus()!=MConstants.SUBSCRIBED){
						   LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
								   new Timestamp(System.currentTimeMillis()),
								   cgToken.getCampaignId()
								   ,vwserviceCampaignDetail.getServiceId(),
								   vwserviceCampaignDetail.getProductId());
						   liveReport.setNoOfDays(ksaServiceConfig.getValidity());						   
						   subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);					   
					   }
					   modelAndView.setView(new RedirectView(KsaConstant.getPortalUrl
							   (ksaServiceConfig.getPortalUrl()
								  ,msisdn))); 
				}else{
					modelAndView.setViewName("ksa/msisdn_missing");
					modelAndView.addObject("token",token);
					modelAndView.addObject("msisdn",msisdn);
					modelAndView.addObject("ksaServiceConfig",ksaServiceConfig);
					if(KsaConstant.isValidMsisdn(msisdn)&&ksaServiceApi.sendSubscriptionPinValidationRequest(msisdn, ksaServiceConfig,pin,token)){
						
						LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
								   new Timestamp(System.currentTimeMillis()),
								   cgToken.getCampaignId()
								   ,vwserviceCampaignDetail.getServiceId(),
								   vwserviceCampaignDetail.getProductId());
								   liveReport.setNoOfDays(ksaServiceConfig.getValidity());						   
						   subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
						   
						   ksaServiceApi.sendSms(msisdn, ksaServiceConfig,
								   ksaServiceConfig.getSubscriptionMsgTemplate(),KsaConstant.ACTIVATION_SMS_PUSH); 
						modelAndView.setView(new RedirectView(ksaServiceConfig.getPortalUrl()
									  +"?msisdn="+msisdn)); 
					}else{
						modelAndView.addObject("otpinfo","Please enter valid pin");
					}
					
				}
				
			}catch(Exception  ex){
				logger.error("Exception" ,ex);
			}
			
			return modelAndView;
		
	}
	
	
	@RequestMapping("send/renewal")
	@ResponseBody
	public String sendRenewal(HttpServletRequest request){
		ksaScheduler.renewalForLastChargingSuccessWithinNoDays();
		return "ok";
	}
	
}
