package net.mycomp.comviva.ooredo.oman;

import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;


@Entity
@Table(name = "tb_ooredoo_oman_service_config")
public class OoredooOmanServiceConfig {

	@Id
	private Integer id;
	@Column(name = "cc_service_id")
	private Integer ccServiceId;
	
	
	@Column(name = "service_name")
	private String serviceName;
	
	@Column(name = "service_node")
	private String serviceNode;
	
	@Column(name = "short_code")
	private String shortCode;
	
	@Column(name = "service_id")
	private String serviceId;
	
	@Column(name = "auth_service_id")
	private String authServiceId;
	
	@Column(name = "auth_preshared_api_key")
	private String authPresharedApiKey;
	
	@Column(name = "auth_sms_api_key")
	private String authSmsApiKey;
	@Column(name = "auth_preshared_sms_key")
	private String authPresharedSmsKey;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "cp_name")
	private String cpName;
	
	@Column(name = "he_url")
	private String heUrl;
	
	@Column(name = "redirect_to_url")
	private String redirectToUrl;
	
	@Column(name = "send_pin_url")
	private String sendPinUrl;
	@Column(name = "pin_validation_url")
	private String pinValidationUrl;
	@Column(name = "unsub_url")
	private String unsubUrl;
	
	@Column(name="mt_url")
	private String mtUrl;
	
	@Column(name = "price_point")
	private Double pricePoint;
	
	@Column(name = "price_point_desc")
	private String pricePointDesc;
	
	@Column(name = "validity")
	private Integer validity;
	
	@Column(name = "validity_desc")
	private String validityDesc;
	
	@Column(name="lp_img")
	private String lpImg;
	
	@Column(name="lp_page")
	private String lpPage;
	
	@Column(name = "sub_keyword")
	private String subKeyword;
	@Column(name = "unsub_keyword")
	private String unsubKeyword;
	
	@Column(name = "plan_id")
	private String planId;
	@Column(name = "bearer_id")
	private String bearerId;
	
	@Column(name = "auth_api_key")
	private String authApiKey;
	
	@Column(name = "sub_msg")
	private String subMsg;
	@Column(name = "unsub_msg")
	private String unsubMsg;
	
	
	@Column(name = "portal_url")
	private String portalUrl;
	
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


public Integer getProductId() {
	return productId;
}


public void setProductId(Integer productId) {
	this.productId = productId;
}


public String getCpName() {
	return cpName;
}


public void setCpName(String cpName) {
	this.cpName = cpName;
}


public String getHeUrl() {
	return heUrl;
}


public void setHeUrl(String heUrl) {
	this.heUrl = heUrl;
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


public String getSubKeyword() {
	return subKeyword;
}


public void setSubKeyword(String subKeyword) {
	this.subKeyword = subKeyword;
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


public String getServiceNode() {
	return serviceNode;
}


public void setServiceNode(String serviceNode) {
	this.serviceNode = serviceNode;
}


public String getRedirectToUrl() {
	return redirectToUrl;
}


public void setRedirectToUrl(String redirectToUrl) {
	this.redirectToUrl = redirectToUrl;
}


public Integer getCcServiceId() {
	return ccServiceId;
}


public void setCcServiceId(Integer ccServiceId) {
	this.ccServiceId = ccServiceId;
}





public String getLpPage() {
	return lpPage;
}


public void setLpPage(String lpPage) {
	this.lpPage = lpPage;
}


public String getPricePointDesc() {
	return pricePointDesc;
}


public void setPricePointDesc(String pricePointDesc) {
	this.pricePointDesc = pricePointDesc;
}


public String getValidityDesc() {
	return validityDesc;
}


public void setValidityDesc(String validityDesc) {
	this.validityDesc = validityDesc;
}


public String getServiceId() {
	return serviceId;
}


public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}


public String getPlanId() {
	return planId;
}


public void setPlanId(String planId) {
	this.planId = planId;
}


public String getBearerId() {
	return bearerId;
}


public void setBearerId(String bearerId) {
	this.bearerId = bearerId;
}


public String getSendPinUrl() {
	return sendPinUrl;
}


public void setSendPinUrl(String sendPinUrl) {
	this.sendPinUrl = sendPinUrl;
}


public String getPinValidationUrl() {
	return pinValidationUrl;
}


public void setPinValidationUrl(String pinValidationUrl) {
	this.pinValidationUrl = pinValidationUrl;
}


public String getUnsubUrl() {
	return unsubUrl;
}


public void setUnsubUrl(String unsubUrl) {
	this.unsubUrl = unsubUrl;
}

public String getMtUrl() {
	return mtUrl;
}


public void setMtUrl(String mtUrl) {
	this.mtUrl = mtUrl;
}


public String getPortalUrl() {
	return portalUrl;
}


public void setPortalUrl(String portalUrl) {
	this.portalUrl = portalUrl;
}


public String getAuthServiceId() {
	return authServiceId;
}


public void setAuthServiceId(String authServiceId) {
	this.authServiceId = authServiceId;
}


public String getAuthPresharedApiKey() {
	return authPresharedApiKey;
}


public void setAuthPresharedApiKey(String authPresharedApiKey) {
	this.authPresharedApiKey = authPresharedApiKey;
}


public String getAuthPresharedSmsKey() {
	return authPresharedSmsKey;
}


public void setAuthPresharedSmsKey(String authPresharedSmsKey) {
	this.authPresharedSmsKey = authPresharedSmsKey;
}


public String getAuthSmsApiKey() {
	return authSmsApiKey;
}


public void setAuthSmsApiKey(String authSmsApiKey) {
	this.authSmsApiKey = authSmsApiKey;
}


public String getAuthApiKey() {
	return authApiKey;
}


public void setAuthApiKey(String authApiKey) {
	this.authApiKey = authApiKey;
}


public String getSubMsg() {
	return subMsg;
}


public void setSubMsg(String subMsg) {
	this.subMsg = subMsg;
}


public String getUnsubMsg() {
	return unsubMsg;
}


public void setUnsubMsg(String unsubMsg) {
	this.unsubMsg = unsubMsg;
}

	
}
