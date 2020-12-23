package net.indonesia.triyakom;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Column;

@Entity
@Table(name = "tb_indonesia_mt_message")

@NamedQueries({ @NamedQuery(name = "MTMessage.findMTMessageById",
query = "SELECT b FROM MTMessage b where b.id=:id ")	
})


public class MTMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "indonesia_charging_config_id")
	private Integer indonesiaChargingConfigId;
		
	@Column(name = "dest_addr")
	private String destAddr;
	@Column(name="short_code")
	private String shortCode;
	@Column(name="short_code_to_prepare_mt_url")
	private String shortCodeToPrepareMtUrl;
	
	@Column(name = "app_id")
	private String appId;
	@Column(name = "app_pwd")
	private String appPwd;
	private String data;
	private String op;
	@Column(name = "rtx_id")
	private String rtxId;
	private String service;
	@Column(name="alphabet_id")
	private String alphabetd;
	@Column(name = "create_time")
	private Timestamp createTime;
	@Column(name="request_url")
	private String requestUrl;
	@Column(name="response")
	private String response;
	@Column(name="response_tid")
	private String responseTid;
	@Column(name="status_id")
	private String statusId;
	
	@Column(name="send_success")
	private Boolean sendSuccess=false;
	@Column(name="token_id")
	private Integer tokenId;
	@Column(name="token")
	private String token;
	
	
	@Column(name="subscriber_current_state")
	private String subscriberCurrentState;
	@Column(name="service_id")
    private Integer serviceId;	
	@Column(name="mo_id")
    private Integer moId;
	
	
	
//	@Column(name="dlr_tid")
//	private String dlrTid;
//	@Column(name="dlr_status_id")
//	private String dlrStatusId;
//	@Column(name="dlr_dtdone")
//	private String dlrDtdone;
//	@Column(name="dlr_op")
//	private String dlrOp;
//	@Column(name="dlr_query_str")
//	private String dlrQueryStr;
//	@Column(name="dlr_received_time")
//	private Timestamp dlrReceivedTime;
//	
//	@Column(name="request_type")
//	private String requestType;
	
	
	public MTMessage(){}
	
	public MTMessage(String action){
		this.action=action;
		createTime=new Timestamp(System.currentTimeMillis());
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
	
	private Boolean status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDestAddr() {
		return destAddr;
	}
	public void setDestAddr(String destAddr) {
		this.destAddr = destAddr;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppPwd() {
		return appPwd;
	}
	public void setAppPwd(String appPwd) {
		this.appPwd = appPwd;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getRtxId() {
		return rtxId;
	}
	public void setRtxId(String rtxId) {
		this.rtxId = rtxId;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getAlphabetd() {
		return alphabetd;
	}
	public void setAlphabetd(String alphabetd) {
		this.alphabetd = alphabetd;
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
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getResponseTid() {
		return responseTid;
	}
	public void setResponseTid(String responseTid) {
		this.responseTid = responseTid;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public Boolean getSendSuccess() {
		return sendSuccess;
	}
	public void setSendSuccess(Boolean sendSuccess) {
		this.sendSuccess = sendSuccess;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getIndonesiaChargingConfigId() {
		return indonesiaChargingConfigId;
	}

	public void setIndonesiaChargingConfigId(Integer indonesiaChargingConfigId) {
		this.indonesiaChargingConfigId = indonesiaChargingConfigId;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getShortCodeToPrepareMtUrl() {
		return shortCodeToPrepareMtUrl;
	}

	public void setShortCodeToPrepareMtUrl(String shortCodeToPrepareMtUrl) {
		this.shortCodeToPrepareMtUrl = shortCodeToPrepareMtUrl;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Integer getTokenId() {
		return tokenId;
	}

	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSubscriberCurrentState() {
		return subscriberCurrentState;
	}

	public void setSubscriberCurrentState(String subscriberCurrentState) {
		this.subscriberCurrentState = subscriberCurrentState;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getMoId() {
		return moId;
	}

	public void setMoId(Integer moId) {
		this.moId = moId;
	}
	
}
