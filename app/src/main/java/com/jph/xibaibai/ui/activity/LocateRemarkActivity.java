package com.jph.xibaibai.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jph.xibaibai.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Eric on 2015/12/10.
 */
public class LocateRemarkActivity extends Activity implements View.OnClickListener{

    private ImageView locate_cancel;
    private Button remark_submit;
    private EditText remark_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate_remark);

        initViews();
    }

    private void initViews(){
        locate_cancel = (ImageView) findViewById(R.id.locate_cancel);
        remark_submit = (Button) findViewById(R.id.remark_submit);
        remark_edit = (EditText) findViewById(R.id.remark_edit);
        locate_cancel.setOnClickListener(this);
        remark_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.locate_cancel:
                finish();
                break;
            case R.id.remark_submit:
                Intent intent = new Intent();
                intent.putExtra("getRemarks",remark_edit.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
