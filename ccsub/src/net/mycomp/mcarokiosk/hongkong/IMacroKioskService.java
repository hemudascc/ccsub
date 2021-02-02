package net.mycomp.mcarokiosk.hongkong;

import net.persist.bean.SubscriberReg;

public interface IMacroKioskService {

	public boolean handleSubscriptionMOMessage(HongkongMOMessage hongkongMOMessage);		
	public void processDeliveryNotification(HongkongDeliveryNotification deliveryNotification);
}
