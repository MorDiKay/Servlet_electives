<%@ page pageEncoding="UTF-8" %>
<%@ include file="/jspf/directive/page.jspf" %>
<%@ include file="/jspf/directive/taglib.jspf" %>
<c:set var="language" value="${not empty param.language ? param.language :
                        not empty language ? language : pageContext.request.locale}" scope="session"/>
<c:set var="topic" value="${not empty sessionScope.topic ? sessionScope.topic : ''}"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>

<html lang="${language}">
<c:set var="title" value="Programming Elective"/>
<%--HEAD--%>
<%@include file="/jspf/head.jspf" %>
<%--HEAD--%>
<script>
    function changeButtonState() {
        var btn = document.getElementById('invisible');
        btn.style.display = document.querySelectorAll(":checked").length ? 'block' : 'none';
    }
</script>
<head>
    <link rel="stylesheet" href="stylesheet/style.css">
</head>
<body>
<%--HEADER--%>
<%@include file="/jspf/header.jspf" %>
<%--HEADER--%>
<ul class="sport_elective_nav">
    <li><a href="/controller?command=electiveProgramming&page=1" style="margin: 0 20px 0 30px;"><fmt:message
            key="sport_elective_jsp.order"/></a></li>
    <li><a href="#" style="margin: 0 20px 0 20px;"><fmt:message key="sport_elective_jsp.sort_by"/></a>
        <ul>
            <li><a href="/controller?command=electiveProgramming&sort=name&page=1"><fmt:message
                    key="sport_elective_jsp.sort_by_name"/></a></li>
            <li><a href="/controller?command=electiveProgramming&sort=startDate&page=1"><fmt:message
                    key="sport_elective_jsp.sort_by_start_date"/></a></li>
            <li><a href="/controller?command=electiveProgramming&sort=endDate&page=1"><fmt:message
                    key="sport_elective_jsp.sort_by_end_date"/></a></li>
            <li><a href="/controller?command=electiveProgramming&sort=duration&page=1"><fmt:message
                    key="sport_elective_jsp.sort_by_duration"/></a></li>
            <li><a href="/controller?command=electiveProgramming&sort=lecturer&page=1"><fmt:message
                    key="sport_elective_jsp.sort_by_lecturer"/></a></li>
            <li><a href="/controller?command=electiveProgramming&sort=count&page=1"><fmt:message
                    key="sport_elective_jsp.sort_by_student_count"/></a></li>
        </ul>
    </li>
    <li><a href="/controller?command=electiveProgramming&page=1&expected=true" style="width: 200px;">
        <fmt:message key="sport_elective_jsp.upcoming_courses"/></a></li>
</ul>
<form action="controller" class="programming_elective_form">
    <input type="hidden" name="command" value="cabinet"/>
    <table id="search_table" class="sport_elective_table">
        <thead>
        <tr>
            <td>№</td>
            <td><fmt:message key="sport_elective_jsp.name"/></td>
            <td><fmt:message key="sport_elective_jsp.start_date"/></td>
            <td><fmt:message key="sport_elective_jsp.end_date"/></td>
            <td><fmt:message key="sport_elective_jsp.status"/></td>
            <td><fmt:message key="sport_elective_jsp.topic"/></td>
            <td><fmt:message key="sport_elective_jsp.lecturer"/></td>
            <td style="width: 120px;"><fmt:message key="sport_elective_jsp.student_count"/></td>
        </tr>
        </thead>
        <c:set var="k" value="0"/>
        <c:forEach var="item" items="${electives}">
            <c:set var="k" value="${k+1}"/>
            <c:if test="${k >= 2}">
                <style>
                    .sport_elective_table {
                        height: 50%;
                    }
                </style>
            </c:if>
            <tr>
                <td>${item.id+1}</td>
                <td style="width: 119px;">${item.name}</td>
                <td>${item.startDate}</td>
                <td>${item.endDate}</td>
                <td>
                    <c:if test="${sessionScope.date > item.startDate && sessionScope.date < item.endDate}">in progress</c:if>
                    <c:if test="${sessionScope.date > item.startDate && sessionScope.date > item.endDate}">ended</c:if>
                    <c:if test="${sessionScope.date < item.startDate}">expected</c:if>
                </td>
                <td>${topic}</td>
                <td>
                    <c:choose>
                        <c:when test="${item.lecturerId != 0}">
                            ${item.lecturerLogin}
                        </c:when>
                        <c:otherwise>
                            Преподаватель еще<br>не закреплен
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${item.studentCount}</td>
                <td style="width: 110px;">
                    <c:if test="${sessionScope.date < item.startDate}">
                        <label><fmt:message key="sport_elective_jsp.sign_in"/> <input class="check-box" type="checkbox"
                                                                                      name="checkItem"
                                                                                      onchange="changeButtonState()"
                                                                                      value="${item.id}"/>
                        </label>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
    <input id="invisible" class="programming_elective_submit" type="submit"
           value='<fmt:message key="sport_elective_jsp.sign_in"/>'>
</form>
<div class="sport_elective_pagination">
    <a href="/controller?command=electiveProgramming&page=1">&lArr;</a>
    <c:if test="${empty param.sort}">
        <a href="/controller?command=electiveProgramming&page=1"><b>1</b></a>
        <a href="/controller?command=electiveProgramming&page=2"><b>2</b></a>
    </c:if>
    <c:if test="${param.sort == 'startDate'}">
        <a href="/controller?command=electiveProgramming&sort=startDate&page=1"><b>1</b></a>
        <a href="/controller?command=electiveProgramming&sort=startDate&page=2"><b>2</b></a>
    </c:if>
    <c:if test="${param.sort == 'endDate'}">
        <a href="/controller?command=electiveProgramming&sort=startDate&page=1"><b>1</b></a>
        <a href="/controller?command=electiveProgramming&sort=endDate&page=2"><b>2</b></a>
    </c:if>
    <c:if test="${param.sort == 'name'}">
        <a href="/controller?command=electiveProgramming&sort=name&page=1"><b>1</b></a>
        <a href="/controller?command=electiveProgramming&sort=name&page=2"><b>2</b></a>
    </c:if>
    <c:if test="${param.sort == 'duration'}">
        <a href="/controller?command=electiveProgramming&sort=duration&page=1"><b>1</b></a>
        <a href="/controller?command=electiveProgramming&sort=duration&page=2"><b>2</b></a>
    </c:if>
    <c:if test="${param.sort == 'count'}">
        <a href="/controller?command=electiveProgramming&sort=count&page=1"><b>1</b></a>
        <a href="/controller?command=electiveProgramming&sort=count&page=2"><b>2</b></a>
    </c:if>
    <c:if test="${param.sort == 'lecturer'}">
        <a href="/controller?command=electiveProgramming&sort=lecturer&page=1"><b>1</b></a>
        <a href="/controller?command=electiveProgramming&sort=lecturer&page=2"><b>2</b></a>
    </c:if>
    <a href="/controller?command=electiveProgramming&page=2">&rArr;</a>
</div>
</body>
<%--FOOTER--%>
<%@ include file="/jspf/footer.jspf" %>
<%--FOOTER--%>
</html>