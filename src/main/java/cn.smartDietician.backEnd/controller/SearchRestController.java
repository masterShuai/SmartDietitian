package cn.smartDietician.backEnd.controller;


import cn.smartDietician.backEnd.domain.entity.Nutrition;
import cn.smartDietician.backEnd.domain.entity.NutritionContent;
import cn.smartDietician.backEnd.protocol.*;
import cn.smartDietician.backEnd.service.SerachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        Nutrition n = SerachService.getNutritionById(paras.getNutritionId());
        if(n!=null){
            return ResponseContent.makeSuccessResponse(n);
        }
        else{
            return ResponseContent.makeFailResponse();
        }
    }

    /**
     * 获取富含该营养元素的前十种食材
     * @param paras
     * @return
     */
    @RequestMapping(value = "/Nutrition/getTopTen", method = RequestMethod.POST)
    public ResponseContent getNutritionContent(@RequestBody SalerNutritionListReqData paras) {
        //初始化部分信息
        System.out.println("getTopTenFoodValitation-----------");
        List<NutritionContent> ncl = SerachService.getTenFood(paras);
        System.out.println(ncl.size());
        if (ncl.size()>0){
            return ResponseContent.makeSuccessResponse(ncl);
        }
        else{
            return ResponseContent.makeFailResponse();
        }
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
    public ResponseContent getFoodByID(@RequestBody SalerFoodListReqDate paras) {
        //初始化部分信息
        System.out.println("getFoodNutritionByIdValitation-----------");
        SalerFoodReqDate food=SerachService.getFoodById(paras.getFoodId());
        if (!food.equals(new SalerFoodReqDate()))
            return ResponseContent.makeSuccessResponse(food);
        else
            return ResponseContent.makeFailResponse();
    }

    /**
     * 通过关键字模糊查找
     * /byName  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Food/byName", method = RequestMethod.POST)
    public ResponseContent getFoodByName(@RequestBody SalerFoodListReqDate paras) {
        //初始化部分信息
        String name = paras.getFoodName();
        System.out.println("getFoodByNameForValitation-----------"+name);
        return ResponseContent.makeSuccessResponse(SerachService.getFoodByName(name));
    }

    /**
     * 测试食材
     * /byId  POST
     *
     * @param end 结束标志
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Food/testAll", method = RequestMethod.GET)
    public ResponseContent getFoodTest(@RequestParam(value = "start", required=true) int start,
                                        @RequestParam(value = "end", required=true) int end)
    {
        //初始化部分信息
        System.out.println("getFoodNutritionByIdValitation-----------");
        List<SalerFoodReqDate> foodReqDateList = new ArrayList<SalerFoodReqDate>();
        for (int i = start;i<=end;i++){
            foodReqDateList.add(SerachService.getFoodById(i));
        }
        return ResponseContent.makeSuccessResponse(foodReqDateList);
    }
//########################################## 菜品 查询;DIY##############################################//

    /*
     * 获取所有菜品名称列表
     * /all  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     * */
    @RequestMapping(value = "/Cooking/all",method = RequestMethod.POST)
    public ResponseContent getCookingAll() {
        //初始化部分信息
        System.out.println("getCookingAllForValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getAllCooking());
    }

    /**
     * 获取指定ID菜品
     * /byId  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Cooking/byId", method = RequestMethod.POST)
    public ResponseContent getCookingByID(@RequestBody SalerCookingListReqDate paras) {
        //初始化部分信息
        System.out.println("getCookingNutritionByIdValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getCookingById(paras.getCookingId()));
    }

    /**
     * 通过关键字模糊查找
     * /byName  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Cooking/byName", method = RequestMethod.POST)
    public ResponseContent getCookingByName(@RequestBody SalerCookingListReqDate paras) {
        //初始化部分信息
        System.out.println("getCookingByNameForValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getCookingByName(paras.getCookingName()));
    }

    /**
     *
     * @param paras 新增菜品信息
     * @return response.content(true||false) 说明添加是否成功
     */
    @RequestMapping(value = "/Cooking/save", method = RequestMethod.POST)
    public ResponseContent saveCooking(@RequestBody SalerCookingReqDate paras,
                                       HttpServletRequest request,
                                       HttpSession session) {
        System.out.println("save start-----------");
        //初始化部分信息
        System.out.println("getSaveCookingValitation-----------");
        String userId;
        if(session.getAttribute("User")!=null){
            userId = session.getAttribute("User").toString();
            return ResponseContent.makeSuccessResponse(SerachService.saveCooking(paras,userId));
        }
        else
            return ResponseContent.makeFailResponse();
    }

    /**
     * 测试菜品
     * /byId  POST
     *
     * @param end 结束索引
     * @param start 开始索引
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/Cooking/testAll", method = RequestMethod.GET)
    public ResponseContent getCooking(@RequestParam(value = "start", required=true) int start,
                                        @RequestParam(value = "end", required=true) int end) {
        //初始化部分信息
        System.out.println("getCookingNutritionByIdValitation-----------");
        List<SalerCookingReqDate> cookingReqDateList = new ArrayList<SalerCookingReqDate>();
        for (int i = start;i<=end;i++){
            cookingReqDateList.add(SerachService.getCookingById(i));
        }
        return ResponseContent.makeSuccessResponse(cookingReqDateList);
    }

    /**
     *
     */

    @RequestMapping(value = "/Cooking/smartDietician", method = RequestMethod.POST)
    public ResponseContent smartDietician(@RequestBody List<NutritionContent> paras) {
        //初始化部分信息
        System.out.println("getCookingNutritionByIdValitation-----------");
        SalerCookingReqDate salerCookingReqDate = SerachService.smartDietician(paras);
        if (salerCookingReqDate.getId()!=0){
            return ResponseContent.makeSuccessResponse();
        }
        else
            return ResponseContent.makeFailResponse();
    }
}
