package com.superscores.android.text.style;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class ClickableSpanDefaultColor extends ClickableSpan {

    /**
     * Makes the text underlined and in the link color.
     */
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(true);
    }
}