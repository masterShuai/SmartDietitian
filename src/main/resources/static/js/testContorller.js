
    var mainApp = angular.module("test", []);

    mainApp.controller('PhoneListCtrl', function($scope, $http) {

        $scope.urls = {
            all: "/RES/Nutrition/all"
            , ById: "/RES/Nutrition/byId"
            , ByName: "/RES/Nutrition/byName"
        };

        $scope.phones = [
            {
                "name": "Nexus S",
                "snippet": "Fast just got faster with Nexus S."
            },
            {
                "name": "Motorola XOOM™ with Wi-Fi",
                "snippet": "The Next, Next Generation tablet."
            },
            {
                "name": "MOTOROLA XOOM™",
                "snippet": "The Next, Next Generation tablet."
            },
            {
                "name": "hh",
                "snippet": "hahahahahahahahah"
            }
        ];

        $scope.item = {
            "nutritionId": 0,
            "nutritionName": "未找到结果"
        };

        $scope.content = [
            {
                "nutritionId": 0,
                "nutritionName": "未找到结果"
            }
        ]

        $http.post($scope.urls.all)
            .success(function(response) {$scope.content = response.content});
    });

