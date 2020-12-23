<%
if(request.getServerName().contains("life.limemoments.com")){
	response.sendRedirect("http://life.limemoments.com/ccsub/cnt/cmp?adid=1&cmpid=3&token=ccc");
}else{
response.sendRedirect("http://www.МирИгр.com/wog/home");
}
%>
