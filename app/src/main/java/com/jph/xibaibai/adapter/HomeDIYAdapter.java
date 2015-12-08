package com.jph.xibaibai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.utils.Constants;
import com.jph.xibaibai.utils.MImageLoader;
import com.jph.xibaibai.utils.SystermUtils;

import java.util.List;

/**
 * Created by Eric on 2015/12/7.
 * 首页DIY项目适配器
 */
public class HomeDIYAdapter extends BaseAdapter {

    private List<Product> diySubyList;

    public HomeDIYAdapter(List<Product> diySubyList){
        this.diySubyList = diySubyList;
    }

    @Override
    public int getCount() {
        return diySubyList.size();
    }

    @Override
    public Object getItem(int position) {
        return diySubyList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_diys,null);
            holder.item_diys_pic = (ImageView) convertView.findViewById(R.id.item_diys_pic);
            holder.item_diys_name = (TextView) convertView.findViewById(R.id.item_diys_name);
            holder.item_diys_price = (TextView) convertView.findViewById(R.id.item_diys_price);
            holder.item_diys_introduce = (TextView) convertView.findViewById(R.id.item_diys_introduce);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = diySubyList.get(position);
        MImageLoader.getInstance(parent.getContext()).displayImage(Constants.BASE_URL + SystermUtils.replacePicpath(product.getP_picPath()), holder.item_diys_pic);
        holder.item_diys_name.setText(product.getP_name());
        holder.item_diys_price.setText(product.getP_price()+"元");
        holder.item_diys_introduce.setText(product.getP_info());
        return convertView;
    }
    class ViewHolder{
        private ImageView item_diys_pic;
        private TextView item_diys_name;
        private TextView item_diys_price;
        private TextView item_diys_introduce;
    }
}
