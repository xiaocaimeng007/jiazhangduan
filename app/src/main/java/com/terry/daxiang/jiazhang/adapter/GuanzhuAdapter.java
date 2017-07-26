package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/20.
 */
public class GuanzhuAdapter extends Adapter<ViewHolder>{

    private Context context;
    private ArrayList<ArticleListBean.Data> datas;
    private LayoutInflater inflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public GuanzhuAdapter(Context context , ArrayList<ArticleListBean.Data> datas){
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_tianjia_guanzhu, parent, false);
        ViewHodler1 hodler1 = new ViewHodler1(view);
        return hodler1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ArticleListBean.Data data = this.datas.get(position);
        ((ViewHodler1)holder).itemView.setTag(data);
        ((ViewHodler1)holder).tv_name.setText(data.getTitle());
        ((ViewHodler1)holder).tv_text.setText(data.getContent());
        ((ViewHodler1)holder).tv_tianjia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onTianjiaClick(data);
                }
            }
        });
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onTianjiaClick(ArticleListBean.Data data);
    }

    class ViewHodler1 extends ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_text)
        TextView tv_text;
        @BindView(R.id.tv_tianjia)
        TextView tv_tianjia;

        public ViewHodler1(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
