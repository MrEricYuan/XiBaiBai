package com.jph.xibaibai.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.UserInfo;
import com.jph.xibaibai.model.http.APIRequests;
import com.jph.xibaibai.model.http.IAPIRequests;
import com.jph.xibaibai.ui.activity.base.TitleActivity;
import com.jph.xibaibai.utils.FileUtil;
import com.jph.xibaibai.utils.MImageLoader;
import com.jph.xibaibai.utils.PhotoUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.jph.xibaibai.utils.sp.SPUserInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;

/**
 * Created by Eric on 2015/12/12.
 * 个人信息
 */
public class ProfileCenterActivity extends TitleActivity implements View.OnClickListener {

    public static String nameFlag = "nameFlags";

    private int uid;

    private UserInfo userInfo;

    private int flagModify = 0; // 修改个人信息的标志

    private IAPIRequests mAPIRequests;

    public static final int REFRESHNAME = 1010;

    LocalBroadcastManager lbManager = null;

    @ViewInject(R.id.info_user_img)
    ImageView info_user_img;
    @ViewInject(R.id.info_name_tv)
    TextView info_name_tv;
    @ViewInject(R.id.info_sex_tv)
    TextView info_sex_tv;
    @ViewInject(R.id.info_phone_tv)
    TextView info_phone_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        mAPIRequests = new APIRequests(this);
    }

    @Override
    public void initData() {
        super.initData();
        uid = SPUserInfo.getsInstance(ProfileCenterActivity.this).getSPInt(SPUserInfo.KEY_USERID);
        userInfo = JSON.parseObject(SPUserInfo.getsInstance(this).getSP(SPUserInfo.KEY_USERINFO), UserInfo.class);
        lbManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(getString(R.string.info_title));
        MImageLoader.getInstance(ProfileCenterActivity.this).displayImageM(userInfo.getU_img(), info_user_img);
        info_name_tv.setText(userInfo.getUname());
        info_sex_tv.setText(userInfo.getSex() == 1 ? getString(R.string.info_man) : getString(R.string.info_femail));
        info_phone_tv.setText(userInfo.getIphone());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PhotoUtil.TAKEPHOTO:
                Uri uri = Uri.fromFile(
                        FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP));
                PhotoUtil.cropPhoto(ProfileCenterActivity.this, uri, uri);
                break;
            case PhotoUtil.PICKPHOTO:
                PhotoUtil.cropPhoto(ProfileCenterActivity.this, Uri.fromFile(new File(PhotoUtil.getPickPhotoPath(this, data))),
                        Uri.fromFile(FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP)));
                break;
            case PhotoUtil.CROPPHOTO:
                File fileHead = FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP);
                info_user_img.setImageURI(Uri.fromFile(fileHead));
                mAPIRequests.changeUserHead(uid, fileHead);
                SystermUtils.isUpdateInfo = true;
                break;
            case REFRESHNAME:
                String name = data.getStringExtra(nameFlag);
                userInfo.setUname(name);
                info_name_tv.setText(name);
                mAPIRequests.changeUserInfo(uid, userInfo, flagModify + 1);
                SystermUtils.isUpdateInfo = true;
                break;
        }
    }

    @OnClick({R.id.info_user_rl, R.id.info_name_rl, R.id.info_sex_rl, R.id.info_exit_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_user_rl:
                showDialogHead();
                break;
            case R.id.info_name_rl:
                Intent intent = new Intent(ProfileCenterActivity.this, ModifyNameActivity.class);
                intent.putExtra(ModifyNameActivity.UPDATENAMEFLAG, info_name_tv.getText().toString());
                startActivityForResult(intent, REFRESHNAME);
                break;
            case R.id.info_sex_rl:
                showDialogSex();
                break;
            case R.id.info_exit_btn:// 退出登录
                SPUserInfo.getsInstance(this).setSP(SPUserInfo.KEY_USERID, -1);
                Intent intent1 = new Intent(ProfileCenterActivity.this, LoginActivity.class);
                sendLocalBroadCast(intent1);
                startActivity(intent1);
                finish();
                break;
        }
    }

    /**
     * 退出登录后关闭首页
     */
    private void sendLocalBroadCast(Intent intent) {
//        通过LocalBroadcastManager的getInstance()方法得到它的一个实例
        intent.setAction("com.xbb.broadcast.LOCAL_FINISH_LOGIN");
        Log.i("Tag", "lbManager is null" + (lbManager == null));
        lbManager.sendBroadcast(intent);//调用sendBroadcast()方法发送广播
    }

    private void showDialogHead() {
        final String[] strs = new String[]{getString(R.string.info_take), getString(R.string.info_album)};
        Dialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.info_change_head)).setItems(strs,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            PhotoUtil.takePhoto(ProfileCenterActivity.this, Uri.fromFile(
                                    FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP)));
                        } else {
                            PhotoUtil.pickPhoto(ProfileCenterActivity.this, Uri.fromFile(
                                    FileUtil.getFile(PhotoUtil.PATH_PHOTOTEMP)));
                        }
                    }
                }).create();
        dialog.show();
    }

    private void showDialogSex() {
        final String[] strs = new String[]{"女", "男"};
        Dialog dialog = new AlertDialog.Builder(this).setTitle("选择性别").setItems(strs,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userInfo.setSex(which == 1 ? 1 : 2);
                        info_sex_tv.setText(which == 1 ? getString(R.string.info_man) : getString(R.string.info_femail));
                        mAPIRequests.changeUserInfo(uid, userInfo, flagModify + 2);
                        SystermUtils.isUpdateInfo = true;
                    }
                }).create();
        dialog.show();
    }
}
