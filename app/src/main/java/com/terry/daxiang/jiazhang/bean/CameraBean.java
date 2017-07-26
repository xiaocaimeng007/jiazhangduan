package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * Created by chen_fulei on 2016/12/19.
 */

public class CameraBean {
    private int status;
    private String message;
    private ArrayList<Data> data;

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
        private int id; //: 226,
        private String title;//: "餐厅",
        private String statrt_date;//: "2017-01-01",
        private String end_date;//: "2017-01-12",
        private String author;//: "小红爸爸",
        private int authorid;//: 8,
        private String grade_name;//: "大一班",
        private int status;//: 0

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

        public String getStatrt_date() {
            return statrt_date;
        }

        public void setStatrt_date(String statrt_date) {
            this.statrt_date = statrt_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getAuthorid() {
            return authorid;
        }

        public void setAuthorid(int authorid) {
            this.authorid = authorid;
        }

        public String getGrade_name() {
            return grade_name;
        }

        public void setGrade_name(String grade_name) {
            this.grade_name = grade_name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
