package net.mycom.nxt.vas;

public enum CGCallbackStatusEnum {
	Failed(0,"Failed"),
	Success(1,"Success"),
	User_Already_Registered(2,"User already registered"),
	UNABLE_TO_SUBSCRIBE(3,"unable to subscribe"),
	Unsubscribe_Complete(4,"Unsubscribe complete"),
	Send_Verification_Code_Limit_Exceeded(5,"Send Verification Code Limit Exceeded"),
	Exceed_Max_Attempt_Reached_Of_Invalid_Pin(6,"Exceed Max Attempt Reached Of Invalid Pin"),

	;
	private Integer status;
	private String statusDesc;
	CGCallbackStatusEnum(Integer status,String statusDesc){
		this.status=status;
		this.statusDesc=statusDesc;
	}
	public static String getStatusDesc(int status){
		for(CGCallbackStatusEnum cgcallbackStatusEnum:values()){
			if(cgcallbackStatusEnum.status==status){
				return cgcallbackStatusEnum.statusDesc;
			}
		}
		return null;
	}
}
