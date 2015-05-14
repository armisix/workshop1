package com.superscores.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.nineoldandroids.view.ViewHelper;
import com.superscores.android.R;
import com.superscores.android.common.utils.SuperLog;

import java.util.ArrayList;

/**
 * The SwipeDismissLayout is the modified version of SwipeRefreshLayout. It should be used whenever
 * the user can dismiss the contents of a view via a vertical swipe gesture. The activity or
 * fragment that instantiates this view should add an OnDismissListener to be notified whenever the
 * swipe to dissmiss gesture is completed. <p/> <p> This layout should be made the parent of the
 * view that will be refreshed as a result of the gesture and can only support one direct child.
 * This view will also be made the target of the gesture and will be forced to match both the width
 * and the height supplied in this layout. The SwipeDismissLayout does not provide accessibility
 * events; instead, there should be an option provided to allow dismiss of the content wherever this
 * gesture is used. </p>
 *
 * @author Pongpat Ratanaamornpin
 */
public class SwipeDismissLayout extends FrameLayout {

    // tag for the scrollable view within container, if no view with this tag found we assume that
    // the container is scrollable
    private final String mTagScrollable;

    private static final float DISMISS_MIN_SCALE = 0.05f;
    private static final float TRIGGER_DISTANCE_FACTOR = .25f;
    private static final float TRIGGER_INSTANTLY_DISTANCE_FACTOR = .50f;

    private static final int MAX_TRIGGER_DISTANCE = 512;
    private static final int INVALID_POINTER = -1;

    private OnDismissListener mOnDismissListener;

    private ViewGroup mContainer;
    private ArrayList<View> mScrollTarget;// the content that gets pulled down

    // private float mOriginPosX = -1;
    // private float mOriginPosY = -1;

    private boolean mIsAnimateAdd = false;
    private boolean mIsAdding = false;
    private boolean mIsDismissing = false;
    private int mTouchSlop;
    private float mDistanceToDismiss = -1;
    private float mDistanceToDismissInstantly = -1;

    private float mInitialMotionY;
    private float mDiffY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;

    private Spring mTranslationSpring;
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mIsReturning;
    private static final int[] LAYOUT_ATTRS = new int[]{android.R.attr.enabled};

    public interface OnDismissListener {
        public void onDismissed();
    }

    public SwipeDismissLayout(Context context) {
        this(context, null);
    }

    public SwipeDismissLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTagScrollable = context.getString(R.string.tag_scrollable);
        mTranslationSpring = mSpringSystem.createSpring();
        mTouchSlop = ViewConfiguration.get(context)
                .getScaledTouchSlop();
        setWillNotDraw(false);

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTranslationSpring.addListener(mTranslationSpringListener);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTranslationSpring.addListener(mTranslationSpringListener);
    }

    @SuppressWarnings("unused")
    public void setOrigin(float posX, float posY) {
        // mOriginPosX = posX;
        // mOriginPosY = posY;
        // applyPivot();
    }

    // private void applyPivot() {
    // if (mContainer != null && mTarget != null) {
    // float pivotX = mOriginPosX + mContainer.getLeft();
    // float pivotY = mOriginPosY + mContainer.getTop();
    // ViewHelper.setPivotX(mContainer, pivotX);
    // ViewHelper.setPivotY(mContainer, pivotY);
    // }
    // }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid out yet.
        if (mContainer == null) {
            if (getChildCount() > 1 && !isInEditMode()) {
                throw new IllegalStateException(
                        getClass().getSimpleName() + " can host only one direct child");
            }
            mContainer = (ViewGroup) getChildAt(0);
            mContainer.post(new Runnable() {
                @Override
                public void run() {
                    if (mIsAnimateAdd) {
                        mTranslationSpring.setCurrentValue(0);
                        animateAdd();
                    } else {
                        mTranslationSpring.setCurrentValue(1);
                    }
                }
            });
        }
        if (mScrollTarget == null) {
            mScrollTarget = new ArrayList<>();
            recursiveTransverseView(mContainer);

            if (mScrollTarget.size() == 0) {
                // if no target found use container as a target
                mScrollTarget.add(mContainer);
            }
        }
    }

    private void recursiveTransverseView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            searchForScrollableTag(viewGroup);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (!(viewGroup instanceof AbsListView) && !(viewGroup instanceof ScrollView) &&
                        !(viewGroup instanceof RecyclerView)) {
                    // do not recursive child if list is already scrollable
                    recursiveTransverseView(viewGroup.getChildAt(i));
                }
            }
        } else {
            searchForScrollableTag(view);
        }
    }

    private void searchForScrollableTag(View view) {
        Object tag = view.getTag();
        if (tag instanceof String) {
            String tagName = (String) tag;
            if (tagName.compareTo(mTagScrollable) == 0) {
                mScrollTarget.add(view);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        final View child = getChildAt(0);
        final int childLeft = getPaddingLeft();
        final int childTop = child.getTop() + getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        ensureTarget();
        if (changed) {
            ViewHelper.setPivotX(mContainer, mContainer.getWidth());
            ViewHelper.setPivotY(mContainer, mContainer.getHeight());

            if (mContainer != null && mContainer.getHeight() > 0) {
                mDistanceToDismiss = (int) (mContainer.getHeight() * TRIGGER_DISTANCE_FACTOR);
            }
            if (mContainer != null && mContainer.getHeight() > 0) {
                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDistanceToDismissInstantly = (int) Math.min(
                        mContainer.getHeight() * TRIGGER_INSTANTLY_DISTANCE_FACTOR,
                        MAX_TRIGGER_DISTANCE * metrics.density);
            }
        }

        // if (mIsAnimateAdd) {
        // mTranslationSpring.setCurrentValue(0);
        // animateAdd("onLayout " + changed);
        // } else {
        // mTranslationSpring.setCurrentValue(1);
        // }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1 && !isInEditMode()) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " can host only one direct child");
        }
        if (getChildCount() > 0) {
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(
                    getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(
                            getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                            MeasureSpec.EXACTLY));
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to scroll up. Override this
     * if the child view is a custom view.
     */
    private boolean canChildScrollUp(float posX, float posY) {
        for (int i = 0; i < mScrollTarget.size(); i++) {
            View view = mScrollTarget.get(i);
            if (isViewContains(view, (int) posX, (int) posY)) {
                if (android.os.Build.VERSION.SDK_INT < 14) {
                    if (view instanceof AbsListView) {
                        final AbsListView absListView = (AbsListView) view;

                        int childCount = absListView.getChildCount();
                        int firstChildTop = absListView.getChildAt(0)
                                .getTop();
                        return childCount > 0 && (absListView.getFirstVisiblePosition() > 0 ||
                                firstChildTop < absListView.getPaddingTop());
                    } else {
                        return view.getScrollY() > 0;
                    }
                } else {
                    return ViewCompat.canScrollVertically(view, -1);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        final int action = MotionEventCompat.getActionMasked(ev);

        if (mIsReturning && action == MotionEvent.ACTION_DOWN) {
            mIsReturning = false;
        }

        if (!isEnabled() || mIsReturning || canChildScrollUp(ev.getX(), ev.getY())) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionY = ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    logError("Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    logError("Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = y - mInitialMotionY;

                if (!mIsBeingDragged && yDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (mIsReturning && action == MotionEvent.ACTION_DOWN) {
            mIsReturning = false;
        }

        if (!isEnabled() || mIsReturning || canChildScrollUp(ev.getX(), ev.getY())) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionY = ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    logError("Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                mDiffY = y - mInitialMotionY;

                if (!mIsBeingDragged && mDiffY > mTouchSlop) {
                    mIsBeingDragged = true;
                }

                if (mIsBeingDragged) {
                    if (mDiffY > mDistanceToDismissInstantly) {
                        animateDismiss();
                    } else {
                        updateScale((int) (mDiffY));
                    }
                }
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (mDiffY > mDistanceToDismiss) {
                    animateDismiss();
                } else {
                    returnToStartPosition();
                }
                return false;
        }

        return true;
    }

    private void returnToStartPosition() {
        mIsReturning = true;
        mTranslationSpring.setEndValue(1);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Updating
    // /////////////////////////////////////////////////////////////////////////

    private void animateAdd() {
        if (!mIsAdding) {
            ensureTarget();
            mIsAdding = true;
            mTranslationSpring.setEndValue(1);
        }
    }

    public void setAnimateAdd(boolean isAnimateAdd) {
        this.mIsAnimateAdd = isAnimateAdd;
    }

    public void animateDismiss() {
        ensureTarget();
        mIsDismissing = true;
        mTranslationSpring.setEndValue(0);
    }

    private void updateScale(int distanceMove) {
        if (distanceMove > mDistanceToDismissInstantly) {
            distanceMove = (int) mDistanceToDismissInstantly;
        } else if (distanceMove < 0) {
            distanceMove = 0;
        }

        float mappedSpringValue = (float) SpringUtil.mapValueFromRangeToRange(distanceMove, 0,
                mDistanceToDismissInstantly, 1, TRIGGER_INSTANTLY_DISTANCE_FACTOR);
        mTranslationSpring.setCurrentValue(mappedSpringValue, true);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Animation
    // /////////////////////////////////////////////////////////////////////////

    private SimpleSpringListener mTranslationSpringListener = new SimpleSpringListener() {
        @Override
        public void onSpringUpdate(Spring spring) {
            double springValue = spring.getCurrentValue();
            float mappedScale = (float) SpringUtil.mapValueFromRangeToRange(springValue, 0, 1,
                    mContainer.getHeight(), 0);

            ViewHelper.setTranslationY(mContainer, mappedScale);
            // ViewHelper.setScaleX(mContainer, mappedScale);
            // ViewHelper.setScaleY(mContainer, mappedScale);

            if (spring.isAtRest()) {
                mIsReturning = false;
                if (mIsAdding) {
                    mIsAnimateAdd = false;
                }
            }

            if (mIsDismissing) {
                if (springValue < DISMISS_MIN_SCALE) {
                    mIsDismissing = false;
                    if (mOnDismissListener != null) {
                        mOnDismissListener.onDismissed();
                    }
                }
            }
        }
    };

    // /////////////////////////////////////////////////////////////////////////
    // Misc.
    // /////////////////////////////////////////////////////////////////////////

    private boolean isViewContains(View view, int rx, int ry) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        Rect rect = new Rect(l[0], l[1], l[0] + view.getWidth(), l[1] + view.getHeight());
        return rect.contains(rx, ry);
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    private void logError(String message) {
        SuperLog.error(SuperLog.LogTag.GENERAL, message);
    }
}