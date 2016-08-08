package com.change.unlock.lock.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 锁屏监听服务基类
 *
 * @author loang 20150507
 */
public class BaseService extends Service {
    private static final String TAG = BaseService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    private void setForeground() {
        Notification note = new Notification(0, null, System.currentTimeMillis());
        note.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(42, note);
    }
}
