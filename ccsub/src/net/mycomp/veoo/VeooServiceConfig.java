package net.mycomp.veoo;

import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_veoo_service_config")
public class VeooServiceConfig {

	@Id	
	private Integer id;
	@Column(name = "op_id")
	private Integer opId;
	@Column(name = "cc_product_id")
	private Integer ccProductId;
	@Column(name = "service_id")
	private Integer serviceId;
	private String country;
	@Column(name = "country_isd_code")
	private String countryIsdCode;
	private String operator;
	@Column(name = "short_code")
	private String shortCode;
	@Column(name = "cost_no_taxes")
	private Double costNoTaxes;
	@Column(name = "cost_w_taxes")
	private Double costWTaxes;
	@Column(name="validity")
	private Integer validity;
	private String currency;
	private String keyword;
	@Column(name = "product_id")
	private String productId;
	@Column(name="veoo_service_id")
	private String veooServiceId;
	
	@Column(name = "welcome_message_template")
	private String welcomeMessageTemplate;
	@Column(name = "already_subscribed_message")
	private String alreadySubscribedMessage;
	
	@Column(name = "cacellation_message_template")
	private String cacellationMessageTemplate;
	@Column(name = "wrong_keyword_message_template")
	private String wrongKeywordMessageTemplate;
	@Column(name = "content_message_template")
	private String contentMessageTemplate;	
	@Column(name = "landing_page")
	private String landingPage;
	@Column(name = "pin_landing_page")
	private String pinLandingPage;
	
	@Column(name = "pin_landing_page2")
	private String pinLandingPage2;
	
	@Column(name="term_condition_page")
	private String termConditionPage;
	
	@Column(name="click_flow_url")
	private String clickFlowUrl;
	@Column(name="cg_header_url")
	private String cgHeaderUrl;
	@Column(name="error_url")
	private String errorUrl;
	
	@Column(name="cg_network")
	private String cgNetwork;
	
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public Double getCostNoTaxes() {
		return costNoTaxes;
	}
	public void setCostNoTaxes(Double costNoTaxes) {
		this.costNoTaxes = costNoTaxes;
	}
	public Double getCostWTaxes() {
		return costWTaxes;
	}
	public void setCostWTaxes(Double costWTaxes) {
		this.costWTaxes = costWTaxes;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getCacellationMessageTemplate() {
		return cacellationMessageTemplate;
	}
	public void setCacellationMessageTemplate(String cacellationMessageTemplate) {
		this.cacellationMessageTemplate = cacellationMessageTemplate;
	}
	public String getWrongKeywordMessageTemplate() {
		return wrongKeywordMessageTemplate;
	}
	public void setWrongKeywordMessageTemplate(String wrongKeywordMessageTemplate) {
		this.wrongKeywordMessageTemplate = wrongKeywordMessageTemplate;
	}
	public String getLandingPage() {
		return landingPage;
	}
	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getContentMessageTemplate() {
		return contentMessageTemplate;
	}
	public void setContentMessageTemplate(String contentMessageTemplate) {
		this.contentMessageTemplate = contentMessageTemplate;
	}
	public String getWelcomeMessageTemplate() {
		return welcomeMessageTemplate;
	}
	public void setWelcomeMessageTemplate(String welcomeMessageTemplate) {
		this.welcomeMessageTemplate = welcomeMessageTemplate;
	}
	
	public String getCgNetwork() {
		return cgNetwork;
	}
	public void setCgNetwork(String cgNetwork) {
		this.cgNetwork = cgNetwork;
	}
	public String getCgHeaderUrl() {
		return cgHeaderUrl;
	}
	public void setCgHeaderUrl(String cgHeaderUrl) {
		this.cgHeaderUrl = cgHeaderUrl;
	}
	public String getClickFlowUrl() {
		return clickFlowUrl;
	}
	public void setClickFlowUrl(String clickFlowUrl) {
		this.clickFlowUrl = clickFlowUrl;
	}
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	public String getVeooServiceId() {
		return veooServiceId;
	}
	public void setVeooServiceId(String veooServiceId) {
		this.veooServiceId = veooServiceId;
	}
	
	
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
public Integer getOpId() {
	return opId;
}
public void setOpId(Integer opId) {
	this.opId = opId;
}
public Integer getValidity() {
	return validity;
}
public void setValidity(Integer validity) {
	this.validity = validity;
}
public String getTermConditionPage() {
	return termConditionPage;
}
public void setTermConditionPage(String termConditionPage) {
	this.termConditionPage = termConditionPage;
}
public Integer getCcProductId() {
	return ccProductId;
}
public void setCcProductId(Integer ccProductId) {
	this.ccProductId = ccProductId;
}
public String getAlreadySubscribedMessage() {
	return alreadySubscribedMessage;
}
public void setAlreadySubscribedMessage(String alreadySubscribedMessage) {
	this.alreadySubscribedMessage = alreadySubscribedMessage;
}
public String getPinLandingPage() {
	return pinLandingPage;
}
public void setPinLandingPage(String pinLandingPage) {
	this.pinLandingPage = pinLandingPage;
}
public String getCountryIsdCode() {
	return countryIsdCode;
}
public void setCountryIsdCode(String countryIsdCode) {
	this.countryIsdCode = countryIsdCode;
}
public String getPinLandingPage2() {
	return pinLandingPage2;
}
public void setPinLandingPage2(String pinLandingPage2) {
	this.pinLandingPage2 = pinLandingPage2;
}



	
}
