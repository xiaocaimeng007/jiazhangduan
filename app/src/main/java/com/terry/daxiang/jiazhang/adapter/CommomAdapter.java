package com.terry.daxiang.jiazhang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by happy on 2017/7/15.
 */

public abstract class CommomAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> data;

    public CommomAdapter(Context context, List<T> list) {
        this.context = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
