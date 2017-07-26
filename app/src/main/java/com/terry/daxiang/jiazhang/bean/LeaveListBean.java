package com.terry.daxiang.jiazhang.bean;

import java.util.ArrayList;

/**
 * Created by chen_fulei on 2016/12/20.
 */

public class LeaveListBean {
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
        private int id; //: 564,
        private String leave_type;//: "事假",
        private String leave_start;//: "2016-12-15",
        private String leave_enddate;//: "2016-12-20",
        private String content;//: "达到国际先进水平、发"

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLeave_type() {
            return leave_type;
        }

        public void setLeave_type(String leave_type) {
            this.leave_type = leave_type;
        }

        public String getLeave_start() {
            return leave_start;
        }

        public void setLeave_start(String leave_start) {
            this.leave_start = leave_start;
        }

        public String getLeave_enddate() {
            return leave_enddate;
        }

        public void setLeave_enddate(String leave_enddate) {
            this.leave_enddate = leave_enddate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
