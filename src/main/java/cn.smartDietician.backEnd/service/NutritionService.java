package cn.smartDietician.backEnd.service;

import cn.smartDietician.backEnd.domain.entity.Food;
import cn.smartDietician.backEnd.domain.entity.Nutrition;
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
@CacheConfig(cacheNames = {"getNutritionById","getAllNutrition","getAllFood","getFoodById"})
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;
    private FoodRepository foodRepository;

    private List<Nutrition> nutritionList = new ArrayList<>();
    private List<Food> foodList = new ArrayList<>();

    @PostConstruct
    public void Init() {
        //iterable转List
        CollectionHelper<Nutrition> helper = new CollectionHelper<>();
        //读取全部营养元素
        nutritionList = helper.iterableToList(nutritionRepository.findAll());
        //读取全部食材
        foodList = helper.iterableToList(foodRepository.findAll());

        System.out.println(String.format("加载到营养元素数量：%d", nutritionList.size()));
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
        List<SalerNutritionListReqData> SalerNutritionList = new ArrayList<>();
        SalerNutritionListReqData sn = new SalerNutritionListReqData();
        for(int i=0;i<nutritionList.size();i++) {
            sn.setNutritionId(nutritionList.get(i).getId());
            sn.setNutritionName(nutritionList.get(i).getName());
            SalerNutritionList.add(sn);
            sn = new SalerNutritionListReqData();
        }
        return SalerNutritionList;
    }

    public List<SalerNutritionListReqData> getNutritionByName(String nutritionName) {
        List<SalerNutritionListReqData> SalerNutritionList = new ArrayList<>();
        SalerNutritionListReqData sn = new SalerNutritionListReqData();
        for(int i=0;i<nutritionList.size();i++){
            nutritionList.get(i);
            if(nutritionList.get(i).getName().contains(nutritionName.trim())) {
                sn.setNutritionId(nutritionList.get(i).getId());
                sn.setNutritionName(nutritionList.get(i).getName());
                SalerNutritionList.add(sn);

                sn = new SalerNutritionListReqData();
            }
        }
        return SalerNutritionList;
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
        SalerFoodReqDate f = null;
        for (int i=0;i<foodList.size();i++){
            if (foodList.get(i).getId()==foodId){

                break;
            }
        }
        return f;
    }

}
