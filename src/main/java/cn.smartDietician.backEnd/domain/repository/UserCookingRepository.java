package cn.smartDietician.backEnd.domain.repository;

import cn.smartDietician.backEnd.domain.entity.User_Cooking;
import cn.smartDietician.backEnd.domain.uionPK.UserCookingUionPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/15.
 */
@Repository
public interface UserCookingRepository extends CrudRepository<User_Cooking,UserCookingUionPK> {

}
