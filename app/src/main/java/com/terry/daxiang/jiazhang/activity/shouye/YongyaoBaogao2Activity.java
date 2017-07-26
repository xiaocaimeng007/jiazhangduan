package com.terry.daxiang.jiazhang.activity.shouye;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.chad.library.adapter.base.BaseViewHolder;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.bean.DrugDetailBean;
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

public class YongyaoBaogao2Activity extends BaseTestActivity implements OnRecyclerViewItemClickListener{

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
    @BindView(R.id.rv_baogao)
    MyRecyclerView rvBaogao;

    @BindView(R.id.iv_title)
    ImageView iv_title;
    @BindView(R.id.txt_title_1)
    TextView txt_title_1;

    private final int RESULT_ALL_DATA = 0x001;

    private String start_date = "";
    private String end_data ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yongyao_baogao2);
        ButterKnife.bind(this);
        showView(tvClose);
        setTextview(tvTitle, "用药报告");

        iv_title.setImageResource(R.mipmap.yongyao_01);
        txt_title_1.setText("吃嘛啉香身体倍儿棒！");

        initData();
    }

    private void initData(){
        try {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            start_date = bundle.getString("start_date" , "");
            end_data = bundle.getString("end_date" , "");
        }catch (Exception e){
            e.printStackTrace();
        }

        RequestService.doGet(Urls.getDrugSearch(SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_ID) ,start_date ,end_data) , RESULT_ALL_DATA , this);

    }

    private void setDataShow(int num){
        try {
            if (num >0 && num <5){
                iv_title.setImageResource(R.mipmap.yongyao_02);
                txt_title_1.setText("身体欠佳，注意锻练哟！");
            }else if (num > 4){
                iv_title.setImageResource(R.mipmap.yongyao_03);
                txt_title_1.setText("萌玛建议您看看大夫了……");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        DrugDetailBean.Data item = (DrugDetailBean.Data)view.getTag();
        Bundle bundle = new Bundle();
        bundle.putString("drug_id", item.getId()+"");
        startActivity(YongyaoGuanliActivity.class , bundle);
    }

    class Adapter extends BaseQuickAdapter<DrugDetailBean.Data> {

        public Adapter(int layoutResId, List<DrugDetailBean.Data> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DrugDetailBean.Data item) {
            helper.itemView.setTag(item);
            helper.setText(R.id.tv_time, item.getDrugs_date())
                    .setText(R.id.tv_yao_time , item.getDrugs_time())
                    .setText(R.id.tv_mingcheng, item.getTitle())
                    .setText(R.id.tv_shuliang, item.getDrugs_quantum())
                    .setText(R.id.tv_songyaoren, item.getDrugs_reason());
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        Log.e("--" , "==> "+ responseStr);
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA :
                try {
                    if (resultBean.isSuccess()){
                        DrugDetailBean drugDetailBean = JsonParser.get(new JSONObject(responseStr) , DrugDetailBean.class , new DrugDetailBean());
                        if (drugDetailBean == null){
                            drugDetailBean = JsonParser.get(new JSONObject(responseStr) , DrugDetailBean.class , new DrugDetailBean());
                        }
                        rvBaogao.setLayoutManager(new LinearLayoutManager(this));
                        Adapter adapter = new Adapter(R.layout.item_yongyao, drugDetailBean.getData());
                        rvBaogao.setAdapter(adapter);
                        adapter.setOnRecyclerViewItemClickListener(this);

                        setDataShow(drugDetailBean.getData().size());

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