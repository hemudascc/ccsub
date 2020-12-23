package net.mycomp.one97;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_one97_otp_send")
public class One97OtpPinSend {

	@Id
	//@GeneratedValue
	private Integer id;
	private String msisdn;
	@Column(name = "transation_id")
	private String transationId;
	@Column(name = "request_query_str")
	private String requestQueryStr;
	@Column(name = "send_otp_url")
	private String sendOtpUrl;
	@Column(name = "send_otp_resp")
	private String sendOtpResp;
	
	@Column(name = "response_to_caller")
	private String responseToCaller;
	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	
    public One97OtpPinSend(){}
	
	public One97OtpPinSend(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.id=One97Constant.otpPinSendIdAtomicInteger.incrementAndGet();
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
	public String getTransationId() {
		return transationId;
	}
	public void setTransationId(String transationId) {
		this.transationId = transationId;
	}
	public String getRequestQueryStr() {
		return requestQueryStr;
	}
	public void setRequestQueryStr(String requestQueryStr) {
		this.requestQueryStr = requestQueryStr;
	}
	public String getSendOtpUrl() {
		return sendOtpUrl;
	}
	public void setSendOtpUrl(String sendOtpUrl) {
		this.sendOtpUrl = sendOtpUrl;
	}
	public String getSendOtpResp() {
		return sendOtpResp;
	}
	public void setSendOtpResp(String sendOtpResp) {
		this.sendOtpResp = sendOtpResp;
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

	public String getResponseToCaller() {
		return responseToCaller;
	}

	public void setResponseToCaller(String responseToCaller) {
		this.responseToCaller = responseToCaller;
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