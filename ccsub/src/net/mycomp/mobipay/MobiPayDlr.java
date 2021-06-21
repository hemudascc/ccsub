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
@Table(name="tb_mobipay_dlr")
public class MobiPayDlr implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="msisdn")
	private String msisdn;
	@Column(name="message_text")
	private String messageText; 
	@Column(name="provider")
	private String provider;
	@Column(name="reference")
	private String reference;
	@Column(name="dlr_id")
	private String dlrId;
	@Column(name="result")
	private String result;
	@Column(name="query_str")
	private String queryStr; 
	@Column(name="token")
	private String token;
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private Boolean status;
	
	MobiPayDlr(){}
	public MobiPayDlr(boolean status) {
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
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDlrId() {
		return dlrId;
	}
	public void setDlrId(String dlrId) {
		this.dlrId = dlrId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
		return "MobiPayDlr [id=" + id + ", msisdn=" + msisdn + ", messageText=" + messageText + ", provider=" + provider
				+ ", reference=" + reference + ", dlrId=" + dlrId + ", result=" + result + ", queryStr=" + queryStr
				+ ", token=" + token + ", createTime=" + createTime + ", status=" + status + "]";
	}
}
