package net.mycomp.mt2.zain.iraq;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_mt2_zain_iraq_notification")
public class Mt2ZainIraqNotification implements Serializable{

	//?Username=&Password=&SubscriptionRef=&MSISDN=&OperatorID=
	//&ServiceTag=&Amount=&CurrencyCode=&CurrencyISOCode=&CurrencySymbol=&CurrencyDescription=&
	//NextRenewalDate=&ChargeStatus=&SubscriptionStatus=&channel=&ServiceId=&operatorId=
	
	//Sub , Get request : PartnerURL?
	//Id=<id from MT2 SDP>
	//&Data=S,<serviceid>,<subscriberReferenceID>
	//&MSISDN=<msisdn>
	//&ShortCode=<shortcode>
	//&Date=20201021
	//&Operator= Zain Iraq
	//&ValidityDays=<days>

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Unsub ,Get request : PartnerURL?Id=<id from MT2 SDP>&Data=U, <serviceid>
	//&MSISDN=<msisdn>&ShortCode=<shortcode>&Date=20201021&Operator= Zain Iraq
	public Mt2ZainIraqNotification(){}
	
	public Mt2ZainIraqNotification(Boolean status) {
		this.status = status;
		this.createTime = new Timestamp(System.currentTimeMillis());
	}
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	
	@Column(name="action")
	private String action;
	
	@Column(name="mt2_id")
	private String mt2id;
	
	@Column(name="data")
	private String data;
	
	@Column(name="msisdn")
	private String msisdn;
	
	@Column(name="short_code")
	private String shortCode;
	
	@Column(name="operator")
	private String operator;
	
	@Column(name="validity_days")
	private String validityDays;
	
	@Column(name="mt2_service_id")
	private String mt2ServiceId;
	
	@Column(name="subscriber_reference_id")
	private String subscriberReferenceID;
	
	@Column(name="token")
	private String token;
	
	@Column(name = "create_time")
	private Timestamp createTime;
	
	@Column(name="date")
	private String date;
	
	@Column(name="send_to_adnetwork")
	private boolean sendToAdnetwork;
	
	@Column(name="query_string")
	private String queryString;
	
	@Column(name="mt2_status")
	private String mtStatus;
	
	@Column(name="status")
	private Boolean status;

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

	public String getMt2id() {
		return mt2id;
	}

	public void setMt2id(String mt2id) {
		this.mt2id = mt2id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValidityDays() {
		return validityDays;
	}

	public void setValidityDays(String validityDays) {
		this.validityDays = validityDays;
	}

	public String getMt2ServiceId() {
		return mt2ServiceId;
	}

	public void setMt2ServiceId(String mt2ServiceId) {
		this.mt2ServiceId = mt2ServiceId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getStatus() {
		return status;
	}
	
	public boolean isSendToAdnetwork() {
		return sendToAdnetwork;
	}

	public void setSendToAdnetwork(boolean sendToAdnetwork) {
		this.sendToAdnetwork = sendToAdnetwork;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getSubscriberReferenceID() {
		return subscriberReferenceID;
	}

	public void setSubscriberReferenceID(String subscriberReferenceID) {
		this.subscriberReferenceID = subscriberReferenceID;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getMtStatus() {
		return mtStatus;
	}

	public void setMtStatus(String mtStatus) {
		this.mtStatus = mtStatus;
	}

	@Override
	public String toString() {
		return "Mt2ZainIraqNotification [id=" + id + ", action=" + action + ", mt2id=" + mt2id + ", data=" + data
				+ ", msisdn=" + msisdn + ", shortCode=" + shortCode + ", operator=" + operator + ", validityDays="
				+ validityDays + ", mt2ServiceId=" + mt2ServiceId + ", subscriberReferenceID=" + subscriberReferenceID
				+ ", token=" + token + ", createTime=" + createTime + ", date=" + date + ", sendToAdnetwork="
				+ sendToAdnetwork + ", status=" + status + "]";
	}
}