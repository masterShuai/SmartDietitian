package cn.smartDietician.backEnd.domain.uionPK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by wangshuai on 2016/4/15.
 */

@Embeddable
public class CookingNutritionUionPK implements Serializable {
    @Column
    private String nutritionId;
    @Column
    private long cookingId;

    public CookingNutritionUionPK(long cookingId, String nutritionId) {
        this.cookingId = cookingId;
        this.nutritionId = nutritionId;
    }

    public String getNutritionId() {
        return nutritionId;
    }

    public void setNutritionId(String nutritionId) {
        this.nutritionId = nutritionId;
    }

    public long getCookingId() {
        return cookingId;
    }

    public void setCookingId(long cookingId) {
        this.cookingId = cookingId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CookingNutritionUionPK){
            CookingNutritionUionPK pk=(CookingNutritionUionPK)obj;
            if(this.nutritionId.trim().equals(pk.nutritionId.trim())&&this.cookingId==pk.cookingId){
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
