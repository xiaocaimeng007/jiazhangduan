package com.terry.daxiang.jiazhang.activity.faxian;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.activity.FaxianListShowActivity;
import com.terry.daxiang.jiazhang.adapter.HotGridAdapter;
import com.terry.daxiang.jiazhang.adapter.ZhuanjiAdapter;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.FindBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;
import com.terry.daxiang.jiazhang.view.NoScrollGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErtongYueduActivity extends BaseTestActivity implements ZhuanjiAdapter.OnRecyclerViewItemClickListener, TabLayout.OnTabSelectedListener {

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
    @BindView(R.id.rl_gengduo)
    RelativeLayout rlGengduo;
    @BindView(R.id.ll_tuijian)
    LinearLayout llTuijian;
    @BindView(R.id.rv_zhuanji)
    MyRecyclerView rvZhuanji;
    @BindView(R.id.ll_zhuanji)
    LinearLayout llZhuanji;
    @BindView(R.id.rv_tuijian)
    NoScrollGridView rvTuijian;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;

    private FindBean findBean = null;
    private ZhuanjiAdapter zhuanjiAdapter;

    private final int RESULT_FIND_DATA = 0x001;
    private final int RESULT_HOT_DATA = 0x003;
    private final int RESULT_FIND_DETAIL = 0x004;
    private final static int RESULT_REQUEST_CODE = 0x0011;
    private int page = 0;

    private String _aid = "";
    private ArticleListBean hotListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ertong_yuedu);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        String title = bundle.getString("title");
        _aid = bundle.getString("content_aid" , "");
        setTextview(tvTitle, title);
        showView(tvClose);
        initData();
    }

    private void initData() {

        RequestService.doGet(Urls.getFindChild(_aid) , RESULT_FIND_DATA , this);
        RequestService.doGet(Urls.geHotList("1" ,"") , RESULT_HOT_DATA , this);

        rvTuijian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ArrayList<ArticleListBean.Data> data = hotListBean.getData();
                    ArticleListBean.Data data1 = data.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", data1.getTitle());
                    bundle.putString("content_cid" , data1.getId()+"");
                    bundle.putString("_context" , data1.getZhaiyao());
                    startActivityForResult(FaxianListShowActivity.class, bundle , RESULT_REQUEST_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //监听recyclerView的高度变化 然后获取高度  固定item的高度
//        rvTuijian.getViewTreeObserver().addOnPreDrawListener(this);
        rvZhuanji.setLayoutManager(new LinearLayoutManager(this));
        zhuanjiAdapter = new ZhuanjiAdapter(this);
        rvZhuanji.setAdapter(zhuanjiAdapter);
        zhuanjiAdapter.setOnItemClickListener(this);

        tabLayout.setOnTabSelectedListener(this);
        swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.holo_red_light));
    }

    @OnClick({R.id.ll_back, R.id.rl_gengduo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_gengduo:
                startActivity(HotActivity.class);
                break;
        }
    }

    @Override
    public void onItemClick(View view, ArticleListBean.Data data) {
        try {
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
    public void onSuccess(String responseStr, int requestCodes) {
        swipe_refresh.setRefreshing(false);
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_FIND_DATA:
                try {
                    findBean= JsonParser.get(new JSONObject(responseStr) , FindBean.class , new FindBean());
                    if (findBean == null){
                        findBean= JsonParser.get(new JSONObject(responseStr) , FindBean.class , new FindBean());
                    }
                    ArrayList<FindBean.Data> data = findBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        tabLayout.addTab(tabLayout.newTab().setText(data.get(i).getTitle()).setTag(i));
                    }
                    loadNetRefreshData(data.get(0).getId()+"","",page+"",RESULT_FIND_DETAIL);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_HOT_DATA:
                try {
                    if (resultBean.isSuccess()){
                        hotListBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (hotListBean == null){
                            hotListBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }

                        HotGridAdapter adapter = new HotGridAdapter(this , hotListBean.getData());
                        rvTuijian.setAdapter(adapter);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case RESULT_FIND_DETAIL:
                try {
                    ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                    if (listBean == null){
                        listBean = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                    }
                    zhuanjiAdapter.setDatas(listBean.getData());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        ToastUtil.show(getString(R.string.string_load_error));
        swipe_refresh.setRefreshing(false);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = (int) tab.getTag();
        loadNetRefreshData(findBean.getData().get(position).getId()+"","",page+"",RESULT_FIND_DETAIL);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 绘本详情
     * @param aId
     * @param maxid
     * @param page
     * @param fag
     */
    private void loadNetRefreshData(String aId, String maxid , String page , int fag){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(true);
                zhuanjiAdapter.setDatas(null);
            }
        });
        RequestService.doGet(Urls.getArticlelistsRefresh(aId , SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID) , page , maxid), fag , this);
    }
}