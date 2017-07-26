package com.terry.daxiang.jiazhang.custom;

import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.terry.daxiang.jiazhang.activity.MyApplication;
import com.hyphenate.easeui.utils.SharedPrefUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求管理
 *
 * Created by fulei on 16/11/8.
 */

public class RequestService {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    private static final MediaType MEDIA_TYPE_VIDEO = MediaType.parse("video/mp4");
    public RequestService(){
        //...
    }

    private static final OkHttpClient okHttpClient;
    private static final ResponseHandler mHandler = new ResponseHandler(Looper.myLooper());

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        okHttpClient =builder.build();
    }

    /**
     * post 表单参数请求 默认不缓存
     *
     * @param url
     * @param params
     * @param requestCode
     * @param responseCallback
     */
    public static void doFormPost(final String url, Map<String, String> params, final int requestCode, final ResponseCallback responseCallback) {
        doFormPost(url, params, requestCode, responseCallback, false);

    }

    /**
     * post 表单参数请求 增加请求结果是否需要缓存
     *
     * @param url
     * @param params
     * @param requestCode
     * @param responseCallback
     */
    public static void doFormPost(final String url, Map<String, String> params, final int requestCode, final ResponseCallback responseCallback, final boolean isNeedCache) {
        initCache(url, responseCallback, requestCode, isNeedCache);
        request(getFormPostRequest(url, params), url, responseCallback, requestCode, isNeedCache);

    }

    public static void doFormPostAll(final String url, Map<String, String> params, final int requestCode, final ResponseCallback responseCallback) {
        initCache(url, responseCallback, requestCode, false);
        request(getFormPostRequestAll(url, params), url, responseCallback, requestCode, false);

    }


    /**
     * get请求 默认不缓存
     *
     * @param url
     * @param requestCode
     * @param responseCallback
     */
    public static void doGet(final String url, final int requestCode, final ResponseCallback responseCallback) {
        doGet(url, requestCode, responseCallback, false);
    }

    /**
     * get请求 增加请求结果是否需要缓存
     *
     * @param url
     * @param requestCode
     * @param responseCallback
     */
    public static void doGet(final String url, final int requestCode, final ResponseCallback responseCallback, boolean isNeedCache) {
        initCache(url, responseCallback, requestCode, isNeedCache);
        request(getGetRequest(url), url, responseCallback, requestCode, isNeedCache);
    }

    public static void doGetAll(final String url, final int requestCode, final ResponseCallback responseCallback) {
        initCache(url, responseCallback, requestCode, false);
        request(getGetRequestAll(url), url, responseCallback, requestCode, false);
    }

    /**
     * 异步上传Multipart文件,上传多个文件和参数
     * @param url
     * @param params
     * @param fileParams
     * @param requestCode
     * @param responseCallback
     */
    public static void sendMultipart(final String url ,final Map<String, String> params, Map<String , String> fileParams,final int requestCode , final ResponseCallback responseCallback){

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }

        if (!fileParams.isEmpty()){
            for (String key : fileParams.keySet()) {
                try {
                    String value = fileParams.get(key);
                    Log.e("--","===>"+value);
                    if (value.endsWith(".mp4")){
                        builder.addFormDataPart(key , value.substring(value.lastIndexOf("/")) ,RequestBody.create(MEDIA_TYPE_VIDEO , new File(value)));
                    }else {
                        builder.addFormDataPart(key , value.substring(value.lastIndexOf("/")) ,RequestBody.create(MEDIA_TYPE_PNG , new File(value)));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url(url)
                .post(requestBody)
                .build();

        request(request, url, responseCallback, requestCode, false);
    }

    /**
     * 发送网络请求（get / post）
     * @param request
     * @param url
     * @param responseCallback
     * @param requestCode
     * @param isNeedCache
     */
    private static void request(Request request, final String url, final ResponseCallback responseCallback, final int requestCode, final boolean isNeedCache) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dealFailRequest(call, e, responseCallback, requestCode);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseResponse(url, response, responseCallback, requestCode, isNeedCache);
            }

        });
    }


    /**
     * 封装一个post请求
     *
     * @param url
     * @param params
     * @return
     */
    private static Request getFormPostRequest(String url, Map<String, String> params) {
        url = Urls.URL_APP_HOST + url;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                formEncodingBuilder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(formEncodingBuilder.build()).build();
        return request;
    }

    private static Request getFormPostRequestAll(String url, Map<String, String> params) {
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (!params.isEmpty()) {
            for (String key : params.keySet()) {
                formEncodingBuilder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(formEncodingBuilder.build()).build();
        return request;
    }


    /**
     * 封装一个get请求
     *
     * @param url
     * @return
     */
    private static Request getGetRequest(String url) {
        url = Urls.URL_APP_HOST + url;
        Log.e("--" , url);
        Request request = new Request.Builder().url(url).build();
        return request;
    }

    private static Request getGetRequestAll(String url) {
        Log.e("--" , url);
        Request request = new Request.Builder().url(url).build();
        return request;
    }


    /**
     * 将请求结果交给主线程处理
     * @param url
     * @param response
     * @param responseCallback
     * @param requestCode
     * @param isNeedCache
     * @throws IOException
     */
    private static void parseResponse(String url, Response response, ResponseCallback responseCallback, int requestCode, boolean isNeedCache) throws IOException{
        boolean isCache = false;
        String responseStr;
        if (response != null) {
            responseStr = response.body().string();
        } else {
            responseStr = SharedPrefUtils.getAppCache(MyApplication.getInstance().getApplicationContext(), url);
            isCache = true;
        }
        if (TextUtils.isEmpty(responseStr)) return;
        Message msg = Message.obtain();
        ResponseHandler.MessageDataBean dataBean = new ResponseHandler.MessageDataBean();
        dataBean.setCallback(responseCallback);
        dataBean.setResponseStr(responseStr);
        dataBean.setRequestCode(requestCode);
        msg.obj = dataBean;
        if (response != null && response.isSuccessful()) {
            msg.what = ResponseHandler.REQUEST_SUCCESS;
            mHandler.sendMessage(msg);
            if (isNeedCache)
                SharedPrefUtils.storeAppCache(MyApplication.getInstance().getApplicationContext(), url, responseStr);
        } else if (isCache) {
            msg.what = ResponseHandler.REQUEST_CACHE;
            mHandler.sendMessage(msg);
        } else {
            msg.what = ResponseHandler.REQUEST_ERROR;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 处理失败请求
     *
     * @param call
     */
    private static void dealFailRequest(Call call, IOException error, ResponseCallback responseCallback, int requestCode) {
        Message msg = Message.obtain();
        ResponseHandler.MessageDataBean dataBean = new ResponseHandler.MessageDataBean();
        dataBean.setCallback(responseCallback);
        dataBean.setResponseStr("");
        dataBean.setRequestCode(requestCode);
        msg.obj = dataBean;
        msg.what = ResponseHandler.REQUEST_FAILURE;
        mHandler.sendMessage(msg);
    }

    /**
     * 初始化缓存
     *
     * @param url
     * @param responseCallback
     * @param requestCode
     */
    private static void initCache(String url, ResponseCallback responseCallback, int requestCode, boolean isNeedCache) {
        try {
            parseResponse(url, null, responseCallback, requestCode, isNeedCache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
