package com.superscores.android.pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.superscores.android.common.utils.UiUtils;

import java.util.ArrayList;

public class PagerHelper implements ViewPager.OnPageChangeListener,
        TabPageIndicator.OnTabReselectedListener, PageChooserDialogFragment.OnPageSelectedListener {

    //Pager divider size in dp unit
    private static final int PAGER_DIVIDER = 1;

    private static final String TAG_PAGER_CHOOSER_DIALOG = "TAG_PAGER_CHOOSER_DIALOG";
    private static final int DEFAULT_PAGE_INDEX = 1;

    private Context mContext;
    private FragmentManager mFragmentManager;

    private ViewPager mViewPager;
    private TabPageIndicator mPageIndicator;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    private int mCurrentPageIndex = DEFAULT_PAGE_INDEX;
    private int mLastPageIndex = 0;

    public PagerHelper(@NonNull Context context, @NonNull FragmentManager fragmentManager,
            @NonNull ViewPager viewPager) {
        this(context, fragmentManager, viewPager, null);
    }

    @SuppressWarnings({"all"})
    public PagerHelper(@NonNull Context context, @NonNull FragmentManager fragmentManager,
            @NonNull ViewPager viewPager, @Nullable TabPageIndicator pageIndicator) {

        mContext = context;
        mFragmentManager = fragmentManager;

        PageChooserDialogFragment pageChooserDialog = (PageChooserDialogFragment) fragmentManager
                .findFragmentByTag(TAG_PAGER_CHOOSER_DIALOG);
        if (pageChooserDialog != null) {
            //in case of orientation change, if chooser dialog exist we register new listener to it.
            pageChooserDialog.registerOnTabSelectedListener(this);
        }

        mViewPager = viewPager;
        mPageIndicator = pageIndicator;

        viewPager = null;

        if (mPageIndicator != null) {
            mPageIndicator.setOnTabReselectedListener(this);
            mPageIndicator.setTheme(mContext.getTheme());
        }

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setPageMargin(UiUtils.dpToPx(mContext, PAGER_DIVIDER));
        mViewPager.setPageMarginDrawable(mContext.getResources()
                .getDrawable(android.R.color.white));
    }

    public void setAdapter(ExtendedFragmentStatePagerAdapter pagerAdapter) {
        setAdapter(pagerAdapter, DEFAULT_PAGE_INDEX);
    }

    public void setAdapter(ExtendedFragmentStatePagerAdapter pagerAdapter, int currentPageIndex) {
        mCurrentPageIndex = currentPageIndex;

        mViewPager.setAdapter(pagerAdapter);

        if (mPageIndicator != null) {
            /*
             * TODO: Fix bug when call mViewPager.setCurrentItem(mStateCurrentPageIndex) for
             * activity that create pagerIndicator after load finish and pagerIndicator not at
             * correct position
             */
            mPageIndicator.setViewPager(mViewPager, mCurrentPageIndex);
            mPageIndicator.setOnPageChangeListener(this);
        } else {
            mViewPager.setOnPageChangeListener(this);
        }

        if (mPageIndicator != null) {
            mPageIndicator.notifyDataSetChanged();
        }
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }

        if (mPageIndicator == null) {
            /*
             * TODO: Remove this when able to fix mViewPager.setCurrentItem(mStateCurrentPageIndex)
             * bug
             */
            mViewPager.setCurrentItem(mCurrentPageIndex);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Tab callback
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void onTabIndicatorReselected(int position) {
        if (mPageIndicator != null) {
            ArrayList<String> tabTitles = mPageIndicator.getTabTitles();
            if (tabTitles != null) {
                PageChooserDialogFragment pageChooserDialog = PageChooserDialogFragment
                        .newInstance(tabTitles, getCurrentPageIndex());
                pageChooserDialog.show(mFragmentManager, TAG_PAGER_CHOOSER_DIALOG);
                pageChooserDialog.registerOnTabSelectedListener(this);
            }
        }
    }

    @Override
    public void onTabDialogSelected(int index) {
        setPageIndex(index);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Pager callback
    // /////////////////////////////////////////////////////////////////////////

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (listener != null) {
            mOnPageChangeListener = listener;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mLastPageIndex = mCurrentPageIndex;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        mCurrentPageIndex = position;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Fragment
    // /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    public <T> T getCurrentFragment(Class<T> type) {
        return getFragmentAtIndex(mViewPager.getCurrentItem(), type);
    }

    @SuppressWarnings("unchecked")
    public <T> T getFragmentAtIndex(Integer index, Class<T> type) {
        if (mViewPager == null || mViewPager.getAdapter() == null || index == null) {
            return null;
        }
        if (index >= 0 && index < mViewPager.getAdapter()
                .getCount()) {
            ExtendedFragmentStatePagerAdapter adapter = (ExtendedFragmentStatePagerAdapter)
                    mViewPager.getAdapter();
            Object temp = adapter.getRegisteredFragment(index);
            if (type.isInstance(temp)) {
                return (T) temp;
            }
        }
        return null;
    }

    //@SuppressWarnings("unused")
    //public <T> T findFragmentByPageId(int pageId, Class<T> type) {
    //    if (mViewPager == null || mViewPager.getAdapter() == null) {
    //        return null;
    //    }
    //    for (int i = 0; i < mViewPager.getAdapter()
    //            .getCount(); i++) {
    //        ExtendedFragmentStatePagerAdapter adapter = (ExtendedFragmentStatePagerAdapter)
    //                mViewPager.getAdapter();
    //        Dep_AbstractLoaderFragment<?> loaderFragment = (Dep_AbstractLoaderFragment<?>)
    //                adapter.getRegisteredFragment(i);
    //        int tempPageId = loaderFragment.getPageId();
    //        if (tempPageId == pageId && type.isInstance(loaderFragment)) {
    //            @SuppressWarnings("unchecked")
    //            T fragment = (T) loaderFragment;
    //            return fragment;
    //        }
    //    }
    //    return null;
    //}

    @SuppressWarnings("unused")
    public ArrayList<Fragment> getActiveFragments() {
        if (mViewPager == null || mViewPager.getAdapter() == null) {
            return null;
        }
        ExtendedFragmentStatePagerAdapter adapter = (ExtendedFragmentStatePagerAdapter)
                mViewPager.getAdapter();
        return adapter != null ? adapter.getRegisteredFragment() : null;
    }

    @SuppressWarnings("unused")
    public <T> ArrayList<T> getActiveFragments(Class<T> type) {
        if (mViewPager == null || mViewPager.getAdapter() == null) {
            return null;
        }
        ExtendedFragmentStatePagerAdapter adapter = (ExtendedFragmentStatePagerAdapter)
                mViewPager.getAdapter();
        ArrayList<Fragment> fragments = adapter.getRegisteredFragment();
        ArrayList<T> typedFragments = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (type.isInstance(fragment)) {
                @SuppressWarnings("unchecked")
                T typedFragment = (T) fragment;
                typedFragments.add(typedFragment);
            }
        }
        return typedFragments;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Pager
    // /////////////////////////////////////////////////////////////////////////

    public int getCurrentPageIndex() {
        return mViewPager.getCurrentItem();
    }

    public void setPageIndex(int index) {
        mViewPager.setCurrentItem(index, true);
    }

    public int getLastPageIndex() {
        return mLastPageIndex;
    }

    @SuppressWarnings("unused")
    public void setOffScreenPageLimit(int limit) {
        mViewPager.setOffscreenPageLimit(limit);
    }

    @SuppressWarnings("unused")
    public int getOffScreenPageLimit() {
        return mViewPager.getOffscreenPageLimit();
    }

    @SuppressWarnings("unused")
    public void setPageIndicatorVisibility(int visibility) {
        mPageIndicator.setVisibility(visibility);
    }
}