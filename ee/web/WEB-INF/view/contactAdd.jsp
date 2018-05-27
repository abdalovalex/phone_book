<%--
  Created by IntelliJ IDEA.
  User: Batman
  Date: 25.05.2018
  Time: 23:08
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="_header.jsp"/>
<div class="starter-template">
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="panel-title">Добаление контакта</span>
        </div>
        <div class="panel-body">
            <form action="/contact-add" method="post" class="">
                <div class="col-md-6 form-horizontal">
                    <div class="form-group <c:if test="${not empty name_error}">has-error</c:if>">
                        <label for="name" class="col-sm-2 control-label">ФИО</label>
                        <div class="col-md-10">
                            <input type="text" class="form-control" id="name" name="name" value="${param.name}">
                        </div>
                        <c:if test="${not empty name_error}">
                            <div class="error-message col-md-offset-2 col-md-10" style="margin-bottom: 5px;">
                                ${name_error}
                            </div>
                            <c:remove var="name_error" scope="session" />
                        </c:if>
                    </div>

                    <c:choose>
                        <c:when test="${empty phone}">
                            <div class="form-group" id="phone_list">
                                <label class="col-md-2 control-label">Телефон</label>
                                <div class="col-md-10" style="margin-bottom: 15px;">
                                    <div class="input-group">
                                        <input type="text" class="form-control" name="Phone[]" value="">
                                        <span class="input-group-btn">
                                        <button class="btn btn-default add_phone" type="button">+</button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${phone}" var="value" varStatus="loop">
                                <div class="form-group <c:if test="${not empty phone_errors[loop.index]}">has-error</c:if>" id="phone_list">
                                    <label class="col-md-2 control-label">Телефон</label>
                                    <div class="col-md-10" style="margin-bottom: 15px;">
                                        <div class="input-group">
                                            <input type="text" class="form-control" name="Phone[]" value="${value}">
                                            <span class="input-group-btn">
                                                <button class="btn btn-default add_phone" type="button">+</button>
                                            </span>
                                        </div>
                                    </div>
                                    <c:if test="${not empty phone_errors[loop.index]}">
                                        <div class="error-message col-md-offset-2 col-md-10" style="margin-top: -15px; margin-bottom: 5px;">
                                            ${phone_errors[loop.index]}
                                        </div>
                                    </c:if>
                                </div>
                            </c:forEach>
                            <c:remove var="phone" scope="session" />
                            <c:remove var="phone_errors" scope="session" />
                        </c:otherwise>
                    </c:choose>

                    <div class="form-group" style="margin-top: -15px;">
                        <label for="address" class="col-sm-2 control-label">Адрес</label>
                        <div class="col-md-10">
                            <input type="text" class="form-control" id="address" name="address" value="${param.address}">
                        </div>
                    </div>

                    <input type="submit" class="btn btn-success" value="Сохранить">
                </div>
                <div class="col-md-6" id="social_link_list">
                    <div class="col-md-6" style="margin-top: 6px ;margin-bottom: 17px;">
                        <label class="control-label">Ссылки на соц.сети</label>
                    </div>
                    <c:choose>
                        <c:when test="${empty socialLink}">
                            <div class="col-md-12" style="margin-bottom: 15px;">
                                <div class="input-group">
                                    <input type="text" class="form-control" name="SocialLink[]" value="">
                                    <span class="input-group-btn">
                                        <button class="btn btn-default social_link_list" type="button">+</button>
                                    </span>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${socialLink}" var="value" varStatus="loop">
                                <div class="col-md-12" style="margin-bottom: 15px;">
                                    <div class="input-group">
                                        <input type="text" class="form-control" name="SocialLink[]" value="${value}">
                                        <span class="input-group-btn">
                                        <button class="btn btn-default social_link_list" type="button">+</button>
                                    </span>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:remove var="socialLink" scope="session" />
                        </c:otherwise>
                    </c:choose>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    $(document).ready(function ()
    {
        var body = $('body');
        body.on('click', '.add_phone', function ()
        {
            var html = '<label class="col-md-2 control-label">Телефон</label>'+
                       '<div class="col-md-10" style="margin-bottom: 15px;">'+
                            '<div class="input-group">'+
                                '<input type="text" class="form-control" name="Phone[]" value="">'+
                                '<span class="input-group-btn">'+
                                    '<button class="btn btn-default add_phone" type="button">+</button>'+
                                '</span>'+
                            '</div>'+
                        '</div>';
            $('#phone_list').append(html);
        });

        body.on('click', '.social_link_list', function ()
        {
            var html = ' <div class="col-md-12" style="margin-bottom: 15px;">'+
                            '<div class="input-group">'+
                                '<input type="text" class="form-control" name="SocialLink[]" value="">'+
                                '<span class="input-group-btn">'+
                                    '<button class="btn btn-default social_link_list" type="button">+</button>'+
                                '</span>'+
                            '</div>'+
                        '</div>';
            $('#social_link_list').append(html);
        });
    });
</script>
<jsp:include page="_footer.jsp"/>