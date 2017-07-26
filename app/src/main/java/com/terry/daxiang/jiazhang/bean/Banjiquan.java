package com.terry.daxiang.jiazhang.bean;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.terry.daxiang.jiazhang.R;
import com.terry.daxiang.jiazhang.activity.HomeActivity;
import com.hyphenate.easeui.utils.SharedPrefUtils;

/**
 * Created by Administrator on 2016/9/15.
 */
public class Banjiquan {
    //= { "通知通告", "精彩瞬间", "每日食谱", "课程计划", "园所安全" ,"互动投诉"}
    private final String[] fenlei ;
    private int current;
    private HomeActivity activity;

    public Banjiquan(Activity activity) {
        this.activity = (HomeActivity) activity;
        fenlei = new String[]{activity.getString(R.string.string_home_bangjiquan_togou),activity.getString(R.string.string_home_bangjiquan_jingcai),activity.getString(R.string.string_home_bangjiquan_shipu),activity.getString(R.string.string_home_bangjiquan_anquan) };
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
        activity.tvTitle1.setText(getFenlei());
        activity.llSend.setVisibility(isShow() ? View.VISIBLE : View.GONE);
    }

    public boolean isShow() {
        if (current ==1) {
            return true;
        } else {
            return false;
        }
    }

    public String getFenlei() {
        return fenlei[current];
    }

    public String getTypeId() {
        String type = "52";
        switch (getCurrent()) {
            case 0:
                type ="52";
                break;
            case 1:
                type ="62";
                break;
            case 2:
                type ="63";
                break;

            case 3:
                type ="65";
                break;
        }

        return type;
    }

    public void setShareMaxID(Context context , String maxid){
        try {
            switch (getCurrent()) {
                case 0:
                    SharedPrefUtils.setString(context , SharedPrefUtils.APP_BANJIQUAN_TONGZHI_MAX , maxid);
                    break;
                case 1:
                    SharedPrefUtils.setString(context , SharedPrefUtils.APP_BANJIQUAN_JINGCAI_MAX , maxid);
                    break;
                case 2:
                    SharedPrefUtils.setString(context , SharedPrefUtils.APP_BANJIQUAN_SHIPU_MAX , maxid);
                    break;

                case 3:
                    SharedPrefUtils.setString(context , SharedPrefUtils.APP_BANJIQUAN_YUANSUO_MAX, maxid);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}