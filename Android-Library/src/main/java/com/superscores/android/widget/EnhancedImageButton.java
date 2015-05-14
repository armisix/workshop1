package com.superscores.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.superscores.android.R;
import com.superscores.android.common.utils.ColorUtils;
import com.superscores.android.common.utils.ColorUtils.ColorType;
import com.superscores.android.compat.DrawableCompat;
import com.superscores.android.graphics.TintableDrawable;

@SuppressWarnings("unused")
public class EnhancedImageButton extends RelativeLayoutDoNotPressWithParent {

    public static final int IMAGE_ID = R.id.enhanced_image_button_icon;
    private static final int NONE = -1;

    private static final int TINT_MODE_NORMAL = 0;
    private static final int TINT_MODE_DISABLED = 1;
    private static final int TINT_MODE_SINGLE_COLOR = 2;

    public static final int SELECTABLE_NONE = -1;
    public static final int SELECTABLE_PARENT = 0;
    public static final int SELECTABLE_CHILD = 1;

    private Drawable mSelectableBackground;
    private int mSelectableArea = SELECTABLE_PARENT;
    private int mImageWidth = 0;
    private int mImageHeight = 0;
    private int mLayoutWidth = 0;
    private int mLayoutHeight = 0;

    private boolean mIsAltState = false;

    private int mImageDrawableId = NONE;
    private int mTintMode;
    private int mColorNormal;
    private int mColorDisabled;
    private int mColorActivated;

    private int mImageDrawableIdAlt = NONE;
    private int mTintModeAlt;
    private int mColorNormalAlt;
    private int mColorDisabledAlt;
    private int mColorActivatedAlt;

    private TintableDrawable mTintableDrawable;
    private ImageView mImageView;

    public EnhancedImageButton(Context context) {
        super(context);
        init(context);
    }

    public EnhancedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public EnhancedImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EnhancedImageButton(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }

    private void init(Context context) {
        mLayoutWidth = getResources().getDimensionPixelSize(R.dimen.prefer_clickable_size);
        mLayoutHeight = getResources().getDimensionPixelSize(R.dimen.prefer_clickable_size);
        mImageWidth = getResources().getDimensionPixelSize(R.dimen.icon_normal);
        mImageHeight = getResources().getDimensionPixelSize(R.dimen.icon_normal);

        mSelectableBackground = DrawableCompat.getDrawableFromAttr(context,
                R.attr.selectableItemBackground);

        setLayoutParams(new RelativeLayout.LayoutParams(mLayoutWidth, mLayoutHeight));

        int[] colors = ColorUtils.getColors(context, ColorUtils.ColorType.CONTROL_NORMAL,
                ColorType.CONTROL_DISABLED, ColorType.CONTROL_ACTIVATED);
        mColorNormal = colors[0];
        mColorDisabled = colors[1];
        mColorActivated = colors[2];

        mColorNormalAlt = mColorNormal;
        mColorDisabledAlt = mColorDisabled;
        mColorActivatedAlt = mColorActivated;

        mTintMode = TINT_MODE_DISABLED;
        mTintModeAlt = TINT_MODE_DISABLED;

        initLayout();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.EnhancedImageButton, 0, 0);
        try {
            // fallback to regular attribute to set image
            int imageSrc = ta.getResourceId(R.styleable.EnhancedImageButton_android_src, NONE);

            mSelectableArea = ta.getInteger(R.styleable.EnhancedImageButton_eib_selectableArea,
                    SELECTABLE_PARENT);
            mImageDrawableId = ta.getResourceId(R.styleable.EnhancedImageButton_eib_image_drawable,
                    imageSrc);
            mImageDrawableIdAlt = ta.getResourceId(
                    R.styleable.EnhancedImageButton_eib_image_drawableAlt, mImageDrawableIdAlt);
            mLayoutWidth = ta.getLayoutDimension(
                    R.styleable.EnhancedImageButton_android_layout_width, 0);
            mLayoutHeight = ta.getLayoutDimension(
                    R.styleable.EnhancedImageButton_android_layout_height, 0);
            mImageWidth = ta.getDimensionPixelSize(R.styleable.EnhancedImageButton_eib_image_width,
                    mLayoutWidth);
            mImageHeight = ta.getDimensionPixelSize(
                    R.styleable.EnhancedImageButton_eib_image_height, mLayoutHeight);

            // color
            int[] colors = ColorUtils.getColors(context, ColorType.CONTROL_NORMAL,
                    ColorType.CONTROL_DISABLED, ColorType.CONTROL_ACTIVATED);
            int colorNormalDefault = colors[0];
            int colorDisabledDefault = colors[1];
            int colorActivated = colors[2];

            mColorNormal = ta.getColor(R.styleable.EnhancedImageButton_eib_colorNormal,
                    colorNormalDefault);
            mColorDisabled = ta.getColor(R.styleable.EnhancedImageButton_eib_colorDisabled,
                    colorDisabledDefault);
            mColorActivated = ta.getColor(R.styleable.EnhancedImageButton_eib_colorActivated,
                    colorActivated);

            mColorNormalAlt = ta.getColor(R.styleable.EnhancedImageButton_eib_colorNormalAlt,
                    colorNormalDefault);
            mColorDisabledAlt = ta.getColor(R.styleable.EnhancedImageButton_eib_colorDisabledAlt,
                    colorDisabledDefault);
            mColorActivatedAlt = ta.getColor(R.styleable.EnhancedImageButton_eib_colorActivatedAlt,
                    colorActivated);

            mTintMode = ta.getInteger(R.styleable.EnhancedImageButton_eib_tintMode,
                    TINT_MODE_DISABLED);
            mTintModeAlt = ta.getInteger(R.styleable.EnhancedImageButton_eib_tintModeAlt,
                    TINT_MODE_DISABLED);

            if (mSelectableArea == SELECTABLE_NONE) {
                mSelectableBackground = null;
            } else {
                mSelectableBackground = DrawableCompat.getDrawableFromAttr(context,
                        R.attr.selectableItemBackground);
            }
        } finally {
            ta.recycle();
        }
        initLayout();
    }

    @SuppressWarnings("deprecation")
    private void initLayout() {
        RelativeLayout.LayoutParams iconParam = new RelativeLayout.LayoutParams(mImageWidth,
                mImageHeight);
        iconParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        mImageView = new ImageView(getContext());
        mImageView.setId(IMAGE_ID);
        mImageView.setLayoutParams(iconParam);
        addView(mImageView);

        if (mImageDrawableId != NONE) {
            setImageDrawable(mImageDrawableId);
        }

        if (mSelectableArea == SELECTABLE_PARENT) {
            setBackgroundDrawable(mSelectableBackground);
            mImageView.setBackgroundDrawable(null);
        } else if (mSelectableArea == SELECTABLE_CHILD) {
            setBackgroundDrawable(null);
            mImageView.setBackgroundDrawable(mSelectableBackground);
        } else {
            setBackgroundDrawable(null);
            mImageView.setBackgroundDrawable(null);
        }
    }

    // @Override
    // protected void onVisibilityChanged(View changedView, int visibility) {
    // super.onVisibilityChanged(changedView, visibility);
    // applyTintMode();
    // invalidate();
    // }

    // @Override
    // public void setVisibility(int visibility) {
    // super.setVisibility(visibility);
    // applyTintMode();
    // invalidate();
    // }

    // /////////////////////////////////////////////////////////////////////////
    // Drawable
    // /////////////////////////////////////////////////////////////////////////

    public void setImageDrawable(@DrawableRes int drawableId) {
        setImageDrawable(getResources().getDrawable(drawableId));
    }

    public void setImageDrawable(Drawable drawable) {
        mTintableDrawable = new TintableDrawable(drawable);
        mImageView.setImageDrawable(mTintableDrawable);
        applyTintMode();
        invalidate();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Tint
    // /////////////////////////////////////////////////////////////////////////

    public void setTintColor(int colorNormal, int colorDisabled, int colorActivated) {
        mColorNormal = colorNormal;
        mColorDisabled = colorDisabled;
        mColorActivated = colorActivated;
        applyTintMode();
    }

    public void setTintColorNormal(int colorNormal) {
        mColorNormal = colorNormal;
        applyTintMode();
    }

    public void setTintColorDisabled(int colorDisabled) {
        mColorDisabled = colorDisabled;
        applyTintMode();
    }

    public void setTintColorActivated(int colorActivated) {
        mColorActivated = colorActivated;
        applyTintMode();
    }

    public void setTintMode(int tintMode) {
        mTintMode = tintMode;
        applyTintMode();
    }

    private void applyTintMode() {
        if (mTintableDrawable == null) {
            return;
        }

        int tintMode;
        int colorNormal;
        int colorDisabled;
        int colorActivated;

        if (mIsAltState) {
            tintMode = mTintModeAlt;
            colorNormal = mColorNormalAlt;
            colorDisabled = mColorDisabledAlt;
            colorActivated = mColorActivatedAlt;
        } else {
            tintMode = mTintMode;
            colorNormal = mColorNormal;
            colorDisabled = mColorDisabled;
            colorActivated = mColorActivated;
        }

        if (tintMode == TINT_MODE_NORMAL) {
            mTintableDrawable.setTintColor(colorNormal, colorDisabled, colorActivated);

        } else if (tintMode == TINT_MODE_DISABLED) {
            mTintableDrawable.disableTint();

        } else if (tintMode == TINT_MODE_SINGLE_COLOR) {
            mTintableDrawable.setTintColor(colorNormal, colorNormal, colorNormal);

        } else {
            throw new IllegalArgumentException("Invalid tint mode value");
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Background
    // /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("deprecation")
    public void setCustomClickableArea(int clickableArea) {
        if (mSelectableArea == SELECTABLE_PARENT) {
            this.setBackgroundDrawable(mSelectableBackground);
            mImageView.setBackgroundDrawable(null);
        } else if (mSelectableArea == SELECTABLE_CHILD) {
            this.setBackgroundDrawable(null);
            mImageView.setBackgroundDrawable(mSelectableBackground);
        } else {
            this.setBackgroundDrawable(null);
            mImageView.setBackgroundDrawable(null);
        }
    }

    public void setSelectableBackground(Drawable selectableBackground) {
        mSelectableBackground = selectableBackground;
        setCustomClickableArea(mSelectableArea);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Image
    // /////////////////////////////////////////////////////////////////////////

    public void forceNormalState() {
        mIsAltState = false;
        setImageDrawable(mImageDrawableId);
    }

    public void forceAltState() {
        mIsAltState = true;
        setImageDrawable(mImageDrawableIdAlt);
    }

    public void toggle() {
        if (mImageDrawableId == NONE || mImageDrawableIdAlt == NONE) {
            return;
        }

        if (!mIsAltState) {
            forceAltState();
        } else {
            forceNormalState();
        }
    }

    public boolean isAltState() {
        return mIsAltState;
    }

    public void setImageDrawableId(int imageDrawableId) {
        mImageDrawableId = imageDrawableId;
    }

    public void setImageDrawableIdAlt(int imageDrawableIdAlt) {
        mImageDrawableIdAlt = imageDrawableIdAlt;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Dimension
    // /////////////////////////////////////////////////////////////////////////

    public void setContainerWidth(int width) {
        mLayoutWidth = width;
        this.setLayoutParams(new RelativeLayout.LayoutParams(mLayoutWidth, mLayoutHeight));
    }

    public void setContainerHeight(int height) {
        mLayoutHeight = height;
        this.setLayoutParams(new RelativeLayout.LayoutParams(mLayoutWidth, mLayoutHeight));
    }

    public void setContainerDimension(int width, int height) {
        mLayoutWidth = width;
        mLayoutHeight = height;
        this.setLayoutParams(new RelativeLayout.LayoutParams(mLayoutWidth, mLayoutHeight));
    }

    public void setImageWidth(int width) {
        mImageWidth = width;
        RelativeLayout.LayoutParams iconParam = new RelativeLayout.LayoutParams(mImageWidth,
                mImageHeight);
        iconParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(new RelativeLayout.LayoutParams(mLayoutWidth, mLayoutHeight));
    }

    public void setImageHeight(int height) {
        mImageHeight = height;
        RelativeLayout.LayoutParams iconParam = new RelativeLayout.LayoutParams(mImageWidth,
                mImageHeight);
        iconParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(new RelativeLayout.LayoutParams(mLayoutWidth, mLayoutHeight));
    }
}