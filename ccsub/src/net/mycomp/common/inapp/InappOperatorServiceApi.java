package net.mycomp.common.inapp;

import net.mycomp.common.inapp.one97.InappOne97Service;
import net.mycomp.common.inapp.tmt.InappTmtService;
import net.persist.bean.VWServiceCampaignDetail;
import net.util.MConstants;
import net.util.MData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service("inappOperatorServiceApi")
public class InappOperatorServiceApi implements IInappOperatorServiceApi{

	
	@Autowired
	@Qualifier("inappTmtService")
	private InappTmtService inappTmtService;
	
	
	@Autowired
	@Qualifier("inappOne97Service")
	private InappOne97Service inappOne97Service;
	
	
    private IInappOperatorServiceApi findProcessRequest(int opId){
    	IInappOperatorServiceApi inappOperatorServiceApi=null;
    	switch(opId){
    	case MConstants.INAPP_QATAR_OOREDOO_OPERATOR_ID:
    	case MConstants.INAPP_VODAFONE_OPERATOR_ID:{
    		inappOperatorServiceApi=inappTmtService;
    		break;
    	}
    	case MConstants.INAPP_ONE97_OOREDOO_QATAR_OPERATOR_ID:
    	case MConstants.INAPP_ONE97_UAE_ETISALAT_OPERATOR_ID:{
    		inappOperatorServiceApi=inappOne97Service;
    		break;
    	}
    	
    	}
		return inappOperatorServiceApi;
	}

	@Override
	public boolean sendPin(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		//VWServiceCampaignDetail vwServiceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail.get(inappOtpSend.getCmpId());
		return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getOpId())
				.sendPin(inappProcessRequest, modelAndView);
	}

	@Override
	public boolean validatePin(InappProcessRequest inappProcessRequest, ModelAndView modelAndView) {
		
		//VWServiceCampaignDetail vwServiceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail.get(inappProcessRequest.getCmpId());
		return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getOpId())
				.validatePin(inappProcessRequest, modelAndView);

	}

	@Override
	public boolean statusCheck(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		//VWServiceCampaignDetail vwServiceCampaignDetail=MData.mapCamapignIdToVWServiceCampaignDetail.get(inappProcessRequest.getCmpId());
		return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getOpId())
				.statusCheck(inappProcessRequest, modelAndView);

	}

	@Override
	public String portalUrl(InappProcessRequest inappProcessRequest,
			ModelAndView modelAndView) {
		return findProcessRequest(inappProcessRequest.vwserviceCampaignDetail.getOpId())
				.portalUrl(inappProcessRequest, modelAndView);
	}

}
