package net.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public interface MConstants {

	
	//public static SimpleDateFormat sdfDDMMyyyy= new SimpleDateFormat("ddMMyyyy");
	//public static SimpleDateFormat sdfDDMMyyyyHH = new SimpleDateFormat("ddMMyyyyHH");
	public static DateTimeFormatter sdfDDMMyyyy = DateTimeFormatter.ofPattern("ddMMyyyy");
	public static DateTimeFormatter sdfDDMMyyyyHH = DateTimeFormatter.ofPattern("ddMMyyyyHH");
	    public static final int DEFAULT_ADNETWORK_ID = 2;
     	public static final int QATAR_OPERATOR_ID=1;
        public static final int ETISALAT_OPERATOR_ID=2;
	
	    public static final int VEOO_HONDURAS_TIGO_OPERATOR_ID=53;
	    public static final int VEOO_NICARAGUA_CLARO_OPERATOR_ID=54;
	    public static final int VEOO_NICARAGUA_MOVISTAR_OPERATOR_ID=55;
	  
	    public static final int UK_OS2_OPERATOR_ID=56;
	    public static final int UK_ORANGE_OPERATOR_ID=57;
	    public static final int UK_VODAFONE_OPERATOR_ID=58;    
	    
	    public static final int DU_OPERATOR_ID=59;
	    public static final int INTARGET_SAFARICOM_OPERATOR_ID=60;
	    public static final int MICROKIOSK_AIS_OPERATOR_ID=61;
	    public static final int MESSAGE_CLOUD_GATWAY_CH=62;
	    
	    public static final int NXT_VAS_JORDAN_UMNAIH_OPERATOR=63;
	    public static final int NXT_VAS_JORDAN_ORANGE_OPERATOR=64;
	    public static final int NXT_VAS_EGYPT_ORANGE_OPERATOR=65;
	    public static final int NXT_VAS_EGYPT_VODAFONE_OPERATOR=66;
	    public static final int NXT_VAS_TUNISIA_ORANGE_OPERATOR=67;
	    public static final int NXT_VAS_KUWAIT_VIVA_OPERATOR=68;
	    
	    public static final int OREDOO_KUWAIT_OPERATOR_ID=69;	    
	    public static final int MESSAGE_CLOUD_GATWAY_ZA_OPERATOR=70;
	    
	    public static final int MOBIMIND_OERATOR_KOREK=71;
	    public static final int MOBIMIND_OERATOR_BATELCO=72;
	    public static final int MOBIMIND_OERATOR_VIVA=73;
	    
	    public static final int VEOO_COSTA_RICA=74;	    
	    public static final int ACTEL_OPERATOR_ETISALAT=75;
	    
	    public static final int ORANGE_IV_OPERATOR_ID=76;
	    public static final int ORANGE_CAMERON_OPERATOR_ID=77;
	    public static final int ORANGE_RDCONGO_OPERATOR_ID=78;
	    public static final int ORANGE_SENEGAL_OPERATOR_ID=79;
	    
	    public static final int TRIAKOM_INDONESIA_SAT_OPERATOR_ID=80;
	    public static final int TRIAKOM_INDONESIA_XL_OPERATOR_ID=81;
	    public static final int TRIAKOM_INDONESIA_IM3_OPERATOR_ID=82;
	    
	    public static final int MK_MALAYSIA_DIGI_OPERATOR_ID=83;
	    public static final int MK_MALAYSIA_UMOBILE_OPERATOR_ID=84;
	    
	    public static final int KSA_STA_OPERATOR_ID=85;
	    
	    public static final int INAPP_QATAR_OOREDOO_OPERATOR_ID=86;
	    public static final int INAPP_VODAFONE_OPERATOR_ID=87;
	    
	    public static final int INAPP_ONE97_OOREDOO_QATAR_OPERATOR_ID=88;
	    public static final int INAPP_ONE97_UAE_ETISALAT_OPERATOR_ID=89;
	    
	    public static final int MICROKIOSK_TRUEMOVE_OPERATOR_ID=90;    
	    public static final int WINTEL_BD_GRAMEEPHONE_OPERATOR_ID=91;	    
	    public static final int KSA_JAIN_OPERATOR_ID=92;
	    
	    public static final int MOBIVATE_SOUTH_AFRICA_CELLC_OPERATOR_ID=93;
	    public static final int MOBIVATE_UK_VODAFONE_OPERATOR_ID=94;
	    
	    public static final int ONMOBILE_OPERATOR_ID=95;
	    
	    public static final int KSA_MOBILY_OPERATOR_ID=96;
	    
	    public static final int WORLD_PLAY_MTN_OPERATOR_ID=97;
	    public static final int WORLD_PLAY_VODACOM_OPERATOR_ID=98;
	    public static final int WORLD_PLAY_CELLC_OPERATOR_ID=99;
	    
	    public static final int MT2_KSA_ZAIN_OPERATOR_ID=100;
	    public static final int MT2_KSA_STC_OPERATOR_ID=101;
	    
	    public static final int MT2_ZAIN_IRAQ_OPERATOR_ID=102;
	    public static final int MT2_UAE_ETISALAT_OPERATOR_ID=103;
	    public static final int MT2_UAE_DU_OPERATOR_ID=104;
	    
	    public static final int MOBIVATE_SOUTH_AFRICA_MTN_OPERATOR_ID=105;
	    public static final int MT2_JORDAN_OPERATOR_ID=106;
	    
	    public static final int COMVIVA_OOREDOO_OMAN_OPERATOR_ID=107;
	    
	    public static final int MOBIVATE_SOUTH_AFRICA_ZA_VODACOM_OPERATOR_ID=108;
	    
	    public static final int MOBIMIND_EGYPT_VODAFONE_OPERATOR_ID=109;
	    
	    public static final int ACTEL_OPERATOR_DU=110;
	    
	    public static final int MOBIMIND_OMAN_OOREDOO_OPERATOR_ID=111;
	    public static final int TPAY_EGYPT_WE_OPERATOR_ID=112;
	    public static final int MOBIMIND_KUWAIT_STC_OPERATOR_ID= 113;
	    
	    public static final int ACTEL_OOREDOO_QATAR_OPERATOR_ID= 114;
	    public static final int ACTEL_VODAFONE_QATAR_OPERATOR_ID= 115;
	    public static final int TPAY_EGYPT_ETISALAT_OPERATOR_ID= 116;
	    
	    
	public static final String CMPID="cmpid";
	public static final String ACTIVE="ACTIVE";
	public static final String INACTIVE="INACTIVE";
	
	public static final Random random=new Random();
	public final String INFO = "INFO";
	public static final String DCT_BLOCK_PREFIX="DCT_BLOCK_PREFIX";
	public final int AMQ_SCHDULE_DELAY= 30*60*1000;
	public  static final int WASTE_SERVICE_ID=100;
	public static final DateFormat dfYYYYMMDDhhmmTZD=new SimpleDateFormat("YYYY-MM-DD'T'hh:mmZ");
	
	public static final DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String KEY_SEPERATOR="-";
	public static final int SUBSCRIBED = 1;
	public static final String SUBSCRIBED_DESC = "SUBSCRIBED";

	public static final int UNSUBSCRIBED = 0;
	public static final String UNSUBSCRIBED_DESC = "UNSUBSCRIBED";

	public static final int SUBSCRIPTION_FAILED = -1;
	public static final String SUBSCRIPTION_FAILED_DESC = "SUBSCRIPTION_FAILED";

	public static final int GRACE_STATUS = 10;
	
	
    
    
  
    
    
	public static final String REDIRECT_TO_ERROR = "REDIRECT_TO_ERROR";
	public static final String REDIRECT_TO_WASTE_URL= "REDIRECT_TO_WASTE_URL_MSISDN_MISSING";
	public static final String REDIRECT_TO_WASTE_URL_COMAPIGN_BLOCK = "REDIRECT_TO_WASTE_URL_COMAPIGN_BLOCK";
	public static final String REDIRECT_TO_WASTE_URL_BLOCKSERIES = "REDIRECT_TO_WASTE_URL_BLOCKSERIES";
	public static final String REDIRECT_TO_WASTE_URL_BLOCK_CIRCLE= "REDIRECT_TO_WASTE_URL_BLOCKCIRCLE";
	public static final String REDIRECT_TO_WASTE_URL_DUPLICATE_MISISDN="REDIRECT_TO_WASTE_URL_DUPLICATE_MISISDN";
	public static final String  REDIRECT_TO_WASTE_URL_PUB_ID_BLOCK="REDIRECT_TO_WASTE_URL_PUB_ID_BLOCK";
	public static final String  REDIRECT_TO_WASTE_URL_DCT_BLOCK="REDIRECT_TO_WASTE_URL_DCT_BLOCK";
	public static final String  REDIRECT_TO_WASTE_URL_WRONG_MSISDN_FORMAT="REDIRECT_TO_WASTE_URL_WRONG_MSISDN_FORMAT";
	public static final String REDIRECT_TO_WASTE_URL_DUE_TO_CHARGING_TYPE_BLOCK = "REDIRECT_TO_WASTE_URL_DUE_TO_CHARGING_TYPE_BLOCK";
	public static final String REDIRECT_TO_WASTE_URL_ALREADY_SUBSCRIBED = "REDIRECT_TO_WASTE_URL_ALREADY_SUBSCRIBED";
	public static final String REDIRECT_TO_HOME_PAGE_COMAPIGN_NOT_FOUND = "REDIRECT_TO_HOME_PAGE_COMAPIGN_NOT_FOUND";
	public static final String REDIRECT_TO_CG = "REDIRECT_TO_CG";
	public static final String PIN_SEND = "PIN_SEND";
	public static final String PIN_VALIDATE = "PIN_VALIDATE";
	public static final String TOKEN_SEPERATOR = "c";

	
	//public static final int DEFAULT_CIRCLE_ID = 500;
	
	public static final String REDIRECT_TO_WASTE_URL_CONVERSION_CAPPING= "REDIRECT_TO_WASTE_URL_CONVERSION_CAPPING";
	public static final String REDIRECT_TO_WASTE_URL_IP_NOT_MATCHING="REDIRECT_TO_WASTE_URL_IP_NOT_MATCHING";
	public static final String REDIRECT_TO_WASTE_URL_EARLIER_CHARGED= "REDIRECT_TO_WASTE_URL_EARLIER_CHARGED";
	

	public static final String DEFAULT_CIRCLE_CODE = "DEFAULT";
	
	
	public final int ALL_OPERATOR_ID=10000;
	public final String CHARGED="CHARGED";
	public final int ALL_ADNETWORK_ID=10000;
	public final int ALL_CIRCLE_ID=10000;
	//public final int DU_OPERATOR_ID=1;
	
    
	public final int PROCESS_IN_PROGRESS=1;
	public final String PROCESS_IN_PROGRESS_DESC="IN_PROCESS";
	public final int PROCESS_IN_COMPLETE=2;
	public final String PROCESS_IN_COMPLETE_DESC="COMPLETE";
	public final int PROCESS_IN_ERROR=3;
	public final String PROCESS_IN_ERROR_DESC="ERROR";
	public static final String MANUAL_CRON="MANUAL_CRON";
	public static final String AUTOSENT_CRON="AUTOSENT_CRON";
	public static final int CHURN_HOUR=24;

	public static String getCappingKey(Integer operatorId,Integer adnetworkId,Integer circleId){		
		return MConstants.dateFormat.format(new Timestamp(System.currentTimeMillis()))
				+KEY_SEPERATOR+operatorId+KEY_SEPERATOR+adnetworkId+KEY_SEPERATOR+circleId;
	}
	
	public static String getAdnetworkTypeConfigKey(String type,Integer operatorId,Integer adnetworkId){		
		return type+KEY_SEPERATOR+operatorId+KEY_SEPERATOR+adnetworkId;	
	}
	public static final String DEFAULT_CIRCLE = "17";
	public static final int DEFAULT_CIRCLE_ID = 17;
	public static final int DEFAULT_ADNETWORK_CAMPAIGN_ID = -1;
	public static final String DEFAULT_PUB_ID = "-1";
	public static final int DEFAULT_OP_ID = -1;
	public static final String DEFAULT_CLICK_TYPE = "-1";
	
	public static final String DEFAULT_ADNETWORK_CAMPAIGN_NAME = "DEFAULT";
	public final String ACT = "ACT";
	public final String ALREADY_SUBSCRIBED = "ALREADY_SUBSCRIBED";
	public final String TEMPORARY_ACT = "TEMPORARY_ACT";
	public final String PARK_TO_ACT = "PARK_TO_ACT";
	public final String GRACE = "GRACE";
	public final String DCT = "DCT";
	public final String RENEW = "RENEW";
	public final String CHURN = "CHURN";
	public final String BLOCKED = "BLOCKED";
	public final String UNBLOCKED = "UNBLOCKED";
	public final String POSTPAID_RESPONSE = "Postpaid:Active";
	public final int BLOCK_CHARGING_TYPE_ALL_ADNETWORK=10000;
	public final int BLOCK_CHARGING_TYPE_ALL_CIRCLE=10000;
	
	public static final String DAILY_REPORT_TYPE="DAILY";
	public static final String MONTHLY_REPORT_TYPE="MONTHLY";

}




