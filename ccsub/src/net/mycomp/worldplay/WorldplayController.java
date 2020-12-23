package net.mycomp.worldplay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jpa.repository.JPASubscriberReg;
import net.mycomp.messagecloud.gateway.MCGConstant;
import net.mycomp.messagecloud.gateway.MCGServiceConfig;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping("wp")
public class WorldplayController {

	private static final Logger logger = Logger
			.getLogger(WorldplayController.class.getName());
	
	@Autowired
	private JMSWorldplayService jmsWorldplayService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@RequestMapping(value = "notification", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String worldPlayNotification(HttpServletRequest request, HttpServletResponse response) {
		String resp = "<?xml version=\"1.0\" encoding=\"utf-8\"?><r><errorCode>0</errorCode></r>";
		WorldplayNotification worldplayNotification=new WorldplayNotification(true);
		try{
		
		worldplayNotification.setQueryStr(request.getQueryString());
		worldplayNotification.setMt(request.getParameter("mt"));
		worldplayNotification.setOperatorName(request.getParameter("operator"));
		worldplayNotification.setClient(request.getParameter("client"));
		worldplayNotification.setRequestId(request.getParameter("requestId"));
		worldplayNotification.setTelNo(request.getParameter("telNo"));
		worldplayNotification.setAdTracking(request.getParameter("adTracking"));
		worldplayNotification.setSource(request.getParameter("source"));
		worldplayNotification.setStatusId(request.getParameter("statusId"));		
		worldplayNotification.setRefId(request.getParameter("refId"));
		worldplayNotification.setAmount(request.getParameter("amount"));
		worldplayNotification.setWpauthReqRef(request.getParameter("wpAuthReqRef"));
		worldplayNotification.setStatusTime(request.getParameter("statusTime"));
		worldplayNotification.setStatusMessage(request.getParameter("statusMessage"));
		
	
		}catch(Exception ex){
			logger.error("exception "+request.getQueryString(),ex);
		}finally{
			jmsWorldplayService.saveWorldplayNotification(worldplayNotification);
		}
	    return resp;
	}
	
	@RequestMapping(value = "portal", method = { RequestMethod.GET, RequestMethod.POST })
	public  ModelAndView portal(HttpServletRequest request, HttpServletResponse response,
			ModelAndView modelAndView) {
	
		WorldplayCGCallback worldplayCGCallback=new WorldplayCGCallback(true);
		try{
			
			String serviceId=request.getParameter("serviceid");
		    logger.info("worldplayCGCallback::: "+request.getQueryString());
		         worldplayCGCallback.setAction("PORTAL");
			    worldplayCGCallback.setWorldPlayServiceId(serviceId);
			    worldplayCGCallback.setQueryStr(request.getQueryString());
				worldplayCGCallback.setMt(request.getParameter("mt"));
				worldplayCGCallback.setClient(request.getParameter("client"));
				worldplayCGCallback.setOperator(request.getParameter("operator"));
				worldplayCGCallback.setTelNo(request.getParameter("telNo"));
				worldplayCGCallback.setRequestId(request.getParameter("requestId"));
				worldplayCGCallback.setSubDate(request.getParameter("subDate"));
				worldplayCGCallback.setAdTracking(request.getParameter("adTracking"));
				
	       if(serviceId.equalsIgnoreCase("2254")){
	    	   modelAndView.setView(new RedirectView("https://www.theway2life.com/EN/"));
	       }else {
	    	   modelAndView.setView(new RedirectView("http://www.gogames.tv/"));
	       }
	
		}catch(Exception ex){
			logger.error("exception "+request.getQueryString(),ex);
		}finally{
			jmsWorldplayService.saveWorldplayCGCallback(worldplayCGCallback);
		}
	    return modelAndView;
	}
	
	
	@RequestMapping(value = "cgcallback/{worldPlaySerivceId}", method = { RequestMethod.GET, RequestMethod.POST })
	public  ModelAndView cgCallback(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="worldPlaySerivceId")String worldPlaySerivceId,ModelAndView modelAndView) {
	
		WorldplayCGCallback worldplayCGCallback=new WorldplayCGCallback(true);
		try{
			
		    logger.info("worldplayCGCallback::: "+request.getQueryString());
		    worldplayCGCallback.setAction("CGCALLBACK");
		    worldplayCGCallback.setWorldPlayServiceId(worldPlaySerivceId);
		    worldplayCGCallback.setQueryStr(request.getQueryString());
			worldplayCGCallback.setMt(request.getParameter("mt"));
			worldplayCGCallback.setClient(request.getParameter("client"));
			worldplayCGCallback.setOperator(request.getParameter("operator"));
			worldplayCGCallback.setTelNo(request.getParameter("telNo"));
			worldplayCGCallback.setRequestId(request.getParameter("requestId"));
			worldplayCGCallback.setSubDate(request.getParameter("subDate"));
			worldplayCGCallback.setAdTracking(request.getParameter("adTracking"));
		
	       if(worldPlaySerivceId.equalsIgnoreCase("2254")){
	    	   modelAndView.setView(new RedirectView("https://www.theway2life.com/EN/"));
	       }else {
	    	   modelAndView.setView(new RedirectView("http://www.gogames.tv/"));
	       }
	
		}catch(Exception ex){
			logger.error("exception "+request.getQueryString(),ex);
		}finally{
			jmsWorldplayService.saveWorldplayCGCallback(worldplayCGCallback);
		}
	    return modelAndView;
	}
	
	@RequestMapping("termcondtion")	
	public ModelAndView termCondition(HttpServletRequest request,ModelAndView modelAndView){
		try{
		String serviceid=request.getParameter("serviceid");
		logger.info("termCondition::::::::: serviceid:: "+serviceid);
		WorldplayServiceConfig worldplayServiceConfig=WorldplayConstant.
				mapServiceIdToWorldplayServiceConfig
				.get(MUtility.toInt(serviceid, 70));	
		modelAndView.addObject("worldplayServiceConfig",worldplayServiceConfig);
		modelAndView.setViewName("worldplay/wp-term-condition");
		}catch(Exception ex){
			logger.error("termCondition:: ",ex);
		}
		return modelAndView;
	}
	
	

}
