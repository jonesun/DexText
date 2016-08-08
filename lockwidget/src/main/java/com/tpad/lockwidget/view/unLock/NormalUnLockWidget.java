package com.tpad.lockwidget.view.unLock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tpad.lockmiddleware.UnLockListener;

/**
 * Created by jone.sun on 2015/8/31.
 */
public class NormalUnLockWidget extends LinearLayout{
    private Button button;
    private UnLockListener unLockListener;
    public NormalUnLockWidget(Context context, AttributeSet attrs, UnLockListener unLockListener) {
        super(context, attrs);
        this.unLockListener = unLockListener;
        init();
    }

    public NormalUnLockWidget(Context context, UnLockListener unLockListener) {
        super(context);
        this.unLockListener = unLockListener;
        init();
    }

    private void init() {
        button = new Button(getContext());
        button.setText("默认解锁(来自lockwidget)");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unLockListener != null) {
                    unLockListener.unLock();
                }
            }
        });
        addView(button, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }
}