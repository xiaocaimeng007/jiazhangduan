package com.hyphenate.easeui.utils;

import android.content.Context;

/**
 * 软件参数设置器 默认全部使用String类型存储数据，如有其他类型数据请自行转换。
 */
public class SharedPrefUtils {

    public static final String APP_CACHE = "app_cache";

    /**
     * SharedPreferences xml 名称
     */
    private static final String APP_SHARED_STR = "HealthCouldMgr_Pref";

    public static final String APP_ISFIRST = "APP_FIRST";
    public static final String APP_USER_ACCOUNT = "app_user_account";
    /*******用户资料*********/
    public static final String APP_USER_ISLOGIN = "app_user_islogin"; // 判断是否登录
    public static final String APP_USER_ID = "app_user_id";
    public static final String APP_USER_GROUPID = "app_user_groupid"; // 分组id
    public static final String APP_USER_CHILDRENID = "app_user_childrenid"; // 孩子id
    public static final String APP_USER_GRADEID ="app_user_gradeid"; //年级id
    public static final String APP_USER_GRADENAME ="app_user_gradename"; //年级id
    public static final String APP_USER_NAME = "app_user_name"; // 教师名称
    public static final String APP_USER_MOBILE = "app_user_mobile"; // 教师手机号码
    public static final String APP_USER_AVATAR = "app_user_avatar";// 头像
    public static final String APP_USER_NICKNAME = "app_user_nickname"; // 昵称
    public static final String APP_USER_SET = "app_user_sex"; // 性别
    public static final String APP_USER_TELPHONE = "app_user_telphone"; // 电话
    /*******用户资料*********/
    public static final String APP_USER_TOKEN = "app_user_token";//登录帐号token

    public static final String APP_BANJIQUAN_YUANSUO_MAX = "app_banjiquan_yuansuo_max";
    public static final String APP_BANJIQUAN_JINGCAI_MAX = "app_banjiquan_jingcai_max";
    public static final String APP_BANJIQUAN_TONGZHI_MAX = "app_banjiquan_tongzhi_max";
    public static final String APP_BANJIQUAN_SHIPU_MAX = "app_banjiquan_shipu_max";

    /**
     * 从SharedPreferences 获取一个boolean值，默认为false
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences(APP_SHARED_STR,
                Context.MODE_PRIVATE).getBoolean(key, false);
    }

    /**
     * 从SharedPreferences 获取一个boolean值
     *
     * @param context
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(APP_SHARED_STR,
                Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    /**
     * 设置 一个boolean 值到SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static synchronized void setBoolean(Context context, String key,
                                               boolean value) {
        context.getSharedPreferences(APP_SHARED_STR, Context.MODE_PRIVATE)
                .edit().putBoolean(key, value).commit();
    }

    /**
     * 从SharedPreferences 获取一个String值，默认为null
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return context.getSharedPreferences(APP_SHARED_STR,
                Context.MODE_PRIVATE).getString(key, "");
    }

    /**
     * 设置 一个String 值到SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static synchronized void setString(Context context, String key,
                                              String value) {
        try {
            context.getSharedPreferences(APP_SHARED_STR, Context.MODE_PRIVATE)
                    .edit().putString(key, value).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 应用网络请求数据缓存
     *
     * @param context
     * @param url
     * @param value
     */
    public static synchronized void storeAppCache(Context context, String url, String value) {
        context.getSharedPreferences(APP_CACHE, Context.MODE_APPEND).edit().putString(url, value).commit();
    }

    /**
     * 获取网络请求的缓存数据
     *
     * @param context
     * @param url
     * @return
     */
    public static synchronized String getAppCache(Context context, String url) {
        return context.getSharedPreferences(APP_CACHE, Context.MODE_APPEND).getString(url, null);
    }

    /**
     * 清除应用缓存
     *
     * @param context
     */
    public static synchronized void clearAppCache(Context context) {
        context.getSharedPreferences(APP_CACHE, Context.MODE_APPEND).edit().clear();
    }

    /**
     * 从SharedPreferences 获取一个Int值,默认值用户自己设置
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static int getInt(Context context, String key, int value) {
        return context.getSharedPreferences(APP_SHARED_STR,
                Context.MODE_PRIVATE).getInt(key, value);
    }

    /**
     * 设置一个Int值到 SharedPreferences中
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setInt(Context context, String key, int value) {
        context.getSharedPreferences(APP_SHARED_STR, Context.MODE_PRIVATE)
                .edit().putInt(key, value).commit();
    }

    /**
     * 清除所有存储数据
     * @param context
     */
    public static void clearSharedPref(Context context){
        context.getSharedPreferences(APP_SHARED_STR, Context.MODE_APPEND).edit().clear();
    }


}
