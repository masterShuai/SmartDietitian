var app = angular.module('smartDietitian', []);

//控制器配置信息
app.controller('HomeController', ['$scope', '$http', HomeController]);
//app.controller('ExamMController',['$scope','$http',ExamMController] );

function HomeController($scope, $http) {
    /**
     * 后台地址
     * @type {{allNutrition: string, allFood: string, allCooking: string, register: string, login: string}}
     */
    $scope.urls = {
        allNutrition: "/Search/Nutrition/all",
        allFood: "/Search/Food/all",
        allCooking: "Search/Cooking/all",
        register: "/User/register",
        login: "/User/login",
        go:"/searchResult"
    }

    $scope.anchorPaper = {
        foodDetails: "/food"
    }

    /**
     * 协议(数据接收发送的格式)
     */

        //____________________________发送________________________________

        //发送给后台请求查找营养元素的 格式
    $scope.sendNutritionData = {
        "nutritionId": "",
        "nutritionName": "维生素"
    };
    //请求详细食材
    $scope.sendFoodData = {
        "foodId": 1,
        "foodName": ""
    };
    //请求菜品
    $scope.sendCookingData =
    {
        "cooking": 1,
        "cookingName": ""
    };

    //____________________________接受________________________________

    //食材名称列表
    $scope.foodContent = [
        {
            "foodId": 0,
            "foodName": ""
        }
    ];

    //营养元素名称列表
    $scope.nutritionContent = [
        {
            "nutritionId": "",
            "nutritionName": ""
        }
    ];

    $scope.cookingContent = [
        {
            "cookingId": 1,
            "cookingName": ""
        }
    ]

    $scope.userCheck = 0;
    $scope.list = [{}];
    $scope.userSelect = "";

    /**
     * 页面初始化
     */
    $scope.loadPaper = function() {
        //加载营养元素名称列表
        $http.post($scope.urls.allNutrition)
            .success(function (response) {
                $scope.nutritionContent = response.content;
            });
        //加载食材名称列表
        $http.post($scope.urls.allFood)
            .success(function (response) {
                $scope.foodContent = response.content;
            });

        //加载菜品名称列表
        $http.post($scope.urls.allCooking)
            .success(function (response) {
                $scope.cookingContent = response.content;
            });
    }

    $scope.check = function(i){
        //选择搜索菜品
        if(i==1){
            userCheck = 1;
            list =  $scope.cookingContent
        }
        //选择搜索食材
        else if(i==2){
            userCheck = 2;
        }
        //选择搜索营养元素
        else if(i==3){
            userCheck = 3;
        }
    }

    $scope.toSearch = function (producerId) {
        var url = $scope.urls.go+"?"+"searchContent="+$scope.userInput;
        window.navigate(url);
    };

}