package com.terry.daxiang.jiazhang.json;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Zhuanji {

    /**
     * name : 奇智奇材-社会(小班)
     * text : 培养小朋友对社会的初步感知和交往能力
     * count : 20
     */

    private List<ZhuanjiBean> data;

    public List<ZhuanjiBean> getData() {
        return data;
    }

    public void setData(List<ZhuanjiBean> data) {
        this.data = data;
    }

    public static class ZhuanjiBean {
        private String name;
        private String text;
        private String count;

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

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
