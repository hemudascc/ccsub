package net.mycomp.mobivate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_mobivate_sms_dlr")
public class MobivateSMSDlr implements  Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name="action")
	private String action;	
	private String msisdn;	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="is_dlr_received")
	private Boolean isDlrReceived;	
	@Column(name = "dlr_received")
	private String dlrReceived;
	
	@Column(name = "dlr_orginator")
	private String dlrOrginator;
	@Column(name = "dlr_recipient")
	private String dlrRecipient;
	@Column(name = "dlr_message_text")
	private String dlrMessageText;
	@Column(name = "dlr_provider")
	private String dlrProvider;
    @Column(name = "dlr_reference")
	private String dlrReference;
	@Column(name = "dlr_smsgateway_id")
	private String dlrSmsgatewayId;
	@Column(name = "dlr_result")
	private String dlrResult;
	
	@Column(name = "duplicate")
	private Boolean duplicate;
	@Column(name = "send_to_adnetwork")
	private Boolean sendToAdnetwork;
	@Column(name="query_str")
	private String queryStr;
	
	@Column(name = "token_id")
	private Integer tokenId;
	@Column(name = "token")
	private String token;
	
	
	@Column(name = "dlr_recieved_time")
	private Timestamp dlrRecievedTime;
	private Boolean status;
	
	public MobivateSMSDlr(){}
	
	public MobivateSMSDlr(boolean status){
		
		this.dlrRecievedTime=new Timestamp(System.currentTimeMillis());
		this.status=status;
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

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Boolean getIsDlrReceived() {
		return isDlrReceived;
	}

	public void setIsDlrReceived(Boolean isDlrReceived) {
		this.isDlrReceived = isDlrReceived;
	}

	public String getDlrReceived() {
		return dlrReceived;
	}

	public void setDlrReceived(String dlrReceived) {
		this.dlrReceived = dlrReceived;
	}

	public String getDlrOrginator() {
		return dlrOrginator;
	}

	public void setDlrOrginator(String dlrOrginator) {
		this.dlrOrginator = dlrOrginator;
	}

	public String getDlrRecipient() {
		return dlrRecipient;
	}

	public void setDlrRecipient(String dlrRecipient) {
		this.dlrRecipient = dlrRecipient;
	}

	public String getDlrMessageText() {
		return dlrMessageText;
	}

	public void setDlrMessageText(String dlrMessageText) {
		this.dlrMessageText = dlrMessageText;
	}

	public String getDlrProvider() {
		return dlrProvider;
	}

	public void setDlrProvider(String dlrProvider) {
		this.dlrProvider = dlrProvider;
	}

	public String getDlrReference() {
		return dlrReference;
	}

	public void setDlrReference(String dlrReference) {
		this.dlrReference = dlrReference;
	}

	public String getDlrSmsgatewayId() {
		return dlrSmsgatewayId;
	}

	public void setDlrSmsgatewayId(String dlrSmsgatewayId) {
		this.dlrSmsgatewayId = dlrSmsgatewayId;
	}

	public String getDlrResult() {
		return dlrResult;
	}

	public void setDlrResult(String dlrResult) {
		this.dlrResult = dlrResult;
	}

	public Timestamp getDlrRecievedTime() {
		return dlrRecievedTime;
	}

	public void setDlrRecievedTime(Timestamp dlrRecievedTime) {
		this.dlrRecievedTime = dlrRecievedTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(Boolean duplicate) {
		this.duplicate = duplicate;
	}

	public Boolean getSendToAdnetwork() {
		return sendToAdnetwork;
	}

	public void setSendToAdnetwork(Boolean sendToAdnetwork) {
		this.sendToAdnetwork = sendToAdnetwork;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	
}
