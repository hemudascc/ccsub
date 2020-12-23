package net.mycomp.mcarokiosk.malaysia;

import net.persist.bean.SubscriberReg;

public interface IMacroKioskService {

	public boolean handleSubscriptionMOMessage(MalasiyaMOMessage malasiyaMOMessage);		
	public boolean sendSubscriptionRenewalRequest(SubscriberReg subscriberReg);
	public void processDeliveryNotification(MalaysiaDeliveryNotification deliveryNotification);
}
