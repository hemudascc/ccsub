package net.mycomp.onmobile;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_onmobile_charging_notification")
public class OnMobileChargingNotification implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	private String msisdn;
	@Column(name = "srv_key")
	private String srvKey;
	@Column(name = "charging_status")
	private String chargingStatus;
	private String type;
	@Column(name = "ref_id")
	private String refId;
	private String action;
	private String mode;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name="send_to_adnetwork")
	private Boolean sendToAdnetwork;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public OnMobileChargingNotification(){}
	public OnMobileChargingNotification(boolean status){
		this.status=status;
		this.sendToAdnetwork=false;
		this.createTime=new Timestamp(System.currentTimeMillis());
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
	public String getSrvKey() {
		return srvKey;
	}
	public void setSrvKey(String srvKey) {
		this.srvKey = srvKey;
	}
	public String getChargingStatus() {
		return chargingStatus;
	}
	public void setChargingStatus(String chargingStatus) {
		this.chargingStatus = chargingStatus;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
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
