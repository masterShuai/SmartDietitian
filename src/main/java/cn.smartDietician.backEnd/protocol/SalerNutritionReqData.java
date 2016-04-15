package cn.smartDietician.backEnd.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by wangshuai on 2016/4/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalerNutritionReqData {
    private String NutritionId;
    private String NutritionName;

    public String getNutritionName() {
        return NutritionName;
    }

    public void setNutritionName(String nutritionName) {
        NutritionName = nutritionName;
    }

    public String getNutritionId() {
        return NutritionId;
    }

    public void setNutritionId(String nutritionId) {
        NutritionId = nutritionId;
    }
}
