package net.mycomp.mcarokiosk.hongkong;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_mk_hongkong_config")
public class MKHongkongConfig {

	@Id 
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "product_id")
	private Integer productId; 
	@Column(name = "operator_name")  
	private String operatorName;
	@Column(name = "op_service_name")
	private String opServiceName;

	private String user;
	@Column(name = "password")
	private String password;

	@Column(name = "shortcode")
	private String shortcode;

	private Double price;
	@Column(name = "price_point")
	private Double pricePoint;
	@Column(name = "signup_price")
	private Double signUpPrice;
	@Column(name = "signup_price_point")
	private Double signUpPricePoint;
	@Column(name = "validity")
	private Integer validity;
	@Column(name = "validity_for_charge")
	private Integer validityForCharge;

	@Column(name = "subscription_cycle")
	private String subscriptionCycle;

	@Column(name = "mt_billing_message_template")
	private String mtBillingMessageTemplate;
	@Column(name = "mt_signup_message_template")
	private String mtSignUpMessageTemplate;
	@Column(name = "mt_welcome_message_template")
	private String mtWelcomeMessageTemplate;
	@Column(name = "mt_renewal_message_template")
	private String mtRenewalMessageTemplate;

	private Integer telcoId;
	private Integer cat;
	private String senderId;
	private String keyword;  
	private String linkid;
	@Column(name = "portal_url")
	private String portalUrl;
	@Column(name = "lp_images")
	@Convert(converter = JpaConverterJson.class)
	private List<String> lpImages;

	@Column(name = "lp_pages")
	private String lpPages;
	@Column(name = "platform")
	private String platform;
	@Column(name = "status")
	private Boolean status = true;
	@Column(name = "max_price")
	private Integer maxPrice;

	public String toString() {

		Field[] fields = this.getClass().getDeclaredFields();
		String str = this.getClass().getName();
		try {
			for (Field field : fields) {
				str += field.getName() + "=" + field.get(this) + ",";
			}
		} catch (IllegalArgumentException ex) {
			System.out.println(ex);
		} catch (IllegalAccessException ex) {
			System.out.println(ex);
		}
		return str;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getTelcoId() {
		return telcoId;
	}

	public void setTelcoId(Integer telcoId) {
		this.telcoId = telcoId;
	}

	public Integer getCat() {
		return cat;
	}

	public void setCat(Integer cat) {
		this.cat = cat;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLinkid() {
		return linkid;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMapKey() {
		return serviceId + "" + telcoId;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSignUpPrice() {
		return signUpPrice;
	}

	public void setSignUpPrice(Double signUpPrice) {
		this.signUpPrice = signUpPrice;
	}

	public Double getSignUpPricePoint() {
		return signUpPricePoint;
	}

	public void setSignUpPricePoint(Double signUpPricePoint) {
		this.signUpPricePoint = signUpPricePoint;
	}

	public List<String> getLpImages() {
		return lpImages;
	}

	public void setLpImages(List<String> lpImages) {
		this.lpImages = lpImages;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOpServiceName() {
		return opServiceName;
	}

	public void setOpServiceName(String opServiceName) {
		this.opServiceName = opServiceName;
	}

	public String getSubscriptionCycle() {
		return subscriptionCycle;
	}

	public void setSubscriptionCycle(String subscriptionCycle) {
		this.subscriptionCycle = subscriptionCycle;
	}

	public String getLpPages() {
		return lpPages;
	}

	public void setLpPages(String lpPages) {
		this.lpPages = lpPages;
	}

	public String getMtBillingMessageTemplate() {
		return mtBillingMessageTemplate;
	}

	public void setMtBillingMessageTemplate(String mtBillingMessageTemplate) {
		this.mtBillingMessageTemplate = mtBillingMessageTemplate;
	}

	public String getMtSignUpMessageTemplate() {
		return mtSignUpMessageTemplate;
	}

	public void setMtSignUpMessageTemplate(String mtSignUpMessageTemplate) {
		this.mtSignUpMessageTemplate = mtSignUpMessageTemplate;
	}

	public String getMtWelcomeMessageTemplate() {
		return mtWelcomeMessageTemplate;
	}

	public void setMtWelcomeMessageTemplate(String mtWelcomeMessageTemplate) {
		this.mtWelcomeMessageTemplate = mtWelcomeMessageTemplate;
	}

	public String getMtRenewalMessageTemplate() {
		return mtRenewalMessageTemplate;
	}

	public void setMtRenewalMessageTemplate(String mtRenewalMessageTemplate) {
		this.mtRenewalMessageTemplate = mtRenewalMessageTemplate;
	}

	public Integer getValidityForCharge() {
		return validityForCharge;
	}

	public void setValidityForCharge(Integer validityForCharge) {
		this.validityForCharge = validityForCharge;
	}

	public Double getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	
}
