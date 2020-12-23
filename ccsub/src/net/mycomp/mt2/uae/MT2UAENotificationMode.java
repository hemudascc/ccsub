package net.mycomp.mt2.uae;

import net.util.SubscriptionMode;

public enum MT2UAENotificationMode {

	WAP("WAP",SubscriptionMode.WAP.getMode()),
	APP("APP",SubscriptionMode.INAPP.getMode()),
	WEB("WEB",SubscriptionMode.WEB.getMode()),
	SMS("SMS",SubscriptionMode.SMS.getMode());
	
	private String channel;
	private String mode;
	
	MT2UAENotificationMode(String channel,String mode){
		this.channel=channel;
		this.mode=mode;
	}
	
	public static String getMode(String channel){
		for(MT2UAENotificationMode mt2UAENotificationMode:values()){
			if(mt2UAENotificationMode.getChannel().equalsIgnoreCase(channel)){
				return mt2UAENotificationMode.mode;
			}
		}
		return WEB.getMode();
	}

	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
