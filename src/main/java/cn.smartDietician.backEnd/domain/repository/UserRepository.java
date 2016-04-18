package cn.smartDietician.backEnd.domain.repository;


import cn.smartDietician.backEnd.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/13.
 * 营养元素表数据库操作
 */

@Repository
public interface UserRepository extends CrudRepository<User,String> {
}
