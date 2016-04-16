
    //删除所有空格函数
    String.prototype.trim=function() {

        return this.replace(/(^\s*)|(\s*$)/g,'');
    }

    var mainApp = angular.module("test", []);

    mainApp.controller('PhoneListCtrl', function($scope, $http) {

        $scope.urls = {
            all: "/Search/Nutrition/all"
            , ById: "/Search/Nutrition/byId"
            , ByName: "/Search/Nutrition/byName"
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
        $scope.NutritionReqData = {
            "nutritionId": 0,
            "nutritionName": "维生素"
        };

        //接受营养元素详细信息的格式
        $scope.NutritionDetail = {"id":"22",
            "name":"维生素C",
            "unit":"毫克",
            "lack_harm":"缺乏维生素C可患坏血病",
            "excess_harm":null,
            "contain_food":"蔬菜和水果中维生素C含量较多,如辣椒、苦瓜、青蒜、萝卜叶、油菜、香菜、番茄等。 "
        }
        $scope.nutritionContent = [
            {
                "nutritionId": 0,
                "nutritionName": "未找到结果"
            }
        ]

        $http.post($scope.urls.all)
            .success(function(response) {
                $scope.nutritionContent = response.content;
            });

        $scope.SearchNutritionByName = function(){

            $scope.SearchResult =false;
            $scope.NutritionReqData.nutritionName =  $scope.NutritionReqData.nutritionName.trim();
            $http.post($scope.urls.ByName,$scope.NutritionReqData)
                .success(function(response) {
                    $scope.nutritionContent = response.content;
                    $scope.SearchResult = true;
                });



            /*console.dir( "SearchResult:"+$scope.SearchResult+" nutritionContent.length:"+$scope.nutritionContent.length);
            console.dir( "nutritionContent:"+$scope.nutritionContent[0]);
            console.dir( "nutritionContent:"+$scope.nutritionContent[1]);
            console.dir( "nutritionContent:"+$scope.nutritionContent[2]);*/
        }

        $scope.isDivVisible = function(){ return $scope.SearchResult;}

        $scope.SearchNutritionById = function(id){
            $scope.NutritionReqData.nutritionId = id;
            $scope.SearchDetailResult =false;
            $http.post($scope.urls.ById,$scope.NutritionReqData)
                .success(function(response) {
                    $scope.NutritionDetail = response.content;
                    $scope.SearchDetailResult = true;
                });
        }

        $scope.isDetailDivVisible = function(){
            return $scope.SearchDetailResult;
        }
    });

