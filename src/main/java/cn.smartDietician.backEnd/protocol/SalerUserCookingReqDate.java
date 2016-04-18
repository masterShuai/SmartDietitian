package cn.smartDietician.backEnd.protocol;

import cn.smartDietician.backEnd.domain.entity.CookingContent;

import java.util.List;

/**
 * Created by wangshuai on 2016/4/15.
 */
public class SalerUserCookingReqDate {
    public List<CookingContent> cookingContent;//当日饮食清单[{"菜品编号":1,"菜品名称":"小鸡炖蘑菇","菜品数量":1,"使用人数":2}]
}