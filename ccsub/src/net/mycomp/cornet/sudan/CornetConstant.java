package net.mycomp.cornet.sudan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.util.MConstants;


public class CornetConstant {

	public static List<CornetConfig> listCornetConfig = new ArrayList<>();
	public static Map<Integer, CornetConfig> mapServiceIdToCornetConfig = new HashMap<>();
	
//	public final static String API_BASE_URL = "https://test.zaindsp.com:3030/api/v1/json/";
	public final static String CG_URL= "http://test.zaindsp.com:3033/?p=6826526825&cid=<cid>";
	public final static String LOGIN_API_URL = "https://test.zaindsp.com:3030/api/v1/json/login.php";
	public final static String INITIATE_SUBSCRIPTION_API_URL = "https://test.zaindsp.com:3030/api/v1/json/initiate.php";
	public final static String PAYMENT_PROCESS_API_URL = "https://test.zaindsp.com:3030/api/v1/json/payment.php";
	public final static String UNSUBSCRIBE_API_URL = "https://test.zaindsp.com:3030/api/v1/json/cancel.php";
	public final static boolean REMEMBER_ME = false;
	public final static String CORNET_SUBSCRIPTION_ID_PREFIX="CORNET_SUBSCRIPTION_ID_PREFIX";
	public final static String CORNET_UNIQUE_TOKEN_PREFIX="CORNET_UNIQUE_TOKEN_PREFIX";
	public final static String CORNET_UNIQUE_ACCESS_TOKEN_PREFIX="CORNET_UNIQUE_ACCESS_TOKEN_PREFIX";
	public final static String PIN_SEND="PIN_SEND";
	public final static String PIN_VERIFY="PIN_VERIFY";
	public final static String UNSUBSCRIBE="UNSUBSCRIBE";
	public final static String SEND_SMS="SEND_SMS";
	public final static String CONTENT_MESSAGE="";
	
	public final static String SUB="SUB";
	public final static String UNSUB="UNSUB";
	public final static String REN="REN";
	public final static String SUB_FAIL="SUB_FAIL";
	
	static String findAction(CornetNotification cornetNotification) {
		String action="";
		switch (cornetNotification.getTransactionType()) {
		case SUB:
			action=MConstants.ACT; 
			break;
		case REN:
			action=MConstants.RENEW; 
			break;
		case SUB_FAIL:
			action=MConstants.GRACE; 
			break;
		case UNSUB:
			action=MConstants.DCT; 
			break;
		default:
			break;
		}
		return action;
	}
}
