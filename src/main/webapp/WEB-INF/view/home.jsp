<%--
  Created by IntelliJ IDEA.
  User: Batman
  Date: 23.05.2018
  Time: 23:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="_header.jsp"/>
<ol class="breadcrumb">
    <li class="active"><a href="/home">Главная</a></li>
</ol>
<div class="row">
    <div class="col-md-12">
        <h4>
        Привет
        <jsp:useBean id="User" class="Bean.User" scope="session" />
        <jsp:getProperty name="User" property="name"/>
        <a href="/logout" class="pull-right">Выйти</a>
        </h4>
    </div>
</div>
<c:if test="${not empty error_contacts_find}" scope="session" var="result">
    <div class="alert alert-danger">${error_contacts_find}</div>
    <c:remove var="error_contacts_find" scope="session" />
</c:if>
<c:if test="${not empty error_not_find_contact}" scope="session" var="result">
    <div class="alert alert-danger">${error_not_find_contact}</div>
    <c:remove var="error_not_find_contact" scope="session" />
</c:if>
<c:if test="${not empty error_delete}" scope="session" var="result">
    <div class="alert alert-danger">${error_delete}</div>
    <c:remove var="error_delete" scope="session" />
</c:if>
<div class="starter-template">
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="panel-title">Телефонная книга</span>
            <a href="/contact-add" class="btn bg-primary btn-xs pull-right">Добавить контакт</a>
        </div>
        <div class="panel-body">
            <table class="table table-bordered">
                <tr>
                    <th>Контакт</th>
                    <th>Телефон</th>
                    <th>Адрес</th>
                    <th>Соц. сети</th>
                    <th>Ред.</th>
                    <th>Уд.</th>
                </tr>
                <c:forEach items="${contactList}" var="contact">
                    <tr>
                        <td>${contact.getName()}</td>
                        <td>
                            <c:forEach items="${contact.getPhone()}" var="phone">
                                <a href="tel:${phone.getPhone()}"><span class="phone">${phone.getPhone()}</span></a>
                            </c:forEach>
                        </td>
                        <td>${contact.getAddress()}</td>
                        <td>
                            <c:forEach items="${contact.getLink()}" var="link">
                                ${link.getLink()}
                            </c:forEach>
                        </td>
                        <td><a href="/contact-update?id=${contact.getId()}">Редактировать</a></td>
                        <td><a href="/contact-delete?id=${contact.getId()}">Удалить</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
<script>
    $(document).ready(function()
    {
        $('.phone').text(function (i, text) {

            text = text.replace(/(\d{1})(\d{3})(\d{3})(\d{2})(\d{2})/, "+$1 ($2) $3-$4-$5");
            console.log(text);
            return text;
        });
    });
</script>
<jsp:include page="_footer.jsp"/>