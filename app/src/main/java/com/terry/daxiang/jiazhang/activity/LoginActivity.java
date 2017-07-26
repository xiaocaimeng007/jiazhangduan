package com.terry.daxiang.jiazhang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.DemoDBManager;
import com.hyphenate.chatuidemo.parse.UserProfileManager;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.LoginBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.CipherUtils;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.EaseUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseTestActivity {
    public static final String TAG = "====s";
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
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_wangjimima)
    TextView tvWangjimima;
    @BindView(R.id.tv_woyaozhuce)
    TextView tvWoyaozhuce;

    private final int RESULT_LOGIN = 0x001;
    private final Activity activity = this;
    private LoginBean loginBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTextview(tvTitle, "登录");
        hideView(llBack);

        String account = SharedPrefUtils.getString(getApplicationContext(), SharedPrefUtils.APP_USER_ACCOUNT);
        if (!TextUtils.isEmpty(account)) {
            etPhoneNum.setText(account);
        }
    }

    @OnClick({R.id.bt_login, R.id.tv_wangjimima, R.id.tv_woyaozhuce})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_wangjimima:
                startActivity(ForgetPassActivity.class);
                break;
            case R.id.tv_woyaozhuce:
                startActivity(RegisterActivity.class);
                break;
        }
    }

    private void login() {
        String phone = etPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(getString(R.string.string_login_phone_error));
            return;
        }

        String _pwd = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(_pwd)) {
            ToastUtil.show(getString(R.string.string_login_psd_error));
            return;
        }
        showProgress();
        String token_key = phone + System.currentTimeMillis() + _pwd;
        String token = CipherUtils.md5(token_key);
        SharedPrefUtils.setString(this, SharedPrefUtils.APP_USER_TOKEN, token);
        SharedPrefUtils.setString(this, SharedPrefUtils.APP_USER_ACCOUNT, phone);
        RequestService.doGet(Urls.getLogin(phone, _pwd, token), RESULT_LOGIN, this);
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr, true);
        switch (requestCodes) {
            case RESULT_LOGIN:
                if (resultBean.isSuccess()) {
                    try {
                        JSONObject jsonObject = new JSONObject(resultBean.getResult());
                        loginBean = JsonParser.get(jsonObject, LoginBean.class, new LoginBean());

                        final String phoneName = etPhoneNum.getText().toString().trim();
//                        final String password = etPassword.getText().toString().trim();
                        //注册环信账户
                        EaseUtils.registered(activity, phoneName, phoneName, new EaseUtils.RegisteredCallBack() {
                            @Override
                            public void registeredSuccessFul() {//注册成功 直接登陆
                                loginEase(phoneName, phoneName);
                            }

                            @Override
                            public void alreadyExisted(String erro) {//该账户已被注册 直接登陆
                                loginEase(phoneName, phoneName);
                            }

                            @Override
                            public void error(String error) {//注册失败
                                hideProgress();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!TextUtils.isEmpty(resultBean.getMessage())) {
                        ToastUtil.show(resultBean.getMessage());
                    }
                    hideProgress();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        hideProgress();
        ToastUtil.show(getString(R.string.string_network_error));
    }

    /***********加载圈圈**************/
    private CustomProgressBar progressBar;

    private void showProgress() {
        btLogin.setEnabled(false);
        if (progressBar == null) {
            progressBar = new CustomProgressBar(this);
        }
        progressBar.show();
    }

    private void hideProgress() {
        btLogin.setEnabled(true);
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }

    /***********登陆环信账户**************/

    public void loginEase(String currentUsername, String currentPassword) {
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // call login method
        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        Log.d(TAG, "login: onSuccess");
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();


                        // get user's info (this should be get from App's server or 3rd party service)
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                        if (loginBean != null) {
                            //存储数据
                            SharedPrefUtils.setBoolean(activity, SharedPrefUtils.APP_USER_ISLOGIN, true);
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_ID, loginBean.getId());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_GROUPID, loginBean.getGroup_id());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_CHILDRENID, loginBean.getChildren_id());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_GRADEID, loginBean.getGrade_id());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_NAME, loginBean.getUser_name());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_MOBILE, loginBean.getMobile());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_AVATAR, Urls.URL_AVATAR_HOST + loginBean.getAvatar());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_NICKNAME, loginBean.getNick_name());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_SET, loginBean.getSex());
                            SharedPrefUtils.setString(activity, SharedPrefUtils.APP_USER_TELPHONE, loginBean.getTelphone());
                            SharedPrefUtils.setString(activity,SharedPrefUtils.APP_USER_GRADENAME,loginBean.getGrade_name());
                        }
                        // update current user's display name for APNs
                        boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(loginBean.getNick_name());
                        if (!updatenick) {
                            Log.e("LoginActivity", "update current user nick fail");
                        }
                        // 将自己服务器返回的环信账号、昵称和头像URL设置到帮助类中。
                        DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(loginBean.getNick_name());
                        DemoHelper.getInstance().setCurrentUserName(loginBean.getNick_name()); // 环信Id
                        // update current user's display name for APNs
                        new UserProfileManager().updateCurrentUserNickName(loginBean.getNick_name());

                        startActivity(HomeActivity.class);//进入主页
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG, "login: onError: " + code);
                runOnUiThread(new Runnable() {
                    public void run() {
                        hideProgress();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
