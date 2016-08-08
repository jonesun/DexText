package com.change.unlock.diy.lock;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.change.unlock.diy.App;
import com.change.unlock.diy.Constants;
import com.change.unlock.diy.R;
import com.change.unlock.lock.ui.activities.LockerActivity;
import com.tpad.lockmiddleware.TimeWidgetMiddleware;
import com.tpad.lockmiddleware.UnLockListener;
import com.tpad.lockmiddleware.UnLockWidgetMiddleware;
import com.tpad.lockmiddleware.WidgetMiddleware;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * 锁屏主页面
 *
 * @author jone.sun on 2015/5/6.
 */
public class DiyLockerMainActivity extends LockerActivity {
    public static final String TAG = DiyLockerMainActivity.class.getSimpleName();
    private boolean isScreenOn = true;
    private DexClassLoader calssLoader;
    private DexClassLoader seekBarClassLoader;
    private TextView txt;
    private LinearLayout rootView;
    private WidgetMiddleware widgetMiddleware;
    private TimeWidgetMiddleware timeWidgetMiddleware;
    private UnLockWidgetMiddleware unLockWidgetMiddleware;

    private RelativeLayout layout_main;
    @Override
    public int getRes() {
        return R.layout.activity_lock_main;
    }

    @Override
    public void initView(View rootView) {
        View top_view = rootView.findViewById(R.id.top_view);
        boolean isFull =  App.getProcessDataSPOperator().getValueByKey(Constants.SP_KEY_FULL_SCREEN, false);
        if(!isFull){
            top_view.setVisibility(View.VISIBLE);
            top_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight()));
        }

        String rootPath = Constants.FILE_APP_PATH + File.separator + "lock1";
        calssLoader = getDexClassLoader(rootPath + File.separator + "classes.dex", "osdk");
        String unlock = App.getProcessDataSPOperator().getValueByKey("unlock", "button");
        if(!TextUtils.isEmpty(unlock) && unlock.equals("seekBar")){
            seekBarClassLoader = getDexClassLoader(rootPath + File.separator + "seekbar_classes.dex", "seekbar");
        }

        layout_main = (RelativeLayout) rootView.findViewById(R.id.layout_main);
        layout_main.setBackgroundDrawable(new BitmapDrawable(rootPath + File.separator + "normal_lock_bg.png"));
        try {
            timeWidgetMiddleware = (TimeWidgetMiddleware) calssLoader.loadClass("com.tpad.lockwidget.view.time.NormalTimeWidgetMiddleware").newInstance();
            widgetMiddleware = (WidgetMiddleware) calssLoader.loadClass("com.tpad.lockwidget.view.AlphaAnimationTextViewWidgetMiddleware").newInstance();
            if(seekBarClassLoader != null){
                unLockWidgetMiddleware = (UnLockWidgetMiddleware) seekBarClassLoader.loadClass("com.tpad.lockwidget.view.unLock.SeekBarUnLockWidgetMiddleware").newInstance();
            }else {
                unLockWidgetMiddleware = (UnLockWidgetMiddleware) calssLoader.loadClass("com.tpad.lockwidget.view.unLock.NormalUnLockWidgetMiddleware").newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(timeWidgetMiddleware != null){
            RelativeLayout.LayoutParams timeWidgetLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeWidgetLayoutParams.topMargin = 16;
            timeWidgetLayoutParams.bottomMargin = 16;
            layout_main.addView(timeWidgetMiddleware.getWidget(DiyLockerMainActivity.this), timeWidgetLayoutParams);
        }
        if(widgetMiddleware != null){
            RelativeLayout.LayoutParams widgetLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            widgetLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout_main.addView(widgetMiddleware.getWidget(DiyLockerMainActivity.this), widgetLayoutParams);
        }
        if(unLockWidgetMiddleware != null){
            RelativeLayout.LayoutParams unLockWidgetLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            unLockWidgetLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            unLockWidgetLayoutParams.bottomMargin = 16;
            layout_main.addView(unLockWidgetMiddleware.getWidget(DiyLockerMainActivity.this, new UnLockListener() {
                @Override
                public void unLock() {
                    Log.e(TAG, "解锁");
                    unlock();
                }
            }), unLockWidgetLayoutParams);
        }
    }

    private DexClassLoader getDexClassLoader(String dexPath, String dexName){
        String dexOutputDir = getDir(dexName, 0).getAbsolutePath();
        String libPath = dexOutputDir + File.separator + "lib";
        //创建类加载器，把dex加载到虚拟机中
        return new DexClassLoader(dexPath, dexOutputDir, libPath,
                getClass().getClassLoader());
    }

    @Override
    public List<String> addRegisterReceiverActions() {
        List<String> broadcastActionList = super.addRegisterReceiverActions();
        broadcastActionList.add(Intent.ACTION_SCREEN_ON);
        broadcastActionList.add(Intent.ACTION_SCREEN_OFF);
        broadcastActionList.add(Intent.ACTION_TIME_CHANGED);
        broadcastActionList.add(Intent.ACTION_TIME_TICK);
        return broadcastActionList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isScreenOn = false;
        Log.e(TAG, "onPause");
    }

    @Override
    public void onBroadcastReceive(Context context, Intent intent) {
        super.onBroadcastReceive(context, intent);
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON: //亮屏
                isScreenOn = true;
                updateTime();
                Log.e(TAG, "亮屏");
                break;
            case Intent.ACTION_SCREEN_OFF: //暗屏
                isScreenOn = false;
                Log.e(TAG, "暗屏");
                break;
            case Intent.ACTION_TIME_CHANGED:
            case Intent.ACTION_TIME_TICK:
                updateTime();
                break;
        }
    }

    private void updateTime(){
        if(isScreenOn && timeWidgetMiddleware != null){
            timeWidgetMiddleware.updateView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            //e.printStackTrace();
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }
}
