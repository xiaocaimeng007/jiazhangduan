package com.terry.daxiang.jiazhang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.terry.daxiang.jiazhang.R;

/**
 * 自定义加载圈圈
 *
 * Created by fulei on 16/11/9.
 */

public class CustomProgressBar extends Dialog {


    public CustomProgressBar(Context context){
        super(context , R.style.MyDialog);
    }

    public CustomProgressBar(Context context , int theme){
        super(context , theme);
    }

    private TextView txt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_progressbar);
        txt_login = (TextView) findViewById(R.id.txt_message);
        txt_login.setVisibility(View.GONE);
    }

    public void showText(String message){
        txt_login.setVisibility(View.VISIBLE);
        txt_login.setText(message);
    }
}
