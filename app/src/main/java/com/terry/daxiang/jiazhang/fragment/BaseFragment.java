package com.terry.daxiang.jiazhang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.terry.daxiang.jiazhang.activity.HomeActivity;
import com.terry.daxiang.jiazhang.custom.ResponseCallback;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/4.
 */
public abstract class BaseFragment extends Fragment implements ResponseCallback {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public abstract View initView();

    public abstract void init();

    public void toast(String s) {
        ((HomeActivity) getActivity()).toast(s);
    }

    public void startActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    public void startActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }
}
