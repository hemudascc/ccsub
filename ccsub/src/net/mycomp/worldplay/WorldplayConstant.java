package net.mycomp.worldplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface WorldplayConstant {

	
	public static final String ACT_GRACE="ACT_GRACE";
	public static final String RENEWAL_GRACE="RENEWAL_GRACE";
	
	public static final String WORLDPLAY_CACHE_PREFIX_TOKEN="WORLDPLAY_CACHE_PREFIX_TOKEN";
	
	//public static final String CHARGING_PENDING="CHARGING_PENDING";
	
	public static Map<Integer,WorldplayServiceConfig> mapServiceIdToWorldplayServiceConfig
	=new HashMap<Integer,WorldplayServiceConfig>();
	
	public static List<WorldplayServiceConfig> listWorldplayServiceConfig
	=new ArrayList<WorldplayServiceConfig>();
	
	
	
	public static WorldplayServiceConfig getWorldplayServiceConfig(String operatorName,String client){
		try{
		for(WorldplayServiceConfig worldplayServiceConfig :listWorldplayServiceConfig){
			if(worldplayServiceConfig.getWorldplayOperatorName().toLowerCase().contains(operatorName.toLowerCase())
					&&worldplayServiceConfig.getClient().toLowerCase().contains(client.toLowerCase())){
				return worldplayServiceConfig;
			}
		}	
		}catch(Exception ex){
			
		}
		return null;
	}
	
	
	
}
