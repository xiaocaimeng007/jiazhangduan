package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * Created by chen_fulei on 2016/12/20.
 */

public class LeaveReportBean {
    private String Month;//: "2016年11月 考勤",
    private String Chuqin;//: "25",
    private String Shijia;//: "0",
    private String Bingjia;//: "0"

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getChuqin() {
        return Chuqin;
    }

    public void setChuqin(String chuqin) {
        Chuqin = chuqin;
    }

    public String getShijia() {
        return Shijia;
    }

    public void setShijia(String shijia) {
        Shijia = shijia;
    }

    public String getBingjia() {
        return Bingjia;
    }

    public void setBingjia(String bingjia) {
        Bingjia = bingjia;
    }
}
