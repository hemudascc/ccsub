package net.mycomp.beecel.jordon;

import java.sql.Timestamp;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.jms.JMSService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.mycomp.mcarokiosk.hongkong.MKHongkongConstant;
import net.persist.bean.LiveReport;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;


@Controller
@RequestMapping("beecell")
public class BCJordonController {
	
	private static final Logger logger = Logger
			.getLogger(BCJordonController.class.getName());
	

	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private JMSJordonService jmsjordonService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@RequestMapping("tocg")
	public ModelAndView toCG(ModelAndView modelAndView,HttpServletRequest  request){
		

		String token=request.getParameter("token");
		String tokenToCg=request.getParameter("tokenToCg");
		BCJordonCGToken bcJordonCGToken=new BCJordonCGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(bcJordonCGToken.getCampaignId());
		logger.info("token: "+token+" tokenToCg "+tokenToCg);
	BCJordonConfig bcJordonConfig=BCJordonConstant
			.mapServiceIdToBCJordonConfig   
			.get(vwServiceCampaignDetail.getServiceId());
		String url=BCJordonConstant.CG_URL.replaceAll("<cid>", tokenToCg).replaceAll("<t>",bcJordonConfig.getT() ).replaceAll("<pub>",bcJordonConfig.getPub());	
		logger.info("toMo:: url: "+url);
		modelAndView.setView(new RedirectView(url));
		return modelAndView;	
	}  

	@RequestMapping("cgcallback")
	public ModelAndView cgcallback(ModelAndView modelAndView,HttpServletRequest  request){
		logger.info("cgCallBackUrl:::::::::: "+request.getQueryString());
		String portalUrl=null;
		BCJordonCGCallback bcJordonCGCallback=new BCJordonCGCallback();
		try{
			
			BCJordonConfig bcJordonServiceConfig=null;
			bcJordonCGCallback.setQueryStr(request.getQueryString());
			bcJordonCGCallback.setMsisdn(request.getParameter("msisdn"));			
			bcJordonCGCallback.setTid(request.getParameter("cid"));		 
			bcJordonCGCallback.setCid(request.getParameter("cid"));	
			CGToken cgToken=new CGToken(bcJordonCGCallback.getTid()); 
			
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			if(vwServiceCampaignDetail!=null){
				 bcJordonServiceConfig=BCJordonConstant.mapServiceIdToBCJordonConfig.get(vwServiceCampaignDetail.getServiceId());
			}
			
			redisCacheService.putObjectCacheValueByEvictionDay(BCJordonConstant.CG_CALLBACK_CAHCHE_PREFIX+bcJordonCGCallback.getMsisdn(),bcJordonCGCallback.getCid(), 1);
			
			if(bcJordonServiceConfig!=null){
				request.getSession().setAttribute("msisdn", request.getParameter("msisdn"));	
				request.getSession().setMaxInactiveInterval(12000);	
				portalUrl=bcJordonServiceConfig.getPortalUrl();
			}
			logger.info("cgCallback::::::::: "+request.getQueryString());
			}catch(Exception ex){
			logger.error("callback", ex);
		}finally{
			if(portalUrl==null){
				portalUrl="";
			}
			bcJordonCGCallback.setRedirectToUrl(portalUrl);
			jmsService.saveObject(bcJordonCGCallback);
		}		
			modelAndView.setView(new RedirectView(portalUrl));
			return modelAndView;
//		return "Ok";	  
	}
	
	@RequestMapping("notification")
	@ResponseBody
	public String notification(HttpServletRequest  request){
		logger.info("notification:::::::::: "+request.getQueryString());
		BCJordonNotification bcJordonNotification = new BCJordonNotification();
		try {
		
		bcJordonNotification.setMsisdn(request.getParameter("msisdn"));
		String notificationType = request.getParameter("notificationtype");
		if(notificationType.equals("1")) {
			bcJordonNotification.setNotificationtype(BCJordonConstant.SUBSCRIBE);
		}else if(notificationType.equals("2")) {
			bcJordonNotification.setNotificationtype(BCJordonConstant.UNSUBSCRIBE);
		}else if(notificationType.equals("3")) {
			bcJordonNotification.setNotificationtype(BCJordonConstant.RENEW);
			bcJordonNotification.setRenew(Boolean.parseBoolean(request.getParameter("renew")));
		}
//		bcJordonNotification.setNotificationtype(request.getParameter("notificationtype"));
		bcJordonNotification.setQueryStr(request.getQueryString());
		bcJordonNotification.setSid(request.getParameter("sid"));
		bcJordonNotification.setMo(request.getParameter("mo"));
		bcJordonNotification.setCreateTime(new Timestamp(System.currentTimeMillis()));
		logger.info("bcJordonNotification : "+bcJordonNotification);
		jmsjordonService.bcJordonNotificationJMSTemplate(bcJordonNotification);
		
		}catch(Exception ex) {
			logger.info("error in notification:: "+ex);
		}
		return "Ok";	
	}  
	
	@RequestMapping("mt/dlr")
	@ResponseBody
	public String dlr(HttpServletRequest  request){
		logger.info("mt/dlr:::::::::: "+request.getQueryString());
		BCJordonMTMessage bcJordonMTMessage = new BCJordonMTMessage();
		try {  	
		bcJordonMTMessage.setMsisdn(request.getParameter("msisdn"));
		bcJordonMTMessage.setMtid(request.getParameter("mtid"));
		bcJordonMTMessage.setQueryStr(request.getQueryString());
		bcJordonMTMessage.setServiceId(MUtility.toInt(request.getParameter("ServiceId"), 0));
		bcJordonMTMessage.setStatus(Boolean.parseBoolean(request.getParameter("status")));
		bcJordonMTMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
		}catch(Exception ex) {
			logger.info("error in MT dlr:: "+ex);
		}finally {
			jmsjordonService.bcJordonMTMessageJMSTemplate(bcJordonMTMessage);
		}
		
		return "Ok";	  
	}
}
