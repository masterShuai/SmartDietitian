package cn.smartDietician.backEnd.controller;


import cn.smartDietician.backEnd.protocol.ResponseContent;
import cn.smartDietician.backEnd.protocol.SalerNutritionListReqData;
import cn.smartDietician.backEnd.service.SerachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2015/10/21.
 */
@RestController
@RequestMapping(value = "/Search")
public class SearchRestController {

    @Autowired
    private SerachService SerachService;

    /*
     * 获取所有营养元素
     * /all  POST
     *
     * @param paras 信息参数
     * @return 返回的信息列表
     * */
    @RequestMapping(value = "/Nutrition/all",method = RequestMethod.POST)
    public ResponseContent getPaperAll() {
        //初始化部分信息
        System.out.println("getPaperForValitation-----------");
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
        System.out.println("getPaperForValitation-----------");
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
        System.out.println("getPaperForValitation-----------");
        return ResponseContent.makeSuccessResponse(SerachService.getNutritionByName(paras.getNutritionName()));
    }

}
