package net.bizao.json.bean;

public class OutboundSMSMessageRequestWrapper {

	private OutboundSMSMessageRequest outboundSMSMessageRequest;
	
	public OutboundSMSMessageRequestWrapper(String text,String clientCorrelator,String notifyURL
			,String callbackData,String senderName,String senderAddress){
		outboundSMSMessageRequest=new OutboundSMSMessageRequest(
				 text, clientCorrelator, notifyURL
				, callbackData, senderName,senderAddress);
	}

	public OutboundSMSMessageRequest getOutboundSMSMessageRequest() {
		return outboundSMSMessageRequest;
	}

	public void setOutboundSMSMessageRequest(
			OutboundSMSMessageRequest outboundSMSMessageRequest) {
		this.outboundSMSMessageRequest = outboundSMSMessageRequest;
	}
	
}
