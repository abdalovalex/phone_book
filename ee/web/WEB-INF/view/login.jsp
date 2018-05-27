<%--
  Created by IntelliJ IDEA.
  User: Batman
  Date: 23.05.2018
  Time: 22:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="_header.jsp"/>
<form action="login" class="form-signin" method="post">
    <h2 class="form-signin-heading">Введите учетные даныне</h2>
    <p style="color: red;">${error}</p>
    <input type="text" class="form-control" placeholder="Логин" name="login" required autofocus>
    <input type="password" class="form-control" placeholder="Пароль" name="password" required>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Вход</button>
</form>
<jsp:include page="_footer.jsp"/>
