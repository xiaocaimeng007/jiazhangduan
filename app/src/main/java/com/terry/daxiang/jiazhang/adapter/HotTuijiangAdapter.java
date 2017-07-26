package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.custom.Urls;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fulei on 16/12/17.
 */

public class HotTuijiangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context ;
    private LayoutInflater inflater;
    private ArrayList<ArticleListBean.Data> datas = new ArrayList<>();
    OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public HotTuijiangAdapter(Context context){
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
        View view = inflater.inflate(R.layout.item_faxian_hot, parent, false);
        view.setOnClickListener(this);
        ViewHodler1 hodler1 = new ViewHodler1(view);
        return hodler1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ArticleListBean.Data data = datas.get(position);
        ((ViewHodler1)holder).itemView.setTag(data);
        Glide.with(context).load(Urls.URL_AVATAR_HOST+data.getImg_url()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ((ViewHodler1)holder).iv_icon.setBackgroundDrawable(new BitmapDrawable(resource));
            }
        });
        ((ViewHodler1)holder).tv_name.setText(data.getTitle());
        ((ViewHodler1)holder).tv_text.setText(data.getZhaiyao());
    }

    class ViewHodler1 extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_text)
        TextView tv_text;
        @BindView(R.id.iv_icon)
        ImageView iv_icon;

        public ViewHodler1(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
}
