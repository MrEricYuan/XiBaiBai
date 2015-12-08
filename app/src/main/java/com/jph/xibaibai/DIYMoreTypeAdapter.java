package com.jph.xibaibai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.xibaibai.model.entity.MoreTypeDIY;
import com.jph.xibaibai.ui.activity.DIYSubActivity;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Eric on 2015/12/4.
 */
public class DIYMoreTypeAdapter extends BaseAdapter {

    private DIYSubActivity activity;

    private List<MoreTypeDIY> mTypeList;

    public DIYMoreTypeAdapter(DIYSubActivity activity, List<MoreTypeDIY> mTypeList) {
        this.activity = activity;
        this.mTypeList = mTypeList;
    }

    @Override
    public int getCount() {
        return mTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diy_moretype, null);
            holder.more_pj_name = (TextView) convertView.findViewById(R.id.more_pj_name);
            holder.diy_halfcar_price = (TextView) convertView.findViewById(R.id.diy_halfcar_price);
            holder.diy_halfcar_check = (ImageView) convertView.findViewById(R.id.diy_halfcar_check);
            holder.diy_allcar_price = (TextView) convertView.findViewById(R.id.diy_allcar_price);
            holder.diy_allcar_check = (ImageView) convertView.findViewById(R.id.diy_allcar_check);
            holder.diy_half_rl = (RelativeLayout) convertView.findViewById(R.id.diy_half_rl);
            holder.diy_all_rl = (RelativeLayout) convertView.findViewById(R.id.diy_all_rl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MoreTypeDIY bean = mTypeList.get(position);
        holder.more_pj_name.setText(bean.getGroupName());
        holder.diy_halfcar_price.setText("￥" + bean.getHalfCarType().getP_price());
        holder.diy_allcar_price.setText("￥" + bean.getAllCarType().getP_price());
        if (bean.getHalfCarType().isChecked()) {
            holder.diy_halfcar_check.setImageResource(R.mipmap.place_checked);
        } else {
            holder.diy_halfcar_check.setImageResource(R.mipmap.place_unchecked);
        }
        if (bean.getAllCarType().isChecked()) {
            holder.diy_allcar_check.setImageResource(R.mipmap.place_checked);
        } else {
            holder.diy_allcar_check.setImageResource(R.mipmap.place_unchecked);
        }
        holder.diy_half_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeMoreTypeState(position, 0);
            }
        });
        holder.diy_all_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeMoreTypeState(position, 1);
            }
        });
        return convertView;
    }

    class ViewHolder {
        /**
         * 此组的名称
         */
        private TextView more_pj_name;
        /**
         * 半车的价格
         */
        private TextView diy_halfcar_price;
        /**
         * 半车的复选按钮
         */
        private ImageView diy_halfcar_check;
        /**
         * 全车的价格
         */
        private TextView diy_allcar_price;
        /**
         * 全车的复选按钮
         */
        private ImageView diy_allcar_check;
        /**
         * 半车的选中
         */
        private RelativeLayout diy_half_rl;
        /**
         * 全车的选中
         */
        private RelativeLayout diy_all_rl;
    }
}
