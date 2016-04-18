package cn.smartDietician.backEnd.domain.uionPK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by wangshuai on 2016/4/15.
 */

@Embeddable
public class FoodNutritionUionPK implements Serializable {
    @Column
    private String nutritionId;
    @Column
    private long foodId;

    public String getNutritionId() {
        return nutritionId;
    }

    public void setNutritionId(String nutritionId) {
        this.nutritionId = nutritionId;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FoodNutritionUionPK){
            FoodNutritionUionPK pk=(FoodNutritionUionPK)obj;
            if(this.nutritionId.trim().equals(pk.nutritionId.trim())&&this.foodId==pk.foodId){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
