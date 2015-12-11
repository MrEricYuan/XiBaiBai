package com.jph.xibaibai.model.entity;

import java.io.Serializable;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/11 17:37
 * 描述：$TODO
 */
public class ConfirmPay implements Serializable{
    private String payPrice="0.0";
    private String couponPrice="0.0";
    private String extra;

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
