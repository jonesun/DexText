package com.change.unlock.lock.services;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.change.unlock.diy.App;
import com.change.unlock.diy.lock.DiyLockerService;

import java.util.ArrayList;
import java.util.List;

/**
 * 锁屏监听服务
 *
 * @author jone.sun on 2015/5/6.
 */
public abstract class LockerService extends BaseService {
    private static final String TAG = LockerService.class.getSimpleName();
    public static final String ACTION_START_LOCKER_SERVICE = "com.tpad.service.IWATCH_LOCKER_SERVICE";
    public final static String ACTION_RECEIVED_STOP_SELF = "ki.tp.action.broadcast.IWATCH_LOCKSERVICE";

    private int phoneState = TelephonyManager.CALL_STATE_IDLE;
    private static final String SP_IS_START_SERVER = "isStartServer";

    @Override
    public IBinder onBind(Intent arg0) {
        return super.onBind(arg0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(!isStartServer()){
            //是否启动锁屏服务
            Log.e(TAG, "不需要启动锁屏服务" + getPackageName());
            stopSelfServer();
            return;
        }
        Log.e(TAG, "启动锁屏服务" + getPackageName());
        notifyOtherStopSelf();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        List<String> broadcastActionList = addRegisterReceiverActions();
        IntentFilter intentFilter = new IntentFilter();
        if (broadcastActionList != null && broadcastActionList.size() > 0) {
            for (String action : broadcastActionList) {
                intentFilter.addAction(action);
            }
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isStartServer()){
            //是否启动锁屏服务
            Log.e(TAG, "关闭锁屏服务" + getPackageName());
            stopSelfServer();
        }else {
            notifyOtherStopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void notifyOtherStopSelf(){
        Intent stop_self = new Intent(ACTION_RECEIVED_STOP_SELF);
        stop_self.putExtra("packageName", getPackageName());
        sendBroadcast(stop_self);
        Log.e(TAG, getPackageName() + "通知其他锁屏服务关闭: " + ACTION_RECEIVED_STOP_SELF);
    }

    public abstract void stopSelfServer();

    public static void startLockerService(Context context) {
        Intent intent = new Intent(context, DiyLockerService.class);
        intent.setAction(ACTION_START_LOCKER_SERVICE);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
//        Intent eintent = new Intent(ServiceUtils.createExplicitFromImplicitIntent(context, intent));
//        context.startService(eintent);
    }

    /**
     * 添加需注册的广播actions
     *
     * @return broadcastActionList
     */
    public List<String> addRegisterReceiverActions() {
        List<String> broadcastActionList = new ArrayList<>();
        broadcastActionList.add(Intent.ACTION_SCREEN_ON);
        broadcastActionList.add(Intent.ACTION_SCREEN_OFF);
        broadcastActionList.add(ACTION_RECEIVED_STOP_SELF);
        return broadcastActionList;
    }

    /**
     * 收到广播
     *
     * @param context
     * @param intent
     */
    public void onBroadcastReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON:
                onScreenStateChanged(true);
                break;
            case Intent.ACTION_SCREEN_OFF:
                onScreenStateChanged(false);
                break;
            case ACTION_RECEIVED_STOP_SELF:
                Log.e(TAG, "packageName: " + intent.getStringExtra("packageName"));
                if(intent.hasExtra("packageName")){
                    String packageName = intent.getStringExtra("packageName");
                    if(!packageName.equals(getPackageName())){
                        saveStartServicFlag("关闭收到广播packageName", false);
                        stopSelfServer();
                    }
                }else {
                    saveStartServicFlag("关闭收到广播", false);
                    stopSelfServer();
                }
                break;
        }
    }

    /**
     * 屏幕状态变化(亮暗屏)
     *
     * @param isOn
     */
    public void onScreenStateChanged(boolean isOn) {
//        Log.e(TAG, getPackageName() + "isOn: " + isOn + " ,phoneState: " + phoneState);
        if (phoneState == TelephonyManager.CALL_STATE_IDLE) { //电话处于空闲状态
            if (!isOn) {
                startLockerActivity();
            }
        }
    }

    /**
     * 电话状态变化
     *
     * @param state
     * @param incomingNumber
     */
    public void onPhoneStateChanged(int state, String incomingNumber) {
        Log.e("locker", "电话状态变化>> 从" + getPhoneStats(phoneState) + "到" + getPhoneStats(state));
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:  //挂电话
                if (phoneState == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //如果是从接电话的状态切换为挂断电话状态时，则不需要上锁
                } else if (phoneState == TelephonyManager.CALL_STATE_RINGING) {
                    //如果直接由响铃到挂电话则上锁
                    startLockerActivity();
                }
                this.phoneState = state;
                break;
            case TelephonyManager.CALL_STATE_RINGING: //响铃
                this.phoneState = state;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: //接起电话
                this.phoneState = state;
                break;
        }
    }

    private String getPhoneStats(int state) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:  //挂电话
                return "空闲状态CALL_STATE_IDLE";
            case TelephonyManager.CALL_STATE_RINGING: //响铃
                return "响铃状态CALL_STATE_RINGING";
            case TelephonyManager.CALL_STATE_OFFHOOK: //接起电话
                return "接起电话CALL_STATE_OFFHOOK";
            default:
                return "未知状态: " + state;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            Log.e(TAG, "onDestroy", e);
        }
        if(isStartServer()){
            startLockerService(this);
        }
    }

    private boolean isStartServer(){
        return getStartServiceFlag(LockerService.this);
    }

    public static void saveStartServicFlag(String reason, boolean isStart){
        App.getProcessDataSPOperator().putValue(SP_IS_START_SERVER, isStart);
        Log.e(TAG, "saveStartServicFlag: " + isStart + " ,reason: " + reason);
    }

    public static boolean getStartServiceFlag(Context context){
        return App.getProcessDataSPOperator().getValueByKey(SP_IS_START_SERVER, true);
    }

    private KeyguardManager mKeyguardManager = null;
    private KeyguardManager.KeyguardLock mKeyguardLock = null;

    public void closeSystemLock() {
        if (mKeyguardManager == null) {
            mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        }
        if (mKeyguardLock == null) {
            mKeyguardLock = mKeyguardManager.newKeyguardLock("");
        }
        mKeyguardLock.disableKeyguard();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceive(context, intent);
        }
    };

    public abstract void startLockerActivity();

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            onPhoneStateChanged(state, incomingNumber);
        }
    };
}
