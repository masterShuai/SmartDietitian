(function() {

    var app = angular.module('sdyapp', ['ngRoute', 'route-segment', 'view-segment']);
    app.constant('fooConfig', {
        urlBase: "/api",
        urlBusinessBase: "/business",
        urlBusinessAll: "/all",
        config2: "Default config2"
    });
    app.config(function ($routeSegmentProvider) {

        $routeSegmentProvider
            .when('/', 'order')
            .when('/order', 'order')
            .when('/sales', 'sales')
            .when('/salesmanage', 'salesmanage')
            .when('/salesmanage/list', 'salesmanage.list')
            .when('/salesmanage/new', 'salesmanage.new')
            .when('/user', 'user')
            .when('/app', 'app')
            .when('/manager', 'manager')
            .when('/business', 'business')
            .when('/business/businesslist', 'business.businesslist')
            .when('/business/businessedit', 'business.businessedit')
            .when('/business/businessmanage', 'business.businessmanage')
            .when('/business/businessnew', 'business.businessnew')
            .when('/business/businessview', 'business.businessview')
            .when('/business/order', 'business.order')
            .when('/business/orderedit', 'business.orderedit')
            .when('/business/orderview', 'business.orderview')
            .when('/business/orderdeliver', 'business.orderdeliver')
            .when('/business/ordertrace', 'business.ordertrace')
            .when('/business/static', 'business.static')
            .segment('order', {
                default: true,
                templateUrl: '/views/order.html',
                controller: OrderController
            })

            .segment('sales', {
                templateUrl: '/views/sales.html',
                controller: SalesController
            })
            .root()
            .segment('salesmanage', {
                templateUrl: '/views/salesmanage.html',
                controller: SalesManagerController
            })
            .within()

            .segment('list', {
                default: true,
                templateUrl: '/views/salesmanage.list.html'
            })
            .segment('new', {
                templateUrl: '/views/salesmanage.new.html'
            })
            .root()
            .segment('user', {
                templateUrl: '/views/user.html',
                controller: UserController
            })

            //====business====================
            .root()
            .segment('business', {
                templateUrl: '/views/business.html',
                controller: BusinessController
            })

            //----business.list----
            .within()
            .segment('businesslist', {
                default: true,
                templateUrl: '/views/business.businesslist.html'
            })
            //----business.*----
            .segment('businessnew', {
                templateUrl: '/views/business.businessnew.html'
            })
            .segment('businessedit', {
                templateUrl: '/views/business.businessedit.html'
            })
            .segment('businessmanage', {
                templateUrl: '/views/business.businessmanage.html'
            })
            .segment('businessview', {
                templateUrl: '/views/business.businessview.html'
            })
            //.up()
            //----business.order.*----
            .segment('order', {
                templateUrl: '/views/business.order.html'
            })
            .segment('orderedit', {
                templateUrl: '/views/business.order.edit.html'
            })
            //.within()
            .segment('orderview', {
                templateUrl: '/views/business.order.view.html'
            })
            .segment('orderdeliver', {
                templateUrl: '/views/business.order.deliver.html'
            })
            .segment('ordertrace', {
                templateUrl: '/views/business.ordertrace.html'
            })
            //----business.static.*----
            .segment('static', {
                templateUrl: '/views/business.static.html'
            })

            .root()
            .segment('app', {
                templateUrl: '/views/app.html',
                controller: AppController
            })
            .root()
            .segment('manager', {
                templateUrl: '/views/manager.html',
                controller: ManagerController
            });
    });

    //控制器配置信息
    app.controller('PageController',['$scope','$http',"$location","$interval",PageController] );
    app.controller('OrderController',['$scope','$http',OrderController] );
    app.controller('SalesController', ['$scope','$http',SalesController]);
    app.controller('SalesManagerController', ['$scope','$http',SalesManagerController]);
    app.controller('UserController', ['$scope','$http',UserController]);
    app.controller('AppController', ['$scope','$http',AppController]);
    app.controller('BusinessController', ['$scope','$http','$upload',BusinessController]);
    app.controller('ManagerController', ['$scope','$http','$upload',ManagerController]);

    //url配置信息
    //var urlBase = "http://127.0.0.1:8090/api";
    var urlBase = "/api";

    var urlHelper=function(module,urlDesc) {
        $scope.menuState.show = !$scope.menuState.show;
    };

    //主页面控制器
    function PageController($scope,$http,$location,$interval) {
        var urlPageBase="/v1/cs";
        var urlPageOrderExNewCount="/o/list/ex/new/count";
        var urlPageOrderExComplainCount="/o/list/ex/complain/count";

        var promise = $interval(function(){
            $scope.postExceptionCount();
            console.log("running postExceptionCount");
        },15000);

        $scope.$on('$destroy',function(){
            $interval.cancel(promise);
        });

        //
        $scope.menuState = {
           show: false
        };
        $scope.excount = {
            count: 0
        };
        $scope.excountShow = {
            show: false
        };

        $scope.toggleMenu = function() {
            $scope.menuState.show = !$scope.menuState.show;
        };
        $scope.postExceptionCount = function() {
            $http.post(urlBase + urlPageBase + urlPageOrderExNewCount,{})
                .success( function(response) {
                    console.log(response.content);
                    $scope.excount.count = response.content;
                    if($scope.excount.count>0){
                        //alert("有超时单");
                        $scope.excountShow.show = true;
                        //播放声音
                        playClicked();
                    }
                    else{
                        $scope.excountShow.show = false;
                    }
                    //
                });
        };

        $scope.go = function ( urlToGo ) {
            //$location.hash(urlToGo);
           $location.path( urlToGo );
        };



        $scope.lbsProvince = ['北京市', '天津市', '河北省', '山西省', '内蒙古自治区', '辽宁省', '吉林省','黑龙江省', '上海市', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省','河南省', '湖北省', '湖南省', '广东省', '广西壮族自治区', '海南省', '重庆市','四川省', '贵州省', '云南省', '西藏自治区', '陕西省', '甘肃省', '青海省', '宁夏回族自治区', '新疆维吾尔自治区', '台灣', '香港特别行政区', '澳门特别行政区'];
        $scope.lbsdistrict =  [{id:"",value:"所有区域"},{id:"黄浦区",value:"黄浦区"},{id:"徐汇区",value:"徐汇区"},{id:"长宁区",value:"长宁区"},{id:"静安区",value:"静安区"},{id:"普陀区",value:"普陀区"},
            {id:"闸北区",value:"闸北区"},{id:"虹口区",value:"虹口区"},{id:"杨浦区",value:"杨浦区"},{id:"闵行区",value:"闵行区"},{id:"宝山区",value:"宝山区"},
            {id:"嘉定区",value:"嘉定区"},{id:"浦东新区",value:"浦东新区"},{id:"金山区",value:"金山区"},{id:"松江区",value:"松江区"},{id:"青浦区",value:"青浦区"},
            {id:"奉贤区",value:"奉贤区"},{id:"崇明县",value:"崇明县"}];
        $scope.orderstates =  [{id:"",value:"所有"},{id:"待接单",value:"待接单"},{id:"已接单",value:"已接单"},{id:"取货中",value:"取货中"},{id:"跑腿中",value:"跑腿中"},{id:"已完成",value:"已完成"}];
        $scope.businessLabels=[];
    }

    //订单控制器
    function OrderController($scope,$http) {
        var urlOrderBase ="/v1/cs";
        var urlCsOrderList ="/o/list";
        var urlCsOrderExNew="/o/list/ex/new";
        var urlCsSalerList ="/s/list";
        var urlCsOrderDispatch ="/o/asign";
        var urlCsOrderContact ="/o/contact";
        var urlCsOrderComplainList ="/o/c/list";
        var urlCsOrderComplainDetailList ="/o/c/list/detail";
        var urlCsOrderComplainReply ="/o/c/reply";

        var urlOrderOne ="/order";

        $scope.menuState={
            list:true,
            complains:false
        };

        $scope.orderComplainSearchParas ={
            orderId:"",
            phone:"",
            state:""
        };

        $scope.orderComplainReplyParas ={
            serviceId:"",
            reply:"",
            orderId:""
        };

        $scope.orderDispatchParas ={
            orderId:"",
            salesmanId:""
        };

        $scope.orderContactParas ={
            orderId:"",
            content:""
        };

        $scope.orderSearchParas ={
            page:0,
            size:10,
            userId:"",
            startTime:"",
            endTime:"",
            state:""
        };

        //data
        $scope.orderComplain = {"orderId":"150716000048","state":"StateComplainning","userId":"15821111890","userName":"","userPhone":"15821111890","serviceId":"","managerId":"","content":"等待时间太久\n跑腿费用过高\n笙歌了","latestReply":"","orderAmount":null,"orderFee":null,"timeCreate":1437619321000,"updateTime":null,"details":""};
        //data
        $scope.orderComplainList = [{"orderId":"150716000048","state":"StateComplainning","userId":"15821111890","userName":"","userPhone":"15821111890","serviceId":"","managerId":"","content":"等待时间太久\n跑腿费用过高\n笙歌了","latestReply":"","orderAmount":null,"orderFee":null,"timeCreate":1437619321000,"updateTime":null,"details":""},{"orderId":"150720000032","state":"StateComplainning","userId":"15821111890","userName":"","userPhone":"15821111890","serviceId":"","managerId":"","content":"contentcontentcontent","latestReply":"","orderAmount":null,"orderFee":null,"timeCreate":1437447887000,"updateTime":null,"details":""}];
        //data
        $scope.orderComplainDetailList = [{"orderId":"150716000048","state":"StateComplainning","userId":"15821111890","userName":"","userPhone":"15821111890","serviceId":"","managerId":"","content":"等待时间太久\n跑腿费用过高\n笙歌了","latestReply":"","orderAmount":null,"orderFee":null,"timeCreate":1437619321000,"updateTime":null,"details":""},{"orderId":"150720000032","state":"StateComplainning","userId":"15821111890","userName":"","userPhone":"15821111890","serviceId":"","managerId":"","content":"contentcontentcontent","latestReply":"","orderAmount":null,"orderFee":null,"timeCreate":1437447887000,"updateTime":null,"details":""}];
        //data
        $scope.orders =  [
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "5月5日 22:45",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            }
        ];

        $scope.order ={
            "orderId": "",
            "salesmanId": "",
            "openId": "",
            "content": "",
            "seller": "",
            "orderType": "0",
            "salesMoney": "",
            "productMoney": "0",
            "unitPrice": "0",
            "product": "",
            "productSize": "",
            "productWeight": "",
            "addressStart": "",
            "addressTarget": "",
            "userGps": "",
            "timeQueue": "null",
            "timeArrival": "null",
            "timeQueueAll": "null",
            "timeStart": "5月5日 22:45",
            "timeStop": "null",
            "timeService": "null",
            "timeSales": "null",
            "timeAllot": "null",
            "timeResponse": "null",
            "timeGrab": "null",
            "evaluation": "",
            "evaluationCon": "",
            "journey": "0",
            "timeConsum": "0",
            "state": "0",
            "phone": "",
            "phone1": "",
            "contact": "",
            "allotNum": "0",
            "cancelCon": "",
            "sendName": ""
        };
        //data
        $scope.saler = {"salesmanId":"13918155341","userName":"张斌","phone":"13918155341","rank":"3.0","level":"S级业务员","picheader":"1002.jpg","latitude":"31.146198","longitude":"121.417702"};
        //data
        $scope.salers =  [
            {"salesmanId":"13918155341","userName":"张斌","phone":"13918155341","rank":"3.0","level":"S级业务员","picheader":"1002.jpg","latitude":"31.146198","longitude":"121.417702"}
        ];

        $scope.menuShowComplain = function(){
            $scope.menuState.list = false;
            $scope.menuState.complains = true;
            console.log("------------");

        };

        $scope.menuShowOrders = function(){
            $scope.menuState.list = true;
            $scope.menuState.complains = false;
        };
        //functions
        $scope.postOrder = function() {
            $http.get(urlBase + urlOrderBase + urlOrderOne)
                .success( function(response) {
                    $scope.order = response.content;
                });
        };

        $scope.scViewOrder = function(orderId) {
            for (b in $scope.orders)
            {
                console.dir(b);
                if($scope.orders[b].orderId==orderId){
                    $scope.order = $scope.orders[b];
                    break;
                }
            }
        };

        $scope.scSalerSelect = function(orderId) {
            for (b in $scope.salers)
            {
                console.dir(b);
                if($scope.salers[b].salesmanId==orderId){
                    $scope.saler = $scope.salers[b];
                    break;
                }
            }
            $scope.orderDispatch();


        };

        $scope.orderDispatch = function(){
            $scope.orderDispatchParas.orderId = $scope.order.orderId;
            $scope.orderDispatchParas.salesmanId = $scope.saler.salesmanId;
            $http.post(urlBase + urlOrderBase + urlCsOrderDispatch,$scope.orderDispatchParas)
                .success( function(response) {
                    //$scope.orders = response.content;
                    $('#doc-modal-sales-select').modal('close');
                    $('#doc-modal-order-detail').modal('close');
                    $scope.getOrdersException();
                });
        };

        $scope.scOrderContact = function(){
            $scope.orderContactParas.orderId = $scope.order.orderId;
            $http.post(urlBase + urlOrderBase + urlCsOrderContact,$scope.orderContactParas)
                .success( function(response) {
                    //$scope.orders = response.content;
                    $('#doc-modal-order-contact').modal('close');
                    $('#doc-modal-order-detail').modal('close');
                    $scope.getOrdersException();
                });
        };

        $scope.getOrders = function() {
            $scope.menuShowOrders();
            $http.post(urlBase + urlOrderBase + urlCsOrderList,$scope.orderSearchParas)
                .success( function(response) {
                $scope.orders = response.content;
            });
        };

        $scope.getOrdersException = function() {
            $scope.menuShowOrders();
            $http.post(urlBase + urlOrderBase + urlCsOrderExNew,{})
                .success( function(response) {
                    $scope.orders = response.content;
                    if($scope.orders.length<=0){

                    }
                });
        };

        $scope.getSalerList = function() {
            $http.post(urlBase + urlOrderBase + urlCsSalerList,{})
                .success( function(response) {
                    $scope.salers = response.content;
                    console.log("getSalerList");
                });
        };

        $scope.init = function () {
            console.log("init---------");
            $scope.getOrders();
            $scope.getSalerList();
        };

        $scope.getOrderComplainStateUserDesc = function(state){
            if(state==="StateComplainning")
                return "申诉中";
            if(state==="StateComfirming")
                return "已反馈，待确认";
            if(state==="StateUserCompleted")
                return "已完成";
            if(state==="StateManageCanceled")
                return "已取消";
            return state;
        };

        $scope.getOrderComplainStateUserColorClass = function(state){
            if(state==="StateComplainning")
                return " am-badge-danger ";
            if(state==="StateComfirming")
                return " am-badge-success ";
            if(state==="StateUserCompleted")
                return " am-badge-secondary ";
            if(state==="StateManageCanceled")
                return "";

            return state;
        };

        $scope.getOrderStateUserDesc = function(state){
            if(state==="StateStart")
                return "待接单";
            if(state==="StateCustomerWaiting")
                return "已接单，待确认";
            if(state==="StateCustomerPayWaiting")
                return "待支付";
            if(state==="StateCustomerPaid")
                return "已支付，待配送";
            if(state==="StateCustomerGoGoGo")
                return "配送中";
            if(state==="StateCustomerComplaining")
                return "申诉中";
            if(state==="StateCustomerConfirm")
                return "已确认，待评分";
            if(state==="StateCustomerEvaluated")
                return "已评分";
            if(state==="StateCustomerCompleted")
                return "已完成";
            if(state==="StateCustomerCanceled")
                return "已取消";
            return state;
        };

        $scope.getOrderStateUserColorClass = function(state){
            if(state==="StateStart")
                return " am-badge-danger ";
            if(state==="StateCustomerWaiting")
                return " am-badge-warning ";
            if(state==="StateCustomerPayWaiting")
                return " am-badge-warning ";
            if(state==="StateCustomerPaid")
                return " am-badge-success ";
            if(state==="StateCustomerGoGoGo")
                return " am-badge-secondary ";
            if(state==="StateCustomerComplaining")
                return " am-badge-danger ";
            if(state==="StateCustomerConfirm")
                return " am-badge-primary ";
            if(state==="StateCustomerEvaluated")
                return " am-badge-primary ";
            if(state==="StateCustomerCompleted")
                return "";
            if(state==="StateCustomerCanceled")
                return " am-badge-danger ";
            return state;
        };

        $scope.getOrderStateSalerDesc = function(state){
            if(state==="StateStart")
                return "待接单";
            if(state==="StateSalerAccepted")
                return "已接单";
            if(state==="StateSalerToAddress1")
                return "抵达出发地";
            if(state==="StateSalerCheckInfoOk")
                return "已确认，待支付";
            if(state==="StateSalerGoGoGo")
                return "配送中";
            if(state==="StateSalerFetchProduct")
                return "已取货";
            if(state==="StateSalerConfirm")
                return "已送达";
            return state;
        };

        $scope.getOrderStateSalerColorClass = function(state){
            if(state==="StateStart")
                return " am-badge-danger ";
            if(state==="StateSalerAccepted")
                return " am-badge-warning ";
            if(state==="StateSalerToAddress1")
                return " am-badge-success ";
            if(state==="StateSalerCheckInfoOk")
                return " am-badge-warning ";
            if(state==="StateSalerGoGoGo")
                return " am-badge-success ";
            if(state==="StateSalerFetchProduct")
                return " am-badge-success ";
            if(state==="StateSalerConfirm")
                return " am-badge-primary ";
            return state;
        };

        $scope.getOrderPayTypeDesc = function(state){
            if(state==="PayTypeNotSet")
                return "未选择";
            if(state==="PayTypeWeixin")
                return "微信支付";
            if(state==="PayTypeZhiFuBao")
                return "支付宝";
            if(state==="PayTypeOffline")
                return "线下支付";
            return state;
        };

        $scope.getOrderPayStateDesc = function(state){
            if(state==="PayStateWaiting")
                return "待支付";
            if(state==="PayStateSuccess")
                return "已支付";
            return state;
        };

        $scope.getAddressUrl = function(){
            return "http://ditu.amap.com/?type=car&policy=5&from[name]="+$scope.order.startAddress+"&to[name]="+$scope.order.targetAddress+"&refwd=0";
        };

        $scope.getOrderComplainDetailList = function() {
            $scope.orderComplainSearchParas.orderId=$scope.orderComplain.orderId;
            $http.post(urlBase + urlOrderBase + urlCsOrderComplainDetailList,$scope.orderComplainSearchParas)
                .success( function(response) {
                    $scope.orderComplainDetailList = response.content;
                    console.log("getSalerList");
                });
        };

        $scope.getOrderComplainList = function() {
            $scope.menuShowComplain();
            $http.post(urlBase + urlOrderBase + urlCsOrderComplainList,$scope.orderComplainSearchParas)
                .success( function(response) {
                    $scope.orderComplainList = response.content;
                    console.log("getSalerList");
                });
        };

        $scope.setOrderComplain = function(orderId) {
            for (b in $scope.orderComplainList)
            {
                console.dir(b);
                if($scope.orderComplainList[b].orderId==orderId){
                    $scope.orderComplain = $scope.orderComplainList[b];
                    $scope.getOrderComplainDetailList();
                    break;
                }
            }
        };

        $scope.setOrderComplainReply = function() {
            $scope.orderComplainReplyParas.orderId =$scope.orderComplain.orderId;
            $http.post(urlBase + urlOrderBase + urlCsOrderComplainReply,$scope.orderComplainReplyParas)
                .success( function(response) {
                    console.log("setOrderComplainReply");
                });
        };

    }

    //业务员控制器
    function SalesManagerController($scope,$http) {
        var urlSMBase ="/v1/sm";

        var urlSearch ="/search";
        var urlNew ="/new";
        var urlSaleState ="/state";

        $scope.currentSaleRefresh="/search";

        $scope.orderSearchParas ={
            page:0,
            size:10,
            salesmanId:"1002",
            startTime:"",
            endTime:"",
            state:""
        };

        $scope.serviceStateParas ={
            salesmanId:"",
            state:0,
            managerId:""
        };

        $scope.salerSearchParas ={
            salesmanId:"",
            salesmanName:"",
            managerId:""
        };

        //data
        $scope.sm_saler_new = {"salesmanId":"13918155341","userName":"张斌","phone":"13918155341","level":"S级业务员","picheader":"1002.jpg","personId":"","businessId":"","distict":"","type":"","city":"","password":""};

        //data
        $scope.sm_saler = {"salesmanId":"13918155341","userName":"张斌","phone":"13918155341","rank":"3.0","level":"S级业务员","picheader":"1002.jpg","latitude":"31.146198","longitude":"121.417702"};
        //data
        $scope.sm_salers =  [
            {"salesmanId":"13918155341","userName":"张斌","phone":"13918155341","rank":"3.0","level":"S级业务员","picheader":"1002.jpg","latitude":"31.146198","longitude":"121.417702"}
        ];

        //data
        $scope.s_orders =  [
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "null",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            }
        ];
        $scope.order ={
            "orderId": "",
            "salesmanId": "",
            "openId": "",
            "content": "",
            "seller": "",
            "orderType": "0",
            "salesMoney": "",
            "productMoney": "0",
            "unitPrice": "0",
            "product": "",
            "productSize": "",
            "productWeight": "",
            "addressStart": "",
            "addressTarget": "",
            "userGps": "",
            "timeQueue": "null",
            "timeArrival": "null",
            "timeQueueAll": "null",
            "timeStart": "",
            "timeStop": "null",
            "timeService": "null",
            "timeSales": "null",
            "timeAllot": "null",
            "timeResponse": "null",
            "timeGrab": "null",
            "evaluation": "",
            "evaluationCon": "",
            "journey": "0",
            "timeConsum": "0",
            "state": "0",
            "phone": "",
            "phone1": "",
            "contact": "",
            "allotNum": "0",
            "cancelCon": "",
            "sendName": ""
        };

        //----[业务员]---------------------
        $scope.smInit = function () {
            console.log("SalesManagerController init---------");
            $scope.salesSearch();
        };
        $scope.smNewPost = function () {
            console.log("SalesManagerController smNewPost---------");
            $http.post(urlBase + urlSMBase + urlNew,$scope.sm_saler_new).success( function(response) {
                $scope.salesSearch();
                //$scope.sm_salers = response.content;
            });
        };

        $scope.salesStateChange = function (salesmanid,state) {
            $scope.serviceStateParas.salesmanId = salesmanid;
            $scope.serviceStateParas.state = state;
            $http.post(urlBase + urlSMBase + urlSaleState,$scope.serviceStateParas).success( function(response) {
                $scope.salesSearch();
                //$scope.sm_salers = response.content;
            });
        };


        //functions
        $scope.salesSearch = function() {
            $http.post(urlBase + urlSMBase + urlSearch,$scope.salerSearchParas).success( function(response) {
                console.log("SalesManagerController salesSearch---------");
                $scope.sm_salers = response.content;
            });
        };

        $scope.getSalesStateDesc = function(state){
            if(state==="1")
                return "启用";
            if(state==="0")
                return "暂停";
            return state;
        };

        $scope.getSalesStateColorClass = function(state){
            if(state==="1")
                return " am-badge-success ";
            if(state==="0")
                return " am-badge-danger ";
            return state;
        };


    }

    //管理员控制器
    function ManagerController($scope,$http) {
        var urlServiceBase ="/v1/m";

        var urlServiceList ="/service/list";
        var urlServiceNew ="/service/new";
        var urlServiceState ="/service/state";

        $scope.currentSaleRefresh="/search";

        $scope.orderSearchParas ={
            page:0,
            size:10,
            salesmanId:"1002",
            startTime:"",
            endTime:"",
            state:""
        };

        $scope.serviceStateParas ={
            workId:"",
            state:"0",
            managerId:""
        };

        $scope.salerSearchParas ={
            salesmanId:"",
            salesmanName:"",
            managerId:""
        };

        //data
        $scope.sm_service_new = {"workId":"13918155341","name":"张斌","password":"1111","sex":"男","state":"1","personId":"","phone":"13918155341"};

        //data
        $scope.sm_service = {"workId":"13918155341","name":"张斌","password":"1111","sex":"男","state":"1","personId":"","phone":"13918155341"};
        //data
        $scope.sm_services =  [
            {"workId":"13918155341","name":"张斌","password":"13918155341","sex":"S级业务员","state":"1002.jpg","personId":"","phone":""}
        ];

         //----[业务员]---------------------
        $scope.serviceInit = function () {
            console.log("SalesManagerController init---------");
            $scope.serviceSearch();
        };
        $scope.serviceNewPost = function () {
            console.log("SalesManagerController smNewPost---------");
            $http.post(urlBase + urlServiceBase + urlServiceNew,$scope.sm_service_new).success( function(response) {
                $scope.serviceSearch();
                //$scope.sm_salers = response.content;
            });
        };
        $scope.serviceView = function(workId) {
            for (b in $scope.sm_services)
            {
                console.dir(b);
                if($scope.sm_services[b].workId==workId){
                    $scope.sm_service = $scope.sm_services[b];

                    break;
                }
            }
        };

        $scope.salesStateChange = function (salesmanid,state) {
            $scope.serviceStateParas.workId = salesmanid;
            $scope.serviceStateParas.state = state;
            $http.post(urlBase + urlServiceBase + urlServiceState,$scope.serviceStateParas).success( function(response) {
                $scope.serviceSearch();
                //$scope.sm_salers = response.content;
            });
        };

        //functions
        $scope.serviceSearch = function() {
            $http.post(urlBase + urlServiceBase + urlServiceList,{}).success( function(response) {
                console.log("SalesManagerController salesSearch---------");
                $scope.sm_services = response.content;
            });
        };
    }

    //业务员控制器
    function SalesController($scope,$http) {
        var urlOrderBase ="/v1/s";

        var urlOrderListNew ="/o/listnew";
        var urlOrderListRunning ="/o/listrunning";
        var urlOrderAccept ="/o/accept";
        var urlOrderconfirm ="/o/confirm";
        var urlOrdercomplete ="/o/complete";
        var urlOrdertoaddress1 ="/o/toaddress1";
        var urlOrdercheckinfo ="/o/checkinfo";
        var urlOrdergogogo ="/o/gogogo";

        $scope.currentSaleRefresh="/o/listnew";

        $scope.orderSearchParas ={
            page:0,
            size:10,
            salesmanId:"1002",
            startTime:"",
            endTime:"",
            state:""
        };

        $scope.orderStateParas ={
            salesmanId:"1002",
            orderId:"",
            newState:""
        };

        //data
        $scope.s_orders =  [
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "null",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            }
        ];
        $scope.order ={
            "orderId": "",
            "salesmanId": "",
            "openId": "",
            "content": "",
            "seller": "",
            "orderType": "0",
            "salesMoney": "",
            "productMoney": "0",
            "unitPrice": "0",
            "product": "",
            "productSize": "",
            "productWeight": "",
            "addressStart": "",
            "addressTarget": "",
            "userGps": "",
            "timeQueue": "null",
            "timeArrival": "null",
            "timeQueueAll": "null",
            "timeStart": "",
            "timeStop": "null",
            "timeService": "null",
            "timeSales": "null",
            "timeAllot": "null",
            "timeResponse": "null",
            "timeGrab": "null",
            "evaluation": "",
            "evaluationCon": "",
            "journey": "0",
            "timeConsum": "0",
            "state": "0",
            "phone": "",
            "phone1": "",
            "contact": "",
            "allotNum": "0",
            "cancelCon": "",
            "sendName": ""
        };

        //----[业务员]---------------------
        $scope.sInit = function () {
            console.log("SalesController init---------");
            $scope.getOrdersNew();
        };

        //functions
        $scope.saleOrderRefreshList = function() {
            if($scope.currentSaleRefresh===urlOrderListNew)
                $scope.getOrdersNew();
            if($scope.currentSaleRefresh===urlOrderListRunning)
                $scope.getOrdersRunning();

        };

        //functions
        $scope.getOrdersNew = function() {
            $scope.currentSaleRefresh = urlOrderListNew;
            $http.post(urlBase + urlOrderBase + urlOrderListNew,$scope.orderSearchParas).success( function(response) {
                $scope.s_orders = response.content;
            });
        };
        $scope.getOrdersRunning = function() {
            $scope.currentSaleRefresh = urlOrderListRunning;
            $http.post(urlBase + urlOrderBase + urlOrderListRunning,$scope.orderSearchParas).success( function(response) {
                $scope.s_orders = response.content;
            });
        };
        $scope.ordersAccept = function(orderId) {
            //修改参数
            $scope.orderStateParas.orderId=orderId;
            $http.post(urlBase + urlOrderBase + urlOrderAccept,$scope.orderStateParas).success( function(response) {
                $scope.saleOrderRefreshList();
                //$scope.orders = response.content;
            });
        };
        $scope.ordersComplete = function(orderId) {
            //修改参数
            $scope.orderStateParas.orderId=orderId;
            $http.post(urlBase + urlOrderBase + urlOrdercomplete,$scope.orderStateParas).success( function(response) {
                $scope.saleOrderRefreshList();
                //$scope.orders = response.content;
            });
        };
        $scope.ordersConfirm = function(orderId) {
            //修改参数
            $scope.orderStateParas.orderId=orderId;
            $http.post(urlBase + urlOrderBase + urlOrderconfirm,$scope.orderStateParas).success( function(response) {
                $scope.saleOrderRefreshList();
                //$scope.orders = response.content;
            });
        };
        $scope.ordersToaddress1 = function(orderId) {
            //修改参数
            $scope.orderStateParas.orderId=orderId;
            $http.post(urlBase + urlOrderBase + urlOrdertoaddress1,$scope.orderStateParas).success( function(response) {
                $scope.saleOrderRefreshList();
                //$scope.orders = response.content;
            });
        };
        $scope.ordersGogogo = function(orderId) {
            //修改参数
            $scope.orderStateParas.orderId=orderId;
            $http.post(urlBase + urlOrderBase + urlOrdergogogo,$scope.orderStateParas).success( function(response) {
                $scope.saleOrderRefreshList();
                //$scope.orders = response.content;
            });
        };
        $scope.ordersCheckinfo = function(orderId) {
            //修改参数
            $scope.orderStateParas.orderId=orderId;
            $http.post(urlBase + urlOrderBase + urlOrdercheckinfo,$scope.orderStateParas).success( function(response) {
                $scope.saleOrderRefreshList();
                //$scope.orders = response.content;
            });
        };
        $scope.lbsGetAllSaler = function() {
            //修改参数
            $http.get("http://lbs.supershank.com/api/v1/lbs/saler/list").success( function(response) {
                for(var salerIndex in response.content){
                    var marker = new AMap.Marker({
                        //复杂图标
                        icon: new AMap.Icon({
                            //图标大小
                            size:new AMap.Size(28,37),
                            //大图地址
                            image:"http://webapi.amap.com/images/custom_a_j.png",
                            imageOffset:new AMap.Pixel(-28,0)
                        }),
                        //在地图上添加点
                        position:new AMap.LngLat(123.45688,31.123456)
                    });
                    marker.position = new AMap.LngLat(response.content[salerIndex].lan,response.content[salerIndex].lon);
                    marker.setMap(map);

                }
            });
        };



    }

    //用户员控制器
    function UserController($scope,$http) {
        var urlOrderBase ="/business";
        var urlOrderAll ="/all";
        var urlOrderOne ="/order";
        //data
        $scope.orders =  [
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "5月5日 22:45",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            },
            {
                "orderId": "150428112620",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "null",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            },
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "null",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            }
        ];
        $scope.order ={
            "orderId": "",
            "salesmanId": "",
            "openId": "",
            "content": "",
            "seller": "",
            "orderType": "0",
            "salesMoney": "",
            "productMoney": "0",
            "unitPrice": "0",
            "product": "",
            "productSize": "",
            "productWeight": "",
            "addressStart": "",
            "addressTarget": "",
            "userGps": "",
            "timeQueue": "null",
            "timeArrival": "null",
            "timeQueueAll": "null",
            "timeStart": "",
            "timeStop": "null",
            "timeService": "null",
            "timeSales": "null",
            "timeAllot": "null",
            "timeResponse": "null",
            "timeGrab": "null",
            "evaluation": "",
            "evaluationCon": "",
            "journey": "0",
            "timeConsum": "0",
            "state": "0",
            "phone": "",
            "phone1": "",
            "contact": "",
            "allotNum": "0",
            "cancelCon": "",
            "sendName": ""
        };
        //functions
        $scope.postOrder = function() {
            $http.get(urlBase + urlOrderBase + urlOrderOne).success( function(response) {
                $scope.order = response;
            });
        };
        $scope.getOrders = function() {
            $http.get(urlBase + urlOrderBase + urlOrderAll).success( function(response) {
                $scope.orders = response;
            });
        };
    }

    //业务员控制器
    function AppController($scope,$http) {
        var urlOrderBase ="/business";
        var urlOrderAll ="/all";
        var urlOrderOne ="/order";
        //data
        $scope.orders =  [
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "5月5日 22:45",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            },
            {
                "orderId": "150428112620",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "null",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            },
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "null",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": ""
            }
        ];
        $scope.order ={
            "orderId": "",
            "salesmanId": "",
            "openId": "",
            "content": "",
            "seller": "",
            "orderType": "0",
            "salesMoney": "",
            "productMoney": "0",
            "unitPrice": "0",
            "product": "",
            "productSize": "",
            "productWeight": "",
            "addressStart": "",
            "addressTarget": "",
            "userGps": "",
            "timeQueue": "null",
            "timeArrival": "null",
            "timeQueueAll": "null",
            "timeStart": "",
            "timeStop": "null",
            "timeService": "null",
            "timeSales": "null",
            "timeAllot": "null",
            "timeResponse": "null",
            "timeGrab": "null",
            "evaluation": "",
            "evaluationCon": "",
            "journey": "0",
            "timeConsum": "0",
            "state": "0",
            "phone": "",
            "phone1": "",
            "contact": "",
            "allotNum": "0",
            "cancelCon": "",
            "sendName": ""
        };
        //functions
        $scope.postOrder = function() {
            $http.get(urlBase + urlOrderBase + urlOrderOne).success( function(response) {
                $scope.order = response;
            });
        };
        $scope.getOrders = function() {
            $http.get(urlBase + urlOrderBase + urlOrderAll).success( function(response) {
                $scope.orders = response;
            });
        };
    }

    //商户控制器
    function BusinessController($scope,$http) {
        var urlBzBase ="/v1/bm";

        var urlBzFindOne ="/get";
        var urlBzSearch ="/search";
        var urlBzStatisticSearch ="/statisticSearch";
        var urlBzNew ="/post";
        var urlBzOrder = "/orders";
        var urlBzOrderTraces = "/traces";
        var urlBzSale = "/saler";
        var urlBzSec ="/secpost";
        //====[data]====
        //----[商户]----
        $scope.businessSearchParas ={
            page:0,
            size:5,
            businessId:"",
            businessArea:""
        };

        $scope.businesses =  {
            first:false,
            last:false,
            number:0,
            numberOfElements:0,
            size:20,
            totalElements:0,
            totalPages:1,
            content: [
            {
                businessId: "",
                businessBriefName: "",
                businessName: "目前还没有商铺信息！",
                parentId: "",
                headQuarter: "",
                openingHours: "",
                openingHoursStart: null,
                openingHoursEnd: null,
                contactId: "",
                contacName: "",
                contactPhone: null,
                contactCellPhone: null,
                businessLicenseId:"",
                contactPersonId:"",
                password: null,
                address: "",
                type: "",
                state: "",
                name: "",
                labels: "",
                gpsType: "",
                latitude: null,
                longitude: null,
                discountRate: null,
                timeRegister: null
            }
            ]
        };
        $scope.business = {
            businessId: "123456789",
            businessBriefName: "达钮科技",
            businessName: "上海达钮信息科技有限公司",
            parentId: "",
            headQuarter: "",
            openingHours: "10：00～22：00",
            openingHoursStart: null,
            openingHoursEnd: null,
            contactId: "",
            contacName: "张三",
            contactPhone: "13402066813",
            contactCellPhone: "13402066813",
            businessLicenseId:"3401111111111111",
            contactPersonId:"3401111111111111",
            address: "测试地址",
            type: "",
            state: "0",
            name: "测试商户",
            labels: "美食,",
            gpsType: "",
            latitude: null,
            longitude: null
        };
        $scope.businessnew = {
            businessId: "123456789",
            businessBriefName: "达钮科技",
            businessName: "上海达钮信息科技有限公司",
            parentId: "",
            headQuarter: "",
            openingHours: "10:00～22:00",
            openingHoursStart: "10:00",
            openingHoursEnd: "22:00",
            contactId: "",
            contacName: "张三",
            contactPhone: "13402066813",
            contactCellPhone: "13402066813",
            businessLicenseId:"3401111111111111",
            contactPersonId:"3401111111111111",
            password: null,
            address: "测试地址",
            type: "",
            state: "",
            name: "测试商户",
            labels: "美食,"
        };
        $scope.businessEmpty = {
            businessId: "",
            businessBriefName: "",
            businessName: "",
            parentId: "",
            headQuarter: "",
            openingHours: "",
            openingHoursStart: "",
            openingHoursEnd: "",
            contactId: "",
            contacName: "",
            contactPhone: "",
            contactCellPhone: "",
            businessLicenseId:"",
            contactPersonId:"",
            password: null,
            address: "",
            type: "",
            state: "",
            name: "",
            labels: ""
        };

        //----[商户订单]---------------------
        $scope.businessOrdersSearchParas ={
            page:0,
            size:20,
            businessId:"",
            businessStartDate:"",
            businessEndDate:"",
            businessState:""
        };

        $scope.orders =  {
            first:false,
            last:false,
            number:0,
            numberOfElements:0,
            size:20,
            totalElements:0,
            totalPages:1,
            content: [
            {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "5月5日 22:45",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": "哈哈镜"
            },
            {
                "orderId": "150428112620",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "5月5日 22:45",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "A",
                "sendName": "哈哈镜"
            }
        ]};
        $scope.order = {
                "orderId": "150428112619",
                "salesmanId": "121212",
                "openId": "",
                "content": "",
                "seller": "",
                "orderType": "0",
                "salesMoney": "10",
                "productMoney": "0",
                "unitPrice": "0",
                "product": "来一份麻辣烫",
                "productSize": "",
                "productWeight": "",
                "addressStart": "",
                "addressTarget": "",
                "userGps": "",
                "timeQueue": "null",
                "timeArrival": "null",
                "timeQueueAll": "null",
                "timeStart": "5月5日 22:45",
                "timeStop": "null",
                "timeService": "null",
                "timeSales": "null",
                "timeAllot": "null",
                "timeResponse": "null",
                "timeGrab": "null",
                "evaluation": "",
                "evaluationCon": "",
                "journey": "0",
                "timeConsum": "0",
                "state": "0",
                "phone": "",
                "phone1": "",
                "contact": "",
                "allotNum": "0",
                "cancelCon": "",
                "sendName": "哈哈镜"
            };
        $scope.orderTraces =  {
            content:[]
        };
        $scope.saler =  {
            salesmanId:"",
            picheader:"",
            sex:"",
            level:"",
            phone:"",
            userName:"",
            age:0
        };

        //----[商户统计]---------------------
        $scope.businessSecurityParamater={
            businessId:"",
            newPassword:""
        };
        $scope.businessPunish={
            punishType:"",
            punishDay:0,
            reason:""
        };
        $scope.businessPunishHistory=[];
        $scope.businessStatisticSearchParas ={
            page:0,
            size:20,
            businessId:"",
            businessStartDate:"",
            businessEndDate:"",
            businessArea:""
        };

        $scope.businessStatistic = {
            content:[
            {
                businessId: "1",
                businessBriefName: "达钮科技",
                address: "测试地址",
                year: "2015",
                month: "5",
                week: "2",
                ordercount: "100"
            },
            {
                businessId: "1",
                businessBriefName: "百度",
                address: "测试地址",
                year: "2015",
                month: "5",
                week: "3",
                ordercount: "10"
            }
        ]} ;

        //====[function]====
        //----[商户]---------------------
        $scope.bzInit = function () {
            console.log("bzInit---------");
            $scope.bzGetFirstPage();
        };

        $scope.bzReset = function(){
            $scope.businessnew =$scope.businessEmpty;
        };

        //获取全部商户信息
        $scope.bzGetFirstPage = function() {
            //console.log("====bzGetFirstPage=ok==="+response.content);
            $scope.businessSearchParas.page=0;
            $scope.businessSearchParas.size=20;
            $scope.businessSearchParas.businessId="";
            $scope.bzSearch();
        };

        //获取全部商户信息
        $scope.bzSearch = function() {
            console.dir($scope.businessSearchParas);
            $http.post(urlBase+urlBzBase+urlBzSearch,$scope.businessSearchParas)
                .success( function(response) {
                    console.dir(response);
                    $scope.businesses = response.content;
            });
        };

        //提交一个新的商户信息
        $scope.bzNewPost = function() {
            $http.post(urlBase+urlBzBase+urlBzNew,$scope.businessnew)
                .success( function(response) {
                    $scope.getAllBusiness();
                    console.log(response);
                    $scope.go("/business/businesslist");
                });
        };

        //提交一个文件
        //$scope.bzFilePost = function() {
            //$upload.http({
            //    url: 'upload',
            //    headers: {'Content-Type': file.type},
            //    data: e.target.result
            //}).progress(function(ev) {
            //    //progress
            //}).success(function(data) {
            //    //success
            //}).error(function(data) {
            //    //error
            //});
        //};
        $scope.getStateDesc = function(state){
            if(state==="1")
                return "启用";
            if(state==="0")
                return "暂停";
            return state;
        };

        $scope.getStateColorClass = function(state){
            if(state==="1")
                return " am-badge-success ";
            if(state==="0")
                return " am-badge-danger ";
            return state;
        };


        //提交一个新的商户信息
        $scope.bzEditPost = function() {
            $http.post(urlBase+urlBzBase+urlBzNew,$scope.business)
                .success( function(response) {
                    $scope.getAllBusiness();
                    console.log(response);
                    $scope.go("/business/businesslist");
                });
        };

        //提交发送随机密码给商户
        $scope.bzSecurityRandom = function () {
            console.log("bzSecurityRandom---------");
            $scope.businessSecurityParamater.newPassword="";
            $scope.bzSecurity();
        };

        //提交发送密码给商户
        $scope.bzSecurity = function () {
            console.log("bzSecurity---------");
            $http.post(urlBase+urlBzBase+urlBzSec,$scope.businessSecurityParamater)
                .success( function(response) {
                    console.log(response);
                });
        };


        //初始化浏览视图需要的数据
        $scope.bzView = function(businessId) {
            console.log("====bzView====");
            console.dir(businessId);
            for (b in $scope.businesses.content)
            {
                console.dir(b);
                if($scope.businesses.content[b].businessId==businessId){
                    $scope.business = $scope.businesses.content[b];
                    break;
                }
            }
            $scope.go("/business/businessview");
        };

        $scope.bzToEdit = function(businessId) {
            console.log("====bzToEdit===="+businessId);
            for (b in $scope.businesses.content)
            {
                console.dir(b);
                if($scope.businesses.content[b].businessId==businessId){
                    $scope.business = $scope.businesses.content[b];
                    break;
                }
            }
            $scope.go("/business/businessedit");
        };


        //----[商户订单]---------------------
        //初始化商户订单页，获取第一页内容
        $scope.bzOrderInit = function() {
            console.log("====bzOrderInit====");
            $scope.businessOrdersSearchParas.page=0;
            $scope.businessOrdersSearchParas.businessId = $scope.business.businessId;
            $scope.bzOrderSearch();
        };

        //todo:未编码
        $scope.bzOrderSearch = function() {
            console.dir("====bzOrderSearch========");
            console.dir($scope.businessOrdersSearchParas);
            $http.post(urlBase+urlBzBase+urlBzOrder,$scope.businessOrdersSearchParas)
                .success( function(response) {
                    console.dir(response);
                    $scope.orders = response.content;
                });
        };

        $scope.bzOrderViewInit = function() {
            console.log("====bzOrderViewInit====");
            $http.get(urlBase+urlBzBase+urlBzSale+"/"+$scope.order.salesmanId)
                .success( function(response) {
                    console.dir(response);
                    $scope.saler = response.content;
                });
            $http.get(urlBase+urlBzBase+urlBzFindOne+"/"+$scope.order.seller)
                .success( function(response) {
                    console.dir(response);
                    $scope.business = response.content;
                });
        };

        //订单追踪初始化
        $scope.bzOrderTraceInit = function() {
            console.log("====bzOrderTraceInit====");
            $scope.bzOrderTraceSearch();
        };

        //订单追踪查询，根据订单ID查询
        $scope.bzOrderTraceSearch = function() {
            var orderid=$scope.order.orderId;
            $http.post(urlBase+urlBzBase+urlBzOrderTraces,{"orderId":orderid})
                .success( function(response) {
                    console.log("====bzOrderTraceSearch=ok===");
                    $scope.orderTraces = response.content;
                });
        };

        //根据指定OrderId，转移到订单浏览页面
        $scope.bzOrderToView = function(orderId) {
            console.log("====bzOrderToView===="+orderId);
            for (b in $scope.orders.content)
            {
                console.dir(b);
                if($scope.orders.content[b].orderId==orderId){
                    $scope.order = $scope.orders.content[b];
                    break;
                }
            }
            $scope.go("/business/orderview");
        };

        //根据指定OrderId，转移到订单浏览页面
        $scope.bzOrderToTrace = function() {
            $scope.go("/business/ordertrack");
        };


        //----[数据统计]---------------------
        //初始化列表数据，获取第一页统计数据。
        $scope.bzStatInit = function() {
            console.log("====bzStatInit====");
            $scope.businessStatisticSearchParas.businessId = $scope.business.businessId;
            $scope.businessStatisticSearchParas.page=0;
            $scope.bzStatSearch();
        };

        //根据搜索参数获取商户统计信息
        //todo:添加相关参数
        $scope.bzStatSearch = function() {
            console.log("====bzStatSearch====");
            console.dir($scope.businessStatisticSearchParas);

            $http.post(urlBase+urlBzBase+urlBzStatisticSearch,$scope.businessStatisticSearchParas)
                .success( function(response) {
                    console.dir(response);
                    $scope.businessStatistic = response.content;
            });
        };

    }
    var playCount=0;
    function playClicked()
    {

        playCount++;
        if(playCount%4 !=1)
            return;

        audio_player = document.getElementById('audio_player');
        //get the state of the player
        if(audio_player.paused)
        {
            audio_player.play();
            newdisplay = "| |";
        }else{
            audio_player.pause();
            newdisplay = ">";
        }
        //element.value=newdisplay;
    }
})();


