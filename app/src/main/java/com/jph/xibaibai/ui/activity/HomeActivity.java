package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.HomeDIYAdapter;
import com.jph.xibaibai.adapter.HomepageAdapter;
import com.jph.xibaibai.model.entity.HomeAdBean;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.entity.ResponseJson;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.model.utils.Constants;
import com.jph.xibaibai.mview.MyViewPager;
import com.jph.xibaibai.ui.MyOrderSetActivity;
import com.jph.xibaibai.ui.activity.base.BaseActivity;
import com.jph.xibaibai.utils.MImageLoader;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.parsejson.BeautyProductParse;
import com.jph.xibaibai.utils.parsejson.HomeDataParse;
import com.jph.xibaibai.utils.sp.SharePerferenceUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eric on 2015/12/1.
 * 软件的主页
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    // ViewPage子View的List
    private List<View> viewList = null;
    // 广告的List
    private List<HomeAdBean> adList = null;
    // 广告下面的原点
    private ImageView[] pointViews;
    // 记录当前的page位置
    private int currentItem = 0;
    // 自动切换ViewPage的管理
    private ScheduledExecutorService scheduledExecutorService;
    // 访问网络
    private IAPIRequests apiRequests;
    // 美容项目数据源
    private List<Product> beautyList = null;
    // DIY项目数据源
    private List<Product> diySubyList = null;
    // DIY适配器
    private HomeDIYAdapter homeDIYAdapter = null;

    @ViewInject(R.id.home_drawerlayout)
    private DrawerLayout drawerLayout;
    @ViewInject(R.id.home_top_ads)
    private MyViewPager home_top_ads; // 广告图
    @ViewInject(R.id.home_points)
    private LinearLayout home_points; //广告图的小点点
    @ViewInject(R.id.home_location_tv)
    private TextView home_location_tv;// 定位的位置
    @ViewInject(R.id.home_inspa_name)
    private TextView beauty1_name;// 内饰SPA
    @ViewInject(R.id.home_inspa_price)
    private TextView beauty1_price;// 内饰SPA价格
    @ViewInject(R.id.home_crystal_wax_name)
    private TextView beauty2_name;// 精致油蜡
    @ViewInject(R.id.home_crystal_wax_price)
    private TextView beauty2_price;// 精致油蜡价格
    @ViewInject(R.id.home_engine_wash_name)
    private TextView beauty3_name;// 发送机干洗
    @ViewInject(R.id.home_engine_wash_price)
    private TextView beauty3_price;// 发送机干洗价格
    @ViewInject(R.id.home_coating_name)
    private TextView beauty4_name;// 皮革上光镀膜
    @ViewInject(R.id.home_coating_price)
    private TextView beauty4_price;// 皮革上光镀膜价格
    @ViewInject(R.id.home_plant_name)
    private TextView beauty5_name;// 植物蜡
    @ViewInject(R.id.home_plant_price)
    private TextView beauty5_price;// 植物蜡价格
    @ViewInject(R.id.home_diyproduct_lv)
    private ListView diyproduct_lv; // diy列表

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    home_top_ads.setCurrentItem(currentItem);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        apiRequests = new APIRequests(this);
        apiRequests.getCarouselAd();
        apiRequests.getBeatyDatas();
        apiRequests.getHomeDIYDatas();
    }

    @Override
    public void initView() {
        super.initView();
        drawerLayout.setScrimColor(0x32000000);// 设置半透明度
        if (!StringUtil.isNull(SharePerferenceUtil.getLocationInfo(HomeActivity.this).getCity())) {
                String cityName = SharePerferenceUtil.getLocationInfo(HomeActivity.this).getCity();
                if (cityName.contains("市")) {
                    cityName = cityName.replace("市", "");
                }
                home_location_tv.setText(cityName);
                diyproduct_lv.setFocusable(false);
                Log.i("Tag", "City=>" + cityName);
        }
        diyproduct_lv.setOnItemClickListener(this);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Tasks.ADCODE:
                adList = HomeDataParse.adDataParse(responseJson.getResult().toString());
                if (adList != null) {
                    initViewPagerData();
                }
                break;
            case Tasks.BEAUTYATACODE:
                beautyList = BeautyProductParse.beautyDataParse(responseJson.getResult().toString());
                if (beautyList != null && beautyList.size() > 0) {
                    for(int i = 0;i<beautyList.size();i++){
                        initBeautydatas(beautyList.get(i).getP_sort());
                    }
                }
                break;
            case Tasks.HOMEDIY_LIST:
                diySubyList = HomeDataParse.diyHomeParse(responseJson.getResult().toString());
                if (diySubyList != null && diySubyList.size() > 0) {
                    homeDIYAdapter = new HomeDIYAdapter(diySubyList);
                    diyproduct_lv.setAdapter(homeDIYAdapter);
                    SystermUtils.setListViewHeight(diyproduct_lv);
                }
                break;
        }
    }

    /**
     * 初始化美容数据
     */
    private void initBeautydatas(int i) {
        switch (i){
            case 0:
                beauty1_name.setText(beautyList.get(i).getP_name());
                beauty1_price.setText((int) beautyList.get(i).getP_price() + "元");
                break;
            case 1:
                beauty2_name.setText(beautyList.get(i).getP_name());
                beauty2_price.setText((int) beautyList.get(i).getP_price() + "元");
                break;
            case 2:
                beauty3_name.setText(beautyList.get(i).getP_name());
                beauty3_price.setText((int) beautyList.get(i).getP_price() + "元");
                break;
            case 3:
                beauty4_name.setText(beautyList.get(i).getP_name());
                beauty4_price.setText((int) beautyList.get(i).getP_price() + "元");
                break;
            case 4:
                beauty5_name.setText(beautyList.get(i).getP_name());
                beauty5_price.setText((int) beautyList.get(i).getP_price() + "元");
                break;
        }
    }

    /**
     * 初始化ViewPage数据
     */
    private void initViewPagerData() {
        initViewPager();
        setPagePoints();
        showPictByTurns();
    }

    /**
     * 初始化ViewPage
     */
    private void initViewPager() {
        viewList = new ArrayList<>();
        for (int i = 0; i < adList.size() + 2; i++) {
            HomeAdBean adBean = null;
            ImageView view = new ImageView(this);
            ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(layout);
            if (i == 0) {
                adBean = adList.get(adList.size() - 1);
            } else if (i == adList.size() + 1) {
                adBean = adList.get(0);
            } else {
                adBean = adList.get(i - 1);
            }
            MImageLoader.getInstance(HomeActivity.this).displayImage(Constants.BASE_URL + SystermUtils.replacePicpath(adBean.getAdPath()), view);
            final HomeAdBean adBean1 = adBean;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加跳转事件
                    WebActivity.startWebActivity(HomeActivity.this, "详情", adBean1.getAdLinkPath());
                }
            });
            viewList.add(view);
        }
        HomepageAdapter homepageAdapter = new HomepageAdapter(viewList);
        home_top_ads.setAdapter(homepageAdapter);
        home_top_ads.setCurrentItem(1);
        home_top_ads.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if (position == 0) {
                    currentItem = viewList.size() - 2;
                } else if (position == viewList.size() - 1) {
                    currentItem = 1;
                }
                if (position != currentItem) {
                    home_top_ads.setCurrentItem(currentItem, false);
                }
                for (int i = 0; i < pointViews.length; i++) {
                    pointViews[currentItem - 1]
                            .setImageResource(R.mipmap.home_doted);
                    if ((currentItem - 1) != i) {
                        pointViews[i]
                                .setImageResource(R.mipmap.home_dot);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置ViewPage下方的圆点
     */
    private void setPagePoints() {
        pointViews = new ImageView[adList.size()];
        ImageView point = null;
        if (home_points != null) {
            home_points.removeAllViews();
        }
        for (int i = 0; i < adList.size(); i++) {
            point = new ImageView(this);
            point.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            point.setPadding(0, 0, 15, 0);
            pointViews[i] = point;
            if (i == 0) {
                pointViews[i].setImageResource(R.mipmap.home_doted);
            } else {
                pointViews[i].setImageResource(R.mipmap.home_dot);
            }
            home_points.addView(pointViews[i]);
        }
    }

    /**
     * 自动切换ViewPage图片
     */
    public void showPictByTurns() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5, TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (home_top_ads) {
                if (currentItem == viewList.size() - 1) {
                    currentItem = 0;
                } else {
                    currentItem++;
                }
                handler.sendEmptyMessage(10); // 通过Handler切换图片
            }
        }
    }

    /**
     * 改变左侧边栏打开状态
     */
    public void toggleLeftLayout() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.home_washcar_btn, R.id.home_center_btn, R.id.home_inspa_rl, R.id.home_crystal_wax_rl, R.id.home_engine_wash_rl,
            R.id.home_coating_rl, R.id.home_plant_rl,R.id.menu_order_layout,R.id.menu_ticket_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_washcar_btn:// 一键洗车
                startActivity(new Intent(HomeActivity.this, PlaceOrdersActivity.class));
                break;
            case R.id.home_center_btn:// 个人中心
                toggleLeftLayout();
                break;
            case R.id.home_inspa_rl:// 内饰SPA
                if(beautyList != null && beautyList.size() > 0){
                    HomeWebActivity.startWebActivity(HomeActivity.this,beautyList.get(0),0);
                }
                break;
            case R.id.home_crystal_wax_rl:// 精制水晶蜡
                if(beautyList != null && beautyList.size() > 0){
                    HomeWebActivity.startWebActivity(HomeActivity.this,beautyList.get(1),0);
                }
                break;
            case R.id.home_engine_wash_rl:// 发送机舱干洗
                if(beautyList != null && beautyList.size() > 0){
                    HomeWebActivity.startWebActivity(HomeActivity.this,beautyList.get(2),0);
                }
                break;
            case R.id.home_coating_rl:// 皮革上光镀膜
                if(beautyList != null && beautyList.size() > 0){
                    HomeWebActivity.startWebActivity(HomeActivity.this,beautyList.get(3),0);
                }
                break;
            case R.id.home_plant_rl:// 天然植物蜡
                if(beautyList != null && beautyList.size() > 0){
                    HomeWebActivity.startWebActivity(HomeActivity.this,beautyList.get(4),0);
                }
                break;
            case R.id.menu_ticket_layout: // 优惠券
                startActivity(new Intent(HomeActivity.this,SelectTicketActivity.class));
                break;
            case R.id.menu_order_layout: // 我的订单
                startActivity(new Intent(HomeActivity.this, MyOrderSetActivity.class));
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.home_diyproduct_lv:
                HomeWebActivity.startWebActivity(HomeActivity.this,diySubyList.get(position),1);
                break;
        }
    }
}
