package com.jph.xibaibai.ui.activity.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.blueware.agent.android.BlueWare;
import com.jph.xibaibai.model.http.XRequestCallBack;
import com.jph.xibaibai.mview.CustomProgressDialog;
import com.lidroid.xutils.ViewUtils;

/**
 * Activity基类
 *
 * @author jph
 */
public class BaseActivity extends AppCompatActivity implements Init, XRequestCallBack {
    protected final String TAG = getClass().getSimpleName();

    private boolean destroyed;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destroyed = false;
        AppManager.getInstance().addActivity(this);
        BlueWare.withApplicationToken("BA9C5DB973D3FDF10D10935FC1E939FD49").start(this.getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void init() {
        initData();
        initView();
        initListener();
    }

    protected void setSuperContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    protected void setSuperContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewUtils.inject(this);
        init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ViewUtils.inject(this);
        init();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        ViewUtils.inject(this);
        init();
    }

    public void initData() {
    }

    public void initView() {
    }

    public void initListener() {
    }


    @Override
    public void onPrepare(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onSuccess(int taskId, Object... params) {

    }

    @Override
    public void onSuccess(int taskId, String flag, Object... params) {

    }

    @Override
    public void onFailed(int taskId, int errorCode, String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void onLoading(int taskId, long count, long current) {

    }

    @Override
    public void onEnd(int taskId) {
        dissmissProgressDialog();
    }

    @Override
    public boolean isCallBack() {
        return !destroyed;
    }


    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
        }
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        });
    }

    public void dissmissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
