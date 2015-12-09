package com.jph.xibaibai.utils.parsejson;

import com.jph.xibaibai.model.entity.MyOrder;
import com.jph.xibaibai.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/9 11:56
 * 描述：订单解析类
 */
public class OrderParse {

    private static OrderParse instance=null;

    public static OrderParse getInstance(){
        if (instance==null)
            instance=new OrderParse();
        return instance;
    }

    public List<MyOrder> parseMyOrderList(String data){
        if (StringUtil.isNull(data))
            return null;
        List<MyOrder> myOrderList=null;
        try {
            JSONArray mArray=new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myOrderList;
    }
}
