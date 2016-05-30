<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error page</title>
</head>
<body>

	<center>
		<span
			style="color: fuchsia; font-size: 800%; border-width: medium; border-color: teal; border-style: solid; border-width: 4mm; margin-left: -50%;">${errorCode}</span>
	</center>
	<h1>
		Error request: "<span style="color: blue;">${pageContext.request.requestURI}</span>"
	</h1>
	<h3>
		Error message: "<span style="color: red;">${errorString}</span>"
	</h3>
	You can proceed your work with other pages:
	<br />
	<a href="/PapersViewer/Index.jsp"> Index.jsp</a>
	<br>
	<a href="/PapersViewer/getAll.do"> getAll</a>
</body>
</html>