package net.mycomp.messagecloud.gateway.za;

import javax.servlet.http.HttpServletRequest;

import net.common.jms.JMSService;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;

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
@RequestMapping("mcgza")
public class MCGZAController {

	private static final Logger logger = Logger
			.getLogger(MCGZAController.class.getName());
	
	@Autowired
	private JMSMCGZAService jmsMCGService;
	
	@Autowired
	private JMSService jmsService; 
	
	
	
	@Autowired
	private JMSMCGZAService jmsMCGZAService;
	
	@Autowired
	private MCGZAApiService mcgZAApiService;
	
	@Autowired
	private MCGZAService mcgZAService;
	
	@Value("${messagecloud.za.portal.url}")
	private String portalUrl;
	
	
	
	@RequestMapping("mo")
	@ResponseBody
	public String mo(ModelAndView modelAndView,HttpServletRequest request){
		//action=mpush_ir_message&message_id=1083545875&id=1083545875&billing=MT&
		//country=UK&number=447445566731&network=THREE14UK&shortcode=68899&message=hello+world
		
		MCGZAMoMessage mcgZAMoMessage=new MCGZAMoMessage(true);
		mcgZAMoMessage.setAction(request.getParameter("action"));
		mcgZAMoMessage.setMessageId(request.getParameter("id"));
		mcgZAMoMessage.setNumber(request.getParameter("number"));
		mcgZAMoMessage.setNetwork(request.getParameter("network"));
		mcgZAMoMessage.setMessage(request.getParameter("message"));
		mcgZAMoMessage.setShortcode(request.getParameter("shortcode"));
		mcgZAMoMessage.setCountry(request.getParameter("country"));
		mcgZAMoMessage.setBilling(request.getParameter("billing"));
		mcgZAMoMessage.setQueryStr(request.getQueryString());
		logger.info("mo::::::::: "+mcgZAMoMessage);
		jmsMCGService.saveZAMOMessage(mcgZAMoMessage);
		return "OK";
	}

	@RequestMapping("dlr")
	@ResponseBody
	public String dlr(ModelAndView modelAndView,HttpServletRequest request){
		//action=mpush_ir_message&message_id=1083545875&id=1083545875&billing=MT&
		//country=UK&number=447445566731&network=THREE14UK&shortcode=68899&message=hello+world
		
		MCGZADeliveryReport mcgZADeliveryReport=new MCGZADeliveryReport(true);
		mcgZADeliveryReport.setAction(request.getParameter("action"));
		mcgZADeliveryReport.setMessageId(request.getParameter("id"));
		mcgZADeliveryReport.setNumber(request.getParameter("number"));
		mcgZADeliveryReport.setReport(request.getParameter("report"));
		mcgZADeliveryReport.setReasonid(request.getParameter("reason_id"));
		logger.info("dlr::::::::: "+mcgZADeliveryReport);
		jmsMCGZAService.saveZADeliveryReport(mcgZADeliveryReport);
		return "OK";
	}

	
	@RequestMapping("success/{token}")
	public ModelAndView success(ModelAndView modelAndView,HttpServletRequest request
			,@PathVariable("token")String token){
	    logger.info("success::::::::token::  "+token+" ,query str: "+request.getQueryString());
	    MCGZACallback mczZACallback=new MCGZACallback(true);
	    modelAndView.setView(new RedirectView(portalUrl));
	    try{
	    mczZACallback.setAction("success");
	    mczZACallback.setObsstatus(request.getParameter("status"));
	    mczZACallback.setObsWindowId(request.getParameter("id"));
	    mczZACallback.setToken(token);
	    CGToken cgToken=new CGToken(token); 
	    mczZACallback.setTokenId(cgToken.getTokenId());
	    mczZACallback.setQueryStr(request.getQueryString());
	   
	    }catch(Exception ex){
	    	logger.error("success",ex);
	    }finally{
	    	 jmsService.saveObject(mczZACallback);
	   	  
	    }
		return modelAndView;
	}
	
	
	@RequestMapping("error/{token}")
	@ResponseBody
	public ModelAndView error(ModelAndView modelAndView,HttpServletRequest request,
			@PathVariable("token")String token){
		logger.info("error::::::::token::  "+token+" ,query str: "+request.getQueryString());
		 MCGZACallback mczZACallback=new MCGZACallback(true);
		 modelAndView.setView(new RedirectView(portalUrl));
		 try{
		    mczZACallback.setAction("error");
		    mczZACallback.setObsstatus(request.getParameter("status"));
		    mczZACallback.setObsWindowId(request.getParameter("id"));
		    mczZACallback.setToken(token);
		    CGToken cgToken=new CGToken(token); 
		    mczZACallback.setTokenId(cgToken.getTokenId());
		    mczZACallback.setQueryStr(request.getQueryString());
		  
		 }catch(Exception ex){
			 logger.error("error:: ",ex);
		 }finally{
			 jmsService.saveObject(mczZACallback);
			   
		 }
			return modelAndView;
		}
	
	@RequestMapping("fallback/{token}")
	@ResponseBody
	public ModelAndView fallback(ModelAndView modelAndView,HttpServletRequest request,
			@PathVariable("token")String token){
		
		logger.info("fallback::::::::token::  "+token+" ,query str: "+request.getQueryString());
		modelAndView.setViewName("mcgza/fallback");	
		MCGZACallback mczZACallback=new MCGZACallback(true);
	    try{	      
		mczZACallback.setAction("fallback");
	    mczZACallback.setObsstatus(request.getParameter("status"));
	    mczZACallback.setObsWindowId(request.getParameter("id"));
	    mczZACallback.setToken(token);
	    CGToken cgToken=new CGToken(token); 
	    mczZACallback.setTokenId(cgToken.getTokenId());
	    mczZACallback.setQueryStr(request.getQueryString());	   
	    modelAndView.addObject("token",token);		
	    }catch(Exception ex){
	    	logger.error("fallback:: ",ex);
	    }finally{
	    	 jmsService.saveObject(mczZACallback);
	    }
	    
		return modelAndView;
	}
	
	
	
	@RequestMapping("fallbackrecover")
	@ResponseBody
	public ModelAndView fallbackrecover(ModelAndView modelAndView,HttpServletRequest request
			){
		
		logger.info("fallback::::::::query str: "+request.getQueryString());	
	    try{	      
				
	    }catch(Exception ex){
	    	logger.error("fallback:: ",ex);
	    }finally{
	    	
	    }	    
		return modelAndView;
	}
	
	@RequestMapping("tocg")
	public ModelAndView toCG(ModelAndView modelAndView,HttpServletRequest request){
		
		String token=request.getParameter("token");
		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		MCGZAServiceConfig mcgZAServiceConfig=MCGZAConstant.
				mapServiceIdToMCGZAServiceConfig.get(vwServiceCampaignDetail.getServiceId());		
		modelAndView.addObject("mcgZAServiceConfig",mcgZAServiceConfig);
		modelAndView.addObject("token",token);
		String msisdn=request.getParameter("msisdn");
		if(msisdn==null){
			msisdn="";
		}
		
		MCGZAOBSWindowTrans mcgZAOBSWindowTrans=mcgZAApiService.
				sendOBSWindowRequest(msisdn
				, mcgZAServiceConfig, 
				token,
				cgToken.getTokenId());
		
		logger.info("mcgZAOBSWindowTrans :: "+mcgZAOBSWindowTrans);
		if(mcgZAOBSWindowTrans.getResponseStatus()!=null&&mcgZAOBSWindowTrans.getResponseStatus()
				.equalsIgnoreCase("OK")){
			modelAndView.setView(new RedirectView(mcgZAOBSWindowTrans.getResponseRedirectUrl()));
		}else{
			modelAndView.setView(new RedirectView(portalUrl));
		}
		return modelAndView;
		
	}
	
@RequestMapping("termscond")	
public ModelAndView termscond(ModelAndView modelAndView,HttpServletRequest request){	
	   
	    String token=request.getParameter("token");
	    modelAndView.addObject("token",token);
		modelAndView.setViewName("mcgza/termscond");	
		return modelAndView;
	}



@RequestMapping("alreadysub")	
public ModelAndView alreadysub(ModelAndView modelAndView,HttpServletRequest request){		
	    String token=request.getParameter("token");
		modelAndView.addObject("token",token);
		modelAndView.setViewName("mcgza/already_sub");	
		return modelAndView;
	}

@RequestMapping("checksub")	
public ModelAndView checkSub(ModelAndView modelAndView,HttpServletRequest request){	
	   
	boolean sub=false;
	 try{
	    String token=request.getParameter("token");
	    modelAndView.addObject("token",token);
	    String msisdn=request.getParameter("msisdn");	    
	    
	    if(token!=null){
	    CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		 sub= mcgZAService.checkSub(vwServiceCampaignDetail.getProductId(), null, msisdn);
	    }else{
	    	sub= mcgZAService.checkSub(MCGZAConstant.PRODUCT_ID, null, msisdn);	
	    }
	    
		if(sub){
	    	modelAndView.setView(new RedirectView(portalUrl+"?msisdn="+msisdn));	
	    }else{
	    	modelAndView.addObject("token",token);
	    	modelAndView.setViewName("mcgza/lp");	
	    }
	 }catch(Exception ex){
		 
	 }
		
		return modelAndView;
	}
	
}
