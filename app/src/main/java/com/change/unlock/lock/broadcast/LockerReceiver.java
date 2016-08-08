package com.change.unlock.lock.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.change.unlock.lock.services.LockerService;

/**
 * 启动锁屏广播
 * @author jone.sun on 2015/4/7.
 */
public class LockerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LockerService.startLockerService(context);
        Log.e("LockerReceiver", intent.getAction() + "");
    }
}
