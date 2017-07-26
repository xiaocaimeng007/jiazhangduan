package com.terry.daxiang.jiazhang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.terry.daxiang.jiazhang.utils.ShareUtil;

/**
 * Created by fulei on 17/2/22.
 */

public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

        // 将该app注册到微信
        msgApi.registerApp(ShareUtil.IW_APP_PLAY_ID);
    }
}
