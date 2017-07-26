package com.terry.daxiang.jiazhang.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created by fulei on 17/2/13.
 */

public class TimeService extends Service {

    /**
     * 心跳 时间
     */
    public final static long TIMEOUT_APPUSERTIME = 30 * 1000; // 30秒

    /**
     * 闹钟管理
     */
    private AlarmManager alarmManager;

    private PendingIntent timeIntent;

    private CheckAccountReceiver checkAccountReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取闹钟管理
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }

        if (checkAccountReceiver == null){
            checkAccountReceiver = new CheckAccountReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(CheckAccountReceiver.ACTION_CHECKACCOUNT_DAXIANG);
            registerReceiver(checkAccountReceiver , filter);
        }

        if (timeIntent == null){
            timeIntent = PendingIntent.getBroadcast(this, 0x0003, new Intent(CheckAccountReceiver.ACTION_CHECKACCOUNT_DAXIANG), PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TIMEOUT_APPUSERTIME, timeIntent);
        }

        return Service.START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
