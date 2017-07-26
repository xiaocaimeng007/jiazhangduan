package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Tuijian {

    /**
     * name : 奇智奇材
     * icon :
     * url :
     */

    private List<TuijianBean> data;

    public List<TuijianBean> getData() {
        return data;
    }

    public void setData(List<TuijianBean> data) {
        this.data = data;
    }

    public static class TuijianBean {
        private String name;
        private String icon;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
