package com.tpad.lockwidget.view;

import android.content.Context;
import android.view.ViewGroup;

import com.tpad.lockmiddleware.WidgetMiddleware;

/**
 * Created by jone.sun on 2015/9/1.
 */
public class AlphaAnimationTextViewWidgetMiddleware implements WidgetMiddleware {
    @Override
    public ViewGroup getWidget(Context context) {
        return new AlphaAnimationTextViewWidget(context);
    }
}
