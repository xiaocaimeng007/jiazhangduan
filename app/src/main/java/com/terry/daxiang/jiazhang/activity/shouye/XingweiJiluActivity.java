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

public class XingweiJiluActivity extends BaseActivity implements OnCheckedChangeListener {

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
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rg_xingwei)
    RadioGroup rgXingwei;
    @BindView(R.id.vp_xingwei)
    NoScrollViewPager vpXingwei;

    public final static String ACTION_XINGWEI_USER_INFO = "action.xingwei.user.info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xingwei_jilu);
        ButterKnife.bind(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_XINGWEI_USER_INFO);
        registerReceiver(broadcastReceiver , filter);

        setTextview(tvTitle, "行为记录");
        rgXingwei.setOnCheckedChangeListener(this);
        initData();
    }

    private void initData() {
        Xingwei_Tian_Fragment fragment1 = new Xingwei_Tian_Fragment();
        Xingwei_Zhou_Fragment fragment2 = new Xingwei_Zhou_Fragment();
        Xingwei_Yue_Fragment fragment3 = new Xingwei_Yue_Fragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        vpXingwei.setAdapter(new MyAdapter(getSupportFragmentManager(), fragments));
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_1:
                vpXingwei.setCurrentItem(0, false);
                break;
            case R.id.rb_2:
                vpXingwei.setCurrentItem(1, false);
                //加载数据
                break;
            case R.id.rb_3:
                vpXingwei.setCurrentItem(2, false);
                //加载数据
                break;
        }
    }

    class MyAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
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