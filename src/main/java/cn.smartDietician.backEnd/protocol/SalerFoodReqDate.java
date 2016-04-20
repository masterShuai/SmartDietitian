package cn.smartDietician.backEnd.protocol;

import cn.smartDietician.backEnd.domain.entity.NutritionContent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by wangshuai on 2016/4/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalerFoodReqDate {

    private long id;//食材编号

    private String city;//产地

    private String name;//食材名称

    private String other_name;//食材别名

    private String kind;//类型

    private String foo_value;//营养价值

    private String diet;//忌食

    private String unit;//食材计量单位

    private String can_eat;//可否生食

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

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getFoo_value() {
        return foo_value;
    }

    public void setFoo_value(String foo_value) {
        this.foo_value = foo_value;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getUnit() {
        return unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCan_eat() {
        return can_eat;
    }

    public void setCan_eat(String can_eat) {
        this.can_eat = can_eat;
    }

    public List<NutritionContent> getNutritionContent() {
        return nutritionContent;
    }

    public void setNutritionContent(List<NutritionContent> nutritionContent) {
        this.nutritionContent = nutritionContent;
    }
}
