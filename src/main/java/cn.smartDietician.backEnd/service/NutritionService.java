package cn.smartDietician.backEnd.service;

import cn.smartDietician.backEnd.domain.entity.Nutrition;
import cn.smartDietician.backEnd.domain.repository.NutritionRepository;
import cn.smartDietician.backEnd.protocol.SalerNutritionReqData;
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
@CacheConfig(cacheNames = {"getNutritionByName","getNutritionById","getAllNutrition"})
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;

    private List<Nutrition> nutritionList = new ArrayList<>();

    @PostConstruct
    public void Init() {
        CollectionHelper<Nutrition> helper = new CollectionHelper<>();
        nutritionList = helper.iterableToList(nutritionRepository.findAll());


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

    @Cacheable({"getNutritionByName"})
    public List<SalerNutritionReqData> getNutritionByName(String nutritionName) {
        List<SalerNutritionReqData> SalerNutritionList = new ArrayList<>();
        SalerNutritionReqData sn = new SalerNutritionReqData();
        for(int i=0;i<nutritionList.size();i++){
            nutritionList.get(i);
            if(nutritionList.get(i).getName().contains(nutritionName.trim())) {
                sn.setNutritionId(nutritionList.get(i).getId());
                sn.setNutritionName(nutritionList.get(i).getName());
                SalerNutritionList.add(sn);

                sn = new SalerNutritionReqData();
            }
        }
        return SalerNutritionList;
    }
    @Cacheable({"getAllNutrition"})
    public List<SalerNutritionReqData> getAllNutrition(){
        List<SalerNutritionReqData> SalerNutritionList = new ArrayList<>();
        SalerNutritionReqData sn = new SalerNutritionReqData();
        for(int i=0;i<nutritionList.size();i++) {
            sn.setNutritionId(nutritionList.get(i).getId());
            sn.setNutritionName(nutritionList.get(i).getName());
            SalerNutritionList.add(sn);
            sn = new SalerNutritionReqData();
        }
        return SalerNutritionList;
    }
}
