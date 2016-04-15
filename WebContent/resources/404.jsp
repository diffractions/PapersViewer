<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error page</title>
</head>
<body>
	<h1>"${pageContext.request.requestURI}" not exists</h1>
	<h3 style="color: red;">${errorString}</h3>
	You can proceed your work with other pages:
	<br />
	<a href="/PapersViewer/Index.jsp"> Index.jsp</a>
	<br>
	<a href="/PapersViewer/getAll.do"> getAll</a>
</body>
</html>