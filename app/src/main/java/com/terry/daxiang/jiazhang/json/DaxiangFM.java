package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class DaxiangFM {


    /**
     * icon :
     * name : 刘老师
     * time : 2016.06.14
     * type : shipin
     * count : 2998
     * text : 西游记视频故事
     * url :
     * zan : [{"user":""},{"user":""}]
     * pinglun : [{"user":""},{"user":""},{"user":""},{"user":""},{"user":""}]
     */

    private List<DaxiangFMBean> data;

    public List<DaxiangFMBean> getData() {
        return data;
    }

    public void setData(List<DaxiangFMBean> data) {
        this.data = data;
    }

    public static class DaxiangFMBean {
        private String icon;
        private String name;
        private String time;
        private String type;
        private String count;
        private String text;
        private String url;
        /**
         * user :
         */

        private List<ZanBean> zan;
        /**
         * user :
         */

        private List<PinglunBean> pinglun;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<ZanBean> getZan() {
            return zan;
        }

        public void setZan(List<ZanBean> zan) {
            this.zan = zan;
        }

        public List<PinglunBean> getPinglun() {
            return pinglun;
        }

        public void setPinglun(List<PinglunBean> pinglun) {
            this.pinglun = pinglun;
        }

        public static class ZanBean {
            private String user;

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }
        }

        public static class PinglunBean {
            private String user;

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }
        }
    }
}
