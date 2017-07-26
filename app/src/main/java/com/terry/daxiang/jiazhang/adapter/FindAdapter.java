package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.FindBean;
import com.terry.daxiang.jiazhang.custom.Urls;

import java.util.List;

/**
 * Created by happy on 2017/7/15.
 */

public class FindAdapter extends CommomAdapter<FindBean.Data> {
    public FindAdapter(Context context, List<FindBean.Data> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_find, null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_find_item);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_find_name);
            viewHolder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FindBean.Data item = getItem(position);
        Glide.with(context).load(Urls.URL_AVATAR_HOST + item.getImg_url()).into(viewHolder.iv);
        if (!TextUtils.isEmpty(item.getTitle())) {
            viewHolder.tv.setText(item.getTitle());
        }
        int color = -1;
        switch (position) {
            case 0:
                color = Color.parseColor("#FE8E20");
                viewHolder.tv_des.setText("绘本阅读，绘本与绘画，演讲与表演");
                break;
            case 1:
                color = Color.parseColor("#CC6600");
                viewHolder.tv_des.setText("磨耳朵式英语，看动画，看电影");
                break;
            case 2:
                color = Color.parseColor("#F65448");
                viewHolder.tv_des.setText("博览，国学历史故事，古诗赏析");
                break;
            case 3:
                viewHolder.tv_des.setText("健康育儿，亲子游戏，在线课堂");
            default:
                color = Color.parseColor("#FF6600");
                break;
        }
        if (color != -1) {
            viewHolder.tv.setTextColor(color);
            viewHolder.tv_des.setTextColor(color);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv, tv_des;
    }
}
