package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.adapter.KaoqinRiliAdapter.Holder;
import com.terry.daxiang.jiazhang.bean.Date;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/21.
 */

public class KaoqinRiliAdapter extends Adapter<Holder> {

    Context context;
    List<Date> data;
    LayoutInflater inflater;
    int width;
    int paddingLeft;
    int itemWidth;
    int[] positions = {0, 7, 14, 21, 6, 13, 20, 27};

    public KaoqinRiliAdapter(Context context, List<Date> data, int paddingLeft, int width) {
        this.context = context;
        this.data = data;
        this.paddingLeft = paddingLeft;
        this.width = width;
        inflater = LayoutInflater.from(context);
        itemWidth = (width - paddingLeft * 8) / 7;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) inflater.inflate(R.layout.item_rili, parent, false);
        view.setHeight(itemWidth);
        view.setWidth(itemWidth);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Date date = data.get(position);
        holder.tvText.setText(date.getText());
        if (date.getText().equals("今天")) {
            holder.tvText.setBackgroundResource(R.drawable.shape_juxing_lvbian3dp);
            holder.tvText.setTextColor(Color.parseColor("#9DBF9A"));
        }
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] == position) {
                Log.e("i", i + "");
                holder.tvText.setBackgroundResource(R.drawable.shape_juxing_bg_hui_huibian_3dp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends ViewHolder {
        @BindView(R.id.tv_text)
        TextView tvText;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
