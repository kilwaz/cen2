<jsp:include page="../includes/header.jsp"/>

<script src="https://resources.kilwaz.me/js/core.min.js"></script>

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content">
        <jsp:include page="../includes/topbar.jsp"/>

        <div class="container-fluid">
            <form id="theForm" method="post" action="import" enctype='multipart/form-data'>
                <div class="form-group">
                    <label for="exampleFormControlFile1">Example file input</label>
                    <input name="importFile" type="file" class="form-control-file" id="exampleFormControlFile1">
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>


            <div ng-app="resetDatabaseApp" ng-controller="resetDatabaseCtrl">
                <button type="button" class="btn btn-primary" ng-click="sendPost()">Reset Database</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>