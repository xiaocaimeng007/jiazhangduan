package com.terry.daxiang.jiazhang.album;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 顯示相冊裏的圖片
 *
 * @author honey_chen
 */
public class ImageGridActivity extends BaseTestActivity
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
     * 圖片幫助類
     */
    private AlbumHelper helper;
    /**
     * 圖片適配器
     */
    private ImageGridAdapter adapter;
    /**
     * 圖片數據集
     */
    private List<ImageItem> dataList;

    private String title = "";

    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        ButterKnife.bind(this);
        helper = AlbumHelper.getHelper();
        helper.init(activity);

        dataList = (List<ImageItem>) getIntent().getSerializableExtra("image_list");
        title = (String) getIntent().getSerializableExtra("album_name");

        initWidget();
    }

    public void initWidget(){
        setTextview(tvTitle , title);
        showView(llSend);
        hideView(ivJiahao);

        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                adapter.notifyDataSetChanged();
            }

        });
        adapter.setTextCallback(new ImageGridAdapter.TextCallback()
        {
            public void onListen(int count)
            {
                if (0 == count)
                {
                    tvSend.setText("完成");
                } else
                {
                    tvSend.setText("完成(" + (count + BKBitmap.bmp.size()) + "/9)");
                }
            }
        });
    }

    @OnClick({R.id.ll_back, R.id.ll_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.ll_send:
                //判断type 是哪个分类
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext(); )
                {
                    list.add(it.next());
                }

                if (BKBitmap.act_bool)
                {
                    BKBitmap.act_bool = false;
                }
                for (int i = 0; i < list.size(); i++)
                {
                    if (BKBitmap.drr.size() < 9)
                    {
                        BKBitmap.drr.add(list.get(list.size() - 1 - i));
                    }
                }
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0:
                    Toast.makeText(activity, "已超出上傳限制9張", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onSuccess(String responseStr, int requestCodes) {

    }

    @Override
    public void onFailure(String responseStr, int requestCode) {

    }
}
