package net.mycomp.actel;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_actel_du_dlr")
public class ActelDuDlr implements Serializable{
	
		@Id
		@GeneratedValue
		private Integer id;
		private String code;
		private String description;
		@Column(name="token")
		private String correlator;
		private String msisdn;
		@Column(name="op_id")
	    private String opId;
		@Column(name="response_status_code")
		private String responseStatusCode;
		@Column(name="response_status_description")
	    private String responseStatusDescription;
		@Column(name="service_id")
	    private String serviceId;
		@Column(name="product_id")
	    private String productId;
		@Column(name="club_id")
	    private String clubId;
		@Column(name="sub_id")
		private String subId;
		@Column(name="sub_status")
	    private String subStatus;
		@Column(name="otp_status")
	    private String otpStatus;
		@Column(name="otp_description")
		private String otpDescription;
		@Column(name="create_time")
		private  Timestamp createTime;
		private Boolean status;
		@Column(name="callback_url")
		private String callbackUrl;
		private String response;
		
		
		public String getResponse() {
			return response;
		}
		public void setResponse(String response) {
			this.response = response;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getCallbackUrl() {
			return callbackUrl;
		}
		public void setCallbackUrl(String callbackUrl) {
			this.callbackUrl = callbackUrl;
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
		public ActelDuDlr() {}
		public ActelDuDlr(boolean status) {
			this.status=status;
			this.createTime=new Timestamp(System.currentTimeMillis());
			
		}
		public String getOpId() {
			return opId;
		}
		public void setOpId(String opId) {
			this.opId = opId;
		}
		public String getResponseStatusCode() {
			return responseStatusCode;
		}
		public void setResponseStatusCode(String responseStatusCode) {
			this.responseStatusCode = responseStatusCode;
		}
		public String getResponseStatusDescription() {
			return responseStatusDescription;
		}
		public void setResponseStatusDescription(String responseStatusDescription) {
			this.responseStatusDescription = responseStatusDescription;
		}
		public String getServiceId() {
			return serviceId;
		}
		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
		}
		public String getClubId() {
			return clubId;
		}
		public void setClubId(String clubId) {
			this.clubId = clubId;
		}
		public String getSubId() {
			return subId;
		}
		public void setSubId(String subId) {
			this.subId = subId;
		}
		public String getSubStatus() {
			return subStatus;
		}
		public void setSubStatus(String subStatus) {
			this.subStatus = subStatus;
		}
		public String getOtpStatus() {
			return otpStatus;
		}
		public void setOtpStatus(String otpStatus) {
			this.otpStatus = otpStatus;
		}
		public String getOtpDescription() {
			return otpDescription;
		}
		public void setOtpDescription(String otpDescription) {
			this.otpDescription = otpDescription;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getCorrelator() {
			return correlator;
		}
		public void setCorrelator(String correlator) {
			this.correlator = correlator;
		}
		public String getMsisdn() {
			return msisdn;
		}
		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
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
