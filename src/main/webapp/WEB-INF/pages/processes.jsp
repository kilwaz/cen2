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
                                {{calcDuration(source)}}
                                {{source.sourceInfo.format.format_long_name}}
                                {{calcResolution(source)}}
                                {{source.sourceInfo.streams[0].display_aspect_ratio}}
                            </div>
                            <div class="row">
                                <div ng-if="source.encodedProgress.passPhase == 0">
                                    <button type="button" class="btn btn-primary"
                                            ng-click="encodeSource(1, source)">Start pass 1
                                    </button>
                                </div>
                                <div ng-if="source.encodedProgress.passPhase == 1">
                                    <button type="button" class="btn btn-primary"
                                            ng-click="encodeSource(2, source)">Start pass 2
                                    </button>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-primary"
                                            ng-click="updateEncodedProgress(source)">Refresh Progress
                                    </button>
                                </div>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar"
                                     ng-style="{'width' : calcPercentEncoded(source, 1) + '%'}" aria-valuemin="0" aria-valuemax="100">
                                    {{calcPercentEncoded(source, 1) | number:2}}%
                                </div>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar"
                                     ng-style="{'width' : calcPercentEncoded(source, 2) + '%'}" aria-valuemin="0" aria-valuemax="100">
                                    {{calcPercentEncoded(source, 2) | number:2}}%
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