package com.jph.xibaibai.model.entity;

import java.io.Serializable;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/12 16:26
 * 描述：我的优惠券
 */
public class MyGiftCoupon implements Serializable{

    private String changeCouponUrl;
    private String couponAmount;
    private String pointsAmount;

    public String getChangeCouponUrl() {
        return changeCouponUrl;
    }

    public void setChangeCouponUrl(String changeCouponUrl) {
        this.changeCouponUrl = changeCouponUrl;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getPointsAmount() {
        return pointsAmount;
    }

    public void setPointsAmount(String pointsAmount) {
        this.pointsAmount = pointsAmount;
    }
}
