<%@ include file="/jspf/directive/page.jspf" %>
<%@ include file="/jspf/directive/taglib.jspf" %>
<html>
<c:set var="title" value="Contact"/>
<%--HEAD--%>
<%@include file="/jspf/head.jspf" %>
<%--HEAD--%>
<head>
    <link rel="stylesheet" href="stylesheet/style.css">
</head>
<body>
<%--HEADER--%>
<%@ include file="/jspf/header.jspf" %>
<%--HEADER--%>
    <h2 id="contact">${contact}</h2>
</body>
<%--FOOTER--%>
<%@ include file="/jspf/footer.jspf" %>
<%--FOOTER--%>
</html>
