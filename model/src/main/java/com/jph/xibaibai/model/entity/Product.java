package com.jph.xibaibai.model.entity;

import java.io.Serializable;

/**
 * Created by Eric on 2015/8/16.
 */
public class Product implements Serializable {
    private static final long serialVersionUID = -4832198051935101542L;
    /**产品id*/
    private int id;
    /**产品名称*/
    private String p_name;
    /**产品价格1*/
    private double p_price;
    /**产品价格2*/
    private double p_price2;
    /**产品介绍*/
    private String p_info;
    /**当前项目的详情介绍页*/
    private String linkPath;
    /**item中图片路径*/
    private String p_picPath;
    /**免费洗车产品*/
    private int p_freewash;

    public int getP_freewash() {
        return p_freewash;
    }

    public void setP_freewash(int p_freewash) {
        this.p_freewash = p_freewash;
    }

    public String getP_picPath() {
        return p_picPath;
    }

    public void setP_picPath(String p_picPath) {
        this.p_picPath = p_picPath;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public double getP_price2() {
        return p_price2;
    }

    public void setP_price2(double p_price2) {
        this.p_price2 = p_price2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public double getP_price() {
        return p_price;
    }

    public void setP_price(double p_price) {
        this.p_price = p_price;
    }

    public String getP_info() {
        return p_info;
    }

    public void setP_info(String p_info) {
        this.p_info = p_info;
    }
}
