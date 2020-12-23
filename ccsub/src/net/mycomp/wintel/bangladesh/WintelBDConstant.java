package net.mycomp.wintel.bangladesh;

import java.util.HashMap;
import java.util.Map;

public interface WintelBDConstant {

	public static Map<Integer,WintelBDServiceConfig> mapServiceIdToWintelBDServiceConfig
	=new HashMap<Integer,WintelBDServiceConfig>();
	
	public static Map<String,WintelBDServiceConfig> mapOperatorToWintelBDServiceConfig
	=new HashMap<String,WintelBDServiceConfig>();
	
	public static final String WINTEL_BD_BILLING_PREFIX="WINTEL_BD_BILLING_PREFIX";
	
	public static String getMsg(String msg,String msisdn,WintelBDServiceConfig wintelBDServiceConfig){
		try{
			return msg
					.replaceAll("<amount>", ""+wintelBDServiceConfig.getPricePoint())
					.replaceAll("<currency>", wintelBDServiceConfig.getCurrency())
					.replaceAll("<validity>",""+ wintelBDServiceConfig.getValidity())
					.replaceAll("<portalurl>", wintelBDServiceConfig.getPortalUrl())
					.replaceAll("<msisdn>", msisdn)
					;
		}catch(Exception ex){
			
		}
		return msg;
	}
}
