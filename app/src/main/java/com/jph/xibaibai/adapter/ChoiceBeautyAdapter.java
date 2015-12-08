package com.jph.xibaibai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Product;

import java.util.List;

/**
 * Created by Eric on 2015/12/4.
 * 已经选中diy项目
 */
public class ChoiceBeautyAdapter extends BaseAdapter {

    private List<Product> productList;

    private int carType;

    public ChoiceBeautyAdapter(List<Product> productList,int carType){
        this.productList = productList;
        this.carType = carType;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diy_onetype,null);
            holder.diy_oproject_name = (TextView) convertView.findViewById(R.id.diy_oproject_name);
            holder.diy_project_price = (TextView) convertView.findViewById(R.id.diy_project_price);
            holder.diy_project_check = (ImageView) convertView.findViewById(R.id.diy_project_check);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.diy_project_check.setVisibility(View.GONE);
        Product bean = productList.get(position);
        holder.diy_oproject_name.setText(bean.getP_name());
        if(carType == 0){
            holder.diy_project_price.setText("￥"+bean.getP_price());
        }else {
            holder.diy_project_price.setText("￥"+bean.getP_price2());
        }
        return convertView;
    }
    class ViewHolder{
        /**美容名称*/
        private TextView diy_oproject_name;
        /**美容价格*/
        private TextView diy_project_price;
        /**美容选中按钮*/
        private ImageView diy_project_check;
    }
}
