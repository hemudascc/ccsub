package net.mycom.nxt.vas;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;

@Entity
@Table(name = "tb_nxt_vas_config")
public class NxtVasConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;	
	@Column(name = "operator_id")
	private Integer operatorId;
	
	@Column(name = "service_name")
	private String serviceName;
	@Column(name = "nxt_vas_product_id")
	private Integer nxtVasProduct_id;
	@Column(name="mcc")
	private String mcc;
	
	@Column(name="mnc")
	private String mnc;
	
	@Column(name="api_key")
	private String apiKey;
	
	private Integer validity;
	private Double amount;
	@Column(name="currency")
	private String currency;
	private Integer language;
	@Column(name="lp_images")
   	@Convert(converter=JpaConverterJson.class)
   	private List<String> lpImages;	
	@Column(name="portal_url")
	private String portalUrl;
	@Column(name="cg_url_template")
	private String cgUrlTemplate;
	
	@Column(name="unsub_url_template")
	private String unsubUrlTemplate;
	@Column(name="free_mt_msg_url_template")
	private String freeMtMsgUrlTemplate;
	
	@Column(name = "status")
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
	
	
	
	public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public List<String> getLpImages() {
		return lpImages;
	}
	public void setLpImages(List<String> lpImages) {
		this.lpImages = lpImages;
	}
	public String getPortalUrl() {
		return portalUrl;
	}
	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}
	public String getCgUrlTemplate() {
		return cgUrlTemplate;
	}
	public void setCgUrlTemplate(String cgUrlTemplate) {
		this.cgUrlTemplate = cgUrlTemplate;
	}
	public Integer getLanguage() {
		return language;
	}
	public void setLanguage(Integer language) {
		this.language = language;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUnsubUrlTemplate() {
		return unsubUrlTemplate;
	}
	public void setUnsubUrlTemplate(String unsubUrlTemplate) {
		this.unsubUrlTemplate = unsubUrlTemplate;
	}
	public String getFreeMtMsgUrlTemplate() {
		return freeMtMsgUrlTemplate;
	}
	public void setFreeMtMsgUrlTemplate(String freeMtMsgUrlTemplate) {
		this.freeMtMsgUrlTemplate = freeMtMsgUrlTemplate;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getNxtVasProduct_id() {
		return nxtVasProduct_id;
	}
	public void setNxtVasProduct_id(Integer nxtVasProduct_id) {
		this.nxtVasProduct_id = nxtVasProduct_id;
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
}
