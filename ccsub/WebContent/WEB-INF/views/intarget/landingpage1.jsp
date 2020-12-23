<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<title>GamePad</title>
<!--<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/stylesheet.css">-->
<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
<style type="text/css">
.input{
	border:1px solid #ababab;
	padding:7px 8px;
	font-size:18px;
	margin:15px auto 5px;
	color:#898989;
}
.banner {
    width: 100%;
    margin: auto;
}
body {
    font-family: Arial, Helvetica, sans-serif;
}
.banner img {
    max-width: 100%;
}
.buttonbar {
    width: 100%;
    margin: auto;
    text-align: center;
    padding-top: 5px;
}

.button {
    width: 150px;
    margin: auto;
    height: 25px;
    font-size: 15px;
    color: #333;
    padding-top: 8px;
    border-radius: 5px;
    background: rgba(235,233,249,1);
    background: -moz-linear-gradient(top, rgba(235,233,249,1) 0%, rgba(216,208,239,1) 50%, rgba(206,199,236,1) 51%, rgba(193,191,234,1) 100%);
    background: -webkit-gradient(left top, left bottom, color-stop(0%, rgba(235,233,249,1)), color-stop(50%, rgba(216,208,239,1)), color-stop(51%, rgba(206,199,236,1)), color-stop(100%, rgba(193,191,234,1)));
    background: -webkit-linear-gradient(top, rgba(235,233,249,1) 0%, rgba(216,208,239,1) 50%, rgba(206,199,236,1) 51%, rgba(193,191,234,1) 100%);
    background: -o-linear-gradient(top, rgba(235,233,249,1) 0%, rgba(216,208,239,1) 50%, rgba(206,199,236,1) 51%, rgba(193,191,234,1) 100%);
    background: -ms-linear-gradient(top, rgba(235,233,249,1) 0%, rgba(216,208,239,1) 50%, rgba(206,199,236,1) 51%, rgba(193,191,234,1) 100%);
    background: linear-gradient(to bottom, rgba(235,233,249,1) 0%, rgba(216,208,239,1) 50%, rgba(206,199,236,1) 51%, rgba(193,191,234,1) 100%);
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ebe9f9', endColorstr='#c1bfea', GradientType=0 );
    border: 1px solid #a0a0a0;
}
.button a {
    color: #333;
    font-weight: bold;
    text-decoration: none;
}

</style>

<script type="text/javascript">

function sendussd(id){ 
	
	//alert('${pageContext.request.contextPath}'+'/cnt/hgate/send/ussd?serviceid='+id);
	 var mobNum = $("#msisdn").val();
	 //alert(mobNum+ "id:: "+id+", token:: ${token}");
     var filter = /^\d*$/;
     if (filter.test(mobNum)==false||mobNum.length!=12) {
       alert("Please enter valid  12 digit mobile number");       
       return  false;
    }
     
    $.ajax({
        url: '${pageContext.request.contextPath}'+
        '/cnt/hgate/send/ussd?serviceid='+id+'&msisdn='+mobNum+"&token=${token}",
        type: 'GET',
        crossDomain:true,
        data: {
            format: 'jsonp'
            
        },
        timeout: 180000,
        success: function(response) {
        	
        	var json = $.parseJSON(response);
        	//alert(json.messgae);
        	window.location.href=json;
        	
         },
    error: function(textstatus,response) {
        alert("Some technical issue to unsubscribe.please try after some time");
        setTimeout(function(){// wait for 5 secs(2)
            location.reload(); // then reload the page.(3)
       }, 5000); 
    }
    });
    return false; // for good measure
}

</script>
</head>

<body>
		
    	<div class="banner" style="margin:0px;">
    	<img 
    	src="${pageContext.request.contextPath}/resources/images/intarget/banner_2.jpg" height="80%" width="100%">
    	
    	</div>
    	
    	 <center>
    	<b>Insert your Mobile Number:</b>
            <input  id="msisdn" name="msisdn" value="" type="text" 
             onKeyPress="if(this.value.length==12||/^\d*$/.test(this.value)==false) return false;" placeholder="Mobile Number" title="Mobile number"/>
           </center>
        <div class="buttonbar">
            
         	<div class="button">
        	<a href="#" onclick="sendussd('${inTargetConfig.serviceId}')">SUBSCRIBE</a>
        	</div>
        	<br>
      </div>
      <div>
      <p><b>Get Access to subscription Game Pad service for KES30/day.
       Game Pad is premium games content like  action,adventure,strategic more.
        Only users 18 years or over. </b> </p>
      </div>
   
</body>

</html>
