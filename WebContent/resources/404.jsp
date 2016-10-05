<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error page</title>
<%@ include file="../resources/blueprint_include_header.jspf"%>
<%@ include file="../resources/character_encoding_in_UTF-8.jspf"%>
</head>
<body>
	<center>
		<span
			style="color: fuchsia; font-size: 800%; border-width: medium; border-color: teal; border-style: solid; border-width: 4mm; margin-left: -50%;">${errorCode}</span>
	</center>
 
	<%
		String err = (String) application
				.getInitParameter("project_context");
		if (request.getAttribute("errorString") != null) {
	%> 
	<h1>
		Error request: "<span style="color: blue;">${pageContext.request.requestURI}</span>"
	</h1>
	<h3>
		Error message: "<span style="color: red;">${errorString}</span>"
	</h3>
	<%
		}
	%>

	You can proceed your work with other pages:
	<%@include file="../Index.jsp"%> 
	
</body>
</html>