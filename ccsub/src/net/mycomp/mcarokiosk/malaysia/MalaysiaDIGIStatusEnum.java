package net.mycomp.mcarokiosk.malaysia;

import org.apache.log4j.Logger;

public enum MalaysiaDIGIStatusEnum {

	
	
	FAILED_TO_DELIVER_MT("2","FAILED_TO_DELIVER_MT",true),
	QUEUE_ERROR("100","QUEUE_ERROR",true),
	SYSTEM_INTERNAL_ERROR("101","SYSTEM_INTERNAL_ERROR",true),
	SYSTEM_INTERNAL_ERROR2("102","SYSTEM_INTERNAL_ERROR",true),
	SYSTEM_INTERNAL_ERROR3("103","SYSTEM_INTERNAL_ERROR",true),
	WRONG_SYNTAX("138","WRONG_SYNTAX",true),
	DB_OPERATION_FAIL("147","DB_OPERATION_FAIL",true),
	INTERNAL_ERROR("149","INTERNAL_ERROR",true),
	INTRACTIVE_SESSION_OPERATION_FAIL("150","INTRACTIVE_SESSION_OPERATION_FAIL",true),
	INTERNAL_ERROR2("153","INTERNAL_ERROR",true),
	INSUFFICEINT_BALANCE("202","INSUFFICEINT_BALANCE",true),
	SUSCRIBER_IS_NOT_ACTIVE("203","SUSCRIBER_IS_NOT_ACTIVE",true),
	INVALID_CP_SESSION("301","INVALID_CP_SESSION",true),
	CP_SESSION_OPERATION_FAIL("302","CP_SESSION_OPERATION_FAIL",true),
	INTERNAL_ERROR3("500","INTERNAL_ERROR",true),
	INTERNAL_ERROR4("601","INTERNAL_ERROR",true),
	INTERNAL_ERROR5("602","INTERNAL_ERROR",true),
	INTERNAL_ERROR6("814","INTERNAL_ERROR",true)
	;
	private static final Logger logger = Logger
			.getLogger(MalaysiaDIGIStatusEnum.class);
	private String status;
	private String statusDesc;
	private Boolean retryBilling;
	
	 MalaysiaDIGIStatusEnum(String status,String statusDesc, Boolean retryBilling){
		this.status=status;
		this.statusDesc=statusDesc;
		this.retryBilling=retryBilling;
	}

	 public static Boolean isRetry(String status){
		 try{
			 for(MalaysiaDIGIStatusEnum malaysiaDIGIStatusEnum:values()){
				 if(malaysiaDIGIStatusEnum.getStatus().equalsIgnoreCase(status)){
					 return malaysiaDIGIStatusEnum.getRetryBilling();
				 }
			 }
		 }catch(Exception ex){
			 logger.error("Exception ",ex);
		 }
		 return false;
	 }
	 
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Boolean getRetryBilling() {
		return retryBilling;
	}

	public void setRetryBilling(Boolean retryBilling) {
		this.retryBilling = retryBilling;
	}
}
