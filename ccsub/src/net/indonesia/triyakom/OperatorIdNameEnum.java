package net.indonesia.triyakom;

import net.util.MConstants;



public enum OperatorIdNameEnum {

	XL(MConstants.TRIAKOM_INDONESIA_XL_OPERATOR_ID,"XL"),
	SAT(MConstants.TRIAKOM_INDONESIA_SAT_OPERATOR_ID,"SAT"),
	IM3(MConstants.TRIAKOM_INDONESIA_IM3_OPERATOR_ID,"IM3")
	;
	 
	OperatorIdNameEnum(int opId,String opName){
	  this.opId=opId;
	  this.opName=opName;
	 }
	
	public static OperatorIdNameEnum getOpertorName(int opId){
		for(OperatorIdNameEnum o:values()){
			if(o.opId==opId){
				return o;
			}
		}
		return null;
	}
	
	
	public static int getOpertorId(String opName){
		for(OperatorIdNameEnum o:values()){
			if(o.opName.equalsIgnoreCase(opName)){
				return o.opId;
			}
		}
		return -1;
	}
	 
	 private int opId;
	 private String opName;
	
	 public int getOpId() {
	  return opId;
	 }
	 public void setOpId(int opId) {
	  this.opId = opId;
	 }
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	} 
	}