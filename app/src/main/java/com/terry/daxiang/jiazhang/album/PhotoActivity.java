package com.terry.daxiang.jiazhang.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.BaseTestActivity;
import com.terry.daxiang.jiazhang.utils.FileUtils;
import com.terry.daxiang.jiazhang.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 显示相片
 *
 * @author honey_chen
 */
public class PhotoActivity extends BaseTestActivity
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

    /**
     * 显示图片的视图
     */
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private List<Bitmap> bmp = new ArrayList<Bitmap>();
    private List<String> drr = new ArrayList<String>();
    private List<String> del = new ArrayList<String>();
    private int max;
    private int page;
    private int id;
    private MyPageAdapter adapter;

    private final Activity activity = this;

    /**
     * 要显示的View
     */
    private ArrayList<View> listViews = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.albumphoto_activity);
        ButterKnife.bind(this);

        try {
            Intent intent = getIntent();
            id = intent.getIntExtra("ID", 0);

            for (int i = 0; i < BKBitmap.bmp.size(); i++)
            {
                bmp.add(BKBitmap.bmp.get(i));
            }
            for (int i = 0; i < BKBitmap.drr.size(); i++)
            {
                drr.add(BKBitmap.drr.get(i));
            }
            max = BKBitmap.max;
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
        setTextview(tvTitle , "");
        showView(llSend);
        setTextview(tvSend,"删除");
        hideView(ivJiahao);

        viewpager.addOnPageChangeListener(new OnPageChangeListener()
        {
            // 页面选择响应函数
            @Override
            public void onPageSelected(int arg0)
            {
                // TODO Auto-generated method stub
                page = arg0;
                tvTitle.setText((1 + page) + "/" + max);
            }

            // 滑动中。。。
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                // TODO Auto-generated method stub

            }

            // 滑动状态改变
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                // TODO Auto-generated method stub

            }
        });
        for (int i = 0; i < bmp.size(); i++)
        {
            if (null == bmp.get(i))
            {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_launcher);
                initListViews(bm);
            } else
            {
                initListViews(bmp.get(i));//
            }
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        viewpager.setAdapter(adapter);// 设置适配器

        viewpager.setCurrentItem(id);
        tvTitle.setText((id + 1) + "/" + max);
    }

    @SuppressWarnings("deprecation")
    private void initListViews(Bitmap bm)
    {
        if (listViews == null)
        {
            listViews = new ArrayList<View>();
        }
        ImageView img = new ImageView(this);// 构造textView对象
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        //img.setScaleType(ScaleType.FIT_XY);
        listViews.add(img);// 添加view
    }

    @OnClick({R.id.ll_back, R.id.ll_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.ll_send:
                CustomDialog dialog = new CustomDialog(activity, llBack, "確定刪除該圖片？", false);
                dialog.setBtnText("确定", "取消");
                dialog.setOnSureClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        // TODO Auto-generated method stub
                        if (listViews.size() == 1)
                        {
                            BKBitmap.bmp.clear();
                            BKBitmap.drr.clear();
                            BKBitmap.max = 0;
                            FileUtils.cleanCacheFiles(FileUtils.fabu_cacheFolder);
                            bmp.clear();
                            drr.clear();
                            max = 0;
                            finish();
                        } else
                        {
                            String newStr = drr.get(page).substring(
                                    drr.get(page).lastIndexOf("/") + 1,
                                    drr.get(page).lastIndexOf("."));
                            bmp.remove(page);
                            drr.remove(page);
                            del.add(newStr);
                            max--;
                            viewpager.removeAllViews();
                            listViews.remove(page);
                            adapter.setListViews(listViews);
                            adapter.notifyDataSetChanged();
                            for (int i = 0; i < del.size(); i++)
                            {
                                FileUtils.delFile(del.get(i) + ".jpg", FileUtils.fabu_cacheFolder);
                            }
                        }
                        BKBitmap.bmp = bmp;
                        BKBitmap.drr = drr;
                        BKBitmap.max = max;
                        tvTitle.setText((1 + page) + "/" + max);
                    }
                });
                dialog.show();
                break;
        }
    }

    /**
     * 图片适配器
     */
    class MyPageAdapter extends PagerAdapter
    {

        private ArrayList<View> listViews;
        private int size;

        /**
         * 构造函数
         *
         * @param listViews
         */
        public MyPageAdapter(ArrayList<View> listViews)
        {

            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        /**
         * 自定义方法
         * 用于添加数据
         *
         * @param listViews
         */
        public void setListViews(ArrayList<View> listViews)
        {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        @Override
        public int getCount()
        {
            // TODO Auto-generated method stub
            return size;
        }

        @Override
        public int getItemPosition(Object object)
        {
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public void destroyItem(View container, int position, Object object)
        {

            ((ViewPager) container).removeView(listViews.get(position % size));
        }

        @Override
        public Object instantiateItem(View container, int position)
        {
            try
            {
                ((ViewPager) container).addView(listViews.get(position % size), 0);

            } catch (Exception e)
            {
            }
            return listViews.get(position % size);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }

    @Override
    public void onSuccess(String responseStr, int requestCodes) {
    }

    @Override
    public void onFailure(String responseStr, int requestCode) {
    }
}
