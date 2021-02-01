package net.mycomp.mt2.uae;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_mt2_uae_tracking_sdp")
public class Mt2UAETracking implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="token")
	private String token;
	@Column(name="msisdn")
	private String msisdn;
	@Column(name = "query_str")
	private String queryStr;	
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private Boolean status;
	
	public Mt2UAETracking(){}
	public Mt2UAETracking(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	@Override
	public String toString() {
		return "Mt2UAETracking [id=" + id + ", token=" + token + ", msisdn=" + msisdn + ", queryStr=" + queryStr
				+ ", createTime=" + createTime + ", status=" + status + "]";
	}
}
