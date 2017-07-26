package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * 获取老师电话本
 * Created by chen_fulei on 2016/11/11.
 */

public class TeaherPhoneBookBean {
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
        private int id;//: 104,
        private String grade;//: "大一班",
        private ArrayList<Teachers> teachers;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public ArrayList<Teachers> getTeachers() {
            return teachers;
        }

        public void setTeachers(ArrayList<Teachers> teachers) {
            this.teachers = teachers;
        }

        public static class Teachers{
            private String teachername;//: "heyun",
            private String teachertitle;//: "主班",
            private String teachertel;//: "17655554444",
            private String avatar;//: "/upload/201610/08/201610081519047253.png"

            public String getTeachername() {
                return teachername;
            }

            public void setTeachername(String teachername) {
                this.teachername = teachername;
            }

            public String getTeachertitle() {
                return teachertitle;
            }

            public void setTeachertitle(String teachertitle) {
                this.teachertitle = teachertitle;
            }

            public String getTeachertel() {
                return teachertel;
            }

            public void setTeachertel(String teachertel) {
                this.teachertel = teachertel;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }

}
