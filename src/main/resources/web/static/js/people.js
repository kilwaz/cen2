var peopleApp = angular.module('peopleApp', []);
var dataTable = undefined;

peopleApp.controller('peopleCtrl', function ($scope, $http) {
    $scope.createPerson = function (person) {
        var requestData = $.param({
            json: JSON.stringify({
                firstName: person.firstName,
                lastName: person.lastName
            })
        });

        var config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        };

        $http.post("createPerson", requestData, config)
            .then(
                function (response) {
                    var firstName = response.data[0].firstName;
                    var lastName = response.data[0].lastName;

                    dataTable.row.add({
                        "firstName": firstName,
                        "lastName": lastName
                    }).draw(false);
                },
                function (response) {
                    // failure callback
                }
            );
    };
});

$(document).ready(function () {
    dataTable = $('#dataTable').DataTable({
        "ajax": "https://kilwaz.me/peopleJSON",
        "columns": [
            {"data": "firstName"},
            {"data": "lastName"}
        ]
    });
});