package com.terry.daxiang.jiazhang.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.ZoomButtonsController;

public class BKWebView extends WebView {

	//private Context context;
	private ZoomButtonsController zoomController = null;

	public BKWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//this.context = context;
		disableZoomController();
	}

	public BKWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.context = context;
		disableZoomController();
	}

	public BKWebView(Context context) {
		super(context);
		//this.context = context;
		disableZoomController();
	}

	@SuppressLint({ "NewApi" })
	private void disableZoomController() {
		if (Build.VERSION.SDK_INT >= 11) {
			getSettings().setBuiltInZoomControls(true);
			getSettings().setDisplayZoomControls(false);
			return;
		}
		getControlls();
	}

	private void getControlls() {
		try {
			this.zoomController = ((ZoomButtonsController) Class
					.forName("android.webkit.WebView")
					.getMethod("getZoomButtonsController", new Class[0])
					.invoke(this, new Object[0]));
			return;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		super.onTouchEvent(paramMotionEvent);
		if (this.zoomController != null)
			this.zoomController.setVisible(false);
		return true;
	}
}
