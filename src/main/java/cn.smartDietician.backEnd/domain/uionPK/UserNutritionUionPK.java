package cn.smartDietician.backEnd.domain.uionPK;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by wangshuai on 2016/4/15.
 */

@Embeddable
public class UserNutritionUionPK implements Serializable {
    @Column
    private String userId;
    @Column
    private String nutritionId;

    public String getNutritionId() {
        return nutritionId;
    }

    public void setNutritionId(String nutritionId) {
        this.nutritionId = nutritionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserNutritionUionPK){
            UserNutritionUionPK pk=(UserNutritionUionPK)obj;
            if(this.nutritionId.equals(pk.nutritionId)&&this.userId.equals(pk.userId)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public UserNutritionUionPK(String userId, String nutritionId) {
        this.userId = userId;
        this.nutritionId = nutritionId;
    }
    public UserNutritionUionPK(){

    }
}
