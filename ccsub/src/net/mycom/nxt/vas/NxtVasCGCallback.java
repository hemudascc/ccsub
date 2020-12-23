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
@Table(name = "tb_nxt_vas_cgcallback")
public class NxtVasCGCallback implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	@Column(name = "campaign_id")
	private Integer campaignId;
	private String transaction;
	@Column(name = "nxt_vas_cg_status")
	private String nxtVasCgStatus;
	@Column(name = "status_desc")
	private String statusDesc;
	private String msisdn;
	@Column(name = "subscriber_id")
	private String subscriberId;
	private String mcc;
	private String mnc;
	private String language;
	
	@Column(name="query_str")
	private String  queryStr;
	
	@Column(name = "create_time")
	private Timestamp  createTime;
	private Boolean status;
	
	public NxtVasCGCallback(){
		
	}
	public NxtVasCGCallback(boolean status){
		this.status=status;
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
	public String getTransaction() {
		return transaction;
	}
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	public String getNxtVasCgStatus() {
		return nxtVasCgStatus;
	}
	public void setNxtVasCgStatus(String nxtVasCgStatus) {
		this.nxtVasCgStatus = nxtVasCgStatus;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
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
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	
}
