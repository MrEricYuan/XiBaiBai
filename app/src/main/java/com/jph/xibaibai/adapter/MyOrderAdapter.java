package com.jph.xibaibai.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.MyOrder;
import com.jph.xibaibai.ui.activity.OrderInfoActivity;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/8 15:29
 * 描述：$TODO
 */
public class MyOrderAdapter extends BaseAdapter {
    public int DELETE = 100;
    public int CANCEL = 101;
    public int PAY = 102;
    public int COMMENT = 103;
    private List<MyOrder> myOrderList;
    private Context mContext;
    private Handler handler;
    private android.os.Message msg;

    public MyOrderAdapter(List<MyOrder> myOrderList, Context mContext, Handler handler) {
        this.myOrderList = myOrderList;
        this.mContext = mContext;
        this.handler = handler;
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
            holder.order_item_orderNo = (TextView) convertView.findViewById(R.id.order_item_orderNo);
            holder.order_item_location = (TextView) convertView.findViewById(R.id.order_item_location);
            holder.order_common_btn = (TextView) convertView.findViewById(R.id.order_common_btn);
            holder.order_special_btn = (TextView) convertView.findViewById(R.id.order_special_btn);
            holder.order_item_price = (TextView) convertView.findViewById(R.id.order_item_price);
            holder.order_item_servicetime = (TextView) convertView.findViewById(R.id.order_item_servicetime);
            holder.order_special_btn.setVisibility(View.GONE);
            holder.order_common_btn.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.order_special_btn.setVisibility(View.GONE);
            holder.order_common_btn.setVisibility(View.GONE);
        }
        final MyOrder myOrder = myOrderList.get(position);
        if (myOrder != null) {
            holder.order_item_name.setText(myOrder.getOrderName());
            holder.order_item_servicetime.setText(myOrder.getServiceTime());
            holder.order_item_time.setText(myOrder.getOrderTime());
            holder.order_item_carinfo.setText(myOrder.getCarType());
            holder.order_item_cartype.setText(myOrder.getCarType());
            holder.order_item_orderNo.setText(myOrder.getOrderNo());
            holder.order_item_location.setText(myOrder.getCarLocation());
            holder.order_item_price.setText(mContext.getString(R.string.sign_yuan) + myOrder.getPrice());
            holder.order_item_state.setText(myOrder.getState());
            switch (myOrder.getCurrentState()) {
                case 0:
                    holder.order_special_btn.setText(mContext.getString(R.string.myorder_pay));
                    holder.order_special_btn.setVisibility(View.VISIBLE);
                    holder.order_common_btn.setVisibility(View.VISIBLE);
                    break;
                case 1:
                case 2:
                case 3:
                    holder.order_common_btn.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    break;
                case 5:
                    holder.order_special_btn.setText(mContext.getString(R.string.myorder_comment));
                    holder.order_special_btn.setVisibility(View.VISIBLE);
                    break;
                case 6:
                case 7:
//                    holder.order_common_btn.setText(mContext.getString(R.string.myorder_delete));
                    break;
            }
            holder.order_special_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (0 == myOrder.getCurrentState()) {
                        msg = handler.obtainMessage();
                        msg.what = PAY;
                        msg.obj = myOrder;
                        handler.sendMessage(msg);
                        return;
                    }
                    if (5 == myOrder.getCurrentState()) {
                        msg = handler.obtainMessage();
                        msg.what = COMMENT;
                        msg.obj = myOrder;
                        handler.sendMessage(msg);
                        return;
                    }
                }
            });
            holder.order_common_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (myOrder.getCurrentState()) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            msg = handler.obtainMessage();
                            msg.what = CANCEL;
                            msg.obj = myOrder;
                            handler.sendMessage(msg);
                            break;
                    /*    case 7:
                            msg = handler.obtainMessage();
                            msg.what = DELETE;
                            msg.obj = myOrder;
                            handler.sendMessage(msg);
                            break;*/
                    }
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderInfoActivity.class);
                    intent.putExtra("orderId", myOrder.getOrderId());
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }


    class ViewHolder {
        private TextView order_item_name;
        private TextView order_item_state;
        private TextView order_item_time;
        private TextView order_item_carinfo;
        private TextView order_item_cartype;
        private TextView order_item_orderNo;
        private TextView order_item_location;
        private TextView order_common_btn;
        private TextView order_special_btn;
        private TextView order_item_price;
        private TextView order_item_servicetime;
    }
}
