package net.mycomp.mobivate;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_mobivate_service_config")
public class MobivateServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId; 
	
	@Column(name = "cc_product_id")
	private Integer ccProductId;
	
	@Column(name = "cc_op_id")
	private Integer ccOpId;
	
	@Column(name="network_name")
	private String networkName;	
	@Column(name="time_zone")
	private String timeZone;
	
	@Column(name = "sms_user_name")
	private String smsUserName;
	@Column(name = "sms_password")
	private String smsPassword;
	@Column(name = "sms_provider")
	private String smsProvider;
	@Column(name = "sms_value")
	private String smsValue;
	@Column(name = "sms_keyword")
	private String smsKeyword;
	
	@Column(name = "service_name")
	private String serviceName;
	private String shortcode;
	@Column(name = "product_id")
	private String productId;
	private String keyword;
	@Column(name = "billing_frequency")
	private String billingFrequency;
	@Column(name = "validity")
	private Integer validity;
	@Column(name="validity_desc")
	private String validityDesc;
	@Column(name = "campaign_name")
	private String campaignName;
	@Column(name = "billing_amount")
	private Double billingAmount;
	@Column(name="billing_amount_desc")
	private String billingAmountDesc;
	@Column(name = "brand_logo")
	private String brandLogo;
	
	@Column(name = "background_color")
	private String backgroundColor;
	@Column(name = "text_colour")
	private String textColour;
	
	@Column(name="cg_url")
	private String cgUrl;
	
	@Column(name="portal_url")
	private String portalUrl;
	
	@Column(name="callback_url")
	private String callbackUrl;
	
	@Column(name="welcome_message_template")
	private String welcomeMessageTemplate;
	@Column(name="already_subscribed_message_template")
	private String alreadySubscribedMessageTemplate;
	
	@Column(name="mt_billed_url")
	private String mtBilledUrl;
	
	@Column(name="billed_message_template")
	private String billedMessageTemplate;
	@Column(name="unsub_msg_template")
	private String unsubMsgTemplate;
	@Column(name="unsub_not_subscribed_msg_template")
	private String unsubNotSubscribedMsgTemplate;
	@Column(name="unsub_error_msg_template")
	private String unsubErrorMsgTemplate;
	
	@Column(name="sms_url")
	private String smsUrl;
	
	@Column(name="lp_img")
	private String lpImg;
	
	@Column(name="term_condition_url")
	private String termConditionUrl;
	@Column(name="sub_api_url")
	private String subApiUrl;
	
	@Column(name="unsub_api_url")
	private String unsubApiUrl;
	
	
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

	

	

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getSmsUserName() {
		return smsUserName;
	}

	public void setSmsUserName(String smsUserName) {
		this.smsUserName = smsUserName;
	}

	public String getSmsPassword() {
		return smsPassword;
	}

	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}

	public String getSmsProvider() {
		return smsProvider;
	}

	public void setSmsProvider(String smsProvider) {
		this.smsProvider = smsProvider;
	}

	public String getSmsValue() {
		return smsValue;
	}

	public void setSmsValue(String smsValue) {
		this.smsValue = smsValue;
	}

	public String getSmsKeyword() {
		return smsKeyword;
	}

	public void setSmsKeyword(String smsKeyword) {
		this.smsKeyword = smsKeyword;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public String getValidityDesc() {
		return validityDesc;
	}

	public void setValidityDesc(String validityDesc) {
		this.validityDesc = validityDesc;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public Double getBillingAmount() {
		return billingAmount;
	}

	public void setBillingAmount(Double billingAmount) {
		this.billingAmount = billingAmount;
	}

	

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getTextColour() {
		return textColour;
	}

	public void setTextColour(String textColour) {
		this.textColour = textColour;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public String getWelcomeMessageTemplate() {
		return welcomeMessageTemplate;
	}

	public void setWelcomeMessageTemplate(String welcomeMessageTemplate) {
		this.welcomeMessageTemplate = welcomeMessageTemplate;
	}

	public String getAlreadySubscribedMessageTemplate() {
		return alreadySubscribedMessageTemplate;
	}

	public void setAlreadySubscribedMessageTemplate(
			String alreadySubscribedMessageTemplate) {
		this.alreadySubscribedMessageTemplate = alreadySubscribedMessageTemplate;
	}

	public String getBilledMessageTemplate() {
		return billedMessageTemplate;
	}

	public void setBilledMessageTemplate(String billedMessageTemplate) {
		this.billedMessageTemplate = billedMessageTemplate;
	}

	public String getUnsubMsgTemplate() {
		return unsubMsgTemplate;
	}

	public void setUnsubMsgTemplate(String unsubMsgTemplate) {
		this.unsubMsgTemplate = unsubMsgTemplate;
	}

	public String getUnsubNotSubscribedMsgTemplate() {
		return unsubNotSubscribedMsgTemplate;
	}

	public void setUnsubNotSubscribedMsgTemplate(
			String unsubNotSubscribedMsgTemplate) {
		this.unsubNotSubscribedMsgTemplate = unsubNotSubscribedMsgTemplate;
	}

	public String getUnsubErrorMsgTemplate() {
		return unsubErrorMsgTemplate;
	}

	public void setUnsubErrorMsgTemplate(String unsubErrorMsgTemplate) {
		this.unsubErrorMsgTemplate = unsubErrorMsgTemplate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getMtBilledUrl() {
		return mtBilledUrl;
	}

	public void setMtBilledUrl(String mtBilledUrl) {
		this.mtBilledUrl = mtBilledUrl;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCgUrl() {
		return cgUrl;
	}

	public void setCgUrl(String cgUrl) {
		this.cgUrl = cgUrl;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}

	public String getLpImg() {
		return lpImg;
	}

	public void setLpImg(String lpImg) {
		this.lpImg = lpImg;
	}

	public String getTermConditionUrl() {
		return termConditionUrl;
	}

	public void setTermConditionUrl(String termConditionUrl) {
		this.termConditionUrl = termConditionUrl;
	}

	public String getBillingAmountDesc() {
		return billingAmountDesc;
	}

	public void setBillingAmountDesc(String billingAmountDesc) {
		this.billingAmountDesc = billingAmountDesc;
	}

	public Integer getCcProductId() {
		return ccProductId;
	}

	public void setCcProductId(Integer ccProductId) {
		this.ccProductId = ccProductId;
	}

	public Integer getCcOpId() {
		return ccOpId;
	}

	public void setCcOpId(Integer ccOpId) {
		this.ccOpId = ccOpId;
	}

	public String getSubApiUrl() {
		return subApiUrl;
	}

	public void setSubApiUrl(String subApiUrl) {
		this.subApiUrl = subApiUrl;
	}

	public String getUnsubApiUrl() {
		return unsubApiUrl;
	}

	public void setUnsubApiUrl(String unsubApiUrl) {
		this.unsubApiUrl = unsubApiUrl;
	}

	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}
	
	
}
