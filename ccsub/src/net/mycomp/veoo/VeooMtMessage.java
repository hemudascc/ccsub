package net.mycomp.veoo;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_veoo_mt_message")
public class VeooMtMessage {
	
@Id
//@GeneratedValue
private Integer id;

@Column(name="action")
private String action;

private String userName;
private String password;
private String source;
private String msisdn;
private String message;
private String coding;
private String charset;
private String format;
private String network;
@Column(name = "network_id")
private String networkId;
@Column(name = "veoo_service_id")
private String veooServiceId;
private String type;
@Column(name = "request_str")
private String requestStr;
private String response;

@Column(name = "create_time")
private Timestamp createTime;
private Boolean status;

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

public VeooMtMessage(){}
public VeooMtMessage(Boolean status){
	this.createTime=new Timestamp(System.currentTimeMillis());
	this.status=true;
	id=VeooConstant.automicIdVeooMTMessage.incrementAndGet();
}

	
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getSource() {
	return source;
}
public void setSource(String source) {
	this.source = source;
}
public String getMsisdn() {
	return msisdn;
}
public void setMsisdn(String msisdn) {
	this.msisdn = msisdn;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getCoding() {
	return coding;
}
public void setCoding(String coding) {
	this.coding = coding;
}
public String getCharset() {
	return charset;
}
public void setCharset(String charset) {
	this.charset = charset;
}
public String getFormat() {
	return format;
}
public void setFormat(String format) {
	this.format = format;
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
public String getNetwork() {
	return network;
}
public void setNetwork(String network) {
	this.network = network;
}

public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getRequestStr() {
	return requestStr;
}
public void setRequestStr(String requestStr) {
	this.requestStr = requestStr;
}
public String getResponse() {
	return response;
}
public void setResponse(String response) {
	this.response = response;
}

public String getVeooServiceId() {
	return veooServiceId;
}

public void setVeooServiceId(String veooServiceId) {
	this.veooServiceId = veooServiceId;
}

public String getAction() {
	return action;
}

public void setAction(String action) {
	this.action = action;
}

public String getNetworkId() {
	return networkId;
}

public void setNetworkId(String networkId) {
	this.networkId = networkId;
}

}
