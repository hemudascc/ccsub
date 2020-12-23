package net.mycomp.ksa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_ksa_api_trans")
public class KsaApiTrans implements Serializable{
	
@Id
@GeneratedValue
private Integer id;
@Column(name="request_type")
private String requestType;

private String msisdn;
@Column(name = "request_url")
private String requestUrl;
private String response;
@Column(name = "response_to_caller")
private Boolean responseToCaller;

@Column(name = "token")
private String token;
@Column(name = "send_to_adnetwork")
private Boolean sendToAdnetwork;


@Column(name = "create_time")
private Timestamp createTime;
private Boolean status;

public KsaApiTrans(){}
public KsaApiTrans(Boolean status,String requestType){
	this.status=status;
	this.createTime=new Timestamp(System.currentTimeMillis());
	this.responseToCaller=false;
	this.sendToAdnetwork=false;
	this.requestType=requestType;
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
public String getRequestUrl() {
	return requestUrl;
}
public void setRequestUrl(String requestUrl) {
	this.requestUrl = requestUrl;
}
public String getResponse() {
	return response;
}
public void setResponse(String response) {
	this.response = response;
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
public Boolean getResponseToCaller() {
	return responseToCaller;
}
public void setResponseToCaller(Boolean responseToCaller) {
	this.responseToCaller = responseToCaller;
}
public String getRequestType() {
	return requestType;
}
public void setRequestType(String requestType) {
	this.requestType = requestType;
}
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public Boolean getSendToAdnetwork() {
	return sendToAdnetwork;
}
public void setSendToAdnetwork(Boolean sendToAdnetwork) {
	this.sendToAdnetwork = sendToAdnetwork;
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
