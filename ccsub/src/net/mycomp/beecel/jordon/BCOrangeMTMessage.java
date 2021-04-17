package net.mycomp.beecel.jordon;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_bc_mt_message")
public class BCOrangeMTMessage implements Serializable{

	@GeneratedValue
	@Id
	private int id;
	@Column(name = "msisdn")
	private String msisdn;
	@Column(name = "mtid")
	private String mtid;
	@Column(name = "serviceId")  
	private int serviceId;
	@Column(name = "request_str")
	private String requestStr;
	@Column(name = "respose_str")
	private String responseStr;
	@Column(name = "create_time")
	private Timestamp createTime;
	private boolean status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getMtid() {
		return mtid;
	}
	public void setMtid(String mtid) {
		this.mtid = mtid;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getRequestStr() {
		return requestStr;
	}
	public void setRequestStr(String requestStr) {
		this.requestStr = requestStr;
	}
	public String getResponseStr() {
		return responseStr;
	}
	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}	

	

}
