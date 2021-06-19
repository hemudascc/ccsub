package net.mycomp.beecel.jordon;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_bc_jordon_config")
public class BCJordonConfig {

	@Id
	private Integer id;
	@Column(name="t")
	private String t;	
	@Column(name="short_code")
	private String shortCode;
	@Column(name="bc_service_id")
	private Integer bcServiceId;
	@Column(name="service_id")
	private Integer serviceId;
	@Column(name="country_code")
	private Integer countryCode;
	@Column(name="sender_name")
	private String senderName;
	@Column(name="op_code")
	private String opCode;
	@Column(name="operator_id")
	private Integer operatorId;
	@Column(name="operator_name")
	private String operatorName;
	@Column(name="pub")
	private String pub;
	@Column(name="op_service_name")
	private String opServiceName;
	@Column(name="portal_url")
	private String portalUrl;	
	@Column(name="price")
	private Double price;
	@Column(name="price_point")
	private Double pricePoint;	
	@Column(name="product_id")
	private Integer productId;
//	@Column(name="mt_billing_message_template")
//	private String mtBillingMessageTemplate;
//	@Column(name="mt_welcome_message_template")
//	private String mtWelcomeMessageTemplate;	
//	@Column(name="mt_renewal_message_template")
//	private String mtRenewalMessageTemplate;
	@Column(name="lp_pages")
	private String lpPages;	
	@Column(name="lp_images")
	private String lpImages;
	@Column(name="unsub_keyword")
	private String unsubKeyword;
	@Column(name="status")
	private Boolean  status=true;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public Integer getBcServiceId() {
		return bcServiceId;
	}
	public void setBcServiceId(Integer bcServiceId) {
		this.bcServiceId = bcServiceId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getPub() {
		return pub;
	}
	public void setPub(String pub) {
		this.pub = pub;
	}
	public String getOpServiceName() {
		return opServiceName;
	}
	public void setOpServiceName(String opServiceName) {
		this.opServiceName = opServiceName;
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
	public Double getPricePoint() {
		return pricePoint;
	}
	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
//	public String getMtBillingMessageTemplate() {
//		return mtBillingMessageTemplate;
//	}
//	public void setMtBillingMessageTemplate(String mtBillingMessageTemplate) {
//		this.mtBillingMessageTemplate = mtBillingMessageTemplate;
//	}
//	public String getMtWelcomeMessageTemplate() {
//		return mtWelcomeMessageTemplate;
//	}
//	public void setMtWelcomeMessageTemplate(String mtWelcomeMessageTemplate) {
//		this.mtWelcomeMessageTemplate = mtWelcomeMessageTemplate;
//	}
//	public String getMtRenewalMessageTemplate() {
//		return mtRenewalMessageTemplate;
//	}
//	public void setMtRenewalMessageTemplate(String mtRenewalMessageTemplate) {
//		this.mtRenewalMessageTemplate = mtRenewalMessageTemplate;
//	}
	public String getLpPages() {
		return lpPages;
	}
	public void setLpPages(String lpPages) {
		this.lpPages = lpPages;
	}
	public String getLpImages() {
		return lpImages;
	}
	public void setLpImages(String lpImages) {
		this.lpImages = lpImages;
	}
	
	public String getUnsubKeyword() {
		return unsubKeyword;
	}
	public void setUnsubKeyword(String unsubKeyword) {
		this.unsubKeyword = unsubKeyword;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "BCJordonConfig [id=" + id + ", t=" + t + ", shortCode=" + shortCode + ", bcServiceId=" + bcServiceId
				+ ", serviceId=" + serviceId + ", countryCode=" + countryCode + ", senderName=" + senderName
				+ ", opCode=" + opCode + ", operatorId=" + operatorId + ", operatorName=" + operatorName + ", pub="
				+ pub + ", opServiceName=" + opServiceName + ", portalUrl=" + portalUrl + ", price=" + price
				+ ", pricePoint=" + pricePoint + ", productId=" + productId + ", lpPages=" + lpPages + ", lpImages="
				+ lpImages + ", unsubKeyword=" + unsubKeyword + ", status=" + status + "]";
	}

	
	
}
