<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="cen" tagdir="/WEB-INF/tags" %>

<jsp:include page="../includes/header.jsp"/>

<%--JSON DATA--%>
<cen:dataSource classPath="data.model.dao.SourceDAO" jsonDataSource="allRawSources" functionName="getSources"
                ngVar="sources"/>

<script src="https://resources.kilwaz.me/js/processes.min.js"></script>

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content">
        <jsp:include page="../includes/topbar.jsp"/>

        <div ng-app="processSourceApp">
            <div ng-controller="processSourceCtrl">
                <div ng-repeat="source in sources" class="card">
                    <div class="card-header">
                        {{source.name}}
                    </div>
                    <div class="card-body">
                        <div class="grid">
                            <div class="row">
                                <p class="card-text">URL: {{source.url}}</p>
                            </div>
                            <div class="row">
                                <p class="card-text">Encoded URL: {{source.encodedUrl}}</p>
                            </div>
                            <div class="row">
                                {{source.sourceInfo.format.duration | number : 2}}
                                {{source.sourceInfo.format.format_long_name}}
                                {{source.sourceInfo.streams[0].width}} x {{source.sourceInfo.streams[0].height}}
                                {{source.sourceInfo.streams[0].display_aspect_ratio}}
                                {{source.sourceInfo.streams[0].avg_frame_rate}}
                                {{source.encodedProgress.totalFrames}}
                            </div>
                            <div class="row">
                                <div ng-if="source.encodedProgress.passPhase == 0">
                                    <a href="#" class="btn btn-primary">Start pass 1</a>
                                </div>
                                <div ng-if="source.encodedProgress.passPhase == 1">
                                    <a href="#" class="btn btn-primary">Start pass 2</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>