package com.terry.daxiang.jiazhang.custom;

/**
 * 网络请求响应监听
 * Created by fulei on 16/11/8.
 */

public interface ResponseCallback {
    void onSuccess(String responseStr, int requestCodes);
    void onFailure(String responseStr, int requestCode);
}
