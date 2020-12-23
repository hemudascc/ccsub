package net.mycomp.etisalat;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.persist.bean.AbsractChargingCallBack;

@Entity
@Table(name = "tb_etisalat_charging_callback")
@XmlRootElement(name="Call_url")
@XmlAccessorType(XmlAccessType.FIELD)

public class EtisalatChargingCallback extends AbsractChargingCallBack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="transaction_id1")
	@Column(name = "transaction_id1")
	private String transactionId1;
	@XmlElement(name="transaction_id2")
	@Column(name = "transaction_id2")
	private String transactionId2;
	@XmlElement(name="msisdn")
	private String msisdn;
	
	@Column(name = "package_id")
	@XmlElement(name="package_id")
	private String packageId;
	@Column(name = "transaction_type")
	@XmlElement(name="TransactionType")
	private String transactionType;
	@XmlElement(name="Amount")
	private String amount;
	@XmlElement(name="keyword")
	private String keyword;
	@XmlElement(name="Channel")
	private String channel;
	
//	@Column(name = "create_time")
//	@XmlTransient
//	private Timestamp createTime;
	
//	@XmlTransient
//	@Column(name = "duplicate")
//	private Boolean duplicate;
	
	@XmlTransient
	@Column(name="request_data")
	private String requestData;
	
	@XmlTransient
	@Column(name="sms_request")
	private String smsRequest;
	@XmlTransient
	@Column(name="sms_respose")
	private String smsResponse;
	
	
	@XmlTransient
	@Column(name = "status")
	private Boolean status;
	
	public EtisalatChargingCallback(){
		super.setCreateTime(new Timestamp(System.currentTimeMillis()));
		this.status=true;
		super.setDuplicate(false);
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

	
	public String getTransactionId1() {
		return transactionId1;
	}
	public void setTransactionId1(String transactionId1) {
		this.transactionId1 = transactionId1;
	}
	public String getTransactionId2() {
		return transactionId2;
	}
	public void setTransactionId2(String transactionId2) {
		this.transactionId2 = transactionId2;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getSmsRequest() {
		return smsRequest;
	}

	public void setSmsRequest(String smsRequest) {
		this.smsRequest = smsRequest;
	}

	public String getSmsResponse() {
		return smsResponse;
	}

	public void setSmsResponse(String smsResponse) {
		this.smsResponse = smsResponse;
	}
	
}
