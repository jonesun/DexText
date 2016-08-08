package com.tpad.lockwidget.view.unLock;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.tpad.lockmiddleware.UnLockListener;

/**
 * Created by jone.sun on 2015/9/11.
 */
public class SeekBarUnLockWidget extends RelativeLayout {
    private SeekBar seekBar;
    private UnLockListener unLockListener;
    public SeekBarUnLockWidget(Context context, AttributeSet attrs, UnLockListener unLockListener) {
        super(context, attrs);
        this.unLockListener = unLockListener;
        init();
    }

    public SeekBarUnLockWidget(Context context, UnLockListener unLockListener) {
        super(context);
        this.unLockListener = unLockListener;
        init();
    }

    private void init() {
        seekBar = new SeekBar(getContext());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() >= 95){
                    if(unLockListener != null){
                        unLockListener.unLock();
                    }
                }
            }
        });
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addView(seekBar, layoutParams);
    }
}
