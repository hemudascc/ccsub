package net.mycomp.common.inapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("inappOperatorService")
public class InappPublisherService implements IInappPublisherService{

	
	@Autowired
	private DefaultInappPublisherService defaultInappPublisherService;
	
	@Autowired
	@Qualifier("collectcentInappPublisherService")
	private CollectcentInappPublisherService collectcentInappPublisherService;
	
private IInappPublisherService findProcessRequest(int adnetworkId){
	IInappPublisherService inappPublisherService=null;
		switch(adnetworkId){
		case InappConstant.INAPP_COLLECENT_ADNETWORK_ID:{
			inappPublisherService=collectcentInappPublisherService;
			break;
		}
		default:{
			inappPublisherService=defaultInappPublisherService;
			break;
		}
		}
		return inappPublisherService;
	}

@Override
public boolean sendOtp(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
	// TODO Auto-generated method stub
	return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getAdNetworkId()).sendOtp(inappProcessRequest, modelAndView);
}

@Override
public boolean otpValidation(InappProcessRequest inappProcessRequest,
		ModelAndView modelAndView) {
	return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getAdNetworkId()).otpValidation(inappProcessRequest, modelAndView);
}

@Override
public boolean statusCheck(InappProcessRequest inappProcessRequest,
		ModelAndView modelAndView) {
	return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getAdNetworkId()).statusCheck(inappProcessRequest, modelAndView);
}

@Override
public String portalUrl(InappProcessRequest inappProcessRequest,
		ModelAndView modelAndView) {
	return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getAdNetworkId()).portalUrl(inappProcessRequest, modelAndView);
}
}
