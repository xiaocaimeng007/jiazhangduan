package com.terry.daxiang.jiazhang.activity.shouye;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.activity.XiangqingActivity;
import com.terry.daxiang.jiazhang.adapter.ChengzhangAdapter;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChengzhangRizhiActivity extends BaseTestActivity implements ChengzhangAdapter.OnRecyclerViewItemClickListener{

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
    @BindView(R.id.rv_chengzhang)
    MyRecyclerView rvChengzhang;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    @BindView(R.id.indeterminate_horizontal_progress)
    ProgressBar indeterminate_horizontal_progress;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_REFRESH_UP_DATA = 0x002;
    private final int RESULT_REFRESH_DOWN_DATA = 0x003;
    private final int RESULT_ALL_COMMENT = 0x004;

    private ChengzhangAdapter adapter;
    private int pageSize = 1;
    private boolean hasMoreData = true;
    private boolean isLoading = false;

    private PopupWindow pw3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chengzhang_rizhi);
        ButterKnife.bind(this);
        setTextview(tvTitle, "成长日志");
        //获取数据
        initAllView();
        loadNetData();
    }

    private void initAllView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvChengzhang.setLayoutManager(layoutManager);
        adapter = new ChengzhangAdapter(this);
        rvChengzhang.setAdapter(adapter);
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
        rvChengzhang.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadNetData(){
        hasMoreData = true;
        indeterminate_horizontal_progress.setVisibility(View.VISIBLE);
        RequestService.doGet(Urls.getChildCZRZ(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID) ,"1" , ""), RESULT_ALL_DATA , this);
    }

    private void loadNetRefreshData(String maxid ,String page , int fag){
        RequestService.doGet(Urls.getChildCZRZ(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID) , page , maxid), fag , this);
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
    public void onItemClick(View view, ArticleListBean.Data data) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("title", data.getFields().getAuthor()+"的成长日志");
            bundle.putString("content_cid" ,data.getId()+"");
            bundle.putBoolean("isdashang" , true);
            startActivity(XiangqingActivity.class, bundle);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onWechat(ArticleListBean.Data data) {
        showPopup2(data.getId()+"" , "");
    }

    private void showPopup2(final String cid , final String commentId) {
        View view = View.inflate(this, R.layout.pinglun, null);
        view.findViewById(R.id.ll_pinglun).setVisibility(View.VISIBLE);
        final EditText et_pinglun = (EditText) view.findViewById(R.id.et_pinglun);
        TextView bt_fasong = (TextView) view.findViewById(R.id.bt_fasong);
        bt_fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_pinglun.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)){
                    pw3.dismiss();
                    sentComment(cid , commentId ,comment);
                }
            }
        });

        pw3 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw3.setBackgroundDrawable(new ColorDrawable());
        pw3.setOutsideTouchable(false);
        pw3.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pw3.showAtLocation(llBack, Gravity.BOTTOM, 0, 0);
    }

    private void sentComment(String cid,final String commentId , String content){
        Map<String ,String> params = new HashMap<String, String>();
        params.put("action" ,"doNewsComment");
        params.put("Id" , cid);
        params.put("commentid" , commentId);
        params.put("userid" , SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID));
        params.put("username" ,SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_NAME));
        params.put("content" , content);

        RequestService.doFormPost("" , params , RESULT_ALL_COMMENT,this);
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
                        adapter.setData(listBean.getData());
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

            case RESULT_ALL_COMMENT:
                try{
                    ToastUtil.show(resultBean.getMessage());
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
}