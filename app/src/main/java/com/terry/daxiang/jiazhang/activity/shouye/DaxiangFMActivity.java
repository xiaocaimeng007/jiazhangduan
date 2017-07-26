package com.terry.daxiang.jiazhang.activity.shouye;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.activity.XiangqingActivity;
import com.terry.daxiang.jiazhang.adapter.DaxiangFMAdapter;
import com.terry.daxiang.jiazhang.adapter.DaxiangFMAdapter.OnRecyclerViewItemClickListener;
import com.terry.daxiang.jiazhang.adapter.DaxiangFMEntity;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaxiangFMActivity extends BaseTestActivity implements OnRecyclerViewItemClickListener {

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
    @BindView(R.id.rv_fm)
    MyRecyclerView rvFm;
    private List<DaxiangFMEntity> data;

    private final int RESULT_ALL_DATA = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daxiang_fm);
        ButterKnife.bind(this);
        setTextview(tvTitle, "大象FM");
        loadNetData();
    }

    private void loadNetData(){
        showProgress();
        RequestService.doGet(Urls.getArticlelistsRefresh("67" , SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID) , "1" , ""), RESULT_ALL_DATA , this);
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(View view, ArticleListBean.Data data) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "大象FM");
        bundle.putString("content_cid" ,data.getId()+"");
        bundle.putBoolean("isdashang" , true);
        startActivity(XiangqingActivity.class, bundle);
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        hideProgress();
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                try{
                    if (resultBean.isSuccess()){
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }

                        DaxiangFMAdapter adapter = new DaxiangFMAdapter(this , listBean.getData());
                        rvFm.setLayoutManager(new LinearLayoutManager(this));
                        rvFm.setAdapter(adapter);
                        adapter.setOnItemClickListener(this);
                    }
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