package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.custom.AsyncBitmapByVideo;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.DateUtils;
import com.terry.daxiang.jiazhang.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fulei on 16/12/17.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<ArticleListBean.Data> datas = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ArticleListAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<ArticleListBean.Data> data) {
        this.datas.clear();
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    public void setDataUp(List<ArticleListBean.Data> data){
        this.datas.addAll(0 , data);
        notifyDataSetChanged();
    }
    public void setDataDown(List<ArticleListBean.Data> data){
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    public List<ArticleListBean.Data> getAllData(){
        return datas;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_articlelist, parent, false);
        view.setOnClickListener(this);
        ViewHodler1 viewHodler1 = new ViewHodler1(view);
        return viewHodler1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ArticleListBean.Data data = this.datas.get(position);
        ((ViewHodler1) holder).itemView.setTag(data);
        Glide.with(context).load(Urls.URL_AVATAR_HOST+data.getImg_url()).error(R.mipmap.icon_defualt_head).into(((ViewHodler1) holder).ivIcon);
        ((ViewHodler1) holder).tvName.setText(data.getFields().getAuthor());
        String times = data.getAdd_time().substring(6 , data.getAdd_time().length() - 2);
        ((ViewHodler1) holder).tvTime.setText(DateUtils.getTime(times));
        ((ViewHodler1) holder).tvText.setText(data.getZhaiyao());
        if (TextUtils.isEmpty(data.getZhaiyao())) {
            ((ViewHodler1) holder).tvText.setVisibility(View.GONE);
        }else {
            ((ViewHodler1) holder).tvText.setVisibility(View.VISIBLE);
        }
        ((ViewHodler1) holder).tvFenlei.setText("");
        final ImageGridAdapter gridAdapter = new ImageGridAdapter(context,data);
        ((ViewHodler1) holder).grid_albums.setAdapter(gridAdapter);
        ((ViewHodler1) holder).grid_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(((ViewHodler1) holder).itemView, data);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (ArticleListBean.Data) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ArticleListBean.Data data);
    }

    class ViewHodler1 extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        CircleImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_fenlei)
        TextView tvFenlei;
        @BindView(R.id.tv_text)
        TextView tvText;
        @BindView(R.id.grid_albums)
        GridView grid_albums;

        public ViewHodler1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class ImageGridAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private boolean isMedia = false;
        private List<ArticleListBean.Data.Albums> albums;
        private List<ArticleListBean.Data.Attach> attachs;
        private AsyncBitmapByVideo asyncBitmapLoader;

        public ImageGridAdapter(Context context ,ArticleListBean.Data data){
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