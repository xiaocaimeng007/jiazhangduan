package com.terry.daxiang.jiazhang.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.custom.AsyncBitmapByVideo;
import com.terry.daxiang.jiazhang.utils.ToastUtil;
import com.terry.daxiang.jiazhang.view.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图像查看器
 * @author honey_chen
 *
 */
public class PhotosGalleryActivity extends BaseActivity{

	@BindView(R.id.ll_back)
	LinearLayout llBack;
	@BindView(R.id.tv_title)
	TextView tvTitle;
	@BindView(R.id.viewpager)
	CustomViewPager viewPager;

	private ArrayList<String> imgUrl = new ArrayList<String>();
	private int position = 0;
	//是否发布的视频
	private boolean isFabu = false;

	/**
	 * 数据适配器
	 */
	private PhotosPageAdapter adapter;
	private AsyncBitmapByVideo asyncBitmapLoader;

	/**
	 * 要显示的View
	 */
	private ArrayList<View> listView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos_gallery);
		ButterKnife.bind(this);
		setTextview(tvTitle , "查看大图");
		initData();
		asyncBitmapLoader = new AsyncBitmapByVideo(this);
		//初始化图片列表
		initPhotoViews();

		adapter = new PhotosPageAdapter(listView);// 构造adapter
		viewPager.setAdapter(adapter);// 设置适配器
		viewPager.setCurrentItem(position);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int arg0) {
				int childCount = viewPager.getChildCount();
				// 当图片滑动到下一页后，遍历当前所有加载过的PhotoView，恢复所有图片的默认状态大小
				for (int i = 0; i < childCount; i++) {
					View childAt = viewPager.getChildAt(i);
					try {
						if (childAt != null && childAt instanceof PhotoView) {
							PhotoView photoView = (PhotoView) childAt;// 得到viewPager里面的页面
							PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);// 把得到的photoView放到这个负责变形的类当中
							mAttacher.getDisplayMatrix().reset();// 得到这个页面的显示状态，然后重置为默认状态或者做其他的操作
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				position = arg0;
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	private void initData(){
		try {
			Bundle bundle = getIntent().getBundleExtra("bundle");
			if(null != bundle){
				position = bundle.getInt("position", 0);
				isFabu = bundle.getBoolean("fabu" , false);
				imgUrl = (ArrayList<String>) bundle.getSerializable("imgUrl");
			}
		}catch (Exception e){

		}
	}

	private void initPhotoViews(){
		if (listView == null){
			listView =  new ArrayList<View>();
		}

		try {
			if (imgUrl != null) {
				for (final String pathUrl : imgUrl) {
					if (pathUrl.endsWith(".mp4")){
						final ImageView imageView = new ImageView(this);
						imageView.setBackgroundResource(R.color.color_eee);
						imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

						if (isFabu){
							imageView.setImageResource(R.mipmap.icon_shipin_click);
							Glide.with(getApplicationContext()).load(pathUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
								@Override
								public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
									if (resource != null){
										imageView.setBackgroundDrawable(new BitmapDrawable(resource));
									}
								}
							});
						}else {
							imageView.setImageResource(R.mipmap.icon_shipin_click);
							Bitmap bitmap = asyncBitmapLoader.LoadBitmapByVideo(pathUrl, imageView, new AsyncBitmapByVideo.ImageCallback() {
								@Override
								public void imageLoaded(Bitmap b, ImageView imageview, String imageUrl) {
									imageview.setBackgroundDrawable(new BitmapDrawable(b));
								}
							});
							if (bitmap != null){
								imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
							}
						}
						imageView.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Uri uri = Uri.parse(pathUrl);
								//调用系统自带的播放器
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setDataAndType(uri, "video/mp4");
								startActivity(intent);
							}
						});
						listView.add(imageView);
					}else{
						PhotoView imageView = new PhotoView(this);
						imageView.setBackgroundResource(R.color.color_eee);
						imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
						Glide.with(getApplicationContext()).load(pathUrl).into(imageView);
						listView.add(imageView);
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        finish();
    }


	private class PhotosPageAdapter extends PagerAdapter {
		ArrayList<View> itmeViews =new ArrayList<View>();

		public PhotosPageAdapter(ArrayList<View> views){
			this.itmeViews = views;
		}

		@Override
		public int getCount() {
			return itmeViews == null ? 0 : itmeViews.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(itmeViews.get(position));

			return itmeViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(itmeViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

}
