var processSourceApp = angular.module('processSourceApp', []);

console.log("Testing logging");

processSourceApp.controller('processSourceCtrl', function ($scope, $http, $filter) {
    console.log("Started");
    getSources($scope);

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

    $scope.encodeSource = function () {
        var requestData = $.param({
            json: JSON.stringify({
                ref: $scope.selectedVideoRef
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

    console.log("Completed");
});