package com.terry.daxiang.jiazhang.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.DateUtils;
import com.terry.daxiang.jiazhang.utils.DensityUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.HCChooseWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseTestActivity {

    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_bbname)
    EditText etBbname;
    @BindView(R.id.et_jianame)
    EditText etJianame;
    @BindView(R.id.et_birthdate)
    EditText et_birthdate;
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.et_yanzhengma)
    EditText etYanzhengma;
    @BindView(R.id.bt_get_yanzheng)
    TextView btGetYanzheng;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_tongyi)
    CheckBox cbTongyi;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.bt_login)
    TextView btLogin;
    @BindView(R.id.et_guanxi)
    EditText et_guanxi;
    @BindView(R.id.et_sex)
    EditText et_sex;

    @BindView(R.id.rl_brith)
    RelativeLayout rl_brith;
    @BindView(R.id.rl_guanxi)
    RelativeLayout rl_guanxi;
    @BindView(R.id.rl_sex)
    RelativeLayout rl_sex;


    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_YANZHENG = 0x002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setTextview(tvTitle,"我要注册");
        cbTongyi.setChecked(false);
        btRegister.setEnabled(false);

        cbTongyi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    btRegister.setEnabled(true);
                }else {
                    btRegister.setEnabled(false);
                }
            }
        });

        cbTongyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbTongyi.setChecked(true);
                btRegister.setEnabled(true);
                Bundle bundle1 = new Bundle();
                bundle1.putString("loadUrl", "http://dx.sitemn.com/cons/regagree.aspx?type=2");
                bundle1.putString("title", "注册协议");
                startActivity(BrowserActivity.class, bundle1);
            }
        });

        initData();
    }

    @OnClick({R.id.ll_back, R.id.bt_get_yanzheng, R.id.bt_register, R.id.bt_login , R.id.rl_brith , R.id.rl_guanxi ,R.id.et_birthdate ,R.id.et_guanxi , R.id.rl_sex})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_get_yanzheng:
                try {
                    String phone =etPhoneNum.getText().toString().trim();
                    if (!TextUtils.isEmpty(phone)){
                        RequestService.doGet(Urls.sendgetRegSMG(phone) , RESULT_YANZHENG , this);
                        initTimer();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.et_guanxi:
            case R.id.rl_guanxi:
                if (positionWindown != null){
                    positionWindown.setCurrentItem(0);
                    positionWindown.show();
                }
                break;
            case R.id.et_birthdate:
            case R.id.rl_brith:
                showPopupWindown(et_birthdate.getText().toString());
                break;
            case R.id.et_sex:
            case R.id.rl_sex:
                if (sexWindown != null){
                    sexWindown.setCurrentItem(0);
                    sexWindown.show();
                }
                break;
            case R.id.bt_register:
                register();
                break;
            case R.id.bt_login:
                finish();
                break;
        }
    }

    private void register(){
        String bbname = etBbname.getText().toString().trim();
        String brith = et_birthdate.getText().toString().trim();
        String jianame = etJianame.getText().toString().trim();
        String guanxi =et_guanxi.getText().toString().trim();
        String phoneNum = etPhoneNum.getText().toString().trim();
        String yanzhengma = etYanzhengma.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(bbname)){
            ToastUtil.show("请输入宝宝姓名");
            return;
        }

        if (TextUtils.isEmpty(brith)){
            ToastUtil.show("请选择宝宝生日");
            return;
        }

        if (TextUtils.isEmpty(jianame)){
            ToastUtil.show("请输入您的全名");
            return;
        }
        if (TextUtils.isEmpty(guanxi)){
            ToastUtil.show("请选择与宝宝的关系");
            return;
        }

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

        Map<String , String> params = new HashMap<>();
        params.put("action" , "doReg");
        params.put("childname" , bbname);
        params.put("familyname" , jianame);
        params.put("gx" , guanxi);
        params.put("mobile",phoneNum);
        params.put("password",password);
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
                    ToastUtil.show(resultBean.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_YANZHENG:
                try {
                    if (!resultBean.isSuccess()){
                        destroyTime();
                        btGetYanzheng.setText("获取验证码");
                        ToastUtil.show(resultBean.getResult());
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

    private void initData(){
        PositionContent p1 = new PositionContent();
        p1.setKey("0");
        p1.setValue("爸爸");
        positionContents.add(p1);

        PositionContent p2 = new PositionContent();
        p2.setKey("1");
        p2.setValue("妈妈");
        positionContents.add(p2);

        initPosition();

        PositionContent s1 = new PositionContent();
        s1.setKey("0");
        s1.setValue("男");
        sexContents.add(s1);

        PositionContent s2 = new PositionContent();
        s2.setKey("1");
        s2.setValue("女");
        sexContents.add(s2);
        initsexPopupWindown();

        Date today = new Date();
        _date_tmp = DateUtils.formateDate(today);
//        et_birthdate.setText(_date_tmp);
    }

    /******选择器**********/
    private HCChooseWindow positionWindown ;
    private List<HCChooseWindow.DataResources> positionContents = new ArrayList<>();
    private void initPosition(){

        positionWindown = new HCChooseWindow(this, llBack, positionContents, "请选择关系", new HCChooseWindow.WheelCallBack() {
            @Override
            public void choose(int index) {
                et_guanxi.setText(positionContents.get(index).getValue());
            }
        });
    }

    private HCChooseWindow sexWindown ;
    private List<HCChooseWindow.DataResources> sexContents = new ArrayList<>();
    private void initsexPopupWindown(){

        sexWindown = new HCChooseWindow(this, llBack, sexContents, "请选择性别", new HCChooseWindow.WheelCallBack() {
            @Override
            public void choose(int index) {
                et_sex.setText(sexContents.get(index).getValue());
            }
        });
    }

    private class PositionContent implements HCChooseWindow.DataResources{
        private String value;
        private String key;

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    /*******************/
    private PopupWindow chooseDate ;
    private TextView btn_title;
    private String _date_tmp = "";
    private void initPopupWindown(){
        View  view = LayoutInflater.from(this).inflate(R.layout.popup_qingjiatiao , null);
        TextView btn_cancel = ButterKnife.findById(view , R.id.btn_cancel);
        TextView btn_sure = ButterKnife.findById(view , R.id.btn_sure);
        btn_title = ButterKnife.findById(view , R.id.btn_title);
        CalendarPickerView calendar_view = ButterKnife.findById(view , R.id.calendar_view);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendar_view.init(DateUtils.getDateByTime("2000-01-01"), nextYear.getTime())
                .withSelectedDate(today);

        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                try {
                    _date_tmp = DateUtils.formateDate(date);
                    btn_title.setText(_date_tmp);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (chooseDate != null){
                        chooseDate.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    et_birthdate.setText(_date_tmp);

                    if (chooseDate != null){
                        chooseDate.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        DisplayMetrics dm = DensityUtils.getScreenDetail(this);
        chooseDate = new PopupWindow(view, dm.widthPixels, dm.heightPixels);
        chooseDate.setAnimationStyle(R.style.dialogfade);
        chooseDate.setFocusable(true);
        chooseDate.setBackgroundDrawable(new BitmapDrawable());
        chooseDate.setOutsideTouchable(true);
    }

    private void showPopupWindown(String time){
        try {
            if (chooseDate == null){
                initPopupWindown();
            }
            btn_title.setText(time);
            chooseDate.showAtLocation(llBack , Gravity.BOTTOM , 0 , 0);
        }catch (Exception e){
            e.printStackTrace();
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
