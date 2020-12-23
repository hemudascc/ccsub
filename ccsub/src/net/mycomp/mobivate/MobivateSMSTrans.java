package net.mycomp.mobivate;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mobivate_sms_trans")
public class MobivateSMSTrans {

	@Id
	private Integer id;
	@Column(name="mt_message_type")
	private String mtMessageType;
	@Column(name="action")
	private String action;	
	@Column(name="token")
	private String token;	
	private String msisdn;	
	@Column(name="service_id")
	private Integer serviceId;	
	@Column(name = "request_str")
	private String requestStr;
	private String response;
	@Column(name = "request_time")
	private Timestamp requestTime;
	private Boolean status;
	
	public MobivateSMSTrans(){}
	
	public MobivateSMSTrans(boolean status){
		id=MobivateConstant.smsTransAtomicInteger.incrementAndGet();
		this.requestTime=new Timestamp(System.currentTimeMillis());
		this.status=status;
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

	public String getMtMessageType() {
		return mtMessageType;
	}

	public void setMtMessageType(String mtMessageType) {
		this.mtMessageType = mtMessageType;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	}
