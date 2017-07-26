package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Pinglun {

    /**
     * user : user
     */

    private List<ZanBean> zan;
    /**
     * name : 刘小明(爸)
     * text : 视频不错,老师辛苦了
     */

    private List<PinglunBean> pinglun;

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
        private String name;
        private String text;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
