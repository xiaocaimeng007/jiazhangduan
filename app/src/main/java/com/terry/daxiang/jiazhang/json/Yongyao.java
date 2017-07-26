package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class Yongyao {

    /**
     * time : 2016-6-20
     * mingcheng : 少儿咳喘口服液
     * shuliang : 2支
     * songyaoren : 1
     */

    private List<YongyaoBean> data;

    public List<YongyaoBean> getData() {
        return data;
    }

    public void setData(List<YongyaoBean> data) {
        this.data = data;
    }

    public static class YongyaoBean {
        private String time;
        private String mingcheng;
        private String shuliang;
        private String songyaoren;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMingcheng() {
            return mingcheng;
        }

        public void setMingcheng(String mingcheng) {
            this.mingcheng = mingcheng;
        }

        public String getShuliang() {
            return shuliang;
        }

        public void setShuliang(String shuliang) {
            this.shuliang = shuliang;
        }

        public String getSongyaoren() {
            return songyaoren;
        }

        public void setSongyaoren(String songyaoren) {
            this.songyaoren = songyaoren;
        }
    }
}
