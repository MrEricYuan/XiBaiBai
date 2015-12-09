package com.jph.xibaibai.utils.parsejson;

import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eirc on 2015/12/9.
 */
public class WashCarPrice {
    public static List<Product> getWashPrice(String json){
        List<Product> productList = null;
        try {
            JSONObject job = new JSONObject(json);
            if(job.has("wash") && !StringUtil.isNull(job.getString("wash"))){
                productList = new ArrayList<>();
                JSONObject jsonObject = job.getJSONObject("wash");
                if(jsonObject.has("out") && !StringUtil.isNull(jsonObject.getString("out"))){
                    JSONObject jJob = jsonObject.getJSONObject("out");
                    Product product = null;
                    product = DIYSubParse.commonMethod(jJob);
                    productList.add(product);
                }
                if(jsonObject.has("outin") && !StringUtil.isNull(jsonObject.getString("outin"))){
                    JSONObject jJob = jsonObject.getJSONObject("outin");
                    Product product = null;
                    product = DIYSubParse.commonMethod(jJob);
                    productList.add(product);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
