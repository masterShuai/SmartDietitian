package cn.smartDietician.backEnd.service;

import cn.smartDietician.backEnd.domain.entity.Nutrition;
import cn.smartDietician.backEnd.domain.repository.NutritionRepository;
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
@CacheConfig(cacheNames = {"allNutrition"})
        public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;

    private List<Nutrition> nutritionList =new ArrayList<>();

    @PostConstruct
    public void Init(){
       CollectionHelper<Nutrition> helper = new CollectionHelper<>();
        nutritionList = helper.iterableToList(nutritionRepository.findAll()) ;


        System.out.println(String.format("加载到营养元素数量：%d", nutritionList.size()));
    }

    @Cacheable({"allNutrition"})
    public Nutrition getNutritionByName() {
        Nutrition n = null;

        return n;
    }
}
