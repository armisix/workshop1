package com.superscores.android.common.helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.superscores.android.R;
import com.superscores.android.common.base.ActivityLifeCycleDelegate;

@SuppressWarnings("unused")
public class SearchStateHelper extends ActivityLifeCycleDelegate {

    private static final String CLASS_NAME = SearchStateHelper.class.getSimpleName();
    private static final String EXTRA_IS_SEARCH_ACTIVATE = CLASS_NAME + "EXTRA_IS_SEARCH_ACTIVATE";
    private static final String EXTRA_LAST_QUERY = CLASS_NAME + "EXTRA_LAST_QUERY";

    private OnSearchStateChangeListener mOnSearchStateChangeListener;

    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;
    private AutoCompleteTextView mSearchAutoComplete;

    private boolean mIsStateRestored = false;
    private boolean mStateIsSearchActivated;
    private CharSequence mStateLastSearchQuery;

    public interface OnSearchStateChangeListener {
        public void onSearchStateChanged(boolean isActivate);
    }

    public SearchStateHelper(@Nullable OnSearchStateChangeListener listener) {
        mOnSearchStateChangeListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mStateIsSearchActivated = savedInstanceState.getBoolean(EXTRA_IS_SEARCH_ACTIVATE,
                    false);
            mStateLastSearchQuery = savedInstanceState.getCharSequence(EXTRA_LAST_QUERY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mSearchMenuItem != null && mSearchView != null) {
            outState.putBoolean(EXTRA_IS_SEARCH_ACTIVATE, isSearchActivate());
            outState.putCharSequence(EXTRA_LAST_QUERY, mSearchView.getQuery());
        }
    }

    public void setSearchItem(@NonNull MenuItem searchItem) {
        mSearchMenuItem = searchItem;
        MenuItemCompat.setOnActionExpandListener(searchItem, mOnActionExpandListener);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView = searchView;
        mSearchAutoComplete = (AutoCompleteTextView) mSearchView.findViewById(R.id.search_src_text);

        if (!mIsStateRestored) {
            mIsStateRestored = true;
            if (mStateIsSearchActivated) {
                activateSearch();
                if (!TextUtils.isEmpty(mStateLastSearchQuery)) {
                    mSearchView.setQuery(mStateLastSearchQuery, true);
                }
            }
        }
    }

    private OnActionExpandListener mOnActionExpandListener = new OnActionExpandListener() {
        @Override
        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            if (mOnSearchStateChangeListener != null) {
                mOnSearchStateChangeListener.onSearchStateChanged(true);
            }
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            if (mOnSearchStateChangeListener != null) {
                mOnSearchStateChangeListener.onSearchStateChanged(false);
            }
            return true;
        }
    };

    public boolean isSearchActivate() {
        return MenuItemCompat.isActionViewExpanded(mSearchMenuItem);
    }

    public void activateSearch() {
        MenuItemCompat.expandActionView(mSearchMenuItem);
    }

    public void dismissSearch() {
        MenuItemCompat.collapseActionView(mSearchMenuItem);
    }

    public SearchView getSearchView() {
        return mSearchView;
    }

    public boolean isAutoCompleteAvailable() {
        return mSearchAutoComplete != null;
    }

    public AutoCompleteTextView getAutoCompleteTextView() {
        return mSearchAutoComplete;
    }

    public CharSequence getLastSearchQuery() {
        if (mSearchView != null) {
            return mSearchView.getQuery();
        } else {
            return "";
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Delegate
    // /////////////////////////////////////////////////////////////////////////

    public void setQueryHint(CharSequence hint) {
        checkInit();
        mSearchView.setQueryHint(hint);
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        setOnQueryTextListener(listener, true);
    }

    public void setOnQueryTextListener(OnQueryTextListener listener, boolean submitExistingQuery) {
        checkInit();
        mSearchView.setOnQueryTextListener(listener);
        if (submitExistingQuery) {
            listener.onQueryTextSubmit(mSearchView.getQuery()
                    .toString());
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Exception
    // /////////////////////////////////////////////////////////////////////////

    private void checkInit() {
        if (mSearchView == null) {
            throw new IllegalStateException(
                    "You must call setSearchView before doing some " + "operation on searchView");
        }
    }
}