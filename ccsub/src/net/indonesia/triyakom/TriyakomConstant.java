package net.indonesia.triyakom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.util.MConstants;


public interface TriyakomConstant {
	
	public static final List<Integer> operatorIds=new ArrayList<Integer>(){{
		add(MConstants.TRIAKOM_INDONESIA_SAT_OPERATOR_ID);
		add(MConstants.TRIAKOM_INDONESIA_XL_OPERATOR_ID);
		add(MConstants.TRIAKOM_INDONESIA_IM3_OPERATOR_ID);
		
		}};

	public static final List<TriyakomConfig> listTriyakomConfig=new ArrayList<TriyakomConfig>();
	public static final Map<Integer,TriyakomConfig> mapServiceIdToTriyakomConfig=new HashMap<Integer,TriyakomConfig>();
	
	public static final List<IndonesiaChargingConfig> listIndonesiaChargingConfig=new ArrayList<IndonesiaChargingConfig>();
	
//	public static final List<IndonesiaServiceConfig> listIndonesiaServiceConfig=new ArrayList<IndonesiaServiceConfig>();
//	public static final Map<Integer,IndonesiaServiceConfig> mapServiceIdToIndonesiaServiceConfig=new HashMap<Integer,IndonesiaServiceConfig>();
	

	public static final String INDONESIA_MONTHLY="INDONESIA_MONTHLY";
	public static final String INDONESIA_WEEKLY="INDONESIA_WEEKLY";
	public final String CHARGED="CHARGED";
	public final String PUSH="PUSH";
	public final String RENEWAL="RENEWAL";
	public final String XL_TRIYAKON_OPERATOR_NAME="XL";
	public final String TELKOMSEL_TRIYAKON_OPERATOR_NAME="TELKOMSEL";
	public final String SMAFTREN_TRIYAKON_OPERATOR_NAME="SF";
	public final String HT1_TRIYAKON_OPERATOR_NAME="HT1";
	
	public final String ACTIVATION_KEY="REG";
	public final String DEACTIVATION_KEY="UNREG";
	public final String RENEW_KEY="RENEW";
	
	
	public final String DLR_DELIVERED="102";
	public final String DLR_INSUFFICIENT="103";
	public final String DLR_UNDELIVERED="101";
	
	public final String MT_GRACE_BILLING="GRACE_BILLING";
	public final String MT_NEW_REQUEST="NEW_REQUEST";
	public final String MT_WELCOME_MSG="WELCOME_MSG";
	
}