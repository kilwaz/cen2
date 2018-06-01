<jsp:include page="../includes/header.jsp"/>

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content">
        <jsp:include page="../includes/topbar.jsp"/>

        <div class="container-fluid">
            <div ng-app>
                <p>Name: <input type="text" ng-model="name"></p>
                <p>You wrote: {{ name || 'LOADING...' }}</p>
            </div>

            <div class="line"></div>

            <h2>Collapsible Sidebar Using Bootstrap 3</h2>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>