package com.terry.daxiang.jiazhang.view.wheel;

import android.content.Context;
import android.graphics.Color;

import com.terry.daxiang.jiazhang.R;

/**
 *  -.- 类似IOS7选择器的模样
 * @author zl.lin
 *
 */
public class WheelViewiOS7 extends WheelView {

	public WheelViewiOS7(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setCenterDrawable(R.drawable.ios7_wheel_val);
		setWheelBackgroundColor(Color.WHITE);
		int colors[] = { Color.TRANSPARENT, Color.TRANSPARENT,
				Color.TRANSPARENT };
		setTopShadowDrawable(colors);
		setBottomShadowDrawable(colors);
	}

}
