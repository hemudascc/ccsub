package net.indonesia.triyakom;

import net.persist.bean.SubscriberReg;


public interface IOpeartorService {
	
	public boolean handleSubscriptionMoMessage(MOMessage moMessage);
	public boolean handleSubscriptionMTPushMessage(MTMessage mtMessage);
	//public boolean handleSubscriptionMTRenewalPushMessage(MTMessage mtMessage);
	
	public boolean handleSubscriptionMTRenewalPushMessage(SubscriberReg subscriberReg);
	public boolean handleSubscriptionMTPushMessage(SubscriberReg subscriberReg);
	
}
