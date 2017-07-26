package com.terry.daxiang.jiazhang.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyphenate.chatuidemo.ui.GroupsActivity;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BrowserActivity;
import com.terry.daxiang.jiazhang.activity.HomeActivity;
import com.terry.daxiang.jiazhang.activity.TesebanActivity;
import com.terry.daxiang.jiazhang.activity.XianhutousuActivity;
import com.terry.daxiang.jiazhang.activity.shouye.ChengzhangRizhiActivity;
import com.terry.daxiang.jiazhang.activity.shouye.DaxiangFMActivity;
import com.terry.daxiang.jiazhang.activity.shouye.JiankongActivity;
import com.terry.daxiang.jiazhang.activity.shouye.KaoqinGuanliActivity;
import com.terry.daxiang.jiazhang.activity.shouye.QingjiatiaoActivity;
import com.terry.daxiang.jiazhang.activity.shouye.XingweiJiluSingleActivity;
import com.terry.daxiang.jiazhang.activity.shouye.YongyaoBaogao1Activity;
import com.terry.daxiang.jiazhang.adapter.LunboPagerAdapter;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.NewsCountBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/15.
 */
public class HomeFragment extends BaseFragment implements OnTouchListener {
    @BindView(R.id.vp_viewpager)
    ViewPager vpViewpager;
    /*@BindView(R.id.indicator)
    CircleIndicator indicator;*/
    @BindView(R.id.iv_l1)
    ImageView ivL1;
    @BindView(R.id.iv_point1)
    ImageView ivPoint1;
    @BindView(R.id.rl_yuansuo)
    RelativeLayout rlYuansuo;
    @BindView(R.id.iv_l2)
    ImageView ivL2;
    @BindView(R.id.iv_point2)
    ImageView ivPoint2;
    @BindView(R.id.rl_jingcai)
    RelativeLayout rlJingcai;
    @BindView(R.id.iv_l3)
    ImageView ivL3;
    @BindView(R.id.iv_point3)
    ImageView ivPoint3;
    @BindView(R.id.rl_jiankong)
    RelativeLayout rlJiankong;
    @BindView(R.id.iv_l4)
    ImageView ivL4;
    @BindView(R.id.iv_point4)
    ImageView ivPoint4;
    @BindView(R.id.rl_daxiangfm)
    RelativeLayout rlDaxiangfm;
    @BindView(R.id.iv_l5)
    ImageView ivL5;
    @BindView(R.id.iv_point5)
    ImageView ivPoint5;
    @BindView(R.id.rl_kaoqin)
    RelativeLayout rlKaoqin;
    @BindView(R.id.iv_l6)
    ImageView ivL6;
    @BindView(R.id.iv_point6)
    ImageView ivPoint6;
    @BindView(R.id.rl_qingjiatiao)
    RelativeLayout rlQingjiatiao;
    @BindView(R.id.iv_l7)
    ImageView ivL7;
    @BindView(R.id.iv_point7)
    ImageView ivPoint7;
    @BindView(R.id.rl_yongyaotiao)
    RelativeLayout rlYongyaotiao;
    @BindView(R.id.iv_l8)
    ImageView ivL8;
    @BindView(R.id.iv_point8)
    ImageView ivPoint8;
    @BindView(R.id.rl_tongzhi)
    RelativeLayout rlTongzhi;
    @BindView(R.id.rl_chengzhang)
    RelativeLayout rlChengzhang;
    @BindView(R.id.rl_xingwei)
    RelativeLayout rlXingwei;
    @BindView(R.id.rl_shipu)
    RelativeLayout rlShipu;
    @BindView(R.id.rl_teseban)
    RelativeLayout rlTeseban;
    @BindView(R.id.ll_diaocha)
    LinearLayout llDiaocha;
    @BindView(R.id.ll_hudong)
    LinearLayout llHudong;
    @BindView(R.id.ll_weixin)
    LinearLayout llWeixin;

    private HomeActivity activity ;

    private final int RESULT_BANNER_DATA = 0x004;

    private final int RESULT_BANJIQUAN_1 = 0x005;
    private final int RESULT_BANJIQUAN_2 = 0x006;
    private final int RESULT_BANJIQUAN_3 = 0x007;

    private ArticleListBean vpLunboData ;

    public final static String ACTION_HOME_POINT_RED = "com.daxiang.home.red.action";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    if (vpViewpager.getCurrentItem() > 0){
                        vpViewpager.setCurrentItem(vpViewpager.getCurrentItem() + 1);
                        handler.sendEmptyMessageDelayed(0, 2000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_home, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_HOME_POINT_RED);
            getActivity().registerReceiver(broadcastReceiver , filter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        activity = (HomeActivity) getActivity();

        loadNetData();

        handler.sendEmptyMessageDelayed(0, 2000);
        vpViewpager.setOnTouchListener(this);
    }

    private void loadNetData() {
        RequestService.doGet(Urls.getArticlelists("120",SharedPrefUtils.getString(getActivity(),SharedPrefUtils.APP_USER_ID)), RESULT_BANNER_DATA, this);
        try {
            RequestService.doGet(Urls.getNewsNewsCount("52", SharedPrefUtils.getString(getActivity() , SharedPrefUtils.APP_BANJIQUAN_TONGZHI_MAX)), RESULT_BANJIQUAN_1 , this);
            RequestService.doGet(Urls.getNewsNewsCount("62" , SharedPrefUtils.getString(getActivity() , SharedPrefUtils.APP_BANJIQUAN_JINGCAI_MAX)), RESULT_BANJIQUAN_2 , this);
            RequestService.doGet(Urls.getNewsNewsCount("65" , SharedPrefUtils.getString(getActivity() , SharedPrefUtils.APP_BANJIQUAN_YUANSUO_MAX)), RESULT_BANJIQUAN_3 , this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl_yuansuo, R.id.rl_jingcai, R.id.rl_jiankong, R.id.rl_daxiangfm, R.id.rl_kaoqin, R.id.rl_qingjiatiao, R.id.rl_yongyaotiao, R.id.rl_tongzhi, R.id.rl_chengzhang, R.id.rl_xingwei, R.id.rl_shipu, R.id.rl_teseban, R.id.ll_diaocha, R.id.ll_hudong, R.id.ll_weixin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_yuansuo:
                activity.banjiquan.setCurrent(3);
                activity.setViewPager(0 , activity.banjiquan.getFenlei());
                activity.setDataChanged();

                break;
            case R.id.rl_jingcai:
                activity.banjiquan.setCurrent(1);
                activity.setViewPager(0 , activity.banjiquan.getFenlei());
                activity.setDataChanged();
                break;
            case R.id.rl_jiankong:
                startActivity(JiankongActivity.class);
                break;
            case R.id.rl_daxiangfm:
                startActivity(DaxiangFMActivity.class);
                break;
            case R.id.rl_kaoqin:
                startActivity(KaoqinGuanliActivity.class);
                break;
            case R.id.rl_qingjiatiao:
                startActivity(QingjiatiaoActivity.class);
                break;
            case R.id.rl_yongyaotiao:
                startActivity(YongyaoBaogao1Activity.class);
                break;
            case R.id.rl_tongzhi:
                activity.banjiquan.setCurrent(0);
                activity.setViewPager(0 , activity.banjiquan.getFenlei());
                activity.setDataChanged();
                break;
            case R.id.rl_chengzhang:
                startActivity(ChengzhangRizhiActivity.class);
                break;
            case R.id.rl_xingwei:
                startActivity(XingweiJiluSingleActivity.class);
                break;
            case R.id.rl_shipu:
                activity.banjiquan.setCurrent(2);
                activity.setViewPager(0 , activity.banjiquan.getFenlei());
                activity.setDataChanged();
                break;
            case R.id.rl_teseban:
                startActivity(TesebanActivity.class);
                break;
            case R.id.ll_diaocha:
                Bundle bundle1 = new Bundle();
                bundle1.putString("title", "调查问卷");
                bundle1.putString("loadUrl", "http://dx.sitemn.com/cons/pagerslist.aspx?uid="+SharedPrefUtils.getString(getActivity() , SharedPrefUtils.APP_USER_ID));
                startActivity(BrowserActivity.class, bundle1);
                break;
            case R.id.ll_hudong:
                startActivity(XianhutousuActivity.class);
                break;
            case R.id.ll_weixin://TODO 群聊入口
//                Bundle bundle2 = new Bundle();
//                bundle2.putString("title", "官方微信");
//                bundle2.putString("url_cid", "gfwx");
//                startActivity(BrowserActivity.class, bundle2);
                startActivityForResult(new Intent(activity, GroupsActivity.class), 0);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(0);
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(0, 2000);
                break;
        }
        return false;
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_BANNER_DATA:
                try {
                    if (resultBean.isSuccess()){
                        vpLunboData = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        if (vpLunboData == null){
                            vpLunboData = JsonParser.get(new JSONObject(responseStr) , ArticleListBean.class , new ArticleListBean());
                        }

                        LunboPagerAdapter adapter = new LunboPagerAdapter(getActivity(), vpLunboData.getData());
                        vpViewpager.setAdapter(adapter);
                        vpViewpager.setCurrentItem(vpLunboData.getData().size() * 10000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_BANJIQUAN_1 :
                try{
                    if (resultBean.isSuccess()){
                        NewsCountBean listBean = JsonParser.get(new JSONObject(responseStr) , NewsCountBean.class , new NewsCountBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , NewsCountBean.class , new NewsCountBean());
                        }
                        if (listBean.getData() > 0){
                            ivPoint8.setVisibility(View.VISIBLE);
                        }else {
                            ivPoint8.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_BANJIQUAN_2 :
                try{
                    if (resultBean.isSuccess()){
                        NewsCountBean listBean = JsonParser.get(new JSONObject(responseStr) , NewsCountBean.class , new NewsCountBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , NewsCountBean.class , new NewsCountBean());
                        }
                        if (listBean.getData() > 0){
                            ivPoint2.setVisibility(View.VISIBLE);
                        }else {
                            ivPoint2.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_BANJIQUAN_3:
                try{
                    if (resultBean.isSuccess()){
                        NewsCountBean listBean = JsonParser.get(new JSONObject(responseStr) , NewsCountBean.class , new NewsCountBean());
                        if (listBean == null){
                            listBean = JsonParser.get(new JSONObject(responseStr) , NewsCountBean.class , new NewsCountBean());
                        }
                        if (listBean.getData() > 0){
                            ivPoint1.setVisibility(View.VISIBLE);
                        }else {
                            ivPoint1.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {

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
                if (ACTION_HOME_POINT_RED.equals(action)){
                    int item = intent.getIntExtra("index" , 0);
                    switch (item){
                        case 0:
                            ivPoint8.setVisibility(View.GONE);
                            break;

                        case 1:
                            ivPoint2.setVisibility(View.GONE);
                            break;
                        case 2:

                            break;

                        case 3:
                            ivPoint1.setVisibility(View.GONE);
                            break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}