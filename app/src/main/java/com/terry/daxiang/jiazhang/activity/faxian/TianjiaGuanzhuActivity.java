package com.terry.daxiang.jiazhang.activity.faxian;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.adapter.GuanzhuAdapter;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TianjiaGuanzhuActivity extends BaseTestActivity implements GuanzhuAdapter.OnRecyclerViewItemClickListener{

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
    @BindView(R.id.rv_guanzhu)
    MyRecyclerView rvGuanzhu;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_ALLUP_DATA = 0x002;
    private final int RESULT_ALLDOWN_DATA = 0x003;
    private final int RESULT_ALL_GUANGZHU = 0x004;

    private GuanzhuAdapter adapter;
    private int pageSize = 1;
    private boolean hasMoreData = true;
    private boolean isLoading = false;

    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tianjia_guanzhu);
        ButterKnife.bind(this);
        showView(tvClose);
        setTextview(tvTitle,"发现 - 添加关注");
        initViewRefresh();
        loadNetData(pageSize+"" , "" , RESULT_ALL_DATA);
    }

    private void initViewRefresh(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvGuanzhu.setLayoutManager(layoutManager);
        //下拉刷新
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (adapter != null){
                        loadNetData("1" ,adapter.getAllData().get(0).getId()+"" ,RESULT_ALLUP_DATA);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        rvGuanzhu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                try {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()+1;
                    if (lastVisibleItemPosition == adapter.getItemCount()) {
                        boolean isRefreshing = refreshlayout.isRefreshing();
                        if (!isRefreshing){
                            if (!isLoading){
                                isLoading = true;
                                //上拉加载更多
                                if (hasMoreData){
                                    pageSize++;
                                    ToastUtil.showLong("加载更多数据", Gravity.BOTTOM);
                                    loadNetData( pageSize+"", "" , RESULT_ALLDOWN_DATA);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadNetData(String page , String maxid , int type){
        RequestService.doGet(Urls.getUNSCList(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID), page ,maxid) , type , this);
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        hideProgress();
        isLoading = false;
        refreshlayout.setRefreshing(false);
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }
                        adapter = new GuanzhuAdapter(activity , listBean.getData());
                        adapter.setOnItemClickListener(this);
                        rvGuanzhu.setAdapter(adapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_ALLUP_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }

                        if (adapter == null){
                            adapter = new GuanzhuAdapter(activity , listBean.getData());
                            rvGuanzhu.setAdapter(adapter);
                            adapter.setOnItemClickListener(this);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_ALLDOWN_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }

                        if (listBean.getData() == null || listBean.getData().size() <=0){
                            ToastUtil.show("没有更多数据" , Gravity.BOTTOM);
                            hasMoreData = false;
                            return;
                        }

                        if (adapter == null){
                            adapter = new GuanzhuAdapter(activity , listBean.getData());
                            rvGuanzhu.setAdapter(adapter);
                            adapter.setOnItemClickListener(this);
                        }else {
                            adapter.setDataDown(listBean.getData());
                        }
                    }else {
                        ToastUtil.show("没有更多数据" , Gravity.BOTTOM);
                        hasMoreData = false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_ALL_GUANGZHU:
                try {
                    try{
                        if (resultBean.isSuccess()){
                            loadNetData("1" , "" , RESULT_ALL_DATA);
                        }
                        ToastUtil.show(resultBean.getMessage());
                    }catch (Exception e){
                        e.printStackTrace();
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
        refreshlayout.setRefreshing(false);
        isLoading = false;

        ToastUtil.show(getString(R.string.string_load_error));
    }

    @Override
    public void onTianjiaClick(ArticleListBean.Data data) {
        try {
            showProgress();
            Map<String , String> params = new HashMap<>();
            params.put("action" , "doFavorite");
            params.put("uid" , SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID));
            params.put("cid" , data.getId()+"");
            params.put("title" , data.getTitle());
            params.put("type","2");
            RequestService.doFormPost("" , params , RESULT_ALL_GUANGZHU , this);

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
    /***********加载圈圈**************/
}
