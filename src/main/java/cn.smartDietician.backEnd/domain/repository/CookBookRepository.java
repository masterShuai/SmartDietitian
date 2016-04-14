package cn.smartDietician.backEnd.domain.repository;


import cn.smartDietician.backEnd.domain.entity.Nutrition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wangshuai on 2016/4/13.
 * 营养元素表数据库操作
 */

@Repository
  public interface CookBookRepository extends CrudRepository<Nutrition,String> {

}
