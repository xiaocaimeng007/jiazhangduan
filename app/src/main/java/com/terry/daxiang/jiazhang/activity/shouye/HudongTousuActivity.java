package com.terry.daxiang.jiazhang.activity.shouye;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HudongTousuActivity extends BaseActivity {

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
    @BindView(R.id.tv_fabu)
    EditText tvFabu;
    @BindView(R.id.iv_tianjia)
    ImageView ivTianjia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hudong_tousu);
        ButterKnife.bind(this);
        setTextview(tvTitle,"互动投诉");
        showView(llSend);
        hideView(ivJiahao);
    }

    @OnClick({R.id.ll_back, R.id.tv_send, R.id.iv_tianjia})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                break;
            case R.id.tv_send:
                break;
            case R.id.iv_tianjia:
                break;
        }
    }
}