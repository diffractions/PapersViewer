<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entity.Paper"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search papers</title>
<%@ include file="../resources/blueprint_include_header.jspf"%>
<%@ include file="../resources/character_encoding_in_UTF-8.jspf"%>
<%@ include file="../resources/base_URL.jspf"%>
</head>
<body>


	Find papers:
	<br>

	<table>
		<c:forEach var="paper" items="${find_papers}">
			<tr>
				<td><a href="http://localhost:8071/${paper.getId()}.pdf"
					target="pdfs"> ${paper.getId()} </a></td>


				<td><%=((Paper) pageContext.getAttribute("paper"))
						.getProporties().get("search_word_sentence").replaceAll(
								request.getParameter("search"),
								"<font style=\"color:red; "
										+ "text-decoration: underline; "
										+ "text-decoration-style:dotted; "
										+ "text-decoration-color: red;\" >"
										+ request.getParameter("search")
										+ "</font>"
									 )%></td>
			</tr>
		</c:forEach>

	</table>






	<%
		request.getSession().removeAttribute("find_papers");
	%>

	<hr>
	<form method="get" action="<%=baseURL%>search.do">
		<input type="text" name="search"><input type="submit"
			value="search">
	</form>
</body>
</html>