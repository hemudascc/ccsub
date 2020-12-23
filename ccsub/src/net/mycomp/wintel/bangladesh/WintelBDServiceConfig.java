package net.mycomp.wintel.bangladesh;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_wintel_bd_service_config")
public class WintelBDServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "op_id")
	private Integer opId;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "client_id")
	private String clientId;
	@Column(name = "bd_service_id")
	private String bdServiceId;
	
	@Column(name = "operator")
	private String operator;
	
	@Column(name = "mt_url")
	private String mtUrl;
	@Column(name="short_code")
	private String shortCode;
	
	private String keyword;
	@Column(name = "price_point")
	private Double pricePoint;
	@Column(name = "currency")
	private String currency;
	
	private Integer validity;
	@Column(name = "validity_desc")
	private String validityDesc;
	
	@Column(name="charging_url")
	private String chargingUrl;
	@Column(name="sub_msg_template")
	private String subMsgTemplate;
	
	@Column(name="renewal_msg_template")
	private String renewalMsgTemplate;
	
	
	@Column(name="portal_url")
	private String portalUrl;
	
	private Boolean status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Double getPricePoint() {
		return pricePoint;
	}
	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}
	public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getChargingUrl() {
		return chargingUrl;
	}
	public void setChargingUrl(String chargingUrl) {
		this.chargingUrl = chargingUrl;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getMtUrl() {
		return mtUrl;
	}
	public void setMtUrl(String mtUrl) {
		this.mtUrl = mtUrl;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getSubMsgTemplate() {
		return subMsgTemplate;
	}
	public void setSubMsgTemplate(String subMsgTemplate) {
		this.subMsgTemplate = subMsgTemplate;
	}
	public Integer getOpId() {
		return opId;
	}
	public void setOpId(Integer opId) {
		this.opId = opId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getValidityDesc() {
		return validityDesc;
	}
	public void setValidityDesc(String validityDesc) {
		this.validityDesc = validityDesc;
	}
	public String getBdServiceId() {
		return bdServiceId;
	}
	public void setBdServiceId(String bdServiceId) {
		this.bdServiceId = bdServiceId;
	}
	public String getRenewalMsgTemplate() {
		return renewalMsgTemplate;
	}
	public void setRenewalMsgTemplate(String renewalMsgTemplate) {
		this.renewalMsgTemplate = renewalMsgTemplate;
	}
	
}
