var app = angular.module('smartDietitian', []);
app.factory('hexafy', function () {
    var userSearch = "";

    return userSearch;
});
//控制器配置信息
app.controller('HomeController', ['$scope', '$http', 'hexafy', HomeController]);


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
        ,topTenFood:"/Search/Nutrition/getTopTen"
        //, allFood: "/Search/Food/all"
        , foodById: "/Search/Food/byId"
        , foodByName: "/Search/Food/byName"
        //, allCooking:"/Search/Cooking/all"
        , cookingById: "/Search/Cooking/byId"
        , cookingByName: "Search/Cooking/byName"
        , register: "/User/register"
        , login: "/User/login"
        , addCooking: "/Search/Cooking/save"
        , isLogin:"/User/isLogin"
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

    //用户类
    $scope.user = {
        "id": "",
        "name": "",
        "password": "",
        "sex": "",
        //"birthday": new Date(),
        "birthday": "",
        "weight": 0,
        "pregnant": false,
        "lactation": false,
        "gestation": 0,
        "manualWork": 0,
        "nutritionMin": [{
            "nutritionId": "",
            "nutritionName": "",
            "content": 0,
            "nutritionUnit": ""
        }],
        "nutritionMan":  [{
            "nutritionId": "",
            "nutritionName": "",
            "content": 0,
            "nutritionUnit": ""
        }],
        "cookingContent":  [{
            "cookingId":0,
            "contentName":"",
            "content":0,
            "numb":0
        }]
    };

    //食材名称列表
    $scope.foodList = [
        {
            "foodId": 0,
            "foodName": "test"
        }
    ];

    //营养元素名称列表
    $scope.nutritionList = [
        {
            "nutritionId": "",
            "nutritionName": "test"
        }
    ];

    //菜品名称列表
    $scope.cookingList = [
        {
            "cookingId": 1,
            "cookingName": "test"
        }
    ];

    //营养元素详细
    $scope.theNutrition={
        "id": "",
        "name": "",
        "unit": "",
        "lack_harm": "",
        "excess_harm": "",
        "contain_food": ""
    };

    //营养含量列表
    $scope.nutritionContentList =[{
        "nutritionId":"",
        "nutritionName":"",
        "content":0,
        "nutritionUnit":""
    }];

    //食材详细信息
    $scope.theFood = {
        "id": 0,//食材编号
        "city": "",//产地
        "name": "",//食材名称
        "other_name": "",//食材别名
        "kind": "",//类型
        "foo_value": "",//营养价值
        "diet": "",//忌食
        "unit": "",//食材计量单位
        "can_eat": "",//可否生食
        "nutritionContent": [{
            "nutritionId": "",
            "nutritionName": "",
            "content": 0,
            "nutritionUnit": ""
        }]
    };


    $scope.userSearch = "";

    //转到查询结果页面
    $scope.goSearching = function () {
        /*
         console.log($scope.userSearch);
         hexafy.userSearch = $scope.userSearch ;
         console.log(hexafy.userSearch);
         */
        window.location.href = "/searchResult?searchContent=" + $scope.userSearch;
    }

    //转到主页
    $scope.goHome = function () {
        window.location.href = "/";
    }

    //用户是否登录
    $scope.loginTag = false;
    $scope.navPage = "login";

//______________________________________________________________

    /**
     * 获取包含查询字符串的 菜品\食材\营养元素列表
     */
    $scope.search = function () {
        //$scope.userSearch = hexafy.userSearch;
        //console.log(hexafy.userSearch);
        //console.log($scope.userSearch);

        $scope.isUserLogin();

        $scope.userChoose = $("#searchContent").val();
        console.log($scope.userChoose);

        $scope.sendNutritionData.nutritionName = $scope.userChoose;
        $scope.sendFoodData.foodName = $scope.userChoose;
        $scope.sendCookingData.cookingName = $scope.userChoose;

        console.log("查询" + $scope.sendFoodData.foodName);
        //加载营养元素名称列表
        $http.post($scope.urls.nutritionByName, $scope.sendNutritionData)
            .success(function (response) {
                console.log(response);
                $scope.nutritionList = response.content;
            });
        console.log("查询营养元素");
        //加载食材名称列表
        $http.post($scope.urls.foodByName, $scope.sendFoodData)
            .success(function (response) {
                $scope.foodList = response.content;
            });
        console.log("查询食材");
        //加载菜品名称列表
        $http.post($scope.urls.cookingByName, $scope.sendCookingData)
            .success(function (response) {
                $scope.cookingList = response.content;
            });
        console.log("查询菜品");
    }

    //获取营养元素详情
    $scope.nutritionDetail = function () {
        $scope.isUserLogin();

        $scope.sendNutritionData.nutritionId= $("#nutritionId").val();
        //console.log($scope.userChoose);
        $http.post($scope.urls.nutritionById, $scope.sendNutritionData)
            .success(function (response) {
                if(response.result=="success"){
                    $scope.theFood = response.content;
                    $scope.topTenFood();
                }else{
                    alert("未查询到您想要的信息,请核对后重试");
                }
            });

    }

    //获取食材中某营养元素含量前十名
    $scope.topTenFood = function(){
        $scope.sendNutritionData.nutritionId= $scope.theNutrition.id;
        $scope.sendNutritionData.nutritionName= $scope.theNutrition.name;
        $http.post($scope.urls.topTenFood, $scope.sendNutritionData)
            .success(function (response) {
                if(response.result=="success"){
                    $scope.nutritionContentList = response.content;
                }else{
                    alert("未查询到您想要的信息,请核对后重试");
                }
            });
    }

    //获取食材详情
    $scope.foodDetail = function () {
        $scope.isUserLogin();

        $scope.sendFoodData.foodId= $("#foodId").val();
        //console.log($scope.userChoose);
        $http.post($scope.urls.foodById, $scope.sendFoodData)
            .success(function (response) {
                if(response.result=="success"){
                    $scope.theFood = response.content;
                    $scope.theFood.unit = "克";
                    //$scope.topTenFood();
                }else{
                    alert("未查询到您想要的信息,请核对后重试");
                }
            });

    }

    //验证用户是否登录
    $scope.isUserLogin = function(){
        $http.post($scope.urls.isLogin)
            .success(function (response) {
               if(response.result=="success")
               {
                   loginTag = true;
                   navPage = "userCenter";
               }
                else{
                   loginTag = false;
                   navPage = "login";
               }
            });
    }

    //导航栏登录按钮显示
    $scope.navigation = function(){
        if($scope.loginTag) {
            return "个人中心";
            //alert("未登录");
        }
        else {
            //alert("未登录");
            return "登录";
        }
    }
}

