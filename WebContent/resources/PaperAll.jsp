<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="entity.Paper"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>All papers</title>
<%@ include file="../resources/blueprint_include_header.jspf"%>
<%@ include file="../resources/character_encoding_in_UTF-8.jspf"%>
</head>
<body>
	All papers:
	<br>

	<table>
		<thead>
			<tr>

				<th>ID</th>
				<form action="getAll.do" method="get">
					<c:forEach var="pr"
						items="${papers.iterator().next().proporties.keySet()}">
						<th>${pr}<input type="checkbox" name="order" value="${pr}">
						</th>
					</c:forEach>
					<tr>
						<th><input type="submit" value="sort"></th>
					</tr>
				</form>
			</tr>
		</thead>
		<c:forEach var="paper" items="${papers}">
			<tr>
				<td><a href="get.do?id=${paper.getId()}">${paper.getId()}</a>	<a href="http://localhost:8071/${paper.getId()}.pdf" target="pdfs">open</a></td>
				<c:forEach var="pr"
					items="${papers.iterator().next().proporties.keySet()}">
					<td>${paper.proporties.get(pr)}</td>
				</c:forEach>


			</tr>
		</c:forEach>
	</table>

	<%
		request.getSession().removeAttribute("papers");
	%>
</body>
</html>