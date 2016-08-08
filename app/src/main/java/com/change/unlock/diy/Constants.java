package com.change.unlock.diy;

import android.os.Environment;

/**
 * Created by jone.sun on 2015/6/23.
 */
public class Constants {
    public final static String ALBUM_PATH = Environment
            .getExternalStorageDirectory().toString();

    public final static String FILE_APP_PATH = ALBUM_PATH + "/dexLocker";
    public final static String FILE_APP_UX_PATH = FILE_APP_PATH + "/content/";

    public final static String SP_KEY_FULL_SCREEN = "fullScreen";
}
