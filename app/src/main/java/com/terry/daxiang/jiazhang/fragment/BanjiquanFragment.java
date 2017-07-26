package com.terry.daxiang.jiazhang.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.HomeActivity;
import com.terry.daxiang.jiazhang.activity.XiangqingActivity;
import com.terry.daxiang.jiazhang.adapter.BanjiquanAdapter;
import com.terry.daxiang.jiazhang.adapter.BanjiquanAdapter.OnRecyclerViewItemClickListener;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/9/15.
 */
public class BanjiquanFragment extends BaseFragment implements OnRecyclerViewItemClickListener {
    @BindView(R.id.rv_banjiquan)
    RecyclerView rvBanjiquan;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    @BindView(R.id.indeterminate_horizontal_progress)
    ProgressBar indeterminate_horizontal_progress;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_REFRESH_UP_DATA = 0x002;
    private final int RESULT_REFRESH_DOWN_DATA = 0x003;
    public final static String ACTION_BANJIQUAN_REFRESH = "action.com.banjiquan.refresh";

    private HomeActivity activity;
    private String _type = "62";
    private BanjiquanAdapter adapter;

    private int pageSize = 1;
    private boolean hasMoreData = true;
    private boolean isLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_BANJIQUAN_REFRESH);
            getActivity().registerReceiver(broadcastReceiver , filter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_banjiquan, null);
    }

    @Override
    public void init() {
        activity = (HomeActivity) getActivity();
        _type = activity.banjiquan.getTypeId();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvBanjiquan.setLayoutManager(layoutManager);
        adapter = new BanjiquanAdapter(getActivity() ,activity.banjiquan.getFenlei());
        rvBanjiquan.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        //下拉刷新
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (adapter != null){
                        loadNetRefreshData(adapter.getAllData().get(0).getId()+"" ,"1" ,RESULT_REFRESH_UP_DATA);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        rvBanjiquan.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                    ToastUtil.showLong("加载更多数据",Gravity.BOTTOM);
                                    loadNetRefreshData("" , pageSize+"" ,RESULT_REFRESH_DOWN_DATA);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        loadNetRefreshData("" , "1" , RESULT_ALL_DATA);
    }

    public void setDataChanged() {
        hasMoreData = true;
        _type = activity.banjiquan.getTypeId();
        indeterminate_horizontal_progress.setVisibility(View.VISIBLE);
        loadNetRefreshData("" , "1" , RESULT_ALL_DATA);
    }

    private void loadNetRefreshData(String maxid ,String page , int fag){
        activity.banjiquan.setShareMaxID(getActivity() , maxid);
        RequestService.doGet(Urls.getArticlelistsRefresh(_type , SharedPrefUtils.getString(getActivity(),SharedPrefUtils.APP_USER_ID) , page , maxid), fag , this);

        Intent intent = new Intent(HomeFragment.ACTION_HOME_POINT_RED);
        intent.putExtra("index" , activity.banjiquan.getCurrent());
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onItemClick(View view, ArticleListBean.Data data) {
        Bundle bundle = new Bundle();
        bundle.putString("title", activity.banjiquan.getFenlei());
        bundle.putString("content_cid" ,data.getId()+"");
        startActivity(XiangqingActivity.class, bundle);
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        refreshlayout.setRefreshing(false);
        isLoading = false;
        indeterminate_horizontal_progress.setVisibility(View.GONE);
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA :
                try{
                    if (resultBean.isSuccess()){
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }
                        adapter.setData(listBean.getData() , activity.banjiquan.getFenlei());
                    }else {
                        adapter.setData(null , activity.banjiquan.getFenlei());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_REFRESH_UP_DATA:
                try{
                    if (resultBean.isSuccess()){
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }
                        adapter.setDataUp(listBean.getData());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case RESULT_REFRESH_DOWN_DATA:
                try{
                    if (resultBean.isSuccess()){
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }
                        adapter.setDataDown(listBean.getData());
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

        indeterminate_horizontal_progress.setVisibility(View.GONE);
        ToastUtil.show(getString(R.string.string_load_error));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        }catch (Exception e){

        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (ACTION_BANJIQUAN_REFRESH.equals(action)){
                    //刷新数据
                    loadNetRefreshData("" , "1" , RESULT_ALL_DATA);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}