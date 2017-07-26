package com.terry.daxiang.jiazhang.custom;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by fulei on 16/11/8.
 */

public class ResponseHandler extends Handler {

    public static final int REQUEST_FAILURE = -1;
    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_ERROR = 1;
    public static final int REQUEST_CACHE = 3;

    public ResponseHandler(Looper looper){
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        MessageDataBean dataBean = (MessageDataBean) msg.obj;
        super.handleMessage(msg);
        switch (msg.what){
            case REQUEST_SUCCESS:
                if (null != dataBean.getCallback()) {
                    dataBean.getCallback().onSuccess(dataBean.getResponseStr() , dataBean.getRequestCode());
                }
                break;

            case REQUEST_ERROR:
                if (null!= dataBean.getCallback()){
                    dataBean.getCallback().onFailure(dataBean.getResponseStr() , dataBean.getRequestCode());
                }
                break;

            case REQUEST_CACHE:
                if (null != dataBean.getCallback()){
                    //.....
                }
                break;

            case REQUEST_FAILURE:
                if (null != dataBean.getCallback()){
                    dataBean.getCallback().onFailure(dataBean.getResponseStr() , dataBean.getRequestCode());
                }
                break;

        }
    }


    public static class MessageDataBean{
        private ResponseCallback callback;
        private String responseStr;
        private int requestCode;

        public ResponseCallback getCallback() {
            return callback;
        }

        public void setCallback(ResponseCallback callback) {
            this.callback = callback;
        }

        public String getResponseStr() {
            return responseStr;
        }

        public void setResponseStr(String responseStr) {
            this.responseStr = responseStr;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }
    }

}
