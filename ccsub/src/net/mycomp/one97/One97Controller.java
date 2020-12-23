package net.mycomp.one97;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import net.common.service.IDaoService;
import net.common.service.RedisCacheService;
import net.util.HTTPResponse;
import net.util.HttpURLConnectionUtil;
import net.util.JsonMapper;
import net.util.MUtility;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("inapp")
public class One97Controller {

	
	 private static final Logger logger = Logger
				.getLogger(One97Controller.class.getName());
	 
	private final String pinSendUrl;
	
	
	private final String pinValidateUrl;
	
	
	private final String statusCheckUrl;
	
	@Autowired
	private IDaoService daoService;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	private HttpURLConnectionUtil httpURLConnectionUtil;
	
	@Autowired
	public One97Controller(@Value("${inapp.one97.pin.send.url}")String pinSendUrl,
			@Value("${inapp.one97.pin.validate.url}") String pinValidateUrl,
			@Value("${inapp.one97.status.check.url}")String statusCheckUrl){
		this.pinSendUrl=pinSendUrl;
		this.pinValidateUrl=pinValidateUrl;
		this.statusCheckUrl=statusCheckUrl;
		httpURLConnectionUtil=new HttpURLConnectionUtil();
	}
	
	@RequestMapping("sendpin")
	@ResponseBody
	public Map<String,String> sendPin(HttpServletRequest request,ModelAndView modelAndView){
		
		Map<String,String> responseMap=new HashMap<String,String>();
		One97OtpPinSend one97OtpPinSend=new One97OtpPinSend(true);
		boolean success=false;
		try{
		one97OtpPinSend.setMsisdn(request.getParameter("msisdn"));
		Integer cmpid=MUtility.toInt(request.getParameter("cmpid"),1);
		one97OtpPinSend.setTransationId(request.getParameter("tid"));
		one97OtpPinSend.setRequestQueryStr(request.getQueryString());		
		One97Config one97Config=One97Constant.mapIdToOne97Config.get(cmpid);		
		String url=One97Constant.getUrl(pinSendUrl,""+one97OtpPinSend.getId() ,one97OtpPinSend.getMsisdn()
				, one97Config, null, null);
		one97OtpPinSend.setSendOtpUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		one97OtpPinSend.setSendOtpResp(httpResponse.getResponseCode()+" : "+httpResponse.getResponseStr());
		redisCacheService.putObjectCacheValueByEvictionMinute
		(One97Constant.ONE97_OTP_PREFIX+one97OtpPinSend.getMsisdn(), httpResponse.getResponseStr(), 10);
		if((httpResponse.getResponseCode()==200&&httpResponse.getResponseStr().contains("pin_sent"))||success){//pin_sent
			String arr[]=httpResponse.getResponseStr().split("\\|");
			redisCacheService.putObjectCacheValueByEvictionMinute
			(One97Constant.ONE97_OTP_PREFIX+one97OtpPinSend.getMsisdn(),arr[1], 30);
		responseMap.put("STATUS", "SUCCESS");
		responseMap.put("MSG", "OTP Sent...");
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "OTP Not Sent...");
		}
		}catch(Exception ex){
			logger.error("sendPin ",ex);
			//one97OtpPinSend.setSendOtpResp(ex.toString());
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "OTP Not Sent...");
		}finally{
			one97OtpPinSend.setResponseToCaller(JsonMapper.getObjectToJson(responseMap));
			daoService.updateObject(one97OtpPinSend);
		}		
		return responseMap;
	} 
	
	@RequestMapping("validatepin")
	@ResponseBody
	public Map<String,String> validatePin(HttpServletRequest request,ModelAndView modelAndView){
		String response="";
		Map<String,String> responseMap=new HashMap<String,String>();
		boolean success=false;
		One97OtpValidation one97OtpValidation=new One97OtpValidation(true);
		try{
		one97OtpValidation.setMsisdn(request.getParameter("msisdn"));
		Integer cmpid=MUtility.toInt(request.getParameter("cmpid"),1);
		one97OtpValidation.setTransationId(request.getParameter("tid"));
		one97OtpValidation.setPin(request.getParameter("pin"));
		one97OtpValidation.setRequestQueryStr(request.getQueryString());		
		One97Config one97Config=One97Constant.mapIdToOne97Config.get(cmpid);
		String token=Objects.toString(redisCacheService.getObjectCacheValue(One97Constant.ONE97_OTP_PREFIX+one97OtpValidation.getMsisdn()));
		String url=One97Constant.getUrl(pinValidateUrl,""+one97OtpValidation.getId() ,one97OtpValidation.getMsisdn()
				, one97Config, one97OtpValidation.getPin(), token);
		one97OtpValidation.setPinValidationUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		one97OtpValidation.setPinValidationResponse(httpResponse.getResponseCode()
				+" : "+httpResponse.getResponseStr());
		String arr[]=One97Constant.parse(httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200&&arr!=null&&arr[0].equalsIgnoreCase("0")&&
				arr[1].contains("306")){
			responseMap.put("STATUS", "SUCCESS");
			responseMap.put("MSG", "OTP verify...");
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "OTP Not verified...");
		}
		
	
		}catch(Exception ex){
			logger.error("sendPin ",ex);
			//one97OtpValidation.setPinValidationResponse(ex.toString());
		
				responseMap.put("STATUS", "FAIL");
				responseMap.put("MSG", "OTP Not verified...");
			
		}finally{
			
			one97OtpValidation.setResponseToCaller(JsonMapper.getObjectToJson(responseMap));
			daoService.updateObject(one97OtpValidation);
			
		}		
		return responseMap;
		
	} 
	
	@RequestMapping("statuscheck")
	@ResponseBody
	public String statusCheck(HttpServletRequest request,ModelAndView modelAndView){
		
		String response="0";
		One97StatusCheck one97StatusCheck=new One97StatusCheck(true);
		try{
			one97StatusCheck.setMsisdn(request.getParameter("msisdn"));
		Integer cmpid=MUtility.toInt(request.getParameter("cmpid"),1);
		
		one97StatusCheck.setQueryStr(request.getQueryString());		
		One97Config one97Config=One97Constant.mapIdToOne97Config.get(cmpid);
		String url=One97Constant.getUrl(statusCheckUrl,null ,one97StatusCheck.getMsisdn()
				, one97Config, null, null);
		one97StatusCheck.setStatusCheckUrl(url);
		HTTPResponse httpResponse=httpURLConnectionUtil.sendGet(url);
		one97StatusCheck.setStatusCheckResp(httpResponse.getResponseCode()
				+" : "+httpResponse.getResponseStr());
		String arr[]=One97Constant.parse(httpResponse.getResponseStr());
		if(httpResponse.getResponseCode()==200&&arr[6].equalsIgnoreCase("ACTIVE")){
		response="1";
		 }
		}catch(Exception ex){
			logger.error("statusCheck ",ex);
			//one97StatusCheck.setStatusCheckResp(ex.toString());
			response="0";
		}finally{
			one97StatusCheck.setResponseToCaller(response);
			daoService.saveObject(one97StatusCheck);
		}		
		return response;
	} 
	
}
