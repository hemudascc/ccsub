package net.mycomp.mt2.uae;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_mt2_uae_service_config")
public class Mt2UAEServiceConfig {
	
	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "service_name")
	private String serviceName;
	
	@Column(name = "service_tag")
	private String serviceTag;
	
	@Column(name="client_id")
	private String clientId;	
	
	@Column(name="mt2_op_id")
	private String mt2OpId;	
	
	@Column(name="mcc")
	private String mcc;
	@Column(name="mnc")
	private String mnc;	
	
	@Column(name="secret_key")
	private String secretKey;
	
	@Column(name="get_subscription_url")
	private String getSubscriptionUrl;
	
	@Column(name="send_otp_url")
	private String sendOtpUrl;
	
	@Column(name="subscribe_url")
	private String subscribeUrl;
	
	@Column(name="cancel_url")
	private String cancelUrl;
	
	@Column(name="sms_url")
	private String smsUrl;
	@Column(name="sms_user")
	private String smsUser;
	
	@Column(name="sms_password")
	private String smsPassword;
	
	
	@Column(name = "portal_url")
	private String portalUrl;
	@Column(name = "price_point")
	private Double pricePoint;
	@Column(name="price_point_desc")
	private String pricePointDesc;
	@Column(name="validity")
	private Integer validity;
	@Column(name="validity_desc")
	private String validityDesc;
	
	@Column(name = "short_code")
	private String shortCode;
	@Column(name = "sub_key")
	private String subKey;
	@Column(name = "unsub_key")
	private String unsubKey;
	
	@Column(name="lp_page")
	private String lpPage;
	
	@Column(name="lp_page2")
	private String lpPage2;
	
	@Column(name="lp_img")
	private String lpImg;
	
	@Column(name="sub_msg_template")
	private String subMsgTemplate;
	
	@Column(name="renew_msg_template")
	private String renewMsgTemplate;
	
	
	@Column(name="unsub_msg_template")
	private String unsubMsgTemplate;
	
	@Column(name="cg_callback_url")
	private String cgCallBackUrl;
	
	@Column(name="msisdn_prefix")
	private String msisdnPrefix;
	
	@Column(name="traffic_sources")
	@Convert(converter=JpaConverterJson.class)
	private List<String> trafficSources;
	
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	

	public String getSendOtpUrl() {
		return sendOtpUrl;
	}

	public void setSendOtpUrl(String sendOtpUrl) {
		this.sendOtpUrl = sendOtpUrl;
	}

	

	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
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

	public String getPricePointDesc() {
		return pricePointDesc;
	}

	public void setPricePointDesc(String pricePointDesc) {
		this.pricePointDesc = pricePointDesc;
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

	public String getLpImg() {
		return lpImg;
	}

	public void setLpImg(String lpImg) {
		this.lpImg = lpImg;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getServiceTag() {
		return serviceTag;
	}

	public void setServiceTag(String serviceTag) {
		this.serviceTag = serviceTag;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getLpPage() {
		return lpPage;
	}

	public void setLpPage(String lpPage) {
		this.lpPage = lpPage;
	}

	public String getGetSubscriptionUrl() {
		return getSubscriptionUrl;
	}

	public void setGetSubscriptionUrl(String getSubscriptionUrl) {
		this.getSubscriptionUrl = getSubscriptionUrl;
	}

	public String getSubscribeUrl() {
		return subscribeUrl;
	}

	public void setSubscribeUrl(String subscribeUrl) {
		this.subscribeUrl = subscribeUrl;
	}

	public String getSubMsgTemplate() {
		return subMsgTemplate;
	}

	public void setSubMsgTemplate(String subMsgTemplate) {
		this.subMsgTemplate = subMsgTemplate;
	}

	public String getRenewMsgTemplate() {
		return renewMsgTemplate;
	}

	public void setRenewMsgTemplate(String renewMsgTemplate) {
		this.renewMsgTemplate = renewMsgTemplate;
	}

	public String getUnsubMsgTemplate() {
		return unsubMsgTemplate;
	}

	public void setUnsubMsgTemplate(String unsubMsgTemplate) {
		this.unsubMsgTemplate = unsubMsgTemplate;
	}

	public String getSmsUser() {
		return smsUser;
	}

	public void setSmsUser(String smsUser) {
		this.smsUser = smsUser;
	}

	public String getSmsPassword() {
		return smsPassword;
	}

	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}

	public String getMt2OpId() {
		return mt2OpId;
	}

	public void setMt2OpId(String mt2OpId) {
		this.mt2OpId = mt2OpId;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getCgCallBackUrl() {
		return cgCallBackUrl;
	}

	public void setCgCallBackUrl(String cgCallBackUrl) {
		this.cgCallBackUrl = cgCallBackUrl;
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

	public String getMsisdnPrefix() {
		return msisdnPrefix;
	}

	public void setMsisdnPrefix(String msisdnPrefix) {
		this.msisdnPrefix = msisdnPrefix;
	}

	public String getLpPage2() {
		return lpPage2;
	}

	public void setLpPage2(String lpPage2) {
		this.lpPage2 = lpPage2;
	}

	public List<String> getTrafficSources() {
		return trafficSources;
	}

	public void setTrafficSources(List<String> trafficSources) {
		this.trafficSources = trafficSources;
	}
	
}
