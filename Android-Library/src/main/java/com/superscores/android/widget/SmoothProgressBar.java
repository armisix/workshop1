package com.superscores.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;

import com.superscores.android.R;

public class SmoothProgressBar extends ProgressBar {

    // private static final int INTERPOLATOR_ACCELERATE = 0;
    // private static final int INTERPOLATOR_LINEAR = 1;
    // private static final int INTERPOLATOR_ACCELERATE_DECELERATE = 2;
    // private static final int INTERPOLATOR_DECELERATE = 3;

    public SmoothProgressBar(Context context) {
        this(context, null);
    }

    public SmoothProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.spbStyle);
    }

    public SmoothProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Resources res = context.getResources();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmoothProgressBar,
                defStyle, 0);

        final int color = ta.getColor(R.styleable.SmoothProgressBar_spb_color,
                res.getColor(R.color.spb_default_color));

        final int sectionsCount = ta.getInteger(R.styleable.SmoothProgressBar_spb_sections_count,
                res.getInteger(R.integer.spb_default_sections_count));

        final int separatorLength = ta.getDimensionPixelSize(
                R.styleable.SmoothProgressBar_spb_stroke_separator_length,
                res.getDimensionPixelSize(R.dimen.spb_default_stroke_separator_length));

        final float strokeWidth = ta.getDimension(R.styleable.SmoothProgressBar_spb_stroke_width,
                res.getDimension(R.dimen.spb_default_stroke_width));

        final float speed = ta.getFloat(R.styleable.SmoothProgressBar_spb_speed,
                Float.parseFloat(res.getString(R.string.spb_default_speed)));

        final int interpolatorMode = ta.getInteger(R.styleable.SmoothProgressBar_spb_interpolator,
                res.getInteger(R.integer.spb_default_interpolator));

        final float interpolatorFactor = ta.getFloat(
                R.styleable.SmoothProgressBar_spb_interpolator_factor,
                Float.parseFloat(res.getString(R.string.spb_default_interpolator_factor)));

        final boolean reversed = ta.getBoolean(R.styleable.SmoothProgressBar_spb_reversed,
                res.getBoolean(R.bool.spb_default_reversed));

        final boolean mirrorMode = ta.getBoolean(R.styleable.SmoothProgressBar_spb_mirror_mode,
                res.getBoolean(R.bool.spb_default_mirror_mode));

        final int colorsId = ta.getResourceId(R.styleable.SmoothProgressBar_spb_colors, 0);
        ta.recycle();

        int[] colors = null;
        // colors
        if (colorsId != 0) {
            colors = res.getIntArray(colorsId);
        }

        SmoothProgressDrawable.Builder builder = new SmoothProgressDrawable.Builder(context).speed(
                speed)
                .interpolatorMode(interpolatorMode)
                .interpolatorFacotr(interpolatorFactor)
                .sectionsCount(sectionsCount)
                .separatorLength(separatorLength)
                .strokeWidth(strokeWidth)
                .reversed(reversed)
                .mirrorMode(mirrorMode);

        if (colors != null && colors.length > 0) {
            builder.colors(colors);
        } else {
            builder.color(color);
        }

        setIndeterminateDrawable(builder.build());
    }

    // TODO: applyStyle with interpolatorFactor
    // public void applyStyle(int styleResId) {
    // TypedArray ta = getContext().obtainStyledAttributes(null, R.styleable.SmoothProgressBar,
    // 0, styleResId);
    //
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_color)) {
    // setSmoothProgressDrawableColor(ta.getColor(R.styleable.SmoothProgressBar_spb_color, 0));
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_colors)) {
    // int colorsId = ta.getResourceId(R.styleable.SmoothProgressBar_spb_colors, 0);
    // if (colorsId != 0) {
    // int[] colors = getResources().getIntArray(colorsId);
    // if (colors != null && colors.length > 0) setSmoothProgressDrawableColors(colors);
    // }
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_sections_count)) {
    // setSmoothProgressDrawableSectionsCount(ta.getInteger(R.styleable
    // .SmoothProgressBar_spb_sections_count, 0));
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_stroke_separator_length)) {
    // setSmoothProgressDrawableSeparatorLength(ta.getDimensionPixelSize(R.styleable
    // .SmoothProgressBar_spb_stroke_separator_length, 0));
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_stroke_width)) {
    // setSmoothProgressDrawableStrokeWidth(ta.getDimension(R.styleable
    // .SmoothProgressBar_spb_stroke_width, 0));
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_speed)) {
    // setSmoothProgressDrawableSpeed(ta.getFloat(R.styleable.SmoothProgressBar_spb_speed, 0));
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_reversed)) {
    // setSmoothProgressDrawableReversed(ta.getBoolean(R.styleable
    // .SmoothProgressBar_spb_reversed, false));
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_mirror_mode)) {
    // setSmoothProgressDrawableMirrorMode(ta.getBoolean(R.styleable
    // .SmoothProgressBar_spb_mirror_mode, false));
    // }
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_interpolator)) {
    // int iInterpolator = ta.getInteger(R.styleable.SmoothProgressBar_spb_interpolator, -1);
    //
    //
    // if (ta.hasValue(R.styleable.SmoothProgressBar_spb_interpolator_factor)) {
    //
    // }
    //
    // Interpolator interpolator;
    // switch (iInterpolator) {
    // case INTERPOLATOR_ACCELERATE_DECELERATE:
    // interpolator = new AccelerateDecelerateInterpolator();
    // break;
    // case INTERPOLATOR_DECELERATE:
    // interpolator = new DecelerateInterpolator();
    // break;
    // case INTERPOLATOR_LINEAR:
    // interpolator = new LinearInterpolator();
    // break;
    // case INTERPOLATOR_ACCELERATE:
    // interpolator = new AccelerateInterpolator();
    // break;
    // default:
    // interpolator = null;
    // }
    // if (interpolator != null) {
    // setInterpolator(interpolator);
    // }
    // }
    // ta.recycle();
    // }

    private SmoothProgressDrawable checkIndeterminateDrawable() {
        Drawable ret = getIndeterminateDrawable();
        if (ret == null || !(ret instanceof SmoothProgressDrawable)) {
            throw new RuntimeException("The drawable is not a SmoothProgressDrawable");
        }
        return (SmoothProgressDrawable) ret;
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
        super.setInterpolator(interpolator);
        Drawable ret = getIndeterminateDrawable();
        if (ret != null && (ret instanceof SmoothProgressDrawable)) {
            ((SmoothProgressDrawable) ret).setInterpolator(interpolator);
        }
    }

    public void setSmoothProgressDrawableInterpolator(Interpolator interpolator) {
        checkIndeterminateDrawable().setInterpolator(interpolator);
    }

    public void setSmoothProgressDrawableColors(int[] colors) {
        checkIndeterminateDrawable().setColors(colors);
    }

    public void setSmoothProgressDrawableColor(int color) {
        checkIndeterminateDrawable().setColor(color);
    }

    public void setSmoothProgressDrawableSpeed(float speed) {
        checkIndeterminateDrawable().setSpeed(speed);
    }

    public void setSmoothProgressDrawableSectionsCount(int sectionsCount) {
        checkIndeterminateDrawable().setSectionsCount(sectionsCount);
    }

    public void setSmoothProgressDrawableSeparatorLength(int separatorLength) {
        checkIndeterminateDrawable().setSeparatorLength(separatorLength);
    }

    public void setSmoothProgressDrawableStrokeWidth(float strokeWidth) {
        checkIndeterminateDrawable().setStrokeWidth(strokeWidth);
    }

    public void setSmoothProgressDrawableReversed(boolean reversed) {
        checkIndeterminateDrawable().setReversed(reversed);
    }

    public void setSmoothProgressDrawableMirrorMode(boolean mirrorMode) {
        checkIndeterminateDrawable().setMirrorMode(mirrorMode);
    }
}