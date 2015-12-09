package com.jph.xibaibai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.MyOrder;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/8 15:29
 * 描述：$TODO
 */
public class MyOrderAdapter extends BaseAdapter {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.myorder_item_layout, null);
            holder.order_item_name = (TextView) convertView.findViewById(R.id.order_item_name);
            holder.order_item_state = (TextView) convertView.findViewById(R.id.order_item_state);
            holder.order_item_time = (TextView) convertView.findViewById(R.id.order_item_time);
            holder.order_item_carinfo = (TextView) convertView.findViewById(R.id.order_item_carinfo);
            holder.order_item_cartype = (TextView) convertView.findViewById(R.id.order_item_cartype);
            holder.order_item_carplateno = (TextView) convertView.findViewById(R.id.order_item_carplateno);
            holder.order_item_location = (TextView) convertView.findViewById(R.id.order_item_location);
            holder.order_common_btn = (TextView) convertView.findViewById(R.id.order_common_btn);
            holder.order_special_btn = (TextView) convertView.findViewById(R.id.order_special_btn);
            holder.order_item_price = (TextView) convertView.findViewById(R.id.order_item_price);
            holder.order_special_btn.setVisibility(View.GONE);
            holder.order_common_btn.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyOrder myOrder = myOrderList.get(position);
        if (myOrder != null) {
            holder.order_item_name.setText(myOrder.getOrderName());
            holder.order_item_time.setText(myOrder.getOrderTime());
            holder.order_item_carinfo.setText(myOrder.getCarInfo());
            holder.order_item_cartype.setText(myOrder.getCarType());
            holder.order_item_carplateno.setText(myOrder.getCarPlateNo());
            holder.order_item_location.setText(myOrder.getCarLocation());
            holder.order_item_price.setText(myOrder.getPrice());
            holder.order_item_state.setText(myOrder.getState());
            switch (myOrder.getCurrentState()) {
                case 0:
                    holder.order_special_btn.setText(mContext.getString(R.string.myorder_pay));
                    holder.order_special_btn.setVisibility(View.VISIBLE);
                    holder.order_common_btn.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    holder.order_common_btn.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
            }
        }
        return convertView;
    }

    class ViewHolder {
        private TextView order_item_name;
        private TextView order_item_state;
        private TextView order_item_time;
        private TextView order_item_carinfo;
        private TextView order_item_cartype;
        private TextView order_item_carplateno;
        private TextView order_item_location;
        private TextView order_common_btn;
        private TextView order_special_btn;
        private TextView order_item_price;
    }
}
