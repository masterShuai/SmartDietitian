(function() {

    var app = angular.module('sdyapp', ['ngRoute']);

    app.config(function ($routeProvider) {

        $routeProvider
            .when('/', { controller: PageController})
            ;
    });

    //控制器配置信息
    app.controller('PageController',['$scope','$http',"$location","$interval",PageController] );

    //url配置信息
    var urlBase = "";

    //主页面控制器
    function PageController($scope,$http,$location,$interval) {
        var urlToSaler="/promotetosaler";

        $scope.postvars ={
            salesmanId:"",
            targetId:"",
            promoteCode:""
        };

        $scope.init = function() {

        };
        $scope.postPhone = function() {
            $scope.postvars.promoteCode = $("#promoteCode").val();
            $scope.postvars.salesmanId = $("#salesmanId").val();
            $http.post(urlBase + urlToSaler,$scope.postvars)
                    .success( function(response) {
                });
        };
    }
})();


