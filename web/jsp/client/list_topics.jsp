<%@ page pageEncoding="UTF-8" %>
<%@ include file="/jspf/directive/page.jspf" %>
<%@ include file="/jspf/directive/taglib.jspf" %>
<c:set var="language" value="${not empty param.language ? param.language :
                        not empty language ? language : pageContext.request.locale}" scope="session"/>
<c:set var="user_login" value="${not empty sessionScope.user.login ? sessionScope.user.login : ''}"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>

<html lang="${language}">
<c:set var="title" value="Menu"/>
<%@include file="/jspf/head.jspf" %>
<head>
    <title>User page</title>
    <link rel="stylesheet" href="stylesheet/style.css">
</head>
<body>
<%@ include file="/jspf/header.jspf" %>

<h2><fmt:message key="list_menu_jsp.hello"/>, ${user_login}</h2>
<div class="listMenu_topics">
    <h2><fmt:message key="list_menu_jsp.topics"/>:</h2>

</div>
<div class="button_topic1">
    <%--   REMOOOOOOOOOOOOOOVEEEEE THE ADRESS
    =============================================== --%>
    <form action="controller">
        <input type="hidden" name="command" value="electiveSport"/>
        <input type="hidden" name="page" value="1"/>
        <button class="button_sport"><img src="img/sport.png" alt="Киберпанк спорт"
                                          width="210" height="250"/></button>
        <div class="button_program_invisible_text1">
            <fmt:message key="list_menu_tag_jsp.sport"/><br><fmt:message key="list_menu_tag1_jsp.sport"/><br>
            <fmt:message key="list_menu_tag2_jsp.sport"/>
        </div>
        <h3 class="h3_sport"><fmt:message key="list_menu_jsp.sport"/></h3>
    </form>
</div>
<div class="button_topic2">
    <form action="controller">
        <input type="hidden" name="command" value="electiveProgramming"/>
        <input type="hidden" name="page" value="1"/>
        <button class="button_program"><img src="img/prog.png" alt="Киберпанк программирование"
                                            width="200" height="260"/></button>
        <div class="button_program_invisible_text">
            <fmt:message key="list_menu_tag_jsp.programming"/><br>
            <fmt:message key="list_menu_tag1_jsp.programming"/>
        </div>
        <h3 class="h3_program"><fmt:message key="list_menu_jsp.programming"/></h3>
    </form>
</div>
</body>

<%@ include file="/jspf/footer.jspf" %>
</html>
