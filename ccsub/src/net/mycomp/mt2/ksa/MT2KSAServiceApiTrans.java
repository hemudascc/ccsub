package net.mycomp.mt2.ksa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.thialand.ThiaConstant;

@Entity
@Table(name = "tb_mt2_ksa_service_api_trans")
public class MT2KSAServiceApiTrans implements Serializable{

	@Id
//	@GeneratedValue
	private Integer id;
	
	private String action;
	@Column(name = "msisdn")
	private String msisdn;
	private String token;
	@Column(name = "campaign_id")
	private Integer campaignId;
	private String request;
	@Column(name = "response_code")
	private Integer responseCode;
	private String response;
	private Boolean success;
	@Column(name = "send_to_adnetwork")
	private Boolean sendToAdnetwork;
	
	@Column(name = "response_to_caller")
	private Boolean responseToCaller;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public MT2KSAServiceApiTrans(){}
	public MT2KSAServiceApiTrans(boolean status,String action){
		
		this.id= Mt2KSAConstant.mt2KSAServiceApiTransIdAtomicInteger.incrementAndGet();
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.responseToCaller=false;
		this.success=false;
		this.action=action;
		
	}
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public Boolean getResponseToCaller() {
		return responseToCaller;
	}
	public void setResponseToCaller(Boolean responseToCaller) {
		this.responseToCaller = responseToCaller;
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
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public Boolean getSendToAdnetwork() {
		return sendToAdnetwork;
	}
	public void setSendToAdnetwork(Boolean sendToAdnetwork) {
		this.sendToAdnetwork = sendToAdnetwork;
	}
}
