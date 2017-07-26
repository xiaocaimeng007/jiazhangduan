package com.terry.daxiang.jiazhang.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.terry.daxiang.jiazhang.json.DaxiangFM.DaxiangFMBean;

/**
 * Created by Administrator on 2016/9/19.
 */
public class DaxiangFMEntity extends MultiItemEntity {
    public static final int YINPIN = 0;
    public static final int SHIPIN = 1;
    public DaxiangFMBean fMBean;

    public DaxiangFMBean getfMBean() {
        return fMBean;
    }

    public void setfMBean(DaxiangFMBean fMBean) {
        this.fMBean = fMBean;
    }
}
