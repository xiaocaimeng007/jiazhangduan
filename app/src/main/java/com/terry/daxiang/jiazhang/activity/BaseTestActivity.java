package com.terry.daxiang.jiazhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.MobActivity;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.custom.ResponseCallback;


public abstract class BaseTestActivity extends ActionBarActivity implements ResponseCallback {

    TextView tvTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        initView();
        super.onCreate(savedInstanceState);
    }

    public void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
    }

    public void setTextview(TextView tv, String title) {
        tv.setText(title);
    }

    public void hideView(View v) {
        v.setVisibility(View.GONE);
    }

    public void showView(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void startActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(this, c);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void startActivityForResult(Class c, Bundle bundle , int code) {
        Intent intent = new Intent(this, c);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent , code);
    }

    public void toast(String s) {
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}