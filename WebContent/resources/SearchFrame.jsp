
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entity.Paper"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE  HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search papers</title>
<%@ include file="../resources/blueprint_include_header.jspf"%>
<%@ include file="../resources/character_encoding_in_UTF-8.jspf"%>
<%@ include file="../resources/base_URL.jspf"%>
</head>

<frameset cols="400,*" rows="1" cols="2">
	<frame src="Search.jsp" name="finds" scrolling="auto" >
	<frame name="pdfs" scrolling="auto" >
</frameset>

</html>