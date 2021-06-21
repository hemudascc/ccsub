package net.mycomp.mt2.ksa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_mt2_ksa_notification_sdp")
public class Mt2KSANotificationSdp implements Serializable {

	//Id=20122212303719741&Data=S,392,87dba3d7-fba1-40f2-9f6b-b9dacb374754
	//&TrackingId=aaa2086a11f048adb81cabd162e2f4d2&MSISDN=971523051932&ShortCode=9132&Date=20202212&Operator=Du&ValidityDays=0
	private static final long serialVersionUID = 1L;
	public Mt2KSANotificationSdp() {}
	public Mt2KSANotificationSdp(Boolean status) {
		this.status = status;
		this.createTime = new Timestamp(System.currentTimeMillis());
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name="sdp_id")
	private String sdpId;
	@Column(name="data")
	private String data;
	@Column(name="tracking_id")
	private String trackingId;
	@Column(name="msisdn")
	private String msisdn;
	@Column(name="short_code")
	private String shortCode;
	@Column(name="sdp_date")
	private String sdpDate;
	@Column(name="operator")
	private String operator;
	@Column(name="validity")
	private String validity;
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="token")
	private String token;
	@Column(name="action")
	private String action;
	@Column(name="sdp_service_id")
	private String sdpServiceId;
	@Column(name="reference_id")
	private String referenceId;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name="status")
	private Boolean status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSdpId() {
		return sdpId;
	}
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getSdpDate() {
		return sdpDate;
	}
	public void setSdpDate(String sdpDate) {
		this.sdpDate = sdpDate;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSdpServiceId() {
		return sdpServiceId;
	}
	public void setSdpServiceId(String sdpServiceId) {
		this.sdpServiceId = sdpServiceId;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	@Override
	public String toString() {
		return "Mt2KSANotificationSdp [id=" + id + ", sdpId=" + sdpId + ", data=" + data + ", trackingId=" + trackingId
				+ ", msisdn=" + msisdn + ", shortCode=" + shortCode + ", sdpDate=" + sdpDate + ", operator=" + operator
				+ ", validity=" + validity + ", createTime=" + createTime + ", token=" + token + ", action=" + action
				+ ", sdpServiceId=" + sdpServiceId + ", referenceId=" + referenceId + ", queryStr=" + queryStr
				+ ", status=" + status + "]";
	}
	
	
}
