package com.jph.xibaibai.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.utils.Constants;
import com.jph.xibaibai.utils.MImageLoader;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;

import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/9 11:06
 * 描述：本地拍照图片
 */

public class CameraPhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> imgList;
    private WindowManager wm;
    private int select;
    private DisplayMetrics dm;
    private int num;

    private boolean editable;

    /**
     * @param mContext
     * @param imgList
     * @param select
     * @param num      一行的张数
     */
    public CameraPhotoAdapter(Context mContext, List<String> imgList, int select, int num) {
        this.mContext = mContext;
        this.imgList = imgList;
        this.select = select;
        this.num = num;
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        this.editable = false;
    }

    public void editAlbum(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    public List<String> getDataSet() {
        return this.imgList;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_recommand_item_layout, null);
            holder.attachement_item = (ImageView) convertView.findViewById(R.id.camera_img_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int width = (dm.widthPixels - 70) / num;
        int height = 0;
        switch (select) {
            case 1:
                height = 3 * width / 4;
                break;
            case 2:
                height = width;
                break;
            case 3:
                height = 9 * width / 16;
                break;
        }
        if (height == 0) {
            height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        holder.attachement_item.setLayoutParams(params);
        String path = imgList.get(position);
        if (!StringUtil.isNull(path)) {
            MImageLoader.getInstance(mContext).displayImage(Constants.BASE_URL + SystermUtils.replacePicpath(path), holder.attachement_item);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView attachement_item;

    }
}
