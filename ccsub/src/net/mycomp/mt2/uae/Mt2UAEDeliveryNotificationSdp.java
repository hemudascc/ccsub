package net.mycomp.mt2.uae;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tb_mt2_uae_delivery_notification_sdp")
public class Mt2UAEDeliveryNotificationSdp implements Serializable {
	
	//[{"MSISDN":"971523051932","Date":"20202212","Id":"637442370408068041","Data":"90570635572.PRCH","Status":"Success","Operator":"Du",
	//"ShortCode":"9132","ServiceId":392,"Price":10.00,"ValidityDays":7}]
	private static final long serialVersionUID = 1L;
	
	public Mt2UAEDeliveryNotificationSdp(){}
	public Mt2UAEDeliveryNotificationSdp(Boolean status) {
		this.status = status;
		this.createTime = new Timestamp(System.currentTimeMillis());
	}
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;
	@Column(name = "sdp_id")
	@JsonProperty("Id")
	private String sdpId;
	@Column(name = "data")
	@JsonProperty("Data")
	private String data;
	@Column(name = "msisdn")
	@JsonProperty("MSISDN")
	private String msisdn;
	@Column(name = "sdp_date")
	@JsonProperty("Date")
	private String date;
	@Column(name = "sdp_status")
	@JsonProperty("Status")
	private String sdpStatus;
	@Column(name = "operator")
	@JsonProperty("Operator")
	private String operator;
	@Column(name = "short_code")
	@JsonProperty("ShortCode")
	private String shortCode;
	@Column(name = "sdp_service_id")
	@JsonProperty("ServiceId")
	private Integer serviceId;
	@Column(name = "price")
	@JsonProperty("Price")
	private Double price;
	@Column(name = "validity")
	@JsonProperty("ValidityDays")
	private Integer validity;
	@Column(name="token")
	private String token;
	@Column(name="action")
	private String action;
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private Boolean status;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSdpId() {
		return sdpId;
	}
	public void setSdpId(String sdpId) {
		this.sdpId = sdpId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSdpStatus() {
		return sdpStatus;
	}
	public void setSdpStatus(String sdpStatus) {
		this.sdpStatus = sdpStatus;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
		return "Mt2UAEDeliveryNotification [id=" + id + ", sdpId=" + sdpId + ", data=" + data + ", msisdn=" + msisdn
				+ ", date=" + date + ", sdpStatus=" + sdpStatus + ", operator=" + operator + ", shortCode=" + shortCode
				+ ", serviceId=" + serviceId + ", price=" + price + ", validity=" + validity + ", token=" + token
				+ ", action=" + action + ", createTime=" + createTime + ", status=" + status + "]";
	}
}
