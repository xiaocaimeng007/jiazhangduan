package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class Kaoqin {

    /**
     * time : 2016年4月考勤
     * chuqin : 27
     * shijia : 2
     * bingjia : 1
     */

    private List<KaoqinBean> data;

    public List<KaoqinBean> getData() {
        return data;
    }

    public void setData(List<KaoqinBean> data) {
        this.data = data;
    }

    public static class KaoqinBean {
        private String time;
        private String chuqin;
        private String shijia;
        private String bingjia;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getChuqin() {
            return chuqin;
        }

        public void setChuqin(String chuqin) {
            this.chuqin = chuqin;
        }

        public String getShijia() {
            return shijia;
        }

        public void setShijia(String shijia) {
            this.shijia = shijia;
        }

        public String getBingjia() {
            return bingjia;
        }

        public void setBingjia(String bingjia) {
            this.bingjia = bingjia;
        }
    }
}
