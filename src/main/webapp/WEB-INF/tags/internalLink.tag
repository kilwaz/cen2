<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="https://kilwaz.me" prefix="cenFun" %>
<%@ attribute name="classLink" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="icon" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="value" required="true" rtexprvalue="true" type="java.lang.String" %>

<a href="${cenFun:resolveURL(classLink)}">
    <c:if test="${not empty icon}">
        <i class="${icon}"></i>
    </c:if>

    ${value}
</a>
