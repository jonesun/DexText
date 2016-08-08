package com.change.unlock.lock.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.change.unlock.lock.services.LockerService;
import com.change.unlock.lock.ui.views.LockLayer;

import java.util.ArrayList;
import java.util.List;

/**
 * 锁屏页面
 *
 * @author jone.sun on 2015/5/6.
 */
public abstract class LockerActivity extends Activity {
    public static final String ACTION_START_LOCKER_SERVICE = "com.tpad.activity.IWATCH_LOCKER_ACTIVITY";
    public final static String ACTION_RECEIVED_UNLOCKED = "ki.tp.action.broadcast.UNLOCKED"; //天天锁屏解锁
    public static int MSG_UN_LOCK = 0x123;// 解锁
    private LockLayer lockLayer;
    private LockerHandler lockerHandler = new LockerHandler();

    public abstract int getRes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(this, getRes(), null);
        initView(rootView);
        lockLayer = LockLayer.getInstance(this);
        lockLayer.setLockView(rootView);// 设置要展示的页面
        lockLayer.lock();// 开启锁屏
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        List<String> broadcastActionList = addRegisterReceiverActions();
        if (broadcastActionList != null && broadcastActionList.size() > 0) {
            IntentFilter intentFilter = new IntentFilter();
            for (String action : broadcastActionList) {
                intentFilter.addAction(action);
            }
            registerReceiver(broadcastReceiver, intentFilter);
        }

        LockerService.startLockerService(LockerActivity.this);
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * 初始化页面布局
     *
     * @param rootView
     */
    public abstract void initView(View rootView);

    /**
     * 添加需注册的广播actions
     *
     * @return
     */
    public List<String> addRegisterReceiverActions() {
        return new ArrayList<>();
    }

    /**
     * 收到广播
     *
     * @param context
     * @param intent
     */
    public void onBroadcastReceive(Context context, Intent intent) {

    }

    public LockerHandler getLockerHandler() {
        return lockerHandler;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void unlock(View view) {
        lockerHandler.handleMessage(lockerHandler.obtainMessage(MSG_UN_LOCK));
    }

    @SuppressLint("HandlerLeak")
    class LockerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (MSG_UN_LOCK == msg.what) {
                try{
                    if (lockLayer != null) {
                        lockLayer.unlock();
                        lockLayer = null;
                    }
                }catch (Exception e){
                    Log.e("locker", "unlock", e);
                }finally {
                    finish();
                }

            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceive(context, intent);
        }
    };

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:  //挂电话
                    Log.e("locker", "电话状态: 空闲");
                    break;
                case TelephonyManager.CALL_STATE_RINGING: //响铃
                    Log.e("locker", "电话状态: 响铃--解锁");
                    if (lockerHandler != null) {
                        lockerHandler.sendEmptyMessage(MSG_UN_LOCK);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: //接起电话
                    Log.e("locker", "电话状态: 接电话");
                    break;
            }
        }
    };

    /***
     * 解锁
     */
    public void unlock(){
        sendBroadcast(new Intent(ACTION_RECEIVED_UNLOCKED));
        if(lockerHandler != null){
            lockerHandler.handleMessage(lockerHandler.obtainMessage(MSG_UN_LOCK));
        }else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
