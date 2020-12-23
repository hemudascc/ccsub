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
input[type="text"] {
    padding: 12px 20px;
    margin: 8px 0;
    box-sizing: border-box;
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
    background-color: #0033cc;
    border: none;
    color: white;
    padding: 18px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 23px;
    font-weight: bold;
    width: 235px;
    border-radius: 12px;
	line-height: inherit;
}

.button a {
    transition: 0.2s ease-in-out;
}
.ok {
    text-align: center;
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
    	<div>
					<p style="text-align:center;font-size:14px;font-weight:bold;">Insert your Mobile Number:</p>
					<p style="text-align:center;font-size:14px;font-weight:bold;"><input  id="msisdn" name="msisdn" value="" size="30" type="text" 
             onKeyPress="if(this.value.length==12||/^\d*$/.test(this.value)==false) return false;" placeholder="Mobile Number" title="Mobile number"/> </p>
		</div>
					
			<div class="ok" ><button class="button" onclick="sendussd('${inTargetConfig.serviceId}')">SUBSCRIBE</button>&nbsp;</div>
				<p style="text-align:center;font-size:14px;font-weight:bold;">Get Access to subscription Game Pad service for KES30/day.
       Game Pad is premium games content like  action,adventure,strategic more.
        Only users 18 years or over.</p>
    <!--	 <center>
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
      </div>-->
   
</body>

</html>
