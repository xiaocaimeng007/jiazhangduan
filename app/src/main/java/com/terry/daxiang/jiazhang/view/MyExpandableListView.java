package com.terry.daxiang.jiazhang.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2016/9/15.
 */
public class MyExpandableListView extends ExpandableListView {
    public MyExpandableListView(Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public MyExpandableListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
