let msisdn,token,subscriptionContractId;
var numberRegex = /^\s*[+-]?(\d+|\.\d+|\d+\.\d+|\d+\.)(e[+-]?\d+)?\s*$/;
$(document).ready(function(){
	token = $("#token").val();
	
	/*click on main subscribe button*/
	$("#sub-button").click(function(){
		$("#pin-label").hide();
		$("#msisdn-label").show();
		$("#msisdn").show();
		$("#msisdn").val("");
		$("#pin").hide();
		$("#send-pin").show();
		$("#subscribe-pin").hide();
		$("#resend-pin").hide();
		$("#message").hide();
		$("#missing-msisdn-modal").modal("show");
	});
	
	$("#send-pin").click(function(){
		if(numberRegex.test($("#msisdn").val())){
		$.get("./tpay/send-pin?token="+token+"&msisdn="+$("#msisdn").val(), function(data, status){
			var obj = JSON.parse(data);
					if(obj.operationStatusCode == 0){
						$("#pin-label").show();
						$("#msisdn-label").hide();
						$("#msisdn").hide();
						$("#pin").val("");
						$("#pin").show();
						$("#send-pin").hide();
						$("#subscribe-pin").show();
						$("#resend-pin").show();
						$("#message").hide();
						subscriptionContractId = obj.subscriptionContractId;
				     }else {
				    	 $("#message").text("Please enter a valid mobile number.");
				    	 $("#message").show();
					}
		});}else{
			 $("#message").text("Please enter a valid mobile number.");
	    	 $("#message").show();
		}
	});	
		$("#resend-pin").click(function(){
			$.get("./tpay/resend-pin?token="+token+"&msisdn="+$("#msisdn").val()+"&subscriptionContractId="+subscriptionContractId, function(data, status){
				var obj = JSON.parse(data);
				if(obj.operationStatusCode == 0){
					$("#message").text("Pin has been resent to you.");
			    	$("#message").show();
			    	
				}
			});
		});
		
		$("#subscribe-pin").click(function(){
			if(numberRegex.test($("#pin").val())){
			$.get("./tpay/validate-pin?token="+token+"&msisdn="+$("#msisdn").val()+"&subscriptionContractId="+subscriptionContractId+"&pin="+$("#pin").val(), function(data, status){
				var obj = JSON.parse(data);
				if(obj.operationStatusCode == 0){
					$("#message").text("Pin has been resent to you.");
			    	$("#message").show();
			    	window.open(obj.protalUrl);
			    	sendWelcomeMT(msisdn,token);
				}
				else{
					$("#message").text("Please enter a valid pin.");
			    	$("#message").show();
				}
			});}
			else{
				 $("#message").text("Please enter a valid pin.");
		    	 $("#message").show();
			}
		});
});


function sendWelcomeMT(msisdn,token){
	$.get("./tpay/send-welcome-mt?token="+token+"&msisdn="+$("#msisdn").val(), function(data, status){
		var obj = JSON.parse(data);
		if(obj.messageDeliveryStatus==true){
			$("#message").text("You are subscribed to the service successfully.");
		}
	});
}