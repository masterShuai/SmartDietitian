package cn.smartDietician.backEnd.domain.entity;

/**
 * Created by wangshuai on 2016/4/15.
 */
public class FoodContent {

    public Long foodId;//食材编号

    public String foodName;//食材名称

    public float content;//食材用量(克)

    public FoodContent(Long foodId, String foodName, float content){
        this.foodId = foodId;
        this.foodName = foodName;
        this.content = content;
    }
}
