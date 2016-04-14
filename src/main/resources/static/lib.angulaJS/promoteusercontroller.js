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
    var urlBase = "/api/v1/s";

    //主页面控制器
    function PageController($scope,$http,$location,$interval) {
        var urlToSaler="/p/touser";

        $scope.postvars ={
            salesmanId:"",
            targetId:"",
            promoteCode:""
        };

        $scope.init = function() {

        };
        $scope.postPhone = function() {
            $http.submit(urlBase + urlToSaler,$scope.postvars)
                .success( function(response) {
                });
        };
    }
})();


