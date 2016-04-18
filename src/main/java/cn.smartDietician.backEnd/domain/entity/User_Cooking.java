package cn.smartDietician.backEnd.domain.entity;

import cn.smartDietician.backEnd.domain.uionPK.UserCookingUionPK;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


/**
 * Created by wangshuai on 2016/4/4.
 * 菜品包含营养类,用于在Service中通过菜品编号,查找其营养元素含量
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User_Cooking {
    @Id
    UserCookingUionPK uionPK;//(用户名,菜品编号)主键

    @Column(nullable = false)
    private Date time;//日期

    @Column(nullable = false)
    private int people;//食用人数

    @Column(nullable = false)
    private  int content;//份数

    public UserCookingUionPK getUionPK() {
        return uionPK;
    }

    public void setUionPK(UserCookingUionPK uionPK) {
        this.uionPK = uionPK;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }
}
