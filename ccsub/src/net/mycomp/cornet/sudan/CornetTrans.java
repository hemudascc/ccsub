package net.mycomp.cornet.sudan;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_cornet_trans")
public class CornetTrans {

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="request_type")
	private String requestType;
	@Column(name="token")
	private String token;
	@Column(name="token_id")
	private Integer tokenId;
	@Column(name="msisdn")
	private String msisdn;
	@Column(name="pin")
	private int pin;
	@Column(name="request", columnDefinition ="TEXT")
	private String request;
	@Column(name="response",columnDefinition ="TEXT")
	private String resposne;
	@Column(name="response_code")
	private Integer responseCode;
	@Column(name="is_active")
	private int isActive;
	@Column(name="sub_date_unix")
	private long subDateUnix;
	@Column(name="unsub_date_unix")
	private long unSubDateUnix;
	@Column(name="retry_date_unix")
	private long retryDateUnix;
	@Column(name="price")
	private double price;  	
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private Boolean status;
	
	public CornetTrans() {}
	public CornetTrans(Boolean status) {
		this.createTime = new Timestamp(System.currentTimeMillis());
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getTokenId() {
		return tokenId;
	}
	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResposne() {
		return resposne;
	}
	public void setResposne(String resposne) {
		this.resposne = resposne;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public long getSubDateUnix() {
		return subDateUnix;
	}
	public void setSubDateUnix(long subDateUnix) {
		this.subDateUnix = subDateUnix;
	}
	public long getUnSubDateUnix() {
		return unSubDateUnix;
	}
	public void setUnSubDateUnix(long unSubDateUnix) {
		this.unSubDateUnix = unSubDateUnix;
	}
	public long getRetryDateUnix() {
		return retryDateUnix;
	}
	public void setRetryDateUnix(long retryDateUnix) {
		this.retryDateUnix = retryDateUnix;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
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
