package cn.smartDietician.backEnd.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by wangshuai on 2016/4/4.
 * 菜品类,用于数据库操作参照实体类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Cooking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//菜品编号
    @Column(nullable = false)
    private String name;//菜品名称
    @Column
    private String otherName;//菜品别名
    @Column
    private String taste;//口味[酸 甜 苦 辣 咸]
    @Column
    private String kind;//类别[肉类 蔬菜水果 汤粥主食 水产 蛋奶豆制品 米面干果腌咸]
    @Column
    private String style;//菜系
    @Column
    private String feature;//简介
    @Column
    private String howToCook;//详细做法

    @Column
    private String authorId;//作者的用户id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getHowToCook() {
        return howToCook;
    }

    public void setHowToCook(String howToCook) {
        this.howToCook = howToCook;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
