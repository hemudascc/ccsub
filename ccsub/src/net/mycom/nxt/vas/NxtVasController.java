package net.mycom.nxt.vas;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.common.service.IDaoService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.SubscriberReg;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("nxv")
public class NxtVasController {

	private static final Logger logger = Logger
			.getLogger(NxtVasController.class.getName());
	
	
	@Autowired
	private JMSNxtVasService jmsNxtVasService;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Value("${nxt.vas.portal.url}")
	private String portalUrl;
	@Value("${unsub.url}")
	private  String unSubUrl;
	
@RequestMapping(value="cgcallback",method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView cgCallBack(ModelAndView modelAndView,HttpServletRequest request){
	//#http://mob.ccd2c.com/ccsub/cnt/nxv/callback?
	//transaction=2260786&status=1&msisdn=962788396222&subscriber_id=11594&mcc=416&mnc=03&language=1#
	NxtVasCGCallback nxtVasCGCallback=new NxtVasCGCallback(true);
	nxtVasCGCallback.setTransaction(request.getParameter("transaction"));
	nxtVasCGCallback.setNxtVasCgStatus(request.getParameter("status"));
	nxtVasCGCallback.setStatusDesc(
			CGCallbackStatusEnum.getStatusDesc(MUtility.toInt(nxtVasCGCallback.getNxtVasCgStatus(), 0)));
	nxtVasCGCallback.setMsisdn(request.getParameter("msisdn"));
	nxtVasCGCallback.setSubscriberId(request.getParameter("subscriber_id"));
	nxtVasCGCallback.setMcc(request.getParameter("mcc"));
	nxtVasCGCallback.setMnc(request.getParameter("mnc"));
	nxtVasCGCallback.setLanguage(request.getParameter("language"));	
	nxtVasCGCallback.setQueryStr(request.getQueryString());
	daoService.saveObject(nxtVasCGCallback);
	return modelAndView;
}

@RequestMapping(value="whook",method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView nxtVasWebHook(ModelAndView modelAndView,HttpServletRequest request){
	
	NxtWebhookNotification nxtWebhookNotification=new NxtWebhookNotification(true);
	nxtWebhookNotification.setAction(request.getParameter("action"));
	nxtWebhookNotification.setProductId(request.getParameter("product_id"));
	nxtWebhookNotification.setSubscriberId(request.getParameter("subscriber_id"));
	nxtWebhookNotification.setCurrency(request.getParameter("currency"));
	nxtWebhookNotification.setAmountCharge(request.getParameter("amount_charge"));
	nxtWebhookNotification.setPaymentDate(request.getParameter("payment_date"));
	nxtWebhookNotification.setMsisdn(request.getParameter("msisdn"));
	nxtWebhookNotification.setMcc(request.getParameter("mcc"));
	nxtWebhookNotification.setMnc(request.getParameter("mnc"));
	nxtWebhookNotification.setQueryStr(request.getQueryString());
	nxtWebhookNotification.setTransactonId(request.getParameter("transaction"));
	nxtWebhookNotification.setLanguage(request.getParameter("language"));
	nxtWebhookNotification.setSubscriberStatus(request.getParameter("status"));
	jmsNxtVasService.saveNxtWebhookNotification(nxtWebhookNotification);
	return modelAndView;
}

@RequestMapping(value="callback",method={RequestMethod.GET,RequestMethod.POST})
@ResponseBody
public ModelAndView  callback(ModelAndView modelAndView,HttpServletRequest request){
	///ccsub/cnt/nxv/callback?
	//transaction=2260786&status=4&msisdn=962788396222&subscriber_id=11594&mcc=416&mnc=03&language=1
	NxtVasCGCallback nxtVasCGCallback=new NxtVasCGCallback(true);
	try{
	logger.info("callback:: "+request.getQueryString());
	nxtVasCGCallback.setTransaction(request.getParameter("transaction"));
	nxtVasCGCallback.setNxtVasCgStatus(request.getParameter("status"));
	nxtVasCGCallback.setStatusDesc(
			CGCallbackStatusEnum.getStatusDesc(MUtility.toInt(nxtVasCGCallback.getNxtVasCgStatus(), 0)));
	nxtVasCGCallback.setMsisdn(request.getParameter("msisdn"));
	nxtVasCGCallback.setSubscriberId(request.getParameter("subscriber_id"));
	nxtVasCGCallback.setMcc(request.getParameter("mcc"));
	nxtVasCGCallback.setMnc(request.getParameter("mnc"));
	nxtVasCGCallback.setLanguage(request.getParameter("language"));	
	nxtVasCGCallback.setQueryStr(request.getQueryString());
	}catch(Exception ex){
		logger.error("callback:: ",ex);
	}finally{
		daoService.saveObject(nxtVasCGCallback);		
	}
	modelAndView.setView(new RedirectView(portalUrl));
	return modelAndView;  
	
	}
@RequestMapping("unsub")
@ResponseBody
public ModelAndView  unsub(ModelAndView modelAndView,HttpServletRequest request,
		@RequestParam(name="msisdn",required=false)String msisdn){
	  logger.info("unsub:: "+request.getQueryString());
	  List<SubscriberReg> subscriberRegList=null;
	  
		if(msisdn!=null){
		subscriberRegList=jpaSubscriberReg.
				findSubscriberRegByMsisdn(msisdn);
		}		
		logger.info("subscriberRegList::::::::msisdn: "+msisdn+", "+subscriberRegList);
		modelAndView.addObject("subscriberRegList",subscriberRegList);
		modelAndView.addObject("msisdn",msisdn);
		modelAndView.addObject("unSubUrl",unSubUrl);
		modelAndView.setViewName("nxtvas/unsub_msisdn");
	    return modelAndView;
	 
	}




@RequestMapping("support")
@ResponseBody
public String  support(ModelAndView modelAndView,HttpServletRequest request){
	  logger.info("support:: "+request.getQueryString());
	  
	  return "ok";
	}



}


