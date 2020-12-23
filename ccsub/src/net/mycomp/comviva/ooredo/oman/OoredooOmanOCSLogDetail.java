package net.mycomp.comviva.ooredo.oman;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name= "tb_ooredoo_oman_ocs_log_detail")
public class OoredooOmanOCSLogDetail implements Serializable{

	
	private static final long serialVersionUID = 1L;
	@Id
	//@GeneratedValue
	private Integer id;
	@Column(name = "msisdn")
	private String msisdn;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "config_id")
	private Integer configId;
	
	@Column(name = "action")
	private String action;
	@Column(name = "request")
	private String requet;
	@Column(name = "response_code")
	private String responseCode;
	
	@Column(name = "response")
	private String response;
	
	@Column(name = "success")
	private Boolean success;
	
	@Column(name = "send_to_adnetwork")
	private Boolean sendToAdnetwork;
	
	@Column(name = "create_date")
	private Timestamp createDate;
	@Column(name = "status")
	private Boolean status;
	
	public OoredooOmanOCSLogDetail(){}
	
	public OoredooOmanOCSLogDetail(Boolean status,String action){
		
		this.id=OoredooOmanConstant.ooredooOmanOCSLogDetailId.incrementAndGet();
		this.createDate=new Timestamp(System.currentTimeMillis());
		this.status=status;
		this.action=action;
		this.success=false;
		this.sendToAdnetwork=false;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRequet() {
		return requet;
	}
	public void setRequet(String requet) {
		this.requet = requet;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Boolean getSendToAdnetwork() {
		return sendToAdnetwork;
	}

	public void setSendToAdnetwork(Boolean sendToAdnetwork) {
		this.sendToAdnetwork = sendToAdnetwork;
	}
}
