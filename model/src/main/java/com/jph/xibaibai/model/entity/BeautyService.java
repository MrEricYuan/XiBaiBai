package com.jph.xibaibai.model.entity;

import java.util.List;

/**
 * Created by Eric on 2015/11/9.
 * 美容服务项目
 */
public class BeautyService {
    /**打蜡的种类的封装*/
    private List<Product> waxList;
    /**非必须洗车服务的封装*/
    private List<Product> notWashList;

    public List<Product> getWaxList() {
        return waxList;
    }

    public void setWaxList(List<Product> waxList) {
        this.waxList = waxList;
    }

    public List<Product> getNotWashList() {
        return notWashList;
    }

    public void setNotWashList(List<Product> notWashList) {
        this.notWashList = notWashList;
    }
}
