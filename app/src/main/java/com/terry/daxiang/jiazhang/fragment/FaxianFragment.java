package com.terry.daxiang.jiazhang.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.faxian.ErtongYueduActivity;
import com.terry.daxiang.jiazhang.activity.faxian.HotActivity;
import com.terry.daxiang.jiazhang.activity.faxian.ShouCangActivity;
import com.terry.daxiang.jiazhang.adapter.FindAdapter;
import com.terry.daxiang.jiazhang.adapter.HotGridAdapter;
import com.terry.daxiang.jiazhang.adapter.LunboPagerAdapter;
import com.terry.daxiang.jiazhang.bean.ArticleListBean;
import com.terry.daxiang.jiazhang.bean.FavoriteBean;
import com.terry.daxiang.jiazhang.bean.FindBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.NoScrollGridView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/15.
 */
public class FaxianFragment extends BaseFragment implements OnTouchListener {
    @BindView(R.id.vp_lunbo)
    ViewPager vpLunbo;
    @BindView(R.id.find_grid_view)
    NoScrollGridView find_grid_view;
    @BindView(R.id.tv_tianjiaguanzhu)
    TextView tvTianjiaguanzhu;
    @BindView(R.id.tv_more)
    TextView tv_more;
    @BindView(R.id.rl_favorite)
    RelativeLayout rl_favorite;
    @BindView(R.id.tv_biaoti)
    TextView tvBiaoti;
    @BindView(R.id.grid_albums)
    NoScrollGridView grid_albums;

    private final int RESULT_FIND_DATA = 0x001;
    private final int RESULT_FAVORITE_DATA = 0x002;
    private final int RESULT_HOT_DATA = 0x003;
    private final int RESULT_BANNER_DATA = 0x004;

    private FindBean findBean = null;
    private ArticleListBean vpLunboData;
    private boolean isFirstRefresh = true;
    private List<FindBean.Data> datas = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    if (vpLunbo.getCurrentItem() > 0) {
                        vpLunbo.setCurrentItem(vpLunbo.getCurrentItem() + 1);
                        handler.sendEmptyMessageDelayed(0, 2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private FindAdapter findAdapter;

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_faxian, null);
    }

    @Override
    public void init() {
        isFirstRefresh = false;
        loadNetData();

        handler.sendEmptyMessageDelayed(0, 2000);
        vpLunbo.setOnTouchListener(this);

        grid_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(HotActivity.class);
            }
        });
        //发现
        findAdapter = new FindAdapter(getActivity(), datas);
        find_grid_view.setAdapter(findAdapter);
        find_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FindBean.Data data = datas.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("title", data.getTitle());
                bundle.putString("content_aid", data.getId() + "");
                startActivity(ErtongYueduActivity.class, bundle);
            }
        });
    }

    private void loadNetData() {
        RequestService.doGet(Urls.getArticlelists("121", SharedPrefUtils.getString(getActivity(), SharedPrefUtils.APP_USER_ID)), RESULT_BANNER_DATA, this);
        RequestService.doGet(Urls.getFindMain(), RESULT_FIND_DATA, this);
        RequestService.doGet(Urls.getFavorite(SharedPrefUtils.getString(getActivity(), SharedPrefUtils.APP_USER_ID), "1"), RESULT_FAVORITE_DATA, this);
        RequestService.doGet(Urls.geHotList("1", ""), RESULT_HOT_DATA, this);
    }

    public void refresh() {
        if (!isFirstRefresh) {
            loadNetData();
        }
    }


    @OnClick({R.id.tv_tianjiaguanzhu, R.id.rl_favorite, R.id.tv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tianjiaguanzhu:
                startActivity(ShouCangActivity.class);
                break;
            case R.id.rl_favorite:
                startActivity(ShouCangActivity.class);
                break;
            case R.id.tv_more:
                startActivity(HotActivity.class);
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
        ResultBean resultBean = ResultUtil.getResult(responseStr, false);
        switch (requestCodes) {
            case RESULT_FIND_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        findBean = JsonParser.get(new JSONObject(responseStr), FindBean.class, new FindBean());
                        if (findBean == null) {
                            findBean = JsonParser.get(new JSONObject(responseStr), FindBean.class, new FindBean());
                        }
                        datas.clear();
                        datas.addAll(findBean.getData());
                        findAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case RESULT_FAVORITE_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        FavoriteBean favoriteBean = JsonParser.get(new JSONObject(responseStr), FavoriteBean.class, new FavoriteBean());
                        String title = favoriteBean.getData().get(0).getTitle();
                        if (TextUtils.isEmpty(title)) {
                            rl_favorite.setVisibility(View.GONE);
                        } else {
                            tvBiaoti.setText(title);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RESULT_HOT_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        ArticleListBean listBean = JsonParser.get(new JSONObject(responseStr), ArticleListBean.class, new ArticleListBean());
                        if (listBean == null) {
                            listBean = JsonParser.get(new JSONObject(responseStr), ArticleListBean.class, new ArticleListBean());
                        }

                        HotGridAdapter adapter = new HotGridAdapter(getActivity(), listBean.getData());
                        grid_albums.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case RESULT_BANNER_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        vpLunboData = JsonParser.get(new JSONObject(responseStr), ArticleListBean.class, new ArticleListBean());
                        if (vpLunboData == null) {
                            vpLunboData = JsonParser.get(new JSONObject(responseStr), ArticleListBean.class, new ArticleListBean());
                        }

                        LunboPagerAdapter adapter = new LunboPagerAdapter(getActivity(), vpLunboData.getData());
                        vpLunbo.setAdapter(adapter);
                        vpLunbo.setCurrentItem(vpLunboData.getData().size() * 10000);
                    }
                } catch (Exception e) {
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
