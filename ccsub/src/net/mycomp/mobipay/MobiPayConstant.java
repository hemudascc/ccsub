package net.mycomp.mobipay;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.util.MConstants;

public interface MobiPayConstant {

	Logger logger = Logger.getLogger(MobiPayConstant.class);

	Map<Integer,MobiPayServiceConfig> mapServiceIdToMobiPayServiceConfig = new HashMap<>(); 

	String MOBIPAY_MSISDN_TOKEN_CACHE_PREFIX = "MOBIPAY_MSISDN_TOKEN_CACHE_PREFIX";
	String SUBSCRIBED="success";
	String DLR_SUCCESS="1";

	static String getPortal(String portalUrl,String msisdn,Integer subId){
		try{
			return portalUrl
					.replaceAll("<msisdn>", msisdn)
					.replaceAll("<subid>",""+ subId)
					;
		}catch(Exception ex){
			logger.error("getPortal ",ex);
		}
		return portalUrl;
	}

	static String getDefaultToken(String provider) {
		if("vodacomsa".equalsIgnoreCase(provider)) {
			return "-1c-1c292";
		}
		if("cellcsa".equalsIgnoreCase(provider)) {
			return "-1c-1c293";
		}
		return null;
	}
	/*
	 * public static void main(String[] args) {
	 * System.out.println(getDefaultToken("vodacomsa")); }
	 */

	static String findAction(MobiPayDlr mobiPayDlr) {
		if(DLR_SUCCESS.equals(mobiPayDlr.getResult())) {
			return MConstants.ACT;
		}
		return null;
	}

}
