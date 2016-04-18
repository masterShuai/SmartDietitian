package cn.smartDietician.backEnd.domain.repository;

import cn.smartDietician.backEnd.domain.entity.Cooking_Food;
import cn.smartDietician.backEnd.domain.uionPK.CookingFoodUionPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/15.
 */
@Repository
public interface CookingFoodRepository extends CrudRepository<Cooking_Food,CookingFoodUionPK> {

}
