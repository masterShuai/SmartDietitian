package cn.smartDietician.backEnd.domain.repository;

import cn.smartDietician.backEnd.domain.entity.Cooking_Nutrition;
import cn.smartDietician.backEnd.domain.uionPK.CookingNutritionUionPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/15.
 */
@Repository
public interface CookingNutritionRepository extends CrudRepository<Cooking_Nutrition,CookingNutritionUionPK> {

}
