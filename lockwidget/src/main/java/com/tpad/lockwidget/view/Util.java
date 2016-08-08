package com.tpad.lockwidget.view;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import tpad.dextext.Callback;
import tpad.dextext.MainInterface;

/**
 * Created by jone.sun on 2015/8/29.
 */
public class Util implements MainInterface{

    @Override
    public void showColorPickerDialog(Context context, final String tag) {
        new ColorPickerDialog(context, new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                Log.e(tag, "选择的是: " + color);
            }
        }, android.R.color.white).show();
    }

    @Override
    public void showAnimTextView(Context context, TextView textView){

    }

    @Override
    public LinearLayout getTestView(Context context, Callback callback){
        return new TestView(context, callback);
    }
}
