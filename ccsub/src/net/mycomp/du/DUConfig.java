package net.mycomp.du;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import net.util.JpaConverterJson;
import net.util.JpaStringListConverter;

@Entity
@Table(name = "tb_du_config")
public class DUConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "product_id")
	private String productId;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "product_price")
	private Integer productPrice;
	@Column(name = "product_val")
	private Integer productVal;
	@Column(name = "cp_id")
	private String cpId;
	@Column(name = "cp_pwd")
	private String cpPwd;
	@Column(name = "cp_name")
	private String cpName;
	@Column(name = "req_mode")
	private String reqMode;
	@Column(name = "req_type")
	private String reqType;
	
	@Column(name = "product_val_unit")
	private String productValUnit;
	@Column(name = "product_desc")
	private String productDesc;
	@Column(name = "wap_mdata_img")
	@Convert(converter=JpaConverterJson.class)
	private List<String> wapMdataImgs;
	
	@Column(name = "ims_id")
	private String imsId;
	@Column(name = "s_renewal_price")
	private Integer sRenewalPrice;
	@Column(name = "renewal_plan_validity")
	private String renewalPlanValidity;
	@Column(name = "request_locale")
	private String requestLocale;
	@Column(name = "service_type")
	private String serviceType;
	@Column(name = "plan_id")
	private String planId;
	
	@Column(name = "portal_url")
	private String portalURL;
	
	@Column(name = "keyword")
	private String keyword;
	@Column(name = "shortcode")
	private String shortcode;
	
	@Column(name = "create_date")
	private Timestamp createDate;
	private Boolean status;
	
	@OneToMany(targetEntity=DuFallbackConfig.class,fetch=FetchType.EAGER)
	@JoinColumn(name = "du_config_id")
	private List<DuFallbackConfig> listDuFallbackConfig;
	
	
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
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}
	public Integer getProductVal() {
		return productVal;
	}
	public void setProductVal(Integer productVal) {
		this.productVal = productVal;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getCpPwd() {
		return cpPwd;
	}
	public void setCpPwd(String cpPwd) {
		this.cpPwd = cpPwd;
	}
	public String getCpName() {
		return cpName;
	}
	public void setCpName(String cpName) {
		this.cpName = cpName;
	}
	public String getReqMode() {
		return reqMode;
	}
	public void setReqMode(String reqMode) {
		this.reqMode = reqMode;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getProductValUnit() {
		return productValUnit;
	}
	public void setProductValUnit(String productValUnit) {
		this.productValUnit = productValUnit;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	
	public List<DuFallbackConfig> getListDuFallbackConfig() {
		return listDuFallbackConfig;
	}
	public void setListDuFallbackConfig(List<DuFallbackConfig> listDuFallbackConfig) {
		this.listDuFallbackConfig = listDuFallbackConfig;
	}

	public String getImsId() {
		return imsId;
	}

	public void setImsId(String imsId) {
		this.imsId = imsId;
	}

	public Integer getsRenewalPrice() {
		return sRenewalPrice;
	}

	public void setsRenewalPrice(Integer sRenewalPrice) {
		this.sRenewalPrice = sRenewalPrice;
	}

	public String getRenewalPlanValidity() {
		return renewalPlanValidity;
	}

	public void setRenewalPlanValidity(String renewalPlanValidity) {
		this.renewalPlanValidity = renewalPlanValidity;
	}

	public String getRequestLocale() {
		return requestLocale;
	}

	public void setRequestLocale(String requestLocale) {
		this.requestLocale = requestLocale;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPortalURL() {
		return portalURL;
	}

	public void setPortalURL(String portalURL) {
		this.portalURL = portalURL;
	}

	public List<String> getWapMdataImgs() {
		return wapMdataImgs;
	}

	public void setWapMdataImgs(List<String> wapMdataImgs) {
		this.wapMdataImgs = wapMdataImgs;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}
}
