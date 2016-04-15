(function () {

    var app = angular.module('exam', []);
    app.filter('to_trusted', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        };
    }]);
    //控制器配置信息
    app.controller('Message1Controller', ['$scope', '$http', '$sce', Message1Controller]);
    app.controller('MessageListController', ['$scope', '$http', '$sce', MessageListController]);

    //主页面控制器
    function Message1Controller($scope, $http, $sce) {
        $scope.urls = {
            messageOne: "/api/se/m/one"
            , messageList: "/api/se/m/list"
        };

        //data
        $scope.messages = [{
            "id": 0,
            "title": "标题",
            "content": "内容"
        }];

        $scope.loadMessage = function () {
            $http.get($scope.urls.messageOne)
                .success(function (response) {
                    $scope.messages = response.content;
                });
        };
    }

    //订单控制器
    function MessageListController($scope, $http, $sce) {
        $scope.urls = {
            messageOne: "/api/se/m/one"
            , messageList: "/api/se/m/list"
        };

        //data
        $scope.messages = [{
            "id": 0,
            "title": "标题",
            "content": "内容"
        }];

        $scope.loadMessage = function () {
            $http.get($scope.urls.messageList)
                .success(function (response) {
                    $scope.messages = response.content;
                });
        };
    }

})();


