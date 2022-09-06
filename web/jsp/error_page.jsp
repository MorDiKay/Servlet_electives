<%@ page isErrorPage="true" %>
<%@ include file="/jspf/directive/page.jspf" %>
<%@ include file="/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Error" scope="page"/>
<%--HEAD--%>
<%@ include file="/jspf/head.jspf" %>
<%--HEAD--%>
<head>
    <link rel="stylesheet" href="stylesheet/style.css">
</head>
<body>
<%--HEADER--%>
<%@ include file="/jspf/header.jspf" %>
<%--HEADER--%>
<div class="error_div">
    <h2 class="error">
        The following error occurred:<br>
        ${sessionScope.errorMessage}<br>
        <c:if test="${home == 'home'}"><a class="error_back" href="/controller?command=listTopics">Back to topic
            list</a></c:if>
        <br><a class="error_back" href="/controller?command=logout">Back to login</a>
    </h2>
</div>
<%--FOOTER--%>
<%@ include file="/jspf/footer.jspf" %>
<%--FOOTER--%>
</body>
</html>