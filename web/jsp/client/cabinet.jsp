<%@ page pageEncoding="UTF-8" %>
<%@ include file="/jspf/directive/page.jspf" %>
<%@ include file="/jspf/directive/taglib.jspf" %>
<c:set var="language" value="${not empty param.language ? param.language :
                        not empty language ? language : pageContext.request.locale}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>

<html lang="${language}">
<c:set var="title" value="Home"/>
<%@include file="/jspf/head.jspf" %>
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
<%@include file="/jspf/header.jspf" %>
<h3><fmt:message key="cabinet_jsp.logged_in_as"/> <i>${sessionScope.user.login}.</i></h3>
<div style="text-align: right; height: auto; margin-top: -25px;"><a href="/controller?command=listTopics">Topic List</a>
</div>
<form action="controller" class="programming_elective_form">
    <input type="hidden" name="command" value="cabinet">
    <table id="search_table" class="sport_elective_table">
        <c:if test="${empty sessionScope.electives}">
            <style>
                .sport_elective_table {
                    display: none;
                }
            </style>
            <h3><fmt:message key="cabinet_jsp.empty_set"/></h3>
            <a class="error_back" href="/controller?command=listTopics"><b>Back to Topic list</b></a>
        </c:if>
        <thead>
        <tr>
            <td>№</td>
            <td><fmt:message key="sport_elective_jsp.name"/></td>
            <td><fmt:message key="sport_elective_jsp.start_date"/></td>
            <td><fmt:message key="sport_elective_jsp.end_date"/></td>
            <td><fmt:message key="sport_elective_jsp.status"/></td>
            <td><fmt:message key="sport_elective_jsp.topic"/></td>
            <td style="width: 150px;"><fmt:message key="sport_elective_jsp.lecturer"/></td>
            <td><fmt:message key="cabinet_jsp.mark"/></td>
        </tr>
        </thead>
        <c:set var="k" value="0"/>
        <c:forEach var="item" items="${sessionScope.electives}">
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
                <td><c:if
                        test="${sessionScope.date > item.startDate && sessionScope.date < item.endDate}">in progress</c:if>
                    <c:if test="${sessionScope.date > item.startDate && sessionScope.date > item.endDate}">ended</c:if>
                    <c:if test="${sessionScope.date < item.startDate}">expected</c:if>
                </td>
                <td><c:if test="${item.topicId == 0}">Sport</c:if>
                    <c:if test="${item.topicId == 1}">Programming</c:if>
                </td>
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
                <td>
                    <c:choose>
                        <c:when test="${item.userMark == 0}"><fmt:message key="cabinet_jsp.not_rated"/></c:when>
                        <c:otherwise>${item.userMark}</c:otherwise>
                    </c:choose>
                </td>
                <td style="width: 110px;">
                    <c:choose>
                        <c:when test="${item.status != 'ended'}">
                            <label><fmt:message key="cabinet_jsp.leave"/><input class="check-box" type="checkbox"
                                                                                name="checkItemDelete"
                                                                                onchange="changeButtonState()"
                                                                                value="${item.id}"/>
                            </label>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="cabinet_jsp.elective_completed"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
    <input id="invisible" class="programming_elective_submit" style="display: none;" type="submit"
           value='<fmt:message key="cabinet_jsp.leave"/>'>
</form>

</body>
<%@ include file="/jspf/footer.jspf" %>
</html>
