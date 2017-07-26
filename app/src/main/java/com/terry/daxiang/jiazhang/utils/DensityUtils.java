package com.terry.daxiang.jiazhang.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/9/3.
 */
public class DensityUtils {
    static DisplayMetrics dm;

    /**
     ** 屏幕参数
      * @param context
      * @return
     */
    public static DisplayMetrics getScreenDetail(Context context) {
        if (dm == null) {
            dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
        }
        return dm;
    }
    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取手机SDK版本
     *
     * @return
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int[] getDefaultDisplay(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int[] ints = new int[2];
        ints[0] = display.getWidth();
        ints[1] = display.getHeight();
        return ints;
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param activity
     * @param bgAlpha
     */
    public static void setWindomBgAlpha(Activity activity, float bgAlpha){

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}