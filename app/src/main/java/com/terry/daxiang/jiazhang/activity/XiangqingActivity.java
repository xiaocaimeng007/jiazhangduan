package com.terry.daxiang.jiazhang.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter.OnRecyclerViewItemClickListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.adapter.PinglunAdapter;
import com.terry.daxiang.jiazhang.bean.ArticleContentBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.AsyncBitmapByVideo;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.ResponseCallback;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.DateUtils;
import com.terry.daxiang.jiazhang.utils.DensityUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.terry.daxiang.jiazhang.utils.ShareUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CircleImageView;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;
import com.terry.daxiang.jiazhang.view.MyRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XiangqingActivity extends BaseTestActivity implements OnRecyclerViewItemClickListener {

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
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_shipin)
    TextView tvShipin;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.ll_zanliebiao)
    LinearLayout llZanliebiao;
    @BindView(R.id.tv_zan)
    TextView tvZan;
    @BindView(R.id.iv_zan)
    ImageView iv_zan;
    @BindView(R.id.tv_huifu)
    TextView tvHuifu;
    @BindView(R.id.rv_pinglun)
    MyRecyclerView rvPinglun;
    @BindView(R.id.linear_dashang)
    LinearLayout linear_dashang;
    @BindView(R.id.tv_juanzeng)
    TextView tvJuanzeng;
    @BindView(R.id.aixinyongchu)
    TextView aixinyongchu;
    @BindView(R.id.aixinhuodong)
    TextView aixinhuodong;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.iv_huifu)
    ImageView ivHuifu;
    @BindView(R.id.grid_albums)
    GridView grid_albums;
    ImageGridAdapter gridAdapter;

    @BindView(R.id.iv_dianzan_one)
    CircleImageView iv_dianzan_one;
    @BindView(R.id.iv_dianzan_two)
    CircleImageView iv_dianzan_two;
    @BindView(R.id.iv_dianzan_three)
    CircleImageView iv_dianzan_three;
    @BindView(R.id.iv_dianzan_four)
    CircleImageView iv_dianzan_four;
    @BindView(R.id.iv_dianzan_five)
    CircleImageView iv_dianzan_five;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_REFRESH_ALL_DATA = 0x004;
    private final int RESULT_ALL_COMMENT = 0x002;
    private final int RESULT_ALL_DIANZAN = 0x003;
    private final int RESULT_ALL_DASHANG = 0x005;
    private final int RESULT_ALL_DASHANG_CHECK = 0x006;

    public final static String ACTION_DAXIANG_SUCCESS = "com.xiangqing.daxiang.action";
    private String _cid = "";
    private boolean isdashang = false;
    private int width;
    private int height;
    private PopupWindow pw2;
    private PopupWindow pw3;

    private ArticleContentBean contentBean;

    private int is_dianzan = 0;

    private IWXAPI iwApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangqing);
        ButterKnife.bind(this);
        if (iwApi == null){
            iwApi = WXAPIFactory.createWXAPI(this , ShareUtil.IW_APP_PLAY_ID);
            iwApi.registerApp(ShareUtil.IW_APP_PLAY_ID);
        }

        Bundle bundle = getIntent().getBundleExtra("bundle");
        String title = bundle.getString("title");
        _cid = bundle.getString("content_cid" , "");

        try {
            isdashang = bundle.getBoolean("isdashang" , false);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (isdashang){
            linear_dashang.setVisibility(View.VISIBLE);
        }else {
            linear_dashang.setVisibility(View.GONE);
        }

        setTextview(tvTitle, title);
//        ivGengduo.setVisibility(View.VISIBLE);
        int[] display = DensityUtils.getDefaultDisplay(this);
        width = display[0];
        height = display[1];

        loadNetData();

        grid_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String path = gridAdapter.getUrlpath().get(position);
                    if (path.endsWith(".mp4")){
                        Uri uri = Uri.parse(path);
                        //调用系统自带的播放器
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);
                    }else if (path.endsWith(".mp3")){
                        Uri uri = Uri.parse(path);
                        //调用系统自带的播放器s
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "audio/mp3");
                        startActivity(intent);
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("fabu" , false);
                        bundle.putSerializable("imgUrl" , gridAdapter.getUrlpath());
                        bundle.putInt("position" ,position);
                        startActivity(PhotosGalleryActivity.class, bundle);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        registerReceiver(broadcastReceiver , new IntentFilter(ACTION_DAXIANG_SUCCESS));

    }

    private void initData() {
        try {
            //头像
            Glide.with(getApplicationContext()).load(Urls.URL_AVATAR_HOST+contentBean.getImg_url()).error(R.mipmap.icon_defualt_head).into(ivIcon);

            tvName.setText(contentBean.getFields().getAuthor());
            String times = contentBean.getAdd_time().substring(6 , contentBean.getAdd_time().length() - 2);
            tvTime.setText(DateUtils.getTime(times));
            tvCount.setText(contentBean.getClick()+"");
            tvZan.setText(contentBean.getArticle_zan().size()+"");
            if (contentBean.getIs_dianzan() == 0){
                iv_zan.setBackgroundResource(R.mipmap.icon_zan);
            }else {
                iv_zan.setBackgroundResource(R.mipmap.icon_zan_ed);
            }

            if (contentBean.getAlbums() != null && contentBean.getAlbums().size() > 0){
                tvShipin.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_image , 0 , 0 , 0);
                tvShipin.setText("图片");
            }else if (contentBean.getAttach() != null && contentBean.getAttach().size() > 0){
                tvShipin.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_shipin , 0 , 0 , 0);
                tvShipin.setText("视频");
            }else {
                tvShipin.setVisibility(View.GONE);
            }
            if (contentBean.getComment() != null){
                tvHuifu.setText(contentBean.getComment().size()+"");
            }else {
                tvHuifu.setText("0");
            }

            if (!TextUtils.isEmpty(contentBean.getZhaiyao())){
                tvText.setVisibility(View.VISIBLE);
                tvText.setText(contentBean.getZhaiyao());
            }else if (!TextUtils.isEmpty(contentBean.getContent())){
                tvText.setVisibility(View.VISIBLE);
                tvText.setText(contentBean.getContent());
            }
            initDianzanHead();
            if (contentBean.getArticle_zan() != null){
                ArrayList<ArticleContentBean.Article_zan> zans = contentBean.getArticle_zan();
                for (int i =0 ; i< zans.size(); i++){
                    String url_img = Urls.URL_AVATAR_HOST+zans.get(i).getImg_url();
                    switch (i){
                        case 0:
                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.ic_launcher).into(iv_dianzan_one);
                            break;

                        case 1:
                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_two);
                            break;

                        case 2:
                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_three);
                            break;

                        case 3:
                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_four);
                            break;

                        case 4:
                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_five);
                            break;
                    }
                }
            }

            is_dianzan = contentBean.getIs_dianzan();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadNetData(){
        RequestService.doGet(Urls.getArticleContent(_cid ,SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID)) , RESULT_ALL_DATA , this);
    }

    private void refreshNetData(){
        RequestService.doGet(Urls.getArticleContent(_cid ,SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID)) , RESULT_REFRESH_ALL_DATA , this);
    }

    private void initDianzanHead(){
        iv_dianzan_one.setImageResource(R.drawable.shape_while_dp);
        iv_dianzan_two.setImageResource(R.drawable.shape_while_dp);
        iv_dianzan_three.setImageResource(R.drawable.shape_while_dp);
        iv_dianzan_four.setImageResource(R.drawable.shape_while_dp);
        iv_dianzan_five.setImageResource(R.drawable.shape_while_dp);

    }

    @OnClick({R.id.ll_back, R.id.iv_gengduo, R.id.tv_zan, R.id.iv_zan,  R.id.tv_huifu,R.id.iv_huifu, R.id.tv_juanzeng, R.id.aixinyongchu, R.id.aixinhuodong})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_gengduo:
                break;
            case R.id.tv_zan:
            case R.id.iv_zan:
                try {
                    showProgress();
                    Map<String , String> params = new HashMap<>();
                    params.put("action" , "doFavorite");
                    params.put("uid" , SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID));
                    params.put("cid" , contentBean.getId()+"");
                    params.put("title" , contentBean.getTitle());
                    params.put("type","3");
                    RequestService.doFormPost("" , params , RESULT_ALL_DIANZAN , this);
                    Log.e("--" , "uid="+SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID)+"&cid="+contentBean.getId());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.tv_huifu:
            case R.id.iv_huifu:
                showPopup2("");
                break;
            case R.id.tv_juanzeng:
                showPopup();
                break;
            case R.id.aixinyongchu:
                Bundle bundle1 = new Bundle();
                bundle1.putString("url_cid", "jjyt");
                startActivity(BrowserActivity.class, bundle1);
                break;
            case R.id.aixinhuodong:
                Bundle bundle2 = new Bundle();
                bundle2.putString("url_cid", "axhd");
                startActivity(BrowserActivity.class, bundle2);
                break;
        }
    }

    private void showPopup() {
        View view = View.inflate(this, R.layout.popup_juanzeng, null);

        final EditText et_jine = (EditText)view.findViewById(R.id.et_jine);
        et_jine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_jine.setText(s);
                        et_jine.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_jine.setText(s);
                    et_jine.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_jine.setText(s.subSequence(0, 1));
                        et_jine.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        TextView bt_shang = (TextView) view.findViewById(R.id.bt_shang);
        bt_shang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String etJine = et_jine.getText().toString().trim();
                    if (TextUtils.isEmpty(etJine)){
                        ToastUtil.show("请输入当赏金额");
                        return;
                    }
                    Double money = Double.parseDouble(etJine);
                    if (money <= 0){
                        ToastUtil.show("请正确输入当赏金额");
                        return;
                    }

                    if (money >10){
                        ToastUtil.show("最多不超过10元");
                        return;
                    }

                    showProgress();

                    int intmoney = (int) Math.ceil( money * 100);

                    RequestService.doGetAll("http://dx.sitemn.com/pay/appserver.ashx?action=doPay&cid=725&money=" + intmoney + "&uid=" + SharedPrefUtils.getString(getApplicationContext(), SharedPrefUtils.APP_USER_ID)
                            , RESULT_ALL_DASHANG, new ResponseCallback() {
                                @Override
                                public void onSuccess(String responseStr, int requestCodes) {
                                    ResultBean resultBean = ResultUtil.getResult(responseStr , false);
                                    hideProgress();
                                    try {
//                                        WxDashangBean bean = JsonParser.get(new JSONObject(resultBean.getResult()) , WxDashangBean.class , new WxDashangBean());
//                                        if (bean == null){
//                                            bean = JsonParser.get(new JSONObject(resultBean.getResult()) , WxDashangBean.class , new WxDashangBean());
//                                        }

                                        JSONObject jsonObject = new JSONObject(responseStr);

                                        if (iwApi == null){
                                            iwApi = WXAPIFactory.createWXAPI(XiangqingActivity.this , ShareUtil.IW_APP_PLAY_ID);
                                            iwApi.registerApp(ShareUtil.IW_APP_PLAY_ID);
                                        }

                                        if(ShareUtil.getInstance().isWXAppInstalled(iwApi)){
                                            ShareUtil.getInstance().sendPayRequest(iwApi , jsonObject.optJSONObject("data"));
                                        }else{
                                            Toast.makeText(getApplicationContext(), "请安装微信", Toast.LENGTH_LONG).show();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(String responseStr, int requestCode) {
                                    hideProgress();
                                    ToastUtil.show(getString(R.string.string_load_error));
                                }
                            });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        pw2 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw2.setBackgroundDrawable(new ColorDrawable());
        pw2.setOutsideTouchable(false);
        pw2.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                DensityUtils.setWindomBgAlpha(XiangqingActivity.this, 1f);
            }
        });
        DensityUtils.setWindomBgAlpha(this, 0.5f);
        pw2.showAtLocation(llHome, Gravity.CENTER, 0, 0);
    }

    private void showPopup2(final String commentId) {
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
                    sentComment(commentId ,comment);
                }
            }
        });

        pw3 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw3.setBackgroundDrawable(new ColorDrawable());
        pw3.setOutsideTouchable(false);
        pw3.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pw3.showAtLocation(llBack, Gravity.BOTTOM, 0, 0);
    }

    private void sentComment(final String commentId , String content){
        Map<String ,String> params = new HashMap<String, String>();
        params.put("action" ,"doNewsComment");
        params.put("Id" , contentBean.getId()+"");
        params.put("commentid" , commentId);
        params.put("userid" , SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID));
        params.put("username" ,SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_NAME));
        params.put("content" , content);

        RequestService.doFormPost("" , params , RESULT_ALL_COMMENT,this);
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            String commentId = contentBean.getComment().get(position).getId()+"";
            showPopup2(commentId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        hideProgress();
        switch (requestCodes){
            case RESULT_ALL_DATA :
                try {
                    if (resultBean.isSuccess()){
                        contentBean = JsonParser.get(new JSONObject(resultBean.getResult()) , ArticleContentBean.class , new ArticleContentBean());
                        if (contentBean == null){
                            contentBean = JsonParser.get(new JSONObject(resultBean.getResult()) , ArticleContentBean.class , new ArticleContentBean());
                        }
                        //
                        initData();
                        try {
                            //图片和视频
                            gridAdapter = new ImageGridAdapter(this , contentBean);
                            grid_albums.setAdapter(gridAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        //实现对话
                        try {
                           if (contentBean.getComment()!= null && contentBean.getComment().size() >0){
                               rvPinglun.setVisibility(View.VISIBLE);
                               PinglunAdapter adapter = new PinglunAdapter(this ,R.layout.item_pinglun, contentBean.getComment());
                               adapter.setOnRecyclerViewItemClickListener(this);
                               rvPinglun.setLayoutManager(new LinearLayoutManager(this));
                               rvPinglun.setAdapter(adapter);
                           }else {
                               rvPinglun.setVisibility(View.GONE);
                           }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_REFRESH_ALL_DATA:
                try {
                    if (resultBean.isSuccess()){
                        contentBean = JsonParser.get(new JSONObject(resultBean.getResult()) , ArticleContentBean.class , new ArticleContentBean());
                        if (contentBean == null){
                            contentBean = JsonParser.get(new JSONObject(resultBean.getResult()) , ArticleContentBean.class , new ArticleContentBean());
                        }

                        try {
                            tvName.setText(contentBean.getFields().getAuthor());
                            String times = contentBean.getAdd_time().substring(6 , contentBean.getAdd_time().length() - 2);
                            tvTime.setText(DateUtils.getTime(times));
                            tvCount.setText(contentBean.getClick()+"");
                            tvZan.setText(contentBean.getArticle_zan().size()+"");
                            if (contentBean.getIs_dianzan() == 0){
                                iv_zan.setBackgroundResource(R.mipmap.icon_zan);
                            }else {
                                iv_zan.setBackgroundResource(R.mipmap.icon_zan_ed);
                            }

                            if (contentBean.getAlbums() != null && contentBean.getAlbums().size() > 0){
                                tvShipin.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_image , 0 , 0 , 0);
                                tvShipin.setText("图片");
                            }else if (contentBean.getAttach() != null && contentBean.getAttach().size() > 0){
                                tvShipin.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_shipin , 0 , 0 , 0);
                                tvShipin.setText("视频");
                            }else {
                                tvShipin.setVisibility(View.GONE);
                            }
                            if (contentBean.getComment() != null){
                                tvHuifu.setText(contentBean.getComment().size()+"");
                            }else {
                                tvHuifu.setText("0");
                            }

                            if (!TextUtils.isEmpty(contentBean.getContent())){
                                tvText.setVisibility(View.VISIBLE);
                                tvText.setText(contentBean.getZhaiyao());
                            }
                            initDianzanHead();
                            if (contentBean.getArticle_zan() != null){
                                ArrayList<ArticleContentBean.Article_zan> zans = contentBean.getArticle_zan();
                                for (int i =0 ; i< zans.size(); i++){
                                    String url_img = Urls.URL_AVATAR_HOST+zans.get(i).getImg_url();
                                    switch (i){
                                        case 0:
                                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_one);
                                            break;

                                        case 1:
                                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_two);
                                            break;

                                        case 2:
                                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_three);
                                            break;

                                        case 3:
                                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_four);
                                            break;

                                        case 4:
                                            Glide.with(getApplicationContext()).load(url_img).error(R.mipmap.icon_defualt_head).into(iv_dianzan_five);
                                            break;
                                    }
                                }
                            }

                            is_dianzan = contentBean.getIs_dianzan();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        //实现对话
                        try {
                            if (contentBean.getComment()!= null && contentBean.getComment().size() >0){
                                rvPinglun.setVisibility(View.VISIBLE);
                                PinglunAdapter adapter = new PinglunAdapter(this , R.layout.item_pinglun, contentBean.getComment());
                                adapter.setOnRecyclerViewItemClickListener(this);
                                rvPinglun.setLayoutManager(new LinearLayoutManager(this));
                                rvPinglun.setAdapter(adapter);
                            }else {
                                rvPinglun.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_ALL_COMMENT:
                try{
                    if (resultBean.isSuccess()){
                        loadNetData();
                    }
                    ToastUtil.show(resultBean.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case RESULT_ALL_DIANZAN:
                try {
                    try{
                        if (resultBean.isSuccess()){
                            refreshNetData();
                            if (is_dianzan == 0){
                                is_dianzan = 1;
                                ToastUtil.show("点赞成功!");
                            }else {
                                is_dianzan = 0;
                                ToastUtil.show("取消点赞!");
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
    }

    private class ImageGridAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private boolean isMedia = false;
        private List<ArticleContentBean.Albums> albums;
        private List<ArticleContentBean.Attach> attachs;
        private AsyncBitmapByVideo asyncBitmapLoader;

        private ArrayList<String> allPath = new ArrayList<>();

        public ImageGridAdapter(Context context ,ArticleContentBean data){
            this.context = context;
            inflater = LayoutInflater.from(context);
            asyncBitmapLoader = new AsyncBitmapByVideo(context);

            try {
                if (data.getAlbums().size() >0){
                    isMedia = false;
                    albums = data.getAlbums();
                    for (ArticleContentBean.Albums album : albums){
                        allPath.add(Urls.URL_AVATAR_HOST+album.getThumb_path());
                    }
                }

                if (data.getAttach().size() > 0){
                    isMedia = true;
                    attachs = data.getAttach();

                    for (ArticleContentBean.Attach attach : attachs){
                        allPath.add(Urls.URL_AVATAR_HOST+attach.getFile_path());
                    }
                }
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public boolean getType(){
            return isMedia;
        }

        public ArrayList<String> getUrlpath(){
            return allPath;
        }

        @Override
        public int getCount() {
            if (isMedia){
                return attachs == null ? 0 : attachs.size();
            }else {
                return albums == null ? 0 : albums.size();
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_imagegrid , null);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_album);
            TextView bt_bofang = (TextView) convertView.findViewById(R.id.bt_bofang);
            if (isMedia){
                if ("mp3".equalsIgnoreCase(attachs.get(position).getFile_ext())){
                    bt_bofang.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }else {
                    bt_bofang.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.mipmap.icon_shipin_click);
                    Bitmap bitmap = asyncBitmapLoader.LoadBitmapByVideo(Urls.URL_AVATAR_HOST + attachs.get(position).getFile_path(), imageView, new AsyncBitmapByVideo.ImageCallback() {
                        @Override
                        public void imageLoaded(Bitmap b, ImageView imageview, String imageUrl) {
                            imageview.setBackgroundDrawable(new BitmapDrawable(b));
                            imageview.setVisibility(View.VISIBLE);
                        }
                    });
                    if (bitmap != null){
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }
                }
            }else {
                bt_bofang.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(Urls.URL_AVATAR_HOST+albums.get(position).getThumb_path()).asBitmap().placeholder(R.color.color_cccccc).error(R.mipmap.ic_launcher).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setBackgroundDrawable(new BitmapDrawable(resource));
                    }
                });
            }
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (ACTION_DAXIANG_SUCCESS.equals(action)){
                    if (pw2 != null && pw2.isShowing()){
                        pw2.dismiss();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

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
