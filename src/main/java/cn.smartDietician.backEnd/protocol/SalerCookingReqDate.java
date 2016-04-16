package cn.smartDietician.backEnd.protocol;

import cn.smartDietician.backEnd.domain.entity.NutritionContent;

import java.util.List;

/**
 * Created by wangshuai on 2016/4/15.
 */
public class SalerCookingReqDate {

    private long id;//菜品编号
    private String name;//菜品名称
    private String otherName;//菜品别名
    private String taste;//口味[酸 甜 苦 辣 咸]
    private String kind;//类别[肉类 蔬菜水果 汤粥主食 水产 蛋奶豆制品 米面干果腌咸]
    private String style;//菜系
    private String feature;//简介
    private String howToCook;//详细做法

    private List<NutritionContent> nutritionContent;//营养含量[{"营养名称":"VC","含量":0,"计量单位":"mg"}]

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getHowToCook() {
        return howToCook;
    }

    public void setHowToCook(String howToCook) {
        this.howToCook = howToCook;
    }

    public List<NutritionContent> getNutritionContent() {
        return nutritionContent;
    }

    public void setNutritionContent(List<NutritionContent> nutritionContent) {
        this.nutritionContent = nutritionContent;
    }
}
