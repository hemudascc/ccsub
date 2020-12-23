package net.bizao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;



import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import net.persist.bean.AbsractChargingCallBack;

@Entity
@Table(name = "tb_bizao_payment_trans")
public class BizaoPayment extends AbsractChargingCallBack implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="msisdn_prefix")
	private String msisdnPrefix;	
	private String msisdn;
	@Column(name="bizao_config_id")
	private Integer bizaoConfigId;	
	@Column(name = "bizao_token")
	private String bizaoToken;
	
	@Column(name = "bizao_alias")
	private String bizaoAlias;
	
	private String amount;
	private String currency;
	private String description;
	@Column(name = "transaction_operation_status")
	private String transactionOperationStatus;
	@Column(name = "reference_code")
	private String referenceCode;
	@Column(name = "client_correlator")
	private String clientCorrelator;
	private String request;
	private String response;
	@Column(name="server_reference_code")
	private String serverReferenceCode ;
	@Column(name="resource_url")
	private String resourceUrl ;
	@Column(name="charged_amount")
	private Double chargedAmount;
	@Column(name="success")
	private Boolean success;
	
	private Boolean status;
	
	public BizaoPayment(){
		
	}
	
	public BizaoPayment(Boolean status){
		//id=BizaoConstant.bizaoPaymentIdAtomicInteger.incrementAndGet();
		setCreateTime(new Timestamp(System.currentTimeMillis()));
		status=true;
		success=false;
	}
	
	
public String toString() {
		
        Field[] fields = this.getClass().getDeclaredFields();
        String str = this.getClass().getName()+super.toString();
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
	
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getBizaoToken() {
		return bizaoToken;
	}
	public void setBizaoToken(String bizaoToken) {
		this.bizaoToken = bizaoToken;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getBizaoConfigId() {
		return bizaoConfigId;
	}

	public void setBizaoConfigId(Integer bizaoConfigId) {
		this.bizaoConfigId = bizaoConfigId;
	}

	
	public String getServerReferenceCode() {
		return serverReferenceCode;
	}

	public void setServerReferenceCode(String serverReferenceCode) {
		this.serverReferenceCode = serverReferenceCode;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public Double getChargedAmount() {
		return chargedAmount;
	}

	public void setChargedAmount(Double chargedAmount) {
		this.chargedAmount = chargedAmount;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public String getMsisdnPrefix() {
		return msisdnPrefix;
	}

	public void setMsisdnPrefix(String msisdnPrefix) {
		this.msisdnPrefix = msisdnPrefix;
	}

	public String getBizaoAlias() {
		return bizaoAlias;
	}

	public void setBizaoAlias(String bizaoAlias) {
		this.bizaoAlias = bizaoAlias;
	}
}
