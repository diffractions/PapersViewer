<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entity.Paper"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Paper</title>
<%@ include file="../resources/blueprint_include_header.jspf"%>
<%@ include file="../resources/character_encoding_in_UTF-8.jspf"%>
</head>
<body>
	You open paper with name "${paper.getName()}"
	<br>
	<a href="select.do?id=${paper.getId()}">Select</a>
	<a href="change.do?id=${paper.getId()}">Change</a>
	<a href="http://localhost:8071/${paper.getId()}.pdf" target="pdfs">open</a>
</body>
</html>