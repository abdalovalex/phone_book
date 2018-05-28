<%--
  Created by IntelliJ IDEA.
  User: Batman
  Date: 23.05.2018
  Time: 22:04
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="/public/css/style.css" rel="stylesheet">
    <link href="/public/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="/public/js/jquery-3.3.1.min.js"></script>
</head>
<body>
<div class="container">
<c:if test="${not empty error_db}" scope="session" var="result">
    <div class="alert alert-danger">${error_db}</div>
    <c:remove var="error_db" scope="session" />
</c:if>
