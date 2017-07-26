package com.terry.daxiang.jiazhang.utils;

import com.chad.library.utils.LogUtil;
import com.terry.daxiang.jiazhang.bean.ResultBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络数据结构处理类
 *
 * @author sgrp
 */
public class ResultUtil {

    public static final int SEVICE_ERROR = 500;//服务器出错
    public static final int REQUEST_SUCCESS = 1;//请求正常正常
    public static final int NET_ERROR = 71;

    /**
     * @param json   客户端返回的json数据
     * @param isShow 是否用toast显示msg
     * @return
     */
    public static synchronized ResultBean getResult(String json, boolean isShow) {
        LogUtil.e(json);
        ResultBean resultBean = new ResultBean();
        if (json == null) {
            return resultBean;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("status")) {
                resultBean.setStatus(jsonObject.getInt("status"));
            }
            if (jsonObject.has("status") && jsonObject.getInt("status") == 0) {
                resultBean.setSuccess(true);
                if (jsonObject.has("message") && isShow) {
//                    ToastUtil.show(jsonObject.getString("message"));
                }
                if (jsonObject.has("data")) {
                    if (jsonObject.getString("data").equals("null"))
                        resultBean.setResult("");
                    else
                        resultBean.setResult(jsonObject.getString("data"));
                }
                if (jsonObject.has("message")) {
                    if (jsonObject.getString("message").equals("null"))
                        resultBean.setMessage("");
                    else
                        resultBean.setMessage(jsonObject.getString("message"));
                }
            } else {
                if (jsonObject.has("message")) {
                    if (jsonObject.getString("message").equals("null"))
                        resultBean.setMessage("");
                    else
                        resultBean.setMessage(jsonObject.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultBean;
    }


}
