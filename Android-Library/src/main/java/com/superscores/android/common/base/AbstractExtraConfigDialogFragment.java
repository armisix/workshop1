package com.superscores.android.common.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superscores.android.common.utils.FragmentUtils;
import com.superscores.android.compat.DrawableCompat;
import com.superscores.android.exception.SuperNotCalledException;

/**
 * Base Dialog Fragment that implements extended lifecycle and extra configuration. <p/> Created by
 * Pongpat on 2/11/15.
 */
public abstract class AbstractExtraConfigDialogFragment extends DialogFragment {

    private FragmentLifeCycleDelegatorHelper mFragmentLifeCycleDelegatorHelper = new FragmentLifeCycleDelegatorHelper();

    private boolean mIsFirstResume = false;

    // is super called
    private boolean mIsFirstResumeSuperCalled;
    private boolean mIsSecondResumeSuperCalled;

    // /////////////////////////////////////////////////////////////////////////
    // Lifecycle
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentLifeCycleDelegatorHelper.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentLifeCycleDelegatorHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentLifeCycleDelegatorHelper.onCreateView(inflater, container, savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentLifeCycleDelegatorHelper.onViewCreated(view, savedInstanceState);

        if (Build.VERSION.SDK_INT < 11) {
            /*
             * manually set background color as SupportFragment do not support transparent
             * background
             */
            int attrs[] = new int[]{android.R.attr.windowBackground};
            TypedArray ta = getActivity().obtainStyledAttributes(attrs);
            DrawableCompat.setBackground(view, new ColorDrawable(ta.getColor(0, 0xFFE5E5E5)));
            ta.recycle();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentLifeCycleDelegatorHelper.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentLifeCycleDelegatorHelper.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentLifeCycleDelegatorHelper.onResume();

        if (!mIsFirstResume) {
            mIsFirstResume = true;
            performOnFirstResume();
        } else {
            performOnSecondResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mFragmentLifeCycleDelegatorHelper.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentLifeCycleDelegatorHelper.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentLifeCycleDelegatorHelper.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentLifeCycleDelegatorHelper.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentLifeCycleDelegatorHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mFragmentLifeCycleDelegatorHelper.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mFragmentLifeCycleDelegatorHelper.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mFragmentLifeCycleDelegatorHelper.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragmentLifeCycleDelegatorHelper.onActivityResult(requestCode, resultCode, data);

        //fix bug fragment do not pass onActivityResult() to child fragment
        FragmentUtils.dispatchOnActivityResult(getChildFragmentManager(), requestCode, resultCode,
                data);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Extended Lifecycle
    // /////////////////////////////////////////////////////////////////////////

    private void performOnFirstResume() {
        mIsFirstResumeSuperCalled = false;
        onFirstResume();
        if (!mIsFirstResumeSuperCalled) {
            throwSuperNotCalled("onFirstResume()");
        }
    }

    protected void onFirstResume() {
        mIsFirstResumeSuperCalled = true;
        mFragmentLifeCycleDelegatorHelper.onFirstResume();
    }

    private void performOnSecondResume() {
        mIsSecondResumeSuperCalled = false;
        onSecondResume();
        if (!mIsSecondResumeSuperCalled) {
            throwSuperNotCalled("onSecondResume()");
        }
    }

    protected void onSecondResume() {
        mIsSecondResumeSuperCalled = true;
        mFragmentLifeCycleDelegatorHelper.onSecondResume();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter
    // /////////////////////////////////////////////////////////////////////////

    protected FragmentLifeCycleDelegatorHelper getLifeCycleDelegatorHelper() {
        return mFragmentLifeCycleDelegatorHelper;
    }

    // /////////////////////////////////////////////////////////////////////////
    // inner class
    // /////////////////////////////////////////////////////////////////////////

    private void throwSuperNotCalled(String methodName) {
        throw new SuperNotCalledException(String.format("Fragment %1$s did not call through " +
                "super" + ".%2$s", this.toString(), methodName));
    }
}