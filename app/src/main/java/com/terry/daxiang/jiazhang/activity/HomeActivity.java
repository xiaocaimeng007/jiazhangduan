package com.terry.daxiang.jiazhang.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.faxian.ShouCangActivity;
import com.terry.daxiang.jiazhang.activity.shouye.ChengzhangRizhiActivity;
import com.terry.daxiang.jiazhang.activity.shouye.DaxiangFMActivity;
import com.terry.daxiang.jiazhang.activity.shouye.JiankongActivity;
import com.terry.daxiang.jiazhang.activity.shouye.KaoqinGuanliActivity;
import com.terry.daxiang.jiazhang.activity.shouye.XingweiJiluSingleActivity;
import com.terry.daxiang.jiazhang.activity.shouye.YongyaoBaogao1Activity;
import com.terry.daxiang.jiazhang.adapter.HomePageAdapter;
import com.terry.daxiang.jiazhang.bean.Banjiquan;
import com.terry.daxiang.jiazhang.bean.ResultBean;
import com.terry.daxiang.jiazhang.custom.RequestService;
import com.terry.daxiang.jiazhang.custom.Urls;
import com.terry.daxiang.jiazhang.fragment.BanjiquanFragment;
import com.terry.daxiang.jiazhang.fragment.FaxianFragment;
import com.terry.daxiang.jiazhang.fragment.HomeFragment;
import com.terry.daxiang.jiazhang.fragment.TongxunluFragment;
import com.terry.daxiang.jiazhang.service.TimeService;
import com.terry.daxiang.jiazhang.utils.DensityUtils;
import com.terry.daxiang.jiazhang.utils.FileUtils;
import com.terry.daxiang.jiazhang.utils.HCAlbumUtils;
import com.terry.daxiang.jiazhang.utils.ResultUtil;
import com.hyphenate.easeui.utils.SharedPrefUtils;
import com.terry.daxiang.jiazhang.view.CircleImageView;
import com.terry.daxiang.jiazhang.view.CustomDialog;
import com.terry.daxiang.jiazhang.view.NoScrollViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 首页
 *
 * @author freychen on 2016/10/19.
 *
 */
public class HomeActivity extends BaseTestActivity implements OnCheckedChangeListener, OnClickListener {

    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    public TextView tvSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.vp_home)
    NoScrollViewPager vpHome;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.rb_3)
    RadioButton rb3;
    @BindView(R.id.rb_4)
    RadioButton rb4;
    @BindView(R.id.rg_home)
    RadioGroup rgHome;
    @BindView(R.id.tv_title1)
    public TextView tvTitle1;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    TextView tvT1 ,tvT2 ,tvT3,tvT4;
    @BindView(R.id.iv_jiahao)
    ImageView ivJiahao;
    @BindView(R.id.ll_send)
    public LinearLayout llSend;
    //记录radiobutton的位置
    private int radioButton = -1;
    private List<RadioButton> radioButtons;
    private PopupWindow pw1;
    public Banjiquan banjiquan;
    private List<Fragment> fragments;
    private int width;
    private int height;
    private PopupWindow pw3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        //获取屏幕宽高 设置"我的"的宽高
        width = (int) (DensityUtils.getDefaultDisplay(this)[0] * 0.45);
        height = DensityUtils.getDefaultDisplay(this)[1];
        setRadioGroup();
        addFragments();
        hideView(llSend);

        JPushInterface.init(getApplicationContext());
        JPushInterface.setAliasAndTags(getApplicationContext(),SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ACCOUNT), null,null);
        //启动服务
        startService(new Intent(this , TimeService.class));
    }

    //添加fragment 填充viewpager
    private void addFragments() {
        BanjiquanFragment fragment1 = new BanjiquanFragment();
        TongxunluFragment fragment2 = new TongxunluFragment();
        HomeFragment fragment3 = new HomeFragment();
        FaxianFragment fragment4 = new FaxianFragment();
        fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        rgHome.setOnCheckedChangeListener(this);
        vpHome.setOffscreenPageLimit(fragments.size());
        vpHome.setAdapter(new HomePageAdapter(getSupportFragmentManager(), fragments));
        setViewPager(2, "首页");
        //添加radiobutton
        radioButtons = new ArrayList<>();
        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(null);
        radioButtons.add(rb3);
        banjiquan = new Banjiquan(this);
        banjiquan.setCurrent(1);
    }

    public void setDataChanged() {
        ((BanjiquanFragment) fragments.get(0)).setDataChanged();
    }

    public void setViewPager(int i, String s) {
        vpHome.setCurrentItem(i, false);
        if (i == 0) {
            showView(llTitle);
            hideView(tvTitle);
            if (banjiquan.isShow()) {
                showView(llSend);
            }
            setTextview(tvTitle1, s);
        } else {
            showView(tvTitle);
            hideView(llTitle);
            hideView(llSend);
            setTextview(tvTitle, s);
        }
        if (i == 2) {
            radioButton = -1;
            hideView(llBack);
        } else {
            radioButton = i;
            showView(llBack);
        }
    }

    //调整radioGroup图标大小
    private void setRadioGroup() {
        int[] imgs = {R.mipmap.icon_banjiquan, R.mipmap.icon_tongxunlu,
                0, R.mipmap.icon_faxian, R.mipmap.icon_wode};
        for (int i = 0; i < rgHome.getChildCount(); i++) {
            if (i == 2) {
                continue;
            }
            RadioButton rb = (RadioButton) rgHome.getChildAt(i);
            int width = DensityUtils.dp2px(this, 25);
            Drawable d = getResources().getDrawable(imgs[i]);
            d.setBounds(0, 0, width, width);
            //左 上 右 下
            rb.setCompoundDrawables(null, d, null, null);
        }
    }

    private void showPopup1() {
        View view = View.inflate(this, R.layout.popup_home, null);
        tvT1 = ButterKnife.findById(view, R.id.tv_t1);
        tvT2 = ButterKnife.findById(view, R.id.tv_t2);
        tvT3 = ButterKnife.findById(view, R.id.tv_t3);
        tvT4 = ButterKnife.findById(view, R.id.tv_t4);
        tvT1.setOnClickListener(this);
        tvT2.setOnClickListener(this);
        tvT3.setOnClickListener(this);
        tvT4.setOnClickListener(this);
        pw1 = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        pw1.setBackgroundDrawable(new ColorDrawable());
        pw1.showAsDropDown(llTitle, DensityUtils.dp2px(this, -15), DensityUtils.dp2px(this, 5));
    }

    private void showPopup3() {
        try {
            View view = View.inflate(this, R.layout.popup_wode, null);
            TextView wodekaoqin = ButterKnife.findById(view, R.id.wodekaoqin);
            TextView wodejiaofei = ButterKnife.findById(view, R.id.wodejiaofei);
            TextView yongyaoguanli = ButterKnife.findById(view, R.id.yongyaoguanli);
            TextView xingweijilu = ButterKnife.findById(view, R.id.xingweijilu);
            TextView hemujiating = ButterKnife.findById(view, R.id.hemujiating);
            TextView shipinjiankong = ButterKnife.findById(view, R.id.shipinjiankong);
            TextView chengzhangrizhi = ButterKnife.findById(view, R.id.chengzhangrizhi);
            TextView daxiangfm = ButterKnife.findById(view, R.id.daxiangfm);
            TextView hudongtousu = ButterKnife.findById(view, R.id.hudongtousu);
            TextView wodeshoucang = ButterKnife.findById(view, R.id.wodeshoucang);
            TextView shezhi = ButterKnife.findById(view, R.id.shezhi);
            TextView tvName = ButterKnife.findById(view,R.id.text_name);

            TextView txt_mobile = ButterKnife.findById(view , R.id.txt_mobile);
            String mobile = SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_MOBILE);
            String telPhone = SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_TELPHONE);
            String nickName = SharedPrefUtils.getString(this,SharedPrefUtils.APP_USER_NICKNAME);
            if (!TextUtils.isEmpty(mobile)){
                txt_mobile.setText(mobile);
            }else  if (!TextUtils.isEmpty(telPhone)){
                txt_mobile.setText(telPhone);
            }
            if(!TextUtils.isEmpty(nickName)){
                tvName.setText(nickName);
            }else{
                tvName.setText("暂无昵称");
            }

            iv_avatar = ButterKnife.findById(view , R.id.iv_avatar);
            Glide.with(this).load(SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_AVATAR)).error(R.mipmap.icon_defualt_head).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_avatar);

            ButterKnife.findById(view , R.id.txt_change_head).setOnClickListener(this);
            ButterKnife.findById(view , R.id.txt_password).setOnClickListener(this);
            wodekaoqin.setOnClickListener(this);
            wodejiaofei.setOnClickListener(this);
            yongyaoguanli.setOnClickListener(this);
            xingweijilu.setOnClickListener(this);
            hemujiating.setOnClickListener(this);
            shipinjiankong.setOnClickListener(this);
            chengzhangrizhi.setOnClickListener(this);
            daxiangfm.setOnClickListener(this);
            hudongtousu.setOnClickListener(this);
            wodeshoucang.setOnClickListener(this);
            shezhi.setOnClickListener(this);
            pw3 = new PopupWindow(view, width, height, true);
            pw3.setBackgroundDrawable(new ColorDrawable());
            pw3.showAtLocation(llHome, Gravity.RIGHT, 0, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_back, R.id.ll_send, R.id.iv_home, R.id.rb_4, R.id.rb_1, R.id.ll_title})
    public void onClick1(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                rgHome.clearCheck();
                setViewPager(2, "首页");
                break;
            case R.id.ll_send:
                Bundle bundle = new Bundle();
                bundle.putInt("current", banjiquan.getCurrent());
                startActivity(FabuActivity.class, bundle);
                break;
            case R.id.ll_title:
                showPopup1();
                break;
            case R.id.iv_home:
                if (vpHome.getCurrentItem() != 2) {
                    rgHome.clearCheck();
                    setViewPager(2, "首页");
                }
                break;
            case R.id.rb_4:
                if (radioButton >= 0) {
                    radioButtons.get(radioButton).setChecked(true);
                }
                showPopup3();
                break;
            case R.id.rb_1:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_t1:
                banjiquan.setCurrent(0);
                setDataChanged();
                pw1.dismiss();
                break;
            case R.id.tv_t2:
                banjiquan.setCurrent(1);
                setDataChanged();
                pw1.dismiss();
                break;
            case R.id.tv_t3:
                banjiquan.setCurrent(2);
                setDataChanged();
                pw1.dismiss();
                break;
            case R.id.tv_t4:
                banjiquan.setCurrent(3);
                setDataChanged();
                pw1.dismiss();
                break;
            case R.id.txt_change_head:
                showPopup2();
                break;
            case R.id.txt_password:
                startActivity(ForgetPassActivity.class);

                break;
            case R.id.wodekaoqin:
                startActivity(KaoqinGuanliActivity.class);
                pw3.dismiss();
                break;
            case R.id.wodejiaofei:
                String loadUrl = "http://dx.sitemn.com//cons/myFee.aspx?uid="+SharedPrefUtils.getString(getApplicationContext() , SharedPrefUtils.APP_USER_ID);
                Bundle bundle1 = new Bundle();
                bundle1.putString("loadUrl", loadUrl);
                bundle1.putString("title", "我的交费");
                startActivity(BrowserActivity.class, bundle1);
                pw3.dismiss();
                break;
            case R.id.yongyaoguanli:
                startActivity(YongyaoBaogao1Activity.class);
                pw3.dismiss();
                break;
            case R.id.xingweijilu:
                startActivity(XingweiJiluSingleActivity.class);
                pw3.dismiss();
                break;
            case R.id.hemujiating:
                Bundle bundle = new Bundle();
                bundle.putString("title", "和睦家庭");
                bundle.putString("loadUrl" ,"http://dx.sitemn.com/cons/myFamiy.aspx?uid="+SharedPrefUtils.getString(getApplicationContext(),SharedPrefUtils.APP_USER_ID ));
                startActivity(BrowserActivity.class , bundle);
                pw3.dismiss();
                break;
            case R.id.shipinjiankong:
                startActivity(JiankongActivity.class);
                pw3.dismiss();
                break;
            case R.id.chengzhangrizhi:
                startActivity(ChengzhangRizhiActivity.class);
                pw3.dismiss();
                break;
            case R.id.daxiangfm:
                startActivity(DaxiangFMActivity.class);
                pw3.dismiss();
                break;
            case R.id.hudongtousu:
                startActivity(XhTousuActivity.class);
                pw3.dismiss();
                break;
            case R.id.wodeshoucang:
                startActivity(ShouCangActivity.class);
                pw3.dismiss();
                break;
            case R.id.shezhi:
                CustomDialog dialog = new CustomDialog(this , llBack , "确认退出吗？" ,false);
                dialog.setBtnText("退出", "取消");
                dialog.show();
                pw3.dismiss();
                dialog.setOnSureClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //环信在这里要选择登出
                        EMClient.getInstance().logout(true, new EMCallBack() {
                            @Override
                            public void onSuccess() {//环信登出成功 进入到登陆界面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        SharedPrefUtils.setBoolean(getApplicationContext() , SharedPrefUtils.APP_USER_ISLOGIN , false);
                                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                            @Override
                            public void onProgress(int progress, String status) {
                            }
                            @Override
                            public void onError(int code, String message) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(HomeActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });

                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_1:
                banjiquan.setCurrent(1);
                setViewPager(0, banjiquan.getFenlei());
                break;
            case R.id.rb_2:
                setViewPager(1, "通讯录");
                break;
            case R.id.rb_3:
                setViewPager(3, "发现");
                ((FaxianFragment)fragments.get(3)).refresh();
                break;
        }
    }

    /**
     * 按下返回键，关闭程序
     */
    private long outLoginTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - outLoginTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                outLoginTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final int TAKE_PICTURE = 0x000078;
    private static final int TAKE_PICTURE_SYSTEM = 0x000079;
    private static final int TAKE_PICTURE_ZOOM = 0x000080;
    private static final int RESULT_ALL_DATA = 0x000081;
    private String newHead = "";
    private PopupWindow pw_avater;
    private File file = null;
    private CircleImageView iv_avatar;

    private void showPopup2() {
        View view = View.inflate(this, R.layout.popup_fabu, null);
        TextView tvPaizhao = ButterKnife.findById(view, R.id.tv_paizhao);
        TextView tvXiangce = ButterKnife.findById(view, R.id.tv_xiangce);
        TextView tvQuxiao = ButterKnife.findById(view, R.id.tv_quxiao);
        tvPaizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoForCamera();
                pw_avater.dismiss();
            }
        });
        tvXiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoForSystem();
                pw_avater.dismiss();
            }
        });
        tvQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw_avater.dismiss();
            }
        });
        pw_avater = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        pw_avater.setBackgroundDrawable(new ColorDrawable());
        pw_avater.showAtLocation(llBack, Gravity.BOTTOM, 0, 0);
    }

    // 手机相册
    public void photoForSystem(){
        file = FileUtils.getSaveFile(FileUtils.up_cacheFolder,String.valueOf(System.currentTimeMillis()) + ".jpg");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent,TAKE_PICTURE_SYSTEM);
    }
    // 手机拍照
    public void photoForCamera() {
        file = FileUtils.getSaveFile(FileUtils.up_cacheFolder ,String.valueOf(System.currentTimeMillis()) + ".jpg");
        //path = file.getPath();
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    // 处理截图
    private void startPhotoZoom(Uri uri) {
        if (null != uri) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url= HCAlbumUtils.getPath(this,uri);
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            }else{
                intent.setDataAndType(uri, "image/*");
            }
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            // intent.putExtra("scale", true);//黑边
            intent.putExtra("scaleUpIfNeeded", true);// 黑边
            intent.putExtra("return-data", true);
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, TAKE_PICTURE_ZOOM);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(activity, "拍照", Toast.LENGTH_LONG).show();
                    Log.e("", "从拍照至服务器");
                    startPhotoZoom(Uri.fromFile(file));
                }
                break;
            case TAKE_PICTURE_SYSTEM:
                if (resultCode == Activity.RESULT_OK) {
                    //Toast.makeText(activity, "系统相册", Toast.LENGTH_LONG).show();
                    Log.e("", "从系统相册至服务器");
                    startPhotoZoom(data.getData());
                }
                break;
            case TAKE_PICTURE_ZOOM:
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("", "接收到更新头像请求");
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        Log.e("", "开始执行");
                        Bitmap photo = bundle.getParcelable("data");
                        if(null != photo) {
                            Log.e("", "开始保存截图");
                            // 保存截图
                            Log.e("", "----->>>>"+file.getName().substring(0, file.getName().indexOf(".")));
                            FileUtils.saveBitmap(photo, file.getName().substring(0, file.getName().indexOf(".")),FileUtils.up_cacheFolder);
                            // 清除本地緩存、更新至服务器
                            Log.e("", "保存截图成功，清除本地緩存、更新至服务器");
                            sendHead(file.getName());
                        }else {
                            Log.e("", "photo is null");
                        }
                    }
                }
                break;
        }
    }

    private void sendHead(String filename){
        File file = FileUtils.getSaveFile(FileUtils.up_cacheFolder, filename);
        newHead = file.getAbsolutePath();
        Log.e("" , "--> "+ file.getAbsolutePath());
        Map<String , String> params = new HashMap<>();
        params.put("action" , "doChangeAvatar");
        params.put("uid" , SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_ID));

        Map<String , String> fileParams = new HashMap<>();
        fileParams.put("file" , newHead);

        RequestService.sendMultipart(Urls.URL_APP_HOST , params , fileParams ,RESULT_ALL_DATA , this );
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
        ResultBean resultBean = ResultUtil.getResult(responseStr , false);
        switch (requestCodes) {
            case RESULT_ALL_DATA:
                try {
                    if (resultBean.isSuccess()){
                        SharedPrefUtils.setString(getApplicationContext() , SharedPrefUtils.APP_USER_AVATAR , newHead);
                        Glide.with(this).load(SharedPrefUtils.getString(this , SharedPrefUtils.APP_USER_AVATAR)).error(R.mipmap.icon_defualt_head).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_avatar);
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

}