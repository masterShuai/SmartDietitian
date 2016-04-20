package cn.smartDietician.backEnd.protocol;

import cn.smartDietician.backEnd.domain.entity.CookingContent;
import cn.smartDietician.backEnd.domain.entity.NutritionContent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by wangshuai on 2016/4/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalerUserReqDate {
    private String id; //用户名
    private String name; //昵称
    private String password; //密码
    private String sex; //性别
    private String birthday;  //出身日期
    private float weight;  //体重(千克)
    private boolean pregnant;  //是否怀孕
    private boolean lactation;  //是否处于哺乳期
    private int gestation;  //孕期(0:非孕妇;1:怀孕初期;2:怀孕中期;3:怀孕后期)
    private int manualWork; //体力劳动程度(0:不从事;1:从事轻微体力劳动;2:中度体力劳动;3:从事重体力劳动)

    public List<NutritionContent> nutritionMin;//推荐摄入量[{"营养编号":"1","营养名称":"VC","含量":0,"计量单位":"mg"}]
    public List<NutritionContent> nutritionMan;//摄入量上限[{"营养编号":"1","营养名称":"VC","含量":0,"计量单位":"mg"}]
    public List<CookingContent> cookingContent;//当日饮食清单[{"菜品编号":1,"菜品名称":"小鸡炖蘑菇","菜品数量":1,"使用人数":2}]

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isPregnant() {
        return pregnant;
    }

    public void setPregnant(boolean pregnant) {
        this.pregnant = pregnant;
    }

    public boolean isLactation() {
        return lactation;
    }

    public void setLactation(boolean lactation) {
        this.lactation = lactation;
    }

    public int getGestation() {
        return gestation;
    }

    public void setGestation(int gestation) {
        this.gestation = gestation;
    }

    public int getManualWork() {
        return manualWork;
    }

    public void setManualWork(int manualWork) {
        this.manualWork = manualWork;
    }

    public List<NutritionContent> getNutritionMin() {
        return nutritionMin;
    }

    public void setNutritionMin(List<NutritionContent> nutritionMin) {
        this.nutritionMin = nutritionMin;
    }

    public List<NutritionContent> getNutritionMan() {
        return nutritionMan;
    }

    public void setNutritionMan(List<NutritionContent> nutritionMan) {
        this.nutritionMan = nutritionMan;
    }

    public List<CookingContent> getCookingContent() {
        return cookingContent;
    }

    public void setCookingContent(List<CookingContent> cookingContent) {
        this.cookingContent = cookingContent;
    }
}
