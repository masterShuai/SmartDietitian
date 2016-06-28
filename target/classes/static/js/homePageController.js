var app = angular.module('smartDietitian', ['chieffancypants.loadingBar']);
/*
app.factory('hexafy', function () {
    var userSearch = "";

    return userSearch;
});
*/

//控制器配置信息
app.controller('HomeController', ['$scope', '$http', HomeController]);


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
        , topTenFood: "/Search/Nutrition/getTopTen"
        //, allFood: "/Search/Food/all"
        , foodById: "/Search/Food/byId"
        , foodByName: "/Search/Food/byName"
        , topTenCooking: "/Search/Food/getTopTen"
        //, allCooking:"/Search/Cooking/all"
        , cookingById: "/Search/Cooking/byId"
        , cookingByName: "Search/Cooking/byName"
        , getCookingName: "Search/Cooking/nameByUserCooking"
        , getTodayNutrition: "Search/Cooking/getTodayNutrition"
        , register: "/User/register"
        , login: "/User/login"
        , addCooking: "/Search/Cooking/save"
        , isLogin: "/User/isLogin"
        , addTodayCooking: "/User/setTodayCooking"
        , getTodayCooking: "/User/getTodayCooking"
        , getUserById: "/User/getUserById"
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
        "foodId": 0,
        "foodName": ""
    };
    //请求菜品
    $scope.sendCookingData =
    {
        "cookingId": 0,
        "cookingName": ""
    };

    //请求登录
    $scope.sendLogin = {"id": "", "passWord": ""};

    //添加饮食计划
    $scope.cookingContent={
        "cookingId": 0,
        "contentName": "",
        "content": 1,
        "numb": 1
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
        "nutritionMan": [{
            "nutritionId": "",
            "nutritionName": "",
            "content": 0,
            "nutritionUnit": ""
        }],
        "cookingContent": [{
            "cookingId": 0,
            "contentName": "",
            "content": 0,
            "numb": 0
        }]
    };

    //食材名称列表
    $scope.foodList = [
        {
            "foodId": 0,
            "foodName": "wating..."
        }
    ];

    $scope.thisFoodContent = 1;

    //营养元素名称列表
    $scope.nutritionList = [
        {
            "nutritionId": "",
            "nutritionName": "wating..."
        }
    ];

    //菜品名称列表
    $scope.cookingList = [
        {
            "cookingId": 1,
            "cookingName": "wating..."
        }
    ];

    //营养元素详细
    $scope.theNutrition = {
        "id": "",
        "name": "",
        "unit": "",
        "lack_harm": "",
        "excess_harm": "",
        "contain_food": ""
    };

    //营养含量列表
    $scope.nutritionContentList = [{
        "nutritionId": "",
        "nutritionName": "",
        "content": 0,
        "nutritionUnit": ""
    }];


    //食材用量列表
    $scope.foodContentList = [{
        "foodId": 0,
        "foodName": "",
        "content": 0

    }];

    //饮食计划中今日菜品信息
    $scope.cookingContentList=[{
        "cookingId": 0,
        "contentName": "",
        "content": 1,
        "numb": 1
    }];

    //今日饮食计划
    $scope.todayCooking= {
        "cookingContent": [{
            "cookingId": 0,
            "cookingName": "",
            "content": 0,
            "numb": 0
        }]
    };

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

    //菜品详情
    $scope.theCooking = {
        "id": 0,
        "name": "",
        "otherName": "",
        "taste": "",
        "kind": "",
        "style": "",
        "feature": "",
        "howToCook": "",
        "authorId": "",
        "nutritionContent": [{
            "nutritionId": "",
            "nutritionName": "",
            "content": 0,
            "nutritionUnit": ""
        }],
        "foodContent": [{
            "foodId": 0,
            "foodName": "",
            "content": 0
        }]
    };

    $scope.userSearch = "";

    $scope.sex = [
        {string : "男性", value : false},
        {string : "女性", value : true}
    ];

    $scope.WomanState= [
        {string : "普通女性", value : 0},
        {string : "孕妇", value : 1},
        {string : "乳母", value : 2}
    ];

    $scope.gestation= [
        {string : "怀孕初期", value : 1},
        {string : "怀孕中期", value : 2},
        {string : "怀孕后期", value : 3}
    ];

    $scope.manualWork = [
        {string : "不从事", value : 0},
        {string : "轻微体力劳动", value : 1},
        {string : "中度体力劳动", value : 2},
        {string : "重体力劳动", value : 3}
    ];

    $scope.userSex=null;
    $scope.userState=null;
    $scope.userGestation=null;
    $scope.userWork=null;

    $scope.fName="";

    $scope.lactation = function(){
        if($scope.userState==null) return false;
        else{
            if($scope.userState.value==1&&$scope.userSex) return true;
            else return false;
        }
    }

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
   // $scope.navLoginTag  = "登录";

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

        $scope.sendNutritionData.nutritionId = $("#nutritionId").val();
        //console.log($scope.userChoose);
        $http.post($scope.urls.nutritionById, $scope.sendNutritionData)
            .success(function (response) {
                if (response.result == "success") {
                    $scope.theNutrition = response.content;
                    $scope.topTenFood();
                } else {
                    alert("未查询到您想要的信息,请核对后重试");
                }
            });

    }

    //获取食材中某营养元素含量前十名
    $scope.topTenFood = function () {
        $scope.sendNutritionData.nutritionId = $scope.theNutrition.id;
        $scope.sendNutritionData.nutritionName = $scope.theNutrition.name;
        $http.post($scope.urls.topTenFood, $scope.sendNutritionData)
            .success(function (response) {
                if (response.result == "success") {
                    $scope.foodContentList = response.content;
                } else {
                    alert("未查询到食材排名,请核对后重试");
                }
            });
    }

    //获取食材详情
    $scope.foodDetail = function () {
        $scope.isUserLogin();
        $scope.sendFoodData.foodId = $("#foodId").val();

        //console.log($scope.userChoose);
        $http.post($scope.urls.foodById, $scope.sendFoodData)
            .success(function (response) {
                if (response.result == "success") {
                    $scope.theFood = response.content;
                    $scope.theFood.unit = "克";
                    $scope.topTenCooking();
                } else {
                    alert("未查询到您想要的信息,请核对后重试");
                }
            });
    }

    //获取菜品中某食材用量前十名
    $scope.topTenCooking = function () {
        $scope.sendFoodData.foodId = $scope.theFood.id;
        $scope.sendFoodData.foodName = $scope.theFood.name;
        $http.post($scope.urls.topTenCooking, $scope.sendFoodData)
            .success(function (response) {
                if (response.result == "success") {
                    $scope.foodContentList = response.content;
                } else {
                    alert("未查询到食材排名,请核对后重试");
                }
            });
    }

    //向今日饮食计划中添加一道菜品
    $scope.addTodayCooking = function(){
        if($scope.cookingContent.content>0&&$scope.cookingContent.numb>=1) {
            $scope.cookingContent.cookingId = $scope.theCooking.id;
            $scope.cookingContent.cookingName = $scope.theCooking.name;
            $http.post($scope.urls.addTodayCooking, $scope.cookingContent)
                .success(function (response) {
                    if (response.result == "success") {
                        $scope.foodContentList = response.content;
                        alert("添加成功!")
                    } else {
                        alert("添加失败");
                    }
                });
        }
        else{
            alert("食用人数或菜品份数不合理!");
        }
    }

    //获取菜品详情
    $scope.cookingDetail = function () {
        $scope.isUserLogin();
        $scope.sendCookingData.cookingId = $("#cookingId").val();
        console.log($("#cookingId").val());

        $http.post($scope.urls.cookingById, $scope.sendCookingData)
            .success(function (response) {
                if (response.result == "success") {
                    $scope.theCooking = response.content;
                } else {
                    alert("未查询到您想要的信息,请核对后重试");
                }
            });

    }

    //验证用户是否登录
    $scope.isUserLogin = function () {
       // alert("验证是否登录...");
        $http.post($scope.urls.isLogin)
            .success(function (response) {
                if (response.content == "success") {
                   // alert("登陆成功");
                    $scope.loginTag = true;
                   // $scope.navLoginTag  = "个人中心";
                    $scope.navPage = "userCenter";
                    return true;
                }
                else {
                    //alert("登陆失败");
                    $scope.loginTag = false;
                    //$scope.navLoginTag  = "登录";
                    $scope.navPage = "login";
                    return false;
                }
            });
    }


    //导航栏登录按钮显示
    $scope.navigation = function () {
        if ($scope.loginTag==true) {
            //alert("已登录");
            return "个人中心";
            //alert("已登录");
        }
        else {
            //alert("未登录");
            return "登录";
        }
    }

    //用户登录操作
    $scope.login = function () {
        if ($scope.sendLogin.id == "" || $scope.sendLogin.passWord == "") {
            alert("登录失败!用户名密码不能为空");
            return;
        }
        $http.post($scope.urls.login, $scope.sendLogin)
            .success(function (response) {
                if (response.result == "success") {
                    alert("ID:"+$scope.sendLogin.id+"登录成功!即将跳转...");
                    history.back(-1);
                } else {
                    alert("登录失败!请检查用户名密码是否正确");
                }
            });
    }

    //用户注册
    $scope.register = function(){
        if($scope.user.id==""){
            alert("用户名不能为空!");
            return;
        }
        if($scope.user.name==""){
            alert("姓名不能为空!");
            return;
        }
        if($scope.user.password==""){
            alert("密码不能为空!");
            return;
        }
        if($scope.user.weight==0||$scope.user.weight==null){
            alert("体重不能为0!");
            return;
        }
        if($("#birthday").val()==""){
            alert("生日不能为空!");
            return;
        }else{
            $scope.user.birthday=$("#birthday").val();
        }
        if($scope.userWork==null){
            alert("体力活动程度不能为空!");
            return;
        }else{
            $scope.user.userWork = $scope.userWork.value;
        }
        if($scope.userSex==null){
            alert("性别不能为空!");
            return;
        }else{
            if($scope.userSex.value){
                $scope.user.sex = "女";
                if($scope.userState!=null){
                    if($scope.userState.value==1){
                        if($scope.userGestation!=null){
                            $scope.user.pregnant= true;
                            $scope.user.lactation=false;
                            $scope.user.gestation= $scope.userGestation.value;
                        }else{
                            alert("请选择孕期");
                            return;
                        }
                    }else if($scope.userState==0){
                        $scope.user.pregnant= false;
                        $scope.user.lactation=false;
                        $scope.user.gestation= 0;
                    }else{
                        $scope.user.pregnant= false;
                        $scope.user.lactation=true;
                        $scope.user.gestation= 0;
                    }
                }else{
                    alert("请选择生理状态");
                    return;
                }
            }else{
                $scope.user.sex = "男";
                $scope.user.pregnant= false;
                $scope.user.lactation=false;
                $scope.user.gestation= 0;
            }
        }


        $http.post($scope.urls.register,$scope.user)
            .success(function (response) {
                if(response.result=="success"){
                    alert("注册成功,返回登录页面!");
                    window.location.href="login";
                }else{
                    alert("注册失败!用户名已存在!");
                }
            });
    }

    //diy页面初始化
    $scope.diyInit = function(){
        $scope.isUserLogin();
        //if($scope.isUserLogin()){
            $scope.theCooking.foodContent.splice(0,1);
            $scope.foodList.splice(0,1);
            return;
        //}else{
           // alert("请先登录!");
          //  window.location.href="login";
       // }

    }

    //根据名称查询食材
    $scope.searchFood = function(){
        $scope.sendFoodData.foodName = $scope.fName;
        $http.post($scope.urls.foodByName, $scope.sendFoodData)
            .success(function (response) {
                $scope.foodList = response.content;
            });
    }

    //选用一种新食材
    $scope.addFood = function(item){
        var newFood = {
            "foodId": 0,
            "foodName": "",
            "content": 0
        };
        newFood.foodId = item.foodId;
        newFood.foodName = item.foodName;
        newFood.content = parseInt($scope.thisFoodContent);
        $scope.theCooking.foodContent.push(newFood);


        $scope.foodList = [];
    }

    $scope.removeFood = function(index){
        $scope.theCooking.foodContent.splice(index,1);
    }

    //提交新菜品
    $scope.addNewCooking = function(){
        $http.post($scope.urls.addCooking,$scope.theCooking)
            .success(function (response) {
                if(response.result=="success"){
                    alert("添加,返回主页!");
                }else{
                    alert("添加失败");
                }
            });
    }

    //获取今日饮食计划,营养含量,推荐摄入量,计算
    $scope.smartInit = function(){
        $scope.isUserLogin();
        $scope.todayCooking;
        //获取今日饮食计划
        $http.post($scope.urls.getTodayCooking).success(function (response) {
                if(response.result!="success"){
                    alert("查询今日饮食计划失败!");
                    history.back(-1);
                }else{
                    //获取今日计划中的菜品名称
                    $scope.todayCooking =  response.content;
                    $http.post($scope.urls.getCookingName,$scope.todayCooking).success(function (response) {
                        if(response.result!="success"){
                            alert("查询菜品名称失败!");
                            history.back(-1);
                        }else{
                            $scope.todayCooking = response.content;
                            //获取今日计划的营养含量
                            $http.post($scope.urls.getTodayNutrition,$scope.todayCooking).success(function (response) {
                                if(response.result!="success"){
                                    alert("计算营养含量失败!");
                                    history.back(-1);
                                }else{
                                    $scope.nutritionContentList = response.content;
                                    //获取用户推荐营养摄入量
                                    $http.post($scope.urls.getUserById)
                                        .success(function (response) {
                                            if(response.result!="success"){
                                                alert("获取推荐摄入量失败!");
                                                history.back(-1);
                                            }else{
                                                $scope. $scope.user = response.content;
                                            }
                                        });
                                }
                            });
                        }
                    });
                }
        });
    }

    $scope.getUserNutrition = function(id){
        for(var i=0;i<$scope.user.nutritionMin.length;i++){
            if($scope.user.nutritionMin[i].nutritionId.equals(id)){
                return user.nutritionMin[i].content;
            }
        }
        return 0;
    }
}

