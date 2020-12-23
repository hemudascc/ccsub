package net.mycomp.etisalat;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;

import net.common.jms.JMSService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.MConstants;
import net.util.MData;
import net.util.SubscriptionType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("eti")
public class EtisalatController {

	private static final Logger logger = Logger
			.getLogger(EtisalatController.class.getName());
	
	private JAXBContext jaxbContext ;
	
	private final String defaultPortalUrl;
	
	private final String  defaultCampaignUrl;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg; 
	
	@Autowired
	private JMSEtisalatService jmsEtisalatService;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	public  EtisalatController(@Value("${etisalat.portal.url}")String defaultPortalUrl,
			@Value("${etisalat.default.campaign.url}") String defaultCampaignUrl) {
		this.defaultPortalUrl=defaultPortalUrl;
		this.defaultCampaignUrl=defaultCampaignUrl;
		
	}
	
	@PostConstruct
	public void init(){
		try{
		jaxbContext = JAXBContext.newInstance(EtisalatChargingCallback.class);
		}catch(Exception ex){
			logger.error("init", ex);
		}finally{
			
		}
	}
	
	@RequestMapping(path="portal",method={RequestMethod.POST,RequestMethod.GET})	
	public ModelAndView portal(HttpServletRequest request,ModelAndView modelAndView){
		modelAndView.setViewName("etisalat/lp");
		return modelAndView;
	}
	
	
	@RequestMapping(path="content",method={RequestMethod.POST,RequestMethod.GET})	
	public ModelAndView accessContent(HttpServletRequest request,ModelAndView modelAndView){
		
		String msisdn=request.getParameter("msisdn");
		List<SubscriberReg> list=jpaSubscriberReg.findSubscriberRegByMsisdn(msisdn);
		boolean subscribed=false;
		if(list!=null&&list.size()>0){
			SubscriberReg subscriberReg=list.get(0);
			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				subscribed=true;
			}
		}
		
		if(subscribed){			
			EtisalatServiceConfig etisalatServiceConfig=EtisalatConstant.
					mapServiceIdToEtisalatServiceConfig.get(EtisalatConstant.ETISALAT_SERVICE_ID);
			String portalUrl="";
			if(etisalatServiceConfig!=null){
				request.getSession().setAttribute("msisdn", request.getParameter("msisdn"));	
				request.getSession().setMaxInactiveInterval(12000);	
				portalUrl=etisalatServiceConfig.getPortalUrl()+"&msisdn="+request.getParameter("msisdn");
			}
			modelAndView.setView(new RedirectView(portalUrl));
		}else{			
			modelAndView.setView(new RedirectView(defaultCampaignUrl.replaceAll("<token>", String.valueOf(System.currentTimeMillis()))));
		}
		
		return modelAndView;
	}
	
	@RequestMapping(path="cg/callback",method={RequestMethod.POST,RequestMethod.GET})	
	@ResponseBody
	public ModelAndView cgCallback(HttpServletRequest request,ModelAndView modelAndView){
		String portalUrl=null;
		EtisalatCGCallback etisalatCGCallback=new EtisalatCGCallback();
		try{
			
			EtisalatServiceConfig etisalatServiceConfig=null;
			etisalatCGCallback.setQueryStr(request.getQueryString());
			etisalatCGCallback.setMsisdn(request.getParameter("msisdn"));			
			etisalatCGCallback.setTid(request.getParameter("txnid"));			
			CGToken cgToken=new CGToken(etisalatCGCallback.getTid()); 
			
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			if(vwServiceCampaignDetail!=null){
				 etisalatServiceConfig=EtisalatConstant.mapServiceIdToEtisalatServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			}
			
			if(etisalatServiceConfig!=null){
				request.getSession().setAttribute("msisdn", request.getParameter("msisdn"));	
				request.getSession().setMaxInactiveInterval(12000);	
				portalUrl=etisalatServiceConfig.getPortalUrl()+"&msisdn="+request.getParameter("msisdn");
			}
			
			LiveReport liveReport=new LiveReport();
			liveReport.setMsisdn(etisalatCGCallback.getMsisdn());
			liveReport.setServiceId(vwServiceCampaignDetail.getServiceId());
			liveReport.setOperatorId(vwServiceCampaignDetail.getOpId());
			liveReport.setNoOfDays(1);
			liveReport.setAction(MConstants.TEMPORARY_ACT);
			liveReport.setReportDate(new Timestamp(System.currentTimeMillis()));
			liveReport.setProductId(vwServiceCampaignDetail.getProductId());
			subscriberRegService.findOrCreateSubscriberByAct(etisalatCGCallback.getMsisdn(), null, liveReport);
			
			logger.info("cgCallback::::::::: "+request.getQueryString());
			}catch(Exception ex){
			logger.error("callback", ex);
		}finally{
			if(portalUrl==null){
				portalUrl=defaultPortalUrl;
			}
			etisalatCGCallback.setRedirectToUrl(portalUrl);
			jmsService.saveObject(etisalatCGCallback);
		}		
		modelAndView.setView(new RedirectView(portalUrl));
		return modelAndView;		
	}
	
	
	@RequestMapping(path="callback",method={RequestMethod.POST,RequestMethod.GET})	
	@ResponseBody
	public String chargingCallback(HttpServletRequest request,ModelAndView modelAndView){
		   
		EtisalatChargingCallback etisalatChargingCallback=null;
		    try{
				 String xml=EtisalatConstant.getSoapRequestToString(request.getInputStream());
				logger.info("chargingCallback:: xml::::::::::::: "+xml);
				 etisalatChargingCallback=EtisalatConstant.parseXMLToJava(jaxbContext, xml);
				 etisalatChargingCallback.setRequestData(xml);
			    logger.info("callback::::::::::::::::: "+etisalatChargingCallback);
				}catch(Exception ex){
					logger.error("chargingCallback", ex);
				}finally{
					jmsEtisalatService.saveEtisalatChargingCallback(etisalatChargingCallback);
				}
				return "OK";
	}
}
