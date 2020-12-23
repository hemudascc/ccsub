package net.mycomp.mt2.ksa;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mt2_ksa_service_config")
public class Mt2KSAServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name="mt2_service_id")
	private String mt2ServiceId;
	
	@Column(name = "service_name")
	private String serviceName;
	
	@Column(name = "operator_name")
	private String operatorName;
	
	@Column(name="operator_name_desc")
	private String operatorNameDesc;
	
	
	@Column(name="client_username")
	private String clientUsername;
	@Column(name="client_password")
	private String clientPassword;
	@Column(name="merchant_id")
	private String merchantId;
	@Column(name="short_code")
	private String shortCode;
	
	@Column(name="unsub_short_code")
	private String unsubShortCode;
	
	@Column(name="sub_key")
	private String subKey;
	@Column(name="unsub_key")
	private String unsubKey;
	
	@Column(name = "send_pin_url")
	private String sendPinUrl;
	@Column(name = "validate_pin_url")
	private String validatePinUrl;
	@Column(name="unsub_url")
	private String unsubUrl;
	
	@Column(name="sub_status_url")
	private String subStatusUrl;
	@Column(name="send_sms_url")
	private String sendSmsUrl;
	@Column(name="add_content_url")
	private String addContentUrl;
	
	@Column(name = "portal_url")
	private String portalUrl;
	@Column(name = "price_point")
	private Double pricePoint;
	@Column(name="price_point_desc")
	private String pricePointDesc;
	
	private Integer validity;
	@Column(name="validity_desc")
	private String validityDesc;
	
	@Column(name="lp_img")
	private String lpImg;
	@Column(name="alert_sms_template")
	private String alertSmsTemplate;
	
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
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getSendPinUrl() {
		return sendPinUrl;
	}
	public void setSendPinUrl(String sendPinUrl) {
		this.sendPinUrl = sendPinUrl;
	}
	public String getValidatePinUrl() {
		return validatePinUrl;
	}
	public void setValidatePinUrl(String validatePinUrl) {
		this.validatePinUrl = validatePinUrl;
	}
	public String getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
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
	public String getMt2ServiceId() {
		return mt2ServiceId;
	}
	public void setMt2ServiceId(String mt2ServiceId) {
		this.mt2ServiceId = mt2ServiceId;
	}
	public String getClientUsername() {
		return clientUsername;
	}
	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}
	public String getClientPassword() {
		return clientPassword;
	}
	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getSubKey() {
		return subKey;
	}
	public void setSubKey(String subKey) {
		this.subKey = subKey;
	}
	public String getUnsubKey() {
		return unsubKey;
	}
	public void setUnsubKey(String unsubKey) {
		this.unsubKey = unsubKey;
	}
	public String getUnsubUrl() {
		return unsubUrl;
	}
	public void setUnsubUrl(String unsubUrl) {
		this.unsubUrl = unsubUrl;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getPricePointDesc() {
		return pricePointDesc;
	}
	public void setPricePointDesc(String pricePointDesc) {
		this.pricePointDesc = pricePointDesc;
	}
	public String getValidityDesc() {
		return validityDesc;
	}
	public void setValidityDesc(String validityDesc) {
		this.validityDesc = validityDesc;
	}
	public String getLpImg() {
		return lpImg;
	}
	public void setLpImg(String lpImg) {
		this.lpImg = lpImg;
	}
	public String getSubStatusUrl() {
		return subStatusUrl;
	}
	public void setSubStatusUrl(String subStatusUrl) {
		this.subStatusUrl = subStatusUrl;
	}
	public String getSendSmsUrl() {
		return sendSmsUrl;
	}
	public void setSendSmsUrl(String sendSmsUrl) {
		this.sendSmsUrl = sendSmsUrl;
	}
	public String getAddContentUrl() {
		return addContentUrl;
	}
	public void setAddContentUrl(String addContentUrl) {
		this.addContentUrl = addContentUrl;
	}
	public String getOperatorNameDesc() {
		return operatorNameDesc;
	}
	public void setOperatorNameDesc(String operatorNameDesc) {
		this.operatorNameDesc = operatorNameDesc;
	}
	public String getUnsubShortCode() {
		return unsubShortCode;
	}
	public void setUnsubShortCode(String unsubShortCode) {
		this.unsubShortCode = unsubShortCode;
	}
	public String getAlertSmsTemplate() {
		return alertSmsTemplate;
	}
	public void setAlertSmsTemplate(String alertSmsTemplate) {
		this.alertSmsTemplate = alertSmsTemplate;
	}
	
}
