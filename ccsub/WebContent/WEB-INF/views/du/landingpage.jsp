<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DU</title>
<style type="text/css">
 
input[type=submit] {
    width: 50%;
    background-color: #FF5733;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

p.b {
    font-family: Arial, Helvetica, sans-serif;
}
</style>
</head>
<body>
<center>
<form action="cnt/subscription/confirm">
<img src="" height="70%" width="100%"/><br>

    <b style="color: #2EA118; font-size: 32px;"></span></p></b>
    <b><p class="b" style="color: #33353a; font-size: 20px;">play unlimited videos</p></b>
    <b><p style="color: #000000; font-size: 22px; ">AED 7 weekly (Auto Renewal)</p></b>
    <input type="hidden" value="${tokentocg}"/>
    <input type="hidden" value="${serviceid}"/>
    <a href="" style="font-size: 50px;background-color:#FF5733;
     border-radius: 20px 10px;">Subscribe Now!</a>
    <p>
    &#9679;  By subscribing to the service, you are accepting all Terms & Conditions</p>
    <p> of the service & authorize du to share your mobile number with Adpoke, </p>
    <p>who manages this subscription service. </p>
    <p>&#9679;  Data charges would apply for browsing & downloading contents on this portal</p>
    
</form>
</center>
</body>
</html>