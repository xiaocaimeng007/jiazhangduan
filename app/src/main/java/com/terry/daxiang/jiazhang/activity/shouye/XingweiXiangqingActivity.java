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
import com.terry.daxiang.jiazhang.json.XingweiXiangqing;
import com.terry.daxiang.jiazhang.json.XingweiXiangqing.XingweiXiangqingBean;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XingweiXiangqingActivity extends BaseActivity {

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
    @BindView(R.id.rv_xingwei)
    MyRecyclerView rvXingwei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xingwei_xiangqing);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        //获取bundle传过来的数据
        String title = bundle.getString("title");
        setTextview(tvTitle, "行为记录 - " + title);
        showView(tvClose);
        initData();
    }

    private void initData() {
        List<XingweiXiangqingBean> data = new Gson().fromJson(JSON.xingweixiangqing, XingweiXiangqing.class).getData();
        rvXingwei.setLayoutManager(new LinearLayoutManager(this));
        rvXingwei.setAdapter(new Adapter(R.layout.item_xingwei_xiangqing, data));
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    class Adapter extends BaseQuickAdapter<XingweiXiangqingBean> {

        public Adapter(int layoutResId, List<XingweiXiangqingBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, XingweiXiangqingBean item) {
            helper.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_text, item.getText());
        }
    }
}
