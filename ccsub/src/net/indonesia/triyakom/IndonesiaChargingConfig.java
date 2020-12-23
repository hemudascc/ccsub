package net.indonesia.triyakom;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_indonesia_charging_config")
public class IndonesiaChargingConfig {

	@Id
	private Integer id;
	@Column(name = "type")
	private String type;
	
	
	
	@Column(name = "service")
	private String service;
	@Column(name = "operator_id")
	private Integer operatorId;
	private String operator;
	@Column(name="short_code")
	private String shortCode;
	@Column(name="short_code_to_prepare_mt_url")
	private String shortCodeToPrepareMtUrl;
	
	@Column(name = "app_id")
	private String appId;
	@Column(name = "app_pwd")
	private String appPwd;
	private Integer charge;
	@Column(name = "validity")
	private Integer validity;
	@Column(name = "flag_pull")
	private Integer flagPull;
	@Column(name = "alphabet_id")
	private Integer alphabetid;
	private String note;
	private Boolean status;
	@Column(name = "sub_message")
	private String subMessage;
	@Column(name = "unsub_message")
	private String unsubMessage;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public Integer getCharge() {
		return charge;
	}
	public void setCharge(Integer charge) {
		this.charge = charge;
	}
	public Integer getFlagPull() {
		return flagPull;
	}
	public void setFlagPull(Integer flagPull) {
		this.flagPull = flagPull;
	}
	public Integer getAlphabetid() {
		return alphabetid;
	}
	public void setAlphabetid(Integer alphabetid) {
		this.alphabetid = alphabetid;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubMessage() {
		return subMessage;
	}
	public void setSubMessage(String subMessage) {
		this.subMessage = subMessage;
	}
	public String getUnsubMessage() {
		return unsubMessage;
	}
	public void setUnsubMessage(String unsubMessage) {
		this.unsubMessage = unsubMessage;
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
	public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	
}
