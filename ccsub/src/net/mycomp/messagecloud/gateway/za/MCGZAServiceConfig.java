package net.mycomp.messagecloud.gateway.za;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_mcg_za_service_config")
public class MCGZAServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "service_name")
	private String serviceName;
	
	@Column(name = "short_code")
	private String shortCode;
	private String keyword;
	@Column(name = "company_code")
	private String companyCode;
	private String ekey;
	@Column(name="app_id")
	private String appId;
	@Column(name="price_point")
	private Double pricePoint;
	@Column(name="validity")
	private Integer  validity;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name = "billing_message")
	private String billingMessage;
	
	@Column(name = "sub_message")
	private String subMessage;
	@Column(name="already_subscribed_message")
	private String alreadySubscribedMessage;
	@Column(name="unsub_message")
	private String unsubMessage;
	
	@Column(name="invalid_message")
	private String invalidmessage;
	
	@Column(name = "lp_images")
	@Convert(converter=JpaConverterJson.class)
	private List<String> lpImages;	
	
	@Column(name="lp_pages")
	private String lpPages;
	
		
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
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getEkey() {
		return ekey;
	}
	public void setEkey(String ekey) {
		this.ekey = ekey;
	}
	public String getBillingMessage() {
		return billingMessage;
	}
	public void setBillingMessage(String billingMessage) {
		this.billingMessage = billingMessage;
	}
	public String getSubMessage() {
		return subMessage;
	}
	public void setSubMessage(String subMessage) {
		this.subMessage = subMessage;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getUnsubMessage() {
		return unsubMessage;
	}
	public void setUnsubMessage(String unsubMessage) {
		this.unsubMessage = unsubMessage;
	}
	public String getInvalidmessage() {
		return invalidmessage;
	}
	public void setInvalidmessage(String invalidmessage) {
		this.invalidmessage = invalidmessage;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	
	public String getAlreadySubscribedMessage() {
		return alreadySubscribedMessage;
	}
	public void setAlreadySubscribedMessage(String alreadySubscribedMessage) {
		this.alreadySubscribedMessage = alreadySubscribedMessage;
	}
	
	
	public List<String> getLpImages() {
		return lpImages;
	}
	public void setLpImages(List<String> lpImages) {
		this.lpImages = lpImages;
	}
	public String getLpPages() {
		return lpPages;
	}
	public void setLpPages(String lpPages) {
		this.lpPages = lpPages;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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

	
}
