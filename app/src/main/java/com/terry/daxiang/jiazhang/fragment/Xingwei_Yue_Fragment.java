package com.terry.daxiang.jiazhang.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.XWXiangqingActivity;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.AsyncBitmapByVideo;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/9/20.
 */

public class Xingwei_Yue_Fragment extends BaseFragment implements BaseQuickAdapter.OnRecyclerViewItemClickListener{
    @BindView(R.id.rv_xingwei)
    MyRecyclerView rvXingwei;

    private final int RESULT_ALL_DATA = 0x001;
    private ArrayList<ArticleListBean.Data> datas ;
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_xingwei_zhou, null);
    }

    @Override
    public void init() {
        rvXingwei.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadNetData();
    }

    private void loadNetData(){
        RequestService.doGet(Urls.getChildMonthActionReport(SharedPrefUtils.getString(getActivity(),SharedPrefUtils.APP_USER_ID)), RESULT_ALL_DATA , this);
    }

    class Adapter extends BaseQuickAdapter<ArticleListBean.Data> {

        public Adapter(int layoutResId, List<ArticleListBean.Data> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ArticleListBean.Data item) {
            helper.setText(R.id.tv_name, item.getTitle())
                    .setText(R.id.tv_text, item.getZhaiyao());
            GridView gridView = helper.getView(R.id.grid_albums);
            ImageGridAdapter gridAdapter = new ImageGridAdapter(getActivity(), item);
            gridView.setAdapter(gridAdapter);
        }

        private class ImageGridAdapter extends BaseAdapter {
            private Context context;
            private LayoutInflater inflater;
            private boolean isMedia = false;
            private List<ArticleListBean.Data.Albums> albums;
            private List<ArticleListBean.Data.Attach> attachs;
            private AsyncBitmapByVideo asyncBitmapLoader;

            public ImageGridAdapter(Context context , ArticleListBean.Data data){
                this.context = context;
                inflater = LayoutInflater.from(context);
                asyncBitmapLoader = new AsyncBitmapByVideo(context);

                try {
                    if (data.getAlbums().size() >0){
                        isMedia = false;
                        albums = data.getAlbums();
                    }

                    if (data.getAttach().size() > 0){
                        isMedia = true;
                        attachs = data.getAttach();
                    }
                    notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            public boolean getType(){
                return isMedia;
            }

            @Override
            public int getCount() {
                if (isMedia){
                    return attachs == null ? 0 : attachs.size();
                }else {
                    return albums == null ? 0 : albums.size();
                }

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
                convertView = inflater.inflate(R.layout.item_imagegrid , null);
                final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_album);
                if (isMedia){
                    imageView.setImageResource(R.mipmap.icon_shipin_click);

                    Bitmap bitmap = asyncBitmapLoader.LoadBitmapByVideo(Urls.URL_AVATAR_HOST + attachs.get(position).getFile_path(), imageView, new AsyncBitmapByVideo.ImageCallback() {
                        @Override
                        public void imageLoaded(Bitmap b, ImageView imageview, String imageUrl) {
                            imageview.setBackgroundDrawable(new BitmapDrawable(b));
                            imageview.setVisibility(View.VISIBLE);
                        }
                    });
                    if (bitmap != null){
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }
                }else {
                    Glide.with(context).load(Urls.URL_AVATAR_HOST+albums.get(position).getThumb_path()).asBitmap().placeholder(R.color.color_cccccc).error(R.mipmap.ic_launcher).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setBackgroundDrawable(new BitmapDrawable(resource));
                        }
                    });
                }
                return convertView;
            }
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes) {
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr), ArticleListBean.class, new ArticleListBean());
                        if (listBean == null) {
                            listBean = JsonParser.get(new JSONObject(responseStr), ArticleListBean.class, new ArticleListBean());
                        }
                        datas =listBean.getData();
                        Adapter adapter = new Adapter(R.layout.item_xingwei_zhou,datas);
                        adapter.setOnRecyclerViewItemClickListener(this);
                        rvXingwei.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        ToastUtil.show(getString(R.string.string_load_error));
    }

    @Override
    public void onItemClick(View view, int position) {

        try {
            ArticleListBean.Data data =datas.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("title", "行为记录");
            bundle.putString("user_name" ,  data.getUser_name());
            bundle.putString("content_cid" ,data.getId()+"");
            startActivity(XWXiangqingActivity.class, bundle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}