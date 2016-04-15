package cn.smartDietician.backEnd.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Hashtable;

/**
 * Created by wangshuai on 2016/4/4.
 * 菜品包含营养类,用于在Service中通过菜品编号,查找其营养元素含量
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cooking_Nutrition {
    //<菜品编号,含量>
    public Hashtable<String, Float> water;//水分g 1
    public Hashtable<String, Float> protein; //蛋白质(g) 2
    public Hashtable<String, Float> fat; //脂肪(g) 3
    public Hashtable<String, Float> carbohydrate; //碳水化合物(g) 4
    public Hashtable<String, Float> energy; //能量(大卡) 5
    public Hashtable<String, Float> DF; //膳食纤维(g) 6
    public Hashtable<String, Float> K;//钾(mg) 7
    public Hashtable<String, Float> Na;//钠(mg) 8
    public Hashtable<String, Float> Mg;//镁(mg) 9
    public Hashtable<String, Float> Ga;//钙(mg) 10
    public Hashtable<String, Float> P;//磷(mg) 11
    public Hashtable<String, Float> Fe;//铁(mg) 12
    public Hashtable<String, Float> Zn;//锌(mg) 13
    public Hashtable<String, Float> Cu;//铜(mg) 14
    public Hashtable<String, Float> Mn;//锰(mg) 15
    public Hashtable<String, Float> Se;//硒(μg) 16
    public Hashtable<String, Float> Co;//钴(μg) 17
    public Hashtable<String, Float> VA; //维生素A(μg) 18
    public Hashtable<String, Float> VB1; //维生素B1,硫胺素(mg) 19
    public Hashtable<String, Float> VB2; //维生素B2,核黄素(mg) 20
    public Hashtable<String, Float> VB3; //维生素B3尼克酸(mg) 21
    public Hashtable<String, Float> VC; //维生素C抗坏血酸(mg) 22
    public Hashtable<String, Float> VE; //维生素E,核黄素(mg) 23
    public Hashtable<String, Float> cholesterol; //胆固醇(mg) 24
}
