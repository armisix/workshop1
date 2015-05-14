package com.superscores.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class RelativeLayoutDoNotPressWithParent extends RelativeLayout {

    public RelativeLayoutDoNotPressWithParent(Context context) {
        super(context);
    }

    public RelativeLayoutDoNotPressWithParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayoutDoNotPressWithParent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RelativeLayoutDoNotPressWithParent(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressed && getParent() instanceof View && ((View) getParent()).isPressed()) {
            return;
        }
        super.setPressed(pressed);
    }
}