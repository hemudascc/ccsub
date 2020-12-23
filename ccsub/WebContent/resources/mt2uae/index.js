function exit(){
	//alert("exit")
	location.href='${mt2UAEServiceConfig.portalUrl}';
	return false;
}

function changeLang2(lang){
	//alert("exit")
	location.href='http://mob.ccd2c.com/ccsub/cnt/mt2uae/chnage/lang?l='+lang+'&token=${token}&msisdn=${msisdn}&page=msisdn_missing';
	return false;
}

function changeLang(lang){
	//alert("exit")
	//token=document.getElementById("token");
	location.href='http://mob.ccd2c.com/ccsub/cnt/mt2uae/chnage/lang?l='+lang+'&token='+document.getElementById("token").value+'&msisdn='+document.getElementById("msisdn").value+'&page=msisdn_missing';
	return false;
}


function changeLangOTP(lang){
	//alert("exit")
	location.href='http://mob.ccd2c.com/ccsub/cnt/mt2uae/chnage/lang?l='+lang+'&token='+document.getElementById("token").value+'&msisdn='+document.getElementById("msisdn").value+'&page=wap_otp';
	return false;
}

