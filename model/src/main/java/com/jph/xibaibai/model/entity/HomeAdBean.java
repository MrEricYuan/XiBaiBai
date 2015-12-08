package com.jph.xibaibai.model.entity;

/**
 * Created by Eric on 2015/12/2.
 * 首页广告的JavaBean
 */
public class HomeAdBean {
    private String adId;
    private String adName;
    private String adPath;
    private String adLinkPath;

    public String getAdPath() {
        return adPath;
    }

    public void setAdPath(String adPath) {
        this.adPath = adPath;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdLinkPath() {
        return adLinkPath;
    }

    public void setAdLinkPath(String adLinkPath) {
        this.adLinkPath = adLinkPath;
    }
}
