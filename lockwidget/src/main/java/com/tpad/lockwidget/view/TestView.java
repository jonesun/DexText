package com.tpad.lockwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import tpad.dextext.Callback;

/**
 * Created by jone.sun on 2015/8/31.
 */
public class TestView extends LinearLayout {
    private Callback callback;
    public TestView(Context context, AttributeSet attrs, Callback callback) {
        super(context, attrs);
        this.callback = callback;
        init();
    }

    public TestView(Context context, Callback callback) {
        super(context);
        this.callback = callback;
        init();
    }

    private void init(){
        setOrientation(VERTICAL);
        TextView textView = new TextView(getContext());
        textView.setText("10:00");
        Button button = new Button(getContext());
        button.setText("解锁(来自lockwidget)");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null){
                    callback.onCallback();
                }
            }
        });
        addView(textView);
        addView(button, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }
}
