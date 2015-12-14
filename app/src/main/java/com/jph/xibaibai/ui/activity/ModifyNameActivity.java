package com.jph.xibaibai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jph.xibaibai.R;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.StringUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Eric on 2015/12/14.
 */
public class ModifyNameActivity extends TitleActivity implements View.OnClickListener{

    @ViewInject(R.id.modify_name_et)
    EditText modify_name_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.info_change_name));
    }

    @OnClick({R.id.modify_name_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify_name_btn:
                if(StringUtil.isNull(modify_name_et.getText().toString())){
                    showToast(getString(R.string.info_change_name_tip));
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(ProfileCenterActivity.nameFlag,modify_name_et.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
