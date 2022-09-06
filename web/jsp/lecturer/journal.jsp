<%@ page pageEncoding="UTF-8" %>
<%@ include file="/jspf/directive/page.jspf" %>
<%@ include file="/jspf/directive/taglib.jspf" %>
<c:set var="language" value="${not empty param.language ? param.language :
                        not empty language ? language : pageContext.request.locale}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>

<html lang="${language}">
<c:set var="title" value="Journal"/>
<%@include file="/jspf/head.jspf" %>
<head>
    <link rel="stylesheet" href="stylesheet/style.css">
</head>
<body>
<%@ include file="/jspf/header.jspf" %>
<form action="controller" class="programming_elective_form">
    <input type="hidden" name="command" value="journal">
    <table id="search_table" class="sport_elective_table">
        <h2><fmt:message key="list_menu_jsp.hello"/>, ${sessionScope.user.login}</h2>
        <%-- CONTENT --%>
        <c:choose>
            <c:when test="${fn:length(userOrderBeanList) == 0 &&  empty electives}"><h3>No such orders</h3></c:when>
            <c:otherwise>
                <thead>
                <tr>
                    <td>№</td>
                    <td><fmt:message key="list_electives_jsp.table.header.student"/></td>
                    <td><fmt:message key="sport_elective_jsp.name"/></td>
                    <td><fmt:message key="sport_elective_jsp.topic"/></td>
                    <td><fmt:message key="list_electives_jsp.table.header.status"/></td>
                    <td><fmt:message key="sport_elective_jsp.lecturer"/></td>
                    <td>Оценка</td>
                </tr>
                </thead>

                <c:set var="k" value="0"/>
                <c:forEach var="item" items="${userOrderBeanList}">
                    <c:set var="k" value="${k+1}"/>
                    <c:if test="${k >= 2}">
                        <style>
                            .sport_elective_table {
                                height: 50%;
                            }
                        </style>
                    </c:if>
                    <tr>
                        <td><c:out value="${k}"/></td>
                        <td>${item.userLogin}</td>
                        <td>${item.name}</td>
                        <td><c:if test="${item.topicId == 0}">Sport</c:if>
                            <c:if test="${item.topicId == 1}">Programming</c:if>
                        </td>
                        <td>${item.status}</td>
                        <td>${item.lecturerLogin}
                            <c:if test="${sessionScope.user.id == item.lecturerId}">(Вы)</c:if>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${item.userMark == 0 && item.endDate <= sessionScope.date}">
                                    <label>
                                        <input type="hidden" name="userId" value="${item.userId}">
                                        <input type="hidden" name="electiveId" value="${item.electiveId}">
                                        <input name="userMark" type="number" min="2" max="12" placeholder="Mark"
                                               title="Set a mark to this student"/>
                                        <input id="user-name-label" name="userMarkSubmit" type="submit" value="Submit"/>
                                    </label>
                                </c:when>
                                <c:when test="${item.userMark == 0 && item.endDate > sessionScope.date}">
                                    <fmt:message key="journal_jsp.mark_at_the_end_of_the_elective"/>
                                </c:when>
                                <c:otherwise>${item.userMark}</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
</form>
</body>
<%@ include file="/jspf/footer.jspf" %>
</html>
