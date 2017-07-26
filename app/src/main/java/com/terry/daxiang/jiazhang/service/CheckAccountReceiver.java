package com.terry.daxiang.jiazhang.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.activity.LoginActivity;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.ResponseCallback;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import org.json.JSONObject;

/**
 * Created by fulei on 17/2/13.
 */

public class CheckAccountReceiver extends BroadcastReceiver implements ResponseCallback {

    public final static String ACTION_CHECKACCOUNT_DAXIANG = "com.checkaccoudnt.daxiang.action";

    private Context context;
    private final int WHAT_CHECK_RESULT = 0x001;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.e("--" , "CheckAccountReceiver -- > onReceive");
        RequestService.doGet(Urls.doCheckLoginState(SharedPrefUtils.getString(context , SharedPrefUtils.APP_USER_ID)) , WHAT_CHECK_RESULT , this);

    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        try {
            Log.e("--" , "CheckAccountReceiver -- >onSuccess -- "+responseStr);
            JSONObject object = new JSONObject(responseStr);
            String data_token = object.optString("data" , "");
            Log.e("--" , SharedPrefUtils.getString(context , SharedPrefUtils.APP_USER_TOKEN)+" <-- CheckAccountReceiver -- > "+data_token);
            if (TextUtils.isEmpty(data_token) || !data_token.equals(SharedPrefUtils.getString(context , SharedPrefUtils.APP_USER_TOKEN))){
                //环信在这里要选择登出
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {//环信登出成功 进入到登陆界面
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                context.stopService(new Intent(context , TimeService.class));
                                SharedPrefUtils.setBoolean(context , SharedPrefUtils.APP_USER_ISLOGIN , false);
                                SharedPrefUtils.setString(context,SharedPrefUtils.APP_USER_ID,"");
                                ToastUtil.show("您的账号在另外一个设备登录,请重新登录!");
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                System.exit(0);
                            }
                        });
                    }
                    @Override
                    public void onProgress(int progress, String status) {
                    }
                    @Override
                    public void onError(final int code, String message) {
                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        Log.e("--" , "CheckAccountReceiver -- >onFailure -- ");
    }
}
