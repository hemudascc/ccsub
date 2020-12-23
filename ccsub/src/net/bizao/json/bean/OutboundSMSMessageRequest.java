package net.bizao.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutboundSMSMessageRequest {

	private List<String> address;
	private String senderAddress;
	private Map<String,String> outboundSMSTextMessage;
	private String clientCorrelator;
	private Map<String,String> receiptRequest;
	private String senderName;
	
	public OutboundSMSMessageRequest(String text,String clientCorrelator,String notifyURL
			,String callbackData,String senderName,String senderAddress){
		address=new ArrayList<String>();
		address.add("acr:X-Orange-ISE2");
		
		outboundSMSTextMessage=new HashMap<String,String>();
		outboundSMSTextMessage.put("message", text);
		this.clientCorrelator=clientCorrelator;
		receiptRequest=new HashMap<String,String>();
		receiptRequest.put("notifyURL", notifyURL);
		receiptRequest.put("callbackData", callbackData);
		this.senderName=senderName;
		this.senderAddress=senderAddress;
		
	}
	
	public List<String> getAddress() {
		return address;
	}
	public void setAddress(List<String> address) {
		this.address = address;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public Map<String, String> getOutboundSMSTextMessage() {
		return outboundSMSTextMessage;
	}
	public void setOutboundSMSTextMessage(Map<String, String> outboundSMSTextMessage) {
		this.outboundSMSTextMessage = outboundSMSTextMessage;
	}
	public String getClientCorrelator() {
		return clientCorrelator;
	}
	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}
	public Map<String, String> getReceiptRequest() {
		return receiptRequest;
	}
	public void setReceiptRequest(Map<String, String> receiptRequest) {
		this.receiptRequest = receiptRequest;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
}
