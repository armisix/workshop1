package com.superscores.android.pager;

import android.support.v4.app.Fragment;

public class MetaFragment {

    private String mTitle;
    private Fragment mFragment;

    public MetaFragment(Fragment fragment) {
        this(null, fragment);
    }

    public MetaFragment(String title, Fragment fragment) {
        mTitle = title;
        mFragment = fragment;
    }

    public String getTitle() {
        return mTitle;
    }

    public Fragment getFragment() {
        return mFragment;
    }
}