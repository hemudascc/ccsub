package net.mycomp.tpay;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TpayConstant {
	public static List<TpayServiceConfig> listTpayServiceConfig = new ArrayList<>();
	public static Map<Integer,TpayServiceConfig> mapServiceIdToTpayServiceConfig = new HashMap<>();
	public static final String RECYCLED_SUBCRIBER ="RECYCLED_SUBCRIBER";
	Integer TPAY_PRODUCT_ID=3;	
	public static final String TPAY_CACHE_PREFIX="TPAY_CACHE_PREFIX";
	public static final String TPAY_TEMP_SUBSCRIBE="TPAY_TEMP_SUBSCRIBE";
	public static final String TPAY_SUB_CONT_ID_PREFIX="TPAY_SUB_CONT_ID_PREFIX";
	public static final String TPAY_SUBCONTID_TOKEN_AND_MSISDN_CACHE_PREFIX="TPAY_SUBCONTID_TOKEN_AND_MSISDN_CACHE_PREFIX";
	public static final String LANG = "en";
	public static final String LANG_EN = "en";
	public static final String LANG_AR = "ar";
	public static final String THEME = "light";
	public static final String HASH_KEY_TYPE = "HMACSHA256";
	public static final String TPAY_JS_API = "http://lookup.tpay.me/idxml.ashx/js?date=<date>&digest=<digest>&simulate=true&operatorcode=<oc>&msisdn=<msisdn>";
	public static final String PUBLIC_KEY = "hODiE6QhSFbTxNWIKpMH";
	public static final String PRIVATE_KEY = "5RZvZlkP4izzuBxLz3eT";	
	public static final String SECRET_KEY = "5RZvZlkP4izzuBxLz3eT";
	public static final String TPAY_PORTAL_URL="http://mob.ccd2c.com/gamepad/tp/home?msisdn=<msisdn>&lang=<lang>";
	public static final String PIN_URL = "https://live.tpay.me/api/TPaySubscription.svc/json/AddSubscriptionContractRequest";
	public static final String PIN_VALIDATE_URL = "https://live.tpay.me/api/TPaySubscription.svc/json/VerifySubscriptionContract";
	public static final String PIN_RESEND_URL = "https://live.tpay.me/api/TPaySubscription.svc/json/SendSubscriptionContractVerificationSMS";
	public static final String SMS_URL = "https://live.tpay.me/api/TPay.svc/json/SendFreeMTMessage";
	public static final String CANCEL_SUBSCRIPTION_CONTARCT_URL = "https://live.tpay.me/api/TPaySubscription.svc/json/CancelSubscriptionContractRequest";
	public static final String WELCOME_MESSAGE_SMS_ENG = "Thanks for subscribing to GamePad to enjoy visit http://mob.ccd2c.com/gamepad/tp/home?msisdn=<msisdn>&lang=<lang>. You will be charged for <price> Egyptian Pound/<billing_sequence> to unsubscribe send <unsub_keyword> to <shortcode> for free. For queries, contact on tech.d2c@collectcent.com";
	public static final String WELCOME_MESSAGE_SMS_ARB = new String("شكرًا لك على الاشتراك في GamePad ، للاستمتاع بزيارة http://mob.ccd2c.com/gamepad/tp/home?msisdn=<msisdn>&lang=<lang>. سيتم تحصيل <price> جنية مصري يوميا لإلغاء الاشتراك ، أرسل <unsub_keyword> إلى <shortcode> مجانا. للاستفسارات ، اتصل على tech.d2c@collectcent.com".getBytes(),StandardCharsets.UTF_8);
	public static final String CONTENT_MESSAGE_SMS_ENG = "Please access the content using http://mob.ccd2c.com/gamepad/tp/home?msisdn=<msisdn>&lang=<lang> URL. For queries, contact on tech.d2c@collectcent.com";
	public static final String CONTENT_MESSAGE_SMS_ARB = "يرجى الوصول إلى المحتوى باستخدام http://mob.ccd2c.com/gamepad/tp/home?msisdn=<msisdn>&lang=<lang> URL. للاستفسارات ، اتصل على tech.d2c@collectcent.com";
	public static final String ACTIVE_CACHE_PREFIX = "TPAY_ACTIVE_CACHE_PREFIX";
	
	static String getMsisdnByOperatorCode(String operatorCode) {
		switch(operatorCode) {
			case "60202":
				return "201063721848";
			case "60201":
				return "201286438693";
			case "60203":
				return "201147213428";
			case "60204":
				return "201558802080";
			default :
				return "";
		}
	}
}
