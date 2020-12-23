package net.mycomp.ksa;

import net.util.MConstants;

public enum KSAServiceEnum {

	ACT("SN",MConstants.ACT),
	ACT2("SA",MConstants.ACT),
	DCT("SS",MConstants.DCT),
	RENEW("SR",MConstants.RENEW);
	
	private String operationId;
	private String action;
	
	KSAServiceEnum(String operationId,String action){
		this.operationId=operationId;
		this.action=action;
	}

	public static  String getMyAction(String operationId){
		for(KSAServiceEnum ksaServiceEnum:values()){
			if(ksaServiceEnum.getOperationId().equalsIgnoreCase(operationId)){
				return ksaServiceEnum.getAction();
			}
		}
		return "";
	}
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
