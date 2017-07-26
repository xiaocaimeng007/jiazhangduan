package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * Created by chen_fulei on 2016/12/13.
 */

public class FavoriteBean {
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
        private int id;//: 185,
        private int uid;//: 17,
        private int cid;//: 229,
        private String title;//: "小红帽历险记1",
        private int type;//: 1,
        private String adddate;//: "/Date(1479969103000)/",
        private String ext1;//: "/upload/userfile/20161123/20161123926b3124c13-bc16-453a-afba-1620a1ba310e.jpg",
        private String ext2;//: "",
        private String ext3;//: "",
        private String ext4;//: ""

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }

        public String getExt1() {
            return ext1;
        }

        public void setExt1(String ext1) {
            this.ext1 = ext1;
        }

        public String getExt2() {
            return ext2;
        }

        public void setExt2(String ext2) {
            this.ext2 = ext2;
        }

        public String getExt3() {
            return ext3;
        }

        public void setExt3(String ext3) {
            this.ext3 = ext3;
        }

        public String getExt4() {
            return ext4;
        }

        public void setExt4(String ext4) {
            this.ext4 = ext4;
        }
    }
}
