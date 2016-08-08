package com.tpad.lockwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jone.sun on 2015/9/1.
 */
public class AlphaAnimationTextViewWidget extends LinearLayout {
    public AlphaAnimationTextViewWidget(Context context) {
        super(context);
        init(context);
    }

    public AlphaAnimationTextViewWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        TextView textView = new TextView(context);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setTextSize(24);
        textView.setText("你若想得到这世界最好的东西，先提供这世界最好的你。");
        AlphaAnimation animation_alpha = new AlphaAnimation(0.1f,1.0f);
        animation_alpha.setRepeatCount(-1);
        animation_alpha.setDuration(2000);
        textView.setAnimation(animation_alpha);
        addView(textView);
    }
}
