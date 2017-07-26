package com.terry.daxiang.jiazhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.view.CircleIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YinDaoActivity extends ActionBarActivity {
    ArrayList<Integer> pages = new ArrayList();
    @BindView(R.id.vp_yindao)
    ViewPager vpYindao;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.go)
    TextView go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_yindao);
        ButterKnife.bind(this);
        pages.add(R.layout.page_yindao1);
        pages.add(R.layout.page_yindao2);
        pages.add(R.layout.page_yindao3);
        vpYindao.setAdapter(new MyAdapter());
        indicator.setViewPager(vpYindao);
        vpYindao.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==pages.size()-1){
                    indicator.setVisibility(View.GONE);
                    go.setVisibility(View.VISIBLE);
                }else {
                    indicator.setVisibility(View.VISIBLE);
                    go.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.go)
    public void onClick() {
        SharedPrefUtils.setBoolean(getApplicationContext() , SharedPrefUtils.APP_ISFIRST , true);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(YinDaoActivity.this, pages.get(position), null);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
