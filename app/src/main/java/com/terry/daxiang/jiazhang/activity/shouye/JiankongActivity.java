package com.terry.daxiang.jiazhang.activity.shouye;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.activity.BrowserActivity;
import com.terry.daxiang.jiazhang.bean.CameraBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;
import com.terry.daxiang.jiazhang.view.MyViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JiankongActivity extends BaseTestActivity {

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
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rg_jiankong)
    RadioGroup rgJiankong;
    @BindView(R.id.viewpager)
    MyViewPager viewpager;
    @BindView(R.id.rv_jiankong_one)
    MyRecyclerView rv_jiankong_one;
    @BindView(R.id.rv_jiankong_two)
    MyRecyclerView rv_jiankong_two;
    @BindView(R.id.rv_jiankong_three)
    MyRecyclerView rv_jiankong_three;

    private final int RESULT_ALL_DATA = 0x001;

    private ArrayList<CameraBean.Data> data_jk1 = new ArrayList<>();
    private ArrayList<CameraBean.Data> data_jk2 = new ArrayList<>();
    private ArrayList<CameraBean.Data> data_jk3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiankong);
        ButterKnife.bind(this);
        showView(llSend);
        setTextview(tvTitle, "视频监控");
        setTextview(tvSend, "申请");
        initShowView();
        loadNetData();
    }

    private void loadNetData(){
        RequestService.doGet(Urls.getCameraJK(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID)) , RESULT_ALL_DATA , this);
    }

    private void initShowView(){
        rv_jiankong_one.setLayoutManager(new LinearLayoutManager(this));
        rv_jiankong_two.setLayoutManager(new LinearLayoutManager(this));
        rv_jiankong_three.setLayoutManager(new LinearLayoutManager(this));

        viewpager.setOnViewChangeListener(new MyViewPager.OnViewChangeListener() {
            @Override
            public void OnViewChange(int view) {
                switch (view){
                    case 0:
                        rb1.setChecked(true);
                        break;

                    case 1:
                        rb2.setChecked(true);
                        break;

                    case 2:
                        rb3.setChecked(true);
                        break;
                }
            }
        });

        rgJiankong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_1:
                        viewpager.scrollToScreen(0);
                        break;

                    case R.id.rb_2:
                        viewpager.scrollToScreen(1);
                        break;
                    case R.id.rb_3:
                        viewpager.scrollToScreen(2);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.ll_back, R.id.ll_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_send:
                startActivity(JiankongShenqingActivity.class);
                break;
        }
    }

    class Adapter extends BaseQuickAdapter<CameraBean.Data> {
        private boolean isShow = false;

        public Adapter(int layoutResId, List<CameraBean.Data> data , boolean isShow) {
            super(layoutResId, data);
            this.isShow = isShow;
        }

        @Override
        protected void convert(BaseViewHolder helper, final CameraBean.Data item) {
            helper.setVisible(R.id.bt_chakan , isShow);
            helper.setText(R.id.tv_weizhi, item.getTitle());
            if (!TextUtils.isEmpty(item.getStatrt_date()) && !"null".equals(item.getStatrt_date())){
                helper.setText(R.id.tv_time, item.getStatrt_date()+" ~ "+item.getEnd_date());
            }else {
                helper.setText(R.id.tv_time,"");
            }
            helper.setOnClickListener(R.id.bt_chakan, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", item.getTitle());
                    bundle.putString("loadUrl" ,"http://dx.sitemn.com/cons/camera.aspx?cameraid="+item.getId());
                    startActivity(BrowserActivity.class , bundle);
                }
            });
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()){
                        CameraBean bean = JsonParser.get(new JSONObject(responseStr) , CameraBean.class , new CameraBean());
                        for (CameraBean.Data data : bean.getData()){
                            switch (data.getStatus()){
                                case 0:
                                    data_jk1.add(data);
                                    break;
                                case 1:
                                    data_jk2.add(data);
                                    break;

                                case 2:
                                    data_jk3.add(data);
                                    break;
                            }
                        }

                        rv_jiankong_one.setAdapter(new Adapter(R.layout.item_jiankong, data_jk1 , true));
                        rv_jiankong_two.setAdapter(new Adapter(R.layout.item_jiankong, data_jk2 , false));
                        rv_jiankong_three.setAdapter(new Adapter(R.layout.item_jiankong, data_jk3 , false));
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
