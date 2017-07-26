package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * Created by chen_fulei on 2016/12/13.
 */

public class FindBean {
    private int status;
    private String message;
    private ArrayList<Data> data = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public static class Data{
        private int id ;//: 54,
        private String title;//: "儿童阅读",
        private String img_url;//: "/upload/201611/10/201611101534149924.png"

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
