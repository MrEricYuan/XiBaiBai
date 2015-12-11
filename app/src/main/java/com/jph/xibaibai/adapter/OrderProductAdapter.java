package com.jph.xibaibai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Product;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/10 15:27
 * 描述：$TODO
 */
public class OrderProductAdapter extends BaseAdapter {
    private List<Product> products;
    private Context mContext;

    public OrderProductAdapter(List<Product> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.order_pro_item_layout, null);
            holder.order_ppro_item_name= (TextView) convertView.findViewById(R.id.order_ppro_item_name);
            holder.order_ppro_item_price= (TextView) convertView.findViewById(R.id.order_ppro_item_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        Product product=products.get(position);
        if (product!=null){
            holder.order_ppro_item_name.setText(product.getP_name());
            holder.order_ppro_item_price.setText(mContext.getString(R.string.sign_yuan)+product.getP_price());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView order_ppro_item_price;
        private TextView order_ppro_item_name;
    }
}
