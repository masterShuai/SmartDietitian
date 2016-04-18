package cn.smartDietician.backEnd.domain.repository;

import cn.smartDietician.backEnd.domain.entity.User_Nutrition_Min;
import cn.smartDietician.backEnd.domain.uionPK.UserNutritionUionPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/15.
 */
@Repository
public interface UserNutritionMinRepository extends CrudRepository<User_Nutrition_Min,UserNutritionUionPK> {

}
