package com.jph.xibaibai.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.BigImageAdpater;
import com.jph.xibaibai.adapter.MyPhotoViewPager;
import com.jph.xibaibai.ui.activity.base.BaseActivity;

import java.util.ArrayList;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/27 17:17
 * 描述：大图浏览界面
 */

public class BigImageActivity extends BaseActivity {
    private MyPhotoViewPager big_img_pager;
    private LinearLayout big_img_group;
    private ArrayList<View> viewlist;
    private ImageView[] points;
    private ArrayList<String> img_urls;
    private BigImageAdpater adapter;
    private static final String ISLOCKED_ARG = "isLocked";
    private ImageView point;
    private int selectIndex;
    private TextView current_index;
    private TextView total_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigimg);
        initWidget(savedInstanceState);

    }


    private void initWidget(Bundle savedInstanceState) {
        big_img_pager = (MyPhotoViewPager) findViewById(R.id.big_img_pager);
        big_img_group = (LinearLayout) findViewById(R.id.big_img_group);
        current_index= (TextView) findViewById(R.id.current_index);
        total_index= (TextView) findViewById(R.id.total_index);

        img_urls = getIntent().getStringArrayListExtra("img_pics");
        selectIndex = getIntent().getIntExtra("pos", 0);
        if (img_urls != null && !img_urls.isEmpty()) {
            current_index.setText((selectIndex+1)+"");
            total_index.setText("/"+img_urls.size());
//            setPoint();
            adapter = new BigImageAdpater(this, img_urls);
            big_img_pager.setAdapter(adapter);
            big_img_pager.addOnPageChangeListener(pageListener);
            big_img_pager.setCurrentItem(selectIndex);
            if (savedInstanceState != null) {
                boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
                        false);
                ((MyPhotoViewPager) big_img_pager).setLocked(isLocked);
            }
        } else {
            showToast("图片下载不正常");
            finish();
        }
    }

    private ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            current_index.setText((arg0+1)+"");
            selectIndex=arg0;
//            for (int i = 0; i < points.length; i++) {
//                points[arg0].setImageResource(R.mipmap.home_doted);
//                if (arg0 != i) {
//                    points[i].setImageResource(R.mipmap.home_dot);
//                }
//            }
        }
    };

//    private void setPoint() {
//        // TODO Auto-generated method stub
//        big_img_group.removeAllViews();
//        points = new ImageView[img_urls.size()];
//        for (int i = 0; i < img_urls.size(); i++) {
//            point = new ImageView(this);
//            point.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
//            point.setPadding(10, 0, 10, 0);
//            points[i] = point;
//            if (i == selectIndex) {
//                // textViews[i].setBackgroundResource(R.drawable.radio_sel);
//                points[i].setImageResource(R.mipmap.home_doted);
//            } else {
//                // textViews[i].setBackgroundResource(R.drawable.radio);
//                points[i].setImageResource(R.mipmap.home_dot);
//            }
//            big_img_group.addView(point);
//        }
//        if (points.length <= 1) {
//            big_img_group.setVisibility(View.GONE);
//
//        }
//    }

}
