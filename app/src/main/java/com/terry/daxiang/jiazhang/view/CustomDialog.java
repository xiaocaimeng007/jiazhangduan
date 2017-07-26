package com.terry.daxiang.jiazhang.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;

public class CustomDialog {

	Context context;
	View parent;
	View view;
	PopupWindow popupWindow;
	Button btn_sure, btn_cancel;
	View bk_btn_span;
	TextView dialog_title;
	EditText tv_msg;
	boolean isEditText;
	String msg = "";
	private OnClickListenerForEdit onClickListenerForEdit;
	public CustomDialog(Context context, View parent, String msg, boolean isEditText) {
		this.context = context;
		this.parent = parent;
		this.msg = msg;
		this.isEditText = isEditText;
		if (context == null) {
			return;
		}
		init();
	}
	public void setOnClickListenerForEdit(OnClickListenerForEdit onClickListenerForEdit){
		this.onClickListenerForEdit = onClickListenerForEdit;
	}
	public void setMsg(String msg1) {
		if (context == null) {
			return;
		}
		tv_msg.setText(msg1);
	}
	public void setOnlyShowSureBtn() {
		if (context == null) {
			return;
		}
		btn_sure.setVisibility(View.VISIBLE);
		btn_cancel.setVisibility(View.GONE);
		bk_btn_span.setVisibility(View.GONE);
	}
	public void setOnlyShowCancelBtn() {
		if (context == null) {
			return;
		}
		btn_sure.setVisibility(View.GONE);
		btn_cancel.setVisibility(View.VISIBLE);
		bk_btn_span.setVisibility(View.GONE);
	}
	public void setBtnText(String sureText, String cancelText) {
		if (context == null) {
			return;
		}
		btn_sure.setText(sureText);
		btn_cancel.setText(cancelText);
	}
	@SuppressWarnings("deprecation")
	private void init() {
		view = LayoutInflater.from(context).inflate(R.layout.bk_dialog, null);
		dialog_title = (TextView) view.findViewById(R.id.bk_dialog_title);
		btn_sure = (Button) view.findViewById(R.id.bk_btn_sure);
		btn_cancel = (Button) view.findViewById(R.id.bk_btn_cancel);
		bk_btn_span = view.findViewById(R.id.bk_btn_span);
		tv_msg = (EditText) view.findViewById(R.id.bk_tv_msg);
		dialog_title.setText(msg);
		if (isEditText) {
			tv_msg.setVisibility(View.VISIBLE);
		} else {
			tv_msg.setVisibility(View.GONE);
		}
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		};

		btn_sure.setOnClickListener(clickListener);
		btn_cancel.setOnClickListener(clickListener);

		// 生成popupwindow
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		popupWindow = new PopupWindow(view, dm.widthPixels, dm.heightPixels);
		popupWindow.setAnimationStyle(R.style.dialogfade);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
	}

	/**
	 * 默认是文本，可以设置密码，数字等
	 * @param type
     */
	public void setEditTextType(int type){
		tv_msg.setInputType(type);
	}

	public void setOnSureClickListener(final OnClickListener clickListener) {
		if (context == null) {
			return;
		}
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isEditText) {
					if(onClickListenerForEdit != null){
						onClickListenerForEdit.onClick(arg0, tv_msg.getText().toString());
					}
				} else {
					if (clickListener != null) {
						clickListener.onClick(arg0);
					}
				}
				dismiss();
			}
		});

	}

	public void setOnCancelClickListener(final OnClickListener clickListener) {
		if (context == null) {
			return;
		}
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (clickListener != null) {
					clickListener.onClick(arg0);
				}
				dismiss();
			}
		});
	}

	public abstract static interface OnClickListenerForEdit {
		public abstract void onClick(View arg0, String str);
	}

	public void setOnDismissListener(OnDismissListener dismissListener) {
		if (context == null) {
			return;
		}
		popupWindow.setOnDismissListener(dismissListener);
	}

	public void dismiss() {
		if (context == null) {
			return;
		}
		popupWindow.dismiss();
	}

	public void show() {
		if (context == null) {
			return;
		}
		try {
			popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
