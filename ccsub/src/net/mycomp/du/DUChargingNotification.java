package net.mycomp.du;


import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import net.persist.bean.AbsractChargingCallBack;

@Entity
@Table(name = "tb_du_charging_notification")
public class DUChargingNotification extends AbsractChargingCallBack{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "calling_party")
	private String callingParty;
	@Column(name = "service_id")
	private String serviceId;
	@Column(name = "service_type")
	private String serviceType;
	@Column(name = "request_plan")
	private String requestPlan;
	@Column(name = "sequence_no")
	private String sequenceNo;
	@Column(name = "charge_amount")
	private Double chargeAmount;
	@Column(name = "applied_plan")
	private String appliedPlan;
	@Column(name = "discount_plan")
	private String discountPlan;
	@Column(name = "validity_days")
	private String validityDays;
	@Column(name = "operation_id")
	private String operationId;
	@Column(name = "bearer_id")
	private String bearerId;
	@Column(name = "error_code")
	private String errorCode;
	private String result;
	@Column(name = "content_id")
	private String contentId;
	private String category;
	@Column(name = "opt_param1")
	private String optParam1;
	@Column(name = "opt_param2")
	private String optParam2;
	@Column(name = "opt_param3")
	private String optParam3;
	
	
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
	public String getCallingParty() {
		return callingParty;
	}
	public void setCallingParty(String callingParty) {
		this.callingParty = callingParty;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getRequestPlan() {
		return requestPlan;
	}
	public void setRequestPlan(String requestPlan) {
		this.requestPlan = requestPlan;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Double getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(Double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public String getAppliedPlan() {
		return appliedPlan;
	}
	public void setAppliedPlan(String appliedPlan) {
		this.appliedPlan = appliedPlan;
	}
	public String getDiscountPlan() {
		return discountPlan;
	}
	public void setDiscountPlan(String discountPlan) {
		this.discountPlan = discountPlan;
	}
	public String getValidityDays() {
		return validityDays;
	}
	public void setValidityDays(String validityDays) {
		this.validityDays = validityDays;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getBearerId() {
		return bearerId;
	}
	public void setBearerId(String bearerId) {
		this.bearerId = bearerId;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getOptParam1() {
		return optParam1;
	}
	public void setOptParam1(String optParam1) {
		this.optParam1 = optParam1;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public String getOptParam2() {
		return optParam2;
	}
	public void setOptParam2(String optParam2) {
		this.optParam2 = optParam2;
	}
	public String getOptParam3() {
		return optParam3;
	}
	public void setOptParam3(String optParam3) {
		this.optParam3 = optParam3;
	}
	
	
}
