package net.mycomp.cornet.sudan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_cornet_config")
public class CornetConfig {


	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="password")
	private String password;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name = "operator_id")
	private Integer operatorId;
	
	@Column(name="service_name")
	private String serviceName;
	
	@Column(name="product_code")
	private String ProductCode;
	
	@Column(name="product_id")
	private int ProductId;
	
	@Column(name="country")
	private String country;
	
	@Column(name="operator_name")
	private String operatorName;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="price")
	private double price;
	
	@Column(name="portal_url")
	private String portalUrl;
	
	@Column(name="status")
	private Boolean status =true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}

	public int getProductId() {
		return ProductId;
	}

	public void setProductId(int productId) {
		ProductId = productId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CornetConfig [id=" + id + ", userName=" + userName + ", password=" + password + ", serviceId="
				+ serviceId + ", operatorId=" + operatorId + ", serviceName=" + serviceName + ", ProductCode="
				+ ProductCode + ", ProductId=" + ProductId + ", country=" + country + ", operatorName=" + operatorName
				+ ", currency=" + currency + ", price=" + price + ", portalUrl=" + portalUrl + ", status=" + status
				+ "]";
	}



	
}
