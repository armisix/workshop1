package com.superscores.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.superscores.android.R;
import com.superscores.android.common.utils.ColorUtils;
import com.superscores.android.widget.TextButton.TextButtonDrawable.ButtonState;

public class TextButton extends TextView {

    public TextButton(Context context, boolean isSelectable) {
        super(context);
        init(context, ColorUtils.getColor(getContext(), ColorUtils.ColorType.ACCENT), isSelectable);
    }

    public TextButton(Context context, int buttonColor, boolean isSelectable) {
        super(context);
        init(context, buttonColor, isSelectable);
    }

    public TextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithAttrs(context, attrs);
    }

    public TextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithAttrs(context, attrs);
    }

    public void initWithAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ThemeTextButton, 0, 0);

        try {
            int buttonColor = ta.getInt(R.styleable.ThemeTextButton_ttb_buttonColor,
                    ColorUtils.getColor(context, ColorUtils.ColorType.ACCENT));
            boolean isSelectable = ta.getBoolean(R.styleable.ThemeTextButton_ttb_selectable, true);

            init(context, buttonColor, isSelectable);
        } finally {
            ta.recycle();
        }
    }

    @SuppressWarnings("deprecation")
    public void init(Context context, int buttonColor, boolean isSelectable) {
        if (!isSelectable) {
            setBackgroundColor(buttonColor);
        } else {
            setBackgroundDrawable(getSelectableTextButton(context, buttonColor));
        }
    }

    public static Drawable getSelectableTextButton(Context context, int buttonColor) {

        if (Build.VERSION.SDK_INT >= 21) {
            int[] colors = ColorUtils.getColors(context, ColorUtils.ColorType.ACCENT_RIPPLE,
                    ColorUtils.ColorType.ACCENT);

            ColorStateList rippleColor = new ColorStateList(new int[][]{new int[]{}},
                    new int[]{colors[0]});

            RippleDrawable rippleDrawable = new RippleDrawable(rippleColor,
                    new ColorDrawable(colors[1]), null);

            return rippleDrawable;

        } else {
            StateListDrawable stateDrawable = new StateListDrawable();

            stateDrawable.addState(new int[]{
                            android.R.attr.state_focused,
                            android.R.attr.state_pressed},
                    new TextButtonDrawable(context, ButtonState.PRESSED, buttonColor));

            stateDrawable.addState(new int[]{
                            -android.R.attr.state_focused,
                            android.R.attr.state_pressed},
                    new TextButtonDrawable(context, ButtonState.PRESSED, buttonColor));

            stateDrawable.addState(new int[]{android.R.attr.state_focused},
                    new TextButtonDrawable(context, ButtonState.FOCUSED, buttonColor));

            stateDrawable.addState(new int[0],
                    new TextButtonDrawable(context, ButtonState.NORMAL, buttonColor));

            return stateDrawable;
        }
    }

    public static class TextButtonDrawable extends Drawable {

        public enum ButtonState {
            NORMAL,
            PRESSED,
            FOCUSED
        }

        private Paint mPaint;
        private Paint mPaintOverlay;
        private Paint mPaintStroke;
        private ButtonState mState;

        private float mStrokeOffset;

        public TextButtonDrawable(Context context, ButtonState state, int buttonColor) {
            mState = state;

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(buttonColor);

            if (mState == ButtonState.PRESSED || mState == ButtonState.FOCUSED) {
                mPaintOverlay = new Paint();
                mPaintOverlay.setAntiAlias(true);

                //MUST_DO
                mPaintOverlay.setColor(context.getResources()
                        .getColor(R.color.ripple_material_dark));

                if (mState == ButtonState.FOCUSED) {
                    float strokeWidth = context.getResources()
                            .getDimensionPixelSize(R.dimen.divider_large);
                    mPaintStroke = new Paint();
                    mPaintStroke.setAntiAlias(true);
                    mPaintStroke.setStyle(Paint.Style.STROKE);
                    mPaintStroke.setStrokeWidth(strokeWidth);
                    mPaintStroke.setColor(buttonColor);
                    mStrokeOffset = strokeWidth / 2;
                }
            }
        }

        @Override
        public void draw(Canvas canvas) {
            int width = getBounds().width();
            int height = getBounds().height();

            canvas.drawRect(0, 0, width, height, mPaint);

            if (mState == ButtonState.PRESSED || mState == ButtonState.FOCUSED) {
                canvas.drawRect(0, 0, width, height, mPaintOverlay);

                if (mState == ButtonState.FOCUSED) {
                    canvas.drawRect(mStrokeOffset, mStrokeOffset, width - mStrokeOffset,
                            height - mStrokeOffset, mPaintStroke);
                }
            }
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }
}