package cn.smartDietician.backEnd.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by wangshuai on 2016/4/4.
 * 菜品类,用于数据库操作参照实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class CookBook {
    @Id
    private String id;//菜品编号
    @Column
    private String name;//菜品名称
    @Column
    private String otherName;//菜品别名
    @Column
    private String taste;//口味
    @Column
    private String kind;//种类
    @Column
    private String style;//菜系
    @Column
    private String feature;//特色
    @Column
    private String howToCook;//做法
}
