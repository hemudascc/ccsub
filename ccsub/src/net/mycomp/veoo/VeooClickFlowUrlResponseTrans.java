package net.mycomp.veoo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_veoo_click_flow_trans")
public class VeooClickFlowUrlResponseTrans implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	private String msisdn;	
	@Column(name = "token_to_cg")
	private String tokenToCg;
	private String request;
	private String response;
	private String result;
	@Column(name = "click_flow_response_status")
	private String clickFlowResponseStatus;
	@Column(name = "redirect_url")
	private String redirectUrl;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public VeooClickFlowUrlResponseTrans(){}
	public VeooClickFlowUrlResponseTrans(boolean status){
		this.status=status;
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
	
	public String getTokenToCg() {
		return tokenToCg;
	}
	public void setTokenToCg(String tokenToCg) {
		this.tokenToCg = tokenToCg;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getClickFlowResponseStatus() {
		return clickFlowResponseStatus;
	}
	public void setClickFlowResponseStatus(String clickFlowResponseStatus) {
		this.clickFlowResponseStatus = clickFlowResponseStatus;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
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
	
}
