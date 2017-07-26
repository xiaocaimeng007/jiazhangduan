package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
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
import com.terry.daxiang.jiazhang.bean.ArticleContentBean;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.custom.AsyncBitmapByVideo;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.DateUtils;
import com.terry.daxiang.jiazhang.view.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/19.
 */
public class DaxiangFMAdapter extends Adapter<DaxiangFMAdapter.ViewHolder1> implements View.OnClickListener {

    LayoutInflater inflater;
    Context context;
    ArrayList<ArticleListBean.Data> datas;
    OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public DaxiangFMAdapter(Context context , ArrayList<ArticleListBean.Data> datas){
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_fm_type1, parent, false);
        view.setOnClickListener(this);
        ViewHolder1 viewHolder1 = new ViewHolder1(view);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder1 holder, int position) {
        final ArticleListBean.Data itemData = datas.get(position);
        holder.itemView.setTag(itemData);
        try {
            Glide.with(context).load(Urls.URL_AVATAR_HOST+itemData.getImg_url()).error(R.mipmap.icon_defualt_head).into(holder.iv_icon);
            holder.tv_name.setText(itemData.getFields().getAuthor());
            String times = itemData.getAdd_time().substring(6 , itemData.getAdd_time().length() - 2);
            holder.tv_time.setText(DateUtils.getTime(times));

            holder.tv_count.setText(itemData.getClick()+"");
            holder.tv_zan.setText(itemData.getArticle_zan().size()+"");
            if (itemData.getIs_dianzan() == 0){
                holder.iv_zan.setBackgroundResource(R.mipmap.icon_zan);
            }else {
                holder.iv_zan.setBackgroundResource(R.mipmap.icon_zan_ed);
            }
            holder.tv_huifu.setText(itemData.getComment() == null ? "0": itemData.getComment().size()+"");
            if (!TextUtils.isEmpty(itemData.getZhaiyao())){
                holder.tv_text.setVisibility(View.VISIBLE);
                holder.tv_text.setText(itemData.getZhaiyao());
            }else {
                holder.tv_text.setVisibility(View.GONE);
            }
            ImageGridAdapter gridAdapter = new ImageGridAdapter(context , itemData);
            if ("图片".equals(itemData.getFields().getFm_type())){
                holder.tv_yinpin.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_shipin , 0 , 0 , 0);
                holder.tv_yinpin.setText("图片");
            }else if ("音频".equals(itemData.getFields().getFm_type())){
                holder.tv_yinpin.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_yinpin , 0 , 0 , 0);
                holder.tv_yinpin.setText("音频");
            }else {
                holder.tv_yinpin.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_shipin, 0 , 0 , 0);
                holder.tv_yinpin.setText("视频");
            }

            if (itemData.getArticle_zan() != null){
                ArrayList<ArticleListBean.Data.Article_zan> zans = itemData.getArticle_zan();
                for (int i =0 ; i< zans.size(); i++){
                    String url_img = Urls.URL_AVATAR_HOST+zans.get(i).getImg_url();
                    switch (i){
                        case 0:
                            Glide.with(context).load(url_img).error(R.mipmap.icon_defualt_head).into(holder.iv_dianzan_one);
                            break;

                        case 1:
                            Glide.with(context).load(url_img).error(R.mipmap.icon_defualt_head).into(holder.iv_dianzan_two);
                            break;

                        case 2:
                            Glide.with(context).load(url_img).error(R.mipmap.icon_defualt_head).into(holder.iv_dianzan_three);
                            break;

                        case 3:
                            Glide.with(context).load(url_img).error(R.mipmap.icon_defualt_head).into(holder.iv_dianzan_four);
                            break;

                        case 4:
                            Glide.with(context).load(url_img).error(R.mipmap.icon_defualt_head).into(holder.iv_dianzan_five);
                            break;
                    }
                }
            }

            holder.grid_albums.setAdapter(gridAdapter);
            holder.grid_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        mOnItemClickListener.onItemClick(holder.itemView, itemData);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
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

    class ViewHolder1 extends ViewHolder{
        @BindView(R.id.iv_icon)
        CircleImageView iv_icon;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_yinpin)
        TextView tv_yinpin;
        @BindView(R.id.tv_count)
        TextView tv_count;
        @BindView(R.id.tv_text)
        TextView tv_text;
        @BindView(R.id.tv_huifu)
        TextView tv_huifu;
        @BindView(R.id.tv_zan)
        TextView tv_zan;
        @BindView(R.id.iv_zan)
        ImageView iv_zan;

        @BindView(R.id.iv_dianzan_one)
        CircleImageView iv_dianzan_one;
        @BindView(R.id.iv_dianzan_two)
        CircleImageView iv_dianzan_two;
        @BindView(R.id.iv_dianzan_three)
        CircleImageView iv_dianzan_three;
        @BindView(R.id.iv_dianzan_four)
        CircleImageView iv_dianzan_four;
        @BindView(R.id.iv_dianzan_five)
        CircleImageView iv_dianzan_five;

        @BindView(R.id.grid_albums)
        GridView grid_albums;

        public ViewHolder1(View itemView){
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
            TextView bt_bofang = (TextView) convertView.findViewById(R.id.bt_bofang);
            if (isMedia){
                if ("mp3".equalsIgnoreCase(attachs.get(position).getFile_ext())){
                    bt_bofang.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }else {
                    bt_bofang.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.mipmap.icon_shipin_click);

                    Bitmap bitmap = asyncBitmapLoader.LoadBitmapByVideo(Urls.URL_AVATAR_HOST + attachs.get(position).getFile_path(), imageView, new AsyncBitmapByVideo.ImageCallback() {
                        @Override
                        public void imageLoaded(Bitmap b, ImageView imageview, String imageUrl) {
                            imageview.setBackgroundDrawable(new BitmapDrawable(b));
                        }
                    });
                    if (bitmap != null){
                        imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }
                }
            }else {
                bt_bofang.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(Urls.URL_AVATAR_HOST+albums.get(position).getThumb_path()).asBitmap().error(R.mipmap.ic_launcher).into(new SimpleTarget<Bitmap>() {
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