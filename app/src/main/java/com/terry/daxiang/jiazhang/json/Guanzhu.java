package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Guanzhu {

    /**
     * name : 奇智奇材-社会(小班)
     * text : 培养小朋友对社会的初步感知和交往能力
     * type : 0
     */

    private List<GuanzhuBean> data;

    public List<GuanzhuBean> getData() {
        return data;
    }

    public void setData(List<GuanzhuBean> data) {
        this.data = data;
    }

    public static class GuanzhuBean {
        private String name;
        private String text;
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
