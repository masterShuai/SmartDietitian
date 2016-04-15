package cn.smartDietician.backEnd.domain.repository;

import cn.smartDietician.backEnd.domain.entity.Food_Nutrition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/15.
 */
@Repository
public interface FoodNutritionRepository extends CrudRepository<Food_Nutrition,Long> {

}
