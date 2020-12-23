package net.mycomp.mt2.zain.iraq;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mt2_zain_iraq_service_config")
public class Mt2ZainIraqServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "zain_iraq_service_id")
	private String zainIraqServiceId;	
	
	@Column(name="zain_service_key")
	private String zainServiceKey;
	
	@Column(name="sub_url")
	private String subUrl;
	
	@Column(name="api_url")
	private String apiUrl;
	
	@Column(name="api_snippet_url")
	private String apiSnippetUrl;
	
	
	@Column(name = "spid")
	private String spid;	
	@Column(name = "service_name")
	private String serviceName;	
	
	@Column(name = "short_code")
	private String shortCode;	
	
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
	
	@Column(name="sub_msg_template")
	private String subMsgTemplate;
	
	@Column(name="renew_msg_template")
	private String renewMsgTemplate;
	
	
	@Column(name="unsub_msg_template")
	private String unsubMsgTemplate;
	
	@Column(name="lp_img")
	private String lpImg;
	
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

	public String getZainIraqServiceId() {
		return zainIraqServiceId;
	}

	public void setZainIraqServiceId(String zainIraqServiceId) {
		this.zainIraqServiceId = zainIraqServiceId;
	}

	public String getSpid() {
		return spid;
	}

	public void setSpid(String spid) {
		this.spid = spid;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
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

	public String getZainServiceKey() {
		return zainServiceKey;
	}

	public void setZainServiceKey(String zainServiceKey) {
		this.zainServiceKey = zainServiceKey;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiSnippetUrl() {
		return apiSnippetUrl;
	}

	public void setApiSnippetUrl(String apiSnippetUrl) {
		this.apiSnippetUrl = apiSnippetUrl;
	}

	public String getSubUrl() {
		return subUrl;
	}

	public void setSubUrl(String subUrl) {
		this.subUrl = subUrl;
	}

	public String getSubMsgTemplate() {
		return subMsgTemplate;
	}

	public void setSubMsgTemplate(String subMsgTemplate) {
		this.subMsgTemplate = subMsgTemplate;
	}

	public String getUnsubMsgTemplate() {
		return unsubMsgTemplate;
	}

	public void setUnsubMsgTemplate(String unsubMsgTemplate) {
		this.unsubMsgTemplate = unsubMsgTemplate;
	}

	public String getRenewMsgTemplate() {
		return renewMsgTemplate;
	}

	public void setRenewMsgTemplate(String renewMsgTemplate) {
		this.renewMsgTemplate = renewMsgTemplate;
	}


}
