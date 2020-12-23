package net.mycomp.etisalat;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_etisalat_service_config")
public class EtisalatServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "package_id")
	private String packageId;
	@Column(name = "package_name")
	private String packageName;
	private String senderId;
	private Integer duration;
	private  Double price;
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
	@Convert(converter=JpaConverterJson.class)
	private List<String> lpImages;	
	
	@Column(name="sub_message_template")
	private String subMessageTemplate;
	
	@Column(name="unsub_message_template")
	private String unsubMessageTemplate;
	@Column(name="already_subscribed_message")
	private String alreadySubscribedMessage;
	
	
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
	public List<String> getLpImages() {
		return lpImages;
	}
	public void setLpImages(List<String> lpImages) {
		this.lpImages = lpImages;
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

}
