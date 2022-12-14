<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.language != null}">
    <fmt:setLocale value="${sessionScope.language}"/>
</c:if>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <title>Book</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Tapestry&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>
<jsp:include page="navbar.jsp"/>
<h2>Book</h2>
<div class="main">
        <img src="${book.image}" alt="${book.title}" width="220px">
    <table>
        <tr>
            <th><fmt:message key="title.field"/></th>
            <th><fmt:message key="title.value"/></th>
        </tr>
        <tr>
            <td><fmt:message key="title.isbn"/></td>
            <td>${book.isbn}</td>
        </tr>
        <tr>
            <td><fmt:message key="title.author"/></td>
            <td>${book.author}</td>
        </tr>
        <tr>
            <td><fmt:message key="title.title"/></td>
            <td>${book.title}</td>
        </tr>
        <tr>
            <td><fmt:message key="title.cover"/></td>
            <td>${book.cover.name}</td>
        </tr>
        <tr>
            <td><fmt:message key="title.pages"/></td>
            <td>${book.pages}</td>
        </tr>
        <tr>
            <td><fmt:message key="title.price"/></td>
            <td>$${book.price}</td>
        </tr>
    </table>
    <c:if test="${userGlobal.role=='ADMIN'}">
        <form action="controller" method="get">
            <input type="hidden" name="command" value="edit_book_form">
            <input type="hidden" name="id" value="${book.id}">
            <input type="submit" value="<fmt:message key="btn.edit"/>">
        </form>
    </c:if>
    <c:if test="${message != null}">${message}</c:if>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
