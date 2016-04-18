package cn.smartDietician.backEnd.domain.uionPK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by wangshuai on 2016/4/15.
 */

@Embeddable
public class CookingFoodUionPK implements Serializable {
    @Column
    private long foodId;
    @Column
    private long cookingId;

    public CookingFoodUionPK(long foodId, long cookingId) {
        this.foodId = foodId;
        this.cookingId = cookingId;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public long getCookingId() {
        return cookingId;
    }

    public void setCookingId(long cookingId) {
        this.cookingId = cookingId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CookingFoodUionPK){
            CookingFoodUionPK pk=(CookingFoodUionPK)obj;
            if(this.foodId==pk.foodId&&this.cookingId==pk.cookingId){
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
