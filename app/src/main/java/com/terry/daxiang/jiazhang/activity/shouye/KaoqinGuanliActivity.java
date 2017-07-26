package com.terry.daxiang.jiazhang.activity.shouye;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.squareup.timessquare.CalendarPickerView;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.bean.LeaveReportBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.DateUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KaoqinGuanliActivity extends BaseTestActivity{

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
    @BindView(R.id.txt_kq_title)
    TextView txt_kq_title;
    @BindView(R.id.tv_tianshu)
    TextView tvTianshu;
    @BindView(R.id.tv_shijia)
    TextView tvShijia;
    @BindView(R.id.tv_bingjia)
    TextView tvBingjia;
    @BindView(R.id.linear_1)
    LinearLayout linear_1;
    @BindView(R.id.calendar_view)
    CalendarPickerView calendar_view;
    @BindView(R.id.chart_kaoqin)
    PieChart chart_kaoqin;

    private final int RESULT_ALL_DATA = 0x001;

    private LeaveReportBean reportBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoqin_guanli);
        ButterKnife.bind(this);
        showView(llSend);
        setTextview(tvTitle, "考勤管理");
        setTextview(tvSend, "请假条");

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendar_view.init(DateUtils.getDateByTime("2000-01-01"), nextYear.getTime())
                .withSelectedDate(today);

        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String dd = DateUtils.formateDateByMoth(date);
                loadNetData(dd);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        initPieChartView();

        String str_today = DateUtils.formateDateByMoth(today);
        loadNetData(str_today);
    }

    private void loadNetData(String moth){
        RequestService.doGet(Urls.getLeaveReport(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID) ,moth ) , RESULT_ALL_DATA , this);
    }

    private void initPieChartView(){
        chart_kaoqin.setUsePercentValues(true);
        chart_kaoqin.getDescription().setEnabled(false);
        chart_kaoqin.setExtraOffsets(5, 10, 5, 5);

        chart_kaoqin.setDragDecelerationFrictionCoef(0.95f);
        chart_kaoqin.setDrawHoleEnabled(true);
        chart_kaoqin.setHoleColor(Color.WHITE);

        chart_kaoqin.setTransparentCircleColor(Color.WHITE);
        chart_kaoqin.setTransparentCircleAlpha(110);

        chart_kaoqin.setHoleRadius(58f);
        chart_kaoqin.setTransparentCircleRadius(61f);

        chart_kaoqin.setDrawCenterText(true);

        chart_kaoqin.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart_kaoqin.setRotationEnabled(true);
        chart_kaoqin.setHighlightPerTapEnabled(true);
        chart_kaoqin.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


    }

    private void showView(){
        if (reportBean != null){
            txt_kq_title.setText(reportBean.getMonth());
            tvTianshu.setText(reportBean.getChuqin());
            tvShijia.setText(reportBean.getShijia());
            tvBingjia.setText(reportBean.getBingjia());
        }

        try {

            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
            try {
                int chuqin = 0;
                if (!TextUtils.isEmpty(reportBean.getChuqin())){
                    chuqin = Integer.parseInt(reportBean.getChuqin());
                }
                entries.add(new PieEntry(chuqin, "出勤"));
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                int shijia = 0;
                if (!TextUtils.isEmpty(reportBean.getShijia())){
                    shijia = Integer.parseInt(reportBean.getShijia());
                }
                entries.add(new PieEntry(shijia, "事假"));
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                int bingjia = 0;
                if (!TextUtils.isEmpty(reportBean.getBingjia())){
                    bingjia = Integer.parseInt(reportBean.getBingjia());
                }
                entries.add(new PieEntry(bingjia, "病假"));
            }catch (Exception e){
                e.printStackTrace();
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();

            colors.add(getResources().getColor(R.color.color_19af8a));
            colors.add(getResources().getColor(R.color.color_f91a01));
            colors.add(getResources().getColor(R.color.color_fd8608));

            dataSet.setColors(colors);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(8f);
            data.setValueTextColor(Color.BLACK);
            chart_kaoqin.setData(data);

            // undo all highlights
            chart_kaoqin.highlightValues(null);

            chart_kaoqin.invalidate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_back, R.id.ll_send ,  R.id.linear_1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_send:
                startActivity(QingjiatiaoActivity.class);
                break;
            case R.id.linear_1:
                startActivity(KaoqinShiJiaActivity.class);
                break;
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()){
                        reportBean = JsonParser.get(new JSONObject(resultBean.getResult()) , LeaveReportBean.class , new LeaveReportBean());
                        showView();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        ToastUtil.show(getString(R.string.string_load_error));
    }


}