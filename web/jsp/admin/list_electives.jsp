<%@ page pageEncoding="UTF-8" %>
<%@ include file="/jspf/directive/page.jspf" %>
<%@ include file="/jspf/directive/taglib.jspf" %>
<c:set var="language" value="${not empty param.language ? param.language :
                        not empty language ? language : pageContext.request.locale}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>
<head>
    <title>Admin page</title>
    <link rel="stylesheet" href="stylesheet/style.css">
</head>
<html lang="${language}">
<c:set var="title" value="Elective List"/>
<%@include file="/jspf/head.jspf" %>
<script>
    //JS function that dynamically displays button when checkbox is checked
    function changeButtonCheck() {
        var button = document.body.getElementsByClassName('programming_elective_submit');
        var btn = null;
        for (var i = 0; i < button.length; i++) {
            if (button[i].getAttribute('id') === 'invisible')
                /*btn = document.body.getElementsByClassName('programming_elective_submit')[i];*/
                btn = document.getElementById('invisible');
            if (button[i].getAttribute('id') === 'invisible1')
                btn = document.getElementById('invisible1');
            if (button[i].getAttribute('id') === 'invisible2')
                btn = document.getElementById('invisible2');
        }
        btn.style.display = document.querySelectorAll(":checked").length ? 'block' : 'none';
    }
</script>
<body>
<%@ include file="/jspf/header.jspf" %>
<form action="controller" class="programming_elective_form">
    <input type="hidden" name="command" value="listElectives">
    <table id="search_table1" class="sport_elective_table">

        <%-- Table to display UserOrderBean entities --%>

        <h2><fmt:message key="list_menu_jsp.hello"/>, ${sessionScope.user.login}</h2>

        <fmt:message key="list_electives_jsp.button.view_all_orders" var="buttonValue"/>
        <form action="controller">
            <input type="submit" value="${buttonValue}" class="admin_view">
        </form>
        <form action="controller">
            <input type="hidden" name="command" value="listElectives">
            <input type="hidden" name="viewSportElectives" value="true">
            <input type="submit" value='<fmt:message key="list_electives_jsp.button.view_sport_electives"/>'
                   class="admin_view">
        </form>
        <form action="controller">
            <input type="hidden" name="command" value="listElectives">
            <input type="hidden" name="viewProgrammingElectives" value="true">
            <input type="submit" value='<fmt:message key="list_electives_jsp.button.view_programming_electives"/>'
                   class="admin_view">
        </form>
        <form action="controller">
            <input type="hidden" name="command" value="listElectives">
            <input type="hidden" name="viewAllStudents" value="true">
            <input type="submit" value='<fmt:message key="list_electives_jsp.button.view_all_students"/>'
                   class="admin_view">
        </form>
        <c:choose>
            <c:when test="${fn:length(userOrderBeanList) == 0 &&  empty electives}"><h3>No such orders</h3></c:when>
            <c:otherwise>
                <c:if test="${empty electives && empty students}">
                    <thead>
                    <tr>
                        <td>№</td>
                        <td><fmt:message key="list_electives_jsp.table.header.student"/></td>
                        <td><fmt:message key="sport_elective_jsp.name"/></td>
                        <td><fmt:message key="sport_elective_jsp.topic"/></td>
                        <td><fmt:message key="list_electives_jsp.table.header.status"/></td>
                        <td style="width: 150px;"><fmt:message key="sport_elective_jsp.lecturer"/></td>
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
                            <td style="width: 119px;">${item.name}</td>
                            <td>
                                <c:if test="${item.topicId == 0}">Sport</c:if>
                                <c:if test="${item.topicId == 1}">Programming</c:if>
                            </td>
                            <td>${item.status}</td>
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
                            <td style="width: 100px;">
                                <c:if test="${item.status != 'ended'}">
                                    <label><fmt:message key="list_electives_jsp.check_delete"/><input class="check-box"
                                                                                                      type="checkbox"
                                                                                                      name="checkItemDelete"
                                                                                                      onclick="changeButtonCheck()"
                                                                                                      value="${item.electiveId}"/>
                                    </label>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </c:otherwise>
        </c:choose>
    </table>
    <input id="invisible" class="programming_elective_submit" type="submit"
           value='<fmt:message key="list_electives_jsp.check_delete"/>'/>
</form>

<%-- Table to display electives by topic --%>
<c:if test="${not empty electives}">
    <form action="controller" class="programming_elective_form" style="margin-top: -40px">
        <input type="hidden" name="command" value="listElectives">
        <c:if test="${not empty param.viewSportElectives}">
            <input type="hidden" name="viewSportElectives" value="true">
        </c:if>
        <c:if test="${not empty param.viewProgrammingElectives}">
            <input type="hidden" name="viewProgrammingElectives" value="true">
        </c:if>
        <table id="search_table" class="sport_elective_table">
            <thead>
            <tr>
                <td>№</td>
                <td><fmt:message key="sport_elective_jsp.name"/></td>
                <td><fmt:message key="sport_elective_jsp.start_date"/></td>
                <td><fmt:message key="sport_elective_jsp.end_date"/></td>
                <td><fmt:message key="sport_elective_jsp.status"/></td>
                <td><fmt:message key="sport_elective_jsp.topic"/></td>
                <td style="width: 165px;"><fmt:message key="sport_elective_jsp.lecturer"/></td>
                <td style="width: 70px;"><fmt:message key="sport_elective_jsp.student_count"/></td>
                <td> Удалить </td>
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
                    <td>${item.status}</td>
                    <td><c:if test="${item.topicId == 0}">Sport</c:if>
                        <c:if test="${item.topicId == 1}">Programming</c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.status == 'expected' && item.lecturerId == 0}">
                                <select name="lecturerList">
                                    <c:forEach var="lecturer" items="${lecturerList}">
                                        <option>${lecturer.login}</option>
                                    </c:forEach>
                                </select>
                                <input type="hidden" name="electiveId" value="${item.id}">
                                <input type="submit" name="lecturerSubmit"
                                       value='<fmt:message key="sport_elective_jsp.confirm"/>'>
                            </c:when>
                            <c:otherwise>${item.lecturerLogin}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${item.studentCount}</td>
                    <td style="width: 100px;">
                        <label><fmt:message key="list_electives_jsp.check_delete"/><input class="check-box"
                                                                                          type="checkbox"
                                                                                          name="electivesDelete"
                                                                                          onchange="changeButtonCheck()"
                                                                                          value="${item.id}"/>
                        </label>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <input id="invisible1" class="programming_elective_submit" type="submit"
               value='<fmt:message key="list_electives_jsp.check_delete"/>'/>
    </form>
</c:if>

<%-- Table to display all users --%>
<c:if test="${not empty students}">
    <form action="controller">
        <input type="hidden" name="command" value="listElectives">
        <c:if test="${not empty param.viewAllStudents}">
            <input type="hidden" name="viewAllStudents" value="true">
        </c:if>
        <table id="search_table" class="sport_elective_table">
            <thead>
            <tr>
                <td>№</td>
                <td><fmt:message key="list_electives_jsp.table.header.student"/></td>
                <td><fmt:message key="list_electives_jsp.student_blocking"/></td>
            </tr>
            </thead>
            <c:set var="k" value="0"/>
            <c:forEach var="item" items="${students}">
                <c:set var="k" value="${k+1}"/>
                <c:if test="${k >= 2}">
                    <style>
                        .sport_elective_table {
                            height: 30%;
                        }
                    </style>
                </c:if>
                <tr>
                    <td><c:out value="${k}"/></td>
                    <td>${item.login}</td>
                    <td style="width: 150px;">
                        <label>
                            <c:if test="${item.blocked == true}"><fmt:message key="list_electives_jsp.student_unblock"/></c:if>
                            <c:if test="${item.blocked == false}"><fmt:message key="list_electives_jsp.student_block"/></c:if>
                            <input class="check-box"
                                   type="checkbox"
                                   name="blockStudent"
                                   onchange="changeButtonCheck()"
                                   value="${item.login}"/>
                        </label>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <input id="invisible2" class="programming_elective_submit" type="submit"
               value='<fmt:message key="sport_elective_jsp.confirm"/>'/>
    </form>
</c:if>
</body>
<%--FOOTER--%>
<%@ include file="/jspf/footer.jspf" %>
<%--FOOTER--%>
</html>