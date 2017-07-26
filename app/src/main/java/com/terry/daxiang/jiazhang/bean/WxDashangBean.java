package com.terry.daxiang.jiazhang.bean;

/**
 * Created by fulei on 17/2/17.
 */

public class WxDashangBean {

    private String appid;//: "wx954b6c174b60aa11",
    private String partnerid;//: "1439553902",
    private String prepayid;//: "wx2017021701591159b67f52ea0179612456",
    private String noncestr ;//: "A97QViB9K43UO2TPRV6A",
    private String timestamp;//: "1487267952",
    private String sign;//: "68EF3E8D1D84BB5CAA7C6DE0426C8C8F"

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
