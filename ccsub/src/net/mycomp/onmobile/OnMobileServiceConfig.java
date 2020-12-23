package net.mycomp.onmobile;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_onmobile_service_config")
public class OnMobileServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name="validity")
	private Integer validity;	
	@Column(name="validity_desc")
	private String validityDesc;
	@Column(name="price")
	private  Double price;
	@Column(name="price_desc")
	private  String priceDesc;		
	@Column(name="portal_url")
	private  String portalUrl;
	@Column(name="lp_page")
	private String lpPage;
	@Column(name = "campaign_name")
	private String campaignName;
	
	@Column(name = "srv_key")
	private String srvKey;
	@Column(name = "cg_url")
	private String cgUrl;
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
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getSrvKey() {
		return srvKey;
	}
	public void setSrvKey(String srvKey) {
		this.srvKey = srvKey;
	}
	public String getCgUrl() {
		return cgUrl;
	}
	public void setCgUrl(String cgUrl) {
		this.cgUrl = cgUrl;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPriceDesc() {
		return priceDesc;
	}
	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}
	public String getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}
	public String getLpPage() {
		return lpPage;
	}
	public void setLpPage(String lpPage) {
		this.lpPage = lpPage;
	}
	
}
