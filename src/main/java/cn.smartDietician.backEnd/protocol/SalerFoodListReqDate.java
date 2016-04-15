package cn.smartDietician.backEnd.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by wangshuai on 2016/4/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalerFoodListReqDate {
    private long foodId;
    private String foodName;

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
