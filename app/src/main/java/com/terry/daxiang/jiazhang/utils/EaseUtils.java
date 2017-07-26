package com.terry.daxiang.jiazhang.utils;

import android.app.Activity;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.terry.daxiang.jiazhang.R;

/**
 * Created by happy on 2017/7/11.
 */

public class EaseUtils {
    /**
     * 注册
     * @param activity
     * @param username
     * @param pwd
     */
    public static void registered(final Activity activity, final String username, final String pwd, final RegisteredCallBack callBack){
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(username, pwd);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            // save current user
                            DemoHelper.getInstance().setCurrentUserName(username);
//                            Toast.makeText(activity, activity.getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                            callBack.registeredSuccessFul();
                        }
                    });
                } catch (final HyphenateException e) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            int errorCode=e.getErrorCode();

                            if(errorCode== EMError.NETWORK_ERROR){
                                Toast.makeText(activity, activity.getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                if(callBack != null){
                                    callBack.error(e.toString()+errorCode);
                                }
                            }else if(errorCode == EMError.USER_ALREADY_EXIST){
//                                Toast.makeText(activity, activity.getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                if(callBack != null){
                                    callBack.alreadyExisted(e.toString()+errorCode);
                                }
                            }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                Toast.makeText(activity, activity.getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                if(callBack != null){
                                    callBack.error(e.toString()+errorCode);
                                }
                            }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                Toast.makeText(activity, activity.getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                if(callBack != null){
                                    callBack.error(e.toString()+errorCode);
                                }
                            }else{
                                Toast.makeText(activity, activity.getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                if(callBack != null){
                                    callBack.error(e.toString()+errorCode);
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public interface RegisteredCallBack{
        void registeredSuccessFul();
        void alreadyExisted(String erro);
        void error(String error);
    }
}
