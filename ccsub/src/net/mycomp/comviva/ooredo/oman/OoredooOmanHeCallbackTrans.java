package net.mycomp.comviva.ooredo.oman;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_ooredoo_oman_he_callback_trans")
public class OoredooOmanHeCallbackTrans implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "status_code")
	private String statusCode;
	@Column(name = "correlator_id")
	private String correlatorId;
	private String token;
	@Column(name = "encypt_msisdn")
	private String encyptMsisdn;
	private String msisdn;
	@Column(name = "query_str")
	private String queryStr;	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean  status;
	
	public OoredooOmanHeCallbackTrans(){}
	public OoredooOmanHeCallbackTrans(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getCorrelatorId() {
		return correlatorId;
	}
	public void setCorrelatorId(String correlatorId) {
		this.correlatorId = correlatorId;
	}
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getEncyptMsisdn() {
		return encyptMsisdn;
	}
	public void setEncyptMsisdn(String encyptMsisdn) {
		this.encyptMsisdn = encyptMsisdn;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
