package net.mycomp.du;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import net.common.jms.JMSService;
import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.persist.bean.ErrorInfo;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller(value="duController")

public class DUController {

	private static  Logger logger = Logger.getLogger(DUController.class);

	@Autowired
	@Qualifier("daoService")	
	private IDaoService daoService;
	
	@Autowired
	@Qualifier("jmsDuService")
	private JMSDUService jmsDuService;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	private final String defaultPortalUrl;
	@Autowired
	public DUController(@Value("${du.default.portal.url}")String defaultPortalUrl){
		this.defaultPortalUrl= defaultPortalUrl;
		
	}
	@PostConstruct	
	public void init(){
	
	 }
	
	@RequestMapping("/du/charging/notification")
	@ResponseBody
	public String chargingNotification(HttpServletRequest request) {
		DUChargingNotification duChargingNotification=null;
		try{
		logger.info("chargingNotification:: "+request.getQueryString());
		 duChargingNotification=new DUChargingNotification();
		duChargingNotification.setCallingParty(request.getParameter("callingParty"));
		duChargingNotification.setServiceId(request.getParameter("serviceId"));
		duChargingNotification.setServiceType(request.getParameter("serviceType"));
		duChargingNotification.setRequestPlan(request.getParameter("requestPlan"));
		duChargingNotification.setSequenceNo(request.getParameter("sequenceNo"));
		duChargingNotification.setChargeAmount(MUtility.toDouble(request.getParameter("chargeAmount"),0));
		duChargingNotification.setAppliedPlan(request.getParameter("appliedPlan"));
		duChargingNotification.setDiscountPlan(request.getParameter("discountPlan"));
		duChargingNotification.setValidityDays(request.getParameter("validityDays"));
		duChargingNotification.setOperationId(request.getParameter("operationId"));
		duChargingNotification.setBearerId(request.getParameter("bearerId"));
		duChargingNotification.setErrorCode(request.getParameter("errorCode"));
		duChargingNotification.setResult(request.getParameter("result"));
		duChargingNotification.setContentId(request.getParameter("contentId"));
		duChargingNotification.setCategory(request.getParameter("category"));
		duChargingNotification.setOptParam1(request.getParameter("optParam1"));
		duChargingNotification.setOptParam2(request.getParameter("optParam2"));
		duChargingNotification.setOptParam3(request.getParameter("optParam3"));
		duChargingNotification.setQueryStr(request.getQueryString());
		duChargingNotification.setCreateTime(new Timestamp(System.currentTimeMillis()));
		duChargingNotification.setStatus(true);
		duChargingNotification.setAction(DUConstant.
				getSubscriberStats(duChargingNotification.getOperationId()));
		}catch(Exception ex){
			logger.error("chargingNotification::::::: ",ex);
		}finally{
		jmsDuService.saveDUChargingCallbackJMSTemplate(duChargingNotification);		
		}
		return "OK";
	}
	
	
	@RequestMapping(value={"/du/charging/callback","/du/cg/callback"})
	//@ResponseBody
	public ModelAndView chargingCallback(HttpServletRequest request,ModelAndView modelAndView) {
		DUCGCallback duCGCallback=null;
		try{
		//http://<IP>:<PORT>/CallBack/CallBackCCG?MSISDN=xxxxxxxxxx&Result=SUCCESS&
		//Reason=Universal_Success&productId=BUDDYCHAT1&transID=150925173523173&
		//TPCGID=xxxxxxxx3741035871&Songname=BIR
		//MSISDN=526356658&Result=SUCCESS&Reason=Success_and_accepted_by_user
		//&productId=5856&transID=1524168517816-108603651-1&TPCGID=180420140158025455&Songname=null

		 duCGCallback=new DUCGCallback();
		duCGCallback.setMsisdn(request.getParameter("MSISDN"));
		if(duCGCallback.getMsisdn()!=null&&!duCGCallback.getMsisdn().startsWith("971")){
			duCGCallback.setMsisdn("971"+duCGCallback.getMsisdn());
		}
		duCGCallback.setResult(request.getParameter("Result"));
		duCGCallback.setReason(request.getParameter("Reason"));
		duCGCallback.setProductId(request.getParameter("productId"));
		duCGCallback.setTransId(request.getParameter("transID"));
		duCGCallback.setTpcgId(request.getParameter("TPCGID"));
		duCGCallback.setSongName(request.getParameter("Songname"));
		duCGCallback.setQueryStr(request.getQueryString());
		duCGCallback.setCreateDate(new Timestamp(System.currentTimeMillis()));
		duCGCallback.setStatus(true);
		CGToken cgToken=new CGToken(duCGCallback.getTransId()); 
		VWServiceCampaignDetail vwServiceCampaignDetail=MData.
				mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		
		DUConfig duConfig=DUConstant.mapServiceIdToDuConfig.
				get(vwServiceCampaignDetail.getServiceId());
		
		redisCacheService.putIntValue(duCGCallback.getClass().getName()+duCGCallback.getTpcgId(),0);
		
		if(duConfig!=null&&duCGCallback.getResult()!=null&&
				duCGCallback.getResult().equalsIgnoreCase(DUConstant.SUCCESS_CG_CALLBACK)){
			String portalUrl=duConfig.getPortalURL()+"?sub=1&msisdn="+duCGCallback.getMsisdn();
			request.getSession().setMaxInactiveInterval(12000);
			request.getSession().setAttribute("msisdn", duCGCallback.getMsisdn());	
			modelAndView.setView(new RedirectView(portalUrl));
		}else if(duConfig!=null&&duCGCallback.getReason()!=null
				&&duCGCallback.getReason().contains(DUConstant.CG_CALLBACK_REASON_ALREADY_SUBSCRIBED)){
			String portalUrl=duConfig.getPortalURL()+"?sub=1&msisdn="+duCGCallback.getMsisdn();
			request.getSession().setMaxInactiveInterval(12000);
			request.getSession().setAttribute("msisdn", duCGCallback.getMsisdn());	
			modelAndView.setView(new RedirectView(portalUrl));
		}else if(duConfig!=null){
			String portalUrl=duConfig.getPortalURL()+"?sub=0";
			modelAndView.setView(new RedirectView(portalUrl));
		}else{
			modelAndView.setView(new RedirectView(defaultPortalUrl));
		}
		logger.info("chargingCallback:: "+request.getQueryString());
		}catch(Exception ex){
			logger.error("chargingCallback query string "+request.getQueryString() ,ex);
			modelAndView.setView(new RedirectView(defaultPortalUrl));
		}finally{
			jmsService.saveObject(duCGCallback);
		}
		return modelAndView;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ModelAndView error(HttpServletRequest request,ModelAndView modelAndView,Exception ex){
		logger.error("error:: query  string: "+request.getQueryString()+", Exception:: ", ex);
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
		errorInfo.setQueryStr(this.getClass().getName()+", query str ="+request.getQueryString());
		errorInfo.setErrorDesc(ex.toString());
		daoService.saveObject(errorInfo);
		modelAndView.setView(new RedirectView(defaultPortalUrl));
		return modelAndView;
	}
}
