var processSourceApp = angular.module('processSourceApp', []);

console.log("Testing logging");

processSourceApp.controller('processSourceCtrl', function ($scope, $http, $filter) {
    console.log("Started");
    getSources($scope);

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

    $scope.updateEncodedProgress = function (source) {
        var requestData = $.param({
            json: JSON.stringify({
                uuid: source.uuid
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
                    var selectedSource = $filter('filter')($scope.sources, {'uuid': source.uuid});
                    if (selectedSource.length === 1) {
                        selectedSource[0].encodedProgress = response.data;
                    }
                },
                function (response) {
                    // failure callback
                }
            );
    };

    $scope.calcPercentEncoded = function (source, pass) {
        if (pass === 1) {
            return ((source.encodedProgress.pass1Progress / source.encodedProgress.totalFrames) * 100);
        } else if (pass === 2) {
            return ((source.encodedProgress.pass2Progress / source.encodedProgress.totalFrames) * 100);
        }
    };

    $scope.calcResolution = function (source) {
        return source.sourceInfo.streams[0].width + " x " + source.sourceInfo.streams[0].height;
    };

    $scope.calcDuration = function (source) {
        var time = source.sourceInfo.format.duration;
        var timeHour = Math.floor(time / 3600);
        var timeMin = Math.floor((time / 60) % 60);
        var timeSec = Math.floor(time % 60);

        return timeHour + ":" + timeMin + ":" + timeSec
    };

    $scope.encodeSource = function (pass, source) {
        var requestData = $.param({
            json: JSON.stringify({
                pass: pass,
                uuid: source.uuid
            })
        });

        var config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        };

        $http.post("encodeSource", requestData, config)
            .then(
                function (response) {
                    // success callback
                },
                function (response) {
                    // failure callback
                }
            );
    };
});