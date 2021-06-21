package net.mycomp.mobipay;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="tb_mobipay_service_config")
public class MobiPayServiceConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	 
	private Integer id;
	@Column(name="product_id")
	private Integer productId;
	@Column(name="service_id")
	private Integer serviceId;	
	@Column(name="operator_id")
	private Integer operatorId;
	@Column(name="price")
	private Double price;
	@Column(name="cg_url")
	private String cgURL;
	@Column(name="portal_url")
	private String portalURL;
	@Column(name="sms_url")
	private String smsURL;
	@Column(name="welcome_message")
	private String welcomeMessage;
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="status")
	private  Boolean status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public String getCgURL() {
		return cgURL;
	}
	public void setCgURL(String cgURL) {
		this.cgURL = cgURL;
	}

	public String getPortalURL() {
		return portalURL;
	}
	public void setPortalURL(String portalURL) {
		this.portalURL = portalURL;
	}
	public String getSmsURL() {
		return smsURL;
	}
	public void setSmsURL(String smsURL) {
		this.smsURL = smsURL;
	}
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
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
	@Override
	public String toString() {
		return "MobiPayServiceConfig [id=" + id + ", productId=" + productId + ", serviceId=" + serviceId
				+ ", operatorId=" + operatorId + ", cgURL=" + cgURL + ", portalURL=" + portalURL + ", welcomeMessage=" + welcomeMessage + ", createTime=" + createTime
				+ ", status=" + status + "]";
	}
	
}