package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class BanjiquanJson {

    /**
     * icon :
     * name : 刘老师
     * time : 2016.06.14
     * fenlei : 通知通告
     * text : 今天宝宝表现很好,和同学做活动很嗨.
     * type : img
     * url : ["","",""]
     */

    private List<BanjiquanBean> data;

    public List<BanjiquanBean> getData() {
        return data;
    }

    public void setData(List<BanjiquanBean> data) {
        this.data = data;
    }

    public static class BanjiquanBean {
        private String icon;
        private String name;
        private String time;
        private String fenlei;
        private String text;
        private String type;
        private List<String> url;

        @Override
        public String toString() {
            return "BanjiquanBean{" +
                    "fenlei='" + fenlei + '\'' +
                    ", icon='" + icon + '\'' +
                    ", name='" + name + '\'' +
                    ", time='" + time + '\'' +
                    ", text='" + text + '\'' +
                    ", type='" + type + '\'' +
                    ", url=" + url +
                    '}';
        }

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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFenlei() {
            return fenlei;
        }

        public void setFenlei(String fenlei) {
            this.fenlei = fenlei;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getUrl() {
            return url;
        }

        public void setUrl(List<String> url) {
            this.url = url;
        }
    }
}
