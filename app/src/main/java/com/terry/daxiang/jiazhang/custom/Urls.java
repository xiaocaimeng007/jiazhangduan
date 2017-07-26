package com.terry.daxiang.jiazhang.custom;

import android.text.TextUtils;

/**
 * Created by fulei on 16/11/8.
 */

public class Urls {
    //base url
    public static String URL_APP_HOST = "http://dx.sitemn.com/ser/familys.ashx";
    //头像host url
    public static String URL_AVATAR_HOST = "http://dx.sitemn.com";

    /**
     * 登录url
     * @param username
     * @param password
     * @return
     */
    public static String getLogin(String username , String password , String token){
        return "?action=doLogin&username="+username+"&password="+password+"&token="+token;
    }

    /**
     * 获取各种文章列表
     * @param aid （52通知公告 62精彩瞬间 63每日食谱 64课程计划 65园所安全 67大象	FM  68互动投诉）
     * @return
     */
    public static String getArticlelists(String aid, String uid){
        return "?action=getNewsList&aid="+aid+"&uid="+uid;
    }

    /**
     * 上拉，下拉刷新数据
     * @param aid
     * @param uid 用户id
     * @param page 分页
     * @param maxid 当前列表最头部数据的id。(上拉加载数据不用传)
     * @return
     */
    public static String getArticlelistsRefresh(String aid , String uid , String page , String maxid){
        String host = "?action=getNewsList&aid="+aid+"&uid="+uid+"&page="+page;
        if (!TextUtils.isEmpty(maxid)){
            host +="&maxid="+maxid;
        }

        return host;
    }

    /**
     * 特色班级
     * @param aid
     * @param uid
     * @param page
     * @param maxid
     * @return
     */
    public static String getTSB(String aid , String uid , String page , String maxid){
        String host = "?action=getTSB&aid="+aid+"&uid="+uid+"&page="+page;
        if (!TextUtils.isEmpty(maxid)){
            host +="&maxid="+maxid;
        }

        return host;
    }

    /**
     * 是否有新帖子
     * @param aid
     * @param maxid
     * @return
     */
    public static String getNewsNewsCount(String aid , String maxid){
        String host = "?action=getNewsNewsCount&aid="+aid+"&maxid="+maxid;

        return host;
    }

    /**
     * 获取内容详情
     * @param cid 文章id
     * @return
     */
    public static String getArticleContent(String cid , String uid ){
        return "?action=getNewsContent&cid="+cid+"&uid="+uid;
    }

    /**
     * 获取链接
     * @param cid
     * @return
     */
    public static String getArticleUrl(String cid){
        return "?action=getNewsUrl&cid="+cid;
    }


    /**
     * 根据时间搜索用药情况
     * @param uid
     * @param start_date
     * @param end_date
     * @return
     */
    public static String getDrugSearch(String uid , String start_date , String end_date){
        return "?action=getDrugSearch&uid="+ uid +"&data1="+start_date+"&data2="+end_date;
    }

    /**
     * 用药详情
     * @param id 用药id
     * @return
     */
    public static String getDrugDetail(String id){
        return "?action=getDrugDetail&id="+id;
    }

    /**
     * 获取发现
     * @return
     */
    public static String getFindMain(){
        return "?action=getFind6Class";
    }

    /**
     * 获取子发现
     * @return
     */
    public static String getFindChild(String aid){
        return "?action=getFind6ChildClass&aid="+aid;
    }

    /**
     * 收藏
     * @param uid
     * @param type 1为收藏
     * @return
     */
    public static String getFavorite(String uid ,String type){
        return "?action=getFavorite&uid="+uid+"&type="+type;
    }

    /**
     * 关注
     * @param uid
     * @param page
     * @return
     */
    public static String getUNSCList(String uid ,String page , String maxid){
        String host = "?action=getUNSCList&uid="+uid+"&page="+page;
        if (!TextUtils.isEmpty(maxid)){
            host +="&maxid="+maxid;
        }
        return host;
    }

    /**
     * 热门
     * @param page
     * @return
     */
    public static String geHotList(String page , String maxid){
        String host = "?action=geHotList&page="+page;
        if (!TextUtils.isEmpty(maxid)){
            host +="&maxid="+maxid;
        }
        return host;
    }

    /**
     * 监控管理
     * @param uid
     * @return
     */
    public static String getCameraJK(String uid){
        return "?action=getCamera&uid="+uid;
    }

    /**
     * 获取视频数据来源
     * @param uid
     * @return
     */
    public static String CameraPosition(String uid){
        return "?action=getCameraPosition&uid="+uid;
    }

    /**
     * 考勤管理
     * @param uid
     * @param month
     * @return
     */
    public static String getLeaveReport(String uid , String month){
        return "?action=getLeaveReport&uid="+uid+"&month="+month;
    }

    /**
     * 事假列表
     * @param uid
     * @return
     */
    public static String getLeaveList(String uid){
        return "?action=getLeaveList&uid="+uid;
    }

   /**
     * 上拉，下拉刷新数据
     * @param uid 用户id
     * @param page 分页
     * @param maxid 当前列表最头部数据的id。(上拉加载数据不用传)
     * @return
     */
    public static String getChildCZRZ(String uid , String page , String maxid){
        String host = "?action=getChildCZRZ&uid="+uid+"&page="+page;
        if (!TextUtils.isEmpty(maxid)){
            host +="&maxid="+maxid;
        }

        return host;
    }

    /**
     * 行为记录--天
     * @param uid
     * @return
     */
    public static String getChildDayActionReport(String uid){
        return "?action=getChildDayActionReport&uid="+uid;
    }

    /**
     * 行为记录--周
     * @param uid
     * @return
     */
    public static String getChildWeekActionReport(String uid){
        return "?action=getChildWeekActionReport&uid="+uid;
    }

    /**
     * 行为记录--月
     * @param uid
     * @return
     */
    public static String getChildMonthActionReport(String uid){
        return "?action=getChildMonthActionReport&uid="+uid;
    }

    /**
     * 验证码
     * @param mobile
     * @return
     */
    public static String sendgetRegSMG(String mobile){
        return "?action=sendgetRegSMG&mobile="+mobile;
    }

    /**
     * 验证码
     * @param mobile
     * @return
     */
    public static String sendgetPasswordSMG(String mobile){
        return "?action=sendgetPasswordSMG&mobile="+mobile;
    }

    /**
     * 心跳检查帐号
     * @return
     */
    public static String doCheckLoginState(String uid){
        return "?action=doCheckLoginState&uid="+uid;
    }

}
