package cn.smartDietician.backEnd.domain.repository;

import cn.smartDietician.backEnd.domain.entity.User_Nutrition_Max;
import cn.smartDietician.backEnd.domain.uionPK.UserNutritionUionPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/15.
 */
@Repository
public interface UserNutritionMaxRepository extends CrudRepository<User_Nutrition_Max,UserNutritionUionPK> {

}
