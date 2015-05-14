package com.superscores.android.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.superscores.android.R;
import com.superscores.android.compat.DrawableCompat;
import com.superscores.android.widget.ObservableHorizontalScrollView;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@SuppressLint("NewApi")
public class TabPageIndicator extends RelativeLayout implements PageIndicator {

    private static final CharSequence EMPTY_TITLE = "";
    private static final Typeface PAGER_TYPEFACE = Typeface.DEFAULT_BOLD;

    private OnTabReselectedListener mTabReselectedListener;

    private Theme mPreferTheme;
    private ViewPager mViewPager;
    private OnPageChangeListener mListener;
    private ObservableHorizontalScrollView mTabContainer;
    private LinearLayout mTabLayout;
    private Indicator mIndicator;
    private int mSelectedTabIndex = 0;
    private int mDividerSize;
    private int mPagerTextSize;
    private int mPagerDividerMargin;
    private boolean mIsTabAdded = false;
    private ArrayList<TabIndicator> mTabList;

    private int mDividerColor;
    private int mSelectableItemBackgroundId;

    private Runnable mTabSelector;

    // /////////////////////////////////////////////////////////////////////////
    // Interface
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabIndicatorReselected(int position);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(new int[]{
                R.attr.pageIndicator_Background,
                R.attr.pageIndicator_Divider,
                R.attr.selectableItemBackground});

        setBackgroundColor(ta.getColor(0, getContext().getResources()
                .getColor(R.color.theme_dark_primary)));
        mDividerColor = ta.getColor(1, getContext().getResources()
                .getColor(R.color.divider_dark));
        mSelectableItemBackgroundId = ta.getResourceId(2, 0);
        ta.recycle();

        mTabContainer = new ObservableHorizontalScrollView(context);
        mTabLayout = new LinearLayout(context);
        mIndicator = new Indicator(context);

        mTabContainer.setOnScrollChangedListener(mOnScrollChangedListener);
        mTabContainer.setHorizontalFadingEdgeEnabled(false);
        mTabContainer.setHorizontalScrollBarEnabled(false);
        mTabContainer.addView(mTabLayout,
                new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));

        LayoutParams indicatorParams = new LayoutParams(MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.pager_indicator_height));
        indicatorParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        this.addView(mTabContainer, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        this.addView(mIndicator, indicatorParams);

        mDividerSize = getResources().getDimensionPixelSize(R.dimen.pager_divider_size);
        mPagerTextSize = getResources().getDimensionPixelSize(R.dimen.pager_text_size);
        mPagerDividerMargin = getResources().getDimensionPixelSize(R.dimen.pager_divider_margin);
    }

    public void setTheme(Theme theme) {
        mPreferTheme = theme;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Indicator moving
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        if (mIsTabAdded) {
            updateLayout(position, positionOffset);
        }
    }

    private void updateLayout(int position, final float positionOffset) {
        final View currentView = mTabLayout.getChildAt(getPositionWithDivider(position));
        final View targetView = mTabLayout.getChildAt(getPositionWithDivider(position + 1));

        if ((currentView != null && currentView.getWidth() == 0) || (targetView != null &&
                targetView.getWidth() == 0)) {
            // tab already add but not yet draw, wait until tab is draw
            mTabLayout.post(new Runnable() {
                @Override
                public void run() {
                    updateScrollPosition(currentView, targetView, positionOffset);
                    updateIndicator(currentView, targetView, positionOffset);
                }
            });
        } else {
            updateScrollPosition(currentView, targetView, positionOffset);
            updateIndicator(currentView, targetView, positionOffset);
        }
    }

    private void updateScrollPosition(View currentView, View targetView, float positionOffset) {
        int scrollPos = 0;

        if (targetView != null && currentView != null) {
            int currentViewCenterX = currentView.getLeft() + currentView.getWidth() / 2;
            int targetViewCenterX = targetView.getLeft() + targetView.getWidth() / 2;
            int totalOffset = (int) ((targetViewCenterX - currentViewCenterX) * positionOffset);

            scrollPos = currentView.getLeft() - (getWidth() - currentView.getWidth()) / 2;
            scrollPos += totalOffset;
        } else {
            if (currentView == null) {
                // scroll to the beginning
                scrollPos = 0;
            }
            if (targetView == null) {
                // scroll to the end
                scrollPos = mTabLayout.getWidth();
            }
        }
        mTabContainer.scrollTo(scrollPos, 0);
    }

    private void updateIndicator(View currentView, View targetView, float positionOffset) {
        int currentWidth;
        int targetWidth;
        int currentOffset;
        int targetOffset;

        if (currentView != null) {
            currentWidth = currentView.getWidth();
            currentOffset = currentView.getLeft();
        } else {
            currentWidth = targetView != null ? targetView.getWidth() : 0;
            currentOffset = targetView != null ? targetView.getLeft() : 0;
        }

        if (targetView != null) {
            targetWidth = targetView.getWidth();
            targetOffset = targetView.getLeft();
        } else {
            targetWidth = currentView != null ? currentView.getWidth() : 0;
            targetOffset = currentView != null ? currentView.getLeft() : 0;
        }
        mIndicator.onUpdateLayout(currentWidth, targetWidth, currentOffset, targetOffset,
                mTabContainer.getScrollX(), positionOffset);
    }

    private ObservableHorizontalScrollView.OnScrollChangedListener mOnScrollChangedListener = new
            ObservableHorizontalScrollView.OnScrollChangedListener() {
        @Override
        public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y,
                int oldx, int oldy) {
            View currentView = mTabLayout.getChildAt(getPositionWithDivider(mSelectedTabIndex));
            mIndicator.onUpdateLayout(currentView != null ? currentView.getLeft() : 0, x);
        }
    };

    private int getPositionWithDivider(int position) {
        return position * 2;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Add tab
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void notifyDataSetChanged() {
        if (getWidth() == 0) {
            // when orientation the tab may not draw before notifyDataSetChanged
            // postpone draw
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        processTab();

                        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
                        if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                            viewTreeObserver.removeGlobalOnLayoutListener(this);
                        }
                    }
                });
            }
        } else {
            processTab();
        }
    }

    private void processTab() {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter == null || adapter.getCount() == 0) {
            return;
        }

        mTabLayout.removeAllViews();

        Paint paint = new Paint();
        paint.setTextSize(mPagerTextSize);
        paint.setTypeface(PAGER_TYPEFACE);

        final int count = adapter.getCount();
        int tabPadding = getResources().getDimensionPixelSize(R.dimen.pager_tab_padding) * 2;
        int tabContainerWidth = getWidth();
        int totalTabWidth = 0;
        int totalDividerSize = mDividerSize * (count - 1);
        int tabWidth[] = new int[count];
        mTabList = new ArrayList<>(count);

        // calculate text length plus normal padding, plus divider
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            tabWidth[i] = (int) paint.measureText(
                    (String) (title != null ? title : EMPTY_TITLE)) + tabPadding;
            totalTabWidth += tabWidth[i];
        }
        totalTabWidth += totalDividerSize;

        // handle extra padding
        if (totalTabWidth < tabContainerWidth) {
            int extraPadding = (tabContainerWidth - totalTabWidth) / count;

            // re-calculate again to handle floating point round issue
            // calculate text length plus extra padding
            totalTabWidth = 0;
            for (int i = 0; i < count; i++) {
                tabWidth[i] += extraPadding;
                totalTabWidth += tabWidth[i];
            }
            totalTabWidth += totalDividerSize;

            if (totalTabWidth != tabContainerWidth) {
                int adjustIndex = 0;
                // rounding problem
                int currentMin = Integer.MAX_VALUE;
                for (int i = 0; i < count; i++) {
                    if (tabWidth[i] < currentMin) {
                        currentMin = tabWidth[i];
                        adjustIndex = i;
                    }
                }
                int adjustPixel = tabContainerWidth - totalTabWidth;
                tabWidth[adjustIndex] += adjustPixel;
            }
        }

        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            mTabList.add(new TabIndicator((String) title, tabWidth[i]));
        }

        // handle tab views
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(mDividerSize,
                LayoutParams.MATCH_PARENT);
        dividerParams.setMargins(0, mPagerDividerMargin, 0, mPagerDividerMargin);

        for (int i = 0; i < mTabList.size(); i++) {
            final TabView tabView = new TabView(getContext());
            tabView.mIndex = i;
            tabView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mPagerTextSize);
            tabView.setTypeface(PAGER_TYPEFACE);
            tabView.setFocusable(true);
            tabView.setGravity(Gravity.CENTER);
            tabView.setText(mTabList.get(i)
                    .getTitle());
            tabView.setOnClickListener(mTabClickListener);
            DrawableCompat.setBackground(tabView, mSelectableItemBackgroundId, mPreferTheme);

            mTabLayout.addView(tabView, new LinearLayout.LayoutParams(mTabList.get(i)
                    .getTabWidth(), LayoutParams.MATCH_PARENT));

            if (i < mTabList.size() - 1) {
                // do not add divider for last index
                View divider = new View(getContext());
                divider.setBackgroundColor(mDividerColor);
                mTabLayout.addView(divider, dividerParams);
            }
        }

        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);

        mIsTabAdded = true;
        setVisibility(View.VISIBLE);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Tab pressed
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabIndicatorReselected(newSelected);
            } else {
                mViewPager.setCurrentItem(newSelected);
            }
        }
    };

    // /////////////////////////////////////////////////////////////////////////
    // Misc.
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void setViewPager(ViewPager view) {
        setViewPager(view, mSelectedTabIndex);
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }

        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        mSelectedTabIndex = initialPosition;
        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(mSelectedTabIndex, false);
        notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mSelectedTabIndex = position;
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setCurrentItem(int position) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }

        mSelectedTabIndex = position;
        updateLayout(mSelectedTabIndex, 0.0f);
        mViewPager.setCurrentItem(position, false);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == getPositionWithDivider(position));
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(getPositionWithDivider(position));
            }
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                mTabContainer.smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    public ArrayList<String> getTabTitles() {
        if (mTabList == null) {
            return null;
        }
        ArrayList<String> titles = new ArrayList<>(mTabList.size());
        for (int i = 0; i < mTabList.size(); i++) {
            titles.add(mTabList.get(i)
                    .getTitle());
        }
        return titles;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Inner class
    // /////////////////////////////////////////////////////////////////////////

    private static class TabIndicator {
        public String mTitle;
        public int mTabWidth;

        public TabIndicator(String title, int tabWidth) {
            mTitle = title;
            mTabWidth = tabWidth;
        }

        public String getTitle() {
            return mTitle;
        }

        public int getTabWidth() {
            return mTabWidth;
        }
    }

    private static class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.textAppearanceActionBarSubTitle);
        }

        public int getIndex() {
            return mIndex;
        }
    }

    private static class Indicator extends View {

        private Paint mPaintIndicator;
        private Paint mPaintDivider;
        private int mDividerHeight;
        private int mPosition;
        private int mSize;

        public Indicator(Context context) {
            super(context);

            mDividerHeight = getResources().getDimensionPixelSize(
                    R.dimen.pager_indicator_highlight_height);

            int attrs[] = new int[]{
                    R.attr.colorAccent,
                    R.attr.pageIndicator_Divider};
            TypedArray ta = context.obtainStyledAttributes(attrs);

            mPaintIndicator = new Paint();
            mPaintIndicator.setColor(ta.getColor(0, getContext().getResources()
                    .getColor(R.color.theme_dark_primary)));

            mPaintDivider = new Paint();
            mPaintDivider.setColor(ta.getColor(1, getContext().getResources()
                    .getColor(R.color.divider_dark)));

            ta.recycle();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(mPosition, 0, mPosition + mSize, getHeight(), mPaintIndicator);
            canvas.drawRect(0, getHeight() - mDividerHeight, getWidth(), getHeight(),
                    mPaintDivider);
        }

        public void onUpdateLayout(int currentSize, int targetSize, int currentOffset,
                int targetOffset, int scrollOffset, float positionOffset) {
            mSize = ((int) ((targetSize - currentSize) * positionOffset)) + currentSize;
            mPosition = ((int) ((targetOffset - currentOffset) * positionOffset)) + currentOffset
                    - scrollOffset;
            invalidate();
        }

        public void onUpdateLayout(int currentOffset, int scrollOffset) {
            mPosition = currentOffset - scrollOffset;
            invalidate();
        }
    }
}