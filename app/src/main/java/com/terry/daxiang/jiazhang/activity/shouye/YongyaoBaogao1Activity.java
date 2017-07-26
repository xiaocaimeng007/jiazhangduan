package com.terry.daxiang.jiazhang.activity.shouye;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseActivity;
import com.terry.daxiang.jiazhang.utils.DateUtils;
import com.terry.daxiang.jiazhang.utils.DensityUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YongyaoBaogao1Activity extends BaseActivity{

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
    @BindView(R.id.txt_start)
    TextView txt_start;
    @BindView(R.id.txt_end)
    TextView txt_end;
    @BindView(R.id.start)
    TextView t_start;
    @BindView(R.id.end)
    TextView t_end;
    @BindView(R.id.ll_kaishi)
    LinearLayout llKaishi;
    @BindView(R.id.ll_jieshu)
    LinearLayout llJieshu;
    @BindView(R.id.bt_baogao)
    TextView btBaogao;
    @BindView(R.id.calendar_view)
    CalendarPickerView calendar_view;

    private String start_date = "";
    private String end_date = "";

    private boolean dateType = false;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yongyao_baogao1);
        ButterKnife.bind(this);
        showView(tvClose);
        setTextview(tvTitle, "用药报告");
        //获取屏幕宽高
        int[] display = DensityUtils.getDefaultDisplay(this);
        width = display[0];
//        dates = DateUtils.getDate(System.currentTimeMillis());
        //添加view测量监听

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendar_view.init(DateUtils.getDateByTime("2000-01-01"), nextYear.getTime())
                .withSelectedDate(today);

        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String dd = DateUtils.formateDate(date);
                if (dd != null){
                    if (dateType){
                        txt_end.setText(dd);
                        end_date = dd;
                    }else {
                        txt_start.setText(dd);
                        start_date = dd;
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        dateType = false;
        initBackgroudColor(t_start);
        String str_today = DateUtils.formateDate(today);
        txt_start.setText(str_today);
        txt_end.setText(str_today);
        start_date = str_today;
        end_date = str_today;

    }

    @OnClick({R.id.ll_back, R.id.ll_kaishi, R.id.ll_jieshu, R.id.bt_baogao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_kaishi:
                dateType = false;
                initBackgroudColor(t_start);
                break;
            case R.id.ll_jieshu:
                dateType = true;
                initBackgroudColor(t_end);
                break;
            case R.id.bt_baogao:
                if (check_date()){
                    Bundle bundle = new Bundle();
                    bundle.putString("start_date", start_date);
                    bundle.putString("end_date", end_date);
                    startActivity(YongyaoBaogao2Activity.class ,bundle);
                }
                break;
        }
    }

    private  boolean check_date(){
        if (DateUtils.compare_date(start_date , end_date) >0){
            ToastUtil.show("请正确选择日期!");
            return false;
        }

        if (DateUtils.daysBetween(start_date , end_date) >31){
            ToastUtil.show("不可以超过30天周期,请重新选择日期!");
            return false;
        }
        return true;
    }

    private void initBackgroudColor(TextView view){
        t_start.setTextColor(getResources().getColor(R.color.black));
        t_end.setTextColor(getResources().getColor(R.color.black));

        view.setTextColor(getResources().getColor(R.color.lanse));
    }

}