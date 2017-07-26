package com.terry.daxiang.jiazhang.activity.shouye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseActivity;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.bean.DrugDetailBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.AsyncBitmapByVideo;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ToastUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YongyaoGuanliActivity extends BaseTestActivity {

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
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_yongyaotime)
    TextView tvYongyaotime;
    @BindView(R.id.tv_songyaoren)
    TextView tvSongyaoren;
    @BindView(R.id.tv_zerenren)
    TextView tvZerenren;
    @BindView(R.id.tv_bingyin)
    TextView tvBingyin;
    @BindView(R.id.iv_shipin)
    ImageView ivShipin;

    private final int RESULT_ALL_DATA = 0x001;
    DrugDetailBean.Data drugDetail;
    private String drug_id = "";

    private AsyncBitmapByVideo asyncBitmapLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yongyao_guanli);
        ButterKnife.bind(this);
//        showView(llSend);
        hideView(ivJiahao);
        setTextview(tvTitle, "用药管理");
//        setTextview(tvSend, "用药报告");


        asyncBitmapLoader = new AsyncBitmapByVideo(this);

        initData();
    }

    private void initData(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        drug_id = bundle.getString("drug_id" , "");

        RequestService.doGet(Urls.getDrugDetail(drug_id) , RESULT_ALL_DATA , this );
    }

    private void showDataByView(){
        try {
            if (drugDetail != null){
                time.setText(drugDetail.getDrugs_date());
                tvName.setText(drugDetail.getTitle());
                tvCount.setText(drugDetail.getDrugs_quantum());
                tvYongyaotime.setText(drugDetail.getDrugs_time());
                tvZerenren.setText(drugDetail.getDrugs_child());
                tvBingyin.setText(drugDetail.getDrugs_reason());

                ivShipin.setImageResource(R.mipmap.icon_shipin_click);

                Bitmap bitmap = asyncBitmapLoader.LoadBitmapByVideo(Urls.URL_AVATAR_HOST + drugDetail.getFile_path(), ivShipin, new AsyncBitmapByVideo.ImageCallback() {
                    @Override
                    public void imageLoaded(Bitmap b, ImageView imageview, String imageUrl) {
                        imageview.setBackgroundDrawable(new BitmapDrawable(b));
                        imageview.setVisibility(View.VISIBLE);
                    }
                });
                if (bitmap != null){
                    ivShipin.setVisibility(View.VISIBLE);
                    ivShipin.setImageResource(R.mipmap.icon_shipin_click);
                    ivShipin.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_back, R.id.ll_send, R.id.iv_shipin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_send:
                finish();
                break;
            case R.id.iv_shipin:
                Uri uri = Uri.parse(Urls.URL_AVATAR_HOST + drugDetail.getFile_path());
                //调用系统自带的播放器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/mp4");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                try{
                    if (resultBean.isSuccess()){
                        DrugDetailBean drugDetailBean = JsonParser.get(new JSONObject(responseStr) , DrugDetailBean.class , new DrugDetailBean());
                        if (drugDetailBean == null){
                            drugDetailBean = JsonParser.get(new JSONObject(responseStr) , DrugDetailBean.class , new DrugDetailBean());
                        }

                        drugDetail = drugDetailBean.getData().get(0);

                        showDataByView();
                    }
                }catch (Exception e){
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