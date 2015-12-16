package com.jph.xibaibai.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.xibaibai.R;
import com.jph.xibaibai.adapter.base.BaseRecyclerAdapter;
import com.jph.xibaibai.model.entity.AllCar;
import com.jph.xibaibai.model.entity.Car;
import com.jph.xibaibai.utils.SystermUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 车辆列表
 * Created by jph on 2015/8/27.
 */
public class RecyclerCarAdapter extends
        BaseRecyclerAdapter<RecyclerView.ViewHolder, Car> {

    private boolean defaultShowable = true;
    private AllCar allCar;
    private CarOnClickListener carOnClickListener;

    public RecyclerCarAdapter(AllCar allCar) {
        super(allCar.getList());
        this.allCar = allCar;
    }

    public AllCar getAllCar() {
        return allCar;
    }

    public void setAllCar(AllCar allCar) {
        this.allCar = allCar;
    }

    public CarOnClickListener getCarOnClickListener() {
        return carOnClickListener;
    }

    public void setCarOnClickListener(CarOnClickListener carOnClickListener) {
        this.carOnClickListener = carOnClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.itemcar_txt_title)
        TextView txtTitle;
        @ViewInject(R.id.itemcar_txt_platenum)
        TextView txtPlateNum;
        @ViewInject(R.id.itemcar_txt_default)
        TextView txtDefault;
        @ViewInject(R.id.itemcar_check_img)
        ImageView icheck_img;
        @ViewInject(R.id.itemcar_edit_tv)
        TextView itemcar_edit_tv;
        @ViewInject(R.id.itemcar_delete_tv)
        TextView itemcar_delete_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this, itemView);
        }

    }

    @Override
    public RecyclerView.ViewHolder onRealCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_car,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onRealBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        super.onRealBindViewHolder(viewHolder, position);
        final Car car = getItem(position);
        ViewHolder viewHolderCar = (ViewHolder) viewHolder;
        viewHolderCar.txtTitle.setText(car.getC_plate_num());
        viewHolderCar.txtPlateNum.setText(car.getC_brand()+"\t"+car.getC_color()+"\t"+ SystermUtils.getCarTypes(car.getC_type()));
        if (defaultShowable) {
            ((ViewHolder) viewHolder).txtDefault.setVisibility(View.VISIBLE);
            if (allCar.getDefaultId() == car.getId()) {
                viewHolderCar.icheck_img.setImageResource(R.mipmap.mycar_checked);
                viewHolderCar.txtDefault.setTextColor(viewHolderCar.itemView.getResources().getColor(R.color.them_color));
                viewHolderCar.txtDefault.setText("默认车辆");
            } else {
                viewHolderCar.icheck_img.setImageResource(R.mipmap.mycar_unchecked);
                viewHolderCar.txtDefault.setTextColor(viewHolderCar.itemView.getResources().getColor(R.color.black_two));
                viewHolderCar.txtDefault.setText("设为默认");
            }
        } else {
            ((ViewHolder) viewHolder).txtDefault.setVisibility(View.GONE);
        }
        viewHolderCar.txtDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allCar.getDefaultId() == car.getId() || carOnClickListener == null) {
                    return;
                }
                carOnClickListener.onClickSetDefault(position);
            }
        });
        viewHolderCar.itemcar_edit_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                carOnClickListener.onClickEditCar(position);
            }
        });
        viewHolderCar.itemcar_delete_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                carOnClickListener.onClickDeleteCar(position);
            }
        });
    }

    public interface CarOnClickListener {
        void onClickSetDefault(int position);
        void onClickEditCar(int position);
        void onClickDeleteCar(int position);
    }

    public boolean isDefaultShowable() {
        return defaultShowable;
    }

    public void setDefaultShowable(boolean defaultShowable) {
        this.defaultShowable = defaultShowable;
    }
}

