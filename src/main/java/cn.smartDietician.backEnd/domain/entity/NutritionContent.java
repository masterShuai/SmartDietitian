package cn.smartDietician.backEnd.domain.entity;

/**
 * Created by wangshuai on 2016/4/15.
 */
public class NutritionContent {

    public  String nutritionId;//营养元素编号

    public String nutritionName;//营养元素名称

    public float content;//营养元素含量

    public String nutritionUnit;//营养元素计量单位

    public NutritionContent(String id,String name,float content,String unit){
        this.nutritionId = id;
        this.nutritionName = name;
        this.content = content;
        this.nutritionUnit = unit;
    }

    public NutritionContent(){

    }
}
