package com.terry.daxiang.jiazhang.activity;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoApplication;
import com.hyphenate.chatuidemo.DemoHelper;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fulei on 16/11/8.
 */

public class MyApplication extends DemoApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);     		// 初始化 JPush
        DemoHelper.getInstance().init(applicationContext);
        EMClient.getInstance().setDebugMode(true);
    }
}
