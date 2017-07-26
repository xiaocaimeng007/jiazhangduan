package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * 用药详情
 *
 * Created by chen_fulei on 2016/11/11.
 */

public class DrugDetailBean {

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
        private int id;//: 120,
        private String title;//: "泻立停",
        private String drugs_child;//: "小花",
        private String grade_name;//: "大二班",
        private String drugs_date;//: "2016-10-20",
        private String drugs_time;//: "午饭后",
        private String drugs_quantum;//: "3片",
        private String drugs_sender;//: "王老师",
        private String drugs_reason;//: "拉肚子",
        private String file_path;//: "/upload/201610/19/201610191517378998.mp4"

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

        public String getDrugs_child() {
            return drugs_child;
        }

        public void setDrugs_child(String drugs_child) {
            this.drugs_child = drugs_child;
        }

        public String getGrade_name() {
            return grade_name;
        }

        public void setGrade_name(String grade_name) {
            this.grade_name = grade_name;
        }

        public String getDrugs_date() {
            return drugs_date;
        }

        public void setDrugs_date(String drugs_date) {
            this.drugs_date = drugs_date;
        }

        public String getDrugs_time() {
            return drugs_time;
        }

        public void setDrugs_time(String drugs_time) {
            this.drugs_time = drugs_time;
        }

        public String getDrugs_quantum() {
            return drugs_quantum;
        }

        public void setDrugs_quantum(String drugs_quantum) {
            this.drugs_quantum = drugs_quantum;
        }

        public String getDrugs_sender() {
            return drugs_sender;
        }

        public void setDrugs_sender(String drugs_sender) {
            this.drugs_sender = drugs_sender;
        }

        public String getDrugs_reason() {
            return drugs_reason;
        }

        public void setDrugs_reason(String drugs_reason) {
            this.drugs_reason = drugs_reason;
        }

        public String getFile_path() {
            return file_path;
        }

        public void setFile_path(String file_path) {
            this.file_path = file_path;
        }
    }

}
