package cn.smartDietician.backEnd.domain.entity;

import cn.smartDietician.backEnd.domain.uionPK.FoodNutritionUionPK;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * Created by wangshuai on 2016/4/4.
 * 菜品包含营养类,用于在Service中通过菜品编号,查找其营养元素含量
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Food_Nutrition {
    @Id
    FoodNutritionUionPK uionPK;

    @Column(nullable = false)
    private float content;

    public float getContent() {
        return content;
    }

    public void setContent(float content) {
        this.content = content;
    }

    public FoodNutritionUionPK getUionPK() {
        return uionPK;
    }

    public void setUionPK(FoodNutritionUionPK uionPK) {
        this.uionPK = uionPK;
    }
}
