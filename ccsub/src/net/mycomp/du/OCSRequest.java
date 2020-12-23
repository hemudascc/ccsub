package net.mycomp.du;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ocsRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OCSRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name="requestType")
	private String requestType;
	@XmlElement(name="serviceNode")
	private String serviceNode;
	@XmlElement(name="sequenceNo")
	private String sequenceNo;
	@XmlElement(name="callingParty")
	private String callingParty;
	@XmlElement(name="serviceType")
	private String serviceType;
	@XmlElement(name="serviceId")
	private String serviceId;
	@XmlElement(name="bearerId")
	private String bearerId;
	@XmlElement(name="chargeAmount")
	private Integer chargeAmount;
	@XmlElement(name="planId")
	private String planId;
	@XmlElement(name="asyncFlag")
	private String asyncFlag;
	@XmlElement(name="renewalFlag")
	private String renewalFlag;
	@XmlElement(name="bundleType")
	private String bundleType;
	@XmlElement(name="serviceUsage")
	private String serviceUsage;
	@XmlElement(name="promoId")
	private String promoId;
	@XmlElement(name="subscriptionFlag")
	private String subscriptionFlag;
	@XmlElement(name="optionalParameter1")
	private String optionalParameter1;
	@XmlElement(name="optionalParameter2")
	private String optionalParameter2;
	@XmlElement(name="optionalParameter3")
	private String optionalParameter3;
	@XmlElement(name="optionalParameter4")
	private String optionalParameter4;
	@XmlElement(name="optionalParameter5")
	private String optionalParameter5;

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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getServiceNode() {
		return serviceNode;
	}

	public void setServiceNode(String serviceNode) {
		this.serviceNode = serviceNode;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getCallingParty() {
		return callingParty;
	}

	public void setCallingParty(String callingParty) {
		this.callingParty = callingParty;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getBearerId() {
		return bearerId;
	}

	public void setBearerId(String bearerId) {
		this.bearerId = bearerId;
	}

	public Integer getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(Integer chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getAsyncFlag() {
		return asyncFlag;
	}

	public void setAsyncFlag(String asyncFlag) {
		this.asyncFlag = asyncFlag;
	}

	public String getRenewalFlag() {
		return renewalFlag;
	}

	public void setRenewalFlag(String renewalFlag) {
		this.renewalFlag = renewalFlag;
	}

	public String getBundleType() {
		return bundleType;
	}

	public void setBundleType(String bundleType) {
		this.bundleType = bundleType;
	}

	public String getServiceUsage() {
		return serviceUsage;
	}

	public void setServiceUsage(String serviceUsage) {
		this.serviceUsage = serviceUsage;
	}

	public String getPromoId() {
		return promoId;
	}

	public void setPromoId(String promoId) {
		this.promoId = promoId;
	}

	public String getSubscriptionFlag() {
		return subscriptionFlag;
	}

	public void setSubscriptionFlag(String subscriptionFlag) {
		this.subscriptionFlag = subscriptionFlag;
	}

	public String getOptionalParameter1() {
		return optionalParameter1;
	}

	public void setOptionalParameter1(String optionalParameter1) {
		this.optionalParameter1 = optionalParameter1;
	}

	public String getOptionalParameter2() {
		return optionalParameter2;
	}

	public void setOptionalParameter2(String optionalParameter2) {
		this.optionalParameter2 = optionalParameter2;
	}

	public String getOptionalParameter3() {
		return optionalParameter3;
	}

	public void setOptionalParameter3(String optionalParameter3) {
		this.optionalParameter3 = optionalParameter3;
	}

	public String getOptionalParameter4() {
		return optionalParameter4;
	}

	public void setOptionalParameter4(String optionalParameter4) {
		this.optionalParameter4 = optionalParameter4;
	}

	public String getOptionalParameter5() {
		return optionalParameter5;
	}

	public void setOptionalParameter5(String optionalParameter5) {
		this.optionalParameter5 = optionalParameter5;
	}

}
