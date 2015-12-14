package com.jph.xibaibai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.ServiceCity;

import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/14 14:01
 * 描述：服务城市列表数据适配
 */
public class ServiceCityAdapter extends BaseAdapter{
    private List<ServiceCity> serviceCityList;
    private Context mContext;

    public ServiceCityAdapter(List<ServiceCity> serviceCityList, Context mContext) {
        this.serviceCityList = serviceCityList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return serviceCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceCityList.get(position);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_service_city_layout,null);
            holder.service_city_name= (TextView) convertView.findViewById(R.id.service_city_name);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        ServiceCity serviceCity=serviceCityList.get(position);
        if (serviceCity!=null)
            holder.service_city_name.setText(serviceCity.getCityName());
        return convertView;
    }

    class ViewHolder{
        private TextView service_city_name;
    }
}
