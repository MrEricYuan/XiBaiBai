package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.ChoiceBeautyAdapter;
import com.jph.xibaibai.adapter.ChoiceDIYAdapter;
import com.jph.xibaibai.model.entity.DIYSubBean;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.model.http.Tasks;
import com.jph.xibaibai.ui.activity.base.BaseActivity;
import com.jph.xibaibai.utils.Constants;
import com.jph.xibaibai.utils.SystermUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by Eric on 2015/12/1.
 * 新下单页面
 */
public class PlaceOrdersActivity extends BaseActivity implements View.OnClickListener {
    // 访问网络
    private IAPIRequests apiRequests;
    // 车类型
    private int carType = 0;
    // 清洗和洗车方式选中记录
    private boolean[] checkState = new boolean[4];
    // DIY项目请求码
    private final int diyIntentCode = 1001;
    // 美容项目请求码
    private final int beautyIntentCode = 1002;
    // diy选中的列表
    private List<Product> diyProductList = null;
    // 选中diy的适配器
    private ChoiceDIYAdapter choiceDIYAdapter = null;
    // 美容项目的选中的列表
    private List<Product> choiceBeautyList = null;
    // 美容项目的选中的适配器
    private ChoiceBeautyAdapter choiceBeautyAdapter = null;
    // DIY总价格
    private double diyTotalPrice = 0.0;
    // 美容总价格
    private double beautyTotalPrice = 0.0;
    // 外部清洗
    private double extralWashPrice = 0.0;
    // 内饰+清洗
    private double allCarWashPrice = 0.0;

    @ViewInject(R.id.title_txt)
    private TextView title_txt;
    @ViewInject(R.id.place_extral_check)
    private ImageView extral_check; // 外部清洗
    @ViewInject(R.id.place_allwash_check)
    private ImageView allwash_check; // 外部+内饰
    @ViewInject(R.id.place_choicediy_lv)
    private ListView place_choicediy_lv; // 已经选中的DIY项目
    @ViewInject(R.id.place_choicebeauty_lv)
    private ListView place_choicebeauty_lv; // 已选中的美容项目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_orders);
    }

    @Override
    public void initData() {
        super.initData();
        SystermUtils.diySubBean = new DIYSubBean();
        SystermUtils.beautyChoiceState = null;
        apiRequests = new APIRequests(this);
        for (int i = 0; i < 4; i++) {
            checkState[i] = false;
        }
    }

    @Override
    public void initView() {
        super.initView();
        title_txt.setText(getString(R.string.place_order));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case diyIntentCode:
                diyProductList = (List<Product>) data.getSerializableExtra("DIYProductsList");
                initDIYAdapter();
                break;
            case beautyIntentCode:
                choiceBeautyList = (List<Product>) data.getSerializableExtra("beautyProductList");
                initBeautyAdapter();
                break;
        }
    }

    /**
     * 初始化DIY选中项目的适配器
     */
    private void initDIYAdapter() {
        if (diyProductList != null && diyProductList.size() > 0) {
            place_choicediy_lv.setVisibility(View.VISIBLE);
            choiceDIYAdapter = new ChoiceDIYAdapter(diyProductList);
            place_choicediy_lv.setAdapter(choiceDIYAdapter);
        } else {
            place_choicediy_lv.setAdapter(null);
            place_choicediy_lv.setVisibility(View.GONE);
        }
        SystermUtils.setListViewHeight(place_choicediy_lv);
        Log.i("Tag", "diyProductList=" + diyProductList.size());
    }

    /**
     * 初始化美容选中项目的适配器
     */
    private void initBeautyAdapter() {
        if (choiceBeautyList != null && choiceBeautyList.size() > 0) {
            place_choicebeauty_lv.setVisibility(View.VISIBLE);
            choiceBeautyAdapter = new ChoiceBeautyAdapter(choiceBeautyList, carType);
            place_choicebeauty_lv.setAdapter(choiceBeautyAdapter);
        } else {
            place_choicebeauty_lv.setVisibility(View.GONE);
        }
        SystermUtils.setListViewHeight(place_choicebeauty_lv);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId){
            case Tasks.GEWASHCAR_DATA:

                break;
        }
    }

    @OnClick({R.id.title_img_left, R.id.place_extral_rl, R.id.place_allwash_rl, R.id.place_diyitem_rl, R.id.place_beauty_rl})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_img_left:
                finish();
                break;
            case R.id.place_extral_rl: // 外部清洗
                if (checkState[0]) {
                    checkState[0] = false;
                    extral_check.setImageResource(R.mipmap.place_unchecked);
                } else {
                    checkState[0] = true;
                    extral_check.setImageResource(R.mipmap.place_checked);
                    if (checkState[1]) {
                        checkState[1] = false;
                        allwash_check.setImageResource(R.mipmap.place_unchecked);
                    }
                }
                break;
            case R.id.place_allwash_rl: // 外部+内饰
                if (checkState[1]) {
                    checkState[1] = false;
                    allwash_check.setImageResource(R.mipmap.place_unchecked);
                } else {
                    checkState[1] = true;
                    allwash_check.setImageResource(R.mipmap.place_checked);
                    if (checkState[0]) {
                        checkState[0] = false;
                        extral_check.setImageResource(R.mipmap.place_unchecked);
                    }
                }
                break;
            case R.id.place_diyitem_rl:// DIY项目选择
                intent.setClass(PlaceOrdersActivity.this, DIYSubActivity.class);
                startActivityForResult(intent, diyIntentCode);
                break;
            case R.id.place_beauty_rl:// 美容项目选择
                intent.setClass(PlaceOrdersActivity.this, BeautyServiceActivity.class);
                startActivityForResult(intent, beautyIntentCode);
                break;
        }
    }
}
