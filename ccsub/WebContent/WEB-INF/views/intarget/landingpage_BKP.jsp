<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width initial-scale=1">
<title>GamePad</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/stylesheet.css">
<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
<style type="text/css">
.input{
	border:1px solid #ababab;
	padding:7px 8px;
	font-size:18px;
	margin:15px auto 5px;
	color:#898989;
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
