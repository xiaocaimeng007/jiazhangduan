package com.terry.daxiang.jiazhang.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.google.gson.Gson;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.adapter.PinglunAdapter;
import com.terry.daxiang.jiazhang.json.JSON;
import com.terry.daxiang.jiazhang.json.Pinglun;
import com.terry.daxiang.jiazhang.json.Pinglun.PinglunBean;
import com.terry.daxiang.jiazhang.utils.DensityUtils;
import com.terry.daxiang.jiazhang.view.CircleImageView;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FMXiangQingActivity extends BaseActivity implements OnRecyclerViewItemClickListener {

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
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_shipin)
    TextView tvShipin;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_bofang)
    ImageView ivBofang;
    @BindView(R.id.tv_tiaoti)
    TextView tvTiaoti;
    @BindView(R.id.ll_zanliebiao)
    LinearLayout llZanliebiao;
    @BindView(R.id.tv_zan)
    TextView tvZan;
    @BindView(R.id.tv_huifu)
    TextView tvHuifu;
    @BindView(R.id.rv_pinglun)
    MyRecyclerView rvPinglun;
    @BindView(R.id.tv_juanzeng)
    TextView tvJuanzeng;
    @BindView(R.id.aixinyongchu)
    TextView aixinyongchu;
    @BindView(R.id.aixinhuodong)
    TextView aixinhuodong;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.et_pinglun)
    EditText etPinglun;
    @BindView(R.id.bt_fasong)
    TextView btFasong;
    @BindView(R.id.ll_pinglun)
    LinearLayout llPinglun;
    private int width;
    private int height;
    private PopupWindow pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm_xiangqing);
        ButterKnife.bind(this);
        showView(ivGengduo);
        setTextview(tvTitle, "大象FM");
        List<PinglunBean> pinglun = new Gson().fromJson(JSON.pinglun, Pinglun.class).getPinglun();
        PinglunAdapter adapter = new PinglunAdapter(this,R.layout.item_pinglun, pinglun);
        rvPinglun.setLayoutManager(new LinearLayoutManager(this));
        rvPinglun.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(this);
        int[] display = DensityUtils.getDefaultDisplay(this);
        width = display[0];
        height = display[1];
    }

    @OnClick({R.id.ll_back, R.id.iv_gengduo, R.id.tv_zan, R.id.tv_huifu,R.id.iv_huifu, R.id.tv_juanzeng, R.id.aixinyongchu, R.id.aixinhuodong})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_gengduo:
                break;
            case R.id.tv_zan:
                break;
            case R.id.tv_huifu:
                break;
            case R.id.iv_huifu:
                llPinglun.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_juanzeng:
                showPopup();
                break;
            case R.id.aixinyongchu:
                break;
            case R.id.aixinhuodong:
                break;
        }
    }

    private void showPopup() {
        View view = View.inflate(this, R.layout.popup_juanzeng, null);
        pw2 = new PopupWindow(view, width, height, true);
        pw2.setBackgroundDrawable(new ColorDrawable());
        pw2.showAtLocation(llHome, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onItemClick(View view, int position) {
        llPinglun.setVisibility(View.VISIBLE);
    }
}
