package com.terry.daxiang.jiazhang.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.terry.daxiang.jiazhang.utils.ShareUtil;


/**
 * 微信返回
 *
 * @author chen_fulei
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        api = ShareUtil.getInstance().regToWx(this, true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq)
    {
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        switch (baseResp.errCode)
        {
            case BaseResp.ErrCode.ERR_OK:
                Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
                break;
        }
        // TODO 微信分享 成功之后调用接口
        this.finish();
    }

}
