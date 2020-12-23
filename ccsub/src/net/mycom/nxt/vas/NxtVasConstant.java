package net.mycom.nxt.vas;

import java.util.HashMap;
import java.util.Map;

public interface NxtVasConstant {

	public static Map<Integer,NxtVasConfig> mapServiceIdToNxtVasConfig=new HashMap<Integer,NxtVasConfig>();
	public static Map<String,NxtVasConfig> mapMccMncToNxtVasConfig=new HashMap<String,NxtVasConfig>();
	
	public static final String SUB="sub";
	public static final String BILL="bill";
	public static final String ACTIVE="active";
	public static final String CANCELED="canceled";
	public static final String SUSPENDED="suspended";
	
}
