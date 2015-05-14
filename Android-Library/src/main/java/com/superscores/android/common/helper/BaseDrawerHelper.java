package com.superscores.android.common.helper;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.superscores.android.R;
import com.superscores.android.common.base.AbstractExtraConfigActivity;
import com.superscores.android.common.base.ActivityLifeCycleDelegate;
import com.superscores.android.common.utils.InterfaceUtils;
import com.superscores.android.common.utils.PrefsUtils;
import com.superscores.android.common.utils.ScreenSizeUtils;
import com.superscores.android.common.utils.SuperLog;
import com.superscores.android.common.utils.SystemUtils;

/**
 * Base drawer helper class. <p/> Created by Pongpat on 2/16/15.
 */
public abstract class BaseDrawerHelper extends ActivityLifeCycleDelegate {

    private static final String EXTRA_STATE_DRAWER_VISIBILITY = "EXTRA_DRAWER_VISIBILITY";
    private static final int CHANGE_ACTIVITY_DELAY = 200;

    private DrawerConfig mDrawerConfig;

    private final AbstractExtraConfigActivity mActivity;
    private final Drawer mDrawer;

    protected ActionBar mActionBar;
    protected Handler mHandler;

    private DrawerLayout mDrawerLayout;
    private ViewGroup mDrawerContainer;

    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mIsNeedSaveDrawerState = false;
    private float mDrawerOffset;
    private int mDrawerLastState = DrawerLayout.STATE_IDLE;

    private int mActionBarSize;
    private int mDrawerWidth;
    private Drawable mDrawerShadow;

    // state
    private boolean mIsNeedRestoreDrawerState = true;
    private boolean mStateDrawerVisible = false;

    public static interface Drawer {
        public String getActivityTitle();
    }

    public interface DrawerConfig {
        public void onDrawerOpening();

        public void onDrawerClosing();
    }

    public static class DefaultDrawerConfig implements DrawerConfig {
        private BaseDrawerHelper mBaseDrawerHelper;

        public DefaultDrawerConfig(BaseDrawerHelper baseDrawerHelper) {
            mBaseDrawerHelper = baseDrawerHelper;
        }

        @Override
        public void onDrawerOpening() {
            mBaseDrawerHelper.onDrawerOpening();
        }

        @Override
        public void onDrawerClosing() {
            mBaseDrawerHelper.onDrawerClosing();
        }
    }

    protected abstract DrawerLayout onGetDrawerLayout();

    protected abstract ViewGroup onGetDrawerContainer();

    protected abstract void onInitDrawerLayout(ViewGroup drawerContainer);

    protected abstract Class<?> getFirstPageClass();

    public BaseDrawerHelper(@NonNull AbstractExtraConfigActivity activity) {
        this(activity, null);
    }

    public BaseDrawerHelper(@NonNull AbstractExtraConfigActivity activity,
            @Nullable DrawerConfig drawerConfig) {
        mActivity = activity;
        mDrawer = InterfaceUtils.attachInterface(activity, Drawer.class);

        if (drawerConfig == null) {
            mDrawerConfig = new DefaultDrawerConfig(this);
        } else {
            mDrawerConfig = drawerConfig;
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Lifecycle
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mStateDrawerVisible = savedInstanceState.getBoolean(EXTRA_STATE_DRAWER_VISIBILITY,
                    false);
        }

        mActionBar = mActivity.getSupportActionBar();
        mHandler = mActivity.getHandler();

        int[] attrs = new int[]{
                R.attr.actionBarSize,
                R.attr.drawerWidth,
                R.attr.drawerShadow};
        TypedArray ta = mActivity.obtainStyledAttributes(attrs);
        mActionBarSize = ta.getDimensionPixelSize(0, 0);
        mDrawerWidth = ta.getDimensionPixelSize(1, mActivity.getResources()
                .getDimensionPixelOffset(R.dimen.drawerWidth));
        mDrawerShadow = ta.getDrawable(2);
        ta.recycle();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onFirstResume() {
        super.onFirstResume();
        initDrawer();
        syncDrawerActionBar(mStateDrawerVisible, true);
    }

    @Override
    public void onSecondResume() {
        super.onSecondResume();
        onRestoreDrawerState();
    }

    @Override
    public void onPause() {
        super.onPause();
        onSaveDrawerState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_STATE_DRAWER_VISIBILITY, mStateDrawerVisible);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Drawer
    // /////////////////////////////////////////////////////////////////////////

    protected void initDrawer() {
        logDebug("initDrawer");

        if (mDrawerLayout == null || mDrawerContainer == null) {
            initDrawerLayout();
        }

        mDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout,
                R.string.drawer_left_open, R.string.drawer_left_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                syncDrawerActionBar(false, false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                syncDrawerActionBar(true, false);
                if (mIsNeedSaveDrawerState) {
                    PrefsUtils.saveDrawerActivelyOpened(mActivity);
                    mIsNeedSaveDrawerState = false;
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (isDrawerIndicatorEnabled()) {
                    super.onDrawerSlide(drawerView, slideOffset);
                }

                if (mDrawerLastState == DrawerLayout.STATE_SETTLING) {
                    if (mDrawerOffset < slideOffset) {
                        syncDrawerActionBar(true, false);
                    } else {
                        syncDrawerActionBar(false, false);
                    }
                }
                mDrawerOffset = slideOffset;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (isDrawerIndicatorEnabled()) {
                    super.onDrawerStateChanged(newState);
                }

                if (newState == DrawerLayout.STATE_DRAGGING) {
                    syncDrawerActionBar(true, false);
                } else if (newState == DrawerLayout.STATE_IDLE) {
                    syncDrawerActionBar(mDrawerLayout.isDrawerOpen(mDrawerContainer), false);
                }
                mDrawerLastState = newState;
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(isDrawerIndicatorEnabled());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (!PrefsUtils.isDrawerActivelyOpened(mActivity)) {
            if (getFirstPageClass().isInstance(mActivity)) {
                mDrawerLayout.openDrawer(mDrawerContainer);
                syncDrawerActionBar(true, true);
            }
            mIsNeedSaveDrawerState = true;
        }
    }

    private void initDrawerLayout() {
        logDebug("initDrawerLayout");
        mDrawerLayout = onGetDrawerLayout();
        mDrawerContainer = onGetDrawerContainer();

        // drawer should not wider than screen width minus action bar size
        int screenWidth = ScreenSizeUtils.getScreenWidth(mActivity);
        int maxDrawerWidth = screenWidth - mActionBarSize;
        if (mDrawerWidth > maxDrawerWidth) {
            mDrawerWidth = maxDrawerWidth;
        }
        ViewGroup.LayoutParams params = mDrawerContainer.getLayoutParams();
        params.width = mDrawerWidth;
        mDrawerContainer.setLayoutParams(params);
        mDrawerLayout.setDrawerShadow(mDrawerShadow, getDrawerGravity());

        onInitDrawerLayout(mDrawerContainer);

        if (mIsNeedRestoreDrawerState) {
            mIsNeedRestoreDrawerState = false;
            onRestoreDrawerState();
        }
    }

    @SuppressWarnings("unused")
    protected void syncDrawerActionBar(boolean isForceUpdate) {
        syncDrawerActionBar(mStateDrawerVisible, isForceUpdate);
    }

    protected void syncDrawerActionBar(boolean isDrawerVisible, boolean isForceUpdate) {
        if (!isForceUpdate && mStateDrawerVisible == isDrawerVisible) {
            return;
        }

        mStateDrawerVisible = isDrawerVisible;
        if (mStateDrawerVisible) {
            handleDrawerVisible();
        } else {
            handleDrawerInvisible();
            mActivity.supportInvalidateOptionsMenu();
        }
    }

    protected void startActivityCloseDrawer(final Intent intent) {
        if (intent == null) {
            return;
        }
        closeDrawer();

        /* if close drawer with start new activity the closing will not smooth */
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.startActivity(intent);
            }
        }, CHANGE_ACTIVITY_DELAY);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Configuration
    // /////////////////////////////////////////////////////////////////////////

    protected boolean isDrawerIndicatorEnabled() {
        return true;
    }

    protected int getDrawerGravity() {
        return GravityCompat.START;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Callback
    // /////////////////////////////////////////////////////////////////////////

    private void handleDrawerVisible() {
        onDrawerPreOpening();
        mDrawerConfig.onDrawerOpening();
    }

    private void handleDrawerInvisible() {
        onDrawerPreClosing();
        mDrawerConfig.onDrawerClosing();
    }

    protected void onDrawerPreOpening() {
    }

    protected void onDrawerOpening() {
        showActionBarTitle(SystemUtils.getApplicationName(mActivity)
                .toString());
    }

    protected void onDrawerPreClosing() {
    }

    protected void onDrawerClosing() {
        showActionBarTitle();
    }

    public void setDrawerConfig(DefaultDrawerConfig drawerConfig) {
        mDrawerConfig = drawerConfig;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Action bar title
    // /////////////////////////////////////////////////////////////////////////

    public void showActionBarTitle() {
        showActionBarTitle(getActivityTitle());
    }

    public void showActionBarTitle(String title) {
        showActionBarTitle(title, null);
    }

    public void showActionBarTitle(String title, String subTitle) {

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP |
                ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setTitle(title);
        if (!TextUtils.isEmpty(subTitle)) {
            mActionBar.setSubtitle(subTitle);
        } else {
            mActionBar.setSubtitle(null);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Misc.
    // /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    public void openDrawer() {
        if (mDrawerLayout != null && mDrawerContainer != null) {
            mDrawerLayout.openDrawer(mDrawerContainer);
        }
    }

    public void closeDrawer() {
        if (mDrawerLayout != null && mDrawerContainer != null) {
            mDrawerLayout.closeDrawer(mDrawerContainer);
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mDrawerContainer);
    }

    public boolean isDrawerVisible() {
        return mStateDrawerVisible;
    }

    protected void onSaveDrawerState() {

    }

    protected void onRestoreDrawerState() {

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter
    // /////////////////////////////////////////////////////////////////////////

    protected AppCompatActivity getActivity() {
        return mActivity;
    }

    public String getActivityTitle() {
        return mDrawer.getActivityTitle();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Log
    // /////////////////////////////////////////////////////////////////////////

    private void logDebug(String message) {
        SuperLog.debug(getClass(), SuperLog.LogTag.DRAWER, message);
    }
}