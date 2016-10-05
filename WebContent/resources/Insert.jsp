<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.Paper"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert papers</title>
<%@ include file="../resources/blueprint_include_header.jspf"%>
<%@ include file="../resources/character_encoding_in_UTF-8.jspf"%>
<%@ include file="../resources/base_URL.jspf"%>
</head>
<body>


	<form action="change.do?id=${paper_id}" method="post">
		<c:forEach var="tabl" items="${tablSet.keySet()}">
			<table>
				<c:forEach var="entr" items="${tablSet.get(tabl)}" varStatus="item">
					<thead>
						<tr>
							<th colspan="3">${tabl}</th>
							<td><input type="hidden" name="${tabl}/count"
								value="${tablSet.get(tabl).size()}"></td>
						</tr>
					</thead>
					<c:forEach var="sett" items="${entr.keySet()}">
						<c:forEach var="col" items="${entr.get(sett)}">
							<tr>
								<td>${sett}</td>
								<td>${col}</td>

								<td><input type="text"
									name="${tabl}/${item.count}/${sett}/_ch" size="25" value=""></td>

								<td><input type="hidden"
									name="${tabl}/${item.count}/${sett}" value="${col}"></td>

							</tr>
						</c:forEach>
					</c:forEach>
				</c:forEach>
			</table>
			<br>
		</c:forEach>
		<input type="submit" value="Submit">
	</form>


</body>
</html>