package com.jph.xibaibai.ui.activity;

import android.os.Bundle;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.ServiceCityAdapter;
import com.jph.xibaibai.model.entity.ServiceCity;
import com.jph.xibaibai.mview.CustomListView;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/14 13:47
 * 描述：$TODO
 */
public class ServiceCityActivity extends TitleActivity {
    @ViewInject(R.id.city_list_lv)
    private CustomListView city_list_lv;

    //    private IAPIRequests mApiRequest;
    private List<ServiceCity> serviceCityList;
    private ServiceCityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_city);
    }

    @Override
    public void initData() {
        super.initData();
//        mApiRequest=new APIRequests(this);
        serviceCityList = new ArrayList<ServiceCity>();
        ServiceCity serviceCity = new ServiceCity();
        serviceCity.setCityName("成都");
        serviceCityList.add(serviceCity);
        serviceCity = new ServiceCity();
        serviceCity.setCityName("重庆");
        serviceCityList.add(serviceCity);

    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.service_city_title));
        adapter = new ServiceCityAdapter(serviceCityList, this);
        city_list_lv.setAdapter(adapter);
    }
}
