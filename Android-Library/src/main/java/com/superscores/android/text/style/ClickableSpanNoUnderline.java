package com.superscores.android.text.style;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class ClickableSpanNoUnderline extends ClickableSpan {

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}