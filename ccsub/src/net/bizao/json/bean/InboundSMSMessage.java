package net.bizao.json.bean;

import java.lang.reflect.Field;

public class InboundSMSMessage {
	
 private String dateaTime;
 private String destinationAddress;
 private String messageId;
 private String message;
 private String senderAddress;
 
 
public String getDateaTime() {
	return dateaTime;
}
public void setDateaTime(String dateaTime) {
	this.dateaTime = dateaTime;
}
public String getDestinationAddress() {
	return destinationAddress;
}
public void setDestinationAddress(String destinationAddress) {
	this.destinationAddress = destinationAddress;
}
public String getMessageId() {
	return messageId;
}
public void setMessageId(String messageId) {
	this.messageId = messageId;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getSenderAddress() {
	return senderAddress;
}
public void setSenderAddress(String senderAddress) {
	this.senderAddress = senderAddress;
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
