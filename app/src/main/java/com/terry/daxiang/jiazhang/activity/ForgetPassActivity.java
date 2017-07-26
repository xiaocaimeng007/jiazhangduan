package com.terry.daxiang.jiazhang.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassActivity extends BaseTestActivity {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.et_yanzhengma)
    EditText etYanzhengma;
    @BindView(R.id.bt_getYanzheng)
    TextView btGetYanzheng;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repassword)
    EditText etRepassword;
    @BindView(R.id.bt_change)
    TextView btChange;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_YANZHENG = 0x002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        ButterKnife.bind(this);
        setTextview(tvTitle,"忘记密码");
    }

    @OnClick({R.id.ll_back, R.id.bt_getYanzheng, R.id.bt_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_getYanzheng:
                try {
                    String phone =etPhoneNum.getText().toString().trim();
                    if (!TextUtils.isEmpty(phone)){
                        RequestService.doGet(Urls.sendgetPasswordSMG(phone) , RESULT_YANZHENG , this);
                        initTimer();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.bt_change:
                sendChange();
                break;
        }
    }

    private void sendChange(){
        String phoneNum = etPhoneNum.getText().toString().trim();
        String yanzhengma = etYanzhengma.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String repassword = etRepassword.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNum)){
            ToastUtil.show("请输入正确手机号码");
            return;
        }
        if (TextUtils.isEmpty(yanzhengma)){
            ToastUtil.show("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)){
            ToastUtil.show("请输入密码");
            return;
        }
        if (!password.equals(repassword)){
            ToastUtil.show("输入密码不一致");
            return;
        }

        Map<String , String> params = new HashMap<>();
        params.put("action" , "getPassword");
        params.put("mobile",phoneNum);
        params.put("newpassword",password);
        params.put("getPassCode" , yanzhengma);
        RequestService.doFormPost("" , params , RESULT_ALL_DATA , this);
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes) {
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()){
                        finish();
                    }
                    ToastUtil.show(resultBean.getResult());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_YANZHENG:
                try {
                    if (!resultBean.isSuccess()){
                        destroyTime();
                        btGetYanzheng.setText("获取验证码");
                        ToastUtil.show(resultBean.getMessage());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        ToastUtil.show(getString(R.string.string_network_error));
        if (requestCode == RESULT_YANZHENG){
            destroyTime();
            btGetYanzheng.setText("获取验证码");
        }
    }

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private int timeNum = 60;

    private void initTimer(){
        btGetYanzheng.setEnabled(false);
        timeNum = 60;
        btGetYanzheng.setText(timeNum+"s");
        destroyTime();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHander.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTimerTask , 1000 , 1000);

    }

    private Handler mHander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            timeNum--;
            if (timeNum <=0){
                destroyTime();
                btGetYanzheng.setText("重新获取验证码");
            }else {
                btGetYanzheng.setText(timeNum+"s");
            }
        }
    };

    private void destroyTime(){
        btGetYanzheng.setEnabled(true);
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyTime();
    }
}
