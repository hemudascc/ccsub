package net.mycomp.mobivate;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mobivate_api_trans")
public class MobivateApiTrans {

	@Id	
	private Integer id;
	private String action;
	@Column(name="token")
	private String token;
	@Column(name = "mobivate_service_config_id")	
	private Integer mobivateServiceConfigId;
	@Column(name = "msg_type")	
	private String msgType;
	private String msisdn;
	private String request;
	private String responseCode;
	private String response;
	private Boolean success;
	@Column(name = "create_time")
	private Timestamp createTime;
	
	private Boolean status;
	
	public MobivateApiTrans(){}
	public MobivateApiTrans(boolean status,String timeZone){
		
		this.id=MobivateConstant.mobivateApiTransId.incrementAndGet();
		this.status=status;
		this.createTime=MobivateConstant.getTimeByZone(timeZone);
		this.success=false;
		
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
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
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
	public Integer getMobivateServiceConfigId() {
		return mobivateServiceConfigId;
	}
	public void setMobivateServiceConfigId(Integer mobivateServiceConfigId) {
		this.mobivateServiceConfigId = mobivateServiceConfigId;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
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
