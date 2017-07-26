package com.terry.daxiang.jiazhang.activity.shouye;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseActivity;
import com.terry.daxiang.jiazhang.json.JSON;
import com.terry.daxiang.jiazhang.json.Jiankong;
import com.terry.daxiang.jiazhang.json.Jiankong.JiankongBean;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JiankongLishiActivity extends BaseActivity {

    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.iv_gengduo)
    ImageView ivGengduo;
    @BindView(R.id.iv_jiahao)
    ImageView ivJiahao;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_jiankong)
    MyRecyclerView rvJiankong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiankong_lishi);
        ButterKnife.bind(this);
        showView(tvClose);
        setTextview(tvTitle, "历史");
        List<JiankongBean> data = new Gson().fromJson(JSON.jiankong, Jiankong.class).getData();
        rvJiankong.setLayoutManager(new LinearLayoutManager(this));
        rvJiankong.setAdapter(new Adapter(R.layout.item_jiankong_lishi, data));
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    class Adapter extends BaseQuickAdapter<JiankongBean> {

        public Adapter(int layoutResId, List<JiankongBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, JiankongBean item) {
            helper.setText(R.id.tv_weizhi, item.getWeizhi())
                    .setText(R.id.tv_zhuangtai, item.getZhuangtai())
                    .setText(R.id.tv_time, item.getShijian());
        }
    }
}
