package net.mycomp.mobipay;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_mobipay_cgcallack")
public class MobiPayCGCallback implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="user_id")
	private String userId;
	@Column(name="network")
	private String network;
	@Column(name="cg_status")
	private String cgStatus;
	@Column(name="token")
	private String token;
	@Column(name="portal_url")
	private String portalURL;
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private Boolean status;
	
	MobiPayCGCallback(){}
	MobiPayCGCallback(Boolean status){
		this.status=status;
		this.createTime= new Timestamp(System.currentTimeMillis());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getCgStatus() {
		return cgStatus;
	}
	public void setCgStatus(String cgStatus) {
		this.cgStatus = cgStatus;
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
	public String getPortalURL() {
		return portalURL;
	}
	public void setPortalURL(String portalURL) {
		this.portalURL = portalURL;
	}
	@Override
	public String toString() {
		return "MobiPayCGCallback [id=" + id + ", userId=" + userId + ", network=" + network + ", cgStatus=" + cgStatus
				+ ", token=" + token + ", createTime=" + createTime + ", status=" + status + "]";
	}
}
