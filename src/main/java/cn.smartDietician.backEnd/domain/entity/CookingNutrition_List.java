package cn.smartDietician.backEnd.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Hashtable;

/**
 * Created by wangshuai on 2016/4/4.
 * 菜品包含营养类,用于在Service中通过菜品编号,查找其营养元素含量
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CookingNutrition_List {
    //<菜品编号,含量>
    public Hashtable<Long, Float> water;//水分g 1
    public Hashtable<Long, Float> protein; //蛋白质(g) 2
    public Hashtable<Long, Float> fat; //脂肪(g) 3
    public Hashtable<Long, Float> carbohydrate; //碳水化合物(g) 4
    public Hashtable<Long, Float> energy; //能量(大卡) 5
    public Hashtable<Long, Float> DF; //膳食纤维(g) 6
    public Hashtable<Long, Float> K;//钾(mg) 7
    public Hashtable<Long, Float> Na;//钠(mg) 8
    public Hashtable<Long, Float> Mg;//镁(mg) 9
    public Hashtable<Long, Float> Ga;//钙(mg) 10
    public Hashtable<Long, Float> P;//磷(mg) 11
    public Hashtable<Long, Float> Fe;//铁(mg) 12
    public Hashtable<Long, Float> Zn;//锌(mg) 13
    public Hashtable<Long, Float> Cu;//铜(mg) 14
    public Hashtable<Long, Float> Mn;//锰(mg) 15
    public Hashtable<Long, Float> Se;//硒(μg) 16
    public Hashtable<Long, Float> Co;//钴(μg) 17
    public Hashtable<Long, Float> VA; //维生素A(μg) 18
    public Hashtable<Long, Float> VB1; //维生素B1,硫胺素(mg) 19
    public Hashtable<Long, Float> VB2; //维生素B2,核黄素(mg) 20
    public Hashtable<Long, Float> VB3; //维生素B3尼克酸,烟酸(mg) 21
    public Hashtable<Long, Float> VC; //维生素C抗坏血酸(mg) 22
    public Hashtable<Long, Float> VE; //维生素E,核黄素(mg) 23
    public Hashtable<Long, Float> cholesterol; //胆固醇(mg) 24

    /**
     * 将菜品-包含-营养元素 信息加入hashtable
     * @param nutritionId 营养元素id
     * @param cookingId 食材id
     * @param content 食材中该营养元素含量
     */
    public void addNewItem(String nutritionId,Long cookingId,float content){
        switch (nutritionId){
            case "1": water.put(cookingId, content);
                break;
            case "2":protein.put(cookingId, content);
                break;
            case "3":fat.put(cookingId, content);
                break;
            case "4":carbohydrate.put(cookingId, content);
                break;
            case "5":energy.put(cookingId, content);
                break;
            case "6":DF.put(cookingId, content);
                break;
            case "7":K.put(cookingId, content);
                break;
            case "8":Na.put(cookingId, content);
                break;
            case "9":Mg.put(cookingId, content);
                break;
            case "10":Ga.put(cookingId, content);
                break;
            case "11":P.put(cookingId, content);
                break;
            case "12":Fe.put(cookingId, content);
                break;
            case "13":Zn.put(cookingId, content);
                break;
            case "14":Cu.put(cookingId, content);
                break;
            case "15":Mn.put(cookingId, content);
                break;
            case "16":Se.put(cookingId, content);
                break;
            case "17":Co.put(cookingId, content);
                break;
            case "18":VA.put(cookingId, content);
                break;
            case "19":VB1.put(cookingId, content);
                break;
            case "20":VB2.put(cookingId, content);
                break;
            case "21":VB3.put(cookingId, content);
                break;
            case "22":VC.put(cookingId, content);
                break;
            case "23":VE.put(cookingId, content);
                break;
            case "24":cholesterol.put(cookingId,content);
                break;
        }
    }

    /**
     * 查找菜品中指定营养元素含量
     * @param nutritionId 营养元素id
     * @param cookingId 食材id
     */
    public float getContent(String nutritionId,Long cookingId){
        float result = 0;
        try{
            switch (nutritionId){
                case "1":result= water.get(cookingId);
                    break;
                case "2":result=protein.get(cookingId);
                    break;
                case "3":result=fat.get(cookingId);
                    break;
                case "4":result=carbohydrate.get(cookingId);
                    break;
                case "5":result=energy.get(cookingId);
                    break;
                case "6":result=DF.get(cookingId);
                    break;
                case "7":result=K.get(cookingId);
                    break;
                case "8":result=Na.get(cookingId);
                    break;
                case "9":result=Mg.get(cookingId);
                    break;
                case "10":result=Ga.get(cookingId);
                    break;
                case "11":result=P.get(cookingId);
                    break;
                case "12":result=Fe.get(cookingId);
                    break;
                case "13":result=Zn.get(cookingId);
                    break;
                case "14":result=Cu.get(cookingId);
                    break;
                case "15":result=Mn.get(cookingId);
                    break;
                case "16":result=Se.get(cookingId);
                    break;
                case "17":result=Co.get(cookingId);
                    break;
                case "18":result=VA.get(cookingId);
                    break;
                case "19":result=VB1.get(cookingId);
                    break;
                case "20":result=VB2.get(cookingId);
                    break;
                case "21":result=VB3.get(cookingId);
                    break;
                case "22":result=VC.get(cookingId);
                    break;
                case "23":result=VE.get(cookingId);
                    break;
                case "24":result=cholesterol.get(cookingId);
                    break;
            }
        }catch (Exception e){
            result = -1;
        }
        return result;
    }

    public boolean removeItem(String nutritionId,Long cookingId){
        boolean result = true;
        try{
            switch (nutritionId){
                case "1":water.remove(cookingId);
                    break;
                case "2":protein.remove(cookingId);
                    break;
                case "3":fat.remove(cookingId);
                    break;
                case "4":carbohydrate.remove(cookingId);
                    break;
                case "5":energy.remove(cookingId);
                    break;
                case "6":DF.remove(cookingId);
                    break;
                case "7":K.remove(cookingId);
                    break;
                case "8":Na.remove(cookingId);
                    break;
                case "9":Mg.remove(cookingId);
                    break;
                case "10":Ga.remove(cookingId);
                    break;
                case "11":P.remove(cookingId);
                    break;
                case "12":Fe.remove(cookingId);
                    break;
                case "13":Zn.remove(cookingId);
                    break;
                case "14":Cu.remove(cookingId);
                    break;
                case "15":Mn.remove(cookingId);
                    break;
                case "16":Se.remove(cookingId);
                    break;
                case "17":Co.remove(cookingId);
                    break;
                case "18":VA.remove(cookingId);
                    break;
                case "19":VB1.remove(cookingId);
                    break;
                case "20":VB2.remove(cookingId);
                    break;
                case "21":VB3.remove(cookingId);
                    break;
                case "22":VC.remove(cookingId);
                    break;
                case "23":VE.remove(cookingId);
                    break;
                case "24":cholesterol.remove(cookingId);
                    break;
            }
        }catch (Exception e){
            result = false;
        }
        return result;
    }

    public CookingNutrition_List() {
       this.water = new Hashtable<Long, Float>();//水分g 1
       this.protein = new Hashtable<Long, Float>(); //蛋白质(g) 2
       this.fat = new Hashtable<Long, Float>(); //脂肪(g) 3
       this.carbohydrate = new Hashtable<Long, Float>(); //碳水化合物(g) 4
       this.energy = new Hashtable<Long, Float>(); //能量(大卡) 5
       this.DF = new Hashtable<Long, Float>(); //膳食纤维(g) 6
       this.K = new Hashtable<Long, Float>();//钾(mg) 7
       this.Na = new Hashtable<Long, Float>();//钠(mg) 8
       this.Mg = new Hashtable<Long, Float>();//镁(mg) 9
       this.Ga = new Hashtable<Long, Float>();//钙(mg) 10
       this.P = new Hashtable<Long, Float>();//磷(mg) 11
       this.Fe = new Hashtable<Long, Float>();//铁(mg) 12
       this.Zn = new Hashtable<Long, Float>();//锌(mg) 13
       this.Cu = new Hashtable<Long, Float>();//铜(mg) 14
       this.Mn = new Hashtable<Long, Float>();//锰(mg) 15
       this.Se = new Hashtable<Long, Float>();//硒(μg) 16
       this.Co = new Hashtable<Long, Float>();//钴(μg) 17
       this.VA = new Hashtable<Long, Float>(); //维生素A(μg) 18
       this.VB1 = new Hashtable<Long, Float>(); //维生素B1,硫胺素(mg) 19
       this.VB2 = new Hashtable<Long, Float>(); //维生素B2,核黄素(mg) 20
       this.VB3 = new Hashtable<Long, Float>(); //维生素B3尼克酸,烟酸(mg) 21
       this.VC = new Hashtable<Long, Float>(); //维生素C抗坏血酸(mg) 22
       this.VE = new Hashtable<Long, Float>(); //维生素E,核黄素(mg) 23
       this.cholesterol = new Hashtable<Long, Float>(); //胆固醇(mg) 24
    }
}
