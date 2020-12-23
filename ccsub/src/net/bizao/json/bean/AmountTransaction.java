package net.bizao.json.bean;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class AmountTransaction {

	private String endUserId;
	private PaymentAmount paymentAmount;
	
	private String transactionOperationStatus;
	private String referenceCode;
	private String clientCorrelator;
	
	private String serverReferenceCode;
	private String resourceURL;
	public AmountTransaction(){}
	public AmountTransaction(String amount,String currency,String description,
			String onBehalfOf,String referenceCode,
			String clientCorrelator){
		endUserId="acr:OrangeAPIToken";
		paymentAmount=new PaymentAmount( amount, currency, description,
				 onBehalfOf);
		this.referenceCode=referenceCode;
		this.clientCorrelator=clientCorrelator;
		transactionOperationStatus="Charged";
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
	
	public String getEndUserId() {
		return endUserId;
	}
	public void setEndUserId(String endUserId) {
		this.endUserId = endUserId;
	}
	public PaymentAmount getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(PaymentAmount paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getTransactionOperationStatus() {
		return transactionOperationStatus;
	}
	public void setTransactionOperationStatus(String transactionOperationStatus) {
		this.transactionOperationStatus = transactionOperationStatus;
	}
	public String getReferenceCode() {
		return referenceCode;
	}
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}
	public String getClientCorrelator() {
		return clientCorrelator;
	}
	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}

	public String getServerReferenceCode() {
		return serverReferenceCode;
	}

	public void setServerReferenceCode(String serverReferenceCode) {
		this.serverReferenceCode = serverReferenceCode;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}
}
