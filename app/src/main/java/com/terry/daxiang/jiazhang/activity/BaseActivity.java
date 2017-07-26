package com.terry.daxiang.jiazhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.terry.daxiang.jiazhang.R;


public class BaseActivity extends ActionBarActivity {

    TextView tvTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
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