package net.mycomp.mt2.zain.iraq;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_mt2_zain_iraq_delivery_notification")
public class Mt2ZainIraqDeliveryNotification implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="dlr_id")
	private Integer dirId;
	@Column(name="msisdn")
	private String MSISDN;
	@Column(name="date")
	private String Date;
	@Column(name="id")
	private String Id;
	@Column(name="data")
	private String Data;
	@Column(name="status")
	private String Status;
	@Column(name="operator")
	private String Operator;
	@Column(name="short_code")
	private String ShortCode;
	@Column(name="service_id")
	private String ServiceId;
	@Column(name="validaity_days")
	private String ValidaityDays;
	@Column(name="create_date")
	private String createDate;
	private String token;
	private String action;
	@Column(name="price")
	private String Price;
	
	public Integer getDirId() {
		return dirId;
	}
	public void setDirId(Integer dirId) {
		this.dirId = dirId;
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
	public String getMSISDN() {
		return MSISDN;
	}
	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getOperator() {
		return Operator;
	}
	public void setOperator(String operator) {
		Operator = operator;
	}
	public String getShortCode() {
		return ShortCode;
	}
	public void setShortCode(String shortCode) {
		ShortCode = shortCode;
	}
	public String getServiceId() {
		return ServiceId;
	}
	public void setServiceId(String serviceId) {
		ServiceId = serviceId;
	}
	public String getValidaityDays() {
		return ValidaityDays;
	}
	public void setValidaityDays(String validaityDays) {
		ValidaityDays = validaityDays;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	@Override
	public String toString() {
		return "Mt2UAEDeliveryNotification [MSISDN=" + MSISDN + ", Date=" + Date + ", Id=" + Id + ", Data=" + Data
				+ ", Status=" + Status + ", Operator=" + Operator + ", ShortCode=" + ShortCode + ", ServiceId="
				+ ServiceId + ", ValidaityDays=" + ValidaityDays + ", createDate=" + createDate + "]";
	}
	
}
