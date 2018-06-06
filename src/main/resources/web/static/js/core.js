var ServerRequestService = angular.module('ServerRequestService', [])
    .service('ServerRequest', function ($http) {
        this.serverRequest = function (json, url, success, failure) {
            var requestData = $.param({
                json: json
            });

            var config = {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                }
            };

            if (success === undefined) {
                success = function (response) {
                };
            }

            if (failure === undefined) {
                failure = function (response) {
                };
            }

            $http.post(url, requestData, config)
                .then(
                    success,
                    failure
                );
        };
    });

var sourceVideoApp = angular.module('sourceVideoApp', ['ServerRequestService']);
var resetDatabaseApp = angular.module('resetDatabaseApp', ['ServerRequestService']);

var frameRate = 23.98;

resetDatabaseApp.controller('resetDatabaseCtrl', function ($scope, $http, ServerRequest) {
    $scope.sendPost = function () {
        ServerRequest.serverRequest("", "rdb");
    }
});

sourceVideoApp.controller('sourceVideoCtrl', function ($scope, $http, $filter, ServerRequest) {
    $scope.currentTime = 0;
    $scope.timeHourTens = 0;
    $scope.timeHourOnes = 0;
    $scope.timeMinTens = 0;
    $scope.timeMinOnes = 0;
    $scope.timeSecTens = 0;
    $scope.timeSecOnes = 0;
    $scope.timeFrameTens = 0;
    $scope.timeFrameOnes = 0;
    $scope.playBackSpeed = 1;
    $scope.selectedSource = undefined;

    getSources($scope);

    $scope.setSourceTime = function () {
        document.getElementsByTagName("video")[0].currentTime = $scope.convertToSeconds();
    };

    $scope.sourceInfo = function (sourceUuid) {
        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: sourceUuid
            }),
            "sourceInfoJSON",
            function (response) {
                var selectedSource = $filter('filter')($scope.sources, {'uuid': sourceUuid});
                if (selectedSource.length === 1) {
                    selectedSource[0].sourceInfo = response.data;
                }
            });
    };

    $scope.setClipStart = function (clip) {
        ServerRequest.serverRequest(
            JSON.stringify({
                clipUuid: clip.uuid,
                startMarkUuid: clip.startMark.uuid
            }),
            "updateClip");
    };

    $scope.setClipEnd = function (clip) {
        ServerRequest.serverRequest(
            JSON.stringify({
                clipUuid: clip.uuid,
                endMarkUuid: clip.endMark.uuid
            }),
            "updateClip");
    };

    $scope.sourceMark = function (sourceUuid) {
        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: sourceUuid
            }),
            "marksJSON",
            function (response) {
                var selectedSource = $filter('filter')($scope.sources, {'uuid': sourceUuid});
                if (selectedSource.length === 1) {
                    selectedSource[0].marks = response.data;
                }
            });
    };

    $scope.sourceClip = function (sourceUuid) {
        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: sourceUuid
            }),
            "clipsJSON",
            function (response) {
                var selectedSource = $filter('filter')($scope.sources, {'uuid': sourceUuid});
                if (selectedSource.length === 1) {
                    selectedSource[0].clips = response.data;

                    angular.forEach(selectedSource[0].clips, function (clip) {
                        var startMarkUuid = clip.startMark.uuid;
                        var endMarkUuid = clip.endMark.uuid;

                        var startMark = $filter('filter')(selectedSource[0].marks, {'uuid': startMarkUuid});
                        if (startMark !== undefined && startMark.length === 1) {
                            clip.startMark = startMark[0];
                        }

                        var endMark = $filter('filter')(selectedSource[0].marks, {'uuid': endMarkUuid});
                        if (endMark !== undefined && endMark.length === 1) {
                            clip.endMark = endMark[0];
                        }
                    });
                }
            });
    };

    $scope.splitSource = function () {
        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: $scope.selectedSource.uuid,
                startTime: 0.0,
                endTime: 10.0
            }),
            "splitSource");
    };

    $scope.convertToSeconds = function () {
        return $scope.timeSecOnes
            + ($scope.timeSecTens * 10)
            + ($scope.timeMinOnes * 60)
            + ($scope.timeMinTens * 600)
            + ($scope.timeHourOnes * 3600)
            + ($scope.timeHourTens * 36000)
            + ((($scope.timeFrameTens * 10) + $scope.timeFrameOnes) / frameRate);
    };

    $scope.createMark = function () {
        var newMark = {
            time: 0,
            uuid: ""
        };

        newMark.time = $scope.convertToSeconds();
        $scope.selectedSource.marks.push(newMark);

        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: $scope.selectedSource.uuid,
                time: newMark.time
            }),
            "createMark",
            function (response) {
                var responseData = response.data;
                if (responseData.length === 1) {
                    newMark.uuid = responseData[0].uuid;
                }
            });
    };

    $scope.lockInClip = function (clip) {
        clip.lockedIn = true;

        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: clip.uuid
            }),
            "lockInClip");
    };

    $scope.createClip = function () {
        var newClip = {
            uuid: ""
        };

        $scope.selectedSource.clips.push(newClip);

        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: $scope.selectedSource.uuid
            }),
            "createClip",
            function (response) {
                var responseData = response.data;
                if (responseData.length === 1) {
                    newClip.uuid = responseData[0].uuid;
                }
            });
    };

    $scope.deleteClip = function (clip) {
        var index = $scope.selectedSource.clips.indexOf(clip);
        $scope.selectedSource.clips.splice(index, 1);


        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: clip.uuid
            }),
            "removeClip");
    };

    $scope.toTimeString = function (time) {
        var timeHour = Math.floor(time / 3600);
        var timeHourTens = Math.floor(timeHour / 10 % 10);
        var timeHourOnes = Math.floor(timeHour % 10);
        var timeMin = Math.floor((time / 60) % 60);
        var timeMinTens = Math.floor(timeMin / 10 % 10);
        var timeMinOnes = Math.floor(timeMin % 10);
        var timeSec = Math.floor(time % 60);
        var timeSecTens = Math.floor(timeSec / 10 % 10);
        var timeSecOnes = Math.floor(timeSec % 10);
        var timeFrame = Math.floor((time % 1) * frameRate);
        var timeFrameTens = Math.floor(timeFrame / 10 % 10);
        var timeFrameOnes = Math.floor(timeFrame % 10);

        return timeHourTens + timeHourOnes + "h" + timeMinTens + timeMinOnes + "m" + timeSecTens + timeSecOnes + "s" + timeFrameTens + timeFrameOnes + "f";
    };

    $scope.deleteMark = function (mark) {
        var index = $scope.selectedSource.marks.indexOf(mark);
        $scope.selectedSource.marks.splice(index, 1);

        ServerRequest.serverRequest(
            JSON.stringify({
                uuid: mark.uuid
            }),
            "removeMark");
    };

    $scope.jumpToMark = function (mark) {
        $scope.currentTime = mark.time;
        document.getElementsByTagName("video")[0].currentTime = $scope.currentTime;
    };

    $scope.setSourceSpeed = function () {
        document.getElementsByTagName("video")[0].playbackRate = $scope.playBackSpeed;
    };
});

sourceVideoApp.directive('sourceChange', function ($filter) {
    return {
        link: function link($scope, element) {
            element.bind('change', function () {
                var selectedSource = element[0].selectedOptions[0].dataset;
                $scope.selectedSource = $filter('filter')($scope.sources, {'uuid': selectedSource.uuid})[0];
                $scope.sourceInfo($scope.selectedSource.uuid);
                $scope.sourceMark($scope.selectedSource.uuid);
                $scope.sourceClip($scope.selectedSource.uuid);
            });
        }
    }
});

sourceVideoApp.directive('sourceVideo', function () {
    return {
        scope: {
            videoCurrentTime: "=videoCurrentTime",
            timeFrameTens: "=timeFrameTens",
            timeFrameOnes: "=timeFrameOnes",
            timeHourTens: "=timeHourTens",
            timeHourOnes: "=timeHourOnes",
            timeMinTens: "=timeMinTens",
            timeMinOnes: "=timeMinOnes",
            timeSecTens: "=timeSecTens",
            timeSecOnes: "=timeSecOnes"
        },
        controller: function ($scope, $element) {
            $scope.onTimeUpdate = function () {
                $scope.$apply(function () {
                    var time = $element[0].currentTime;
                    $scope.videoCurrentTime = time;

                    var timeHour = Math.floor(time / 3600);
                    $scope.timeHourTens = Math.floor(timeHour / 10 % 10);
                    $scope.timeHourOnes = Math.floor(timeHour % 10);
                    var timeMin = Math.floor((time / 60) % 60);
                    $scope.timeMinTens = Math.floor(timeMin / 10 % 10);
                    $scope.timeMinOnes = Math.floor(timeMin % 10);
                    var timeSec = Math.floor(time % 60);
                    $scope.timeSecTens = Math.floor(timeSec / 10 % 10);
                    $scope.timeSecOnes = Math.floor(timeSec % 10);
                    var timeFrame = Math.floor((time % 1) * frameRate);
                    $scope.timeFrameTens = Math.floor(timeFrame / 10 % 10);
                    $scope.timeFrameOnes = Math.floor(timeFrame % 10);
                });
            }
        },
        link: function (scope, elm) {
            // Use this $watch to restart the video if it has ended
            scope.$watch('videoCurrentTime', function (newVal) {

                if (elm[0].ended) {
                    // Do a second check because the last 'timeupdate'
                    // after the video stops causes a hiccup.
                    if (elm[0].currentTime !== newVal) {
                        elm[0].currentTime = newVal;
                        elm[0].play();
                    }
                }
            });
            // Otherwise keep any model syncing here.
            elm.bind('timeupdate', scope.onTimeUpdate);
        }
    }
});