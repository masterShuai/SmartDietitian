package cn.smartDietician.backEnd.utils;

import cn.smartDietician.backEnd.domain.entity.Food_Nutrition;

import java.util.Comparator;

/**
 * Created by wangshuai on 2016/5/24.
 */
public class SortFoodByNutrition implements Comparator{
    public int compare(Object o1, Object o2){
        Food_Nutrition f1 = (Food_Nutrition) o1;
        Food_Nutrition f2 = (Food_Nutrition) o2;
        if (f1.getContent() > f2.getContent())
            return -1;
        else if (f1.getContent() == f2.getContent()) {
            return 0;
        }
        return 1;
    }
}
