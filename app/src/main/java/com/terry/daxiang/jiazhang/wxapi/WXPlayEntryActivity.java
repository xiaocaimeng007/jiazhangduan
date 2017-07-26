package com.terry.daxiang.jiazhang.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.terry.daxiang.jiazhang.activity.XiangqingActivity;
import com.terry.daxiang.jiazhang.utils.ShareUtil;


/**
 * 微信返回
 *
 * @author chen_fulei
 */
public class WXPlayEntryActivity extends Activity implements IWXAPIEventHandler
{

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this , ShareUtil.IW_APP_PLAY_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent , this);
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        boolean isBack = false;
        switch (baseResp.errCode)
        {

            case BaseResp.ErrCode.ERR_OK:
                isBack = true;
                sendBroadcast(new Intent(XiangqingActivity.ACTION_DAXIANG_SUCCESS));
                Toast.makeText(this, "感谢您的打赏,谢谢!", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
                break;
        }

        if (!isBack && baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            isBack = true;
            sendBroadcast(new Intent(XiangqingActivity.ACTION_DAXIANG_SUCCESS));
            Toast.makeText(this, "感谢您的打赏,谢谢!", Toast.LENGTH_LONG).show();
        }

        this.finish();
    }

}
