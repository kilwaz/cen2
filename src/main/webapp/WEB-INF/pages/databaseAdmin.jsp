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
                    <option ng-repeat="source in sources" data-ref="{{source.uuid}}" value="{{source.url}}">
                        {{source.name}}
                    </option>
                </select>

                <button type="button" class="btn btn-primary" ng-click="probeSource()">Probe</button>
                <button type="button" class="btn btn-primary" ng-click="encodeSource()">Encode</button>

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

                <input type="button" value="Set" ng-click="setSourceTime()"/>
                <input type="button" value="Mark" ng-click="createMark()"/>

                <br/>
                <input type="number" min="0" max="1" ng-change="setSourceSpeed()" ng-model="playBackSpeed"/>

                <br/>
                <p>Marks:</p>
                <ul ng-repeat="mark in marks">
                    <li ng-repeat="time in mark">
                        {{ time }} seconds.
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp"/>