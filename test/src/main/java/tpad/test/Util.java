package tpad.test;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import tpad.dextext.Callback;
import tpad.dextext.MainInterface;
import tpad.test.view.ColorPickerDialog;
import tpad.test.view.TestView;

/**
 * Created by jone.sun on 2015/8/29.
 */
public class Util implements MainInterface{

    @Override
    public void showColorPickerDialog(Context context, final String tag) {
        new ColorPickerDialog(context, new ColorPickerDialog.OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                Log.e(tag, "选择的是: " + color);
            }
        }, android.R.color.white).show();
    }

    @Override
    public void showAnimTextView(Context context, TextView textView){
        Animation mAnimation = AnimationUtils.loadAnimation(App.getInstance(), tpad.test.R.anim.simple_animation);
        textView.setAnimation(mAnimation);
        mAnimation.start();//执行动画效果
    }

    @Override
    public LinearLayout getTestView(Context context, Callback callback){
        return new TestView(context, callback);
    }
}
