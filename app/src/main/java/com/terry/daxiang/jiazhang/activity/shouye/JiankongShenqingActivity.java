package com.terry.daxiang.jiazhang.activity.shouye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.activity.PhotosGalleryActivity;
import com.terry.daxiang.jiazhang.bean.CameraPositionBean;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.AsyncRevitionImage;
import com.terry.daxiang.jiazhang.custom.JsonParser;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.utils.FileUtils;
import com.terry.daxiang.jiazhang.utils.HCAlbumUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomProgressBar;
import com.terry.daxiang.jiazhang.view.HCChooseWindow;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JiankongShenqingActivity extends BaseTestActivity {

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
    @BindView(R.id.bt_shenqing)
    TextView btShenqing;
    @BindView(R.id.edit_username)
    TextView edit_username;
    @BindView(R.id.edit_phone)
    TextView edit_phone;
    @BindView(R.id.edit_position)
    TextView edit_position;
    @BindView(R.id.grid_albums)
    GridView grid_albums;

    private final int RESULT_ALL_DATA = 0x001;
    private final int RESULT_SEND_DATA = 0x002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiankong_shenqing);
        ButterKnife.bind(this);
        setTextview(tvTitle, "视频监控申请");
        loadNetData();
    }

    private void loadNetData(){
        RequestService.doGet(Urls.CameraPosition(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID)) , RESULT_ALL_DATA , this);

        edit_username.setText(SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_NICKNAME));
        String phone1 = SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_MOBILE);
        String phone2 = SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_TELPHONE);
        if (!TextUtils.isEmpty(phone1)){
            edit_phone.setText(phone1);
        }else if (!TextUtils.isEmpty(phone2)){
            edit_phone.setText(phone2);
        }else {
            edit_phone.setText("");
        }
        imageGridAdapter = new ImageGridAdapter(this , filepaths);
        grid_albums.setAdapter(imageGridAdapter);

        grid_albums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == filepaths.size()){
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

    @OnClick({R.id.ll_back, R.id.bt_shenqing , R.id.edit_position})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_shenqing:
                send();
                break;

            case R.id.edit_position:
                if (positionWindown != null){
                    positionWindown.setCurrentItem(0);
                    positionWindown.show();
                }
                break;
        }
    }

    private void send(){
        Map<String , String> params = new HashMap<>();
        params.put("action" , "doCreateCamera");
        params.put("uid" , SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_ID));
        params.put("position" , positionContents.get(_position).getKey());
        params.put("filecount" , filepaths.size()+"");

        Map<String , String> fileParams = new HashMap<>();
        for (int i =0 ; i < filepaths.size() ; i++) {
            int k = i +1;
            fileParams.put("file"+k , filepaths.get(i));
        }
        showProgress();
        RequestService.sendMultipart(Urls.URL_APP_HOST , params , fileParams ,RESULT_SEND_DATA , this );
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        hideProgress();
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes){
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()){
                        CameraPositionBean bean = JsonParser.get(new JSONObject(responseStr) , CameraPositionBean.class , new CameraPositionBean());
                        for (CameraPositionBean.Data data : bean.getData()){
                            PositionContent positionContent = new PositionContent();
                            positionContent.setKey(data.getId()+"");
                            positionContent.setValue(data.getTitle());
                            positionContents.add(positionContent);
                        }
                        edit_position.setText(positionContents.get(0).getValue());
                        initPosition();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case RESULT_SEND_DATA:
                try {
                    if (resultBean.isSuccess()) {
                        finish();
                    }
                    ToastUtil.show(resultBean.getMessage());
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
        }
    }

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
            convertView = inflater.inflate(R.layout.item_imagegrid_1 , null);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_album);
            imageView.setVisibility(View.VISIBLE);
            if (position == datas.size()){
                imageView.setBackgroundResource(R.drawable.shape_juxing_heibian0dp);
                imageView.setImageResource(R.mipmap.icon_tianjia);
                if (position == 9){
                    imageView.setVisibility(View.GONE);
                }
            }else {
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

    /******************/
    private final int TAKE_PICTURE = 0x000078;
    private final int TAKE_PICTURE_SYSTEM = 0x000079;
    private String filepath="";
    private ArrayList<String> filepaths = new ArrayList<>();
    private ImageGridAdapter imageGridAdapter;
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

    /*********************/
    private PopupWindow pw2;
    private void showPopup2() {
        View view = View.inflate(this, R.layout.popup_fabu, null);
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
    /******选择器**********/
    private HCChooseWindow positionWindown ;
    private List<HCChooseWindow.DataResources> positionContents = new ArrayList<>();
    private int _position = 0;
    private void initPosition(){

        positionWindown = new HCChooseWindow(JiankongShenqingActivity.this, llBack, positionContents, "申请来源", new HCChooseWindow.WheelCallBack() {
            @Override
            public void choose(int index) {
                edit_position.setText(positionContents.get(index).getValue());
                _position = index;
            }
        });
    }

    private class PositionContent implements HCChooseWindow.DataResources{
        private String value;
        private String key;

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    /*******************/

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
