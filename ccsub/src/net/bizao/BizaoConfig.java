
package net.bizao;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_bizao_config")
public class BizaoConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "product_id")
	private Integer productId;
	
	
	@Column(name="service_name")
	private String serviceName;
	
	@Column(name="msisdn_prefix")
	private String msisdnPrefix;
	
	@Column(name="service_type")
	private String serviceType;
	
	
	@Column(name="country_code")
	private String countryCode;
	
	@Column(name="country_name")
	private String countryName;
	
	@Column(name = "op_id")
	private Integer opId;
	
	private String circle;
	@Column(name = "price_point")
	private Double pricePoint;
	
	@Column(name = "price_point_desc")
	private String pricePointDesc;
	
	private String currency;
	@Column(name="currency_desc")
	private String currencyDesc;
	private Integer validity;
	@Column(name="validity_desc")
	private String validityDesc;
	
	@Column(name="on_behalf_of")
	private String onBehalfOf;
	
	@Column(name="lp_images")
   	@Convert(converter=JpaConverterJson.class)
   	private List<String> lpImages;
	@Column(name="portal_url")
	private String portalUrl;
	
	@Column(name="cg_url")
	private String cgUrl;
	@Column(name="campaign_url")
	private String campaignUrl;
	@Column(name="keyword")
	private String keyword;
	
	@Column(name="sender_address")
	private String senderAddress;
	@Column(name="sender_name")
	private String senderName;
	
	@Column(name="short_code")
	private String shortCode;
	
	@Column(name="mo_short_code")
	private String moShortCode;
	
	
	@Column(name="activation_msg_template")
	private String activationMsgTemplate;
	@Column(name="informatic_msg_template")
	private String informaticMsgTemplate;
	
	
	
	@Column(name="deacivation_link_msg_template")
	private String deacivationLinkMsgTemplate;
	
	@Column(name="deactivation_msg_template")
	private String deactivationMsgTemplate;
	
	@Column(name="invalid_keyword_msg_template")
	private String invalidKeywordMsgTemplate;
	
	@Column(name="not_subscribed_msg_template")
	private String notSubscribedMsgTemplate;
	
	@Column(name="renewal_alert_msg_template")
	private String renewalAlertMsgTemplate;
	
	@Column(name="renewal_msg_template")
	private String renewalMsgTemplate;
	
	private Boolean status;

	
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMsisdnPrefix() {
		return msisdnPrefix;
	}

	public void setMsisdnPrefix(String msisdnPrefix) {
		this.msisdnPrefix = msisdnPrefix;
	}

	
	public Integer getOpId() {
		return opId;
	}

	public void setOpId(Integer opId) {
		this.opId = opId;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public Double getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public List<String> getLpImages() {
		return lpImages;
	}

	public void setLpImages(List<String> lpImages) {
		this.lpImages = lpImages;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public String getCgUrl() {
		return cgUrl;
	}

	public void setCgUrl(String cgUrl) {
		this.cgUrl = cgUrl;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getActivationMsgTemplate() {
		return activationMsgTemplate;
	}

	public void setActivationMsgTemplate(String activationMsgTemplate) {
		this.activationMsgTemplate = activationMsgTemplate;
	}

	public String getDeactivationMsgTemplate() {
		return deactivationMsgTemplate;
	}

	public void setDeactivationMsgTemplate(String deactivationMsgTemplate) {
		this.deactivationMsgTemplate = deactivationMsgTemplate;
	}

	public String getInvalidKeywordMsgTemplate() {
		return invalidKeywordMsgTemplate;
	}

	public void setInvalidKeywordMsgTemplate(String invalidKeywordMsgTemplate) {
		this.invalidKeywordMsgTemplate = invalidKeywordMsgTemplate;
	}

	public String getNotSubscribedMsgTemplate() {
		return notSubscribedMsgTemplate;
	}

	public void setNotSubscribedMsgTemplate(String notSubscribedMsgTemplate) {
		this.notSubscribedMsgTemplate = notSubscribedMsgTemplate;
	}

	public String getRenewalMsgTemplate() {
		return renewalMsgTemplate;
	}

	public void setRenewalMsgTemplate(String renewalMsgTemplate) {
		this.renewalMsgTemplate = renewalMsgTemplate;
	}

	public String getDeacivationLinkMsgTemplate() {
		return deacivationLinkMsgTemplate;
	}

	public void setDeacivationLinkMsgTemplate(String deacivationLinkMsgTemplate) {
		this.deacivationLinkMsgTemplate = deacivationLinkMsgTemplate;
	}

	public String getInformaticMsgTemplate() {
		return informaticMsgTemplate;
	}

	public void setInformaticMsgTemplate(String informaticMsgTemplate) {
		this.informaticMsgTemplate = informaticMsgTemplate;
	}

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public String getMoShortCode() {
		return moShortCode;
	}

	public void setMoShortCode(String moShortCode) {
		this.moShortCode = moShortCode;
	}

	public String getPricePointDesc() {
		return pricePointDesc;
	}

	public void setPricePointDesc(String pricePointDesc) {
		this.pricePointDesc = pricePointDesc;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getValidityDesc() {
		return validityDesc;
	}

	public void setValidityDesc(String validityDesc) {
		this.validityDesc = validityDesc;
	}

	public String getRenewalAlertMsgTemplate() {
		return renewalAlertMsgTemplate;
	}

	public void setRenewalAlertMsgTemplate(String renewalAlertMsgTemplate) {
		this.renewalAlertMsgTemplate = renewalAlertMsgTemplate;
	}

	public String getCampaignUrl() {
		return campaignUrl;
	}

	public void setCampaignUrl(String campaignUrl) {
		this.campaignUrl = campaignUrl;
	}

	
	
	
}
