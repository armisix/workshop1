package com.superscores.android.common.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Created by Pongpat on 2/14/15.
 */
@SuppressWarnings("unused")
interface ActivityLifeCycle {

    public void onCreate(Bundle savedInstanceState);

    public void onPostCreate(Bundle savedInstanceState);

    public void onRestart();

    public void onStart();

    public void onResume();

    public void onFirstResume();

    public void onSecondResume();

    public void onPostResume();

    public void onResumeFragments();

    public void onFirstResumeFragments();

    public void onSecondResumeFragments();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public void onSaveInstanceState(Bundle outState);

    public void onRestoreInstanceState(Bundle savedInstanceState);

    public void onConfigurationChanged(Configuration newConfig);

    public void onActivityResult(int requestCode, int resultCode, Intent data);
}