package com.tpad.lockwidget.view.time;

import android.content.Context;
import android.view.ViewGroup;

import com.tpad.lockmiddleware.TimeWidgetMiddleware;

/**
 * Created by jone.sun on 2015/9/1.
 */
public class NormalTimeWidgetMiddleware implements TimeWidgetMiddleware {
    private NormalTimeWidget normalTimeWidget;
    @Override
    public ViewGroup getWidget(Context context) {
        normalTimeWidget = new NormalTimeWidget(context);
        return normalTimeWidget;
    }

    @Override
    public void updateView() {
        if(normalTimeWidget != null){
            normalTimeWidget.update();
        }
    }
}
