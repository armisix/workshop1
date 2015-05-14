package com.superscores.android.common.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Created by Pongpat on 2/14/15.
 */
public abstract class ActivityLifeCycleDelegate implements ActivityLifeCycle {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
    }

    @Override
    public void onRestart() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onFirstResume() {
    }

    @Override
    public void onSecondResume() {
    }

    @Override
    public void onPostResume() {
    }

    @Override
    public void onResumeFragments() {
    }

    @Override
    public void onFirstResumeFragments() {
    }

    @Override
    public void onSecondResumeFragments() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}