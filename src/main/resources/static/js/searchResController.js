var app = angular.module('smartDietitian', []);

//控制器配置信息
app.controller('SearchResController', ['$scope', '$http', SearchResController]);

function SearchResController($scope, $http) {
    $scope.loadPaper = function() {
        $scope.userChoose = $("#searchContent").val();
        console.dir($scope.searchName);
        alert($scope.searchName);
        return;
    }
}

