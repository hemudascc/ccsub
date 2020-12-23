package net.mycomp.one97;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_one97_config")
public class One97Config {

	@Id
	private Integer id;
	@Column(name = "package_id")
	private Integer packageId;
	@Column(name = "buy_channel")
	private String buyChannel;
	@Column(name = "package_name")
	private String packageName;
	private String agency;
	@Column(name = "operator_name")
	private String operatorName;
	@Column(name = "short_code")
	private String shortCode;
	private String keyword;
	@Column(name = "price_point")
	private Double pricePoint;
	@Column(name = "validity")
	private Integer validity;
	private Boolean status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public String getBuyChannel() {
		return buyChannel;
	}
	public void setBuyChannel(String buyChannel) {
		this.buyChannel = buyChannel;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Double getPricePoint() {
		return pricePoint;
	}
	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
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
	
	
	
}
