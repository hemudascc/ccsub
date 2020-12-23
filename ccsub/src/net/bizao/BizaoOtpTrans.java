package net.bizao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_bizao_otp_trans")
public class BizaoOtpTrans implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name="msisdn_prefix")
	private String msisdnPrefix;
	
	private String msisdn;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "bizao_config_id")
	private Integer bizaoConfigId;
	
	@Column(name = "token")
	private String token;
	
	
	private String otp;	
	@Column(name = "otp_message")
	private String otpMessage;
	
	@Column(name = "request")
	private String request;
	
	@Column(name = "response_code")
	private Integer responseCode;
	
	@Column(name = "response")
	private String response;
	
	@Column(name="challenge_id")
	private String challengeId;
	
	@Column(name = "is_used")
	private boolean isUsed;
	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public BizaoOtpTrans(){
		createTime=new Timestamp(System.currentTimeMillis());
		status=true;
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
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getOtpMessage() {
		return otpMessage;
	}
	public void setOtpMessage(String otpMessage) {
		this.otpMessage = otpMessage;
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
	public Integer getBizaoConfigId() {
		return bizaoConfigId;
	}
	public void setBizaoConfigId(Integer bizaoConfigId) {
		this.bizaoConfigId = bizaoConfigId;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public String getMsisdnPrefix() {
		return msisdnPrefix;
	}

	public void setMsisdnPrefix(String msisdnPrefix) {
		this.msisdnPrefix = msisdnPrefix;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
