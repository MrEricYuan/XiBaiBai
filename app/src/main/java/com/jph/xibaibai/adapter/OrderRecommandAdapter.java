package com.jph.xibaibai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.model.entity.ArtificerRecommand;
import com.jph.xibaibai.model.entity.Product;
import com.jph.xibaibai.mview.CustomGridView;
import com.jph.xibaibai.ui.activity.BigImageActivity;
import com.jph.xibaibai.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XiBaiBai
 * 作者：Hi-Templar
 * 创建时间：2015/12/10 17:45
 * 描述：$TODO
 */
public class OrderRecommandAdapter extends BaseAdapter {
    private List<ArtificerRecommand> recommandList;
    private Context mContext;

    public OrderRecommandAdapter(List<ArtificerRecommand> recommandList, Context mContext) {
        this.recommandList = recommandList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return recommandList.size();
    }

    @Override
    public Object getItem(int position) {
        return recommandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_recommand_item_layout, null);
            holder.recommand_album = (CustomGridView) convertView.findViewById(R.id.recommand_album);
            holder.recommand_diy_name = (TextView) convertView.findViewById(R.id.recommand_diy_name);
            holder.recommand_remark = (TextView) convertView.findViewById(R.id.recommand_remark);
            holder.recommand_diyLayot= (RelativeLayout) convertView.findViewById(R.id.recommand_diyLayot);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ArtificerRecommand recommand = recommandList.get(position);
        if (recommand != null) {
            Product product = recommand.getDiyData();
            if (product != null) {
                holder.recommand_diy_name.setText(product.getP_name());
                holder.recommand_diyLayot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            if (!StringUtil.isNull(recommand.getRemark()))
                holder.recommand_remark.setText(mContext.getString(R.string.order_info_recommandbz)+recommand.getRemark());
            if (recommand.getExplainAlbum()!=null&&!recommand.getExplainAlbum().isEmpty()){
                CameraPhotoAdapter adapter=new CameraPhotoAdapter(mContext,recommand.getExplainAlbum(),3,3);
                holder.recommand_album.setAdapter(adapter);
                holder.recommand_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> picList = new ArrayList<String>();
                        picList.addAll(recommand.getExplainAlbum());
                        Intent intent = new Intent(mContext,
                                BigImageActivity.class);

                        intent.putStringArrayListExtra("img_pics", picList);
                        intent.putExtra("pos", position);
                        mContext.startActivity(intent);
                    }
                });
            }

        }
        return convertView;
    }

    class ViewHolder {
        private TextView recommand_diy_name;
        private TextView recommand_remark;
        private CustomGridView recommand_album;
        private RelativeLayout recommand_diyLayot;
    }
}
