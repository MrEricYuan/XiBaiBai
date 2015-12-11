package com.jph.xibaibai.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.utils.Constants;
import com.jph.xibaibai.utils.MImageLoader;
import com.jph.xibaibai.utils.StringUtil;
import com.jph.xibaibai.utils.SystermUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/27 17:24
 * 描述：
 */

public class BigImageAdpater extends PagerAdapter {

    private Activity mContext;

    private ArrayList<String> pathList = null;

    private ImageLoader imgload = null;


    public BigImageAdpater(Activity context, ArrayList<String> pathList) {
        this.mContext = context;
        imgload = ImageLoader.getInstance();
        this.pathList = pathList;
    }

    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pathList == null ? 0 : pathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View image = LayoutInflater.from(mContext).inflate(
                R.layout.item_pager_image, view, false);
        assert image != null;
        PhotoView img = (PhotoView) image.findViewById(R.id.image);
        img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                // TODO Auto-generated method stub
                mContext.finish();
            }
        });

        final ProgressBar spinner = (ProgressBar) image
                .findViewById(R.id.loading);
        String path = pathList.get(position);
        spinner.setVisibility(View.VISIBLE);
        if (!StringUtil.isNull(path)) {
            MImageLoader.getInstance(mContext).displayImage(Constants.BASE_URL + SystermUtils.replacePicpath(path), img,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            spinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            spinner.setVisibility(View.GONE);
                        }
                    });

        }

        view.addView(image, 0);
        return image;
    }
}
