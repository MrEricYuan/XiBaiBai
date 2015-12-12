package com.jph.xibaibai.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/10 13:35
 * 描述：$TODO
 */
public class MyOrderInformation implements Serializable {
    private String orderId;
    private int state;
    private String orderSate;

    private String orderNo;
    private String orderTime;
    private String driverName;
    private String carInfo;
    private String carplateNo;
    private String carType;
    private String payType;
    private String carLocation;
    private String serviceTime;

    private OrderComment comment;

    private String artificerName;
    private String artificerTel;
    private List<String> beforeAlbum;
    private List<String> afterAlbum;
    private List<ArtificerRecommand> recommandList;

    private String orderPrice = "0.0";
    private String payPrice = "0.0";
    private String couponOffset="0.0";
    private List<Product> serviceList;

    private int commentLevel;
    private String commentContent;

    public int getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(int commentLevel) {
        this.commentLevel = commentLevel;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCouponOffset() {
        return couponOffset;
    }

    public void setCouponOffset(String couponOffset) {
        this.couponOffset = couponOffset;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderSate() {
        return orderSate;
    }

    public void setOrderSate(String orderSate) {
        this.orderSate = orderSate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public String getCarplateNo() {
        return carplateNo;
    }

    public void setCarplateNo(String carplateNo) {
        this.carplateNo = carplateNo;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarLocation() {
        return carLocation;
    }

    public void setCarLocation(String carLocation) {
        this.carLocation = carLocation;
    }

    public OrderComment getComment() {
        return comment;
    }

    public void setComment(OrderComment comment) {
        this.comment = comment;
    }

    public String getArtificerName() {
        return artificerName;
    }

    public void setArtificerName(String artificerName) {
        this.artificerName = artificerName;
    }

    public String getArtificerTel() {
        return artificerTel;
    }

    public void setArtificerTel(String artificerTel) {
        this.artificerTel = artificerTel;
    }

    public List<String> getBeforeAlbum() {
        return beforeAlbum;
    }

    public void setBeforeAlbum(List<String> beforeAlbum) {
        this.beforeAlbum = beforeAlbum;
    }

    public List<String> getAfterAlbum() {
        return afterAlbum;
    }

    public void setAfterAlbum(List<String> afterAlbum) {
        this.afterAlbum = afterAlbum;
    }

    public List<ArtificerRecommand> getRecommandList() {
        return recommandList;
    }

    public void setRecommandList(List<ArtificerRecommand> recommandList) {
        this.recommandList = recommandList;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public List<Product> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Product> serviceList) {
        this.serviceList = serviceList;
    }
}
