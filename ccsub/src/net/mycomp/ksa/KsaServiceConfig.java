package net.mycomp.ksa;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_ksa_config")
public class KsaServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name="service_name")
	private String serviceName;
	
	@Column(name = "ksa_service_id")
	private String ksaServiceId;
	@Column(name = "ksa_operator_id")
	private String ksaOperatorId;
	
	@Column(name = "validity")
	private Integer validity;
	
	@Column(name = "price_point")
	private Double pricePoint;
	
	@Column(name = "price_point_desc")
	private String pricePointDesc;
	
	@Column(name = "unsub_command")
	private String unsubCommand;
	
	@Column(name = "short_code")
	private String shortCode;
	
	@Column(name = "portal_url")
	private String portalUrl;
	
	@Column(name = "profile_check_url")
	private String profileCheckUrl;
	@Column(name = "subscription_pin_url")
	private String subscriptionPinUrl;
	@Column(name = "subscription_pin_validation_url")
	private String subscriptionPinValidationUrl;
	@Column(name = "sms_push_url")
	private String smsPushUrl;
	@Column(name = "unsubscription_url")
	private String unsubscriptionUrl;
	
	@Column(name = "bulk_sms_url")
	private String bulkSmsUrl;
	
	@Column(name = "subscription_msg_template")
	private String subscriptionMsgTemplate;
	
	@Column(name = "unsubscription_msg_template")
	private String unsubscriptionMsgTemplate;
	
	@Column(name = "renewal_sub_id")
	private Integer renewalSubId;
	
	@Column(name = "renewal_msg_template")
	private String renewalMsgTemplate;
	
	
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
	public String getKsaServiceId() {
		return ksaServiceId;
	}
	public void setKsaServiceId(String ksaServiceId) {
		this.ksaServiceId = ksaServiceId;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getKsaOperatorId() {
		return ksaOperatorId;
	}
	public void setKsaOperatorId(String ksaOperatorId) {
		this.ksaOperatorId = ksaOperatorId;
	}
	public String getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}
	public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getSubscriptionMsgTemplate() {
		return subscriptionMsgTemplate;
	}
	public void setSubscriptionMsgTemplate(String subscriptionMsgTemplate) {
		this.subscriptionMsgTemplate = subscriptionMsgTemplate;
	}
	public String getUnsubscriptionMsgTemplate() {
		return unsubscriptionMsgTemplate;
	}
	public void setUnsubscriptionMsgTemplate(String unsubscriptionMsgTemplate) {
		this.unsubscriptionMsgTemplate = unsubscriptionMsgTemplate;
	}
	public String getProfileCheckUrl() {
		return profileCheckUrl;
	}
	public void setProfileCheckUrl(String profileCheckUrl) {
		this.profileCheckUrl = profileCheckUrl;
	}
	public String getSubscriptionPinUrl() {
		return subscriptionPinUrl;
	}
	public void setSubscriptionPinUrl(String subscriptionPinUrl) {
		this.subscriptionPinUrl = subscriptionPinUrl;
	}
	public String getSubscriptionPinValidationUrl() {
		return subscriptionPinValidationUrl;
	}
	public void setSubscriptionPinValidationUrl(String subscriptionPinValidationUrl) {
		this.subscriptionPinValidationUrl = subscriptionPinValidationUrl;
	}
	public String getSmsPushUrl() {
		return smsPushUrl;
	}
	public void setSmsPushUrl(String smsPushUrl) {
		this.smsPushUrl = smsPushUrl;
	}
	public String getUnsubscriptionUrl() {
		return unsubscriptionUrl;
	}
	public void setUnsubscriptionUrl(String unsubscriptionUrl) {
		this.unsubscriptionUrl = unsubscriptionUrl;
	}
	public String getBulkSmsUrl() {
		return bulkSmsUrl;
	}
	public void setBulkSmsUrl(String bulkSmsUrl) {
		this.bulkSmsUrl = bulkSmsUrl;
	}
	public Integer getRenewalSubId() {
		return renewalSubId;
	}
	public void setRenewalSubId(Integer renewalSubId) {
		this.renewalSubId = renewalSubId;
	}
	public String getRenewalMsgTemplate() {
		return renewalMsgTemplate;
	}
	public void setRenewalMsgTemplate(String renewalMsgTemplate) {
		this.renewalMsgTemplate = renewalMsgTemplate;
	}
	public Double getPricePoint() {
		return pricePoint;
	}
	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}
	public String getPricePointDesc() {
		return pricePointDesc;
	}
	public void setPricePointDesc(String pricePointDesc) {
		this.pricePointDesc = pricePointDesc;
	}
	public String getUnsubCommand() {
		return unsubCommand;
	}
	public void setUnsubCommand(String unsubCommand) {
		this.unsubCommand = unsubCommand;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
}
