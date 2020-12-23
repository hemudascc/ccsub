package net.mycomp.comviva.ooredo.oman;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import net.common.jms.JMSService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("ooman")
public class OoredooOmanController {

	
	private static final Logger logger = Logger
			.getLogger(OoredooOmanController.class.getName());
	
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private OoredooOmanServiceApi ooredooOmanServiceApi;
	
	@Autowired
	private JMSOoredooOmanService jmsOoredooOmanService;
	
	@Autowired
	private JMSService jmsService; 
	
	@RequestMapping(value={"notification"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String notification(HttpServletRequest request,ModelAndView modelAndView){
		logger.info("cgNotificaton::::IP:::::::: "+request.getRemoteAddr());
		if(request.getRemoteAddr()!=null && request.getRemoteAddr().equals("52.29.118.72")) {
		OoredooOmanNotification ooredooOmanNotification=new OoredooOmanNotification(true);
		try{
			logger.info("notification:: "+request.getQueryString());
			
			
			//http://IP:PORT/<CP_Context>?serviceId=1234&appliedPlan=9898
			//&sequenceNo=123432939&operationId=SN&bearerId=WAP&validityDays=1&chargeAmount=5&callingParty=968XXXXXXXXX
			ooredooOmanNotification.setAppliedPlan(request.getParameter("appliedPlan"));
			ooredooOmanNotification.setServiceId(request.getParameter("serviceId"));
			ooredooOmanNotification.setSequenceNo(request.getParameter("sequenceNo"));
			ooredooOmanNotification.setOperationId(request.getParameter("operationId"));
			ooredooOmanNotification.setBearerId(request.getParameter("bearerId"));
			ooredooOmanNotification.setValidityDays(MUtility.toInt(request.getParameter("validityDays"),0));
			ooredooOmanNotification.setChargeAmount(MUtility.toDouble(request.getParameter("chargeAmount"),0));
			ooredooOmanNotification.setMsisdn(OoredooOmanConstant.formatMsisdn(
					request.getParameter("callingParty")));
			ooredooOmanNotification.setQueryStr(request.getQueryString());
		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}finally{
			jmsOoredooOmanService.saveOoredooOmanNotification(ooredooOmanNotification);
		}
		}
		return "OK";
	}
	
	@RequestMapping(value={"he/callback/{cgtoken}"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView heCallback(HttpServletRequest request,ModelAndView modelAndView,
			@PathVariable(value="cgtoken")String cgtoken,
			
			@RequestParam(name="token",required=false,defaultValue="") String encyptedMsisdn
			){
		
		OoredooOmanHeCallbackTrans ooredooOmanHeCallbackTrans=new OoredooOmanHeCallbackTrans(true);
		try{
			ooredooOmanHeCallbackTrans.setQueryStr(request.getQueryString());
			ooredooOmanHeCallbackTrans.setCorrelatorId(request.getParameter("correlatorId"));
			ooredooOmanHeCallbackTrans.setToken(cgtoken);
		
			ooredooOmanHeCallbackTrans.setEncyptMsisdn(Objects.toString(encyptedMsisdn).trim().replaceAll(" ", "+"));
			
			ooredooOmanHeCallbackTrans.setStatusCode(request.getParameter("statusCode"));
			ooredooOmanHeCallbackTrans.setMsisdn(OoredooOmanConstant.formatMsisdn(ooredooOmanServiceApi
					.decrypt(ooredooOmanHeCallbackTrans.getEncyptMsisdn()
					)));
			
		CGToken cgToken=new CGToken(cgtoken); 
		VWServiceCampaignDetail vwServiceCampaignDetail=
				MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		OoredooOmanServiceConfig ooreodoOmanServiceConfig=
				OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig
				.get(vwServiceCampaignDetail.getServiceId());
		modelAndView.addObject("token", cgtoken);
		
		modelAndView.addObject("ooreodoOmanServiceConfig", ooreodoOmanServiceConfig);
		modelAndView.setViewName("ooredoooman/msisdn_missing");
		if((ooredooOmanHeCallbackTrans.getStatusCode().equalsIgnoreCase("1")||
				ooredooOmanHeCallbackTrans.getStatusCode().equalsIgnoreCase("-2"))
				&&ooredooOmanHeCallbackTrans.getMsisdn()!=null){
			modelAndView.addObject("msisdn", ooredooOmanHeCallbackTrans.getMsisdn());
			modelAndView.setViewName("ooredoooman/msisdn_lp");
		}else{
			//modelAndView.setViewName("ooredoooman/error");
		}
		
	}catch(Exception ex){
		logger.info("heCallback::: ",ex);
	}finally{
		jmsService.saveObject(ooredooOmanHeCallbackTrans);
	}
		return modelAndView;
	}
	
	
	@RequestMapping(value={"web/send/otp"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView webSendOTP(HttpServletRequest request,ModelAndView modelAndView){
		
       try{
    	   modelAndView.setViewName("ooredoooman/msisdn_missing");
    	    
    	   logger.info("webSendOTP:::::::: 11 msisdn:: "+request.getParameter("msisdn"));
			String msisdn=OoredooOmanConstant.formatMsisdn(request.getParameter("msisdn"));
			String token=request.getParameter("token");	
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token);
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwServiceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			
		
			OoredooOmanServiceConfig ooreodoOmanServiceConfig=
					OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig
					.get(vwServiceCampaignDetail.getServiceId());		
			
	    
	    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwServiceCampaignDetail.getProductId());
	    	if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			   modelAndView.setView(new RedirectView(OoredooOmanConstant.getPortalUrl
					   (ooreodoOmanServiceConfig.getPortalUrl()
						  ,msisdn,subscriberReg.getSubscriberId()))); 
			   return modelAndView;
		}
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("ooreodoOmanServiceConfig",ooreodoOmanServiceConfig);
				
				OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=ooredooOmanServiceApi
						.sendPinApi(msisdn, token, ooreodoOmanServiceConfig);

				if(ooredooOmanOCSLogDetail.getSuccess()){
					modelAndView.setViewName("ooredoooman/pin_validate");
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
	public ModelAndView sendOTPValidation(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){
		 try{
			    modelAndView.setViewName("ooredoooman/pin_validate");
			    modelAndView.addObject("l",lang);
				String msisdn=OoredooOmanConstant.formatMsisdn(request.getParameter("msisdn"));
				String token=request.getParameter("token");
				String pin=request.getParameter("pin"); 
				String ip=request.getRemoteAddr();
				logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token+", pin:: "+pin);
				CGToken cgToken=new CGToken(token);
				VWServiceCampaignDetail vwServiceCampaignDetail=MData
						.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
				
				OoredooOmanServiceConfig ooreodoOmanServiceConfig=
						OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig
						.get(vwServiceCampaignDetail.getServiceId());	
				    	SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
								msisdn, 
								vwServiceCampaignDetail.getProductId());
				    	if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
							   modelAndView.setView(new RedirectView(OoredooOmanConstant.getPortalUrl
									   (ooreodoOmanServiceConfig.getPortalUrl()
										  ,msisdn,subscriberReg.getSubscriberId()))); 
							   return modelAndView;
						}
						  
		    

				    	modelAndView.addObject("token",token);
						modelAndView.addObject("msisdn",msisdn);
						modelAndView.addObject("ooreodoOmanServiceConfig",ooreodoOmanServiceConfig);
						
						OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=ooredooOmanServiceApi
								.pinValidation(msisdn, token, ooreodoOmanServiceConfig, pin);
					if(ooredooOmanOCSLogDetail.getSuccess()){
						//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
						LiveReport liveReport=new LiveReport(vwServiceCampaignDetail.getOpId(),
								   new Timestamp(System.currentTimeMillis()),
								   cgToken.getCampaignId()
								   ,vwServiceCampaignDetail.getServiceId(),
								   vwServiceCampaignDetail.getProductId());
								   liveReport.setNoOfDays(ooreodoOmanServiceConfig.getValidity());						   
								   subscriberReg=  subscriberRegService
										   .findOrCreateSubscriberByAct(msisdn,null, liveReport);
						   
						   modelAndView.clear();
						   modelAndView.setView(new RedirectView(OoredooOmanConstant.getPortalUrl
								   (ooreodoOmanServiceConfig.getPortalUrl()
									  ,msisdn,subscriberReg.getSubscriberId()))); 
						
					}else{
						modelAndView.addObject("otpinfo","Please enter valid pin");
					}
			}catch(Exception  ex){
				logger.error("Exception" ,ex);
			}
			return modelAndView;
		
	}
	
	@RequestMapping(value={"test/unsub"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public OoredooOmanOCSLogDetail testUnsub(HttpServletRequest request,ModelAndView modelAndView){
		OoredooOmanServiceConfig ooreodoOmanServiceConfig=
				OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig
				.get(91);
		OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=ooredooOmanServiceApi
				.unsubSubscribe(request.getParameter("msisdn"), ""+System.currentTimeMillis(), ooreodoOmanServiceConfig);
		return ooredooOmanOCSLogDetail;
	}
	
	@RequestMapping(value={"test/mt"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public OoredooOmanOCSLogDetail testMt(HttpServletRequest request,ModelAndView modelAndView){
		OoredooOmanServiceConfig ooreodoOmanServiceConfig=
				OoredooOmanConstant.mapCCServiceIdToOoreodoOmanServiceConfig
				.get(91);
		OoredooOmanOCSLogDetail ooredooOmanOCSLogDetail=ooredooOmanServiceApi
				.sendMT(request.getParameter("msisdn"), ""+System.currentTimeMillis(), 
						ooreodoOmanServiceConfig,
						"Access Gamezine App with exclusive content here http://game-zine.com/?bp=omanord (1250bz/week) . To unsubscribe send UNSUB GA to 91186");
		return ooredooOmanOCSLogDetail;
	}
	
}
