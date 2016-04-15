package cn.smartDietician.backEnd.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by wangshuai on 2016/4/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Nutrition {
    @Id
    private String id;//营养编号
    @Column(nullable = false)
    private String name;//营养名称
    @Column(nullable = false)
    private String unit;//计量单位
    @Column
    private String lack_harm;//缺乏危害
    @Column
    private String excess_harm;//过量危害
    @Column
    private String contain_food;//富含该营养元素的食物


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLack_harm() {
        return lack_harm;
    }

    public void setLack_harm(String lack_harm) {
        this.lack_harm = lack_harm;
    }

    public String getExcess_harm() {
        return excess_harm;
    }

    public void setExcess_harm(String excess_harm) {
        this.excess_harm = excess_harm;
    }

    public String getContain_food() {
        return contain_food;
    }

    public void setContain_food(String contain_food) {this.contain_food = contain_food;}
}
