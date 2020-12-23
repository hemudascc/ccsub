package net.mycomp.du;

import net.persist.bean.Otp;
import net.persist.bean.SubscriberReg;
import net.persist.bean.VWServiceCampaignDetail;
import net.util.MConstants;
import net.util.MData;

public class DUFactory {

	public static OCSRequest createDeactivationRequest(SubscriberReg  subscriberReg,DUConfig duConfig){
		
		
		 
		
		OCSRequest ocsRequest=new OCSRequest();
		ocsRequest.setRequestType("1007");
		ocsRequest.setServiceNode(DUConstant.COLLECTCENT);
		ocsRequest.setSequenceNo(System.currentTimeMillis()+MConstants.TOKEN_SEPERATOR+subscriberReg.getMsisdn()+MConstants.TOKEN_SEPERATOR+subscriberReg.getSubscriberId());
		ocsRequest.setCallingParty(subscriberReg.getMsisdn());
		ocsRequest.setServiceType(duConfig.getServiceType());
		ocsRequest.setServiceId(duConfig.getProductId());
		ocsRequest.setBearerId("SMS");
		ocsRequest.setChargeAmount(-1);
		ocsRequest.setPlanId(duConfig.getPlanId());
		ocsRequest.setAsyncFlag("Y");
		ocsRequest.setRenewalFlag("Y");
		ocsRequest.setBundleType("N");
		ocsRequest.setServiceUsage("-1");
		ocsRequest.setPromoId("-1");
		ocsRequest.setSubscriptionFlag("S");
		ocsRequest.setOptionalParameter1("-1");
		ocsRequest.setOptionalParameter2("-1");
		ocsRequest.setOptionalParameter3("-1");
		ocsRequest.setOptionalParameter4("-1");
		ocsRequest.setOptionalParameter5("-1");
		return ocsRequest;
	}
	
	
	
public static OCSRequest createOTPRequest(Otp otp,String msisdn,DUConfig duConfig){
		 
		
		OCSRequest ocsRequest=new OCSRequest();
		ocsRequest.setRequestType("2015");
		ocsRequest.setServiceNode(DUConstant.COLLECTCENT);
		ocsRequest.setSequenceNo(System.currentTimeMillis()+MConstants.TOKEN_SEPERATOR+msisdn+MConstants.TOKEN_SEPERATOR+otp.getOtp());
		ocsRequest.setCallingParty(msisdn);
		ocsRequest.setServiceType(duConfig.getServiceType());
		ocsRequest.setServiceId(duConfig.getProductId());
		ocsRequest.setBearerId("SMS");
		ocsRequest.setChargeAmount(-1);
		ocsRequest.setPlanId(duConfig.getPlanId());
		ocsRequest.setAsyncFlag("Y");
		ocsRequest.setRenewalFlag("Y");
		ocsRequest.setBundleType("N");
		ocsRequest.setServiceUsage("-1");
		ocsRequest.setPromoId("-1");
		ocsRequest.setSubscriptionFlag("S");
		ocsRequest.setOptionalParameter1("msgText#"+otp.getMsg());
		ocsRequest.setOptionalParameter2("reqSource#"+duConfig.getShortcode());
		ocsRequest.setOptionalParameter3("languageId#en");
		ocsRequest.setOptionalParameter4("-1");
		ocsRequest.setOptionalParameter5("-1");
		return ocsRequest;
	}
}
