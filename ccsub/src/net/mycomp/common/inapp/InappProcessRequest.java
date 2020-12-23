package net.mycomp.common.inapp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import net.persist.bean.VWServiceCampaignDetail;
import net.util.JpaConverterJson;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

@Entity
@Table(name = "tb_inapp_process_request")
public class InappProcessRequest implements Serializable{

	@Id
	private Integer id;
	private String action;	
	private String msisdn;
	private Integer cmpid;	
	private String pin;
	private String txid;
	@Column(name = "cg_token")
	private String cgToken;
	@Column(name = "query_str")
	private String queryStr;	
	private Boolean success;
	
	@Column(name = "pin_request_count")
	private Integer pinRequestCount;
	
	@Column(name = "send_pin_count")
	private Integer sendPinCount;
	
	@Column(name = "pin_validation_request_count")
	private Integer pinValidationRequestCount;
	
	@Column(name = "pin_validate_count")
	private Integer pinValidateCount;
	
	@Column(name = "pin_validate_amount")
	private Double pinValidateAmount;
	
	@Column(name = "status_check_count")
	private Integer statusCheckCount;
	
	
	@Column(name = "conversion_count")
	private Integer conversionCount;	
	
	@Column(name = "conversion_send_to_adenetwork")
	private Boolean conversionSendToAdenetwork;
	
	@Column(name="response_object")
	@Convert(converter=JpaConverterJson.class)
	private Object responseObject;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	@Transient	
	
	public transient  VWServiceCampaignDetail vwserviceCampaignDetail;
	
	@Transient
	private transient  Map<String,String> requestMap;	
	
	public InappProcessRequest(){		
	}
   
	public InappProcessRequest(boolean status){
		
		this.id=InappConstant.inappProcessRequestId.incrementAndGet();
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.status=status;
		this.success=false;
		this.conversionSendToAdenetwork=false;
		this.pinRequestCount=0;
		this.sendPinCount=0;
		this.pinValidationRequestCount=0;
		this.pinValidateCount=0;
		this.pinValidateAmount=0d;
		this.statusCheckCount=0;
		this.conversionCount=0;
		this.conversionSendToAdenetwork=false;
		
	}
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public Integer getCmpid() {
		return cmpid;
	}
	public void setCmpid(Integer cmpid) {
		this.cmpid = cmpid;
	}

	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid) {
		this.txid = txid;
	}
	
	

	public Map<String, String> getRequestMap() {
		return requestMap;
	}
	public void setRequestMap(Map<String, String> requestMap) {
		this.requestMap = requestMap;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Object getResponseObject() {
		return responseObject;
	}
	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getCgToken() {
		return cgToken;
	}

	public void setCgToken(String cgToken) {
		this.cgToken = cgToken;
	}

	public Boolean getConversionSendToAdenetwork() {
		return conversionSendToAdenetwork;
	}

	public void setConversionSendToAdenetwork(Boolean conversionSendToAdenetwork) {
		this.conversionSendToAdenetwork = conversionSendToAdenetwork;
	}

	public Integer getPinRequestCount() {
		return pinRequestCount;
	}

	public void setPinRequestCount(Integer pinRequestCount) {
		this.pinRequestCount = pinRequestCount;
	}

	public Integer getSendPinCount() {
		return sendPinCount;
	}

	public void setSendPinCount(Integer sendPinCount) {
		this.sendPinCount = sendPinCount;
	}

	public Integer getPinValidationRequestCount() {
		return pinValidationRequestCount;
	}

	public void setPinValidationRequestCount(Integer pinValidationRequestCount) {
		this.pinValidationRequestCount = pinValidationRequestCount;
	}

	

	public Integer getConversionCount() {
		return conversionCount;
	}

	public void setConversionCount(Integer conversionCount) {
		this.conversionCount = conversionCount;
	}

	public Integer getPinValidateCount() {
		return pinValidateCount;
	}

	public void setPinValidateCount(Integer pinValidateCount) {
		this.pinValidateCount = pinValidateCount;
	}

	public Integer getStatusCheckCount() {
		return statusCheckCount;
	}

	public void setStatusCheckCount(Integer statusCheckCount) {
		this.statusCheckCount = statusCheckCount;
	}

	public Double getPinValidateAmount() {
		return pinValidateAmount;
	}

	public void setPinValidateAmount(Double pinValidateAmount) {
		this.pinValidateAmount = pinValidateAmount;
	}
	
}
