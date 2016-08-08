package com.change.unlock.diy.utils;

import android.content.Context;
import android.util.Log;

import com.change.unlock.diy.Constants;
import com.change.unlock.diy.R;
import com.hu.andun7z.AndUn7z;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jone.sun on 2015/6/25.
 */
public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static void intFilePath(){
        new File(Constants.FILE_APP_PATH).mkdirs();
        File file_nomedia = new File(Constants.FILE_APP_PATH, ".nomedia");
        if(!file_nomedia.exists()){
            try {
                file_nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void saveStringToFile(String filePath, String str){
        File useUxSign = new File(filePath);
        if(useUxSign.exists()){
            useUxSign.delete();
        }
        createFile(useUxSign);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(useUxSign));
            bw.write(str);
            bw.close();
        } catch (IOException e) {
            Log.e(TAG, "saveStringToFile", e);
        }
    }

    public static String getStringFromFile(String filePath, String defaultStr){
        String str = defaultStr;
        File file = new File(filePath);
        if(file.exists()){
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(file));
                str = br.readLine();
            } catch (Exception e) {
                Log.e(TAG, "getUseUxSign", e);
            }
        }
        Log.e(TAG, "str: " + str);
        return str;
    }

    public static File createFile(File file){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "createFile:" + file.getPath(), e);
            }
        }
        return file;
    }

    /***
     * 获取预制的ux文件路径
     * @param context
     * @return
     */
    public static File getOwnUxFile(Context context) {
        File file = new File(Constants.FILE_APP_UX_PATH  + context.getString(R.string.app_name));
        if(!file.exists()){
            copyAssetsUxFile(context);
        }
        return file;
    }

    public static boolean copyAssetsUxFile(Context context) {
//        String uxFileName = context.getString(R.string.ux_name) + Constants.UX_FILE_END;
        return AndUn7z.extractAssets(context, "lock1.7z", Constants.FILE_APP_PATH);
//        File file = new File(Constants.FILE_UX_PATH + uxFileName);
//        if (!file.exists()) {
//            try {
//                copyFromAssets(context,
//                        uxFileName, file.getPath());
//            } catch (Exception e) {
//                Log.e(TAG, "copyAssetsUxFile", e);
//            }
//        }
    }

    public static void copyFromAssets(Context context, String assetPath,
                                      String tempPath) throws Exception {
        InputStream inputStream = context.getAssets().open(assetPath);
        FileOutputStream fileOutputStream = new FileOutputStream(tempPath);
        int length = -1;
        byte[] buffer = new byte[0x400000];
        while ((length = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, length);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }

    public static void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                new File(newPath).getParentFile().mkdirs();
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "copyFile", e);
        }finally {
            try{
                if(fs != null){
                    fs.close();
                }
                if(inStream != null){
                    inStream.close();
                }
            }catch (Exception e){
                Log.e(TAG, "copyFile", e);
            }
        }
    }
}
