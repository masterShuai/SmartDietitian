package cn.smartDietician.backEnd.controller;


import cn.smartDietician.backEnd.protocol.ResponseContent;
import cn.smartDietician.backEnd.protocol.SalerFoodListReqDate;
import cn.smartDietician.backEnd.protocol.SalerFoodReqDate;
import cn.smartDietician.backEnd.protocol.SalerNutritionListReqData;
import cn.smartDietician.backEnd.service.SerachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 * 查询功能控制器
 */
@RestController
@RequestMapping(value = "/Search")
public class SearchRestController {

    @Autowired
    private SerachService SerachService;

//########################################## 营养元素 查询 ##############################################//
    /*
     * 获取所有营养元素名称列表
     * /all  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     * */
    @RequestMapping(value = "/Nutrition/all",method = RequestMethod.POST)
    public ResponseContent getNutritionAll(){

        /*List<SalerNutritionListReqData> s =SerachService.getAllNutrition();
        for (int i=0;i<s.size();i++){
            System.out.println(s.get(i).getNutritionName());
        }*/

        //初始化部分信息
        System.out.println("getAllNutritionForValitation-----------");


        return ResponseContent.makeSuccessResponse(SerachService.getAllNutrition());
    }

    /**
     * 获取指定ID营养元素
     * /byId  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Nutrition/byId", method = RequestMethod.POST)
    public ResponseContent getNutritionByID(@RequestBody SalerNutritionListReqData paras) {
        //初始化部分信息
        System.out.println("getNutritionByIdValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getNutritionById(paras.getNutritionId()));
    }

    /**
     * 通过关键字模糊查找
     * /byName  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Nutrition/byName", method = RequestMethod.POST)
    public ResponseContent getNutritionByName(@RequestBody SalerNutritionListReqData paras) {
        //初始化部分信息
        System.out.println("getNutritionByNameForValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getNutritionByName(paras.getNutritionName()));
    }

//########################################## 食材 查询 ##############################################//

    /*
     * 获取所有食材名称列表
     * /all  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     * */
    @RequestMapping(value = "/Food/all",method = RequestMethod.POST)
    public ResponseContent getFoodAll() {
        //初始化部分信息
        System.out.println("getFoodAllForValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getAllFood());
    }

    /**
     * 获取指定ID食材
     * /byId  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Food/byId", method = RequestMethod.POST)
    public ResponseContent getNutritionByID(@RequestBody SalerFoodListReqDate paras) {
        //初始化部分信息
        System.out.println("getNutritionByIdValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getFoodById(paras.getFoodId()));
    }

    /**
     * 通过关键字模糊查找
     * /byName  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Food/byName", method = RequestMethod.POST)
    public ResponseContent getNutritionByName(@RequestBody SalerFoodListReqDate paras) {
        //初始化部分信息
        System.out.println("getFoodByNameForValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getFoodByName(paras.getFoodName()));
    }

    /**
     * 测试食材
     * /byId  POST
     *
     * @param end 结束标志
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Food/testAll", method = RequestMethod.GET)
    public ResponseContent getNutrition(@RequestParam(value = "start", required=true) int start,
                                        @RequestParam(value = "end", required=true) int end)
    {
        //初始化部分信息
        System.out.println("getNutritionByIdValitation-----------");
        List<SalerFoodReqDate> foodReqDateList = new ArrayList<>();
        for (int i = start;i<end;i++){
            foodReqDateList.add(SerachService.getFoodById(i));
        }
        return ResponseContent.makeSuccessResponse(foodReqDateList);
    }
}
