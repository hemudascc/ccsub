package net.mycomp.messagecloud;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;

import net.persist.bean.SubscriberReg;
import net.util.MConstants;

import org.apache.log4j.Logger;

public interface MessageCloudConstant {

	 static final Logger logger = Logger
			.getLogger(MessageCloudConstant.class.getName());
	
	 public static final String CANCEL="CANCEL";
	 public static final String SUCCESS="SUCCESS";
	public static Map<Integer,MessageCloudServiceConfig> mapServiceIdMessageCloudServiceConfig=new HashMap<Integer,MessageCloudServiceConfig>();
	public static Map<String,MessageCloudServiceConfig> mapNetworkNameMessageCloudServiceConfig=
			new HashMap<String,MessageCloudServiceConfig>();
	
	
	public static String getAction(MessagecloudNotification messagecloudNotification,
			SubscriberReg subscriberReg){
		try{
		if(messagecloudNotification.getAction()!=null){
			return messagecloudNotification.getAction();
		}	
		if(messagecloudNotification.getCgStatus()!=null
				&&messagecloudNotification.getCgStatus().equalsIgnoreCase("UNSUBSCRIBED")
				&&messagecloudNotification.getStop()!=null
				&&messagecloudNotification.getStop().equalsIgnoreCase("1")){
			return MConstants.DCT;
			
		}
		
		if(messagecloudNotification.getCgStatus()!=null
				&&messagecloudNotification.getCgStatus().equalsIgnoreCase("OK")
				&&messagecloudNotification.getBilled()!=null
				&&messagecloudNotification.getBilled().equalsIgnoreCase("1")&&subscriberReg==null){
			return MConstants.ACT;
		}
		
		if(messagecloudNotification.getCgStatus()!=null
				&&messagecloudNotification.getCgStatus().equalsIgnoreCase("OK")
				&&messagecloudNotification.getBilled()!=null
				&&messagecloudNotification.getBilled().equalsIgnoreCase("1")&&subscriberReg!=null){
			return MConstants.RENEW;
		}
		
		}catch(Exception ex){
			logger.error("getAction:: ",ex);
		}
		return "NA";
	}
}
