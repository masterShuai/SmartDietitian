package cn.smartDietician.backEnd.service;

import cn.smartDietician.backEnd.domain.entity.Food;
import cn.smartDietician.backEnd.domain.entity.Food_Nutrition;
import cn.smartDietician.backEnd.domain.entity.Nutrition;
import cn.smartDietician.backEnd.domain.entity.NutritionContent;
import cn.smartDietician.backEnd.domain.repository.FoodNutritionRepository;
import cn.smartDietician.backEnd.domain.repository.FoodRepository;
import cn.smartDietician.backEnd.domain.repository.NutritionRepository;
import cn.smartDietician.backEnd.protocol.SalerFoodListReqDate;
import cn.smartDietician.backEnd.protocol.SalerFoodReqDate;
import cn.smartDietician.backEnd.protocol.SalerNutritionListReqData;
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
        "getNutritionById",
        "getAllNutrition",
        "getAllFood",
        "getFoodById",
})
public class SerachService {

    @Autowired
    private NutritionRepository nutritionRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodNutritionRepository foodNutritionRepository;

    private List<Nutrition> nutritionList = new ArrayList<>();
    private List<Food> foodList = new ArrayList<>();
    private List<Food_Nutrition> foodNutritionList = new ArrayList<>();

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
        System.out.println(String.format("加载到食材-营养含量信息：%d", foodList.size()));

    }

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
     * 通过名称模糊查询营养元素
     * @param nutritionName
     * @return
     */
    public List<SalerNutritionListReqData> getNutritionByName(String nutritionName) {
        List<SalerNutritionListReqData> salerNutritionList = new ArrayList<>();
        SalerNutritionListReqData sn = new SalerNutritionListReqData();
        for(int i=0;i<nutritionList.size();i++){
            nutritionList.get(i);
            if(nutritionList.get(i).getName().contains(nutritionName.trim())) {
                sn.setNutritionId(nutritionList.get(i).getId());
                sn.setNutritionName(nutritionList.get(i).getName());
                salerNutritionList.add(sn);

                sn = new SalerNutritionListReqData();
            }
        }
        return salerNutritionList;
    }

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

    @Cacheable({"getFoodById"})
    public SalerFoodReqDate getFoodById(long foodId) {
        SalerFoodReqDate sf = null;
        Food f;
        for (int i=0;i<foodList.size();i++){
            f = foodList.get(i);
            if (f.getId()==foodId){
                sf.setId(foodId);
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
     * 通过食材id,获取食材的营养含量信息
     * @param id
     * @return
     */
    public List<NutritionContent> getContentByFoodId(long id){
        List<NutritionContent> ncl = new ArrayList<>();
        NutritionContent nc;
        Food_Nutrition fn = new Food_Nutrition();
        for(int i = 0;i< foodNutritionList.size();i++){
            fn = foodNutritionList.get(i);
            if (fn.getUionPK().getFoodid()==id){
                nc = new NutritionContent(fn.getUionPK().getNutritionid(),
                        getNutritionById(fn.getUionPK().getNutritionid()).getName(),
                        fn.getContent(),
                        getNutritionById(fn.getUionPK().getNutritionid()).getUnit());
                ncl.add(nc);
            }
        }
        return ncl;
    }
}
