package net.test;

import java.sql.Timestamp;
import java.util.TimeZone;

import net.thialand.ThiaConstant;


public class ThiaMain {

	
	public static void main(String arg[]){
		ThiaConstant.yyyyMMddHHmmssAccessToken.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		
		Timestamp ts=new Timestamp(System.currentTimeMillis()); 
		System.out.println("time: "+ts);
		System.out.println("time: "+ThiaConstant.yyyyMMddHHmmssAccessToken.format(ts));
		
		
		System.out.println("retryBillingDate11: "+ThiaConstant.retryBillingDate);
		ThiaConstant.retryBillingDate.replace(0,ThiaConstant.retryBillingDate.length(),
				new String(ThiaConstant.thialandRetryDateFormat.format(
				new Timestamp(System.currentTimeMillis()))));
		
		System.out.println("retryBillingDate11: "+ThiaConstant.retryBillingDate);
		
		System.out.println("contains : "+("Rejected|external:Rejected:03314BROADCAST".contains("external:Rejected:03314BROADCAST")));
		
	}
}

