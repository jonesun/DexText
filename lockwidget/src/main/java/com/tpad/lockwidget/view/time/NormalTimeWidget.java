package com.tpad.lockwidget.view.time;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jone.sun on 2015/9/1.
 */
public class NormalTimeWidget extends LinearLayout{
    private TextView timeTxt;
    private TextView dateTxt;
    public NormalTimeWidget(Context context) {
        super(context);
        init();
    }

    public NormalTimeWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setOrientation(VERTICAL);
        timeTxt = new TextView(getContext());
        timeTxt.setText("00:00");
        timeTxt.setTextColor(getResources().getColor(android.R.color.white));
        timeTxt.setTextSize(48);

        dateTxt = new TextView(getContext());
        dateTxt.setText("2015-9-1");
        dateTxt.setTextColor(getResources().getColor(android.R.color.white));
        dateTxt.setTextSize(35);

        addView(timeTxt);
        addView(dateTxt);
        update();
    }

    public void update(){
        Date currentDate = new Date();
        if(timeTxt != null){
            timeTxt.setText(new SimpleDateFormat("HH:mm").format(currentDate));
        }
        if(dateTxt != null){
            dateTxt.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentDate));
        }
    }
}
