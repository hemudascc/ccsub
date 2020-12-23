package net.mycomp.veoo;

public enum EnumVeooDeliveryStatus {

	
	VEOO_DELIVERED("0","VEOO_DELIVERED","DELIVERD","Delivered to handset",null),	
	VEOO_REJ_ORIGINBLOCKED("41","VEOO_REJ_ORIGINBLOCKED","REJECTD","Shortcode / Network / User combination is disallowed","Remove from database"),
	VEOO_REJ_BLOCKED("42","VEOO_REJ_BLOCKED","REJECTD","Subscriber is blocked","Remove from database"),
	VEOO_REJ_STOPPED("43","VEOO_REJ_STOPPED","UNDELIV","Subscriber in stopped state","Remove from database"),
	VEOO_REJ_INVSERVICE("44","VEOO_REJ_INVSERVICE","REJECTD","Missing or Invalid Service ID","Adjust service_id value"),
	VEOO_REJ_AVE("45","VEOO_REJ_AVE","REJECTD","Age verification failed","Remove from database"),
	VEOO_REJ_DEAD("46","VEOO_REJ_DEAD","REJECTD","Handset was not chargeable for a long time","This rejection occurs when no MTs have been delivered to this MSISDN for this service for an extended period. It indicates the MSISDN is no longer billable by this service"),
	VEOO_REJ_MVNOMSISDN("48","VEOO_REJ_MVNOMSISDN","REJECTD","MVNO MSISDN cannot be billed","Remove from database"),
	VEOO_REJ_NOMORE_SMS("49","VEOO_REJ_NOMORE_SMS","UNDELIV","All allowed charged messages sent","No need to continue to submit the messages this day. Please continue next day."),
	VEOO_FAIL_QUITEHRS("50","VEOO_FAIL_QUITEHRS","UNDELIV","Quite hours","Please try to resubmit a message in allowed hours. For most networks it will be daytime hours."),
	VEOO_REJ_SBSRBD("51","VEOO_REJ_SBSRBD","UNDELIV","Subscriber is already subscribed","Subsriber is already subsribed to the service. No need to retry"),
	VEOO_FAIL_BADSUB("56","VEOO_FAIL_BADSUB","UNDELIV","Unknown subscriber","HLR or remove from database"),
	VEOO_FAIL_EXPIRED("57","VEOO_FAIL_EXPIRED","EXPIRED","Network time out","Retry later"),
	VEOO_FAIL_NOCREDIT("58","VEOO_FAIL_NOCREDIT","UNDELIV","Insufficient credit","Retry as per operator requirements"),
	VEOO_FAIL_PORTEDOUT("59","VEOO_FAIL_PORTEDOUT","UNDELIV","MSISDN is ported away from operator","HLR or remove from database"),
	VEOO_FAIL_UNKNOWN("60","VEOO_FAIL_UNKNOWN","UNDELIV","Unknown error","Report to Veoo"),
	VEOO_FAIL_UNSUPPORT("61","VEOO_FAIL_UNSUPPORT","UNDELIV","Handset error","Retry or report to Veoo"),
	VEOO_FAIL_ABSENTSUB("62","VEOO_FAIL_ABSENTSUB","UNDELIV","Absent subscriber","HLR or remove from database"),
	VEOO_FAIL_ROAMING("63","VEOO_FAIL_ROAMING","UNDELIV","Subscriber is roaming","Retry later"),
	VEOO_FAIL_SYSERROR("64","VEOO_FAIL_SYSERROR","UNDELIV","System error","Report to Veoo"),
	VEOO_FAIL_NOSMSSERVICE("65","VEOO_FAIL_NOSMSSERVICE","UNDELIV","Teleservice not provisioned","Remove from database"),
	VEOO_FAIL_EXPIRED2("66","VEOO_FAIL_EXPIRED","UNDELIV","Expired","Report to Veoo"),
	VEOO_FAIL_TEMPERR("67","VEOO_FAIL_TEMPERR","UNDELIV","Temporary error","Report to Veoo"),
	VEOO_FAIL_REJCTD("68","VEOO_FAIL_REJCTD","UNDELIV","Rejected","Report to Veoo"),
	VEOO_OA_BLOCKED("69","VEOO_OA_BLOCKED","UNDELIV","Call bared","Remove from database"),
	VEOO_FAIL_RJCTD("70","VEOO_FAIL_RJCTD","REJECTD","Rejected","Report to Veoo"),
	VEOO_SPAM_RJCTD("71","VEOO_SPAM_RJCTD","UNDELIV","Spam/Flood Protection","Retry later"),
	VEOO_SS7_ERR("72","VEOO_SS7_ERR","UNDELIV","Routing/SS7 error","Report to Veoo"),
	VEOO_SPAM_DLTD("73","VEOO_SPAM_DLTD","DELETE","Spam/Flood Protection","Retry later"),
	VEOO_FAIL_ABSENTSBXP("74","VEOO_FAIL_ABSENTSBXP","EXPIRED","Absent subscriber","HLR or remove from database"),
	VEOO_FAIL_UNKN("75","VEOO_FAIL_UNKN","UNKNOWN","Unknown error","Report to Veoo"),
	VEOO_RJCTD_ABSENTSBXP("76","VEOO_RJCTD_ABSENTSBXP","REJECTD","Absent subscriber","HLR or remove from database"),
	VEOO_REJ_NOTSBSRBD("77","VEOO_REJ_NOTSBSRBD","REJECTD","User is not subscribed to the service","Remove from database"),
	VEOO_REJ_NOCREDIT("78","VEOO_REJ_NOCREDIT","REJECTD","Insufficient credit","Retry as per operator requirements"),
	VEOO_EXP_UNSUPPORT("79","VEOO_EXP_UNSUPPORT","EXPIRED","Handset error","Retry or report to Veoo"),
	VEOO_FAIL_ENC("80","VEOO_FAIL_ENC","REJECTD","Incorrect data_coding value","Adjust data_coding value"),
	VEOO_FAIL_VDPR("81","VEOO_FAIL_VDPR","EXPIRED","Message validity period has expired","Retry later"),
	VEOO_EXP_BLOCKED("82","VEOO_EXP_BLOCKED","EXPIRED","Subscriber is blocked","Remove from database"),
	VEOO_RJCTD_BADSUB("83","VEOO_RJCTD_BADSUB","REJECTD","Unknown subscriber","Remove from database"),
	VEOO_OA_BLOCKED_EXP("84","VEOO_OA_BLOCKED_EXP","EXPIRED","Call Barred","Remove from database"),
	VEOO_EXP_ROAMING("86","VEOO_EXP_ROAMING","EXPIRED","Subscriber is roaming","Retry later"),
	VEOO_FAIL_INVALID_OA("87","VEOO_FAIL_INVALID_OA","UNDELIV","Invalid Source Address","Please use allowed/valid source address"),
	VEOO_FAIL_INVALID_ESM("88","VEOO_FAIL_INVALID_ESM","REJECTD","Invalid esm_class field data","Please use appropriate esm_class	 as perSMPP spec"),
	VEOO_FAIL_INVALID_ESM2("89","VEOO_FAIL_INVALID_ESM","REJECTD","Invalid message length","That happens when you submit concatenated message and split its parts incorrectly. Please refer to https://docs.veoo.com/docs/smpp_docs#header-concatenated-messages for more details	"),
	VEOO_FAIL_SRV_RSRS("90","VEOO_FAIL_SRV_RSRS","UNDELIV","Operation was rejected due to insufficient server resources","There is a load on MNO side	 please re-submit the message later"),
	VEOO_MNOSUBSEXP("91","VEOO_MNOSUBSEXP","EXPIRED","Subscription has expired on Operator side","Last successful billing was done more than 90 days earlier	 user is unsubscribed on operator’s side - please unsibsribe end-user"),
	VEOO_FAIL_INVALID_UDH("92","VEOO_FAIL_INVALID_UDH","REJECTD","Invalid UDH length indicator","The message was submitted with unsupported or wrong UDH indicator."),	
	VEOO_MT_QIF("93","VEOO_MT_QIF","UNDELIV","MT Queue is Full","There is a queue that is full. Please retry later."),	
	VEOO_DND_BLOCK("205","VEOO_DND_BLOCK","UNDELIV","DND Restriction","In case subsriber would like to receive messages ask him to contact operator in order to remove DND till then do not retry	");

	 String statusCode;
	 String statusText;
	 String state;
	 String label;
	 String actionRequired;
	 
	 EnumVeooDeliveryStatus(String statusCode,String statusText,String state,String label,
			 String actionRequired ){
		 this.statusCode=statusCode;
		 this.statusText=statusText;
		 this.state=state;
		 this.label=label;
		 this.actionRequired=actionRequired;
		 
	 }
	
}
