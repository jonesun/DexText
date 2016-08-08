package com.change.unlock.diy;

import android.app.Application;

import com.tpad.processData.ProcessDataOperator;
import com.tpad.processData.ProcessSpDataOperator;

/**
 * Created by jone.sun on 2015/7/10.
 */
public class App extends Application {
    private static ProcessDataOperator processDataSPOperator;

    @Override
    public void onCreate() {
        super.onCreate();
        processDataSPOperator = ProcessSpDataOperator.getInstance(this);
    }

    public static ProcessDataOperator getProcessDataSPOperator() {
        return processDataSPOperator;
    }
}
