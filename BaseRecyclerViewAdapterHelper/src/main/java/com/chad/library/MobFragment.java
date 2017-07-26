package com.chad.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.ui.MobAnnotate;
import com.chad.library.ui.MobSkipActivity;
import com.chad.library.utils.LogUtil;

/**
 * 基础Fragment初始化
 * Created by fulei on 16/10/24.
 */
public abstract class MobFragment extends Fragment implements View.OnClickListener , MobSkipActivity {

    private View view;
    public Activity activity;
    public Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflaterView(inflater, container, savedInstanceState);
        //注释可以初始化View
        MobAnnotate.init(this);
        //初始化Handler
        initHandle();
        // 初始化数据
        initData();
        // 初始化界面
        initWidget(view);

        return view;
    }

    @Override
    public View getView() {
        //this.getActivity().getWindow().getDecorView();
        return view;
    }

    @Override
    public void onDestroy() {
        mHandler = null;
        super.onDestroy();
    }

    /**
     * 添加Fragment layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 处理消息结果
     *
     * @param msg
     */
    protected abstract void onHeadler(Message msg)throws Exception;

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    private void initHandle(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    onHeadler(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(getClass().getName(), "handleMessage():"+e);
                }
            }
        };
    }

    /**
     * 初始化界面，当嵌套在外部HCActivity时，初始化外部的控件不可使用@HCBindView进行绑定控件
     * @param parentView
     */
    protected void initWidget(View parentView) {

    }

    /**
     * 控件点击
     * @param v
     */
    protected void widgetClick(View v) {

    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls) {
        // TODO Auto-generated method stub
        showActivity(activity, cls);
        activity.finish();
    }

    @Override
    public void skipActivity(Activity activity, Intent intent) {
        // TODO Auto-generated method stub
        showActivity(activity, intent);
        activity.finish();
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls, Bundle bundle) {
        // TODO Auto-generated method stub
        showActivity(activity, cls, bundle);
        activity.finish();

    }

    @Override
    public void showActivity(Activity activity, Class<?> cls) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        setAnimAction();
    }

    @Override
    public void showActivity(Activity activity, Intent intent) {
        // TODO Auto-generated method stub
        activity.startActivity(intent);
        setAnimAction();
    }

    @Override
    public void showActivity(Activity activity, Class<?> cls, Bundle bundle) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        setAnimAction();
    }

    @Override
    public void showActivityForResult(Activity activity, Intent intent,
                                      int requestCode) {
        // TODO Auto-generated method stub
        activity.startActivityForResult(intent, requestCode);
        setAnimAction();
    }

    @Override
    public void showActivityForResult(Activity activity, Class<?> cls,
                                      int requestCode) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
        setAnimAction();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showActivityForResult(Activity activity, Intent intent,
                                      int requestCode, Bundle bundle) {
        // TODO Auto-generated method stub
        activity.startActivityForResult(intent, requestCode, bundle);
        setAnimAction();
    }

    /**
     * 设置跳转动画
     */
    private void setAnimAction(){
        if("left".equals(MobContance.isAnimDirection)){
            activity.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_right);
        }else if("right".equals(MobContance.isAnimDirection)){
            activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        }
    }
}
