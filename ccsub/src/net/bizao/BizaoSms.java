package net.bizao;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_bizao_sms")
public class BizaoSms {

	@Id
	private Integer id;
	@Column(name="action")
	private String action;	
	@Column(name="bizao_token")
	private String bizaoToken;
	@Column(name="bizao_alias")
	private String bizaoAlias;
	
	@Column(name = "sender_address")
	private String senderAddress;
	@Column(name = "text_msg")
	private String textMsg;
	@Column(name = "sender_name")
	private String senderName;
	private String request;
	private String response;
	@Column(name = "request_time")
	private Timestamp requestTime;
	@Column(name = "dlr_received")
	private Boolean dlrReceived;
	@Column(name = "dlr_received_time")
	private Timestamp dlrReceivedTime;
	private Boolean status;
	
	public BizaoSms(){
		
		id=BizaoConstant.bizaoSmsAtomicInteger.incrementAndGet();
		
		requestTime=new Timestamp(System.currentTimeMillis());
		status=true;
	}
	
	public BizaoSms(Boolean status){
		id=BizaoConstant.bizaoSmsAtomicInteger.incrementAndGet();
		requestTime=new Timestamp(System.currentTimeMillis());
		status=true;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getTextMsg() {
		return textMsg;
	}
	public void setTextMsg(String textMsg) {
		this.textMsg = textMsg;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
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
	public Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	public Boolean getDlrReceived() {
		return dlrReceived;
	}
	public void setDlrReceived(Boolean dlrReceived) {
		this.dlrReceived = dlrReceived;
	}
	public Timestamp getDlrReceivedTime() {
		return dlrReceivedTime;
	}
	public void setDlrReceivedTime(Timestamp dlrReceivedTime) {
		this.dlrReceivedTime = dlrReceivedTime;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getBizaoToken() {
		return bizaoToken;
	}

	public void setBizaoToken(String bizaoToken) {
		this.bizaoToken = bizaoToken;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBizaoAlias() {
		return bizaoAlias;
	}

	public void setBizaoAlias(String bizaoAlias) {
		this.bizaoAlias = bizaoAlias;
	}
}
