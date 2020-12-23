package net.mycomp.ksa;

import net.util.SubscriptionMode;

public enum KSANotificationMode {

	WAP("1",SubscriptionMode.WAP.getMode()),
	APP("2",SubscriptionMode.INAPP.getMode()),
	WEB("3",SubscriptionMode.WEB.getMode()),
	SMS("4",SubscriptionMode.SMS.getMode());
	
	private String bearerId;
	private String mode;
	
	KSANotificationMode(String bearerId,String mode){
		this.bearerId=bearerId;
		this.mode=mode;
	}
	
	public static String getMode(String bearerId){
		for(KSANotificationMode ksaNotificationMode:values()){
			if(ksaNotificationMode.getBearerId().equalsIgnoreCase(bearerId)){
				return ksaNotificationMode.mode;
			}
		}
		return WEB.getMode();
	}

	public String getBearerId() {
		return bearerId;
	}

	public void setBearerId(String bearerId) {
		this.bearerId = bearerId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
