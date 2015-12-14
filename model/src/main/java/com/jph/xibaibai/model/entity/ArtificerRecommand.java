package com.jph.xibaibai.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/10 13:51
 * 描述：$TODO
 */
public class ArtificerRecommand implements Serializable{
    private Product diyData;
    private List<String> explainAlbum;
    private String remark;

    public Product getDiyData() {
        return diyData;
    }

    public void setDiyData(Product diyData) {
        this.diyData = diyData;
    }

    public List<String> getExplainAlbum() {
        return explainAlbum;
    }

    public void setExplainAlbum(List<String> explainAlbum) {
        this.explainAlbum = explainAlbum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
