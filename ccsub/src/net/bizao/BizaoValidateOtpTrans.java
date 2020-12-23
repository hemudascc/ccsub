package net.bizao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_bizao_validate_otp_trans")
public class BizaoValidateOtpTrans implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="msisdn_prefix")
	private String msisdnPrefix;
	private String msisdn;
	private String otp;
	private String request;
	@Column(name = "token")
	private String token;	
	@Column(name = "bizao_config_id")
	private Integer bizaoConfigId;
	
	@Column(name="request_url")
	private String requestUrl;
	
	@Column(name = "response_code")
	private Integer responseCode;
	@Column(name = "bizao_token")
	private String bizaoToken;
	@Column(name = "bizao_alias")
	private String bizaoAalias;
	
	private String response;
	@Column(name = "is_valid")
	private Boolean isValid;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public BizaoValidateOtpTrans(){
		createTime=new Timestamp(System.currentTimeMillis());
		status=true;
		isValid=false;
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
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
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
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
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

	

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getBizaoToken() {
		return bizaoToken;
	}

	public void setBizaoToken(String bizaoToken) {
		this.bizaoToken = bizaoToken;
	}

	public String getBizaoAalias() {
		return bizaoAalias;
	}

	public void setBizaoAalias(String bizaoAalias) {
		this.bizaoAalias = bizaoAalias;
	}
	
}
