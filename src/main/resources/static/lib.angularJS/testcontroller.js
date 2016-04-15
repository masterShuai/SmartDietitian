(function () {

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
            .when('/user', 'user')
            .when('/app', 'app')
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
            //----business.list.*----
            //.within()
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
            //.up()
            .segment('static', {
                templateUrl: '/views/business.static.html'
            })

            .root()
            .segment('app', {
                templateUrl: '/views/app.html',
                controller: AppController
            });
    });

    //控制器配置信息
    app.controller('PageController', ['$scope', '$http', "$location", PageController]);
    app.controller('OrderController', ['$scope', '$http', OrderController]);
    app.controller('SalesController', ['$scope', '$http', SalesController]);
    app.controller('UserController', ['$scope', '$http', UserController]);
    app.controller('AppController', ['$scope', '$http', AppController]);
    app.controller('BusinessController', ['$scope', '$http', '$upload', BusinessController]);

    //url配置信息
    var urlBase = "http://127.0.0.1:8090/api";

    var urlHelper = function (module, urlDesc) {
        $scope.menuState.show = !$scope.menuState.show;
    };

    //主页面控制器
    function PageController($scope, $http, $location) {
        //
        $scope.menuState = {
            show: false
        };
        $scope.toggleMenu = function () {
            $scope.menuState.show = !$scope.menuState.show;
        };

        $scope.go = function (urlToGo) {
            //$location.hash(urlToGo);
            $location.path(urlToGo);
        };


        //父控制器调度事件的代码。保留给controller之间通信用的事件。会在接受之后广播给所有子控制器。
        //$scope.$on("Ctr1NameChange",
        //    function (event, msg) {
        //        console.log("parent", msg);
        //        $scope.$broadcast("Ctr1NameChangeFromParrent", msg);
        //    });

        //原始发出事件的代码
        //controller("childCtr1", function ($scope) {
        //    $scope.change = function (name) {
        //        console.log("childCtr1", name);
        //        $scope.$emit("Ctr1NameChange", name);
        //    };
        //})
        //接收事件的代码
        //$scope.$on("Ctr1NameChangeFromParrent",
        //    function (event, msg) {
        //        console.log("childCtr2", msg);
        //        $scope.ctr1Name = msg;
        //    });

        $scope.lbsProvince = ['北京市', '天津市', '河北省', '山西省', '内蒙古自治区', '辽宁省', '吉林省', '黑龙江省', '上海市', '江苏省', '浙江省', '安徽省', '福建省', '江西省', '山东省', '河南省', '湖北省', '湖南省', '广东省', '广西壮族自治区', '海南省', '重庆市', '四川省', '贵州省', '云南省', '西藏自治区', '陕西省', '甘肃省', '青海省', '宁夏回族自治区', '新疆维吾尔自治区', '台灣', '香港特别行政区', '澳门特别行政区'];
        $scope.lbsdistrict = [{id: "", value: "所有区域"}, {id: "黄浦区", value: "黄浦区"}, {id: "徐汇区", value: "徐汇区"}, {
            id: "长宁区",
            value: "长宁区"
        }, {id: "静安区", value: "静安区"}, {id: "普陀区", value: "普陀区"},
            {id: "闸北区", value: "闸北区"}, {id: "虹口区", value: "虹口区"}, {id: "杨浦区", value: "杨浦区"}, {
                id: "闵行区",
                value: "闵行区"
            }, {id: "宝山区", value: "宝山区"},
            {id: "嘉定区", value: "嘉定区"}, {id: "浦东新区", value: "浦东新区"}, {id: "金山区", value: "金山区"}, {
                id: "松江区",
                value: "松江区"
            }, {id: "青浦区", value: "青浦区"},
            {id: "奉贤区", value: "奉贤区"}, {id: "崇明县", value: "崇明县"}];
        $scope.orderstates = [{id: "", value: "所有"}, {id: "待接单", value: "待接单"}, {id: "已接单", value: "已接单"}, {
            id: "取货中",
            value: "取货中"
        }, {id: "跑腿中", value: "跑腿中"}, {id: "已完成", value: "已完成"}];
        $scope.businessLabels = [];
    }


    //订单控制器
    function OrderController($scope, $http) {
        var urlOrderBase = "/api/v1/s";
        var urlSalerRegister = "/register";
        var urlSalerstartWork = "/startWork";
        var urlSalerstopWork = "/stopWork";
        var urlSalerOrderlistnew = "/order/listnew";
        var urlSalerorderlistrunning = "/order/listrunning";
        var urlSalerorderaccept = "/order/accept";
        var urlSalerorderconfirm = "/order/confirm";
        var urlSalerordercomplete = "/order/complete";

        var urlOrderAll = "/orders";
        var urlOrderOne = "/order";
        //data
        $scope.orders = [
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
        $scope.order = {
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
        //functions
        $scope.postOrder = function () {
            $http.get(urlBase + urlOrderBase + urlOrderOne)
                .success(function (response) {
                    $scope.order = response.content;
                });
        };

        $scope.getOrders = function () {
            $http.get(urlBase + urlOrderBase + urlOrderAll)
                .success(function (response) {
                    $scope.orders = response.content;
                });
        };

        $scope.init = function () {
            console.log("init---------");
            $scope.getOrders();
        };

    }


    //业务员控制器
    function SalesController($scope, $http) {
        var urlOrderBase = "/api/v1/s";
        var urlSalerRegister = "/register";
        var urlSalerstartWork = "/startWork";
        var urlSalerstopWork = "/stopWork";
        var urlSalerOrderlistnew = "/order/listnew";
        var urlSalerorderlistrunning = "/order/listrunning";
        var urlSalerorderaccept = "/order/accept";
        var urlSalerorderconfirm = "/order/confirm";
        var urlSalerordercomplete = "/order/complete";
        //data
        $scope.orders = [
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
        $scope.order = {
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
        $scope.postOrder = function () {
            $http.get(urlBase + urlOrderBase + urlOrderOne).success(function (response) {
                $scope.order = response;
            });
        };
        $scope.getOrders = function () {
            $http.get(urlBase + urlOrderBase + urlOrderAll).success(function (response) {
                $scope.orders = response;
            });
        };
    }

    //业务员控制器
    function UserController($scope, $http) {
        var urlOrderBase = "/business";
        var urlOrderAll = "/all";
        var urlOrderOne = "/order";
        //data
        $scope.orders = [
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
        $scope.order = {
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
        $scope.postOrder = function () {
            $http.get(urlBase + urlOrderBase + urlOrderOne).success(function (response) {
                $scope.order = response;
            });
        };
        $scope.getOrders = function () {
            $http.get(urlBase + urlOrderBase + urlOrderAll).success(function (response) {
                $scope.orders = response;
            });
        };
    }


    //业务员控制器
    function AppController($scope, $http) {
        var urlOrderBase = "/business";
        var urlOrderAll = "/all";
        var urlOrderOne = "/order";
        //data
        $scope.orders = [
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
        $scope.order = {
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
        $scope.postOrder = function () {
            $http.get(urlBase + urlOrderBase + urlOrderOne).success(function (response) {
                $scope.order = response;
            });
        };
        $scope.getOrders = function () {
            $http.get(urlBase + urlOrderBase + urlOrderAll).success(function (response) {
                $scope.orders = response;
            });
        };
    }

    //商户控制器
    function BusinessController($scope, $http) {
        var urlBzBase = "/business";
        var urlBzFindOne = "/get";
        var urlBzSearch = "/search";
        var urlBzStatisticSearch = "/statisticSearch";
        var urlBzNew = "/post";
        var urlBzOrder = "/orders";
        var urlBzOrderTraces = "/traces";
        var urlBzSale = "/saler";
        var urlBzSec = "/secpost";
        //====[data]====
        //----[商户]----
        $scope.businessSearchParas = {
            page: 0,
            size: 5,
            businessId: "",
            businessArea: ""
        };

        $scope.businesses = {
            first: false,
            last: false,
            number: 0,
            numberOfElements: 0,
            size: 20,
            totalElements: 0,
            totalPages: 1,
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
                    businessLicenseId: "",
                    contactPersonId: "",
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
            businessLicenseId: "3401111111111111",
            contactPersonId: "3401111111111111",
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
            businessLicenseId: "3401111111111111",
            contactPersonId: "3401111111111111",
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
            businessLicenseId: "",
            contactPersonId: "",
            password: null,
            address: "",
            type: "",
            state: "",
            name: "",
            labels: ""
        };

        //----[商户订单]---------------------
        $scope.businessOrdersSearchParas = {
            page: 0,
            size: 20,
            businessId: "",
            businessStartDate: "",
            businessEndDate: "",
            businessState: ""
        };

        $scope.orders = {
            first: false,
            last: false,
            number: 0,
            numberOfElements: 0,
            size: 20,
            totalElements: 0,
            totalPages: 1,
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
                    "cancelCon": "S",
                    "sendName": "哈哈镜"
                }
            ]
        };
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
        $scope.orderTraces = {
            content: []
        };
        $scope.saler = {
            salesmanId: "",
            picheader: "",
            sex: "",
            level: "",
            phone: "",
            userName: "",
            age: 0
        };

        //----[商户统计]---------------------
        $scope.businessSecurityParamater = {
            businessId: "",
            newPassword: ""
        };
        $scope.businessPunish = {
            punishType: "",
            punishDay: 0,
            reason: ""
        };
        $scope.businessPunishHistory = [];
        $scope.businessStatisticSearchParas = {
            page: 0,
            size: 20,
            businessId: "",
            businessStartDate: "",
            businessEndDate: "",
            businessArea: ""
        };

        $scope.businessStatistic = {
            content: [
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
            ]
        };

        //====[function]====
        //----[商户]---------------------
        $scope.bzInit = function () {
            console.log("bzInit---------");
            $scope.bzGetFirstPage();
        };

        $scope.bzReset = function () {
            $scope.businessnew = $scope.businessEmpty;
        };

        //获取全部商户信息
        $scope.bzGetFirstPage = function () {
            //console.log("====bzGetFirstPage=ok==="+response.content);
            $scope.businessSearchParas.page = 0;
            $scope.businessSearchParas.size = 20;
            $scope.businessSearchParas.businessId = "";
            $scope.bzSearch();
        };

        //获取全部商户信息
        $scope.bzSearch = function () {
            console.dir($scope.businessSearchParas);
            $http.post(urlBase + urlBzBase + urlBzSearch, $scope.businessSearchParas)
                .success(function (response) {
                    console.dir(response);
                    $scope.businesses = response.content;
                });
        };

        //提交一个新的商户信息
        $scope.bzNewPost = function () {
            $http.post(urlBase + urlBzBase + urlBzNew, $scope.businessnew)
                .success(function (response) {
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

        //提交一个新的商户信息
        $scope.bzEditPost = function () {
            $http.post(urlBase + urlBzBase + urlBzNew, $scope.business)
                .success(function (response) {
                    $scope.getAllBusiness();
                    console.log(response);
                    $scope.go("/business/businesslist");
                });
        };

        //提交发送随机密码给商户
        $scope.bzSecurityRandom = function () {
            console.log("bzSecurityRandom---------");
            $scope.businessSecurityParamater.newPassword = "";
            $scope.bzSecurity();
        };

        //提交发送密码给商户
        $scope.bzSecurity = function () {
            console.log("bzSecurity---------");
            $http.post(urlBase + urlBzBase + urlBzSec, $scope.businessSecurityParamater)
                .success(function (response) {
                    console.log(response);
                });
        };


        //初始化浏览视图需要的数据
        $scope.bzView = function (businessId) {
            console.log("====bzView====");
            console.dir(businessId);
            for (b in $scope.businesses.content) {
                console.dir(b);
                if ($scope.businesses.content[b].businessId == businessId) {
                    $scope.business = $scope.businesses.content[b];
                    break;
                }
            }
            $scope.go("/business/businessview");
        };

        $scope.bzToEdit = function (businessId) {
            console.log("====bzToEdit====" + businessId);
            for (b in $scope.businesses.content) {
                console.dir(b);
                if ($scope.businesses.content[b].businessId == businessId) {
                    $scope.business = $scope.businesses.content[b];
                    break;
                }
            }
            $scope.go("/business/businessedit");
        };


        //----[商户订单]---------------------
        //初始化商户订单页，获取第一页内容
        $scope.bzOrderInit = function () {
            console.log("====bzOrderInit====");
            $scope.businessOrdersSearchParas.page = 0;
            $scope.businessOrdersSearchParas.businessId = $scope.business.businessId;
            $scope.bzOrderSearch();
        };

        //todo:未编码
        $scope.bzOrderSearch = function () {
            console.dir("====bzOrderSearch========");
            console.dir($scope.businessOrdersSearchParas);
            $http.post(urlBase + urlBzBase + urlBzOrder, $scope.businessOrdersSearchParas)
                .success(function (response) {
                    console.dir(response);
                    $scope.orders = response.content;
                });
        };

        $scope.bzOrderViewInit = function () {
            console.log("====bzOrderViewInit====");
            $http.get(urlBase + urlBzBase + urlBzSale + "/" + $scope.order.salesmanId)
                .success(function (response) {
                    console.dir(response);
                    $scope.saler = response.content;
                });
            $http.get(urlBase + urlBzBase + urlBzFindOne + "/" + $scope.order.seller)
                .success(function (response) {
                    console.dir(response);
                    $scope.business = response.content;
                });
        };

        //订单追踪初始化
        $scope.bzOrderTraceInit = function () {
            console.log("====bzOrderTraceInit====");
            $scope.bzOrderTraceSearch();
        };

        //订单追踪查询，根据订单ID查询
        $scope.bzOrderTraceSearch = function () {
            var orderid = $scope.order.orderId;
            $http.post(urlBase + urlBzBase + urlBzOrderTraces, {"orderId": orderid})
                .success(function (response) {
                    console.log("====bzOrderTraceSearch=ok===");
                    $scope.orderTraces = response.content;
                });
        };

        //根据指定OrderId，转移到订单浏览页面
        $scope.bzOrderToView = function (orderId) {
            console.log("====bzOrderToView====" + orderId);
            for (b in $scope.orders.content) {
                console.dir(b);
                if ($scope.orders.content[b].orderId == orderId) {
                    $scope.order = $scope.orders.content[b];
                    break;
                }
            }
            $scope.go("/business/orderview");
        };

        //根据指定OrderId，转移到订单浏览页面
        $scope.bzOrderToTrace = function () {
            $scope.go("/business/ordertrack");
        };


        //----[数据统计]---------------------
        //初始化列表数据，获取第一页统计数据。
        $scope.bzStatInit = function () {
            console.log("====bzStatInit====");
            $scope.businessStatisticSearchParas.businessId = $scope.business.businessId;
            $scope.businessStatisticSearchParas.page = 0;
            $scope.bzStatSearch();
        };

        //根据搜索参数获取商户统计信息
        //todo:添加相关参数
        $scope.bzStatSearch = function () {
            console.log("====bzStatSearch====");
            console.dir($scope.businessStatisticSearchParas);

            $http.post(urlBase + urlBzBase + urlBzStatisticSearch, $scope.businessStatisticSearchParas)
                .success(function (response) {
                    console.dir(response);
                    $scope.businessStatistic = response.content;
                });
        };

    }

})();


