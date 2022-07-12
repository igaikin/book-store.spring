<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.language != null}">
    <fmt:setLocale value="${sessionScope.language}"/>
</c:if>
<fmt:setBundle basename="messages"/>
<nav>
    <ul id="nav">
        <li>
            <a href="/bookstore.com"><fmt:message key="btn.home"/></a>
        </li>
        <li>
            <a href="controller?command=books"><fmt:message key="btn.catalogue"/></a>
        </li>
        <li>
            <a href="controller?command=cart"><fmt:message key="btn.cart"/></a>
        </li>
        <c:if test="${userGlobal == null}">
            <li>
                <a href="controller?command=login_page"><fmt:message key="btn.login"/></a>
            </li>
            /
            <li>
                <a href="controller?command=register_user_form"><fmt:message key="btn.register"/></a>
            </li>
        </c:if>
        <c:if test="${userGlobal != null}">
            <li>
                <a href="controller?command=profile&id=${userGlobal.id}"><fmt:message key="btn.profile"/></a>
            </li>
            <c:if test="${userGlobal.role=='ADMIN'}">
                <li>
                    <a href="controller?command=orders"> <fmt:message key="btn.orders"/> </a>
                </li>
                <li>
                    <a href="controller?command=users"> <fmt:message key="btn.users"/> </a>
                </li>
            </c:if>
            <li>
                <a href="controller?command=logout&page=${requestScope['javax.servlet.forward.request_uri']}"><fmt:message key="btn.logout"/> </a>
            </li>
        </c:if>
        <li class="language">
            <a href="controller?command=change_language">
                <img src='../images/lang/uk.png' alt="en">
            </a>
            <a href="controller?command=change_language">
                <img src='../images/lang/de.png' alt="de">
            </a>
            <a href="controller?command=change_language">
                <img src='../images/lang/ru.png' alt="ru">
            </a>
        </li>
    </ul>
</nav>

<style>
    #nav {
        margin: 0;
        padding: 0;
        list-style-type: none;
        border: 2px solid #0066FF;
        border-radius: 20px 5px;
        width: 100%;
        text-align: match-parent;
        background-color: #33ADFF;
    }

    #nav li {
        display: inline;
    }

    #nav a {
        color: #fff;
        padding: 5px 10px;
        text-decoration: none;
        font-weight: bold;
        display: inline;
        width: 100px;
    }

    #nav a:hover {
        border-radius: 20px 5px;
        background-color: #0066FF;
    }
</style>