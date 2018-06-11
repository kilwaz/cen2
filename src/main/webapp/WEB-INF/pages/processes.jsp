<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="cen" tagdir="/WEB-INF/tags" %>

<jsp:include page="../includes/header.jsp"/>

<%--JSON DATA--%>
<cen:dataSource classPath="data.model.dao.ClipDAO" jsonDataSource="allEncodingClips" functionName="getClips"
                ngVar="clips"/>

<script src="https://resources.kilwaz.me/js/processes.min.js"></script>

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content">
        <jsp:include page="../includes/topbar.jsp"/>

        <div ng-app="encodedProgressApp">
            <div ng-controller="encodedProgressCtrl">
                <div ng-repeat="clip in clips" class="card">
                    <div class="card-header">
                        Clip for - {{clip.source.name}}
                    </div>
                    <div class="card-body">
                        <div class="grid">
                            <div class="row">
                                <p class="card-text">URL: {{clip.source.url}}</p>
                            </div>
                            <div class="row">
                                {{calcDuration(clip.source.sourceInfo.format.duration)}}
                                {{clip.source.sourceInfo.format.format_long_name}}
                                {{calcResolution(clip.source)}}
                                {{clip.source.sourceInfo.streams[0].display_aspect_ratio}}
                            </div>
                            <div class="row">
                                {{calcDuration(clip.startMark.time)}}
                                {{calcDuration(clip.endMark.time)}}
                            </div>
                            <div class="row">
                                <div>
                                    <button type="button" class="btn btn-primary"
                                            ng-click="updateEncodedProgress(clip)">Refresh Progress
                                    </button>
                                </div>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar"
                                     ng-style="{'width' : calcPercentEncoded(clip, 1) + '%'}" aria-valuemin="0"
                                     aria-valuemax="100">
                                    {{calcPercentEncoded(clip, 1) | number:2}}%
                                </div>
                            </div>
                            <div class="progress">
                                <div class="progress-bar" role="progressbar"
                                     ng-style="{'width' : calcPercentEncoded(clip, 2) + '%'}" aria-valuemin="0"
                                     aria-valuemax="100">
                                    {{calcPercentEncoded(clip, 2) | number:2}}%
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