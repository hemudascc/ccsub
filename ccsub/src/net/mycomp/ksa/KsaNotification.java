package net.mycomp.ksa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_ksa_notification")
public class KsaNotification implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//http://IP:PORT/<SP Context>?callingParty=<MSISDN>&maServiceId=xyz&bearerId=1
//	&operationId=SN&appliedPlan=ABC&price=100&validity=30&spTransactionId=178687987989
	@Id
	@GeneratedValue
	private Integer id;
	private String action;
	@Column(name = "calling_party")
	private String callingParty;
	@Column(name = "ma_service_id")
	private String maServiceId;
	@Column(name = "bearer_id")
	private String bearerId;
	@Column(name = "operation_id")
	private String operationId;
	@Column(name = "applied_plan")
	private String appliedPlan;
	private String price;
	private String validity;
	@Column(name = "sp_transaction_id")
	private String spTransactionId;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	public KsaNotification(){}
	public KsaNotification(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCallingParty() {
		return callingParty;
	}
	public void setCallingParty(String callingParty) {
		this.callingParty = callingParty;
	}
	public String getMaServiceId() {
		return maServiceId;
	}
	public void setMaServiceId(String maServiceId) {
		this.maServiceId = maServiceId;
	}
	public String getBearerId() {
		return bearerId;
	}
	public void setBearerId(String bearerId) {
		this.bearerId = bearerId;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getAppliedPlan() {
		return appliedPlan;
	}
	public void setAppliedPlan(String appliedPlan) {
		this.appliedPlan = appliedPlan;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getSpTransactionId() {
		return spTransactionId;
	}
	public void setSpTransactionId(String spTransactionId) {
		this.spTransactionId = spTransactionId;
	}
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
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
}
