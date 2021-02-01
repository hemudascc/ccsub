package net.mycomp.onmobile;

import javax.servlet.http.HttpServletRequest;

import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("onmobile")
public class OnMobileController {

	private static final Logger logger = Logger
			.getLogger(OnMobileController.class.getName());
	
	
	@Autowired
	private JMSOnMobileService jmsOnMobileService;
	
	@RequestMapping("cgcallback")	
	public ModelAndView callback(HttpServletRequest request,ModelAndView modelAndView){
	    logger.info("callback query string  "+request.getQueryString());
	    OnMobileCGCallback onMobileCGCallback=new OnMobileCGCallback(true);
	    
	   try{
		   onMobileCGCallback.setMsisdn(OnMobileConstant.decrypt(request.getParameter("u")));
		   onMobileCGCallback.setOnMobileStatus(request.getParameter("status"));
		   
		   
		   CGToken cgToken=new CGToken(onMobileCGCallback.getToken());
		   VWServiceCampaignDetail vwServiceCampaignDetail=MData
				   .mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		   OnMobileServiceConfig onMobileServiceConfig=
				   OnMobileConstant.mapServiceIdToOnMobileServiceConfig.get(vwServiceCampaignDetail.getServiceId());
		   modelAndView.setView(new RedirectView(onMobileServiceConfig.getPortalUrl()));
	    }catch(Exception ex){
	    	logger.error("Exception ",ex);
	    }finally{
	    	jmsOnMobileService.saveCGCallback(onMobileCGCallback);
	    }
	    return modelAndView;
	}
	
	@RequestMapping("notification")	
	@ResponseBody
	public String notification(HttpServletRequest request,ModelAndView modelAndView){
	    logger.info("notification query string  "+request.getQueryString());
	    OnMobileChargingNotification onMobileChargingNotification=new OnMobileChargingNotification(true);
	    try{
	    	onMobileChargingNotification.setMsisdn(request.getParameter("msisdn"));
	    	onMobileChargingNotification.setSrvKey(request.getParameter("srvkey"));
	    	onMobileChargingNotification.setChargingStatus(request.getParameter("status"));
	    	onMobileChargingNotification.setType(request.getParameter("type"));
	    	onMobileChargingNotification.setRefId(request.getParameter("refid"));
	    	onMobileChargingNotification.setAction(request.getParameter("action"));
	    	onMobileChargingNotification.setMode(request.getParameter("mode"));
	    	onMobileChargingNotification.setQueryStr(request.getQueryString());
	    }catch(Exception ex){
	    	logger.error("Exception ",ex);
	    }finally{
	    	jmsOnMobileService.saveOnMobileChargingNotification(onMobileChargingNotification);
	    }
	    return "SUCCESS";
	}
	
}
