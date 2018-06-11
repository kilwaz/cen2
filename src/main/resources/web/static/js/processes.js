var processSourceApp = angular.module('encodedProgressApp', []);

processSourceApp.controller('encodedProgressCtrl', function ($scope, $http, $filter) {
    getClips($scope);

    $scope.sourceInfo = function (sourceRef) {
        var requestData = $.param({
            json: JSON.stringify({
                uuid: sourceRef
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

    $scope.updateEncodedProgress = function (clip) {
        var requestData = $.param({
            json: JSON.stringify({
                uuid: clip.uuid
            })
        });

        var config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        };

        $http.post("encodedProgressJSON", requestData, config)
            .then(
                function (response) {
                    var selectedSource = $filter('filter')($scope.clips, {'uuid': clip.uuid});
                    if (selectedSource.length === 1) {
                        selectedSource[0].encodedProgress = response.data;
                    }
                },
                function (response) {
                    // failure callback
                }
            );
    };

    $scope.calcPercentEncoded = function (clip, pass) {
        if (pass === 1) {
            return ((clip.encodedProgress.pass1Progress / clip.encodedProgress.totalFrames) * 100);
        } else if (pass === 2) {
            return ((clip.encodedProgress.pass2Progress / clip.encodedProgress.totalFrames) * 100);
        }
    };

    $scope.calcResolution = function (source) {
        return source.sourceInfo.streams[0].width + " x " + source.sourceInfo.streams[0].height;
    };

    $scope.calcDuration = function (time) {
        var timeHour = Math.floor(time / 3600);
        var timeMin = Math.floor((time / 60) % 60);
        var timeSec = Math.floor(time % 60);

        return timeHour + ":" + timeMin + ":" + timeSec
    };
});