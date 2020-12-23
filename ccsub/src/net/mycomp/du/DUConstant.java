package net.mycomp.du;

import java.util.HashMap;
import java.util.Map;

import net.util.MConstants;

public interface DUConstant {

	public static Map<Integer,DUConfig> mapServiceIdToDuConfig=new HashMap<Integer,DUConfig>();
	//public static Map<String,DUConfig> mapProductIdToDuConfig=new HashMap<String,DUConfig>();
	public static Map<String,DUConfig> mapPlanIdToDuConfig=new HashMap<String,DUConfig>();
	
	public static final String SUCCESS_CG_CALLBACK="SUCCESS";
	public static final String FAIL_CG_CALLBACK="FAIL";
	public static final String REJECT_CG_CALLBACK="REJECT";
	public static String COLLECTCENT="COLLECTCENT";
	public static String CG_CALLBACK_REASON_ALREADY_SUBSCRIBED="User_is_already_subscribed";
	public static String OTP_MT="OTP_MT";
	/*
	 * action	OperationId s
		Success  for  new request	SN,SR,RN, 
		Success  for  Renewal request	YR, GR, RR
		 subsciber Deactivation	SS
		System churn	YS,GS,RS
		Block user from CC tool	SH
		Un-block user from CC tool	SA

	 */
	public static String getSubscriberStats(String operationId){
		
		String action="";
		switch(operationId){
		case "SN":
		case "SR":
		case "RN":
			action=MConstants.ACT;
		break;
		case "YR":
		case "GR":
		case "RR":
			action=MConstants.RENEW;
		break;
		case "SS":
			action=MConstants.DCT;
		break;
		case "YS":
		case "GS":
		case "RS":
			action=MConstants.CHURN;
		break;
		case "SH":
			action=MConstants.BLOCKED;
		break;
		case "SA":
			action=MConstants.UNBLOCKED;
		break;
		}
		return action;
	} 

}
