package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/20.
 */
public class ZhuanjiAdapter extends Adapter<ViewHolder> implements View.OnClickListener {
    private Context context;
    private ArrayList<ArticleListBean.Data> datas = new ArrayList<>();
    private LayoutInflater inflater;

    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public ZhuanjiAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void setDatas(List<ArticleListBean.Data> data) {

        this.datas.clear();
        if (data != null) {
            this.datas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setDataUp(List<ArticleListBean.Data> data) {
        this.datas.addAll(0, data);
        notifyDataSetChanged();
    }

    public void setDataDown(List<ArticleListBean.Data> data) {
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    public List<ArticleListBean.Data> getAllData() {
        return datas;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, ArticleListBean.Data data);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (ArticleListBean.Data) v.getTag());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_zhuanji, parent, false);
        view.setOnClickListener(this);
        ViewHodler1 hodler1 = new ViewHodler1(view);
        return hodler1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ArticleListBean.Data data = this.datas.get(position);
        ((ViewHodler1) holder).itemView.setTag(data);
        ((ViewHodler1) holder).tv_name.setText(data.getTitle());
        ((ViewHodler1) holder).tv_description.setText(data.getZhaiyao() == null ? "" : data.getZhaiyao());
    }

    class ViewHodler1 extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_description)
        TextView tv_description;

        public ViewHodler1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}