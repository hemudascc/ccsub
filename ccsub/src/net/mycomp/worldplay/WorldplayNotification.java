package net.mycomp.worldplay;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_worldplay_notification")
public class WorldplayNotification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "action")
	private String action;
	@Column(name="action_type")
	private String actionType;
	
	@Column(name = "worldplay_service_config_id")
	private Integer worldplayServiceConfigId;
	
	@Column(name = "mt")
	private String mt;
	@Column(name="operator_name")
	private String operatorName;
	private String client;
	@Column(name = "request_id")
	private String requestId;
	@Column(name = "tel_no")
	private String telNo;
	@Column(name = "ad_tracking")
	private String adTracking;
	
	@Column(name = "source")
	private String source;
	@Column(name = "status_id")
	private String statusId;
	
	@Column(name = "ref_id")
	private String refId;
	@Column(name = "amount")
	private String amount;
	@Column(name = "wpauth_req_ref")
	private String wpauthReqRef;
	@Column(name = "status_time")
	private String statusTime;
	@Column(name = "status_message")
	private String statusMessage;
	
	@Column(name = "token")
	private String token;
	@Column(name = "send_to_adnetwork")	
	private Boolean sendToAdnetwork;
	
	@Column(name = "query_str")
	private String queryStr;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public WorldplayNotification(){}
	public WorldplayNotification(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.sendToAdnetwork=false;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getAdTracking() {
		return adTracking;
	}
	public void setAdTracking(String adTracking) {
		this.adTracking = adTracking;
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
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getWorldplayServiceConfigId() {
		return worldplayServiceConfigId;
	}
	public void setWorldplayServiceConfigId(Integer worldplayServiceConfigId) {
		this.worldplayServiceConfigId = worldplayServiceConfigId;
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
public String getRefId() {
	return refId;
}
public void setRefId(String refId) {
	this.refId = refId;
}
public String getAmount() {
	return amount;
}
public void setAmount(String amount) {
	this.amount = amount;
}
public String getWpauthReqRef() {
	return wpauthReqRef;
}
public void setWpauthReqRef(String wpauthReqRef) {
	this.wpauthReqRef = wpauthReqRef;
}
public String getStatusTime() {
	return statusTime;
}
public void setStatusTime(String statusTime) {
	this.statusTime = statusTime;
}
public String getStatusMessage() {
	return statusMessage;
}
public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
}
public String getOperatorName() {
	return operatorName;
}
public void setOperatorName(String operatorName) {
	this.operatorName = operatorName;
}
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public Boolean getSendToAdnetwork() {
	return sendToAdnetwork;
}
public void setSendToAdnetwork(Boolean sendToAdnetwork) {
	this.sendToAdnetwork = sendToAdnetwork;
}
public String getActionType() {
	return actionType;
}
public void setActionType(String actionType) {
	this.actionType = actionType;
}


}
