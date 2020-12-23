package net.mycomp.common.inapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import net.common.jms.JMSService;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.VWServiceCampaignDetail;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("com")
public class InappController {

	
	 private static final Logger logger = Logger
				.getLogger(InappController.class.getName());
	 
	 @Autowired
	 private JMSService jmsService;
	 
	@Autowired
	private InappPublisherService inappPublisherService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private InappRequestFactory inappRequestFactory;
	
	@Autowired
	private JMSInappService jmsInappService;
	
	@RequestMapping("pin/send")
	@ResponseBody
	public String sendPin(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
		inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.SEND_PIN.action);
		inappProcessRequest.setPinRequestCount(1);
		inappPublisherService.sendOtp(inappProcessRequest,modelAndView);
		
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}finally{
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return inappProcessRequest.getResponseObject().toString();
	} 
	
	
	@RequestMapping("pin/validation")
	@ResponseBody
	public String validatePin(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
			inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.PIN_VALIDATION.action);
			inappProcessRequest.setPinValidationRequestCount(1);
			inappPublisherService.otpValidation(inappProcessRequest, modelAndView);
		
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}finally{	
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return inappProcessRequest.getResponseObject().toString();
		
	} 
	
	@RequestMapping("pin/checksub")
	@ResponseBody
	public String statusCheck(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
		inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.STATUS_CHECK.action);
		inappProcessRequest.setStatusCheckCount(1);
		inappPublisherService.statusCheck(inappProcessRequest, modelAndView);
		
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}finally{	
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return inappProcessRequest.getResponseObject().toString();
	} 
	
	
	@RequestMapping("portal/url")
	//@ResponseBody
	public ModelAndView portalUrl(HttpServletRequest request,ModelAndView modelAndView){
		
		InappProcessRequest inappProcessRequest=null;
		try{
		
		inappProcessRequest=inappRequestFactory.createRequestBean(request,InappAction.PORTAL_URL.action);
		
		inappPublisherService.portalUrl(inappProcessRequest,modelAndView);
		
		}catch(Exception ex){
			logger.error("sendPin ",ex);
		}finally{
			jmsInappService.saveInappProcessRequest(inappProcessRequest);
			//daoService.updateObject(inappProcessRequest);
		}		
		return modelAndView;
	} 
	
	
}
