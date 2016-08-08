package com.change.unlock.diy.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by jone.sun on 2015/6/19.
 */
public class PhoneUtil {
    private Context context;
    private DisplayMetrics metrics;
    private float wScale = 0f;
    private static int defaultWScale = 720;
    private static PhoneUtil instance = null;
    public static PhoneUtil getInstance(Context context){
        if(instance == null){
            instance = new PhoneUtil(context);
        }
        return instance;
    }
    public PhoneUtil(Context context){
        this.context = context;
    }

    public int getTextScaleSize(int size){
        return px2sp(size * getWScale());
    }

    public int px2sp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getScale(int size){
        return (int)(size * getWScale());
    }

    public DisplayMetrics getPhoneScreen() {
        if(metrics == null){
            metrics = new DisplayMetrics();
            WindowManager WM = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            WM.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics;
    }

    private float getWScale(){
        return getWScale(defaultWScale);
    }

    private float getWScale(int w) {
        if(wScale == 0f){
            int widthPhone = getPhoneScreen().widthPixels;
            wScale = widthPhone * 1.0f / w;
        }
        return wScale;
    }

    public boolean isExistsAppByPkgName(String pkgName) {
        boolean result = false;
        if (pkgName == null)
            return result;
        try {
            ApplicationInfo api = context.getPackageManager()
                    .getApplicationInfo(pkgName, 0);
            result = true;
        } catch (PackageManager.NameNotFoundException e) {
            return result;
        }
        return result;
    }

    public boolean isPhoneCurrWifiOpen() {
        WifiManager mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;
        try {
            wifiInfo = mWifiManager.getConnectionInfo();
        } catch (Exception e) {
            wifiInfo = null;
            Log.e("PhoneUtil", "isPhoneCurrWifiOpen", e);
        }
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
            // WIFI is on
            return true;
        } else {
            // WIFI is off
            return false;
        }
    }

    /**
     * is Phone CurrNetwork Open(whether 3G net open or not)
     *
     * @param context
     * @return true or false
     */
    public boolean isPhoneCurrNetworkOpen(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info == null) {
                    return false;
                } else {
                    if (info.isAvailable()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasNetWork(Context context) {
        return isPhoneCurrNetworkOpen(context) || isPhoneCurrWifiOpen();
    }

    /**
     * 打開默認瀏覽器
     *
     * @param url
     */
    public void StartDefaultBrower(String url) {
        if (isExistsAppByPkgName("com.android.browser")) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setClassName("com.android.browser",
                    "com.android.browser.BrowserActivity");
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startBrower(url);
            }
        } else {
            startBrower(url);
        }
    }

    public void startBrower(String url) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}
