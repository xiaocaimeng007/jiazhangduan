package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class Jiankong {

    /**
     * weizhi : 大象顺义东区园所(小一班)
     * zhuangtai : 正常
     * shijian : 2016年6月1日 - 2016年6月30日
     */

    private List<JiankongBean> data;

    public List<JiankongBean> getData() {
        return data;
    }

    public void setData(List<JiankongBean> data) {
        this.data = data;
    }

    public static class JiankongBean {
        private String weizhi;
        private String zhuangtai;
        private String shijian;

        public String getWeizhi() {
            return weizhi;
        }

        public void setWeizhi(String weizhi) {
            this.weizhi = weizhi;
        }

        public String getZhuangtai() {
            return zhuangtai;
        }

        public void setZhuangtai(String zhuangtai) {
            this.zhuangtai = zhuangtai;
        }

        public String getShijian() {
            return shijian;
        }

        public void setShijian(String shijian) {
            this.shijian = shijian;
        }
    }
}
