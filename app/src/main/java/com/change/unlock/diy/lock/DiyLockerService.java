package com.change.unlock.diy.lock;

import android.content.Context;
import android.content.Intent;

import com.change.unlock.lock.services.LockerService;
import com.change.unlock.lock.ui.activities.LockerActivity;

/**
 * 锁屏锁屏监听服务
 * @author jone.sun on 2015/5/6.
 */
public class DiyLockerService extends LockerService {

    @Override
    public void stopSelfServer() {
        stopSelf();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void startLockerActivity() {
        startLockerActivity(DiyLockerService.this);
        closeSystemLock();
    }

    public static void startLockerActivity(Context context) {
        Intent lockIntent = new Intent(context, DiyLockerMainActivity.class);
        lockIntent.setAction(LockerActivity.ACTION_START_LOCKER_SERVICE);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(lockIntent);
    }

}
