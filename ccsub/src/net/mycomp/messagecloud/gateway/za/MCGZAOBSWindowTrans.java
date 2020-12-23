package net.mycomp.messagecloud.gateway.za;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mcg_za_obs_window_trans")
public class MCGZAOBSWindowTrans {

	@Id
	@GeneratedValue
	private Integer id;
	private String msisdn;
	private String token;
	@Column(name = "token_id")
	private Integer tokenId;
	@Column(name = "request_str")
	private String requestStr;
	@Column(name = "response_str")
	private String responseStr;
	@Column(name = "response_id")
	private String responseId;
	@Column(name = "response_redirect_url")
	private String responseRedirectUrl;
	@Column(name = "response_status")
	private String responseStatus;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public MCGZAOBSWindowTrans(){}
	public MCGZAOBSWindowTrans(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
		
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
	public String getRequestStr() {
		return requestStr;
	}
	public void setRequestStr(String requestStr) {
		this.requestStr = requestStr;
	}
	public String getResponseStr() {
		return responseStr;
	}
	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}
	public String getResponseId() {
		return responseId;
	}
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	public String getResponseRedirectUrl() {
		return responseRedirectUrl;
	}
	public void setResponseRedirectUrl(String responseRedirectUrl) {
		this.responseRedirectUrl = responseRedirectUrl;
	}
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
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
	
	
}
