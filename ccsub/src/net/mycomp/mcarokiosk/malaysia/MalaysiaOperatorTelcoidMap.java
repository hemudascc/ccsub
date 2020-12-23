package net.mycomp.mcarokiosk.malaysia;

import net.util.MConstants;

public enum MalaysiaOperatorTelcoidMap {

	
	DIGI(MConstants.MK_MALAYSIA_DIGI_OPERATOR_ID,3,"DIGI"),
	UMOBILE(MConstants.MK_MALAYSIA_UMOBILE_OPERATOR_ID,5,"UOBILE");
	
	private int opId;
	private int telcoId;
	private String descp;
	
	MalaysiaOperatorTelcoidMap(int opId,int telcoId,String descp){
		this.opId=opId;
		this.telcoId=telcoId;
		this.descp=descp;
	}
	
	
	public static int getOperatorId(int telcoId){
		
		for(MalaysiaOperatorTelcoidMap thiaOperatorTelcoMap:values()){
			if(thiaOperatorTelcoMap.getTelcoId()==telcoId){
				return thiaOperatorTelcoMap.getOpId();
			}
		}
		return 0;		
	}
	
	public static int getTelcoId(int operatorId){
		for(MalaysiaOperatorTelcoidMap thiaOperatorTelcoMap:values()){
			if(thiaOperatorTelcoMap.getOpId()==operatorId){
				return thiaOperatorTelcoMap.getTelcoId();
			}
		}
		return 0;		
	}
	
	public int getOpId() {
		return opId;
	}
	public void setOpId(int opId) {
		this.opId = opId;
	}
	public int getTelcoId() {
		return telcoId;
	}
	public void setTelcoId(int telcoId) {
		this.telcoId = telcoId;
	}
	public String getDescp() {
		return descp;
	}
	public void setDescp(String descp) {
		this.descp = descp;
	}
}
