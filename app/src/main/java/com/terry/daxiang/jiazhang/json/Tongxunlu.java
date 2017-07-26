package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class Tongxunlu {

    /**
     * icon :
     * name : 刘老师
     * zhiwei : 主班
     * phone : 123456789
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String icon;
        private String name;
        private String zhiwei;
        private String phone;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getZhiwei() {
            return zhiwei;
        }

        public void setZhiwei(String zhiwei) {
            this.zhiwei = zhiwei;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
