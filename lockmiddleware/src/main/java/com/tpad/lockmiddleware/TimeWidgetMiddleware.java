package com.tpad.lockmiddleware;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by jone.sun on 2015/9/1.
 */
public interface TimeWidgetMiddleware {
    ViewGroup getWidget(Context context);

    void updateView();
}
