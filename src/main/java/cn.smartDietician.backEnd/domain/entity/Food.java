package cn.smartDietician.backEnd.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by wangshuai on 2016/4/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity

//营养物质类
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;//食材编号
    @Column(nullable = false)
    private String name;//食材名称
    @Column
    private String city;//产地
    @Column
    private String other_name;//食材别名
    @Column
    private String kind;//类型
    @Column
    private String foo_value;//营养价值
    @Column
    private String diet;//忌食
    @Column
    private String unit;//食材计量单位
    @Column
    private String can_eat;//可否生食

    public String getCan_eat() {
        return can_eat;
    }

    public void setCan_eat(String can_eat) {
        this.can_eat = can_eat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getFoo_value() {
        return foo_value;
    }

    public void setFoo_value(String foo_value) {
        this.foo_value = foo_value;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
