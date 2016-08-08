package com.change.unlock.diy.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.change.unlock.diy.App;
import com.change.unlock.diy.R;
import com.change.unlock.diy.utils.FileUtil;
import com.change.unlock.lock.services.LockerService;
import com.tpad.lockmiddleware.UnLockListener;
import com.tpad.lockmiddleware.UnLockWidgetMiddleware;

import java.io.File;

import dalvik.system.DexClassLoader;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DexClassLoader calssLoader;
    private LinearLayout rootView;
    private UnLockWidgetMiddleware unLockWidgetMiddleware;
    private RadioGroup radio_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileUtil.intFilePath();
        FileUtil.copyAssetsUxFile(MainActivity.this);
        findViews();
        initViews();
        //启动锁屏服务
        LockerService.startLockerService(MainActivity.this);
        LockerService.saveStartServicFlag("初始化useUxName", true);

        rootView = (LinearLayout) findViewById(R.id.rootView);
        String rootPath = Environment.getExternalStorageDirectory() + File.separator + "dexTest" + File.separator;
        String dexPath = rootPath + File.separator + "classes.dex";
        String dexOutputDir = getDir("osdk1", 0).getAbsolutePath();
        String libPath = dexOutputDir + File.separator + "lib";
        //创建类加载器，把dex加载到虚拟机中

        calssLoader = new DexClassLoader(dexPath, dexOutputDir, libPath,
                getClass().getClassLoader());
        try {
            Class<?> clazz = calssLoader.loadClass("com.tpad.lockwidget.view.unLock.NormalUnLockWidgetMiddleware");
            unLockWidgetMiddleware = (UnLockWidgetMiddleware) clazz
                    .newInstance();// 接口
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(unLockWidgetMiddleware != null){
            rootView.addView(unLockWidgetMiddleware.getWidget(MainActivity.this, new UnLockListener() {
                @Override
                public void unLock() {
                    Log.e(TAG, "解锁");
                    finish();
                }
            }));
        }
    }

    private void findViews(){
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_use_seek_bar){
                    App.getProcessDataSPOperator().putValue("unlock", "seekBar");
                }else {
                    App.getProcessDataSPOperator().putValue("unlock", "button");
                }
            }
        });
    }

    private void initViews(){
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
