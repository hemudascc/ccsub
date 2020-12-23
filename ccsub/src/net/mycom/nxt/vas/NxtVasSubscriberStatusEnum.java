package net.mycom.nxt.vas;

public enum NxtVasSubscriberStatusEnum {

	NEW_SUBSCRIBER(1,"new"),
	ALREADY_EXIST_SUBSCRIBER(4,"exist"),
	NONE(0,"");
	
	public int status;
	public String statusDescp;
	
	NxtVasSubscriberStatusEnum(int status,String statusDescp){
		this.status=status;
		this.statusDescp=statusDescp;
	}
	
	public static NxtVasSubscriberStatusEnum getNxtVasSubscriberStatusEnum(int status){
		for(NxtVasSubscriberStatusEnum nxtVasSubscriberStatusEnum:values()){
			if(nxtVasSubscriberStatusEnum.status==status){
				return nxtVasSubscriberStatusEnum;
			}
			
		}
		return NEW_SUBSCRIBER;
	}
}
