package cn.smartDietician.backEnd.service;

import cn.smartDietician.backEnd.domain.entity.*;
import cn.smartDietician.backEnd.domain.repository.*;
import cn.smartDietician.backEnd.protocol.*;
import cn.smartDietician.backEnd.utils.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangshuai on 2016/4/6.
 */
@Service
@CacheConfig(cacheNames = {
        "getAllNutrition",
        "getNutritionById",
        "getAllFood",
        "getFoodById",
        "getAllCooking",
        "getCookingById",
})
public class SerachService {

    @Autowired
    private NutritionRepository nutritionRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodNutritionRepository foodNutritionRepository;
    @Autowired
    private CookingRepository cookingRepository;
    @Autowired
    private CookingNutritionRepository cookingNutritionRepository;

    //全部营养元素表信息
    private List<Nutrition> nutritionList = new ArrayList<>();
    //全部食材表信息
    private List<Food> foodList = new ArrayList<>();
    //全部食材_营养元素表信息
    private List<Food_Nutrition> foodNutritionList= new ArrayList<>();
    //全部菜品表信息
    private List<Cooking> cookingList = new ArrayList<>();
    //全部菜品_营养含量信息
    private CookingNutrition_List cookingNutritionHashTable = new CookingNutrition_List();

    //加入智能配餐基准的营养元素数量
    private int nutritionCount = 24;

    @PostConstruct
    public void Init() {
        //iterable转List
        CollectionHelper<Nutrition> helper = new CollectionHelper<>();
        //读取全部营养元素
        nutritionList = helper.iterableToList(nutritionRepository.findAll());
        System.out.println(String.format("加载到营养元素数量：%d", nutritionList.size()));
        //读取全部食材
        foodList = helper.iterableToList(foodRepository.findAll());
        System.out.println(String.format("加载到食材数量：%d", foodList.size()));
        //读取食材_营养元素表
        foodNutritionList = helper.iterableToList(foodNutritionRepository.findAll());
        System.out.println(String.format("加载到食材-营养含量信息：%d", foodNutritionList.size()));
        //读取全部菜品
        cookingList = helper.iterableToList(cookingRepository.findAll());
        System.out.println(String.format("加载到菜品数量：%d", cookingList.size()));

        List<Cooking_Nutrition> cookingNutritionList =new ArrayList<>();
        cookingNutritionList = helper.iterableToList(cookingNutritionRepository.findAll());
        System.out.println(String.format("加载到菜品-营养含量信息：%d", cookingNutritionList.size()));

        for (int i=0;i<cookingNutritionList.size();i++)
        {
            cookingNutritionHashTable.addNewItem(cookingNutritionList.get(i).getUionPK().getNutritionId(),
                    cookingNutritionList.get(i).getUionPK().getCookingId(),
                    cookingNutritionList.get(i).getContent());
        }
        System.out.println("加载到菜品-营养含量信息到HashTable完成");
        cookingNutritionList = null;
    }
//------------------------------------------营养---------------------------------------------//
    /**
     * 通过id 获取指定营养元素详细信息
     * @param nutritionId
     * @return
     */
    @Cacheable({"getNutritionById"})
    public Nutrition getNutritionById(String nutritionId) {
        Nutrition n = null;
        for (int i=0;i<nutritionList.size();i++){
            if (nutritionList.get(i).getId().trim().equalsIgnoreCase(nutritionId.trim())){
                n = nutritionList.get(i);
                break;
            }
        }
        return n;
    }

    /**
     * 获取所有营养元素名称列表
     * @return
     */
    @Cacheable({"getAllNutrition"})
    public List<SalerNutritionListReqData> getAllNutrition(){
        List<SalerNutritionListReqData> salerNutritionList = new ArrayList<>();
        SalerNutritionListReqData sn = new SalerNutritionListReqData();
        for(int i=0;i<nutritionList.size();i++) {
            sn.setNutritionId(nutritionList.get(i).getId());
            sn.setNutritionName(nutritionList.get(i).getName());
            salerNutritionList.add(sn);
            sn = new SalerNutritionListReqData();
        }
        return salerNutritionList;
    }

    /**
     * 通过名称模糊查询营养元素名称列表
     * @param nutritionName
     * @return
     */
    public List<SalerNutritionListReqData> getNutritionByName(String nutritionName) {
        List<SalerNutritionListReqData> salerNutritionList = new ArrayList<>();
        SalerNutritionListReqData sn = new SalerNutritionListReqData();
        for(int i=0;i<nutritionList.size();i++){
            nutritionList.get(i);
            if(nutritionList.get(i).getName().toLowerCase().contains(nutritionName.trim().toLowerCase())) {
                sn.setNutritionId(nutritionList.get(i).getId());
                sn.setNutritionName(nutritionList.get(i).getName());
                salerNutritionList.add(sn);
                sn = new SalerNutritionListReqData();
            }
        }
        return salerNutritionList;
    }

//------------------------------------------食材---------------------------------------------//
    /**
     *获取所有食材名称列表
     * @return
     */
    @Cacheable({"getAllFood"})
    public List<SalerFoodListReqDate> getAllFood(){
        List<SalerFoodListReqDate> SalerFoodList = new ArrayList<>();
        SalerFoodListReqDate sn = new SalerFoodListReqDate();
        for(int i=0;i<foodList.size();i++) {
            sn.setFoodId(foodList.get(i).getId());
            sn.setFoodName(foodList.get(i).getName());
            SalerFoodList.add(sn);
            sn = new SalerFoodListReqDate();
        }
        return SalerFoodList;
    }

    /**
     * 通过id 获取指定食材详细信息
     * @param foodId
     * @return
     */
    @Cacheable({"getFoodById"})
    public SalerFoodReqDate getFoodById(long foodId) {
        SalerFoodReqDate sf = new SalerFoodReqDate();
        Food f;
        for (int i=0;i<foodList.size();i++){
            f = foodList.get(i);
            if (f.getId()==foodId){
                sf.setId(foodId);
                sf.setCity(f.getCity());
                sf.setName(f.getName());
                sf.setOther_name(f.getOther_name());
                sf.setKind(f.getKind());
                sf.setFoo_value(f.getFoo_value());
                sf.setDiet(f.getDiet());
                sf.setUnit(f.getUnit());
                sf.setCan_eat(f.getCan_eat());
                sf.setNutritionContent(getContentByFoodId(foodId));
                break;
            }
        }
        return sf;
    }

    /**
     * 通过名称模糊查询食材名称列表
     * @param foodName
     * @return
     */
    public List<SalerFoodListReqDate> getFoodByName(String foodName) {
        List<SalerFoodListReqDate> salerFoodList = new ArrayList<>();
        SalerFoodListReqDate fn = new SalerFoodListReqDate();
        for(int i=0;i<foodList.size();i++){
            foodList.get(i);
            if(foodList.get(i).getName().toLowerCase().contains(foodName.trim().toLowerCase())) {
                fn.setFoodId(foodList.get(i).getId());
                fn.setFoodName(foodList.get(i).getName());
                salerFoodList.add(fn);

            }
        }
        return salerFoodList;
    }


//------------------------------------------菜品---------------------------------------------//
    /**
     *获取所有菜品名称列表
     * @return
     */
    @Cacheable({"getAllCooking"})
    public List<SalerCookingListReqDate> getAllCooking(){
        List<SalerCookingListReqDate> SalerCookingList = new ArrayList<>();
        SalerCookingListReqDate cn = new SalerCookingListReqDate();
        for(int i=0;i<cookingList.size();i++) {
            cn.setCookingId(cookingList.get(i).getId());
            cn.setCookingName(cookingList.get(i).getName());
            SalerCookingList.add(cn);
            cn = new SalerCookingListReqDate();
        }
        return SalerCookingList;
    }

    /**
     * 通过id 获取指定食材详细信息
     * @param cookingId 食材ID
     * @return
     */
    @Cacheable({"getCookingById"})
    public SalerCookingReqDate getCookingById(long cookingId) {
        SalerCookingReqDate sc = new SalerCookingReqDate();
        Cooking c;
        for (int i=0;i<cookingList.size();i++){
            c = cookingList.get(i);
            if (c.getId()==cookingId){
                sc.setId(cookingId);
                sc.setName(c.getName());
                sc.setOtherName(c.getOtherName());
                sc.setTaste(c.getTaste());
                sc.setKind(c.getKind());
                sc.setStyle(c.getStyle());
                sc.setFeature(c.getFeature());
                sc.setHowToCook(c.getHowToCook());

                sc.setNutritionContent(getContentByCookingId(cookingId));
                break;
            }
        }
        return sc;
    }

    /**
     * 通过名称模糊查询食材名称列表
     * @param cookingName 模糊匹配字符串
     * @return
     */
    public List<SalerCookingListReqDate> getCookingByName(String cookingName) {
        List<SalerCookingListReqDate> salerCookingList = new ArrayList<>();
        SalerCookingListReqDate cn = new SalerCookingListReqDate();
        for(int i=0;i<cookingList.size();i++){
            cookingList.get(i);
            if(cookingList.get(i).getName().toLowerCase().contains(cookingName.trim().toLowerCase())) {
                cn.setCookingId(cookingList.get(i).getId());
                cn.setCookingName(cookingList.get(i).getName());
                salerCookingList.add(cn);
            }
        }
        return salerCookingList;
    }






//######################### 内部调用函数 ############################################

    /**
     * 内部调用,获取某种菜品的营养含量
     * @param id
     * @return
     */
    public List<NutritionContent> getContentByFoodId(long id){
        List<NutritionContent> ncl = new ArrayList<>();
        NutritionContent nc;
        Food_Nutrition fn = new Food_Nutrition();


        for(int i = 0;i< foodNutritionList.size();i++){
            fn = foodNutritionList.get(i);
            if (fn.getUionPK().getFoodId()==id){
                nc = new NutritionContent(fn.getUionPK().getNutritionId(),
                        getNutritionById(fn.getUionPK().getNutritionId()).getName(),
                        fn.getContent(),
                        getNutritionById(fn.getUionPK().getNutritionId()).getUnit());
                ncl.add(nc);
            }
        }
        return ncl;
    }

    /**
     * 内部调用,获取某种菜品的营养含量
     * @param CId
     * @return
     */
    public List<NutritionContent> getContentByCookingId(long CId){
        List<NutritionContent> ncl = new ArrayList<>();
        NutritionContent nc;
        Nutrition n;
        float content;

        for (Integer i=1;i<=nutritionCount;i++){
            content=cookingNutritionHashTable.getContent(i.toString(),CId);
            if (content!=-1){
                n = getNutritionById(i.toString());
                nc = new NutritionContent(n.getId(),
                        n.getName(),
                        content,
                        n.getUnit());
                ncl.add(nc);
            }
        }
        return ncl;
    }
}
