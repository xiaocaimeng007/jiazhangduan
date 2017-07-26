package com.terry.daxiang.jiazhang.activity.shouye;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseActivity;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.fragment.Xingwei_Tian_Fragment;
import com.terry.daxiang.jiazhang.fragment.Xingwei_Yue_Fragment;
import com.terry.daxiang.jiazhang.fragment.Xingwei_Zhou_Fragment;
import com.terry.daxiang.jiazhang.view.CircleImageView;
import com.terry.daxiang.jiazhang.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XingweiJiluSingleActivity extends BaseActivity{

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
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;

    public final static String ACTION_XINGWEI_USER_INFO = "action.xingwei.user.info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xingwei_single);
        ButterKnife.bind(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_XINGWEI_USER_INFO);
        registerReceiver(broadcastReceiver , filter);

        setTextview(tvTitle, "行为记录");
        Xingwei_Tian_Fragment fragment1 = new Xingwei_Tian_Fragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_xingwei , fragment1).commitAllowingStateLoss();
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String xw_name = intent.getStringExtra("xw_hildname");
                String xw_childavator = intent.getStringExtra("xw_childavator");
                Glide.with(context).load(Urls.URL_AVATAR_HOST+xw_childavator).into(ivIcon);
                tvName.setText(xw_name);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

}