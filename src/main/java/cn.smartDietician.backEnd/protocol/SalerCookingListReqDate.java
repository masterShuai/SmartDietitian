package cn.smartDietician.backEnd.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by wangshuai on 2016/4/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalerCookingListReqDate {
    private long cookingId;
    private String cookingName;

    public long getCookingId() {
        return cookingId;
    }

    public void setCookingId(long cookingId) {
        this.cookingId = cookingId;
    }

    public String getCookingName() {
        return cookingName;
    }

    public void setCookingName(String cookingName) {
        this.cookingName = cookingName;
    }
}
