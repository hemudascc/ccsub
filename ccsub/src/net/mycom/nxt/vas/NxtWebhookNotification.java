package net.mycom.nxt.vas;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_nxt_webhook_notification")
public class NxtWebhookNotification implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	private String action;
	@Column(name = "product_id")
	private String productId;
	@Column(name = "subscriber_id")
	private String subscriberId;
	@Column(name="subscriber_status")
	private String subscriberStatus;
	private String currency;
	@Column(name = "amount_charge")
	private String amountCharge;
	@Column(name = "payment_date")
	private String paymentDate;
	private String msisdn;
	private String mcc;
	private String mnc;
	@Column(name = "transacton_id")
	private String transactonId;
	@Column(name="language")
	private String language;
	@Column(name="query_str")
	private String  queryStr;
	
	@Column(name="token")
	private String  token;
	
	
	@Column(name = "create_time")
	private Timestamp createTime;
	@Column(name="duplicate")
	private boolean duplicate;
	private Boolean  status;
	
	public NxtWebhookNotification(){}
	
	public NxtWebhookNotification(boolean status){
		status=true;
		createTime=new Timestamp(System.currentTimeMillis());
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
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getAmountCharge() {
		return amountCharge;
	}
	public void setAmountCharge(String amountCharge) {
		this.amountCharge = amountCharge;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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
	public String getTransactonId() {
		return transactonId;
	}
	public void setTransactonId(String transactonId) {
		this.transactonId = transactonId;
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
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public boolean isDuplicate() {
		return duplicate;
	}
	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSubscriberStatus() {
		return subscriberStatus;
	}

	public void setSubscriberStatus(String subscriberStatus) {
		this.subscriberStatus = subscriberStatus;
	}
	
}
