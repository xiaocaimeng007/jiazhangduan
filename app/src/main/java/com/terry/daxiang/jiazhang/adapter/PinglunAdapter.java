package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleContentBean;
import com.terry.daxiang.jiazhang.json.Pinglun.PinglunBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class PinglunAdapter extends BaseQuickAdapter<ArticleContentBean.Comment> {

    private Context context;

    public PinglunAdapter(Context context ,int layoutResId, List data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleContentBean.Comment item) {
//        helper.setText(R.id.tv_name, item.getName())
//                .setText(R.id.tv_text, item.getText());

        TextView textView = helper.getView(R.id.tv_text);
        TextView txt_name = helper.getView(R.id.tv_name);
        String content = item.getContent();
        if (content.startsWith("回复")){
            txt_name.setText("回复");
            txt_name.setTextColor(context.getResources().getColor(R.color.lan));
            textView.setText(content.substring(2));
        }else {
            txt_name.setText(item.getUser_name()+" : ");
            txt_name.setTextColor(context.getResources().getColor(R.color.color_7c7c7c));
            textView.setText(content);
        }
    }
}