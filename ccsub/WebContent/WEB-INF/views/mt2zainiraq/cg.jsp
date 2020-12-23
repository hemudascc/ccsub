<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<a href="${cgUrl}" referrerpolicy="unsafe-url" id="redirect"></a>
</body>
<script>
$(document).ready(function(){
	 $("#redirect")[0].click();
});
</script>
</html>