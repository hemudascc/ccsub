package net.mycomp.mt2.uae;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mt2_uae_cgcallback_sdp")
public class Mt2UAECGCallbackSdp implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "tracking_id")
	private String trackingId;
	@Column(name = "token")
	private String token;
	@Column(name = "query_str")
	private String queryStr;	
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private Boolean status;
	
	public Mt2UAECGCallbackSdp(){}
	public Mt2UAECGCallbackSdp(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
		return "Mt2UAECGCallback [id=" + id + ", trackingId=" + trackingId + ", token=" + token + ", queryStr="
				+ queryStr + ", createTime=" + createTime + ", status=" + status + "]";
	}
}
