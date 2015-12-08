package com.jph.xibaibai.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Product;

import java.util.List;

/**
 * Created by Eric on 2015/12/4.
 * 单个DIY产品适配器
 */
public class DIYOneTypeAdapter extends BaseAdapter {

    private List<Product> oneTypeDIYList;

    private boolean[] oneTypeState = null;

    public DIYOneTypeAdapter(List<Product> oneTypeDIYList){
        this.oneTypeDIYList = oneTypeDIYList;
        initCheckState();
    }

    private void initCheckState(){
        oneTypeState = new boolean[oneTypeDIYList.size()];
        for(int i = 0;i < oneTypeDIYList.size();i++){
            oneTypeState[i] = false;
        }
    }

    public boolean[] getOneTypeState() {
        return oneTypeState;
    }

    public void setOneTypeState(boolean[] oneTypeState) {
        this.oneTypeState = oneTypeState;
    }

    @Override
    public int getCount() {
        return oneTypeDIYList.size();
    }

    @Override
    public Object getItem(int position) {
        return oneTypeDIYList.get(position);
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
            holder.diy_project_rl = (RelativeLayout) convertView.findViewById(R.id.diy_project_rl);
            holder.diy_oproject_name = (TextView) convertView.findViewById(R.id.diy_oproject_name);
            holder.diy_project_price = (TextView) convertView.findViewById(R.id.diy_project_price);
            holder.diy_project_check = (ImageView) convertView.findViewById(R.id.diy_project_check);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product bean = oneTypeDIYList.get(position);
        holder.diy_oproject_name.setText(bean.getP_name());
        holder.diy_project_price.setText("￥"+bean.getP_price());
        if(oneTypeState[position]){
            holder.diy_project_check.setImageResource(R.mipmap.place_checked);
        }else {
            holder.diy_project_check.setImageResource(R.mipmap.place_unchecked);
        }
        return convertView;
    }

    class ViewHolder{
        /**单类型DIY选中*/
        private RelativeLayout diy_project_rl;
        /**单类型DIY名称*/
        private TextView diy_oproject_name;
        /**单类型DIY价格*/
        private TextView diy_project_price;
        /**单类型DIY选中按钮*/
        private ImageView diy_project_check;
    }
}
