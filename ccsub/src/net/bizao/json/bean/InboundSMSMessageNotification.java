package net.bizao.json.bean;

import java.lang.reflect.Field;

public class InboundSMSMessageNotification {

	private String callbackData;
	private InboundSMSMessage inboundSMSMessage;
	
	
	public String getCallbackData() {
		return callbackData;
	}
	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}
	public InboundSMSMessage getInboundSMSMessage() {
		return inboundSMSMessage;
	}
	public void setInboundSMSMessage(InboundSMSMessage inboundSMSMessage) {
		this.inboundSMSMessage = inboundSMSMessage;
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
