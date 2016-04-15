package cn.smartDietician.backEnd.controller;


import cn.smartDietician.backEnd.protocol.ResponseContent;
import cn.smartDietician.backEnd.protocol.SalerNutritionListReqData;
import cn.smartDietician.backEnd.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2015/10/21.
 */
@RestController
@RequestMapping(value = "/RES/Nutrition")
public class NutritionRestController {

    @Autowired
    private NutritionService NutritionService;

    /*
     * 获取所有营养元素
     * /all  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     * */
    @RequestMapping(value = "/all",method = RequestMethod.POST)
    public ResponseContent getPaperAll() {
        //初始化部分信息
        System.out.println("getPaperForValitation-----------");
        return ResponseContent.makeSuccessResponse(NutritionService.getAllNutrition());
    }

    /**
     * 获取指定ID营养元素
     * /byId  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/byId", method = RequestMethod.POST)
    public ResponseContent getNutritionByID(@RequestBody SalerNutritionListReqData paras) {
        //初始化部分信息
        System.out.println("getPaperForValitation-----------");
        return ResponseContent.makeSuccessResponse(NutritionService.getNutritionById(paras.getNutritionId()));
    }

    /**
     * 新建信息
     * /v  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     */
    @RequestMapping(value = "/byName", method = RequestMethod.POST)
    public ResponseContent getNutritionByName(@RequestBody SalerNutritionListReqData paras) {
        //初始化部分信息
        System.out.println("getPaperForValitation-----------");
        return ResponseContent.makeSuccessResponse(NutritionService.getNutritionByName(paras.getNutritionName()));
    }


}
