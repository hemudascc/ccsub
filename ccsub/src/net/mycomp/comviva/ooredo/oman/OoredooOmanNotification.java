package net.mycomp.comviva.ooredo.oman;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_ooredoo_oman_notification")
public class OoredooOmanNotification implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//http://IP:PORT/<CP_Context>?serviceId=1234&appliedPlan=9898
	//&sequenceNo=123432939&operationId=SN&bearerId=WAP&validityDays=1&chargeAmount=5&callingParty=968XXXXXXXXX
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="my_action")
	private String myAction;
	private String msisdn;	
	@Column(name = "service_id")
	private String serviceId;	
	@Column(name = "applied_plan")
	private String appliedPlan;
	@Column(name = "sequence_no")
	private String sequenceNo;
	@Column(name = "operation_id")
	private String operationId;
	@Column(name = "bearer_id")
	private String bearerId;	
	@Column(name = "charge_amount")
	private Double chargeAmount;
	@Column(name = "validity_days")
	private Integer validityDays;	
	
	@Column(name="query_str")
	private String queryStr;
	
	@Column(name = "token")
	private String token;
	@Column(name = "token_id")
	private Integer tokenId;
	@Column(name = "send_to_adnetwork")
	private Boolean sendToAdnetwork;
	
	@Column(name = "request_time")
	private Timestamp requestTime;
	private boolean status;
	
	
	public OoredooOmanNotification(){
				
	}
	public OoredooOmanNotification(boolean status){
		this.requestTime=new Timestamp(System.currentTimeMillis());
		this.status=status;	
		this.sendToAdnetwork=false;
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


public Integer getId() {
	return id;
}


public void setId(Integer id) {
	this.id = id;
}


public String getMsisdn() {
	return msisdn;
}


public void setMsisdn(String msisdn) {
	this.msisdn = msisdn;
}


public String getAppliedPlan() {
	return appliedPlan;
}


public void setAppliedPlan(String appliedPlan) {
	this.appliedPlan = appliedPlan;
}


public String getSequenceNo() {
	return sequenceNo;
}


public void setSequenceNo(String sequenceNo) {
	this.sequenceNo = sequenceNo;
}


public String getOperationId() {
	return operationId;
}


public void setOperationId(String operationId) {
	this.operationId = operationId;
}


public String getBearerId() {
	return bearerId;
}


public void setBearerId(String bearerId) {
	this.bearerId = bearerId;
}


public Double getChargeAmount() {
	return chargeAmount;
}


public void setChargeAmount(Double chargeAmount) {
	this.chargeAmount = chargeAmount;
}


public Integer getValidityDays() {
	return validityDays;
}


public void setValidityDays(Integer validityDays) {
	this.validityDays = validityDays;
}


public String getQueryStr() {
	return queryStr;
}


public void setQueryStr(String queryStr) {
	this.queryStr = queryStr;
}


public Timestamp getRequestTime() {
	return requestTime;
}


public void setRequestTime(Timestamp requestTime) {
	this.requestTime = requestTime;
}


public boolean isStatus() {
	return status;
}


public void setStatus(boolean status) {
	this.status = status;
}
public String getServiceId() {
	return serviceId;
}
public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public Integer getTokenId() {
	return tokenId;
}
public void setTokenId(Integer tokenId) {
	this.tokenId = tokenId;
}
public Boolean getSendToAdnetwork() {
	return sendToAdnetwork;
}
public void setSendToAdnetwork(Boolean sendToAdnetwork) {
	this.sendToAdnetwork = sendToAdnetwork;
}
public String getMyAction() {
	return myAction;
}
public void setMyAction(String myAction) {
	this.myAction = myAction;
}
	
	
}
