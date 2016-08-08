package com.tpad.lockwidget.view.unLock;

import android.content.Context;
import android.view.ViewGroup;

import com.tpad.lockmiddleware.UnLockListener;
import com.tpad.lockmiddleware.UnLockWidgetMiddleware;

/**
 * Created by jone.sun on 2015/8/31.
 */
public class NormalUnLockWidgetMiddleware implements UnLockWidgetMiddleware {
    @Override
    public ViewGroup getWidget(Context context, UnLockListener unLockListener) {
        return new NormalUnLockWidget(context, unLockListener);
    }
}
