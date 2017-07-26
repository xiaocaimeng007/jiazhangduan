package com.terry.daxiang.jiazhang.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleContentBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.DensityUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ShareUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fulei on 16/12/17.
 */

public class FaxianListShowActivity extends BaseTestActivity{

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_gengduo)
    ImageView iv_gengduo;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.txt_content)
    TextView txt_content;
    @BindView(R.id.bt_tianjia)
    TextView bt_tianjia;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_ALL_COMMENT = 0x002;
    private String _aid = "" ,title = "" , _context="" , _shareUrl = "";
    private int shoucan = 0;
    private ArticleContentBean contentBean;

    private boolean isShoucan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faxian_xingqing);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        title = bundle.getString("title");
        _aid = bundle.getString("content_cid" , "");
        _context = bundle.getString("_context" , "");

        showView(iv_gengduo);
        setTextview(tvTitle, "");
        txt_title.setText(title);
        txt_content.setText(_context);
        initViewMorePopup();

        loadNetData();
    }

    private void initShoucan(){
        if (shoucan == 1){
            bt_tianjia.setText("取消收藏");
        }else {
            bt_tianjia.setText("添加收藏");
        }
    }

    private void loadNetData(){
        RequestService.doGet(Urls.getArticleContent(_aid ,SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID)) , RESULT_ALL_DATA , this);
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        hideProgress();
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA :
                try {
                    if (resultBean.isSuccess()) {
                        contentBean = JsonParser.get(new JSONObject(resultBean.getResult()), ArticleContentBean.class, new ArticleContentBean());
                        if (contentBean == null) {
                            contentBean = JsonParser.get(new JSONObject(resultBean.getResult()), ArticleContentBean.class, new ArticleContentBean());
                        }
                        initData();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case RESULT_ALL_COMMENT :
                try{
                    isShoucan = true;
                    if (resultBean.isSuccess()){
                        if (shoucan == 0){
                            shoucan = 1;
                        }else {
                            shoucan = 0;
                        }
                        initShoucan();
                    }
                    ToastUtil.show(resultBean.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;


        }
    }

    private void initData() {
        try {
            _shareUrl = contentBean.getShareurl();

            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(Uri.parse(Urls.URL_AVATAR_HOST+contentBean.getFields().getVideo_src()));
            //让VideiView获取焦点
            videoView.requestFocus();

            shoucan = contentBean.getIs_shoucang();
            txt_title.setText(contentBean.getTitle());
            txt_content.setText(contentBean.getZhaiyao());
            initShoucan();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        ToastUtil.show(getString(R.string.string_load_error));
        hideProgress();
    }

    @OnClick({R.id.ll_back,R.id.bt_tianjia , R.id.iv_gengduo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.bt_tianjia:

                showProgress();
                Map<String , String> params = new HashMap<>();
                params.put("action" , "doFavorite");
                params.put("uid" , SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID));
                params.put("cid" , _aid);
                params.put("title" , title);
                params.put("type","1");
                Log.e("--", "uid="+SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID)+"&cid="+_aid+"&title="+title);
                RequestService.doFormPost("" , params , RESULT_ALL_COMMENT , this);
                break;
            case R.id.iv_gengduo:
                try {
                    if (more_popupWindow == null) {
                        initViewMorePopup();
                    }

                    if (more_popupWindow.isShowing()) {
                        more_popupWindow.dismiss();
                    } else {
                        DensityUtils.setWindomBgAlpha(this, 0.5f);
                        more_popupWindow.showAtLocation(llBack, Gravity.BOTTOM, 0, 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isShoucan){
            setResult(RESULT_OK, new Intent());
            finish();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 更多弹框
     */
    private PopupWindow more_popupWindow;
    private IWXAPI iwApi;

    private void initViewMorePopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.inc_popup_share, null);
        more_popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        more_popupWindow.setAnimationStyle(R.style.threads_view_more_popwin);
        more_popupWindow.setBackgroundDrawable(new BitmapDrawable());
        more_popupWindow.setOutsideTouchable(true);

        more_popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                DensityUtils.setWindomBgAlpha(FaxianListShowActivity.this, 1f);
            }
        });

        view.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (more_popupWindow != null) {
                    more_popupWindow.dismiss();
                }
            }
        });


        view.findViewById(R.id.txt_friend).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(iwApi == null){
                    iwApi = ShareUtil.getInstance().regToWx(FaxianListShowActivity.this, true);
                }
                if(ShareUtil.getInstance().isWXAppInstalled(iwApi)){
                    byte[] bp = null;
                    ShareUtil.getInstance().SendMessageToWXSession(iwApi, _shareUrl , title , bp);
                }else{
                    Toast.makeText(getApplicationContext(), "请安装微信", Toast.LENGTH_LONG).show();
                }

                if (more_popupWindow != null) {
                    more_popupWindow.dismiss();
                }
            }
        });

        view.findViewById(R.id.txt_kongjian).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(iwApi == null){
                    iwApi = ShareUtil.getInstance().regToWx(FaxianListShowActivity.this, true);
                }
                if(ShareUtil.getInstance().isWXAppInstalled(iwApi)){
                    ShareUtil.getInstance().SendMessageToWXTimeline(iwApi, _shareUrl , title);
                }else{
                    Toast.makeText(getApplicationContext(), "请安装微信", Toast.LENGTH_LONG).show();
                }

                if (more_popupWindow != null) {
                    more_popupWindow.dismiss();
                }
            }
        });
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
