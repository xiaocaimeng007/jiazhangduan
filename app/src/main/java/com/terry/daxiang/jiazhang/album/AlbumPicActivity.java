package com.terry.daxiang.jiazhang.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 從相冊選擇圖片
 *
 * @author honey_chen
 */
public class AlbumPicActivity extends BaseTestActivity
{
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.iv_jiahao)
    ImageView ivJiahao;
    @BindView(R.id.gridview)
    GridView gridview;

    /**
     * 相冊幫助類
     */
    private AlbumHelper helper;

    /**
     * 相冊縮略圖
     */
    public static Bitmap bimap;

    /**
     * 相冊列表
     */
    public List<ImageBucket> dataList;
    /**
     * 相冊數據適配器
     */
    private ImageBucketAdapter adapter;

    public static final int AlbumCode = 0x000071;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumpic_activity);
        ButterKnife.bind(this);

        try {
            /**
             * 初始化相冊幫助類
             */
            helper = AlbumHelper.getHelper();
            helper.init(activity);

            dataList = helper.getImagesBucketList(true);
            bimap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_tianjia);
        }catch (Exception e){
            e.printStackTrace();
        }

        initWidget();
    }


    /**
     * 初始化控件
     */
    public void initWidget()
    {
        // 標題
        setTextview(tvTitle , "手机相册");

        adapter = new ImageBucketAdapter(activity, dataList);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                // TODO Auto-generated method stub
                /**
                 * 通知适配器，绑定的数据发生了改变，应当刷新视图
                 */
                Intent intent = new Intent(activity, ImageGridActivity.class);
                intent.putExtra("image_list", (Serializable) dataList.get(arg2).imageList);
                intent.putExtra("album_name", (Serializable) dataList.get(arg2).bucketName);

                startActivityForResult(intent, AlbumCode);
            }
        });
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
    protected void onActivityResult(int requestCode, int arg1, Intent arg2)
    {
        super.onActivityResult(requestCode, arg1, arg2);
        switch (requestCode)
        {
            case AlbumCode:
                if (arg1 == RESULT_OK)
                {
                    activity.finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {

    }

    @Override
    public void onFailure(String responseStr, int requestCode) {

    }
}
