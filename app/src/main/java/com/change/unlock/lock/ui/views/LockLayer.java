package com.change.unlock.lock.ui.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * 锁屏操作页
 *
 * @author jone.sun on 2015/4/7.
 */
public class LockLayer {
    private Context mContext;
    private WindowManager mWindowManager;// 窗口管理器
    private View mLockView;// 锁屏视图
    private LayoutParams wmParams;
    private static LockLayer mLockLayer;
    private boolean isLocked;// 是否锁定

    public static synchronized LockLayer getInstance(Context mContext) {
        if (mLockLayer == null) {
            mLockLayer = new LockLayer(mContext);
        }
        return mLockLayer;
    }

    public LockLayer(Context mContext) {
        this.mContext = mContext;
        init();
    }

    private void init() {
        isLocked = false;
        mWindowManager = (WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wmParams = new LayoutParams();
//        wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
//        //锁屏窗口。
//        public static final int TYPE_KEYGUARD = FIRST_SYSTEM_WINDOW +4;
//        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
//        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.width = LayoutParams.MATCH_PARENT;
        wmParams.height = LayoutParams.MATCH_PARENT;
        wmParams.flags = 1280;
    }

    /**
     * 锁屏
     */
    public synchronized void lock() {
        if (mLockView != null && !isLocked) {
            mWindowManager.addView(mLockView, wmParams);
        }
        isLocked = true;
    }

    /**
     * 解锁
     */
    public synchronized void unlock() {
        if (mWindowManager != null && isLocked) {
            mWindowManager.removeView(mLockView);
        }
        isLocked = false;
    }

    /**
     * 更新
     */
    public synchronized void update() {
        if (mLockView != null && !isLocked) {
            mWindowManager.updateViewLayout(mLockView, wmParams);
        }
    }

    /**
     * 设置锁屏视图
     *
     * @param v
     */
    public synchronized void setLockView(View v) {
        mLockView = v;
    }
}
