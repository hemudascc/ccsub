package net.mycomp.cornet.sudan;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

@Entity
@Table(name = "tb_cornet_notification")
public class CornetNotification implements Serializable{

	@Id
	@GeneratedValue
	private long id;
	@Column(name="msisdn")
	private String msisdn;
	@Column(name="price")
	private double price;
	@Column(name="product_code")
	private String product_code;
	@Column(name="sub_date")
	private String sub_date;
	@Column(name="unsub_date")
	private String unsub_date;
	@Column(name="token")
	private String token;
	@Column(name="transaction_type")
	private String transactionType;
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private int status;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getSub_date() {
		return sub_date;
	}
	public void setSub_date(String sub_date) {
		this.sub_date = sub_date;
	}
	public String getUnsub_date() {
		return unsub_date;
	}
	public void setUnsub_date(String unsub_date) {
		this.unsub_date = unsub_date;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "CornetNotification [id=" + id + ", msisdn=" + msisdn + ", price=" + price + ", product_code="
				+ product_code + ", sub_date=" + sub_date + ", unsub_date=" + unsub_date + ", token=" + token
				+ ", transactionType=" + transactionType + ", createTime=" + createTime + ", status=" + status + "]";
	}


}
