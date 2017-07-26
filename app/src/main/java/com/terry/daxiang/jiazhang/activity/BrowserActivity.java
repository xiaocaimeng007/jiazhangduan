package com.terry.daxiang.jiazhang.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ArticleContentBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.BKWebView;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chen_fulei on 2016/11/24.
 */

public class BrowserActivity extends BaseTestActivity {
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.browser_progressbar)
    ProgressBar browser_progressbar;
    @BindView(R.id.browser)
    BKWebView browser;

    private final int RESULT_ALL_DATA = 0x001;

    /**
     * 标题，链接
     */
    private String title = "", url_cid = "" , loadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        url_cid = bundle.getString("url_cid", "");
        loadUrl = bundle.getString("loadUrl", "");
        title = bundle.getString("title", "");
        setTextview(tvTitle, title);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setSupportZoom(true);

        browser.setWebChromeClient(new BKWebChromeClient());
        browser.setWebViewClient(new BKWebViewClient());
//        browser.loadUrl(url);
        if (TextUtils.isEmpty(loadUrl)){
            loadNetData();
        }else {
            browser.loadUrl(loadUrl);
        }

    }

    private void loadNetData(){
        showProgress();
        RequestService.doGet(Urls.getArticleUrl(url_cid) , RESULT_ALL_DATA , this);
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        hideProgress();
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                 try {
                     ArticleContentBean contentBean = JsonParser.get(new JSONObject(resultBean.getResult()) , ArticleContentBean.class , new ArticleContentBean());
                     if (contentBean == null){
                         contentBean = JsonParser.get(new JSONObject(resultBean.getResult()) , ArticleContentBean.class , new ArticleContentBean());
                     }
                     browser.loadUrl(contentBean.getLink_url());
                     setTextview(tvTitle, contentBean.getTitle());

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
        finish();
    }

    class BKWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            browser_progressbar.setProgress(newProgress);

        }
    }

    class BKWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            browser_progressbar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            browser_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            browser_progressbar.setProgress(10);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onBackPressed() {
        browser.loadUrl("about:blank");
        super.onBackPressed();
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
