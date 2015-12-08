package com.jph.xibaibai.model.entity;

import java.util.List;

/**
 * Created by Eric on 2015/12/4.
 * DIY产品类型
 */
public class DIYSubBean {

    /**含有全车和半车的产品列表*/
    private List<MoreTypeDIY> moreTypeList;
    /**单个的产品列表*/
    private List<Product> oneTypeList;
    /**DIY单类型的状态保存*/
    private boolean[] diyOneTypeState;

    public boolean[] getDiyOneTypeState() {
        return diyOneTypeState;
    }

    public void setDiyOneTypeState(boolean[] diyOneTypeState) {
        this.diyOneTypeState = diyOneTypeState;
    }

    public List<MoreTypeDIY> getMoreTypeList() {
        return moreTypeList;
    }

    public void setMoreTypeList(List<MoreTypeDIY> moreTypeList) {
        this.moreTypeList = moreTypeList;
    }

    public List<Product> getOneTypeList() {
        return oneTypeList;
    }

    public void setOneTypeList(List<Product> oneTypeList) {
        this.oneTypeList = oneTypeList;
    }
}
