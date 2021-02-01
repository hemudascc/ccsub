package net.mycomp.mt2.uae;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mt2_uae_notification")
public class Mt2UAENotification implements Serializable{

	//?Username=&Password=&SubscriptionRef=&MSISDN=&OperatorID=
	//&ServiceTag=&Amount=&CurrencyCode=&CurrencyISOCode=&CurrencySymbol=&CurrencyDescription=&
	//NextRenewalDate=&ChargeStatus=&SubscriptionStatus=&channel=&ServiceId=&operatorId=
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "my_action")
	private String myAction;	
	
	private String userName;	
	private String password;	
	@Column(name = "subscription_ref")
	private String subscriptionRef;	
	private String msisdn;	
	@Column(name = "operator_id")
	private String operatorID;	
	@Column(name = "service_tag")
	private String serviceTag;	
	private String amount;	
	@Column(name = "currency_code")
	private String currencyCode;	
	@Column(name = "currency_iso_code")
	private String currencyISOCode;	
	@Column(name = "currency_symbol")
	private String currencySymbol;	
	@Column(name = "currency_description")
	private String currencyDescription;	
	@Column(name = "next_renewal_date")
	private String nextRenewalDate;	
	@Column(name = "charge_status")
	private String chargeStatus;	
	@Column(name = "subscription_status")
	private String subscriptionStatus;	
	private String channel;	
	@Column(name = "service_id")
	private String serviceId;	
	@Column(name="trx_id")
	private String trxid;
	
	@Column(name="send_to_adnetwork")
	private Boolean sendToAdnetwork;
	
	@Column(name = "query_str")
	private String queryStr;	
	@Column(name = "token")
	private String token;	
	
	@Column(name = "crete_time")
	private Timestamp createTime;	
	private Boolean status;
	
	public Mt2UAENotification(){}
	public Mt2UAENotification(boolean status){
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSubscriptionRef() {
		return subscriptionRef;
	}
	public void setSubscriptionRef(String subscriptionRef) {
		this.subscriptionRef = subscriptionRef;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getOperatorID() {
		return operatorID;
	}
	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}
	public String getServiceTag() {
		return serviceTag;
	}
	public void setServiceTag(String serviceTag) {
		this.serviceTag = serviceTag;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyISOCode() {
		return currencyISOCode;
	}
	public void setCurrencyISOCode(String currencyISOCode) {
		this.currencyISOCode = currencyISOCode;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getCurrencyDescription() {
		return currencyDescription;
	}
	public void setCurrencyDescription(String currencyDescription) {
		this.currencyDescription = currencyDescription;
	}
	public String getNextRenewalDate() {
		return nextRenewalDate;
	}
	public void setNextRenewalDate(String nextRenewalDate) {
		this.nextRenewalDate = nextRenewalDate;
	}
	
	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}
	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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
	
	public String getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMyAction() {
		return myAction;
	}
	public void setMyAction(String myAction) {
		this.myAction = myAction;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTrxid() {
		return trxid;
	}
	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}
	public Boolean getSendToAdnetwork() {
		return sendToAdnetwork;
	}
	public void setSendToAdnetwork(Boolean sendToAdnetwork) {
		this.sendToAdnetwork = sendToAdnetwork;
	}
}
