package com.jph.xibaibai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.StringUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2015/12/7.
 * 从首页跳入H5详情页
 */
public class HomeWebActivity extends TitleActivity implements View.OnClickListener {
    // 传入Object对象
    public static final String INTENTKEY_OBJECT_DATA = "intentkey_object_product";
    // 传入从哪儿进入
    public static final String INTENTKEY_STRING_FLAG = "intentkey_string_flag";
    // 传入的Product对象
    private Product product = null;
    // 标题
    private String webTitle;
    // 网页地址
    private String webUrl;
    // 0-美容或者1-DIY
    private int formWhere = 0;
    //当前选中的产品
    private List<Product> productList = null;

    @ViewInject(R.id.web_webview)
    private WebView webView;
    @ViewInject(R.id.home_web_choice)
    private TextView home_web_choice;
    @ViewInject(R.id.home_web_place)
    private TextView home_web_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView.loadUrl(webUrl);
    }

    public static void startWebActivity(Context context, Product product, int flag) {
        Intent intent = new Intent(context, HomeWebActivity.class);
        intent.putExtra(INTENTKEY_OBJECT_DATA, product);
        intent.putExtra(INTENTKEY_STRING_FLAG, flag);
        context.startActivity(intent);
    }

    @Override
    public void initData() {
        super.initData();
        productList = new ArrayList<>();
        product = (Product) getIntent().getSerializableExtra(INTENTKEY_OBJECT_DATA);
        if (product != null) {
            webTitle = product.getP_name();
            webUrl = product.getLinkPath();
        }
        productList.add(product);
        formWhere = getIntent().getIntExtra(INTENTKEY_STRING_FLAG, 0);
    }

    @Override
    public void initView() {
        super.initView();
        if (StringUtil.isNull(webTitle))
            hideTitleView();
        else
            setTitle(webTitle);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 调用拨号程序
                if (url.startsWith("mailto:") || url.startsWith("geo:")
                        || url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url); // 在当前的webview中跳转到新的url
                return true;
            }
        });
    }

    @OnClick({R.id.home_web_choice, R.id.home_web_place})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.home_web_choice:
                if (formWhere == 0) { // 美容
                    intent.setClass(HomeWebActivity.this, BeautyServiceActivity.class);
                } else {// DIY
                    intent.setClass(HomeWebActivity.this, DIYSubActivity.class);
                }
                intent.putExtra(PlaceOrdersActivity.HOMEWEB_PRODUCT_LIST, (Serializable) productList);
                intent.putExtra(BeautyServiceActivity.HOMEWEB_FLAG,100);
                startActivity(intent);
                break;
            case R.id.home_web_place:
                intent.setClass(HomeWebActivity.this, PlaceOrdersActivity.class);
                intent.putExtra(PlaceOrdersActivity.HOMEWEB_PRODUCT_LIST, (Serializable) productList);
                intent.putExtra(PlaceOrdersActivity.HOMEWEB_PRODUCT_FLAG, formWhere);
                Log.i("Tag", "HomeWeb=>productList=" + productList.size());
                startActivity(intent);
                break;
        }
    }
}
