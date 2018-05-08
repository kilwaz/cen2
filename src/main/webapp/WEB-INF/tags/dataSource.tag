<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib uri="https://kilwaz.me" prefix="cenFun" %>
<%@ attribute name="classPath" required="true" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="jsonDataSource" required="true" rtexprvalue="true" type="java.lang.String" %>

<script>
    ${cenFun:jsonData(classPath,jsonDataSource)}
</script>
