package com.chad.library.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * activity 跳转
 *
 * Created by fulei on 16/10/25.
 */
public interface MobSkipActivity {
    /**
     * 跳转Activity,已finish()
     * @param activity
     * @param cls
     */
    public void skipActivity(Activity activity, Class<?> cls);
    /**
     * 跳转Activity,已finish()
     * @param activity
     * @param intent
     */
    public void skipActivity(Activity activity, Intent intent);
    /**
     * 跳转Activity,已finish()
     * @param activity
     * @param cls
     * @param bundle
     */
    public void skipActivity(Activity activity, Class<?> cls, Bundle bundle);
    /**
     * 显示Activity, 无finish()
     * @param activity
     * @param cls
     */
    public void showActivity(Activity activity, Class<?> cls);
    /**
     * 显示Activity, 无finish()
     * @param activity
     * @param intent
     */
    public void showActivity(Activity activity, Intent intent);
    /**
     * 显示Activity, 无finish()
     * @param activity
     * @param cls
     * @param bundle
     */
    public void showActivity(Activity activity, Class<?> cls, Bundle bundle);
    /**
     * 带返回参数的跳转
     * @param activity
     * @param intent
     * @param requestCode
     */
    public void showActivityForResult(Activity activity, Intent intent, int requestCode);
    /**
     * 带返回参数的跳转
     * @param activity
     * @param cls
     * @param requestCode
     */
    public void showActivityForResult(Activity activity, Class<?> cls, int requestCode);
    /**
     * 带返回参数的跳转
     * @param activity
     * @param intent
     * @param requestCode
     */
    public void showActivityForResult(Activity activity, Intent intent, int requestCode, Bundle bundle);
}
