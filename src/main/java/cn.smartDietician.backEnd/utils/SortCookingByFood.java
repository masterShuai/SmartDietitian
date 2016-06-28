package cn.smartDietician.backEnd.utils;

import cn.smartDietician.backEnd.domain.entity.Cooking_Food;

import java.util.Comparator;

/**
 * Created by wangshuai on 2016/5/24.
 */
public class SortCookingByFood implements Comparator{
    public int compare(Object o1, Object o2){
        Cooking_Food f1 = (Cooking_Food) o1;
        Cooking_Food f2 = (Cooking_Food) o2;
        if (f1.getContent() > f2.getContent())
            return -1;
        else if (f1.getContent() == f2.getContent()) {
            return 0;
        }
        return 1;
    }
}
