package com.terry.daxiang.jiazhang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fulei on 16/12/17.
 */

public class FaxianListActivity extends BaseTestActivity implements BaseQuickAdapter.OnRecyclerViewItemClickListener{

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_banjiquan)
    RecyclerView rvBanjiquan;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    @BindView(R.id.indeterminate_horizontal_progress)
    ProgressBar indeterminate_horizontal_progress;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_REFRESH_UP_DATA = 0x002;
    private final int RESULT_REFRESH_DOWN_DATA = 0x003;

    private final static int RESULT_REQUEST_CODE = 0x0011;

    private faxianAdapter adapter;
    private String _aid = "";
    private String title = "";
    private int pageSize = 1;
    private boolean hasMoreData = true;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articlelist);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        title = bundle.getString("title");
        _aid = bundle.getString("content_aid" , "");
        setTextview(tvTitle, title);
        initAllView();
        hasMoreData = true;
        indeterminate_horizontal_progress.setVisibility(View.VISIBLE);
        loadNetRefreshData("" , "1" , RESULT_ALL_DATA);
    }

    private void initAllView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvBanjiquan.setLayoutManager(layoutManager);
        adapter = new faxianAdapter(R.layout.item_faxian_lk , null ,this);
        rvBanjiquan.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(this);

        //下拉刷新
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (adapter != null){
                        loadNetRefreshData(adapter.getData().get(0).getId()+"" ,"1" ,RESULT_REFRESH_UP_DATA);
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
                                    ToastUtil.showLong("加载更多数据", Gravity.BOTTOM);
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
    }

    private void loadNetRefreshData(String maxid ,String page , int fag){
        RequestService.doGet(Urls.getArticlelistsRefresh(_aid , SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID) , page , maxid), fag , this);
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

                        adapter.setNewData(listBean.getData());
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
                        adapter.getData().addAll(0 , listBean.getData());
                        adapter.notifyDataSetChanged();
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
                        adapter.addData(listBean.getData());
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
    public void onItemClick(View view, int position) {

        try {
            ArticleListBean.Data data = adapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("title", data.getTitle());
            bundle.putString("content_cid" ,data.getId()+"");
            bundle.putString("_context" ,data.getZhaiyao());
            startActivityForResult(FaxianListShowActivity.class, bundle , RESULT_REQUEST_CODE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_REQUEST_CODE && resultCode == RESULT_OK){
            loadNetRefreshData("" , "1" , RESULT_ALL_DATA);
        }

    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    class faxianAdapter extends BaseQuickAdapter<ArticleListBean.Data> {
        Context context;
        public faxianAdapter(int layoutResId, List<ArticleListBean.Data> data , Context context) {
            super(layoutResId, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, final ArticleListBean.Data item) {
            helper.itemView.setTag(item);
            final ImageView iv_icon= helper.getView(R.id.iv_album);
            iv_icon.setImageResource(R.mipmap.icon_shipin_click);
            Glide.with(context).load(Urls.URL_AVATAR_HOST+item.getImg_url()).asBitmap().error(R.mipmap.icon_defualt_head).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    iv_icon.setBackgroundDrawable(new BitmapDrawable(resource));
                }
            });

            helper.setText(R.id.tv_title , item.getTitle())
                    .setText(R.id.tv_content, item.getZhaiyao());

        }
    }

}
