package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Chengzhang {

    /**
     * name : 刘老师
     * time : 2016.06.14
     * type : img
     * text : 宝宝今天表现不错,和其他小朋友做活动玩得很嗨
     * url : ["","",""]
     */

    private List<ChengzhangBean> data;

    public List<ChengzhangBean> getData() {
        return data;
    }

    public void setData(List<ChengzhangBean> data) {
        this.data = data;
    }

    public static class ChengzhangBean {
        private String name;
        private String time;
        private String type;
        private String text;
        private List<String> url;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getUrl() {
            return url;
        }

        public void setUrl(List<String> url) {
            this.url = url;
        }
    }
}
