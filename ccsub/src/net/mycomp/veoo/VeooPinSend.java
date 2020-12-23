package net.mycomp.veoo;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_veoo_pin_sent")
public class VeooPinSend {

	@Id
	@GeneratedValue
	private Integer id;
	private String msisdn;
	@Column(name = "request_str")
	private String requestStr;
	@Column(name = "response_str")
	private String responseStr;
	
	@Column(name = "pin_send")
	private Boolean pinSend;
	
	@Column(name = "pin_status")
	private String pinStatus;
	private String message;
	@Column(name = "hlr_mcc")
	private String hlrMcc;
	@Column(name = "hlr_mnc")
	private String hlrMnc;
	@Column(name = "hlr_status")
	private String hlrStatus;
	private String result;
	private String type;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public VeooPinSend(){}
	
	public VeooPinSend(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.pinSend=false;
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
	public String getPinStatus() {
		return pinStatus;
	}
	public void setPinStatus(String pinStatus) {
		this.pinStatus = pinStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getHlrMcc() {
		return hlrMcc;
	}
	public void setHlrMcc(String hlrMcc) {
		this.hlrMcc = hlrMcc;
	}
	public String getHlrMnc() {
		return hlrMnc;
	}
	public void setHlrMnc(String hlrMnc) {
		this.hlrMnc = hlrMnc;
	}
	public String getHlrStatus() {
		return hlrStatus;
	}
	public void setHlrStatus(String hlrStatus) {
		this.hlrStatus = hlrStatus;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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

	public Boolean getPinSend() {
		return pinSend;
	}

	public void setPinSend(Boolean pinSend) {
		this.pinSend = pinSend;
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
