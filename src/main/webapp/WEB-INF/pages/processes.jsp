<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="cen" tagdir="/WEB-INF/tags" %>

<jsp:include page="../includes/header.jsp"/>

<%--JSON DATA--%>
<cen:dataSource classPath="data.model.dao.ProcessDAO" jsonDataSource="allCommandLineProcesses" functionName="getProcesses" ngVar="processes"/>

<script src="https://resources.kilwaz.me/js/processes.min.js"></script>

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content">
        <jsp:include page="../includes/topbar.jsp"/>

        Total active threads are ${requestScope.activeThreads}

    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>