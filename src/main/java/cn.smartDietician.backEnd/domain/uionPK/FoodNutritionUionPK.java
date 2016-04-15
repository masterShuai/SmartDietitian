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
    private String nutritionid;
    @Column
    private long foodid;

    public String getNutritionid() {
        return nutritionid;
    }

    public void setNutritionid(String nutritionid) {
        this.nutritionid = nutritionid;
    }

    public long getFoodid() {
        return foodid;
    }

    public void setFoodid(long foodid) {
        this.foodid = foodid;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FoodNutritionUionPK){
            FoodNutritionUionPK pk=(FoodNutritionUionPK)obj;
            if(this.nutritionid.equals(pk.nutritionid)&&this.foodid==pk.foodid){
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
