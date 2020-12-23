package net.mycomp.veoo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_veoo_pin_validation")
public class VeooPinValidation implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	
	private String msisdn;
	private String request;
	private String response;
	@Column(name = "pin_validation_status")
	private String pinValidationStatus;
	@Column(name = "pin_validation_message")
	private String pinValidationMessage;
	
	@Column(name = "pin_validate")
	private Boolean pinValidate;
	
	private String token;
	@Column(name = "token_id")
	private Integer tokenId;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public VeooPinValidation(){}
	public VeooPinValidation(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.pinValidate=false;
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
	public String getPinValidationStatus() {
		return pinValidationStatus;
	}
	public void setPinValidationStatus(String pinValidationStatus) {
		this.pinValidationStatus = pinValidationStatus;
	}
	public String getPinValidationMessage() {
		return pinValidationMessage;
	}
	public void setPinValidationMessage(String pinValidationMessage) {
		this.pinValidationMessage = pinValidationMessage;
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
	public Boolean getPinValidate() {
		return pinValidate;
	}
	public void setPinValidate(Boolean pinValidate) {
		this.pinValidate = pinValidate;
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
public Integer getServiceId() {
	return serviceId;
}
public void setServiceId(Integer serviceId) {
	this.serviceId = serviceId;
}

	
}
