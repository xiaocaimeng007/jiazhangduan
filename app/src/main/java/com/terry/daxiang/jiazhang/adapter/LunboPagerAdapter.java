package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.custom.Urls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class LunboPagerAdapter extends PagerAdapter {

    ArrayList<ArticleListBean.Data> datas;
    Context context;

    public LunboPagerAdapter(Context context, ArrayList<ArticleListBean.Data> datas) {
        this.context=context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        ArticleListBean.Data itemdata = datas.get(position % datas.size());

        final ImageView imageView = new ImageView(context);
        Glide.with(context).load(Urls.URL_AVATAR_HOST+itemdata.getImg_url()).asBitmap().error(R.mipmap.icon_lunbo2).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageView.setBackgroundDrawable(new BitmapDrawable(resource));
            }
        });
        ViewParent parent = imageView.getParent();
        // remove掉View之前已经加到一个父控件中，否则报异常
        if (parent != null) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
