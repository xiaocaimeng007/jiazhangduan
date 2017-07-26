package com.terry.daxiang.jiazhang.activity.faxian;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.activity.FaxianListShowActivity;
import com.terry.daxiang.jiazhang.activity.XiangqingActivity;
import com.terry.daxiang.jiazhang.adapter.HotTuijiangAdapter;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen_fulei on 2016/12/14.
 */

public class HotActivity extends BaseTestActivity implements HotTuijiangAdapter.OnRecyclerViewItemClickListener{
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rv_guanzhu)
    MyRecyclerView rvGuanzhu;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_ALLUP_DATA = 0x002;
    private final int RESULT_ALLDOWN_DATA = 0x003;

    private HotTuijiangAdapter adapter;
    private int pageSize = 1;
    private boolean hasMoreData = true;
    private boolean isLoading = false;

    private final Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faxian_hot);
        ButterKnife.bind(this);
        setTextview(tvTitle,"热门推荐");

        initViewRefresh();
        loadNetData("1" , "" , RESULT_ALL_DATA);
    }

    private void initViewRefresh(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvGuanzhu.setLayoutManager(layoutManager);
        adapter = new HotTuijiangAdapter(this);
        rvGuanzhu.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        //下拉刷新
        refreshlayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
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
        RequestService.doGet(Urls.geHotList(page ,maxid) , type , this);
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
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
                        if (adapter == null){
                            adapter = new HotTuijiangAdapter(activity);
                            rvGuanzhu.setAdapter(adapter);
                        }
                        adapter.setData(listBean.getData());

                    }else {
                        ToastUtil.show(resultBean.getMessage());
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
                            adapter = new HotTuijiangAdapter(activity);
                            rvGuanzhu.setAdapter(adapter);
                        }else {
                            adapter.setDataUp(listBean.getData());
                        }
                    }else {
                        ToastUtil.show(resultBean.getMessage());
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
                            adapter = new HotTuijiangAdapter(activity);
                            rvGuanzhu.setAdapter(adapter);
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
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        refreshlayout.setRefreshing(false);
        isLoading = false;

        ToastUtil.show(getString(R.string.string_load_error));
    }

    @Override
    public void onItemClick(View view, ArticleListBean.Data data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("title", data.getTitle());
            bundle.putString("content_cid" ,data.getId()+"");
            bundle.putString("_context" ,data.getZhaiyao());
            startActivity(FaxianListShowActivity.class, bundle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ll_back)
    public void onClick() {
        finish();
    }
}
