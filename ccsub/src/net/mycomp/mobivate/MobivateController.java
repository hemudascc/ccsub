package net.mycomp.mobivate;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.common.service.SubscriberRegService;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MData;

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
@RequestMapping("mobic")
public class MobivateController {

	private static final Logger logger = Logger
			.getLogger(MobivateController.class.getName());
	
	@Autowired
	private SubscriberRegService subscriberRegService; 
	
	@Autowired
	private MobivateApiService mobivateApiService;
	
	@Autowired
	private JMSMobivateService jmsMobivateService;
	
	@RequestMapping("callback/{token}")	
	public ModelAndView callback(HttpServletRequest request,ModelAndView modelAndView,
			@PathVariable(value="token")String token){
		
		logger.info("callback::::::::: "+request.getQueryString());
		MobivateCGCallback mobivateCGCallback=null;
		try{
			//http://yourdomain/returned?status=success&userid=277123456789&network=vodacomsa
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			MobivateServiceConfig mobivateServiceConfig=
					MobivateConstant.mapServiceIdToMobivateServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			mobivateCGCallback=new MobivateCGCallback(true,mobivateServiceConfig.getTimeZone());
			mobivateCGCallback.setCgStatus(request.getParameter("status"));
			mobivateCGCallback.setUserId(request.getParameter("userid"));
			mobivateCGCallback.setNetwork(request.getParameter("network"));
			mobivateCGCallback.setQueryStr(request.getQueryString());
			mobivateCGCallback.setToken(token);
			
			mobivateCGCallback.setCampaignId(cgToken.getCampaignId());
			int subId=0;
			if(mobivateCGCallback.getCgStatus()!=null&&mobivateCGCallback.getCgStatus()
					.equalsIgnoreCase(MobivateConstant.SUCCESS)){
				LiveReport liveReport =new LiveReport(vwServiceCampaignDetail.getOpId(),
						new Timestamp(System.currentTimeMillis()),
						-1, vwServiceCampaignDetail.getServiceId(), vwServiceCampaignDetail.getProductId());
						
				SubscriberReg subscriberReg=subscriberRegService.findOrCreateSubscriberByAct(mobivateCGCallback.getUserId()
						, null, liveReport);	
				if(subscriberReg!=null){
					subId=subscriberReg.getSubscriberId();
				}
				
			}		
		 
			modelAndView.setView(new RedirectView(MobivateConstant.getPortal(mobivateServiceConfig.getPortalUrl()
						,mobivateCGCallback.getUserId(),subId)));
			
			
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}finally{
			jmsMobivateService.saveMobivateCGCallback(mobivateCGCallback);
		}
		return modelAndView;
	}
	
	@RequestMapping("callback")	
	@ResponseBody
	public String cgcallback(HttpServletRequest request,ModelAndView modelAndVie){
		logger.info("cgcallback:::::::::::: "+request.getQueryString());
		return "ok";
	}
	
	@RequestMapping(value={"noti"},method={RequestMethod.GET,RequestMethod.POST})	
	@ResponseBody
	public String noti(HttpServletRequest request,ModelAndView modelAndVie){
		logger.info("notification:::::::::::: "+request.getQueryString());
		String queryStr="";
		Map<String,String[]> map=request.getParameterMap();
		Iterator<String> itr=map.keySet().iterator();
		while(itr.hasNext()){
		    String param=(String)itr.next();
		    String value=request.getParameter(param);
		    queryStr+=param+"="+value+"&";
		}
		logger.info("notification::::::::::::queryStr "+queryStr);
		return "ok";
	}
	
	@RequestMapping(value={"mo"},method={RequestMethod.GET,RequestMethod.POST})	
	@ResponseBody
	public String mo(HttpServletRequest request,ModelAndView modelAndVie){
		MobivateMO mobivateMO=null;
		try{
			logger.info("mo::::: "+request.getQueryString());
			mobivateMO=new MobivateMO();
			mobivateMO.setOriginator(request.getParameter("ORIGINATOR"));
			mobivateMO.setRecipient(request.getParameter("RECIPIENT"));
			mobivateMO.setMessageText(request.getParameter("MESSAGE_TEXT"));
			mobivateMO.setProvider(request.getParameter("PROVIDER"));
			mobivateMO.setReference(request.getParameter("REFERENCE"));
			mobivateMO.setValue(request.getParameter("VALUE"));
			mobivateMO.setKeyword(request.getParameter("KEYWORD"));
			mobivateMO.setCampaign(request.getParameter("CAMPAIGN"));
			mobivateMO.setRecipient(request.getParameter("RECIPIENT"));
			mobivateMO.setQueryStr(request.getQueryString());
			
		}catch(Exception ex){
			logger.error("mo ",ex);
		}finally{
			jmsMobivateService.saveMobivateMOJMSTemplate(mobivateMO);
		}
		return "OK";
		
	}
	
	@RequestMapping(value={"dlr"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String dlr(HttpServletRequest request,ModelAndView modelAndVie){
		MobivateSMSDlr mobivateSMSDlr=null;
		try{
		logger.info("dlr::: "+request.getQueryString());	
		 mobivateSMSDlr=new MobivateSMSDlr(true);
		mobivateSMSDlr.setMsisdn(request.getParameter("RECIPIENT"));
		mobivateSMSDlr.setDlrMessageText(request.getParameter("MESSAGE_TEXT"));
		mobivateSMSDlr.setDlrProvider(request.getParameter("PROVIDER"));
		mobivateSMSDlr.setDlrReference(request.getParameter("REFERENCE"));
		mobivateSMSDlr.setDlrSmsgatewayId(request.getParameter("ID"));
		mobivateSMSDlr.setDlrResult(request.getParameter("RESULT"));
		mobivateSMSDlr.setQueryStr(request.getQueryString());
		}catch(Exception ex){
			logger.error("dlr ",ex);
		}finally{
			jmsMobivateService.saveMobivateSMSDlrJMSTemplate(mobivateSMSDlr);
		}
		return "OK";
	}
	
	@RequestMapping("ukwayoflife_tc")	
	public ModelAndView ukwayoflifeTc(HttpServletRequest request,ModelAndView modelAndView){
		modelAndView.setViewName("mobivate/ukwayoflife_tc");		  
		return modelAndView;
	}
	
	@RequestMapping("ukgamepad_tc")	
	public ModelAndView ukgamepadTc(HttpServletRequest request,ModelAndView modelAndView){
		modelAndView.setViewName("mobivate/ukgamepad_tc");		  
		return modelAndView;
	}
	
	@RequestMapping(value={"ukvoda/tocg"},method={RequestMethod.GET,RequestMethod.POST})	
	public ModelAndView ukVodaToCG(HttpServletRequest request,ModelAndView modelAndView){
		
		MobivateSMSDlr mobivateSMSDlr=null;
		try{
			
		String token=request.getParameter("token");
		modelAndView.addObject("token", token);
		modelAndView.setViewName("mobivate/uk_vodafone_lp");
		CGToken  cgToken=new CGToken(token);
		VWServiceCampaignDetail vwServiceCampaignDetail=
				MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		MobivateServiceConfig mobivateServiceConfig=
				MobivateConstant.mapServiceIdToMobivateServiceConfig.get(vwServiceCampaignDetail.getServiceId());
		
		modelAndView.addObject("mobivateServiceConfig",mobivateServiceConfig );	
	    modelAndView.addObject("token",token);
	
		MobivateApiTrans mobivateApiTrans= mobivateApiService.getUKVodaofoneCGUrl(mobivateServiceConfig, token);
		if(mobivateApiTrans.getSuccess()){
			modelAndView.clear();
			modelAndView.setView(new RedirectView(mobivateApiTrans.getResponse()));
		}
		}catch(Exception ex){
			logger.error("dlr ",ex);
		}finally{
			jmsMobivateService.saveMobivateSMSDlrJMSTemplate(mobivateSMSDlr);
		}
		return modelAndView;
	}
}
