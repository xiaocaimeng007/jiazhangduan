package com.terry.daxiang.jiazhang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.terry.daxiang.jiazhang.R;
import com.hyphenate.easeui.utils.SharedPrefUtils;


public class MainActivity extends ActionBarActivity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (SharedPrefUtils.getBoolean(MyApplication.getInstance() , SharedPrefUtils.APP_USER_ISLOGIN , false)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (DemoHelper.getInstance().isLoggedIn()) {//自动登陆 确保自动加载所有的聊天内容
                                // auto login mode, make sure all group and conversation is loaed before enter the main screen
                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();
                            }
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }).start();
                }else {
                    if (SharedPrefUtils.getBoolean(getApplicationContext() , SharedPrefUtils.APP_ISFIRST , false)){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(MainActivity.this, YinDaoActivity.class);
                        startActivity(intent);
                    }
                }

                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);
        handler.sendEmptyMessageDelayed(0, 2000);
    }
}