package cn.smartDietician.backEnd.protocol;

import cn.smartDietician.backEnd.domain.entity.NutritionContent;

import java.util.List;

/**
 * Created by wangshuai on 2016/4/15.
 */
public class SalerUserNutritionReqDate {

    public List<NutritionContent> nutritionMin;//推荐摄入量[{"营养编号":"1","营养名称":"VC","含量":0,"计量单位":"mg"}]
    public List<NutritionContent> nutritionMan;//摄入量上限[{"营养编号":"1","营养名称":"VC","含量":0,"计量单位":"mg"}]

}