package net.bizao.json.bean;

import java.lang.reflect.Field;

public class InboundSMSMessageNotificationWrapper {

	private InboundSMSMessageNotification inboundSMSMessageNotification;

	public InboundSMSMessageNotification getInboundSMSMessageNotification() {
		return inboundSMSMessageNotification;
	}

	public void setInboundSMSMessageNotification(
			InboundSMSMessageNotification inboundSMSMessageNotification) {
		this.inboundSMSMessageNotification = inboundSMSMessageNotification;
	}
	

public String toString() {
		
        Field[] fields = this.getClass().getDeclaredFields();
        String str = this.getClass().getName();
        try {
            for (Field field : fields) {
                str += field.getName() + "=" + field.get(this) + ",";
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        } catch (IllegalAccessException ex) {
            System.out.println(ex);
        }
        return str;
    }

}
