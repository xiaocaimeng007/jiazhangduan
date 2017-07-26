package com.terry.daxiang.jiazhang.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.terry.daxiang.jiazhang.activity.MyApplication;

public class ToastUtil {
    private static Toast mToast = null;

    /**
     * 防止Toast重复显示
     * @param text
     */
    public static void show(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void show(String text, int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(gravity, 0, 120);
        mToast.show();
    }

    public static void showLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }

        mToast.show();
    }

    public static void showLong(String text , int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.setGravity(gravity, 0, 120);
        mToast.show();
    }

    public static void show(int strId) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), strId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(strId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showLong(int strId) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), strId, Toast.LENGTH_LONG);
        } else {
            mToast.setText(strId);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
}

