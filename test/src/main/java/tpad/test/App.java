package tpad.test;

import android.app.Application;

/**
 * Created by jone.sun on 2015/8/31.
 */
public class App extends Application {
    private static App instance = null;
    public static App getInstance(){
        if(instance == null){
            instance = new App();
        }
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
