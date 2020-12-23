package net.mycomp.common.inapp;

import java.util.HashMap;
import java.util.Map;

import net.adnetwork.callback.service.AdnetworkCallbackService;
import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.VWServiceCampaignDetail;
import net.util.JsonMapper;
import net.util.MData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Service("collectcentInappPublisherService")
public class CollectcentInappPublisherService implements IInappPublisherService{

	@Autowired
	private InappOperatorServiceApi inappOperatorServiceApi;
	
	@Autowired
	private AdnetworkCallbackService adnetworkCallbackService;
	
	@Override
	public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
          
		 inappOperatorServiceApi.sendPin(inappProcessRequest, modelAndView);
		 Map<String,String> responseMap=new HashMap<String,String>();
		 
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
			responseMap.put("STATUS", "SUCCESS");
			responseMap.put("MSG", "OTP Sent...");
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "OTP Not Sent...");
			inappProcessRequest.setResponseObject("0");
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		
		return true;
	}

	@Override
	public boolean otpValidation(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		inappOperatorServiceApi.validatePin(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			
			AdnetworkOperatorConfig adnetworkOperatorConfig = MData.mapAdnetworkOpConfig
					.get(inappProcessRequest.vwserviceCampaignDetail.getOpId())
					.get(inappProcessRequest.vwserviceCampaignDetail.getAdNetworkId());		
			
			if(adnetworkCallbackService.
			isSendActMoreThanZeroPricePointAdnetworkCallBack(null,adnetworkOperatorConfig)){
				//inappProcessRequest.setResponseObject("1");
				inappProcessRequest.setConversionSendToAdenetwork(true);
				responseMap.put("STATUS", "SUCCESS");
				responseMap.put("MSG", "OTP verify...");
			}else{
				responseMap.put("STATUS", "FAIL");
				responseMap.put("MSG", "OTP Not verified...");
				//inappProcessRequest.setResponseObject("0");
			}
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "OTP Not verified...");
			//inappProcessRequest.setResponseObject("0");
		}
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		return true;
	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		
		inappOperatorServiceApi.statusCheck(inappProcessRequest, modelAndView);
		if(inappProcessRequest.isSuccess()){
			inappProcessRequest.setResponseObject("1");
		}else{
			inappProcessRequest.setResponseObject("0");
		}
		
		return true;
	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		Map<String,String> responseMap=new HashMap<String,String>();
		String url=inappOperatorServiceApi.portalUrl(inappProcessRequest, modelAndView);
		if(url!=null){
			responseMap.put("STATUS", "SUCCESS");
			responseMap.put("MSG", url);
		}else{
			responseMap.put("STATUS", "FAIL");
			responseMap.put("MSG", "In Active User");
		}		
		inappProcessRequest.setResponseObject(JsonMapper.getObjectToJson(responseMap));
		modelAndView.setView(new RedirectView(url));
		return url;
	}

	

}
