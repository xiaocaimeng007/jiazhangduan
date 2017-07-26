package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.custom.Urls;

import java.util.ArrayList;

/**
 * Created by fulei on 16/12/17.
 */

public class HotGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ArticleListBean.Data> datas;

    public HotGridAdapter(Context context ,ArrayList<ArticleListBean.Data> datas){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_hot , null);
        ArticleListBean.Data item = datas.get(position);
        TextView txt_title = (TextView) convertView.findViewById(R.id.txt_title);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_album);
        txt_title.setText(item.getTitle());
        Glide.with(context).load(Urls.URL_AVATAR_HOST+item.getImg_url()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageView.setBackgroundDrawable(new BitmapDrawable(resource));
            }
        });
        return convertView;
    }
}