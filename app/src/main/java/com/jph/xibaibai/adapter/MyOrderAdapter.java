package com.jph.xibaibai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.MyOrder;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/8 15:29
 * 描述：$TODO
 */
public class MyOrderAdapter extends BaseAdapter{
    private List<MyOrder> myOrderList;
    private Context mContext;

    public MyOrderAdapter(List<MyOrder> myOrderList, Context mContext) {
        this.myOrderList = myOrderList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return myOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return myOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.myorder_item_layout,null);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{

    }
}
