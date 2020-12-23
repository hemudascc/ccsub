package net.bizao;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.bizao.BizaoMsisdnToAlias.Info;
import net.bizao.json.bean.InboundSMSMessageNotification;
import net.bizao.json.bean.InboundSMSMessageNotificationWrapper;
import net.common.jms.JMSService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.ErrorInfo;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("bz")
public class BizaoController {

	private static final Logger logger = Logger
			.getLogger(BizaoController.class.getName());
	
	
	@Value("${bizao.invalid.mobilenumber}")
	private String bizaoInvalidMobilenumber;
	
	@Value(value="${bizao.otp.sent}")
	private String bizaoOtpSent;
	
	@Value("${bizao.invalid.country.mobilenumber}")
	private String bizaoInvalidCountryMobilenumber;
	
	@Value("${bizao.payment.request.failed}")
	private String bizaoPaymentRequestFailed;
	
	@Value("${bizao.invalid.otp}")
	private String bizaoInvalidOtp;
	@Value("${bizao.unsub.success}")
	private String bizaoUnsubSuccess;
	@Value("${bizao.not.subscribed}")
	private String bizaoNotSubscribed;
	@Value("${bizao.unsub.failed}")
	private String bizaoUnsubFailed;
	@Value("${unsub.url}")
	private  String unSubUrl;
	@Value("${bizao.unsub.customer.care}")
	private String unsubCustomerCare;
	
//	@Value("${bizao.sms.activation.template}")
//	private String bizaoSmsActivationTemplate;
	
	@Autowired
	private JMSBizaoService jmsBizaoService;
	
	@Autowired
	private JMSService jmsService;
	
	@Autowired
	private BizaoApiService bizaoApiService;
	
	@Autowired
	private JPASubscriberReg jpaSubscriberReg;
	
	@Autowired
	private SubscriberRegService subscriberRegService;
	
	@Autowired
	private BizaoScheduler bizaoScheduler;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Value("${bizao.portal.url}")
	private String defaultPortalUrl;
	
	public BizaoController(){
		
	}
	
	@RequestMapping("change/msisdnprefix")
	public ModelAndView changeMsisdnPrefix(ModelAndView modelAndView,HttpServletRequest request
			){
		
		modelAndView.addObject("msisdn",request.getParameter("msisdn"));
		modelAndView.addObject("source",request.getParameter("source"));
		modelAndView.addObject("uniqueid",request.getParameter("uniqueid"));
		
		modelAndView.addObject("listMsisdnPrefix", BizaoConstant.listMsisdnPrefix); 
		modelAndView.addObject("msisdnprefix",request.getParameter("msisdnprefix"));
		String stype=request.getParameter("stype");
		if(stype==null){
			stype=BizaoConstant.DAILY;
		}
		modelAndView.addObject("stype",request.getParameter("stype"));
		
		
		BizaoConfig bizaoConfig=BizaoConstant.mapBizaoMsisdnPrefixServiceTypeToBizaoConfig.
				get(request.getParameter("msisdnprefix")+stype);
		modelAndView.addObject("bizaoConfig", bizaoConfig);
		
		String cgtoken=request.getParameter("cgtoken");
		BizaoCGToken bizaoCGToken=new BizaoCGToken(cgtoken);
		bizaoCGToken.setConfigId(bizaoConfig.getId());
		modelAndView.addObject("cgtoken", bizaoCGToken.getBizaoCGToken());
	    modelAndView.setViewName("bizao/msisdn_missing");
	    return modelAndView;	    
	}
	
	
	
	@RequestMapping(value="send/otp",method= RequestMethod.POST)
	public ModelAndView sendOTP(ModelAndView modelAndView,HttpServletRequest  request){
		
	   String msisdn=request.getParameter("msisdn");
	  
	   modelAndView.addObject("source",request.getParameter("source"));
	   String uniqueid=request.getParameter("uniqueid");
		modelAndView.addObject("uniqueid",uniqueid);
	   String msisdnPrefix=MUtility.removeNull(request.getParameter("msisdnprefix"));
	   String cgToken=request.getParameter("cgtoken");	
	   modelAndView.addObject("msisdnprefix",request.getParameter("msisdnprefix"));
	   BizaoCGToken bizaoCGToken=new BizaoCGToken(cgToken);
	   String stype=request.getParameter("stype");
		if(stype==null){
			stype=BizaoConstant.DAILY;
		}
		modelAndView.addObject("stype",request.getParameter("stype"));
		
	   BizaoConfig bizaoConfig=BizaoConstant.mapBizaoMsisdnPrefixServiceTypeToBizaoConfig.
			   get(msisdnPrefix+stype);
	   
	   bizaoCGToken.setConfigId(bizaoConfig.getId());
	   modelAndView.setViewName("bizao/msisdn_missing");
		 if(!bizaoApiService.isValidSecurity(msisdn, uniqueid, cgToken)){
		 logger.info("webCallback:::  security failed "+msisdn+" ,uniqid:: "+uniqueid);
		 modelAndView.setViewName("bizao/error");
		 return modelAndView;
	 }
	   
	   if(msisdn==null||msisdn.equalsIgnoreCase("")){
		   modelAndView.addObject("otpinfo",BizaoConstant.getMsg(bizaoInvalidMobilenumber, bizaoConfig));
	   }else{
		 BizaoOtpTrans bizaoOtp=bizaoApiService.sendOtp(msisdn, bizaoConfig, null);
	   if(bizaoOtp.getResponseCode()==201&&bizaoOtp.getResponse()!=null){
		   modelAndView.addObject("otpinfo", BizaoConstant.getMsg(bizaoOtpSent, bizaoConfig));
		   modelAndView.setViewName("bizao/msisdn_otp");
		}else{				
			modelAndView.addObject("otpinfo", BizaoConstant.getMsg(bizaoInvalidCountryMobilenumber, bizaoConfig));
			modelAndView.setViewName("bizao/msisdn_missing");
		}
	   
	   //jmsService.saveObject(bizaoOtp);
	   }	   
		modelAndView.addObject("msisdn",msisdn);
		modelAndView.addObject("listMsisdnPrefix", BizaoConstant.listMsisdnPrefix); 
		modelAndView.addObject("msisdnprefix",msisdnPrefix); 
		modelAndView.addObject("bizaoConfig", bizaoConfig);
		modelAndView.addObject("cgtoken", bizaoCGToken.getBizaoCGToken());	    
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="send/billed",method={ RequestMethod.POST, RequestMethod.GET})
	public ModelAndView sendBilled(ModelAndView modelAndView,HttpServletRequest  request){
		
		BizaoValidateOtpTrans bizaoValidateOtp=null;
		
		try{
		String msisdn=request.getParameter("msisdn");
		modelAndView.addObject("source",request.getParameter("source"));
		String uniqueid=request.getParameter("uniqueid");
		modelAndView.addObject("uniqueid",uniqueid);
		
		String msisdnPrefix=request.getParameter("msisdnprefix");
		String otp=request.getParameter("otp");
		String cgtoken=request.getParameter("cgtoken");	
		modelAndView.addObject("msisdnprefix",request.getParameter("msisdnprefix"));
		BizaoConfig bizaoConfig=BizaoConstant.mapIdToBizaoConfig.
				get(MUtility.toInt(request.getParameter("bizaoconfigid"), 1));
		BizaoCGToken bizaoCGToken=
				new BizaoCGToken(cgtoken);
		bizaoCGToken.setConfigId(bizaoConfig.getId());
	
		 
		bizaoValidateOtp=bizaoApiService.validateOTP(msisdn, bizaoConfig,bizaoCGToken.getBizaoCGToken(), otp);
		//bizaoValidateOtp.setIsValid(true);
		//bizaoValidateOtp.setBizaoAalias("PDKSUB-200-eIYInLP+aAmgoIkG4OFyFMBSXpRUWIfDznYAjmICo/M=");
		
		if(bizaoValidateOtp.getIsValid()){
			
			SubscriberReg subscriberReg=jpaSubscriberReg.
					findSubscriberRegByMsisdnAndProductId(bizaoValidateOtp.getBizaoAalias(),
							bizaoConfig.getProductId());
			
			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				request.getSession().setAttribute("sub", "1");
				request.getSession().setMaxInactiveInterval(20000);
				modelAndView.clear();
			    modelAndView.setView(new RedirectView(BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),
			    		subscriberReg.getSubscriberId(),
			    		bizaoValidateOtp.getBizaoAalias())));
			    return modelAndView;
			}
			

			 
		BizaoPayment bizaoPayment=bizaoApiService.makePayment(MConstants.ACT,msisdn, bizaoConfig, 
				bizaoCGToken.getBizaoCGToken(),
					bizaoValidateOtp.getBizaoToken(),bizaoValidateOtp.getBizaoAalias());
		
			
			if(bizaoPayment.getSuccess()==true){		
				request.getSession().setAttribute("sub", "1");
				request.getSession().setMaxInactiveInterval(20000);
				LiveReport liveReport=new LiveReport(bizaoConfig.getOpId(),new Timestamp(System.currentTimeMillis())
						 ,MData.findCampaignId(bizaoCGToken.getAdnetworkId(),
								 bizaoConfig.getServiceId()),bizaoConfig.getServiceId(),bizaoConfig.getProductId()); 
				liveReport.setMsisdn(bizaoPayment.getBizaoAlias());
				liveReport.setParam1(bizaoPayment.getBizaoToken());
				liveReport.setCircleId(0);		 
				liveReport.setAction(MConstants.ACT);
				liveReport.setNoOfDays(bizaoConfig.getValidity());
				
				 subscriberReg=subscriberRegService.findOrCreateSubscriberByAct(bizaoPayment.getBizaoAlias(), null,
						 liveReport);
				 
				 modelAndView.clear();
				 //Add new page
				 modelAndView.addObject("bizaoConfig", bizaoConfig);
				 modelAndView.addObject("portalurl",BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl()
				    		,subscriberReg.getSubscriberId(),bizaoValidateOtp.
				    		getBizaoAalias()));
				 modelAndView.addObject("unsuburl",unsubCustomerCare);
				 modelAndView.setViewName("bizao/final_page");
				 
//			    modelAndView.setView(new RedirectView(BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl()
//			    		,subscriberReg.getSubscriberId(),bizaoValidateOtp.
//			    		getBizaoAalias())));
			    return modelAndView;
			    
			}else{
				modelAndView.addObject("billedinfo", BizaoConstant.getMsg(bizaoPaymentRequestFailed, bizaoConfig));
		        modelAndView.setViewName("bizao/msisdn_missing");
			}
		}else{			
			modelAndView.addObject("billedinfo", BizaoConstant.getMsg(bizaoInvalidOtp, bizaoConfig));			
			modelAndView.setViewName("bizao/msisdn_missing");
		}
		 String stype=request.getParameter("stype");
			if(stype==null){
				stype=BizaoConstant.DAILY;
			}
		
		modelAndView.addObject("stype",request.getParameter("stype"));
		modelAndView.addObject("msisdn", msisdn);
		modelAndView.addObject("listMsisdnPrefix", BizaoConstant.listMsisdnPrefix); 
		modelAndView.addObject("msisdnprefix", msisdnPrefix); 
		modelAndView.addObject("bizaoConfig", bizaoConfig);
		modelAndView.addObject("cgtoken", bizaoCGToken.getBizaoCGToken());
		
		}catch(Exception ex){
			logger.error("Exception::::::: ",ex);
		}finally{
		
		}
	    return modelAndView;
	}
	
	@RequestMapping("sms/callback")
	@ResponseBody
	public String smsCallback(ModelAndView modelAndView,HttpServletRequest  request){
		logger.info("smsCallback::: "+request.getQueryString());
		
		return "ok";
	}
	
	
	
	@RequestMapping(value="mo",
			method= {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String mo(ModelAndView modelAndView,HttpServletRequest  request){
		logger.info("mo::: "+request.getQueryString());
		BizaoMoTrans bizaoMoTrans=new BizaoMoTrans();
		try{
		
		bizaoMoTrans.setBizaoToken(request.getHeader("bizao-token"));
		bizaoMoTrans.setBizaoAlias(request.getHeader("bizao-alias"));
		String json = BizaoConstant.extractData(request.getInputStream());
		bizaoMoTrans.setRequest(json);
		InboundSMSMessageNotificationWrapper inboundSMSMessageNotificationWrapper=JsonMapper.getJsonToObject(json, InboundSMSMessageNotificationWrapper.class);
		logger.info("mo:: inboundSMSMessageNotification "+inboundSMSMessageNotificationWrapper);
		InboundSMSMessageNotification inboundSMSMessageNotification=inboundSMSMessageNotificationWrapper.getInboundSMSMessageNotification();
		bizaoMoTrans.setMessage(inboundSMSMessageNotification.getInboundSMSMessage().getMessage());
		bizaoMoTrans.setDestinationAddress(inboundSMSMessageNotification.getInboundSMSMessage().getDestinationAddress());
		}catch(Exception ex){
			logger.error("mo:: Exception: ",ex);
		}finally{
			jmsBizaoService.saveBizaoMOTrans(bizaoMoTrans);
		}
		return "ok";
	}
	
	@RequestMapping("web/callback")
	public ModelAndView callback(ModelAndView modelAndView,HttpServletRequest  request){
		return modelAndView;
	}
	
	
	@RequestMapping("3g/callback/{token}")
	public ModelAndView webCallback(ModelAndView modelAndView,HttpServletRequest  request,
			@PathVariable(value="token") String strBizaoToken
			
			){
		
		try{
		
	//	logger.info("webCallback::::::::::::: "+request.getQueryString()+" , uniqid:: "+uniqid);
		
		
		BizaoCGToken bizaoCGToken=new BizaoCGToken(strBizaoToken);
		BizaoConfig bizaoConfig=BizaoConstant.
 				mapIdToBizaoConfig.get(bizaoCGToken.getConfigId());
		
		// modelAndView.addObject("uniqid",uniqid);
		 
		String bizaToken=request.getHeader("Bizao-Token");
		if(bizaToken==null){
			bizaToken=request.getHeader("orangeapitoken");
		}
		String bizaoAlias=request.getHeader("Bizao-Alias");
			if(bizaoAlias==null){
			bizaoAlias=request.getHeader("x-orange-ise2");
		}
		//bizaoAlias="PDKSUB-200-knf3ndjuMYOAXjxja3c2TY4SYjDGtq3TuswGEcLFaHQ=";
		//bizaToken="B648iNMdd3KlmYECnCkJjJQOq8BQPXDMd59DMMwhRKovwbDCB93PVC4jP1ypV0TVA8S|MCO=OCI|tcd=1549027119|ted=1549027219|XSMrDZXBpeZjabw5DUPzPuyFGEQ=";
		logger.info("webCallback::::::::::::: strBizaoToken:: "+strBizaoToken+" ,"+request.getQueryString()
				+"bizaToken:: "+bizaToken+", bizaoAlias:: "+bizaoAlias);
		
		if(bizaToken!=null){
			
			SubscriberReg subscriberReg= jpaSubscriberReg.findSubscriberRegByMsisdnAndServiceId(bizaoAlias,bizaoConfig.getServiceId());
			
			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				modelAndView.clear();
				 modelAndView.setView(new RedirectView(BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),subscriberReg.getSubscriberId(),
				    		bizaoAlias)));
				 return modelAndView;
			}
			
//		 if(!bizaoApiService.isValidSecurity(bizaoAlias, uniqid, bizaToken)){
//			 logger.info("webCallback:::  security failed "+bizaoAlias+" ,uniqid:: "+uniqid);
//			 modelAndView.setViewName("bizao/error");
//			 return modelAndView;
//		 }
		 
		
			BizaoPayment bizaoPayment=bizaoApiService.makePayment(MConstants.ACT,
					null, bizaoConfig, bizaoCGToken.getBizaoCGToken(),
					bizaToken,bizaoAlias);
			
			if (bizaoPayment.getAction().equalsIgnoreCase(MConstants.ACT)&&bizaoPayment.getSuccess()==true) { 
				
				LiveReport liveReport=new LiveReport(bizaoConfig.getOpId(),new Timestamp(System.currentTimeMillis())
						 ,MData.findCampaignId(bizaoCGToken.getAdnetworkId(),
								 bizaoConfig.getServiceId()),bizaoConfig.getServiceId(),bizaoConfig.getProductId()); 
				liveReport.setMsisdn(bizaoPayment.getBizaoAlias());
				liveReport.setParam1(bizaoPayment.getBizaoToken());
				liveReport.setCircleId(0);		 
				liveReport.setAction(MConstants.ACT);
				liveReport.setNoOfDays(bizaoConfig.getValidity());				
				 subscriberReg=subscriberRegService.findOrCreateSubscriberByAct(bizaoPayment.getBizaoAlias(), null,
						 liveReport);
				 modelAndView.clear();
			 modelAndView.setView(new RedirectView(BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),subscriberReg.getSubscriberId(),bizaoAlias)));
			 return modelAndView;
			   }else{
				   modelAndView.clear();
				 modelAndView.setView(new RedirectView(BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),0,bizaoAlias)));
				 return modelAndView;
			}			
			//jmsBizaoService.saveBizaoPayment(bizaoPayment);	
		   
		
		}else{
			modelAndView.addObject("bizaoConfig", bizaoConfig);
			modelAndView.addObject("listMsisdnPrefix", BizaoConstant.listMsisdnPrefix);
			modelAndView.addObject("token",bizaoCGToken.getBizaoCGToken());
			modelAndView.setViewName("bizao/msisdn_missing");
		  }		
		}catch(Exception ex){
			logger.error("Exception ",ex);
			modelAndView.setView(new RedirectView(defaultPortalUrl));
		}
		return modelAndView;
	}	
	
	
	
	@RequestMapping("sendSms")
	@ResponseBody
	public String sendSMS(ModelAndView modelAndView,HttpServletRequest  request){
		logger.info("smsCallback::: "+request.getQueryString());
		BizaoConfig bizaoConfig=BizaoConstant.
 				mapIdToBizaoConfig.get(1);
		String portalUrl=bizaoConfig.getPortalUrl().replaceAll("<subid>", String.valueOf(
				"63561"));
		String msg=bizaoConfig.getInformaticMsgTemplate().replaceAll("<currency>", bizaoConfig.getCurrencyDesc())
				 .replaceAll("<amount>", String.valueOf(bizaoConfig.getPricePoint()))
				 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
				  .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
				   .replaceAll("<portalurl>", String.valueOf(portalUrl));
		bizaoApiService.sendSms(MConstants.ACT, bizaoConfig.getMsisdnPrefix(), 
						 bizaoConfig, 
						msg, "B64ql9noOp+T/g3BpRE0pRR5u9jl0qe1Q0+zNXKRCRXGM4xKj1Pah1nf9WGU8Vq96UD|MCO=OCI|tcd=1548412104|ted=1548412204|LZoLC1kMOQpqBvflYY7yI4P3k3c="
						,"PDKSUB-200-ms/tobPR7SV0YcEobSAP3t2H6XhQxQX18gD7ipVFyEI=","");
		
		return "ok";
	}
	
	@RequestMapping("p")	
	public ModelAndView portal(ModelAndView modelAndView,HttpServletRequest  request){
		
		String portalUrl="";
		try{
		Integer subscriberId=MUtility.toInt(request.getParameter("id"), 0);
		SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegById(subscriberId);
		BizaoConfig bizaoConfig=BizaoConstant.
 				mapIdToBizaoConfig.get(subscriberReg.getServiceId());
		
		portalUrl=BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),subscriberReg.getSubscriberId(),
				subscriberReg.getMsisdn()); 
		
		logger.info("unsub::: "+request.getQueryString());	
		}catch(Exception ex){
			logger.error("portal   ",ex);
			portalUrl=defaultPortalUrl;
		}finally{
			modelAndView.setView(new RedirectView(portalUrl));
		}
		return modelAndView;
	}
	
	@RequestMapping(value={"cus"})	
	public ModelAndView unsubBySubId(ModelAndView modelAndView,HttpServletRequest  request){
		String subcriptionId=request.getParameter("subid");
		if(subcriptionId==null){
			subcriptionId=request.getParameter("s");
		}
		if(subcriptionId==null||MUtility.toInt(subcriptionId,-1)<=0){
			modelAndView.setView(new RedirectView("http://192.241.253.234/ccsub/cnt/bz/unsub/msisdn"));
			return modelAndView;
		}
		SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegById(MUtility.toInt(subcriptionId,0));
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
			BizaoConfig bizaoConfig=	BizaoConstant.mapServiceIdToBizaoConfig.get(subscriberReg.getServiceId());
			modelAndView.addObject("bizaoConfig", bizaoConfig);
			modelAndView.addObject("subscriberReg", subscriberReg);
		}else{
			
		   modelAndView.addObject("info", bizaoNotSubscribed);	
		}
		modelAndView.addObject("portalUrl",defaultPortalUrl);
		modelAndView.addObject("unSubUrl",unSubUrl);
		modelAndView.setViewName("bizao/deactivtion");
		return modelAndView;
	}
	
	@RequestMapping(value={"unsub","u"})	
	public ModelAndView prcoessUnsub(ModelAndView modelAndView,HttpServletRequest  request){
		
		logger.info("unsub::: "+request.getQueryString());
		
		try{
		
		SubscriberReg subscriberReg=null;
		String msisdn=request.getParameter("msisdn");
		Integer serviceId=MUtility.toInt(request.getParameter("serviceid"),0);
		String subcriptionId=request.getParameter("subid");
		if(subcriptionId==null){
			subcriptionId=request.getParameter("s");
		}
		Integer  subId=MUtility.toInt(subcriptionId,0);
		if(msisdn!=null){
		List<SubscriberReg> list=jpaSubscriberReg.findSubscriberRegByMsisdnAndStatus(msisdn, 
				MConstants.SUBSCRIBED);
		
		if(list!=null&&list.size()>0){
			 subscriberReg=list.get(0);
		 }
		}else if(subId!=0){
			 subscriberReg=jpaSubscriberReg.findSubscriberRegById(subId);
		}
		
		if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
		BizaoConfig bizaoConfig=BizaoConstant.
				mapServiceIdToBizaoConfig.get(subscriberReg.getServiceId());
		
//		String msg=bizaoConfig.getDeactivationMsgTemplate()
//				 .replaceAll("<currency>", bizaoConfig.getCurrencyDesc())
//				 .replaceAll("<amount>", String.valueOf(bizaoConfig.getPricePoint()))
//				 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
//				  .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
//				  .replaceAll("<servicename>", String.valueOf(bizaoConfig.getServiceName()))
//				  .replaceAll("<portalurl>", BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl(),0,""));
		
		String msg=BizaoConstant.getMsg(bizaoConfig.getDeactivationMsgTemplate(),
				bizaoConfig, bizaoConfig.getPricePoint(), 0);
		logger.info("subscriberReg::::::::::: "+subscriberReg+", bizaoConfig:: "+bizaoConfig);
		subscriberRegService.updateDeactivation(subscriberReg.getMsisdn(),
				bizaoConfig.getProductId());
		bizaoApiService.sendSms(MConstants.DCT, bizaoConfig.getMsisdnPrefix(), 
						 bizaoConfig, 
						msg, subscriberReg.getParam1()
						,subscriberReg.getMsisdn(),"");
		    modelAndView.addObject("info",BizaoConstant.getMsg(bizaoUnsubSuccess, bizaoConfig));
		  }else{
			modelAndView.addObject("info",BizaoConstant.getMsg(bizaoNotSubscribed, null) );
		}
		}catch(Exception ex){
			logger.error("unsub::: ",ex);
			modelAndView.addObject("info", BizaoConstant.getMsg(bizaoUnsubFailed, null));
		}
		//modelAndView.setViewName("bizao/unsub");
		modelAndView.setView(new RedirectView(defaultPortalUrl));
		return modelAndView;
	}
	
	
	@RequestMapping("ip")
	@ResponseBody
	public BizaoIpPool ip(ModelAndView modelAndView,HttpServletRequest  request){
	 String ip=request.getParameter("ip");
	 BizaoIpPool bizaoIpPool=BizaoConstant.
				findBizaoOperatorByIp(ip);
		logger.info("processBilling:: bizaoIpPool:: "+bizaoIpPool);
		return bizaoIpPool;
	}
	
	@RequestMapping("msisdntoalias")
	@ResponseBody
	public BizaoMsisdnToAlias msisdnToAlias(ModelAndView modelAndView,HttpServletRequest  request){
	
	try{
	 String msisdn=request.getParameter("msisdn");
	 String msisdnprefix=request.getParameter("msisdnprefix");	
	 logger.info("msisdnToAlias:: msisdnprefix:: "+msisdnprefix+" ,msisdn:: "+msisdn);
	 BizaoConfig bizaoConfig=BizaoConstant.mapBizaoMsisdnPrefixServiceTypeToBizaoConfig.get(msisdnprefix+BizaoConstant.DAILY); 
	 logger.info("msisdnToAlias::BizaoConstant.mapBizaoMssdnPrefixToBizaoConfig:: "
	 		+ ""+BizaoConstant.mapBizaoMsisdnPrefixServiceTypeToBizaoConfig);
	 logger.info("msisdnToAlias:: bizaoConfig:: "+bizaoConfig);
	 return bizaoApiService.getAliasAndToken(msisdn, bizaoConfig);
	 }catch(Exception ex){
		 logger.error("error",ex);
	 }
	 return null;
	}
	
	
	
	
	
	//. charging.url=http://103.241.146.159/international/cnt/bz/ussdsub?
	//adid=1&cmpid=1&token=&bizaotoken=$$BIZAOTOKEN$$&
	//bizaoalias=$$BIZAOALIAS$$&circle=$$CIRCLE$$&price=$$PRICE$$&validity=$$VAL$$&currency=$$CURR$$

	@RequestMapping("ussdfreesub")
	@ResponseBody
	public boolean ussdfreesub(HttpServletRequest  request,
			@RequestParam(name="bizaotoken",required=true)String bizaToken,
			@RequestParam(name="bizaoalias",required=true)String bizaoAlias,
			@RequestParam(name="serviceid",required=true)Integer serviceId,
			@RequestParam(name="freevalidity",required=true)Integer freevalidity
			){
		try{
		BizaoConfig bizaoConfig= BizaoConstant.mapServiceIdToBizaoConfig.get(serviceId);
		 LiveReport liveReport=new LiveReport(bizaoConfig.getOpId(),new Timestamp(System.currentTimeMillis()),
				 -1 ,serviceId, bizaoConfig.getProductId());
		    liveReport.setNoOfDays(freevalidity);
		    subscriberRegService.findOrCreateSubscriberByAct(bizaoAlias, null, liveReport);
		   return true; 
		}catch(Exception ex){
			
		}
		return false;
	  }
	
	
	@RequestMapping("ussdsub")
	@ResponseBody
	public UssdResponse ussdsub(HttpServletRequest  request,
			@RequestParam(name="bizaotoken",required=true)String bizaToken,
			@RequestParam(name="bizaoalias",required=true)String bizaoAlias
			){
	 
		UssdResponse ussdResponse=new UssdResponse();
		
		logger.info("webCallback::::::::::::: "+request.getQueryString());
		BizaoCGToken bizaoCGToken=new BizaoCGToken(request.getParameter("token"));
		BizaoConfig bizaoConfig=BizaoConstant.
				mapIdToBizaoConfig.get(bizaoCGToken.getConfigId());
		logger.info("ussdsub::::::::::::: "+request.getQueryString());
		
		if(bizaToken!=null){
			
			SubscriberReg subscriberReg= jpaSubscriberReg
					.findSubscriberRegByMsisdnAndServiceId(bizaoAlias,bizaoConfig.getServiceId());
			
			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){
				ussdResponse.setAction("ALREADY_SUBSCRIBED");	
				
				
			}else{
				
			BizaoPayment bizaoPayment=bizaoApiService.makePayment(MConstants.ACT,
					null, bizaoConfig, bizaoCGToken.getBizaoCGToken(),
					bizaToken,bizaoAlias);
			if (bizaoPayment.getAction().equalsIgnoreCase(MConstants.ACT)&&bizaoPayment.getSuccess()==true) { 
				ussdResponse.setSuccess(true);
				ussdResponse.setAction("SUBSCRIBED");
				ussdResponse.setAmountCharged(bizaoPayment.getChargedAmount());		
				
//				String msg=bizaoConfig.getActivationMsgTemplate()
//						.replaceAll("<currency>", bizaoConfig.getCurrencyDesc())
//						 .replaceAll("<amount>", String.valueOf(bizaoPayment.getChargedAmount()))
//						 .replaceAll("<validity>", String.valueOf(bizaoConfig.getValidity()))
//						 .replaceAll("<shortcode>", String.valueOf(bizaoConfig.getShortCode()))
//				          ;
				String msg=BizaoConstant.getMsg(
						bizaoConfig.getActivationMsgTemplate(), bizaoConfig, bizaoPayment.getChargedAmount(),
						0);
				
			
				ussdResponse.setInfo(msg);
			}else{
				ussdResponse.setAction("FAILED");
			}
		 //jmsBizaoService.saveBizaoPayment(bizaoPayment);	
			}
		
		}		 
	return ussdResponse;	
	}
	
//	@RequestMapping(value = {"callcenter/agent"}, method = RequestMethod.GET)	
//	public ModelAndView promoUser(HttpServletRequest request,HttpServletResponse response,			
//			ModelAndView modelAndView,@RequestParam(name="msisdn",required=false)String msisdn,
//			@RequestParam(name="msisdnalias",required=false)String msisdnAlias
//			) {
//		String msisdnPrefix=request.getParameter("msisdnprefix");
//		List<SubscriberReg> subscriberRegList=null;
//		if(msisdn!=null){
//			
//			 BizaoConfig bizaoConfig=BizaoConstant.mapBizaoMssdnPrefixToBizaoConfig.get(msisdnPrefix); 
//			 BizaoMsisdnToAlias bizaoMsisdnToAlias= bizaoApiService.getAliasAndToken(msisdn, bizaoConfig);
//			 String tokenAlias="";
//			 if(bizaoMsisdnToAlias!=null&&bizaoMsisdnToAlias.getInfos()!=null&&bizaoMsisdnToAlias.getInfos().size()>0){
//				 tokenAlias=bizaoMsisdnToAlias.getInfos().get(0).getValue();
//			 }
//		subscriberRegList=jpaSubscriberReg.
//				findSubscriberRegByMsisdn(tokenAlias);
//		}		
//		logger.info("subscriberRegList::::::::msisdn: "+msisdn+", "+subscriberRegList);
//		modelAndView.addObject("subscriberRegList",subscriberRegList);
//		modelAndView.addObject("mapServiceIdToBizaoConfig",BizaoConstant.mapServiceIdToBizaoConfig);
//		
//		modelAndView.addObject("msisdn",msisdn);
//		modelAndView.addObject("msisdnPrefix",msisdnPrefix);
//		modelAndView.addObject("listMsisdnPrefix", BizaoConstant.listMsisdnPrefix);
//		modelAndView.addObject("unSubUrl",unSubUrl);
//		modelAndView.setViewName("bizao/callcenter_unsub");
//	    return modelAndView;		
//	}
	
	@RequestMapping(value = {"unsub/msisdn"}, method = RequestMethod.GET)	
	public ModelAndView unsubByMsisdn(HttpServletRequest request,HttpServletResponse response,			
			ModelAndView modelAndView
			) {
		
		try{
		String msisdn=request.getParameter("msisdn");
		String msisdnprefix=request.getParameter("msisdnprefix");	
		modelAndView.addObject("msisdn",msisdn);
		modelAndView.addObject("msisdnprefix",msisdnprefix);
		
		BizaoConfig bizaoConfig=BizaoConstant.mapBizaoMsisdnPrefixServiceTypeToBizaoConfig.get(msisdnprefix+BizaoConstant.DAILY); 
		 logger.info("msisdnToAlias::BizaoConstant.mapBizaoMssdnPrefixToBizaoConfig:: "
		 		+ ""+BizaoConstant.mapBizaoMsisdnPrefixServiceTypeToBizaoConfig);
		 logger.info("msisdnToAlias:: bizaoConfig:: "+bizaoConfig);
		 BizaoMsisdnToAlias bizaoMsisdnToAlias= bizaoApiService.getAliasAndToken(msisdn, bizaoConfig);
		 String msisdnAlias=null;
		 if(bizaoMsisdnToAlias!=null&&bizaoMsisdnToAlias.getInfos()!=null&&bizaoMsisdnToAlias.getInfos().size()>0){
			 for(Info info:bizaoMsisdnToAlias.getInfos()){
				 if(info.getKey().equalsIgnoreCase("user_federation_id")){
					 msisdnAlias= info.getValue();
				 }
			 }
		 }		 
		List<SubscriberReg> subscriberRegList=null;
		subscriberRegList=jpaSubscriberReg.
				findSubscriberRegByMsisdnAndStatus(msisdnAlias,MConstants.SUBSCRIBED);
		
		
	    logger.info("subscriberRegList::::::::msisdn: "+msisdnAlias+", "+subscriberRegList);
		modelAndView.addObject("subscriberRegList",subscriberRegList);
		modelAndView.addObject("mapServiceIdToBizaoConfig",BizaoConstant.mapServiceIdToBizaoConfig);
		modelAndView.addObject("msisdnalias",msisdnAlias);
		modelAndView.addObject("unSubUrl",unSubUrl);
		modelAndView.addObject("listMsisdnPrefix", BizaoConstant.listMsisdnPrefix); 
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}		
		modelAndView.setViewName("bizao/unsub_by_msisdn");
	    return modelAndView;		
	}
	
	
	@RequestMapping(value = {"callcenter/agent"}, method = RequestMethod.GET)	
	public ModelAndView promoUser(HttpServletRequest request,HttpServletResponse response,			
			ModelAndView modelAndView,
			@RequestParam(name="msisdnalias",required=false)String msisdnAlias
			) {
		try{
		List<SubscriberReg> subscriberRegList=null;
		subscriberRegList=jpaSubscriberReg.
				findSubscriberRegByMsisdn(msisdnAlias);
		logger.info("subscriberRegList::::::::msisdn: "+msisdnAlias+", "+subscriberRegList);
		modelAndView.addObject("subscriberRegList",subscriberRegList);
		modelAndView.addObject("mapServiceIdToBizaoConfig",BizaoConstant.mapServiceIdToBizaoConfig);
		modelAndView.addObject("msisdnalias",msisdnAlias);
		modelAndView.addObject("unSubUrl",unSubUrl);
		}catch(Exception ex){
			logger.error("Exception ",ex);
		}
		
		modelAndView.setViewName("bizao/callcenter_unsub");
	    return modelAndView;		
	}
	
	
	@RequestMapping(value = {"send/renewal"}, method = RequestMethod.GET)	
	@ResponseBody
	public String sendRenewalRequest(){
		bizaoScheduler.sendRenewalBilled();
		return "OK:"+System.currentTimeMillis();
	}
	
	@RequestMapping(value = {"check/dupcache"}, method = RequestMethod.GET)	
	@ResponseBody
	public String checkCacheDuplicatePaymentKey(HttpServletRequest request){//testing
		String key=request.getParameter("key");
		Long value=redisCacheService.getObjectCacheValueByEvictionSecond(BizaoConstant.BIZAO_PAYMENT_TRX+
				key, 1l, 15);
		
		boolean duplicate=false;
		if(value!=null&&value>1l){
			duplicate=true;	
		}
		return "key:: "+key+" , value:: "+value+" , dupilcate:: "+duplicate;
	}
	
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ModelAndView error(HttpServletRequest request,Exception ex){
		logger.error("error:: query  string: "+request.getQueryString()+", Exception:: ",ex);
			return new ModelAndView(new RedirectView(defaultPortalUrl));
	}
	
	
	@RequestMapping(value = {"pubpage"}, method = RequestMethod.GET)		
	public ModelAndView pubPage(HttpServletRequest request,ModelAndView modelAndView){//testing
		//http://192.241.253.234/ccsub/cnt/cmp?adid=1&cmpid=144&token=dfs
		try{
		logger.info("pubPage::::: "+request.getQueryString());
		String adid=request.getParameter("adid");
		String cmpid=request.getParameter("cmpid");
		String token=request.getParameter("token");
		modelAndView.addObject("adid",adid);
		modelAndView.addObject("cmpid",cmpid);
		modelAndView.addObject("token",token);
		VWServiceCampaignDetail vwServiceCampaignDetail=
				MData.mapCamapignIdToVWServiceCampaignDetail.get(MUtility.toInt(cmpid, 297));
		BizaoConfig bizaoConfig=	BizaoConstant.mapServiceIdToBizaoConfig.get(vwServiceCampaignDetail.getServiceId());
		modelAndView.addObject("bizaoConfig",bizaoConfig);
		modelAndView.setViewName("bizao/pub_page");
		}catch(Exception ex){
			logger.error("pubPage ",ex);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = {"test/final/page"}, method = RequestMethod.GET)		
	public ModelAndView testFinalPage(HttpServletRequest request,ModelAndView modelAndView){//testing
		//http://192.241.253.234/ccsub/cnt/cmp?adid=1&cmpid=144&token=dfs
		try{
			 modelAndView.clear();
			 modelAndView.setViewName("bizao/final_page");
			 //Add new page
			 Integer subId=MUtility.toInt(request.getParameter("subid"),0);
			 SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegById(subId);
			 
			 BizaoConfig bizaoConfig= BizaoConstant.mapServiceIdToBizaoConfig.get(subscriberReg.getServiceId());
			 
			 modelAndView.addObject("portalurl",BizaoConstant.getPortalUrl(bizaoConfig.getPortalUrl()
			    		,subscriberReg.getSubscriberId(),"test"));
			 modelAndView.addObject("unsuburl",unsubCustomerCare);
			 
			 
		}catch(Exception ex){
			logger.error("pubPage ",ex);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = {"fraud"}, method = RequestMethod.GET)		
	public ModelAndView fraud(HttpServletRequest request,ModelAndView modelAndView){//testing
		logger.info("fraud::::: "+request.getQueryString());
		modelAndView.setView(new RedirectView(defaultPortalUrl));
		return modelAndView;
	}	
}

