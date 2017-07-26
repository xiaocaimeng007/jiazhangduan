package com.chad.library.utils;

/**
 *  logCat 打印
 * Created by chen_fulei on 2016/10/25.
 */
public class LogUtil {

    public static final String TAG = "Mob";
    public static final boolean LOG_AD_RESPONSES = false;

    public static boolean isLoggables(int logLevel) {
        return android.util.Log.isLoggable(TAG, logLevel);
    }

    public static void d(final String msg) {
        if (isLoggables(android.util.Log.DEBUG)) {
            android.util.Log.d(TAG, msg);
        }
    }
    public static void d(final String TAG ,final String msg) {
        if (isLoggables(android.util.Log.DEBUG)) {
            android.util.Log.d(TAG, msg);
        }
    }

    public static void d(final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.DEBUG)) {
            android.util.Log.d(TAG, msg, tr);
        }
    }

    public static void d(final String TAG ,final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.DEBUG)) {
            android.util.Log.d(TAG, msg, tr);
        }
    }

    public static void e(final String msg) {
        if (isLoggables(android.util.Log.ERROR)) {
            android.util.Log.e(TAG, msg);
        }
    }

    public static void e(final String TAG ,final String msg) {
        if (isLoggables(android.util.Log.ERROR)) {
            android.util.Log.e(TAG, msg);
        }
    }

    public static void e(final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.ERROR)) {
            android.util.Log.w(TAG, msg, tr);
        }
    }

    public static void e(final String TAG ,final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.ERROR)) {
            android.util.Log.w(TAG, msg, tr);
        }
    }

    public static void i(final String msg) {
        if (isLoggables(android.util.Log.INFO)) {
            android.util.Log.i(TAG, msg);
        }
    }

    public static void i(final String TAG ,final String msg) {
        if (isLoggables(android.util.Log.INFO)) {
            android.util.Log.i(TAG, msg);
        }
    }

    public static void i(final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.INFO)) {
            android.util.Log.i(TAG, msg, tr);
        }
    }

    public static void i(final String TAG ,final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.INFO)) {
            android.util.Log.i(TAG, msg, tr);
        }
    }

    public static void v(final String msg) {
        if (isLoggables(android.util.Log.VERBOSE)) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static void v(final String TAG ,final String msg) {
        if (isLoggables(android.util.Log.VERBOSE)) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static void v(final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.VERBOSE)) {
            android.util.Log.v(TAG, msg, tr);
        }
    }

    public static void v(final String TAG ,final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.VERBOSE)) {
            android.util.Log.v(TAG, msg, tr);
        }
    }

    public static void w(final String msg) {
        if (isLoggables(android.util.Log.WARN)) {
            android.util.Log.w(TAG, msg);
        }
    }

    public static void w(final String TAG ,final String msg) {
        if (isLoggables(android.util.Log.WARN)) {
            android.util.Log.w(TAG, msg);
        }
    }

    public static void w(final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.WARN)) {
            android.util.Log.w(TAG, msg, tr);
        }
    }
    public static void w(final String TAG ,final String msg, final Throwable tr) {
        if (isLoggables(android.util.Log.WARN)) {
            android.util.Log.w(TAG, msg, tr);
        }
    }

}
