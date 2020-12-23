package net.mycomp.actel;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.common.jms.JMSService;
import net.common.service.RedisCacheService;
import net.common.service.SubscriberRegService;
import net.jpa.repository.JPASubscriberReg;
import net.persist.bean.LiveReport;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.process.bean.CGToken;
import net.util.JsonMapper;
import net.util.MConstants;
import net.util.MData;
import net.util.MUtility;

@Controller
@RequestMapping("actel")
public class ActelController {

	private static final Logger logger = Logger
			.getLogger(ActelController.class.getName());

	@Autowired
	private JMSActelService jmsActelService;

	@Autowired
	private ActelService actelService;
	
//	@Autowired
//	private ActelApiService actelApiService;
	
	
	
	@Autowired
	private ActelNewApiService actelNewApiService;

	@Autowired
	private JPASubscriberReg jpaSubscriberReg;

	@Autowired
	private SubscriberRegService subscriberRegService;

	@Autowired
	private JMSService jmsService;

	@Autowired
	private RedisCacheService redisCacheService;

	@Value("actel.he.url")
	private String heURL;

	@Value("actel.he.callback.url")
	private String heCallbackURL;

	@RequestMapping(path="dlr",method={RequestMethod.POST,RequestMethod.GET})	
	@ResponseBody
	public String dlr(HttpServletRequest request,ModelAndView modelAndView){
		//http://IP/GetDLR.aspx?id_application={0}
		//&country={1}&operator={2}&opid={3}&msisdn={4}&billedshortcode={5}
		//&freeshortcode={6}&smsid={7}&rate={8}&action={9}&type={10}&status={11}&description={12}&
		//currency={13}&lang={14}&dlrdate={15}&flow={16}&traffic_source={17}

		logger.info("dlr::::::: querystring: "+request.getQueryString());
		ActelDlr actelDlr =new ActelDlr(true);
		try{

			actelDlr.setQueryStr(request.getQueryString());			    
			actelDlr.setIdApplication(request.getParameter("id_application"));
			actelDlr.setCountry(request.getParameter("country"));
			actelDlr.setOperator(request.getParameter("operator"));
			actelDlr.setOpid(request.getParameter("opid"));
			actelDlr.setMsisdn(request.getParameter("msisdn"));
			actelDlr.setBilledShortCode(request.getParameter("billedshortcode"));
			actelDlr.setFreeShortCode(request.getParameter("freeshortcode"));
			actelDlr.setSmsId(request.getParameter("smsid"));
			actelDlr.setRate(request.getParameter("rate"));
			actelDlr.setAction(request.getParameter("action"));
			actelDlr.setType(request.getParameter("type"));
			actelDlr.setDlrStatus(request.getParameter("status"));
			actelDlr.setDescription(request.getParameter("description"));		   
			actelDlr.setCurrency(request.getParameter("currency"));
			actelDlr.setLang(request.getParameter("lang"));
			actelDlr.setDlrdate(request.getParameter("dlrdate"));
			actelDlr.setFlow(request.getParameter("flow"));
			actelDlr.setTrafficSource(request.getParameter("traffic_source"));
			actelDlr.setClickId(request.getParameter("clickid"));
			actelDlr.setHasFreeTrial(request.getParameter("hasfreetrial"));
			actelDlr.setIdBillingRequestType(request.getParameter("id_billing_request_type"));

		}catch(Exception ex){
			logger.error("dlr:: ",ex);
		}finally{
			jmsActelService.saveActelDlr(actelDlr);
		}

		return "0";
	}


	@RequestMapping(path="cgcallback",method={RequestMethod.POST,RequestMethod.GET})	
	@ResponseBody
	public String cgcallback(HttpServletRequest request,ModelAndView modelAndView){
		//http://IP/GetDLR.aspx?id_application={0}
		//&country={1}&operator={2}&opid={3}&msisdn={4}&billedshortcode={5}
		//&freeshortcode={6}&smsid={7}&rate={8}&action={9}&type={10}&status={11}&description={12}&
		//currency={13}&lang={14}&dlrdate={15}&flow={16}&traffic_source={17}

		logger.info("cgcallback::::::: querystring: "+request.getQueryString());

		return "0";
	}



	@RequestMapping(value={"web/send/otp"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView webSendOTP(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){

		try{
			modelAndView.setViewName("actel/msisdn_missing");
			modelAndView.addObject("l",lang);
			logger.info("webSendOTP:::::::: 11 msisdn:: "+request.getParameter("msisdn"));
			
			String token=request.getParameter("token");	
			
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

//			ActelServiceConfig actelServiceConfig=ActelConstant
//					.mapServiceIdToActelServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			
			String msisdn=ActelConstant.formatMsisdn(request.getParameter("msisdn"),actelNewServiceConfig.getMsisdnPrefix());
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token);
			SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwserviceCampaignDetail.getProductId());

			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){

				modelAndView.setView(new RedirectView(ActelConstant.getPortalUrl
						(actelNewServiceConfig.getPortalUrl()
								,msisdn,subscriberReg.getSubscriberId()))); 
			}else{
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("actelServiceConfig",actelNewServiceConfig);

				//ActelApiTrans actelApiTrans=actelApiService.sendOTP(actelServiceConfig, msisdn,token, "WEB");
				ActelApiTrans actelApiTrans=actelNewApiService.sendOTP(actelNewServiceConfig,
						msisdn,token, "WEB");
				if(actelApiTrans.getSuccess()){
					modelAndView.setViewName("actel/wap_otp");
					modelAndView.addObject("otpinfo","We have sent you a PIN code on your phone number");
				}else{
					//modelAndView.setViewName("actel/msisdn_missing");
					modelAndView.addObject("otpinfo","Please enter valid mobile number");
				}

			}

		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return modelAndView;
	}

	@RequestMapping(value={"web/send/otp/validation"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView sendOTPValidation(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){
		try{
			modelAndView.setViewName("actel/wap_otp");
			modelAndView.addObject("l",lang);
			
			String token=request.getParameter("token");
			String pin=request.getParameter("pin"); 
			String ip=request.getRemoteAddr();
			
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

//			ActelServiceConfig actelServiceConfig=ActelConstant
//					.mapServiceIdToActelServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			String msisdn=ActelConstant.formatMsisdn(request.getParameter("msisdn"),actelNewServiceConfig.getMsisdnPrefix());
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token+", pin:: "+pin);
			SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwserviceCampaignDetail.getProductId());

			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){

				modelAndView.setView(new RedirectView(ActelConstant.getPortalUrl
						(actelNewServiceConfig.getPortalUrl()
								,msisdn,subscriberReg.getSubscriberId()))); 
			}else{
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("actelServiceConfig",actelNewServiceConfig);
				modelAndView.setViewName("actel/wap_otp");
				ActelApiTrans actelApiTrans=actelNewApiService.validateOTP(actelNewServiceConfig, msisdn,token, "WEB", pin, ip);

				if(actelApiTrans.getSuccess()){
					//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
					LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
							new Timestamp(System.currentTimeMillis()),
							cgToken.getCampaignId()
							,vwserviceCampaignDetail.getServiceId(),
							vwserviceCampaignDetail.getProductId());
					liveReport.setNoOfDays(actelNewServiceConfig.getFreePeriodDays());						   
					subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
					modelAndView.addObject("portalurl",actelNewServiceConfig.getPortalUrl()
							+"?msisdn="+msisdn);
					modelAndView.setViewName("actel/final");

				}else{
					modelAndView.addObject("otpinfo","Please enter valid pin");
				}

			}

		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return modelAndView;

	}


	@RequestMapping(value={"chnage/lang"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView changeLang(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){
		try{
			String page=request.getParameter("page");

			modelAndView.setViewName("actel/"+page);
			modelAndView.addObject("l",lang);
			
			String token=request.getParameter("token");
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			String msisdn=ActelConstant.formatMsisdn(request.getParameter("msisdn"),actelNewServiceConfig.getMsisdnPrefix());
			String pin=request.getParameter("pin"); 
			modelAndView.addObject("token",token);
			modelAndView.addObject("msisdn",msisdn);
			modelAndView.addObject("actelServiceConfig",actelNewServiceConfig);

		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return modelAndView;

	}


	@RequestMapping(path="send/otp",method={RequestMethod.POST,RequestMethod.GET})	
	@ResponseBody
	public String sendOTP(HttpServletRequest request,ModelAndView modelAndView){
		//http://IP/GetDLR.aspx?id_application={0}
		try{
			logger.info("sendOTP querystr:: "+request.getQueryString());
			String msisdn=request.getParameter("msisdn");
			String mode=request.getParameter("mode");
			Integer cmpid=MUtility.toInt(request.getParameter("cmpid"),0);
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cmpid);
			logger.info("sendOTP querystr:: "+request.getQueryString());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant.mapServiceIdToActelNewServiceConfig
					.get(vwServiceCampaignDetail.getServiceId());

			//return actelApiService.sendOTP(actelServiceConfig, msisdn,null, mode).getSuccess()?"1":"0";
			return actelNewApiService.sendOTP(actelNewServiceConfig, msisdn,null, mode).getSuccess()?"1":"0";
		}catch(Exception ex){
			logger.error("sendOTP:: ",ex);
		}
		return "0";
	}

	@RequestMapping(path="validate/otp",method={RequestMethod.POST,RequestMethod.GET})	
	@ResponseBody
	public String validateOTP(HttpServletRequest request,ModelAndView modelAndView){
		try{
			logger.info("validateOTP querystr:: "+request.getQueryString());
			
			String mode=request.getParameter("mode");
			String ip=request.getRemoteAddr();
			String pin=request.getParameter("pin");
			Integer cmpid=MUtility.toInt(request.getParameter("cmpid"),0);
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cmpid);
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			String msisdn=ActelConstant.formatMsisdn(request.getParameter("msisdn"),
					actelNewServiceConfig.getMsisdnPrefix());
			
			SubscriberReg subscriberReg=jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					ActelConstant.formatMsisdn(msisdn
							, actelNewServiceConfig.getMsisdnPrefix()),
							vwServiceCampaignDetail.getProductId());
			if(subscriberReg==null||subscriberReg.getStatus()!=MConstants.SUBSCRIBED){
				
				ActelApiTrans actelApiTrans=actelNewApiService.validateOTP(actelNewServiceConfig, msisdn,null, mode,pin,ip);
			if(actelApiTrans.getSuccess()){
				LiveReport liveReport=new LiveReport(vwServiceCampaignDetail.getOpId(),
						new Timestamp(System.currentTimeMillis()),
						cmpid
						,vwServiceCampaignDetail.getServiceId(),
						vwServiceCampaignDetail.getProductId());
				liveReport.setNoOfDays(actelNewServiceConfig.getFreePeriodDays());						   
				subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
			
				return "1";
			}
		 }
			//return actelNewApiService.validateOTP(actelNewServiceConfig, msisdn,null, mode,pin,ip).getSuccess()?"1":"0";
		}catch(Exception ex){
			logger.error("sendOTP:: ",ex);
		}
		return "0";
	}



	@RequestMapping("/he/callback/{token}")
	public ModelAndView heCallbackHandler(HttpServletRequest request, ModelAndView modelAndView,
			@PathVariable(value = "token")String token) {
		ActelHECallback actelHECallback = null;
		try{
			logger.info("actel he callback request:"+request.getQueryString());
			String msisdn=request.getParameter("msisdn").replaceAll("nomsisdn", "");
			String country=request.getParameter("country");
			String operatorid=request.getParameter("operatorid");
			String operator=request.getParameter("operator"); 
			String queryStr = request.getQueryString();
			actelHECallback = new ActelHECallback(msisdn, country, operatorid, operator, token, queryStr);

			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			modelAndView.addObject("msisdn", msisdn);
			modelAndView.addObject("token", token);
			modelAndView.addObject("actelServiceConfig", actelNewServiceConfig);
			modelAndView.addObject("l", 0);
			modelAndView.setViewName("actel/prelanding");
		}catch (Exception e) {
			logger.error("HE Callback Error"+e);
		}finally {
			jmsService.saveObject(actelHECallback);
		}

		return modelAndView;
	}


	@RequestMapping(value="tocg")
	public ModelAndView toCg(ModelAndView modelAndView, HttpServletRequest request) {
		String token = request.getParameter("token");
		String lang = request.getParameter("l");
		String serviceId = request.getParameter("serviceId");
		String msisdn = request.getParameter("msisdn");
		System.out.println("Token:"+token+" lang:"+lang+" serviceId:"+serviceId);
		CGToken cgToken = new CGToken(token);	
		VWServiceCampaignDetail vwServiceCampaignDetail = MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
		ActelNewServiceConfig actelNewServiceConfig = ActelConstant
				.mapServiceIdToActelNewServiceConfig.get(vwServiceCampaignDetail.getServiceId());
		//if("99".equals(serviceId)) {
		String cgURL = actelNewServiceConfig.getCgUrl()
				.replaceAll("<application_id>", actelNewServiceConfig.getIdApplication())
				.replaceAll("<image_url>",actelNewServiceConfig.getLpImages())
				.replaceAll("<token>", token)
				.replaceAll("<msisdn>", msisdn);
		logger.info("redirecting to cg url: "+cgURL);

		redisCacheService.putObjectCacheValueByEvictionMinute(ActelConstant.ACTEL_DU_MSISDN_CACHE_PREFIX+token, msisdn, 2000);

		modelAndView.setView(new RedirectView(cgURL));
		//		}
		//		else {
		//			modelAndView.addObject("token", token);
		//			modelAndView.addObject("actelServiceConfig", actelServiceConfig);
		//			modelAndView.addObject("l", lang);
		//			modelAndView.setViewName("actel/msisdn_missing");
		//		}

		return modelAndView;
	}

	@RequestMapping(value="du/cgcallback")
	public ModelAndView duCGCallback(ModelAndView modelAndView, HttpServletRequest request) {
		//http://%3Cnotificationurlsharedbythepartner%3E/?correlatorId=ABCD1234&statusCode=1
		logger.info("du CG Callback queryString : "+ request.getQueryString());
		String token = request.getParameter("correlatorId");
		String status = request.getParameter("statusCode");
		CGToken cgToken=new CGToken(token);
		VWServiceCampaignDetail vwserviceCampaignDetail=MData
				.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

		ActelNewServiceConfig actelNewServiceConfig=ActelConstant
				.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());

		String msisdn = (String)redisCacheService.getObjectCacheValue(ActelConstant.ACTEL_DU_MSISDN_CACHE_PREFIX+token);
		if(!StringUtils.isEmpty(msisdn)) {
			SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwserviceCampaignDetail.getProductId());

			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){

				modelAndView.setView(new RedirectView(ActelConstant.getPortalUrl
						(actelNewServiceConfig.getPortalUrl()
								,msisdn,subscriberReg.getSubscriberId()))); 
			}else{
				if("1".equals(status)){
					LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
							new Timestamp(System.currentTimeMillis()),
							cgToken.getCampaignId()
							,vwserviceCampaignDetail.getServiceId(),
							vwserviceCampaignDetail.getProductId());
					liveReport.setNoOfDays(actelNewServiceConfig.getFreePeriodDays());						   
					subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
					modelAndView.addObject("portalurl",actelNewServiceConfig.getPortalUrl()
							+"?msisdn="+msisdn);
					modelAndView.setViewName("actel/final");
				}else{
					modelAndView.setViewName("actel/prelanding");
				}
			}
		}

		return modelAndView;

	}

	@RequestMapping(value="du/dlr",method=RequestMethod.POST)
	@ResponseBody 
	public ResponseEntity<?> duDLR(HttpServletRequest request){

		ActelDuDlr actelDuDLR=new ActelDuDlr(true);
		Map<String, String> responseMap = new HashMap<>(); 
		try { 
			logger.info(actelDuDLR);
			//{"code":"1","description":"Success","correlator":"XXXXXXXX","msisdn":"9715XXXXXXXXX",
			//"otp":{"status":"1","description":"Success "},"subscription":{"msisdn":"9715XXXXXXXXX ",
			//"opId":"268","responseStatusCode":"1","responseStatusdescription":"success","serviceId":"XXXXX",
			//"productId":"XXXX","clubId":"XXXXX","subid":"XXXXXXXXX","subStatus":"ACTIVE"}} 
			String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			//String json =""; logger.info("json: "+request); 
			Map requestMap=JsonMapper.getJsonToObject(json, Map.class);
			actelDuDLR.setCode(Objects.toString(requestMap.get("code")));
			actelDuDLR.setDescription(Objects.toString(requestMap.get("description")));
			actelDuDLR.setCorrelator(Objects.toString(requestMap.get("correlator")));
			actelDuDLR.setMsisdn(Objects.toString(requestMap.get("msisdn"))); Map
			otpMap=(Map)requestMap.get("otp");
			actelDuDLR.setOtpStatus(Objects.toString(otpMap.get("status")));
			actelDuDLR.setOtpDescription(Objects.toString(requestMap.get("description")));
			Map subscriptionMap=(Map)requestMap.get("subscription");
			actelDuDLR.setOpId(Objects.toString(subscriptionMap.get("opId")));
			actelDuDLR.setResponseStatusCode(Objects.toString(subscriptionMap.get("responseStatusCode")));
			actelDuDLR.setResponseStatusDescription(Objects.toString(subscriptionMap.get("responseStatusdescription")));
			actelDuDLR.setServiceId(Objects.toString(subscriptionMap.get("serviceId")));
			actelDuDLR.setProductId(Objects.toString(subscriptionMap.get("productId")));
			actelDuDLR.setClubId(Objects.toString(subscriptionMap.get("clubId")));
			actelDuDLR.setSubId(Objects.toString(subscriptionMap.get("subid")));
			actelDuDLR.setSubStatus(Objects.toString(subscriptionMap.get("subStatus")));

			responseMap.put("correlatorId", actelDuDLR.getCorrelator());
			responseMap.put("responseStatus", "1"); }
		catch(Exception ex) {
			logger.error("duDLR :: ",ex); 
		}finally{
			jmsActelService.saveActelDuDlr(actelDuDLR); 
		} 
		return new ResponseEntity<>(responseMap, HttpStatus.OK); 
	}
	
	@RequestMapping("prelanding/{token}")
	public ModelAndView preLandingPage(ModelAndView modelAndView, @PathVariable(value = "token")String token) {
		modelAndView.addObject("token", token);
		modelAndView.addObject("l", 0);
		modelAndView.setViewName("actel/prelanding");		
		return modelAndView;
	}
	//////////////////////Ooredo Qatar Start/////////////////////////////
	
	@RequestMapping("/ooredoo/he/callback/{token}")
	public ModelAndView heCallbackOoredoQatarHandler(HttpServletRequest request, ModelAndView modelAndView,
			@PathVariable(value = "token")String token) {
		ActelHECallback actelHECallback = null;
		try{
			logger.info("actel he callback request:"+request.getQueryString());
			String msisdn=request.getParameter("msisdn").replaceAll("nomsisdn", "");
			String country=request.getParameter("country");
			String operatorid=request.getParameter("operatorid");
			String operator=request.getParameter("operator"); 
			String queryStr = request.getQueryString();
			actelHECallback = new ActelHECallback(msisdn, country, operatorid, operator, token, queryStr);

			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwServiceCampaignDetail=
					MData.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwServiceCampaignDetail.getServiceId());
			modelAndView.addObject("msisdn", msisdn);
			modelAndView.addObject("token", token);
			modelAndView.addObject("actelServiceConfig", actelNewServiceConfig);
			modelAndView.addObject("l", 0);
			modelAndView.setViewName("actel/ooredooqatar_msisdn");
		}catch (Exception e) {
			logger.error("HE Callback Error"+e);
		}finally {
			jmsService.saveObject(actelHECallback);
		}

		return modelAndView;
	}

	@RequestMapping(value={"ooredoo/web/send/otp"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView webSendOTPOoredoo(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){

		try{
			modelAndView.setViewName("actel/ooredooqatar_msisdn");
			modelAndView.addObject("l",lang);
			String token=request.getParameter("token");	
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
           
//			ActelServiceConfig actelServiceConfig=ActelConstant
//					.mapServiceIdToActelServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			
			logger.info("webSendOTP:::::::: 11 msisdn:: "+request.getParameter("msisdn"));
			String msisdn=ActelConstant.ooredoFormatMsisdn(request.getParameter("msisdn")
					,actelNewServiceConfig.getMsisdnPrefix());
			
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token);
			

			SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwserviceCampaignDetail.getProductId());

			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){

				modelAndView.setView(new RedirectView(ActelConstant.getPortalUrl
						(actelNewServiceConfig.getPortalUrl()
								,msisdn,subscriberReg.getSubscriberId()))); 
			}else{
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("actelServiceConfig",actelNewServiceConfig);

				//ActelApiTrans actelApiTrans=actelApiService.ooredoSendOTP(actelServiceConfig, msisdn,token, "WEB");
				ActelApiTrans actelApiTrans=actelNewApiService.sendOTP(actelNewServiceConfig, msisdn, token, "WEB");
						//ooredoSendOTP(actelServiceConfig, msisdn,token, "WEB");
				if(actelApiTrans.getSuccess()){
					modelAndView.setViewName("actel/ooredooqatar_wap_otp");
					modelAndView.addObject("otpinfo","We have sent you a PIN code on your phone number");
				}else{
					//modelAndView.setViewName("actel/msisdn_missing");
					modelAndView.addObject("otpinfo","Please enter valid mobile number");
				}

			}

		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}

		return modelAndView;
	}

	@RequestMapping(value={"ooredoo/web/send/otp/validation"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView ooredooSendOTPValidation(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){
		try{
			modelAndView.setViewName("actel/ooredooqatar_wap_otp");
			modelAndView.addObject("l",lang);
			
			String token=request.getParameter("token");
			String pin=request.getParameter("pin"); 
			String ip=request.getRemoteAddr();
			
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

//			ActelServiceConfig actelServiceConfig=ActelConstant
//					.mapServiceIdToActelServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			String msisdn=ActelConstant.formatMsisdn(request.getParameter("msisdn"),actelNewServiceConfig.getMsisdnPrefix());
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token+", pin:: "+pin);
			SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwserviceCampaignDetail.getProductId());

			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){

				modelAndView.setView(new RedirectView(ActelConstant.getPortalUrl
						(actelNewServiceConfig.getPortalUrl()
								,msisdn,subscriberReg.getSubscriberId()))); 
			}else{
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("actelServiceConfig",actelNewServiceConfig);
				modelAndView.setViewName("actel/ooredooqatar_wap_otp");
				//ActelApiTrans actelApiTrans=actelApiService.ooredooValidateOTP(actelServiceConfig, msisdn,token, "WEB", pin, ip);
				ActelApiTrans actelApiTrans=actelNewApiService.validateOTP(actelNewServiceConfig, msisdn, token, "WEB", pin, ip);
						
				if(actelApiTrans.getSuccess()){
					//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
					LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
							new Timestamp(System.currentTimeMillis()),
							cgToken.getCampaignId()
							,vwserviceCampaignDetail.getServiceId(),
							vwserviceCampaignDetail.getProductId());
					liveReport.setNoOfDays(actelNewServiceConfig.getFreePeriodDays());						   
					 subscriberReg=subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
					modelAndView.addObject("portalurl",ActelConstant.getPortalUrl
							(actelNewServiceConfig.getPortalUrl()
									,msisdn,subscriberReg.getSubscriberId()));
					modelAndView.setViewName("actel/ooredo_qatar_final");

				}else{
					modelAndView.addObject("otpinfo","Please enter valid pin");
				}

			}

		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return modelAndView;

	}
    ////////////////////Ooredo Qatar End/////////////////////////////
	
	//////////////////Vodafone Qatar Start/////////////////////////////////
	
	@RequestMapping(value={"vodafone/web/send/otp"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView webSendVodafoneOTPOoredoo(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){

		try{
			modelAndView.setViewName("actel/vodafone_qatar_wap_otp");
			modelAndView.addObject("l",lang);
			String token=request.getParameter("token");	
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());
           
//			ActelServiceConfig actelServiceConfig=ActelConstant
//					.mapServiceIdToActelServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			
			logger.info("webSendOTP:::::::: 11 msisdn:: "+request.getParameter("msisdn"));
			String msisdn=ActelConstant.formatMsisdn(request.getParameter("msisdn")
					,actelNewServiceConfig.getMsisdnPrefix());
			
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token);
			

			SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwserviceCampaignDetail.getProductId());

			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){

				modelAndView.setView(new RedirectView(ActelConstant.getPortalUrl
						(actelNewServiceConfig.getPortalUrl()
								,msisdn,subscriberReg.getSubscriberId()))); 
			}else{
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("actelServiceConfig",actelNewServiceConfig);

				//ActelApiTrans actelApiTrans=actelApiService.ooredoSendOTP(actelServiceConfig, msisdn,token, "WEB");
				ActelApiTrans actelApiTrans=actelNewApiService.sendOTP(actelNewServiceConfig, msisdn, token, "WEB");
						//ooredoSendOTP(actelServiceConfig, msisdn,token, "WEB");
				if(actelApiTrans.getSuccess()){
					modelAndView.setViewName("actel/vodafone_qatar_wap_otp");
					modelAndView.addObject("otpinfo","We have sent you a PIN code on your phone number");
				}else{
					//modelAndView.setViewName("actel/msisdn_missing");
					modelAndView.addObject("otpinfo","Please enter valid mobile number");
				}

			}

		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}

		return modelAndView;
	}

	@RequestMapping(value={"vodafone/web/send/otp/validation"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView ooredooSendVodafoneOTPValidation(HttpServletRequest request,ModelAndView modelAndView,
			@RequestParam(name="l",defaultValue="0")Integer lang){
		try{
			modelAndView.setViewName("actel/vodafone_qatar_wap_otp");
			modelAndView.addObject("l",lang);
			
			String token=request.getParameter("token");
			String pin=request.getParameter("pin"); 
			String ip=request.getRemoteAddr();
			
			CGToken cgToken=new CGToken(token);
			VWServiceCampaignDetail vwserviceCampaignDetail=MData
					.mapCamapignIdToVWServiceCampaignDetail.get(cgToken.getCampaignId());

//			ActelServiceConfig actelServiceConfig=ActelConstant
//					.mapServiceIdToActelServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(vwserviceCampaignDetail.getServiceId());
			String msisdn=ActelConstant.formatMsisdn(request.getParameter("msisdn"),actelNewServiceConfig.getMsisdnPrefix());
			logger.info("webSendOTP:::::::: msisdn:: "+msisdn+", token:: "+token+", pin:: "+pin);
			SubscriberReg subscriberReg=  jpaSubscriberReg.findSubscriberRegByMsisdnAndProductId(
					msisdn, 
					vwserviceCampaignDetail.getProductId());

			if(subscriberReg!=null&&subscriberReg.getStatus()==MConstants.SUBSCRIBED){

				modelAndView.setView(new RedirectView(ActelConstant.getPortalUrl
						(actelNewServiceConfig.getPortalUrl()
								,msisdn,subscriberReg.getSubscriberId()))); 
			}else{
				modelAndView.addObject("token",token);
				modelAndView.addObject("msisdn",msisdn);
				modelAndView.addObject("actelServiceConfig",actelNewServiceConfig);
				modelAndView.setViewName("actel/vodafone_qatar_final");
				//ActelApiTrans actelApiTrans=actelApiService.ooredooValidateOTP(actelServiceConfig, msisdn,token, "WEB", pin, ip);
				ActelApiTrans actelApiTrans=actelNewApiService.validateOTP(actelNewServiceConfig, msisdn, token, "WEB", pin, ip);
						
				if(actelApiTrans.getSuccess()){
					//modelAndView.addObject("otpinfo","We sent you a PIN code on your phone number");
					LiveReport liveReport=new LiveReport(vwserviceCampaignDetail.getOpId(),
							new Timestamp(System.currentTimeMillis()),
							cgToken.getCampaignId()
							,vwserviceCampaignDetail.getServiceId(),
							vwserviceCampaignDetail.getProductId());
					liveReport.setNoOfDays(actelNewServiceConfig.getFreePeriodDays());						   
					 subscriberReg=subscriberRegService.findOrCreateSubscriberByAct(msisdn,null, liveReport);
					modelAndView.addObject("portalurl",ActelConstant.getPortalUrl
							(actelNewServiceConfig.getPortalUrl()
									,msisdn,subscriberReg.getSubscriberId()));
					modelAndView.setViewName("actel/vodafone_qatar_final");

				}else{
					modelAndView.addObject("otpinfo","Please enter valid pin");
				}

			}

		}catch(Exception  ex){
			logger.error("Exception" ,ex);
		}
		return modelAndView;


	}
	/////////////////Vodafone Qatar End////////////////////////////////////
	
	
	@RequestMapping(value={"reload/config"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String reloadConfig(HttpServletRequest request,ModelAndView modelAndView
			){
		
		try{
			actelService.init();
		}catch(Exception ex){
		logger.error("Excption ",ex);
		}
		return "ok";
       }
	
	@RequestMapping(value={"check/sub/status"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ActelApiTrans checkSubcriptionStatus(HttpServletRequest request,ModelAndView modelAndView
			){
		ActelApiTrans actelApiTrans=null;
		try{
			String msisdn=request.getParameter("msisdn");
			Integer serviceId=MUtility.toInt(request.getParameter("serviceid"),0);
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(serviceId);
			 actelApiTrans=actelNewApiService.checkSubscriptionStatus(actelNewServiceConfig, msisdn, "test");
		}catch(Exception ex){
		logger.error("Excption ",ex);
		}
		return actelApiTrans;
       }
	
	@RequestMapping(value={"/new/send/otp"},method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ActelApiTrans newApiSendOTP(HttpServletRequest request,ModelAndView modelAndView
			){
		ActelApiTrans actelApiTrans=null;
		try{
			String msisdn=request.getParameter("msisdn");
			Integer serviceId=MUtility.toInt(request.getParameter("serviceid"),0);
			ActelNewServiceConfig actelNewServiceConfig=ActelConstant
					.mapServiceIdToActelNewServiceConfig.get(serviceId);
			 actelApiTrans=actelNewApiService.sendOTP(actelNewServiceConfig, msisdn, "test","WAP");
		}catch(Exception ex){
		logger.error("Excption ",ex);
		}
		return actelApiTrans;
       }
	
}

