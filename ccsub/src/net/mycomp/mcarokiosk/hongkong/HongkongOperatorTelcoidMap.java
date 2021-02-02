package net.mycomp.mcarokiosk.hongkong;

import net.util.MConstants;

public enum HongkongOperatorTelcoidMap {

	
	SMARTONE(MConstants.MK_HONGKONG_SMARTONE_OPERATOR_ID,4,"SMARTONE"),
	HUTCHISON(MConstants.MK_HONGKONG_HUTCHISON_OPERATOR_ID,1,"HUTCHISON");
	
	private int opId;
	private int telcoId;
	private String descp;
	
	HongkongOperatorTelcoidMap(int opId,int telcoId,String descp){
		this.opId=opId;
		this.telcoId=telcoId;
		this.descp=descp;
	}
	
	
	public static int getOperatorId(int telcoId){
		
		for(HongkongOperatorTelcoidMap thiaOperatorTelcoMap:values()){
			if(thiaOperatorTelcoMap.getTelcoId()==telcoId){
				return thiaOperatorTelcoMap.getOpId();
			}
		}
		return 0;		
	}
	
	public static int getTelcoId(int operatorId){
		for(HongkongOperatorTelcoidMap thiaOperatorTelcoMap:values()){
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
