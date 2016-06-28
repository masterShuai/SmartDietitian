var mainApp = angular.module("test", []);

mainApp.controller('PhoneListCtrl', function ($scope, $http) {

    $scope.urls = {
        allNutrition: "/Search/Nutrition/all"
        , nutritionById: "/Search/Nutrition/byId"
        , nutritionByName: "/Search/Nutrition/byName"
        , allFood: "/Search/Food/all"
        , foodById: "/Search/Food/byId"
        , foodByName: "/Search/Food/byName"
        , register: "/User/register"
        , login: "/User/login"
        , addCooking: "/Search/Cooking/save"
        , addSomeCooking: "/Search/Cooking/saveMany"
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
        "foodId": 1,
        "foodName": ""
    };
    //请求菜品
    $scope.sendCookingData =
    {
        "cooking": 1,
        "cookingName": ""
    };
    //请求登录
    $scope.sendLogin = {"id": "wangshuai", "passWord": "1234"};
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
    };


    //请求添加菜品
    var newCooking = {
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
                "foodId": "",
                "foodName": "",
                "content": 0

        }]
    };

    var newCookingList =[
        //{
        //    "id": 1,
        //    "name": "白灼虾",
        //    "otherName": "",
        //    "taste": "咸",
        //    "kind": "水产",
        //    "style": "粤菜",
        //    "feature": "灼虾是广东省广州地区一道汉族传统名菜，属粤菜系。主要食材是虾，主要烹饪工艺是白灼，“白灼”二字指的是将原滋原味的鲜虾直接放进清水里煮食。广州人喜欢用白灼之法来做虾，为的是保持其鲜、甜、嫩的原味，然后将虾剥壳蘸酱汁而食。",
        //    "howToCook": "锅入少油，加入蒜和姜片稍煎香，在锅中加入半锅水，加入其它A料将水煮开。将虾去掉虾线，剪虾须、虾脚后，下入锅中煮制大约煮1分钟左右的样子，虾变红色后关火蒜压蓉，小葱切花，姜切细粒锅入B料的油烧七八分热，油要冒点烟，淋入调料碗中激出葱蒜的香味，再加入其他B料。将虾肉剥好蘸食吃",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "982",
        //        "foodName": "",
        //        "content": 500
        //
        //    },{
        //        "foodId": "246",
        //        "foodName": "",
        //        "content": 15
        //
        //    },{
        //        "foodId": "296",
        //        "foodName": "",
        //        "content": 15
        //
        //    },{
        //        "foodId": "245",
        //        "foodName": "",
        //        "content": 15
        //
        //    },{
        //        "foodId": "194",
        //        "foodName": "",
        //        "content": 3
        //
        //    },{
        //        "foodId": "1309",
        //        "foodName": "",
        //        "content": 10
        //
        //    },{
        //        "foodId": "1282",
        //        "foodName": "",
        //        "content": 15
        //
        //    },{
        //        "foodId": "1233",
        //        "foodName": "",
        //        "content": 5
        //
        //    }]
        //},
        //{
        //    "id": 2,
        //    "name": "水煮鱼",
        //    "otherName": "",
        //    "taste": "辣",
        //    "kind": "水产",
        //    "style": "川菜",
        //    "feature": "水煮鱼，又称“江水煮江鱼”，是重庆渝北风味，看似原始的做法，味道却不一般，水煮鱼选新鲜生猛活鱼，又充分发挥辣椒、椒麻益气养血功效，做出来的鱼肉一点也不会变韧，口感滑嫩，油而不腻。既去除了鱼的腥味，又保持了鱼的鲜嫩。满目的辣椒红亮养眼，辣而不燥，麻而不苦。“麻上头，辣过瘾”，让水煮鱼在全国流行得一塌糊涂，变成了人人都热爱的一道名菜。 ",
        //    "howToCook": "草鱼洗干净，沿鱼骨取下肉，切成连刀片。用白酒、蛋清、盐、胡椒粉、淀粉抓匀备用热锅，下麻油，炒香豆皮和油菜，盛在碗中冷锅凉油慢慢炒热郫县豆酱，加入花椒大料桂皮小茴香麻椒、葱姜蒜、辣椒炒香，下入鱼骨和鱼头，炒到变色后添水烧开，转文火煮10分钟下入腌好的鱼片，鱼片变白扶起后，加麻油、盐调味即可。把蒸锅的鱼连同汤一同倒入装豆皮油菜的大碗中最后用锅子加热100克左右的油，至冒烟，趁热浇在鱼片上",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "904",
        //        "foodName": "",
        //        "content": 1500
        //    },{
        //        "foodId": "1104",
        //        "foodName": "",
        //        "content": 300
        //    },{
        //        "foodId": "300",
        //        "foodName": "",
        //        "content": 300
        //    },{
        //        "foodId": "1272",
        //        "foodName": "",
        //        "content": 30
        //    },{
        //        "foodId": "296",
        //        "foodName": "",
        //        "content": 15
        //    },{
        //        "foodId": "194",
        //        "foodName": "",
        //        "content": 15
        //    },{
        //        "foodId": "854",
        //        "foodName": "",
        //        "content": 50
        //    },{
        //        "foodId": "1280",
        //        "foodName": "",
        //        "content": 15
        //    },{
        //        "foodId": "1258",
        //        "foodName": "",
        //        "content": 15
        //    }]
        //},
        //{
        //    "id": 3,
        //    "name": "切糕",
        //    "otherName": "",
        //    "taste": "甜",
        //    "kind": "小吃",
        //    "style": "京菜",
        //    "feature": "切糕为北京著名小吃由糯米和黄米面制成的糕，多和以红枣和豆沙，刀切零售，故此而得名。 ",
        //    "howToCook": "将糯米红枣淘洗4小时以上糯米控干水分入锅，红枣同时进行蒸制蒸制10-12分钟左右开盖，倒入开水，边倒开水边搅拌，使糯米充分吸收水分；待糯米完全被搅拌成糨粥状，盖盖子继续蒸制15分钟。取出糯米饭、红枣，将糯米饭沾水揉匀，用铲子按成厚薄相当的层片。均匀的码上一层红枣达到三层饭二层枣，层次分明，饭枣分开。重物压于其上，将糯米饭压瓷实待切糕放凉，倒扣下来。用刀顺边从上往下切，放在盘内撒上白糖即可。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "50",
        //        "foodName": "",
        //        "content": 200
        //
        //    },{
        //        "foodId": "567",
        //        "foodName": "",
        //        "content": 200
        //
        //    }]
        //},
        //{
        //    "id": 4,
        //    "name": "木瓜鲜奶冻",
        //    "otherName": "",
        //    "taste": "甜",
        //    "kind": "小吃",
        //    "style": "",
        //    "feature": "木瓜鲜奶冻是一道由木瓜等食材制成的食品。",
        //    "howToCook": "首先，大约50g淡奶油+100g牛奶倒入锅内 其次 处理吉利丁片：如果小半只木瓜就取一片吉利丁片就可以了 如果大半只就1.5片 整只就2片 将吉利丁片放在冷水里软化 大约1分钟就软了将软化后的吉利丁片取出放入锅内 开小火 和淡奶油以及牛奶均匀搅拌 大约2分钟 锅稍热 搅拌完全即可关火切小半只木瓜 将中间的木瓜籽用不锈钢汤勺挖出 尽量挖的深一点 口圆一点 这样切出来的会很好看液体倒入 倒满木瓜放入冰箱 冷藏至少3小时 取出 切块",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "327",
        //        "foodName": "",
        //        "content": 500
        //
        //    },{
        //        "foodId": "817",
        //        "foodName": "",
        //        "content": 50
        //
        //    },{
        //        "foodId": "825",
        //        "foodName": "",
        //        "content": 100
        //
        //    }]
        //},
        //{
        //    "id": 5,
        //    "name": "韭菜猪肉水饺",
        //    "otherName": "",
        //    "taste": "咸",
        //    "kind": "",
        //    "style": "",
        //    "feature": "",
        //    "howToCook": "剁好猪肉后，什么调料都不用放，就只放蠔油，搅拌均匀。。韭菜切碎，马蹄切粒，放入肉馅中拌均匀，然后视肉馅多少来决定放一只还是两只鸡蛋，再拌均匀就可以了。。或者你可以将馅料放置一会儿再包也行，那样更入味一些。。包好可以做蒸饺、水饺饺子皮平放手板心，右手食指和拇指在中心线捏一下，捏紧左手食指轻轻推一个皱褶去右手食指的位置，然后用右手两个手指把皱褶捏紧",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "262",
        //        "foodName": "",
        //        "content": 500
        //
        //    },{
        //        "foodId": "854",
        //        "foodName": "",
        //        "content": 100
        //
        //    },{
        //        "foodId": "723",
        //        "foodName": "",
        //        "content": 500
        //
        //    }]
        //},
        //{
        //    "id": 6,
        //    "name": "糖醋排骨",
        //    "otherName": "",
        //    "taste": "甜",
        //    "kind": "肉类",
        //    "style": "沪菜",
        //    "feature": "糖醋排骨，这个做法不需要炒糖色",
        //    "howToCook": "排骨剁成小块和凉水一起下锅，大火煮开后再煮3分钟，撇净浮沫后将排骨捞出沥干水。葱姜蒜切大块。料酒/白砂糖/香醋/生抽加一小碗清水调匀。放入排骨，继续中小火翻炒至排骨表面微焦。将之前调好的料汁倒入锅中，料汁的水平面超过排骨即可。如果料汁过少，可以再加点清水。将火调大，收干汤汁后撒一些白芝麻，炒匀后关火装盘",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "738",
        //        "foodName": "",
        //        "content": 500
        //    },{
        //        "foodId": "1233",
        //        "foodName": "",
        //        "content": 20
        //    },{
        //        "foodId": "1270",
        //        "foodName": "",
        //        "content": 30
        //    },{
        //        "foodId": "1282",
        //        "foodName": "",
        //        "content": 40
        //    }]
        //},
        {
            "id": 7,
            "name": "香辣小龙虾",
            "otherName": "",
            "taste": "辣",
            "kind": "水产",
            "style": "沪菜",
            "feature": "麻辣小龙虾又叫长沙口味虾、香辣小龙虾等，是湖南省著名的汉族小吃，以小龙虾制成，口味辣鲜香，色泽红亮，质地滑嫩，滋味香辣。",
            "howToCook": "先将小龙虾用淡盐水浸泡1～2个小时，剪去全部鳌足，用牙刷清洗腹部及外壳；用手捏住尾巴中间那块尾甲，掐断并拽出虾肠；用剪刀减去头部（靠近虾嘴）的三分之一，挖出里面的黑色囊状物虾胃，冲洗后沥水；准备好葱段、姜片、蒜瓣、花椒和干辣椒；事先将蒸鱼豉汁、生抽、陈醋、白糖、盐调制成碗汁备用；先将花椒粒放入油锅中小火炒香，捡出花椒不要；用花椒油爆香葱段、姜片、蒜瓣和干辣椒，倒入小龙虾一起翻炒，加入白酒/料酒蒸发出酒味；炒至小龙虾变红时倒入调好的碗汁，加半碗清水大火煮开，然后加盖中火焖6、7分钟使其入味，看汤汁略收干即可盛出。",
            "authorId": "",
            "nutritionContent": [{
                "nutritionId": "",
                "nutritionName": "",
                "content": 0,
                "nutritionUnit": ""
            }],
            "foodContent": [{
                "foodId": "993",
                "foodName": "",
                "content": 300

            },{
                "foodId": "296",
                "foodName": "",
                "content": 15

            },{
                "foodId": "194",
                "foodName": "",
                "content": 20

            },{
                "foodId": "248",
                "foodName": "",
                "content": 20

            },{
                "foodId": "346",
                "foodName": "",
                "content": 50

            },{
                "foodId": "1275",
                "foodName": "",
                "content": 10

            },{
                "foodId": "1177",
                "foodName": "",
                "content": 30

            }]
        },
        //{
        //    "id": 0,
        //    "name": "鸡蛋布丁",
        //    "otherName": "",
        //    "taste": "甜",
        //    "kind": "小吃",
        //    "style": "",
        //    "feature": "甜品可以满足味蕾的需求，让人心情大好用最简单的鸡蛋、鲜奶做成的一道没有防腐剂的营养点心食用蛋类，可以补充奶类中铁的匮，蛋中的磷很丰富，但钙相对不足，所以，将奶类与鸡蛋，就可以做到营养互补。",
        //    "howToCook": "准备好做鸡蛋布丁45g白砂糖，备用，可以根据个人喜爱口味有所增减将45g白砂糖加入牛奶中，搅拌至白砂糖完全融化则可【将盛牛奶的容器放入热水中有助于白砂糖的融化】将鸡蛋完全打匀至微微起泡，加入少许柠檬汁，蛋白不挂在筷子或打蛋器上则完成加入牛奶后的拌匀奶液进行过筛，筛2-3次即可。没有过筛器皿可以选择煲汤袋，重点是隔掉没有完全打匀的蛋清。烤箱预热170度 10分钟。将调制好的奶液均匀倒入布丁瓶或可以进烤箱的容器中封口铺上锡纸可以防止烤好的布丁长气泡，放置在加有适量热水的烤盘中。170度烤40分钟。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "854",
        //        "foodName": "",
        //        "content": 150
        //    },{
        //        "foodId": "821",
        //        "foodName": "",
        //        "content": 250
        //    },{
        //        "foodId": "1233",
        //        "foodName": "",
        //        "content": 50}
        //    ]
        //},
        {
            "id": 9,
            "name": "回锅肉",
            "otherName": "",
            "taste": "咸",
            "kind": "肉类",
            "style": "川菜",
            "feature": "回锅肉，就不用介绍了吧，四川人家家会做啊，我觉得普及程度应该跟番茄炒蛋差不多吧，听说川菜厨子考试第一道菜就是回锅肉呢。至于评判标准呢，听说是两个：一是肉片下锅爆炒，必须熬至肉片呈茶船状，成都人说：“熬起灯盏窝儿了”；二是肉片的大小是筷子夹起时会不断抖动。达不到上述两个标准， 必是失败的回锅肉。",
            "howToCook": "先将肉入冷水锅，放入姜片，到一大勺料酒，大火将水烧开，然后转中火，煮半小时左右。等到用筷子戳进去能扎得透，且没有血水流出，即可关火。将煮好的肉晾凉，切成薄片，这里是考验刀工的地方啦，太薄了，一炒就碎，太厚了，炒不出灯盏窝儿。（可以放到冷冻室里急冻个五分钟，这样比较好切）将青蒜斜刀切成寸长段。炒锅倒油，大火将锅烧热，将切好的肉片下锅，翻炒至肉片卷曲，肥肉透明最好，这样猪肉的油都被炒出来了，吃起来不腻。然后把肉推动锅的一边，倒入花椒粒，豆瓣酱翻炒，等看到油的颜色变红，就可以把肉扒拉过来炒匀了。倒入青蒜，料酒、盐、糖、生抽，翻炒均匀即可。",
            "authorId": "",
            "nutritionContent": [{
                "nutritionId": "",
                "nutritionName": "",
                "content": 0,
                "nutritionUnit": ""
            }],
            "foodContent": [{
                "foodId": "722",
                "foodName": "",
                "content": 300
            },{
                "foodId": "1272",
                "foodName": "",
                "content": 20
            },{
                "foodId": "1306",
                "foodName": "",
                "content": 40
            },{
                "foodId": "245",
                "foodName": "",
                "content": 25
            }]
        },
        //{
        //    "id": 10,
        //    "name": "虾仁菠萝炒饭",
        //    "otherName": "",
        //    "taste": "甜",
        //    "kind": "主食",
        //    "style": "",
        //    "feature": "虾仁菠萝炒饭",
        //    "howToCook": "直立从1/3处于剖开菠萝 。用水果刀沿着菠萝的四边划开，将菠萝芯用刀划成小块，用铁勺挖出果肉。菠萝果肉尽量沥去果汁。菠萝空壳放入烤箱烤到干一点，防止菠萝一直出水。160度大概5-8分钟，可以根据自己的情况定~剥好青豆和虾仁，甜椒切小块，香肠切片，菠萝取适量也切小块。热油，下青豆、甜椒和香肠，炒到4分熟的时候，下虾仁，加耗油、生抽、盐，翻炒几下。下米饭（米饭提前压碎），快速翻炒。最后下菠萝，再翻炒两下就可以上锅了。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "423",
        //        "foodName": "",
        //        "content": 1000
        //    },{
        //        "foodId": "353",
        //        "foodName": "",
        //        "content": 50
        //    },{
        //        "foodId": "817",
        //        "foodName": "",
        //        "content": 50
        //    },{
        //        "foodId": "1282",
        //        "foodName": "",
        //        "content": 15
        //    },{
        //        "foodId": "1309",
        //        "foodName": "",
        //        "content": 20
        //    }]
        //},
        //{
        //    "id": 11,
        //    "name": "日式蛋包饭",
        //    "otherName": "",
        //    "taste": "咸",
        //    "kind": "主食",
        //    "style": "",
        //    "feature": "使用鸡肉的日式蛋包饭",
        //    "howToCook": "鸡肉，洋葱，蘑菇切1cm小丁，牛肉汤块切片青豆过水鸡蛋两个打散，加入一勺淡奶油或者奶锅中加少许油，倒入蒜末加入洋葱末翻炒洋葱炒至发黄变色，加入鸡肉丁鸡肉变色，加入蘑菇加入牛肉汤块和一片香叶翻炒至汤汁基本收干加入米饭翻炒均匀盛出备用",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "759",
        //        "foodName": "",
        //        "content": 500
        //
        //    },{
        //        "foodId": "240",
        //        "foodName": "",
        //        "content": 50
        //
        //    },{
        //        "foodId": "403",
        //        "foodName": "",
        //        "content": 50
        //
        //    },{
        //        "foodId": "854",
        //        "foodName": "",
        //        "content": 100
        //
        //    },{
        //        "foodId": "817",
        //        "foodName": "",
        //        "content": 20
        //
        //    }]
        //},
        //{
        //    "id": 12,
        //    "name": "肉末茄子",
        //    "otherName": "",
        //    "taste": "咸",
        //    "kind": "",
        //    "style": "",
        //    "feature": "肉末茄子",
        //    "howToCook": "茄子洗净，切块。猪肉切成肉末，加入芡粉、姜末、盐、少量水，码味上浆。放入大量油，开始炸茄子。炸软后的茄子是酱紫的，其实锅内的油并没有少太多。茄子捞起备用。将多余的油倒出，剩少许油用于炒料。加入一勺豆瓣酱，蒜片，小火煸炒出香味。倒入腌制后的肉末，炒香。再加入炸过的茄子，此时加入少许生抽老抽，撒一丢丢盐，大火翻炒30秒，火速出锅。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "350",
        //        "foodName": "",
        //        "content": 500
        //
        //    },{
        //        "foodId": "726",
        //        "foodName": "",
        //        "content": 50
        //
        //    },{
        //        "foodId": "283",
        //        "foodName": "",
        //        "content": 15
        //
        //    },{
        //        "foodId": "1272",
        //        "foodName": "",
        //        "content": 15
        //
        //    }]
        //},
        //{
        //    "id": 13,
        //    "name": "孜然土豆片",
        //    "otherName": "",
        //    "taste": "辣",
        //    "kind": "小吃",
        //    "style": "川菜",
        //    "feature": "孜然土豆片",
        //    "howToCook": "把土豆切成2mm左右的薄片，先用清水泡5分钟左右，淀粉才会出来，不然炒出来的土豆很溶，是一坨。洋葱切成小块。切洋葱的时候先对半切开，然后每一半均匀的分成3份，再从中间切开，就会变成这样啦。热油，油要是平时炒菜的2倍，不然土豆会粘锅。把土豆炒至变色，起锅放至一旁。热油，放入洋葱翻炒至微香。接着倒入准备好的调料。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "207",
        //        "foodName": "",
        //        "content": 200
        //
        //    },{
        //        "foodId": "348",
        //        "foodName": "",
        //        "content": 50
        //
        //    },{
        //        "foodId": "194",
        //        "foodName": "",
        //        "content": 10
        //
        //    },{
        //        "foodId": "1309",
        //        "foodName": "",
        //        "content": 10
        //
        //    },{
        //        "foodId": "1282",
        //        "foodName": "",
        //        "content": 15
        //
        //    }]
        //},
        //{
        //    "id": 14,
        //    "name": "鸡蛋糕",
        //    "otherName": "",
        //    "taste": "甜",
        //    "kind": "小吃",
        //    "style": "",
        //    "feature": "老式鸡蛋糕在中国已经做了很多年了。最简单的三种原料，做出比较有实在口感的蛋糕。和戚风蛋糕完全不同，虽然如此，但也得到了很多人的喜爱。",
        //    "howToCook": "把材料准备好，鸡蛋必须40度左右，全蛋打散，放在无水无油的盘子中，加入细砂糖，开高速搅打到体积膨大，颜色发白。转低速搅打到细腻，无大泡。划过有花纹，不易消失。加入过筛的低筋面粉。自右上方下铲，划过盆底，自左上方捞出，将面粉拉起，转动盆，这个方式搅拌至无干粉，装入模具中，面上加入芝麻点缀！烤箱预热170度，将烤盘放入中层，烤25分钟左右",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "45",
        //        "foodName": "",
        //        "content": 100
        //
        //    },{
        //        "foodId": "1233",
        //        "foodName": "",
        //        "content": 85
        //
        //    },{
        //        "foodId": "854",
        //        "foodName": "",
        //        "content": 200
        //
        //    }]
        //},
        //{
        //    "id": 15,
        //    "name": "茄子豆角",
        //    "otherName": "",
        //    "taste": "咸",
        //    "kind": "",
        //    "style": "",
        //    "feature": "茄子豆角",
        //    "howToCook": "茄子洗净切成长条状，用清水浸泡片刻后捞出备用豆角洗净摘掉老筋切成长段，干辣椒切小段、大蒜拍破切碎锅中倒入适量油，油温7成热后下茄子，炸软捞出控油豆角，下油锅炸至表皮起皱，炒锅留少许底油，放入蒜末和干辣椒爆香放入茄子和豆角翻炒。盖上锅盖焖煮一会，待汤汁收浓即可。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "350",
        //        "foodName": "",
        //        "content": 200
        //
        //    },{
        //        "foodId": "164",
        //        "foodName": "",
        //        "content": 200
        //
        //    },{
        //        "foodId": "1013",
        //        "foodName": "",
        //        "content": 20
        //
        //    },{
        //        "foodId": "1282",
        //        "foodName": "",
        //        "content": 15
        //
        //    }]
        //},
        //{
        //    "id": 16,
        //    "name": "草莓酱",
        //    "otherName": "",
        //    "taste": "甜",
        //    "kind": "调味料",
        //    "style": "",
        //    "feature": "草莓酱",
        //    "howToCook": "草莓洗净去蒂切成1cm大小的方块加入白糖混匀用手捏软静置半小时后入锅，小火煮煮的过程中要经常搅拌防止粘锅，还要撇去浮沫煮差不多半小时，铲子上能挂上薄薄的酱加入柠檬汁继续煮十分钟左右，铲子上能挂上厚厚稠稠的酱，就可以装瓶了，放凉后放入冰箱冷藏",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "426",
        //        "foodName": "",
        //        "content": 350
        //
        //    },{
        //        "foodId": "1233",
        //        "foodName": "",
        //        "content": 50
        //
        //    }]
        //},
        {
            "id": 17,
            "name": "冰淇淋",
            "otherName": "",
            "taste": "甜",
            "kind": "小吃",
            "style": "",
            "feature": "无需搅拌的冰淇淋",
            "howToCook": "将两个鸡蛋的蛋白和蛋黄分离开，只需要蛋黄部分。把50克白糖放入蛋黄里，加入几滴柠檬汁或者几滴醋，用电动打蛋器搅拌均匀，直到蛋黄液颜色变浅黄色即可。把50g纯牛奶倒入小奶锅加热，小火微微加热，直到牛奶热到表面冒泡泡即可关火。趁热，把热牛奶慢慢倒入打好的蛋黄液，一边倒入一边快速搅拌，（注意，不能让蛋黄结块，结块就变成鸡蛋花了，影响口感）。然后再把这个蛋奶液倒回小奶锅，加入一点点盐。开小火微微加热，一边加热一边用筷子快速搅拌（注意，火越小越好，避免糊锅），直到液体微微煮开，即可关火。放凉待用。接下来，打发淡奶油，淡奶油打发到9分发，即提起打蛋器能出现垂直的尖尖角即可。把打发好的淡奶油倒入放凉的蛋奶液里，搅拌均匀，就是冰淇淋液咯，（如果想吃巧克力味，蔓越梅味，茉茶味或者各种水果口味的，这一步就可以添加进去，一起搅拌均匀，今天这款是加了蔓越梅口味的，味道非常赞。）然后把混合好的冰淇淋液倒入一个干净带盖子的容器里，放入冰箱冷冻室，冷冻三个小时以上",
            "authorId": "",
            "nutritionContent": [{
                "nutritionId": "",
                "nutritionName": "",
                "content": 0,
                "nutritionUnit": ""
            }],
            "foodContent": [{
                "foodId": "817",
                "foodName": "",
                "content": 200

            },{
                "foodId": "821",
                "foodName": "",
                "content": 50

            },{
                "foodId": "854",
                "foodName": "",
                "content": 100

            },{
                "foodId": "1233",
                "foodName": "",
                "content": 50

            }]
        }
        //, {
        //    "id": 18,
        //    "name": "鱼子刺身饭",
        //    "otherName": "",
        //    "taste": "淡",
        //    "kind": "水产",
        //    "style": "",
        //    "feature": "刺身饭里最喜爱的搭配莫过于海胆和三文鱼子。海胆的滑糯和鱼子的爆破，海胆的腥甜和鱼子的咸津，都在口中完美的舞出平衡。 ",
        //    "howToCook": "隔夜米饭用木铲抖松，铺在碗底。将海胆刺身和三文鱼子平铺在米饭上。可用少许海苔芝麻点缀。觉得海胆味腥的，可以用酱油和芥末拌在一起，沿海胆倒入碗中，或直接蘸刺身吃。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "1358",
        //        "foodName": "",
        //        "content": 100
        //
        //    }]
        //},
        //{
        //    "id": 19,
        //    "name": "黄焖鸡米饭",
        //    "otherName": "",
        //    "taste": "咸",
        //    "kind": "主食",
        //    "style": "湘菜",
        //    "feature": "黄焖鸡米饭",
        //    "howToCook": "所有工作开始之前，先准备一口高压锅，鸡块切块洗净控干水分放入高压锅中，香菇洗净切块放入高压锅中姜洗净切块放入高压锅中放入少量老抽放入生抽、盐、味精、料酒黄焖鸡炖好后，把尖椒切块放入锅中，和鸡块搅拌均匀。",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "768",
        //        "foodName": "",
        //        "content": 1000
        //
        //    },{
        //        "foodId": "414",
        //        "foodName": "",
        //        "content": 50
        //
        //    },{
        //        "foodId": "1309",
        //        "foodName": "",
        //        "content": 20
        //
        //    },{
        //        "foodId": "1282",
        //        "foodName": "",
        //        "content": 15
        //
        //    },{
        //        "foodId": "346",
        //        "foodName": "",
        //        "content": 50
        //
        //    }]
        //},
        //{
        //    "id": 0,
        //    "name": "清蒸鲈鱼",
        //    "otherName": "",
        //    "taste": "淡",
        //    "kind": "水产",
        //    "style": "",
        //    "feature": "",
        //    "howToCook": "把鱼清洗干净,用厨房纸抹干水份,表面铺上薑丝,放进鱼盘中,待用!锅中烧开水,把鱼放进去,盖上盖子蒸6-8分钟!一定要水烧开了才开始计时!时间到,把鱼端出来,蒸过的鱼会出一点水,倒掉盘中的水均匀的在蒸好的鱼上倒入蒸鱼豉油,再铺上葱丝!把锅洗干净,倒入少许油烧开,然后淋上鱼表面上就可以了!也可以加一点豆腐和鱼一起去蒸!",
        //    "authorId": "",
        //    "nutritionContent": [{
        //        "nutritionId": "",
        //        "nutritionName": "",
        //        "content": 0,
        //        "nutritionUnit": ""
        //    }],
        //    "foodContent": [{
        //        "foodId": "913",
        //        "foodName": "",
        //        "content": 1000
        //
        //    },{
        //        "foodId": "1309",
        //        "foodName": "",
        //        "content": 20
        //
        //    }]
        //}
    ];

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
    };




    $http.post($scope.urls.allNutrition)
        .success(function (response) {
            $scope.nutritionContent = response.content;
        });

    $scope.SearchNutritionByName = function () {

        $scope.SearchResult = false;
        $scope.sendNutritionData.nutritionName = $scope.sendNutritionData.nutritionName;
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
        $scope.sendNutritionData.nutritionId = id;
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

    $scope.login = function () {
        //$scope.sendRegister.birthday.setFullYear(1993,12,15);
        $http.post($scope.urls.login, $scope.sendLogin)
            .success(function (response) {
                alert(response.content);
            });
    }

    $scope.addOneCooking = function () {
        //$scope.sendRegister.birthday.setFullYear(1993,12,15);
        $http.post($scope.urls.addCooking, newCooking)
            .success(function (response) {
                alert(response.content);
            });
    }

    $scope.addCooking = function () {
        //$scope.sendRegister.birthday.setFullYear(1993,12,15);
        $http.post($scope.urls.addSomeCooking, newCookingList)
            .success(function (response) {
                alert(response.content);
            });
    }

    $scope.isDetailDivVisible = function () {
        return $scope.SearchDetailResult;
    }


    $scope.SearchFoodById = function () {
        $http.post($scope.urls.foodById, $scope.sendFoodData)
            .success(function (response) {
                $scope.NutritionDetail = response.content;
                $scope.SearchDetailResult = true;
            });
    }
});

