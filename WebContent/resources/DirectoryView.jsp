<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="dates" class="java.util.Date" />


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<table>
		<c:forEach var="file" items="${filesChildren}">
			<tr>
				<th align="left"><a href="${file.getName()}">${file.getName()}</a></th>
				<th align="right">${file.length()/1000.0}KB</th>
			</tr>
		</c:forEach>
	</table>

	<%
		request.getSession().removeAttribute("filesChildren");
	%>

	<br />
	<c:out value="${dates}"></c:out>


</body>
</html>