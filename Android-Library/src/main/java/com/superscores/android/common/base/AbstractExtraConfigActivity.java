package com.superscores.android.common.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.superscores.android.common.utils.FragmentUtils;
import com.superscores.android.exception.SuperNotCalledException;

/**
 * Base Activity that implements extended lifecycle and extra configuration. <p/> <p/> Created by
 * Pongpat on 2/11/15.
 */
public abstract class AbstractExtraConfigActivity extends AppCompatActivity {

    private ActivityLifeCycleDelegatorHelper mActivityLifeCycleDelegatorHelper = new ActivityLifeCycleDelegatorHelper();
    private Handler mHandler;

    private boolean mIsFirstResume = true;
    private boolean mIsFirstResumeFragments = true;

    // is super called
    private boolean mIsFirstResumeSuperCalled;
    private boolean mIsSecondResumeSuperCalled;
    private boolean mIsFirstResumeFragmentsSuperCalled;
    private boolean mIsSecondResumeFragmentsSuperCalled;

    private boolean mIsStateAlreadySaved = true;

    // /////////////////////////////////////////////////////////////////////////
    // Configuration
    // /////////////////////////////////////////////////////////////////////////

    protected boolean isStrictModeEnable() {
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Lifecycle
    // /////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLifeCycleDelegatorHelper.onCreate(savedInstanceState);
        initStrictMode();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActivityLifeCycleDelegatorHelper.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mActivityLifeCycleDelegatorHelper.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivityLifeCycleDelegatorHelper.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivityLifeCycleDelegatorHelper.onResume();

        /* custom activity life cycle */
        if (mIsFirstResume) {
            performOnFirstResume();
            mIsFirstResume = false;
        } else {
            performOnSecondResume();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mActivityLifeCycleDelegatorHelper.onPostResume();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mActivityLifeCycleDelegatorHelper.onResumeFragments();

        mIsStateAlreadySaved = false;
        if (mIsFirstResumeFragments) {
            performOnFirstResumeFragments();
            mIsFirstResumeFragments = false;
        } else {
            performOnSecondResumeFragments();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityLifeCycleDelegatorHelper.onPause();

        mIsStateAlreadySaved = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActivityLifeCycleDelegatorHelper.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityLifeCycleDelegatorHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mActivityLifeCycleDelegatorHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mActivityLifeCycleDelegatorHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActivityLifeCycleDelegatorHelper.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mActivityLifeCycleDelegatorHelper.onActivityResult(requestCode, resultCode, data);

        // dispatch on activity result to fragments
        FragmentUtils.dispatchOnActivityResult(getSupportFragmentManager(), requestCode, resultCode,
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
        mActivityLifeCycleDelegatorHelper.onFirstResume();
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
        mActivityLifeCycleDelegatorHelper.onSecondResume();
    }

    private void performOnFirstResumeFragments() {
        mIsFirstResumeFragmentsSuperCalled = false;
        onFirstResumeFragments();
        if (!mIsFirstResumeFragmentsSuperCalled) {
            throwSuperNotCalled("onFirstResumeFragments()");
        }
    }

    /**
     * This will called on first resume only. Use for setup anything after onCreate().
     */
    protected void onFirstResumeFragments() {
        mIsFirstResumeFragmentsSuperCalled = true;
        mActivityLifeCycleDelegatorHelper.onFirstResumeFragments();
    }

    private void performOnSecondResumeFragments() {
        mIsSecondResumeFragmentsSuperCalled = false;
        onSecondResumeFragments();
        if (!mIsSecondResumeFragmentsSuperCalled) {
            throwSuperNotCalled("onSecondResumeFragments()");
        }
    }

    /**
     * This will called on second and subsequently resume.<br> Typically use for update view when
     * user press back to created activity.
     */
    protected void onSecondResumeFragments() {
        mIsSecondResumeFragmentsSuperCalled = true;
        mActivityLifeCycleDelegatorHelper.onSecondResumeFragments();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter
    // /////////////////////////////////////////////////////////////////////////

    public boolean isStateAlreadySaved() {
        return mIsStateAlreadySaved;
    }

    protected ActivityLifeCycleDelegatorHelper getLifeCycleDelegatorHelper() {
        return mActivityLifeCycleDelegatorHelper;
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    // /////////////////////////////////////////////////////////////////////////
    // inner class
    // /////////////////////////////////////////////////////////////////////////

    private void throwSuperNotCalled(String methodName) {
        String classIdentifier = getComponentName().toShortString();
        throw new SuperNotCalledException(String.format("Activity %1$s did not call through " +
                "super" + ".%2$s", classIdentifier, methodName));
    }

    @TargetApi(16)
    private void initStrictMode() {
        if (isStrictModeEnable()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy
                    .Builder();
            threadPolicyBuilder.detectAll();
            threadPolicyBuilder.detectDiskReads();
            threadPolicyBuilder.detectDiskWrites();
            threadPolicyBuilder.detectNetwork();
            // threadPolicyBuilder.penaltyLog();
            // threadPolicyBuilder.penaltyDialog();
            // threadPolicyBuilder.penaltyDeath();
            //if (Build.VERSION.SDK_INT >= 11) {
            //    threadPolicyBuilder.penaltyFlashScreen();
            //}
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());

            if (Build.VERSION.SDK_INT >= 11) {
                StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
                vmPolicyBuilder.detectAll();
                vmPolicyBuilder.detectLeakedSqlLiteObjects();
                vmPolicyBuilder.detectLeakedClosableObjects();
                vmPolicyBuilder.detectActivityLeaks();
                vmPolicyBuilder.penaltyLog();
                // vmPolicyBuilder.penaltyDeath();

                if (Build.VERSION.SDK_INT >= 16) {
                    vmPolicyBuilder.detectLeakedRegistrationObjects();
                }

                StrictMode.setVmPolicy(vmPolicyBuilder.build());
            }
        }
    }
}