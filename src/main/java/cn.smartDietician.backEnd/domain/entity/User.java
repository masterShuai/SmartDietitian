package cn.smartDietician.backEnd.domain.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {
    @Id
    private String id; //用户名
    @Column(nullable = false)
    private String name; //昵称
    @Column(nullable = false)
    private String password; //密码
    @Column(nullable = false)
    private String sex; //性别
    @Column(nullable = false)
    private Date birthday;  //出身日期
    @Column(nullable = false)
    private float weight;  //体重(千克)
    @Column(nullable = false)
    private boolean pregnant;  //是否怀孕
    @Column(nullable = false)
    private boolean lactation;  //是否处于哺乳期
    @Column(nullable = false)
    private int gestation;  //孕期(0:非孕妇;1:怀孕初期;2:怀孕中期;3:怀孕后期)
    @Column(nullable = false)
    private int manualWork; //体力劳动程度(0:不从事;1:从事轻微体力劳动;2:中度体力劳动;3:从事重体力劳动)

    /*private float water; //水分g 推荐摄入量
    private float protein; //蛋白质(g) 推荐摄入量
    private float fat; //脂肪(g) 推荐摄入量(比例)
    private float carbohydrate; //碳水化合物(g) 推荐摄入量(比例)
    private float energy; //能量(大卡) 推荐摄入量
    private float DF; //膳食纤维(g) 推荐摄入量
    private float K;//钾(mg)推荐摄入量
    private float Na;//钠(mg)推荐摄入量
    private float Mg;//镁(mg)推荐摄入量
    private float Ga;//钙(mg)推荐摄入量
    private float P;//磷(mg)推荐摄入量
    private float Fe;//铁(mg)推荐摄入量
    private float Zn;//锌(mg)推荐摄入量
    private float Cu;//铜(mg)推荐摄入量
    private float Mn;//锰(mg)推荐摄入量
    private float Se;//硒(μg)推荐摄入量
    private float Co;//钴(μg)推荐摄入量
    private float VA; //维生素A(μg) 推荐摄入量
    private float VB1; //维生素B1,硫胺素(mg) 推荐摄入量
    private float VB2; //维生素B2,核黄素(mg) 推荐摄入量
    private float VB3; //维生素B3尼克酸(mg) 推荐摄入量
    private float VC; //维生素C抗坏血酸(mg) 推荐摄入量
    private float VE; //维生素E,核黄素(mg) 推荐摄入量
    private float cholesterol; //胆固醇(mg) 推荐摄入量

    private float fat_MAX; //脂肪(g) 摄入量上限(比例)
    private float carbohydrate_MAX; //碳水化合物(g) 摄入量上限(比例)
    private float DF_MAX;//膳食纤维(g)  摄入量上限
    private float cholesterol_MAX;//胆固醇(mg)  摄入量上限
    private float VA_MAX; //维生素A(μg) 摄入量上限
    private float carotene_MAX;//胡萝卜素(mg) 摄入量上限
    private float VB1_MAX; //维生素B1,硫胺素(g)  摄入量上限
    private float VB2_MAX; //维生素B2,核黄素(g)  摄入量上限
    private float VC_MAX; //维生素C抗坏血酸(mg) 摄入量上限
    private float VE_MAX; //维生素E,核黄素(mg) 摄入量上限
    private float Ga_MAX;//钙(mg)摄入量上限
    private float P_MAX;//磷(mg)摄入量上限
    private float Fe_MAX;//铁(mg)摄入量上限
    private float Zn_MAX;//锌(mg)摄入量上限
    private float Cu_MAX;//铜(mg)摄入量上限
    private float Mn_MAX;//锰(mg)摄入量上限
    private float Se_MAX;//硒(μg)摄入量上限*/

    public boolean setNutrition(){
        try{

        }catch (Exception e){
            return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isPregnant() {
        return pregnant;
    }

    public boolean isLactation() {
        return lactation;
    }

    public int getGestation() {
        return gestation;
    }

    public int getManualWork() {
        return manualWork;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setPregnant(boolean pregnant) {
        this.pregnant = pregnant;
    }

    public void setLactation(boolean lactation) {
        this.lactation = lactation;
    }

    public void setGestation(int gestation) {
        this.gestation = gestation;
    }

    public void setManualWork(int manualWork) {
        this.manualWork = manualWork;
    }
/*
    public float getWater() {
        return water;
    }

    public float getProtein() {
        return protein;
    }

    public float getFat() {
        return fat;
    }

    public float getCarbohydrate() {
        return carbohydrate;
    }

    public float getEnergy() {
        return energy;
    }

    public float getDF() {
        return DF;
    }

    public float getK() {
        return K;
    }

    public float getNa() {
        return Na;
    }

    public float getMg() {
        return Mg;
    }

    public float getGa() {
        return Ga;
    }

    public float getP() {
        return P;
    }

    public float getFe() {
        return Fe;
    }

    public float getZn() {
        return Zn;
    }

    public float getCu() {
        return Cu;
    }

    public float getMn() {
        return Mn;
    }

    public float getSe() {
        return Se;
    }

    public float getCo() {
        return Co;
    }

    public float getVA() {
        return VA;
    }

    public float getVB1() {
        return VB1;
    }

    public float getVB2() {
        return VB2;
    }

    public float getVB3() {
        return VB3;
    }

    public float getVC() {
        return VC;
    }

    public float getVE() {
        return VE;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public float getFat_MAX() {
        return fat_MAX;
    }

    public float getCarbohydrate_MAX() {
        return carbohydrate_MAX;
    }

    public float getDF_MAX() {
        return DF_MAX;
    }

    public float getCholesterol_MAX() {
        return cholesterol_MAX;
    }

    public float getVA_MAX() {
        return VA_MAX;
    }

    public float getCarotene_MAX() {
        return carotene_MAX;
    }

    public float getVB1_MAX() {
        return VB1_MAX;
    }

    public float getVB2_MAX() {
        return VB2_MAX;
    }

    public float getVC_MAX() {
        return VC_MAX;
    }

    public float getVE_MAX() {
        return VE_MAX;
    }

    public float getGa_MAX() {
        return Ga_MAX;
    }

    public float getP_MAX() {
        return P_MAX;
    }

    public float getFe_MAX() {
        return Fe_MAX;
    }

    public float getZn_MAX() {
        return Zn_MAX;
    }

    public float getCu_MAX() {
        return Cu_MAX;
    }

    public float getMn_MAX() {
        return Mn_MAX;
    }

    public float getSe_MAX() {
        return Se_MAX;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public void setCarbohydrate(float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public void setDF(float DF) {
        this.DF = DF;
    }

    public void setK(float k) {
        K = k;
    }

    public void setNa(float na) {
        Na = na;
    }

    public void setMg(float mg) {
        Mg = mg;
    }

    public void setGa(float ga) {
        Ga = ga;
    }

    public void setP(float p) {
        P = p;
    }

    public void setFe(float fe) {
        Fe = fe;
    }

    public void setZn(float zn) {
        Zn = zn;
    }

    public void setCu(float cu) {
        Cu = cu;
    }

    public void setMn(float mn) {
        Mn = mn;
    }

    public void setSe(float se) {
        Se = se;
    }

    public void setCo(float co) {
        Co = co;
    }

    public void setVA(float VA) {
        this.VA = VA;
    }

    public void setVB1(float VB1) {
        this.VB1 = VB1;
    }

    public void setVB2(float VB2) {
        this.VB2 = VB2;
    }

    public void setVB3(float VB3) {
        this.VB3 = VB3;
    }

    public void setVC(float VC) {
        this.VC = VC;
    }

    public void setVE(float VE) {
        this.VE = VE;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public void setFat_MAX(float fat_MAX) {
        this.fat_MAX = fat_MAX;
    }

    public void setCarbohydrate_MAX(float carbohydrate_MAX) {
        this.carbohydrate_MAX = carbohydrate_MAX;
    }

    public void setDF_MAX(float DF_MAX) {
        this.DF_MAX = DF_MAX;
    }

    public void setCholesterol_MAX(float cholesterol_MAX) {
        this.cholesterol_MAX = cholesterol_MAX;
    }

    public void setVA_MAX(float VA_MAX) {
        this.VA_MAX = VA_MAX;
    }

    public void setCarotene_MAX(float carotene_MAX) {
        this.carotene_MAX = carotene_MAX;
    }

    public void setVB1_MAX(float VB1_MAX) {
        this.VB1_MAX = VB1_MAX;
    }

    public void setVB2_MAX(float VB2_MAX) {
        this.VB2_MAX = VB2_MAX;
    }

    public void setVC_MAX(float VC_MAX) {
        this.VC_MAX = VC_MAX;
    }

    public void setVE_MAX(float VE_MAX) {
        this.VE_MAX = VE_MAX;
    }

    public void setGa_MAX(float ga_MAX) {
        Ga_MAX = ga_MAX;
    }

    public void setP_MAX(float p_MAX) {
        P_MAX = p_MAX;
    }

    public void setFe_MAX(float fe_MAX) {
        Fe_MAX = fe_MAX;
    }

    public void setZn_MAX(float zn_MAX) {
        Zn_MAX = zn_MAX;
    }

    public void setCu_MAX(float cu_MAX) {
        Cu_MAX = cu_MAX;
    }

    public void setMn_MAX(float mn_MAX) {
        Mn_MAX = mn_MAX;
    }

    public void setSe_MAX(float se_MAX) {
        Se_MAX = se_MAX;
    }*/
}


