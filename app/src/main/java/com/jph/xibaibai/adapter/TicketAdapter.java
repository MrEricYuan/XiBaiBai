package com.jph.xibaibai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Coupon;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/7 11:46
 * 描述：$TODO
 */
public class TicketAdapter extends BaseAdapter {

    private List<Coupon> ticketList;
    private Context mContext;

    public TicketAdapter(List<Coupon> ticketList, Context mContext) {
        this.ticketList = ticketList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ticket_item_layout, null);
            holder.ticket_decline_tv = (TextView) convertView.findViewById(R.id.ticket_decline_tv);
            holder.ticket_item_price = (TextView) convertView.findViewById(R.id.ticket_item_price);
            holder.ticket_item_name = (TextView) convertView.findViewById(R.id.ticket_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Coupon coupon = ticketList.get(position);
        if (coupon != null) {
            holder.ticket_item_name.setText(coupon.getCoupons_name());
            holder.ticket_decline_tv.setText(coupon.getExpired_time());
            holder.ticket_item_price.setText(coupon.getCoupons_price());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView ticket_item_name;
        private TextView ticket_item_price;
        private TextView ticket_decline_tv;
    }

}
