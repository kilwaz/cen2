var sourceVideoApp = angular.module('sourceVideoApp', []);
var resetDatabaseApp = angular.module('resetDatabaseApp', []);

var frameRate = 23.98;

resetDatabaseApp.controller('resetDatabaseCtrl', function ($scope, $http) {
    $scope.sendPost = function () {
        var requestData = $.param({
            json: JSON.stringify({
                name: "post data"
            })
        });

        var config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        };

        $http.post("rdb", requestData, config)
            .then(
                function (response) {
                    // success callback
                },
                function (response) {
                    // failure callback
                }
            );
    }
});

sourceVideoApp.controller('sourceVideoCtrl', function ($scope, $http, $filter) {
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

    $scope.marks = [];

    getSources($scope);

    $scope.setSourceTime = function () {
        document.getElementsByTagName("video")[0].currentTime = $scope.convertToSeconds();
    };

    $scope.sourceInfo = function (sourceRef) {
        var requestData = $.param({
            json: JSON.stringify({
                ref: sourceRef
            })
        });

        var config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        };

        $http.post("sourceInfoJSON", requestData, config)
            .then(
                function (response) {
                    var selectedSource = $filter('filter')($scope.sources, {'uuid': sourceRef});
                    if (selectedSource.length === 1) {
                        selectedSource[0].sourceInfo = response.data;
                    }
                },
                function (response) {
                    // failure callback
                }
            );
    };

    $scope.splitSource = function () {
        var requestData = $.param({
            json: JSON.stringify({
                uuid: $scope.selectedVideoRef,
                startTime: 0.0,
                endTime: 10.0
            })
        });

        var config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        };

        $http.post("splitSource", requestData, config)
            .then(
                function (response) {

                },
                function (response) {
                    // failure callback
                }
            );
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
            time: 0
        };

        newMark.time = $scope.convertToSeconds();

        $scope.marks.push(newMark);
    };

    $scope.setSourceSpeed = function () {
        document.getElementsByTagName("video")[0].playbackRate = $scope.playBackSpeed;
    };
});

sourceVideoApp.directive('sourceChange', function () {
    return {
        link: function link($scope, element) {
            element.bind('change', function () {
                var selectedSource = element[0].selectedOptions[0].dataset;
                $scope.selectedVideoRef = selectedSource.ref;
                $scope.sourceInfo(selectedSource.ref);
            });
        }
    }
});

sourceVideoApp.directive('sourceVideo', function ($window, $timeout) {
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