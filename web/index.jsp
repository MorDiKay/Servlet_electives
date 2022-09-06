<%@ page pageEncoding="UTF-8" %>
<%@ include file="jspf/directive/page.jspf" %>
<%@ include file="jspf/directive/taglib.jspf" %>
<c:set var="language" value="${not empty param.language ? param.language :
                        not empty language ? language : pageContext.request.locale}" scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="resources"/>

<html lang="${language}">
<head>
    <title>Elective System</title>
    <link rel="stylesheet" href="stylesheet/style.css">
</head>
<body>
<img src="img/elective.png" alt="extracurricular activities" width="320" height="130" align="right"/>
<img src="img/elective1.png" alt="extracurricular activities" width="320" height="130" align="left"/>
<div class="welcome" style="padding-top: 1px">
    <div class="i18n" style="float: right; padding-left: 500px">
        <button style="background-color: black"><a style="color: #4CAF50; text-decoration: none"
                                                   href="${pageContext.request.contextPath}/?language=en"><b>En</b></a>
        </button>
        <button style="background-color: black"><a style="text-decoration: none;color: #4CAF50;"
                                                   href="${pageContext.request.contextPath}/?language=ru"><b>Ru</b></a>
        </button>
    </div>
    <h2 style="line-height: 0.3"><fmt:message key="login_jsp.welcome"/>
        <p style="text-indent: 200px"><fmt:message
            key="login_jsp.to"/> </p>
        <p style="text-indent: 245px"><fmt:message key="login_jsp.elective_system"/></p></h2>
</div>

<table id="main-container">
    <tr>
        <td class="content center">
            <div class="login_submit">
                <form id="login_form" action="controller" method="post">
                    <input type="hidden" name="command" value="login"/>

                    <fieldset class="Enter">
                        <legend>
                            <fmt:message key="login_jsp.label.login"/>
                        </legend>
                        <input name="login" maxlength="40"/><br/>
                    </fieldset>
                    <br/>
                    <fieldset class="Enter">
                        <legend>
                            <fmt:message key="login_jsp.label.password"/>
                        </legend>
                        <input type="password" name="password" maxlength="40"/>
                    </fieldset>
                    <br/>
                    <input class="submit_login" type="submit" value='<fmt:message key="login_jsp.button.login"/>'>
                </form>
            </div>
        </td>
    </tr>
</table>
</body>
<%@ include file="jspf/footer.jspf" %>
</html>
