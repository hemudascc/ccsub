package net.mycomp.actel;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_actel_service_config")
public class ActelServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "operator_id")
	private Integer operatorId;
	
	@Column(name="service_name")
	private String serviceName;
	
	@Column(name = "lang_id")
	private Integer langId;
	
	@Column(name = "id_application")
	private String idApplication;
	
	@Column(name = "package_id")
	private String packageId;
	@Column(name = "package_name")
	private String packageName;
	private String senderId;
	private Integer duration;
	@Column(name="validity_desc")
	private String validityDesc;
	private  Double price;
	@Column(name="price_desc")
	private  String priceDesc;
	
	@Column(name = "free_period_days")
	private Integer freePeriodDays;
	@Column(name = "short_code")
	private String shortCode;
	@Column(name = "sub_key")
	private String subKey;
	@Column(name = "unsub_key")
	private String unsubKey;	
	@Column(name = "cg_url")
	private String cgUrl;	
	@Column(name = "portal_url")
	private String portalUrl;	
	@Column(name = "lp_images")
	//@Convert(converter=JpaConverterJson.class)
	private String lpImages;	
	
	@Column(name="sub_message_template")
	private String subMessageTemplate;
	
	@Column(name="unsub_message_template")
	private String unsubMessageTemplate;
	@Column(name="already_subscribed_message")
	private String alreadySubscribedMessage;
	
	@Column(name="pin_api")
	private String pinApi;
	@Column(name="pin_validation_api")
	private String pinValidationApi;
	@Column(name="country")
	private String country;
	
	@Column(name="sms_url")
	private String smsUrl;
	
	@Column(name="he_url")
	private String heUrl;
	@Column(name="msisdn_prefix")
	private String msisdnPrefix;
	
	@Column(name="msisdn_length")
	private Integer msisdnLength;
	
	@Column(name="he_callback")
	private String heCallback;
	
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
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getFreePeriodDays() {
		return freePeriodDays;
	}
	public void setFreePeriodDays(Integer freePeriodDays) {
		this.freePeriodDays = freePeriodDays;
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getCgUrl() {
		return cgUrl;
	}
	public void setCgUrl(String cgUrl) {
		this.cgUrl = cgUrl;
	}
	public String getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}
	
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getSubMessageTemplate() {
		return subMessageTemplate;
	}
	public void setSubMessageTemplate(String subMessageTemplate) {
		this.subMessageTemplate = subMessageTemplate;
	}

	public String getUnsubMessageTemplate() {
		return unsubMessageTemplate;
	}

	public void setUnsubMessageTemplate(String unsubMessageTemplate) {
		this.unsubMessageTemplate = unsubMessageTemplate;
	}

	public String getAlreadySubscribedMessage() {
		return alreadySubscribedMessage;
	}

	public void setAlreadySubscribedMessage(String alreadySubscribedMessage) {
		this.alreadySubscribedMessage = alreadySubscribedMessage;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public Integer getLangId() {
		return langId;
	}

	public void setLangId(Integer langId) {
		this.langId = langId;
	}

	public String getIdApplication() {
		return idApplication;
	}

	public void setIdApplication(String idApplication) {
		this.idApplication = idApplication;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getPriceDesc() {
		return priceDesc;
	}
	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}
	public String getValidityDesc() {
		return validityDesc;
	}
	public void setValidityDesc(String validityDesc) {
		this.validityDesc = validityDesc;
	}
	public String getLpImages() {
		return lpImages;
	}
	public void setLpImages(String lpImages) {
		this.lpImages = lpImages;
	}
	public String getPinApi() {
		return pinApi;
	}
	public void setPinApi(String pinApi) {
		this.pinApi = pinApi;
	}
	public String getPinValidationApi() {
		return pinValidationApi;
	}
	public void setPinValidationApi(String pinValidationApi) {
		this.pinValidationApi = pinValidationApi;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeCallback() {
		return heCallback;
	}
	public void setHeCallback(String heCallback) {
		this.heCallback = heCallback;
	}
	public String getHeUrl() {
		return heUrl;
	}
	public void setHeUrl(String heUrl) {
		this.heUrl = heUrl;
	}
	public String getMsisdnPrefix() {
		return msisdnPrefix;
	}
	public void setMsisdnPrefix(String msisdnPrefix) {
		this.msisdnPrefix = msisdnPrefix;
	}
	public Integer getMsisdnLength() {
		return msisdnLength;
	}
	public void setMsisdnLength(Integer msisdnLength) {
		this.msisdnLength = msisdnLength;
	}
	public String getSmsUrl() {
		return smsUrl;
	}
	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}


}
