var mainApp = angular.module("test", []);

mainApp.controller('PhoneListCtrl', function ($scope, $http) {

    $scope.urls = {
        allNutrition: "/Search/Nutrition/all"
        , nutritionById: "/Search/Nutrition/byId"
        , nutritionByName: "/Search/Nutrition/byName"
        , allFood: "/Search/Nutrition/all"
        , foodById: "/Search/Nutrition/byId"
        , foodByName: "/Search/Nutrition/byName"
        , register: "/User/register"
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

    $scope.SearchResult = false;
    $scope.SearchDetailResult = false;

    //发送给后台请求查找营养元素的 格式
    $scope.sendNutritionData = {
        "nutritionId": "",
        "nutritionName": "维生素"
    };
    //请求详细食材
    $scope.sendFoodData = {
        "foodId": 0,
        "foodName": ""
    };
    //请求菜品
    $scope.sendCookingData =
    {
        "cooking": 1,
        "cookingName": ""
    };
    //请求登录
    $scope.sendLogin = {"ID":"123","password":"123"};

    //注册用户数据
    $scope.sendRegister = {
        "id": "wangshuai",
        "name": "王帅",
        "password": "1234",
        "sex": "男",
        //"birthday": new Date(),
        "birthday": "1993-12-15",
        "weight": 44.0,
        "pregnant": false,
        "lactation": false,
        "gestation": 0,
        "manualWork": 1,
        "nutritionMin": null,
        "nutritionMan": null,
        "cookingContent": null
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //接受营养元素详细信息的格式
    $scope.NutritionDetail = {
        "id": "",
        "name": "",
        "unit": "",
        "lack_harm": "",
        "excess_harm": "",
        "contain_food": ""
    };
    //营养元素名称列表
    $scope.nutritionContent = [
        {
            "nutritionId": "",
            "nutritionName": ""
        }
    ];
    //接受食材详细信息的格式
    $scope.FoodDetail = {
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

    //食材名称列表
    $scope.foodContent = [
        {
            "foodId": 0,
            "foodName": ""
        }
    ];

    //菜品详情
    $scope.CookingDetail = {
        "id": 0,
        "name": null,
        "otherName": null,
        "taste": null,
        "kind": null,
        "style": null,
        "feature": null,
        "howToCook": null,
        "authorId": null,
        "nutritionContent": [{
            "nutritionId": "",
            "nutritionName": "",
            "content": 0,
            "nutritionUnit": ""
        }],
        "foodContent": [{
            "foodId": 0,
            "foodName": ""
        }]
    }


    $http.post($scope.urls.allNutrition)
        .success(function (response) {
            $scope.nutritionContent = response.content;
        });

    $scope.SearchNutritionByName = function () {

        $scope.SearchResult = false;
        $scope.NutritionReqData.nutritionName = $scope.NutritionReqData.nutritionName;
        $http.post($scope.urls.nutritionByName, $scope.sendNutritionData)
            .success(function (response) {
                $scope.nutritionContent = response.content;
                $scope.SearchResult = true;
            });


        /*console.dir( "SearchResult:"+$scope.SearchResult+" nutritionContent.length:"+$scope.nutritionContent.length);
         console.dir( "nutritionContent:"+$scope.nutritionContent[0]);
         console.dir( "nutritionContent:"+$scope.nutritionContent[1]);
         console.dir( "nutritionContent:"+$scope.nutritionContent[2]);*/
    }

    $scope.isDivVisible = function () {
        return $scope.SearchResult;
    }

    $scope.SearchNutritionById = function (id) {
        $scope.NutritionReqData.nutritionId = id;
        $scope.SearchDetailResult = false;
        $http.post($scope.urls.nutritionById, $scope.sendNutritionData)
            .success(function (response) {
                $scope.NutritionDetail = response.content;
                $scope.SearchDetailResult = true;
            });
    }

    $scope.register = function () {
        //$scope.sendRegister.birthday.setFullYear(1993,12,15);
        $http.post($scope.urls.register, $scope.sendRegister)
            .success(function (response) {
                alert(response.content);
            });
    }


    $scope.isDetailDivVisible = function () {
        return $scope.SearchDetailResult;
    }

});

