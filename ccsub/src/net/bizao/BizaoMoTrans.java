package net.bizao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_bizao_mo_trans")
public class BizaoMoTrans implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "action")
	private String action;
	
	@Column(name = "destination_address")
	private String destinationAddress;
	
	@Column(name = "bizao_token")
	private String bizaoToken;
	@Column(name = "bizao_alias")
	private String bizaoAlias;
	private String message;
	private String request;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public BizaoMoTrans(){
		createTime=new Timestamp(System.currentTimeMillis());
		status=true;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBizaoToken() {
		return bizaoToken;
	}
	public void setBizaoToken(String bizaoToken) {
		this.bizaoToken = bizaoToken;
	}
	public String getBizaoAlias() {
		return bizaoAlias;
	}
	public void setBizaoAlias(String bizaoAlias) {
		this.bizaoAlias = bizaoAlias;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	
	
}
