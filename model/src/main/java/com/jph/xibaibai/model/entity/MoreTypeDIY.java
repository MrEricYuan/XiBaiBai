package com.jph.xibaibai.model.entity;

/**
 * Created by Eric on 2015/12/4.
 * 含有全车和半车的diy选项
 */
public class MoreTypeDIY {

    /**当前类型的名字*/
    private String groupName;
    /**全车类型*/
    private Product allCarType;
    /**半车类型*/
    private Product halfCarType;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Product getAllCarType() {
        return allCarType;
    }

    public void setAllCarType(Product allCarType) {
        this.allCarType = allCarType;
    }

    public Product getHalfCarType() {
        return halfCarType;
    }

    public void setHalfCarType(Product halfCarType) {
        this.halfCarType = halfCarType;
    }
}
