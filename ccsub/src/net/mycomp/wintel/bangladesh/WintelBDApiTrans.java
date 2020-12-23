package net.mycomp.wintel.bangladesh;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_wintel_bd_api_trans")
public class WintelBDApiTrans {

	@Id
	@GeneratedValue
	private Integer id;
	private String action;
	private String msisdn; 
	private String request;
	private String response;
	private Boolean success;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public WintelBDApiTrans(){}
	public WintelBDApiTrans(Boolean status,String action){
		this.status=status;
		this.action=action;
		this.createTime=new Timestamp(System.currentTimeMillis());
		this.success=false;
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
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
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
