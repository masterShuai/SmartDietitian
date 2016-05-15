var app = angular.module('smartDietitian', []);
app.factory('hexafy', function() {
    var userSearch="";

    return userSearch;
});
//控制器配置信息
app.controller('HomeController', ['$scope', '$http','hexafy',HomeController]);


function HomeController($scope, $http, hexafy) {

    /**
     * 初始化
     */
        $scope.tittle = "智能营养师主页";
        /**
         * 后台地址
         * @type {{allNutrition: string, allFood: string, allCooking: string, register: string, login: string}}
         */
        $scope.urls = {
            //allNutrition: "/Search/Nutrition/all",
            nutritionById: "/Search/Nutrition/byId"
            , nutritionByName: "/Search/Nutrition/byName"
            //, allFood: "/Search/Food/all"
            , foodById: "/Search/Food/byId"
            , foodByName: "/Search/Food/byName"
            //, allCooking:"/Search/Cooking/all"
            ,cookingById:"/Search/Cooking/byId"
            ,cookingByName:"Search/Cooking/byName"
            , register: "/User/register"
            , login: "/User/login"
            , addCooking: "/Search/Cooking/save"
        };

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
                "foodName": "test"
            }
        ];

        //营养元素名称列表
        $scope.nutritionContent = [
            {
                "nutritionId": "",
                "nutritionName": "test"
            }
        ];

        $scope.cookingContent = [
            {
                "cookingId": 1,
                "cookingName": "test"
            }
        ];


        $scope.userSearch = "";

    //转到查询结果页面
    $scope.goSearching = function(){
/*
        console.log($scope.userSearch);
        hexafy.userSearch = $scope.userSearch ;
        console.log(hexafy.userSearch);
*/
        window.location.href="/searchResult?searchContent=" + $scope.userSearch;
    }

    //转到主页
    $scope.goHome = function(){
        window.location.href="/";
    }

    /**
     * 获取包含查询字符串的 菜品\食材\营养元素列表
     */
    $scope.search = function () {
        //$scope.userSearch = hexafy.userSearch;
        //console.log(hexafy.userSearch);
        //console.log($scope.userSearch);

        $scope.userChoose = $("#searchContent").val();
        console.log($scope.userChoose);

        $scope.sendNutritionData.nutritionName = $scope.userChoose;
        $scope.sendFoodData.foodName = $scope.userChoose;
        $scope.sendCookingData.cookingName = $scope.userChoose;

        console.log( "查询"+$scope.sendFoodData.foodName);
        //加载营养元素名称列表
        $http.post($scope.urls.nutritionByName,$scope.sendNutritionData)
            .success(function (response) {
                console.log(response);
                $scope.nutritionContent = response.content;
            });
        console.log("查询营养元素");
        //加载食材名称列表
        $http.post($scope.urls.foodByName,$scope.sendFoodData)
            .success(function (response) {
                $scope.foodContent = response.content;
            });
        console.log("查询食材");
        //加载菜品名称列表
        $http.post($scope.urls.cookingByName,$scope.sendCookingData)
            .success(function (response) {
                $scope.cookingContent = response.content;
            });
        console.log("查询菜品");
    }

}

