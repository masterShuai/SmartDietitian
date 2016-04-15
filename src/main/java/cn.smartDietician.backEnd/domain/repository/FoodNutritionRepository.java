package cn.smartDietician.backEnd.domain.repository;

import cn.smartDietician.backEnd.domain.entity.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/15.
 */
@Repository
public interface FoodNutritionRepository extends CrudRepository<Food,Long> {

}
