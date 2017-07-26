package com.terry.daxiang.jiazhang.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.terry.daxiang.jiazhang.bean.WxDashangBean;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * 社区分享和登录的工具
 */
public class ShareUtil
{
    private static ShareUtil shareUtil = null;

    //微信app_id
    public static final String IW_APP_ID = "wx4a00a0c95dd0ce3d";
    public static final String IW_APP_PLAY_ID = "wx954b6c174b60aa11";
    //whatsapp 包名
    public static final String APP_WHATSAPP = "com.whatsapp";
    //LINE包名
    public static final String APP_LINE = "jp.naver.line.android";

    public static ShareUtil getInstance()
    {
        if (shareUtil == null)
        {
            synchronized (ShareUtil.class)
            {
                if (shareUtil == null)
                {
                    shareUtil = new ShareUtil();
                }
            }
        }
        return shareUtil;
    }

    /**
     * 分享到LINE
     *
     * @param activity
     * @param msgText
     */
    public void shareToLine(Activity activity, String msgText, String title)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, title + "\n" + msgText);
            intent.setPackage(APP_LINE);
            activity.startActivity(intent);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 分享到whatsapp
     *
     * @param activity
     * @param msgText
     */
    public void shareToWhatsapp(Activity activity, String msgText, String title)
    {
        try
        {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msgText);
            sendIntent.putExtra(Intent.EXTRA_TITLE, title);
            sendIntent.setType("text/plain");
            sendIntent.setPackage(APP_WHATSAPP);
            activity.startActivity(sendIntent);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 在onCreate()方法中调用
     *
     * @param activity
     * @param isRegister
     * @return 返回微信接口
     */
    public IWXAPI regToWx(Activity activity, boolean isRegister)
    {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, IW_APP_ID, true);
        if (isRegister)
        {
            api.registerApp(IW_APP_ID);
        }
        return api;
    }

    /**
     * 分享链接到微信朋友列表
     *
     * @param api
     * @param text
     */
    public void SendMessageToWXSession(IWXAPI api, String text, String title, byte[] bmp)
    {
        // 初始化一个WXTextObject对象
        WXWebpageObject textObj = new WXWebpageObject();
        textObj.webpageUrl = text;
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(textObj);
        msg.mediaObject = textObj;
        msg.title = title;

        if (bmp != null && bmp.length > 0)
        {
            // 设置消息的缩略图
            msg.thumbData = bmp;
        }
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "webpage";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        api.sendReq(req);
    }

    /**
     * 判断是否安装微信
     *
     * @param api
     * @return 返回是否安装微信
     */
    public boolean isWXAppInstalled(IWXAPI api)
    {
        return api.isWXAppInstalled();
    }

    /**
     * 分享到微信朋友圈
     *
     * @param api
     * @param text
     * @param title
     */
    public void SendMessageToWXTimeline(IWXAPI api, String text, String title)
    {
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(textObj);
        msg.mediaObject = textObj;
        msg.title = title;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "text";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(req);
    }

    public void sendPayRequest(IWXAPI api , JSONObject jsonObject){
        try {
            PayReq req = new PayReq();
            req.appId = jsonObject.optString("appid" ,"");
            req.partnerId = jsonObject.optString("partnerid" ,"");
            req.prepayId = jsonObject.optString("prepayid" ,"");
            req.nonceStr = jsonObject.optString("noncestr" ,"");
            req.timeStamp = jsonObject.optString("timestamp" ,"");
            req.packageValue = jsonObject.optString("package" ,"");
            req.sign = jsonObject.optString("sign" ,"");
            api.sendReq(req);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * bitmap 转换 字节数组
     * @param bmp
     * @param needRecycle
     * @return 返回byte[]
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle)
        {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try
        {
            output.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
