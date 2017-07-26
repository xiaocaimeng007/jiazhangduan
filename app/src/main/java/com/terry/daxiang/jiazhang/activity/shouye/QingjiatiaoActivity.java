package com.terry.daxiang.jiazhang.activity.shouye;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.utils.DateUtils;
import com.terry.daxiang.jiazhang.utils.DensityUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QingjiatiaoActivity extends BaseTestActivity implements OnCheckedChangeListener{

    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.iv_gengduo)
    ImageView ivGengduo;
    @BindView(R.id.iv_jiahao)
    ImageView ivJiahao;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rg_qingjiatiao)
    RadioGroup rgQingjiatiao;
    @BindView(R.id.ll_kaishi)
    LinearLayout llKaishi;
    @BindView(R.id.ll_jieshu)
    LinearLayout llJieshu;
    @BindView(R.id.tv_liyou)
    TextView tvLiyou;
    @BindView(R.id.et_liyou)
    EditText etLiyou;
    @BindView(R.id.bt_qingjiatiao)
    TextView btQingjiatiao;
    @BindView(R.id.txt_date_start)
    TextView txt_date_start;
    @BindView(R.id.txt_date_end)
    TextView txt_date_end;

    private final int RESULT_ALL_DATA = 0x001;

    private PopupWindow chooseDate ;
    private TextView btn_title;
    private String _date_tmp = "2016-12-20";
    private String view_date_tmp = "2016年12月20日";
    private String _date_start = "2016-12-20";
    private String _date_end = "2016-12-20";
    private boolean isStart = true;

    private String leavetype = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qingjiatiao);
        ButterKnife.bind(this);
        setTextview(tvTitle,"请假条");
        Calendar lastDate = Calendar.getInstance();
        String nowDate1 = DateUtils.formateDate(lastDate.getTime() , "yyyy年MM月dd日");
        String nowDate2 = DateUtils.formateDate(lastDate.getTime() );

        view_date_tmp = nowDate1;
        _date_tmp = nowDate2;
        _date_start = nowDate2;
        _date_end = nowDate2;

        txt_date_start.setText(nowDate1);
        txt_date_end.setText(nowDate1);

        initPopupWindown();
        rgQingjiatiao.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.ll_back, R.id.ll_kaishi, R.id.ll_jieshu, R.id.bt_qingjiatiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_kaishi:
                try {
                    isStart = true;
                    showPopupWindown(txt_date_start.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ll_jieshu:
                try {
                    isStart = false;
                    showPopupWindown(txt_date_end.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.bt_qingjiatiao:
                send();
                break;
        }
    }

    private void send(){
        String content = etLiyou.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            ToastUtil.show("请输入详情!");
            return;
        }

        long s = DateUtils.getStringToDate(_date_start);
        long e = DateUtils.getStringToDate(_date_end);
        if (s > e){
            ToastUtil.show("时间选择不正确!");
            return;
        }

        Map<String , String> params = new HashMap<>();
        params.put("action" , "doPublishLeave");
        params.put("uid" , SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_ID));
        params.put("leavecontent" , content);
        params.put("leavetype" , leavetype);
        params.put("leavestart" , _date_start);
        params.put("leaveend" , _date_end);

        showProgress();

        RequestService.doFormPost("" , params ,RESULT_ALL_DATA , this );
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_1:
                leavetype = "1";
                tvLiyou.setText("事假理由");
                break;
            case R.id.rb_2:
                leavetype = "2";
                tvLiyou.setText("病假理由");
                break;
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        hideProgress();
        ResultBean resultbean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA :
                try{
                    if (resultbean.isSuccess()){
                        finish();
                    }
                    ToastUtil.show(resultbean.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        hideProgress();
        ToastUtil.show(getString(R.string.string_load_error));
    }
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
                   view_date_tmp = DateUtils.formateDate(date , "yyyy年MM月dd日");
                   btn_title.setText(view_date_tmp);
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

                    if (isStart){
                        _date_start = _date_tmp;
                        txt_date_start.setText(view_date_tmp);
                    }else {
                        _date_end = _date_tmp;
                        txt_date_end.setText(view_date_tmp);
                    }

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
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(etLiyou.getWindowToken(), 0);
            if (chooseDate == null){
                initPopupWindown();
            }
            btn_title.setText( time);
            chooseDate.showAtLocation(llBack , Gravity.BOTTOM , 0 , 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /***********加载圈圈**************/
    private CustomProgressBar progressBar ;
    private void showProgress(){
        if (progressBar == null){
            progressBar = new CustomProgressBar(this);
        }
        progressBar.show();
    }

    private void hideProgress(){
        if (progressBar != null && progressBar.isShowing()){
            progressBar.dismiss();
        }
    }
}