package com.terry.daxiang.jiazhang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.album.AlbumPicActivity;
import com.terry.daxiang.jiazhang.album.BKBitmap;
import com.terry.daxiang.jiazhang.album.PhotoActivity;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.fragment.BanjiquanFragment;
import com.terry.daxiang.jiazhang.utils.FileUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FabuActivity extends BaseTestActivity {

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
    @BindView(R.id.iv_jiahao)
    ImageView ivJiahao;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_fabu)
    EditText tvFabu;
    @BindView(R.id.iv_diqiu)
    ImageView ivDiqiu;
    @BindView(R.id.rl_xuanze)
    RelativeLayout rlXuanze;
    @BindView(R.id.grid_albums)
    GridView grid_albums;
    private PopupWindow pw2;

    private final int RESULT_ALL_DATA = 0x001;
    private static final int THREAD_PHOTO_WHAT = 0x11216;

    private String[] fenlei;
    private int type;
//    private ArrayList<String> filepaths = new ArrayList<>();
    private ImageGridAdapter imageGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        ButterKnife.bind(this);
        fenlei = new String[]{getString(R.string.string_home_bangjiquan_togou),getString(R.string.string_home_bangjiquan_jingcai),getString(R.string.string_home_bangjiquan_shipu)
                ,getString(R.string.string_home_bangjiquan_anquan)};

        Bundle bundle = getIntent().getBundleExtra("bundle");
        type = bundle.getInt("current");

        setTextview(tvTitle, "发布" + getString(R.string.string_home_bangjiquan_jingcai));
        showView(llSend);
        hideView(ivJiahao);
        initData();
    }

    private void initData(){
        imageGridAdapter = new ImageGridAdapter(this);
        grid_albums.setAdapter(imageGridAdapter);
        imageGridAdapter.update();
        grid_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == BKBitmap.bmp.size()){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(tvFabu.getWindowToken(), 0);
                    showPopup2();
                }else {
                    Intent intent = new Intent(FabuActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onRestart()
    {
        if (null != imageGridAdapter) {
            imageGridAdapter.update();
        }
        super.onRestart();
    }

    @OnClick({R.id.ll_back, R.id.ll_send, R.id.rl_xuanze})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.ll_send:
                //判断type 是哪个分类
                send();
                break;
            case R.id.rl_xuanze:
                break;
        }
    }

    private void send(){
        String content = tvFabu.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            if (BKBitmap.bmp.size() <=0){
                ToastUtil.show("请填写发送的内容");
                return;
            }

        }else if (calculateLength(content) <4){
            ToastUtil.show("内容必须3个字以上");
            return;
        }

        Map<String , String> params = new HashMap<>();
        params.put("action" , "doPublishNews");
        params.put("authorid" , SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_ID));
        params.put("newscontent" , content);
        params.put("filecount" , BKBitmap.bmp.size()+"");

        Map<String , String> fileParams = new HashMap<>();
        for (int i =0 ; i < BKBitmap.bmp.size() ; i++) {
            int k = i +1;
            String cacheName = BKBitmap.drr.get(i).substring(BKBitmap.drr.get(i).lastIndexOf("/") + 1, BKBitmap.drr.get(i).lastIndexOf("."));
            File file = FileUtils.getSaveFile(FileUtils.fabu_cacheFolder, cacheName + ".jpg");
//            Log.e("--" , "====> "+ BKBitmap.drr.get(i));
//            Log.e("--" , "---------> "+ file.getAbsolutePath());
            fileParams.put("file"+k , file.getAbsolutePath());
        }
//        System.out.println(params);
//        System.out.println(fileParams);
        showProgress();

        RequestService.sendMultipart(Urls.URL_APP_HOST , params , fileParams ,RESULT_ALL_DATA , this );
    }

    private long calculateLength(CharSequence c) {
        try {
            double len = 0;
            for (int i = 0; i < c.length(); i++) {
                int tmp = (int) c.charAt(i);
                if (tmp > 0 && tmp < 127) {
                    len += 0.5;
                } else {
                    len++;
                }
            }
            return Math.round(len);
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        hideProgress();
        ResultBean resultbean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA :
                try{
                    if (resultbean.isSuccess()){
                        // 完成上传服务器后 .........
                        FileUtils.cleanCacheFiles(FileUtils.fabu_cacheFolder);
                        BKBitmap.bmp.clear();
                        BKBitmap.drr.clear();
                        BKBitmap.max = 0;
                        sendBroadcast(new Intent(BanjiquanFragment.ACTION_BANJIQUAN_REFRESH));
                        finish();
                    }
                    ToastUtil.show(resultbean.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            // 完成上传服务器后 .........
            FileUtils.cleanCacheFiles(FileUtils.fabu_cacheFolder);
            BKBitmap.bmp.clear();
            BKBitmap.drr.clear();
            BKBitmap.max = 0;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
        hideProgress();
        ToastUtil.show(getString(R.string.string_load_error));
    }

    private void showPopup2() {
        View view = View.inflate(this, R.layout.popup_fabu, null);
        TextView tvShipin = ButterKnife.findById(view, R.id.tv_shipin);
        TextView tvPaizhao = ButterKnife.findById(view, R.id.tv_paizhao);
        TextView tvXiangce = ButterKnife.findById(view, R.id.tv_xiangce);
        TextView tvQuxiao = ButterKnife.findById(view, R.id.tv_quxiao);
        tvPaizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoForCamera();
                pw2.dismiss();
            }
        });
        tvXiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoForSystem();
                pw2.dismiss();
            }
        });
        tvQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw2.dismiss();
            }
        });
        pw2 = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw2.setBackgroundDrawable(new ColorDrawable());
        pw2.showAtLocation(llBack, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("--" , "==> "+filepath);
                    if (BKBitmap.drr.size() < 9)
                    {
                        isFromCamera = true;
                        BKBitmap.drr.add(filepath);
                        if (null != imageGridAdapter) {
                            imageGridAdapter.notifyDataSetChanged();
                        }
                    }
                }

                break;
        }
    }

    /******************/
    private final int TAKE_PICTURE = 0x000078;
    private final int TAKE_PICTURE_SYSTEM = 0x000079;
    private String filepath="";
    private boolean isFromCamera = false;
    // 手机相册
    public void photoForSystem(){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        startActivityForResult(intent,TAKE_PICTURE_SYSTEM);

        startActivity(AlbumPicActivity.class);

    }
    // 手机拍照
    public void photoForCamera() {
        File file = FileUtils.getSaveFile(FileUtils.fabu_cacheFolder,String.valueOf(System.currentTimeMillis()) + ".jpg");
        filepath = file.getAbsolutePath();
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /*********************/

    private class ImageGridAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public ImageGridAdapter(Context context){
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        public void update()
        {
            loading();
        }

        @Override
        public int getCount() {
            return (BKBitmap.bmp.size() + 1);
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
            ViewHolder vh = null;
            if (null == convertView)
            {
                convertView = inflater.inflate( R.layout.item_fabu_imagegrid, null);
                vh = new ViewHolder();
                vh.image = (ImageView) convertView.findViewById(R.id.iv_album);
                vh.relative_tianjia = (RelativeLayout) convertView.findViewById(R.id.relative_tianjia);
                convertView.setTag(vh);
            } else
            {
                vh = (ViewHolder) convertView.getTag();
            }
            if (position == BKBitmap.bmp.size()){
                vh.image.setVisibility(View.GONE);
                vh.relative_tianjia.setVisibility(View.VISIBLE);
                if (position == 9){
                    vh.image.setVisibility(View.GONE);
                    vh.relative_tianjia.setVisibility(View.GONE);
                }
            }else {
                vh.image.setVisibility(View.VISIBLE);
                vh.relative_tianjia.setVisibility(View.GONE);
                vh.image.setImageResource(R.drawable.shape_while_dp);
                if (null == BKBitmap.bmp.get(position))
                {
                    vh.image.setBackgroundResource(R.mipmap.icon_launcher);
                } else
                {
                    vh.image.setBackgroundDrawable(new BitmapDrawable(BKBitmap.bmp
                            .get(position)));
                }
            }
            return convertView;
        }

        public class ViewHolder
        {
            public ImageView image;
            public RelativeLayout relative_tianjia;
        }

        private void loading()
        {
            new Thread(new Runnable()
            {
                public void run()
                {
                    while (true)
                    {
                        if (BKBitmap.max == BKBitmap.drr.size())
                        {
                            if (null != mHandler)
                            {
                                Message message = new Message();
                                message.what = THREAD_PHOTO_WHAT;
                                mHandler.sendMessage(message);
                            }
                            break;
                        } else
                        {
                            try
                            {
                                String path = BKBitmap.drr.get(BKBitmap.max);
                                BKBitmap.max += 1;
                                Bitmap bm;
                                if (isFromCamera)
                                {
                                    isFromCamera = false;
                                    bm = BKBitmap.revitionImageSize(path, true);
                                } else
                                {
                                    bm = BKBitmap.revitionImageSize(path, false);
                                }
                                BKBitmap.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr, FileUtils.fabu_cacheFolder);

                                if (null != mHandler)
                                {
                                    Message message = new Message();
                                    message.what = THREAD_PHOTO_WHAT;
                                    mHandler.sendMessage(message);
                                }
                            } catch (IOException e)
                            {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case THREAD_PHOTO_WHAT:
                    if (null != imageGridAdapter) {
                        imageGridAdapter.notifyDataSetChanged();
                    }
                    break;
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
}
