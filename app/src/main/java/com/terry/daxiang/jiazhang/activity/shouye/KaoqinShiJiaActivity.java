package com.terry.daxiang.jiazhang.activity.shouye;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.bean.LeaveListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KaoqinShiJiaActivity extends BaseTestActivity{

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
    @BindView(R.id.rv_shijia)
    MyRecyclerView rv_shijia;

    private final int RESULT_ALL_DATA = 0x001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoqin_shijian);
        ButterKnife.bind(this);
        setTextview(tvTitle, "请假条列表");
        rv_shijia.setLayoutManager(new LinearLayoutManager(this));
        loadNetData();
    }

    private void loadNetData(){
        RequestService.doGet(Urls.getLeaveList(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID)) , RESULT_ALL_DATA , this);
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
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
                        LeaveListBean listBean = JsonParser.get(new JSONObject(responseStr) , LeaveListBean.class , new LeaveListBean());

                        rv_shijia.setAdapter(new Adapter(R.layout.item_kaoqin_shijia , listBean.getData()));
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

    class Adapter extends BaseQuickAdapter<LeaveListBean.Data> {
        public Adapter(int layoutResId, List<LeaveListBean.Data> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final LeaveListBean.Data item) {
            helper.setText(R.id.txt_type, item.getLeave_type()+" :").setText(R.id.tv_weizhi, item.getContent());
            if (!TextUtils.isEmpty(item.getLeave_start()) && !"null".equals(item.getLeave_start())){
                helper.setText(R.id.tv_time, item.getLeave_start()+" ~ "+item.getLeave_enddate());
            }else {
                helper.setText(R.id.tv_time,"");
            }
        }
    }
}