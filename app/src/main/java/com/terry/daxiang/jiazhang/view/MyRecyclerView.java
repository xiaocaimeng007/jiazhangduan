package com.terry.daxiang.jiazhang.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/9/15.
 */
public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

}
