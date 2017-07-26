package com.chad.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chad.library.ui.MobAnnotate;
import com.chad.library.ui.MobSkipActivity;
import com.chad.library.utils.LogUtil;

import java.lang.ref.WeakReference;

/**
 * 基础Activity初始化
 * Created by fulei on 16/10/24.
 */
public abstract class MobActivity extends AppCompatActivity implements View.OnClickListener , MobSkipActivity{

    public Activity activity ;
    protected WeakHeadler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);

        try {
            setRootView();
        } catch (Exception e) {
            LogUtil.e(getClass().getName(), "setRootView()->"+e);
        }
        try {
            MobAnnotate.init(this);
        } catch (Exception e) {
            LogUtil.e(getClass().getName(), "Annotate.init()->"+e);
        }
        try {
            initHandle();
        } catch (Exception e) {
            LogUtil.e(getClass().getName(), "initHandle()->"+e);
        }
        try {
            // 初始化数据
            initData();
        } catch (Exception e) {
            LogUtil.e(getClass().getName(), "initData()->"+e);
        }

        try {
            // 初始化控件
            initWidget();
        } catch (Exception e) {
            LogUtil.e(getClass().getName(), "initWidget()->"+e);
        }

    }

    public abstract void setRootView();

    /**
     * 初始化数据
     */
    public void initData() {
        // TODO Auto-generated method stub

    }
    /**
     * 初始化控件
     */
    public void initWidget() {
        // TODO Auto-generated method stub
    }

    public abstract void widgetClick(View v);

    private void initHandle() {
        mHandler = new WeakHeadler(activity);
    }

    /**
     * 处理消息结果
     *
     * @param msg
     */
    protected abstract void onHeadler(Message msg)throws Exception;

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    @Override
    protected void onDestroy() {
        mHandler = null;
        activity = null;

        super.onDestroy();

        // 提示系统可以释放内存了
        System.gc();
    }

    /**
     * 切换视图
     * @param resView
     * @param hcFragement
     */
    public void changeFragment(int resView,MobFragment hcFragement){
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(resView, hcFragement, hcFragement.getClass().getName());
            transaction.commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls) {
        showActivity(activity, cls);
        activity.finish();
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls, Bundle bundle) {
        showActivity(activity, cls, bundle);
        activity.finish();
    }

    @Override
    public void skipActivity(Activity activity, Intent intent) {

        showActivity(activity, intent);
        activity.finish();
    }

    @Override
    public void showActivity(Activity activity, Class<?> cls) {
        try {
            Intent intent = new Intent();
            intent.setClass(activity, cls);
            activity.startActivity(intent);
            setAnimAction();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showActivity(Activity activity, Class<?> cls, Bundle bundle) {
        try {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setClass(activity, cls);
            activity.startActivity(intent);
            setAnimAction();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showActivity(Activity activity, Intent intent) {
        try {
            activity.startActivity(intent);
            setAnimAction();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showActivityForResult(Activity activity, Class<?> cls, int requestCode) {
        try {
            Intent intent = new Intent();
            intent.setClass(activity, cls);
            activity.startActivityForResult(intent, requestCode);
            setAnimAction();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void showActivityForResult(Activity activity, Intent intent, int requestCode) {
        try {
            activity.startActivityForResult(intent, requestCode);
            setAnimAction();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void showActivityForResult(Activity activity, Intent intent, int requestCode, Bundle bundle) {
        try {
            activity.startActivityForResult(intent, requestCode, bundle);
            setAnimAction();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    /**
     * 重写handler 使用软引用 ，及时释放activity
     * @author chen_fulei
     *
     */
    public class WeakHeadler extends Handler{
        WeakReference<Activity> reference = null;

        public WeakHeadler(Activity act){
            reference = new WeakReference<Activity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                onHeadler(msg);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(getClass().getName(), "handleMessage():"+e);
            }
            super.handleMessage(msg);
        }
    }

}
