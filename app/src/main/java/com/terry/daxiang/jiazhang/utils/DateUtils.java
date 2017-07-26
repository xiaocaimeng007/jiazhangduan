package com.terry.daxiang.jiazhang.utils;

import android.text.TextUtils;

import com.terry.daxiang.jiazhang.bean.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */

public class DateUtils {
    public static List<Date> getDate(long thisTime) {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(thisTime);
        Date date1 = new Date();
        date1.setTime(thisTime);
        date1.setText("今天");
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int count = 28 - day_of_week;
        //获取今天以前的日期
        for (int i = 1; i < day_of_week; i++) {
            Date date = new Date();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            long timeInMillis = calendar.getTimeInMillis();
            date.setText(day + "");
            date.setTime(timeInMillis);
            if (day == 1) {
                int month = calendar.get(Calendar.MONTH);
                date.setText(month + "月");
            }
            dates.add(date);
        }
        dates.add(date1);
        calendar.setTimeInMillis(thisTime);
        //获取今天以后的日期
        for (int i = 0; i < count; i++) {
            Date date = new Date();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            long timeInMillis = calendar.getTimeInMillis();
            date.setText(day + "");
            date.setTime(timeInMillis);
            if (day == 1) {
                int month = calendar.get(Calendar.MONTH);
                date.setText(month + "月");
            }
            dates.add(date);
        }
        return dates;
    }

    public static String formateDate(String date) {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(simpleDateFormat.parse(date));
        }catch (Exception e){

        }
        return date;
    }

    public static String formateDate(java.util.Date date) {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(date);
        }catch (Exception e){

        }
        return null;
    }

    public static String formateDate(java.util.Date date , String format) {
        try{
            if (TextUtils.isEmpty(format)){
                format ="yyyy-MM-dd";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        }catch (Exception e){

        }
        return null;
    }

    public static String formateDateByMoth(java.util.Date date) {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            return simpleDateFormat.format(date);
        }catch (Exception e){

        }
        return null;
    }

    public static String getTime(long currentTimeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(currentTimeMillis);
    }

//    public static String getTime(String currentTimeMillis) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
//        long l = 0;
//        try {
//            l = Long.parseLong(currentTimeMillis);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return dateFormat.format(l);
//    }

    public static String getTime(String currentTimeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        long l = 0;
        try {
            l = Long.parseLong(currentTimeMillis);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dateFormat.format(l);
    }

    /**
     * HH:mm
     * @return
     */
    public static String getTimeByDate(String format , java.util.Date date){
        try {
            if (TextUtils.isEmpty(format)){
                format = "HH:mm";
            }
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static long getStringToDate(String date){
        try {
            return getDateByTime(date).getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  -1;
    }

    public static java.util.Date getDateByTime(String date){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(date);
        }catch (Exception e){
        }
        return null;
    }

    /**
     * 比较两个日期
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date dt1 = df.parse(DATE1);
            java.util.Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            }else {
                return 0;
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static int daysBetween(String smdate,String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;

        try {
            cal.setTime(sdf.parse(smdate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            time2 = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }
}
