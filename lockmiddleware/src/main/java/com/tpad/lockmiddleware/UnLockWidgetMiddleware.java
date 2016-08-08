package com.tpad.lockmiddleware;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by jone.sun on 2015/8/31.
 */
public interface UnLockWidgetMiddleware {
    ViewGroup getWidget(Context context, UnLockListener unLockListener);
}
