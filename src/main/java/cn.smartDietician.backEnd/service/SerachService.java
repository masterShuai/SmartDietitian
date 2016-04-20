package cn.smartDietician.backEnd.service;

import cn.smartDietician.backEnd.domain.entity.*;
import cn.smartDietician.backEnd.domain.repository.*;
import cn.smartDietician.backEnd.domain.uionPK.CookingFoodUionPK;
import cn.smartDietician.backEnd.domain.uionPK.CookingNutritionUionPK;
import cn.smartDietician.backEnd.protocol.*;
import cn.smartDietician.backEnd.utils.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Hashtable;
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
        "computeTodayNtrition",
        "smartDietician"
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
    @Autowired
    private CookingFoodRepository cookingFoodRepository;


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
    //全部菜品_食材含表信息
    private List<Cooking_Food> cookingFoodList= new ArrayList<>();


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

        cookingFoodList = helper.iterableToList(cookingFoodRepository.findAll());
        System.out.println(String.format("加载到菜品-食材含量信息：%d", cookingFoodList.size()));

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
     * 通过id 获取指定菜品详细信息
     * @param cookingId 食材ID
     * @return
     */
    @Cacheable({"getCookingById"})
    public SalerCookingReqDate getCookingById(long cookingId) {
        SalerCookingReqDate sc = new SalerCookingReqDate();
        sc.setId(0);
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
                sc.setAuthorId(c.getAuthorId());

                sc.setNutritionContent(getContentByCookingId(cookingId));
                sc.setFoodContent(getFoodByCookingId(cookingId));
                break;
            }
        }
        return sc;
    }

    /**
     * 通过名称模糊查询菜品名称列表
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

    /**
     * 添加菜品
     * @param cookingReqDate 前端请求数据
     * @return
     */
    public boolean saveCooking(SalerCookingReqDate cookingReqDate,String userId){
        int state = 0;
        Cooking cooking = new Cooking();
        Hashtable<String,Float> cookingNutritions = new Hashtable<>();
        try{
            //将菜品信息存入菜品表
            //cooking.setId(cookingReqDate.getId());
            cooking.setName(cookingReqDate.getName());
            cooking.setOtherName(cookingReqDate.getOtherName());
            cooking.setTaste(cookingReqDate.getTaste());
            cooking.setKind(cookingReqDate.getKind());
            cooking.setStyle(cookingReqDate.getStyle());
            cooking.setFeature(cookingReqDate.getFeature());
            cooking.setHowToCook(cookingReqDate.getHowToCook());
            cooking.setAuthorId(userId);
            cookingRepository.save(cooking);
            state = 1;
            //加入内存
            cooking.setId(cookingList.size()+1);
            cookingList.add(cooking);
            state = 2;

            //将菜品食材用量存入菜品-食材表,并统计菜品营养含量
            List<Cooking_Food> cookingFoods = new ArrayList<>();
            //遍历前端请求数据中,菜品选用食材List
            List<FoodContent> foodContents = cookingReqDate.getFoodContent();
            Cooking_Food cf=new Cooking_Food();
            for (int i=0;i<foodContents.size();i++){
                cf.setUionPK(new CookingFoodUionPK(foodContents.get(i).foodId,cooking.getId()));
                cf.setContent(foodContents.get(i).content);
                cookingFoods.add(cf);
                //加入内存
                cookingFoodList.add(cf);
                List<NutritionContent> nutritionContents = getContentByFoodId(foodContents.get(i).foodId);
                for(int j=0;j<nutritionContents.size();j++){
                    if(cookingNutritions.get(nutritionContents.get(j).nutritionId) != null) {
                        cookingNutritions.put(nutritionContents.get(j).nutritionId
                                , nutritionContents.get(j).content + cookingNutritions.get(j));
                    }else{
                        cookingNutritions.put(nutritionContents.get(j).nutritionId
                                , nutritionContents.get(j).content);
                    }
                }
            }
            //将菜品食材用量存入菜品-食材表
            cookingFoodRepository.save(cookingFoods);
            state = 3;

            //菜品-营养含量存入数据库
            List<Cooking_Nutrition> cooking_Nutritions = new ArrayList<>();
            Cooking_Nutrition cn = new Cooking_Nutrition();
            for(Integer k = 1;k<nutritionCount;k++){
                if(cookingNutritions.get(k.toString())!=null){
                    cn.setUionPK(new CookingNutritionUionPK(cooking.getId(),k.toString()));
                    cn.setContent(cookingNutritions.get(k.toString()));
                    cooking_Nutritions.add(cn);
                    cookingNutritionHashTable.addNewItem(k.toString(),cooking.getId(),cn.getContent());
                }
            }
            state = 4;
            cookingNutritionRepository.save(cooking_Nutritions);

        }catch (Exception E){
            System.out.println("save cooking error state:"+state);
            if (state>=1){
                //恢复菜品数据库 到添加菜品请求之前
                cookingRepository.delete(cooking);
            }
            if (state>=2){
                //恢复菜品list 到添加菜品请求之前
                cookingList.remove(cooking);
            }
            if (state>=3){
                List<Cooking_Food> cookingFoods = new ArrayList<>();

                //恢复菜品选用食材List到添加菜品请求之前
                List<FoodContent> foodContents = cookingReqDate.getFoodContent();
                Cooking_Food cf=new Cooking_Food();
                for (int i=0;i<foodContents.size();i++){
                    cf.setUionPK(new CookingFoodUionPK(foodContents.get(i).foodId,cooking.getId()));
                    cf.setContent(foodContents.get(i).content);
                    cookingFoods.add(cf);
                    //移出内存
                    cookingFoodList.remove(cf);
                    List<NutritionContent> nutritionContents = getContentByFoodId(foodContents.get(i).foodId);
                    for(int j=0;j<nutritionContents.size();j++){
                        if(cookingNutritions.get(nutritionContents.get(j).nutritionId) != null) {
                            cookingNutritions.put(nutritionContents.get(j).nutritionId
                                    , nutritionContents.get(j).content + cookingNutritions.get(j));
                        }else{
                            cookingNutritions.put(nutritionContents.get(j).nutritionId
                                    , nutritionContents.get(j).content);
                        }
                    }
                }
                //恢复菜品-食材表
                cookingFoodRepository.delete(cookingFoods);
            }
            if (state>=4){
                List<Cooking_Nutrition> cooking_Nutritions = new ArrayList<>();
                Cooking_Nutrition cn = new Cooking_Nutrition();
                for(Integer k = 1;k<nutritionCount;k++){
                    if(cookingNutritions.get(k.toString())!=null){
                        cn.setUionPK(new CookingNutritionUionPK(cooking.getId(),k.toString()));
                        cn.setContent(cookingNutritions.get(k.toString()));
                        cooking_Nutritions.add(cn);
                        cookingNutritionHashTable.removeItem(k.toString(), cooking.getId());
                    }
                }
                cookingNutritionRepository.delete(cooking_Nutritions);
            }
            return  false;
        }
        return true;
    }

    /**
     * 计算多种菜品营养总含量
     * @param salerUserCookingReqDate
     * @return
     */
    @Cacheable({"computeTodayNtrition"})
    public List<NutritionContent> getTodayNutrition(SalerUserCookingReqDate salerUserCookingReqDate){
        List<NutritionContent> ncl = new ArrayList<>();
        NutritionContent nc;
        Nutrition n;
        float content;

        for (Integer i=1;i<=nutritionCount;i++){
            n = getNutritionById(i.toString());
            nc = new NutritionContent(n.getId(), n.getName(), 0, n.getUnit());
            for(int j=0;j<salerUserCookingReqDate.cookingContent.size();j++){
                content=cookingNutritionHashTable.getContent(i.toString(),
                        salerUserCookingReqDate.cookingContent.get(j).cookingId);
                if (content!=-1){
                    nc.content+=content
                            *salerUserCookingReqDate.cookingContent.get(j).content
                            /salerUserCookingReqDate.cookingContent.get(j).numb;
                }
            }
            ncl.add(nc);
        }
        return ncl;
    }

    @Cacheable({"smartDietician"})
    public SalerCookingReqDate smartDietician(List<NutritionContent> nutritionContentList){
        int maxLevel = nutritionContentList.size();
        int result = 0; //智能匹配出的菜品编号
        Hashtable<Integer,Float> cookingLevel = new Hashtable<>();//记录菜品等级
        //菜品评级
        for(int i=0;i<cookingList.size();i++){
            long nowCookingId = cookingList.get(i).getId();
            float needingContent = nutritionContentList.get(i).content;
            float level = 0;
            for(Integer j=0;j<maxLevel;j++){
                float content = cookingNutritionHashTable.getContent(j.toString(),nowCookingId);
                if(content!=-1){
                    if(content>=needingContent){
                        level+=1;
                    }else if(content<needingContent&&content>0){
                        level+=(content/needingContent);
                    }
                }
            }
            if(level==maxLevel){
                result = i;
                break;
            }
            else{
                cookingLevel.put(i,level);
            }
        }

        int secondaryResult = 0;
        for(int k=0;k<cookingList.size();k++){
            if(cookingLevel.get(k)>secondaryResult)
                secondaryResult = k;
        }



        return getCookingById(result);
    }

//######################### 内部调用函数 ############################################

    /**
     * 内部调用,获取某种食材的营养含量
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

    /**
     * 内部调用,获取某种菜品的食材用量
     * @param id
     * @return
     */
    public List<FoodContent> getFoodByCookingId(long id){
        List<FoodContent> fcl = new ArrayList<>();
        FoodContent fc;
        Cooking_Food cf = new Cooking_Food();

        for(int i = 0;i< cookingFoodList.size();i++){
            cf = cookingFoodList.get(i);
            if (cf.getUionPK().getCookingId()==id){
                fc = new FoodContent(cf.getUionPK().getFoodId(),
                        getFoodById(id).getName(),
                        cf.getContent());
                fcl.add(fc);
            }
        }
        return fcl;
    }
}
