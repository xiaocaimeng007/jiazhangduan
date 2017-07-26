package com.terry.daxiang.jiazhang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.AsyncRevitionImage;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.fragment.BanjiquanFragment;
import com.terry.daxiang.jiazhang.utils.FileUtils;
import com.terry.daxiang.jiazhang.utils.HCAlbumUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XianhutousuActivity extends BaseTestActivity {

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
    @BindView(R.id.txt_xingxiang)
    TextView txt_xingxiang;
    private PopupWindow pw2;

    private final int RESULT_ALL_DATA = 0x001;

    private ArrayList<String> filepaths = new ArrayList<>();
    private ImageGridAdapter imageGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        ButterKnife.bind(this);


        setTextview(tvTitle, "发布" + getString(R.string.string_home_bangjiquan_tousu));
        showView(llSend);
        showView(txt_xingxiang);
        hideView(ivJiahao);
        initData();
    }

    private void initData(){
        imageGridAdapter = new ImageGridAdapter(this , filepaths);
        grid_albums.setAdapter(imageGridAdapter);

        grid_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == filepaths.size()){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(tvFabu.getWindowToken(), 0);
                    showPopup2();
                }else {
                    String path = filepaths.get(position);
                    if (path.endsWith(".mp4")){
                        Uri uri = Uri.parse(path);
                        //调用系统自带的播放器
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("fabu" , true);
                        bundle.putSerializable("imgUrl" , filepaths);
                        bundle.putInt("position" ,position);
                        startActivity(PhotosGalleryActivity.class, bundle);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imageGridAdapter != null){
            imageGridAdapter.refresh(filepaths);
        }
    }

    @OnClick({R.id.ll_back, R.id.ll_send, R.id.rl_xuanze})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
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
        if (TextUtils.isEmpty(content) || calculateLength(content) < 5){
            ToastUtil.show("内容必须4个字以上");
            return;
        }

        Map<String , String> params = new HashMap<>();
        params.put("action" , "doPublishTS");
        params.put("authorid" , SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_ID));
        params.put("newscontent" , content);
        params.put("filecount" , filepaths.size()+"");

        Map<String , String> fileParams = new HashMap<>();
        for (int i =0 ; i < filepaths.size() ; i++) {
            int k = i +1;
            fileParams.put("file"+k , filepaths.get(i));
        }
        System.out.println(params);
        System.out.println(fileParams);
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
        tvShipin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoForVideo();
                pw2.dismiss();
            }
        });
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
                    filepaths.add(filepath);
                    if (imageGridAdapter != null){
                        imageGridAdapter.refresh(filepaths);
                    }
                }
                break;
            case TAKE_PICTURE_SYSTEM:
                if (resultCode == Activity.RESULT_OK) {
                    String url= HCAlbumUtils.getPath(this,data.getData());
                    filepaths.add(url);
                    if (imageGridAdapter != null){
                        imageGridAdapter.refresh(filepaths);
                    }
                }
                break;

            case TAKE_REQUEST_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(filepath);
                    if (!file.exists() || file.length() < 1){
                        try {
                            AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                            FileInputStream fis = videoAsset.createInputStream();
                            File tmpFile = FileUtils.getSaveFile(FileUtils.up_cacheFolder,String.valueOf(System.currentTimeMillis()) + ".mp4");
                            filepath = tmpFile.getAbsolutePath();
                            FileOutputStream fos = new FileOutputStream(tmpFile);
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = fis.read(buf)) > 0) {
                                fos.write(buf, 0, len);
                            }
                            fis.close();
                            fos.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    Log.e("--" , "==> "+filepath);
                    filepaths.add(filepath);
                    if (imageGridAdapter != null){
                        imageGridAdapter.refresh(filepaths);
                    }
                }
                break;
        }
    }

    /******************/
    private final int TAKE_PICTURE = 0x000078;
    private final int TAKE_PICTURE_SYSTEM = 0x000079;
    private final int TAKE_REQUEST_VIDEO = 0x000080;
    private String filepath="";
    // 手机相册
    public void photoForSystem(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent,TAKE_PICTURE_SYSTEM);
    }
    // 手机拍照
    public void photoForCamera() {
        File file = FileUtils.getSaveFile(FileUtils.up_cacheFolder,String.valueOf(System.currentTimeMillis()) + ".jpg");
        filepath = file.getAbsolutePath();
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    //手机录视频
    public void photoForVideo(){
        File file = FileUtils.getSaveFile(FileUtils.up_cacheFolder,String.valueOf(System.currentTimeMillis()) + ".mp4");
        filepath = file.getAbsolutePath();
        Intent captureImageCamera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri = Uri.fromFile(file);
        captureImageCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);                //指定要保存的位置。
        //captureImageCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, );            //设置拍摄的质量
        captureImageCamera.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);            //限制持续时长
        startActivityForResult(captureImageCamera,TAKE_REQUEST_VIDEO);
    }
    /*********************/

    private class ImageGridAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<String> datas = new ArrayList<>();
        private AsyncRevitionImage revitionImage;

        public ImageGridAdapter(Context context ,ArrayList<String> data){
            this.context = context;
            inflater = LayoutInflater.from(context);
            revitionImage = new AsyncRevitionImage(context);
            datas.addAll(data);
        }

        public void refresh(ArrayList<String> data){
            revitionImage.clearCache();
            datas.clear();
            datas.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return datas.size()+1;
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
            convertView = inflater.inflate(R.layout.item_fabu_imagegrid , null);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_album);
            RelativeLayout relative_tianjia = (RelativeLayout) convertView.findViewById(R.id.relative_tianjia);
            imageView.setVisibility(View.VISIBLE);
            if (position == datas.size()){
                imageView.setBackgroundResource(R.drawable.shape_juxing_heibian0dp);
                imageView.setImageResource(R.mipmap.icon_tianjia);
                if (position == 9){
                    imageView.setVisibility(View.GONE);
                }

                imageView.setVisibility(View.GONE);
                relative_tianjia.setVisibility(View.VISIBLE);
                if (position == 9){
                    imageView.setVisibility(View.GONE);
                    relative_tianjia.setVisibility(View.GONE);
                }

            }else {
                imageView.setVisibility(View.VISIBLE);
                relative_tianjia.setVisibility(View.GONE);
                final String filePath = datas.get(position);
                if (filePath.endsWith(".mp4")){
                    imageView.setVisibility(View.GONE);
                    imageView.setImageResource(R.mipmap.icon_shipin_click);
                    Glide.with(context).load(filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource != null){
                                imageView.setVisibility(View.VISIBLE);
                                imageView.setBackgroundDrawable(new BitmapDrawable(resource));
                            }
                        }
                    });

                }else {
                    if (filePath.startsWith(FileUtils.getSavePath(FileUtils.up_cacheFolder))){
                        Bitmap bitmap = revitionImage.revitionImage(filePath, imageView , new AsyncRevitionImage.ImageCallback() {
                        @Override
                        public void imageLoaded(Bitmap b, ImageView imageview, String imageUrl) {
                            imageview.setBackgroundDrawable(new BitmapDrawable(b));
                        }
                        });

                        if (bitmap != null){
                            imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        }
                    }else {
                        Glide.with(context).load(filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                imageView.setBackgroundDrawable(new BitmapDrawable(resource));
                            }
                        });
                    }
                }
            }
            return convertView;
        }
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
