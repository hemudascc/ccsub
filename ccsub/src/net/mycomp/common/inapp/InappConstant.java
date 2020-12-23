package net.mycomp.common.inapp;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public interface InappConstant {

	 static final Logger logger = Logger
			.getLogger(InappConstant.class.getName());
	
	public static AtomicInteger inappProcessRequestId=new AtomicInteger(0);
	
	public static final String INAPP="INAPP";
	public static final int INAPP_COLLECENT_ADNETWORK_ID=44;
	
}
