package com.jph.xibaibai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.ui.activity.BeautyProductActivity;

import java.util.List;

/**
 * Created by Eric on 2015/12/5.
 * 美容项目适配器
 */
public class BeautyProductAdapter extends BaseAdapter {

    private BeautyProductActivity activity;

    private List<Product> beautyList;

    private int carType;

    private boolean[] beautyState = null;

    public BeautyProductAdapter(BeautyProductActivity activity,List<Product> beautyList,int carType) {
        this.activity = activity;
        this.beautyList = beautyList;
        this.carType = carType;
        initBeautyState();
    }

    private void initBeautyState(){
        beautyState = new boolean[beautyList.size()];
        for(int i=0;i<beautyList.size();i++){
            beautyState[i] = false;
        }
    }

    public boolean[] getBeautyState() {
        return beautyState;
    }

    public void setBeautyState(boolean[] beautyState) {
        this.beautyState = beautyState;
    }

    @Override
    public int getCount() {
        return beautyList.size();
    }

    @Override
    public Object getItem(int position) {
        return beautyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beauty_product,null);
            holder.no_sort_layout = (LinearLayout) convertView.findViewById(R.id.no_sort_layout);
            holder.sort_layout = (LinearLayout) convertView.findViewById(R.id.sort_layout);
            holder.beauty_product_name = (TextView) convertView.findViewById(R.id.beauty_product_name);
            holder.beauty_genalcar_price = (TextView) convertView.findViewById(R.id.beauty_genalcar_price);
            holder.beauty_bigcar_price = (TextView) convertView.findViewById(R.id.beauty_bigcar_price);
            holder.beauty_genalcar_check = (ImageView) convertView.findViewById(R.id.beauty_genalcar_check);
            holder.beauty_bigcar_check = (ImageView) convertView.findViewById(R.id.beauty_bigcar_check);
            holder.b_project_name = (TextView) convertView.findViewById(R.id.diy_oproject_name);
            holder.b_project_price = (TextView) convertView.findViewById(R.id.diy_project_price);
            holder.b_project_check = (ImageView) convertView.findViewById(R.id.diy_project_check);
            holder.beauty_genalcar_rl = (RelativeLayout) convertView.findViewById(R.id.beauty_genalcar_rl);
            holder.beauty_bigcar_rl = (RelativeLayout) convertView.findViewById(R.id.beauty_bigcar_rl);
            holder.b_project_rl = (RelativeLayout) convertView.findViewById(R.id.diy_project_rl);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = beautyList.get(position);
        if(product.getP_price() == product.getP_price2()){
            holder.no_sort_layout.setVisibility(View.VISIBLE);
            holder.sort_layout.setVisibility(View.GONE);
            holder.b_project_name.setText(product.getP_name());
            holder.b_project_price.setText("￥"+product.getP_price());
            if(beautyState[position]){
                holder.b_project_check.setImageResource(R.mipmap.place_checked);
            }else {
                holder.b_project_check.setImageResource(R.mipmap.place_unchecked);
            }
        }else {
            holder.no_sort_layout.setVisibility(View.GONE);
            holder.sort_layout.setVisibility(View.VISIBLE);
            holder.beauty_product_name.setText(product.getP_name());
            holder.beauty_genalcar_price.setText("￥" + product.getP_price());
            holder.beauty_bigcar_price.setText("￥" + product.getP_price2());
            if(carType == 0){
                if(beautyState[position]){
                    holder.beauty_genalcar_check.setImageResource(R.mipmap.place_checked);
                }else {
                    holder.beauty_genalcar_check.setImageResource(R.mipmap.place_unchecked);
                }
            }else {
                if(beautyState[position]){
                    holder.beauty_bigcar_check.setImageResource(R.mipmap.place_checked);
                }else {
                    holder.beauty_bigcar_check.setImageResource(R.mipmap.place_unchecked);
                }
            }
        }
        holder.beauty_genalcar_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carType == 0){
                    activity.changeBeautyState(position);
                }
            }
        });
        holder.beauty_bigcar_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carType == 1){
                    activity.changeBeautyState(position);
                }
            }
        });
        holder.b_project_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeBeautyState(position);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private LinearLayout no_sort_layout;
        private LinearLayout sort_layout;
        // 分类型的
        private RelativeLayout beauty_genalcar_rl;
        private RelativeLayout beauty_bigcar_rl;
        private TextView beauty_product_name;
        private TextView beauty_genalcar_price;
        private TextView beauty_bigcar_price;
        private ImageView beauty_genalcar_check;
        private ImageView beauty_bigcar_check;
        // 不分类型的
        private RelativeLayout b_project_rl;
        private TextView b_project_name;
        private TextView b_project_price;
        private ImageView b_project_check;
    }
}
