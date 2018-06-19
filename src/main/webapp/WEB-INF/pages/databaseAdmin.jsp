<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="cen" tagdir="/WEB-INF/tags" %>

<jsp:include page="../includes/header.jsp"/>

<%--JSON DATA--%>
<cen:dataSource classPath="data.model.dao.SourceDAO" jsonDataSource="allSources" functionName="getSources"
                ngVar="sources"/>

<script src="https://resources.kilwaz.me/js/core.min.js"></script>

<style>
    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

    input[type="number"],
    select.form-control {
        background: transparent;
        border: none;
        width: 20px;
        border-bottom: 1px solid #000000;
        text-align: center;
        -webkit-box-shadow: none;
        box-shadow: none;
        border-radius: 0;
    }

    input[type="number"]:focus,
    select.form-control:focus {
        -webkit-box-shadow: none;
        box-shadow: none;
    }

    #player {
        height: 450px;
        background-color: #5a6268;
    }
</style>

<div class="wrapper">
    <jsp:include page="../includes/sidebar.jsp"/>

    <!-- Page Content Holder -->
    <div id="content" class="w-100">
        <jsp:include page="../includes/topbar.jsp"/>

        <div ng-app="sourceVideoApp">
            <div ng-controller="sourceVideoCtrl">
                <label for="sourceSelect">Source: </label>
                <select name="sourceSelect" id="sourceSelect" ng-model="currentVideo" source-change>
                    <option value="" selected="selected"></option>
                    <option ng-repeat="source in sources" data-uuid="{{source.uuid}}" value="{{source.url}}">
                        {{source.name}}
                    </option>
                </select>

                <table id="searchTextResults">
                    <tr>
                        <th>Duration</th>
                        <th>Format</th>
                        <th>Bit rate</th>
                        <th>Resolution</th>
                        <th>Aspect Ratio</th>
                        <th>FPS</th>
                    </tr>
                    <tr>
                        <td>{{selectedSource.sourceInfo.format.duration | number : 2}}</td>
                        <td>{{selectedSource.sourceInfo.format.format_long_name}}</td>
                        <td>{{selectedSource.sourceInfo.format.bit_rate}}</td>
                        <td>{{selectedSource.sourceInfo.streams[0].width}} x {{source.sourceInfo.streams[0].height}}
                        </td>
                        <td>{{selectedSource.sourceInfo.streams[0].display_aspect_ratio}}</td>
                        <td>{{selectedSource.sourceInfo.streams[0].avg_frame_rate}}</td>
                    </tr>
                </table>

                <video id="player" class="embed-responsive" controls source-video
                       video-current-time="currentTime"
                       ng-src="{{ currentVideo }}"
                       time-sec-tens="timeSecTens" time-sec-ones="timeSecOnes"
                       time-min-tens="timeMinTens" time-min-ones="timeMinOnes" time-hour-tens="timeHourTens"
                       time-hour-ones="timeHourOnes" time-frame-tens="timeFrameTens" time-frame-ones="timeFrameOnes">
                </video>

                <input type="number" min="0" max="9" ng-model="timeHourTens"/>
                <input type="number" min="0" max="9" ng-model="timeHourOnes"/>
                <span>H</span>
                <input type="number" min="0" max="5" ng-model="timeMinTens"/>
                <input type="number" min="0" max="9" ng-model="timeMinOnes"/>
                <span>M</span>
                <input type="number" min="0" max="5" ng-model="timeSecTens"/>
                <input type="number" min="0" max="9" ng-model="timeSecOnes"/>
                <span>S</span>
                <input type="number" min="0" max="9" ng-model="timeFrameTens"/>
                <input type="number" min="0" max="9" ng-model="timeFrameOnes"/>

                <button type="button" class="btn btn-primary" ng-click="setSourceTime()">Set</button>

                <input type="number" min="0" max="10" ng-change="setSourceSpeed()" ng-model="playBackSpeed"/>

                <div class="container no-gutters">
                    <div class="row no-gutters">
                        <div class="col">
                            Marks:
                            <button type="button" class="btn btn-primary" ng-click="createMark()">Mark</button>
                            <div ng-repeat="mark in selectedSource.marks" class="card">
                                <div class="card-body">
                                    {{ toTimeString(mark.time) }}
                                    <button type="button" class="btn btn-primary"
                                            ng-click="jumpToMark(mark)">Show
                                    </button>
                                    <button type="button" class="btn btn-primary"
                                            ng-click="deleteMark(mark)">Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="col">
                            Clips:
                            <button type="button" class="btn btn-primary" ng-click="createClip()">Clip
                            </button>
                            <div ng-repeat="clip in selectedSource.clips" class="card">
                                <div class="card-body">
                                    <div ng-switch on="clip.lockedIn">
                                        <div ng-switch-default>
                                            <select ng-model="clip.startMark"
                                                    ng-change="setClipStart(clip)">
                                                <option ng-repeat="mark in selectedSource.marks"
                                                        ng-value="mark">
                                                    {{ toTimeString(mark.time) }}
                                                </option>
                                            </select>
                                            -
                                            <select ng-model="clip.endMark"
                                                    ng-change="setClipEnd(clip)">
                                                <option ng-repeat="mark in selectedSource.marks"
                                                        ng-value="mark">
                                                    {{ toTimeString(mark.time) }}
                                                </option>
                                            </select>
                                        </div>
                                        <div ng-switch-when="true">
                                            {{ toTimeString(clip.startMark.time) }} - {{
                                            toTimeString(clip.endMark.time)
                                            }}
                                        </div>
                                    </div>

                                    <div ng-switch on="clip.lockedIn">
                                        <div ng-switch-default>
                                            <button type="button" class="btn btn-primary"
                                                    ng-click="lockInClip(clip)">
                                                Lock In
                                            </button>
                                        </div>
                                        <div ng-switch-when="true">
                                            <button type="button" class="btn btn-primary"
                                                    ng-click="unlockClip(clip)">
                                                Unlock
                                            </button>
                                            <button type="button" class="btn btn-primary"
                                                    ng-click="splitClip(clip)">
                                                Split
                                            </button>
                                            <button type="button" class="btn btn-primary"
                                                    ng-click="finaliseClip(clip)">
                                                Finalise
                                            </button>
                                        </div>
                                    </div>

                                    <button type="button" class="btn btn-primary"
                                            ng-click="deleteClip(clip)">
                                        Delete
                                    </button>
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