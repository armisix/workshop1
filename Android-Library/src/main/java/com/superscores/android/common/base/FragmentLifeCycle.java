package com.superscores.android.common.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pongpat on 3/13/15.
 */
interface FragmentLifeCycle {

    public void onAttach(Activity activity);

    public void onCreate(Bundle savedInstanceState);

    //we do not return view here
    public void onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState);

    public void onViewCreated(View view, Bundle savedInstanceState);

    public void onActivityCreated(Bundle savedInstanceState);

    public void onStart();

    public void onResume();

    public void onFirstResume();

    public void onSecondResume();

    public void onPause();

    public void onSaveInstanceState(Bundle outState);

    public void onStop();

    public void onDestroy();

    public void onDetach();

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    public void onConfigurationChanged(Configuration newConfig);

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater);

    public void onPrepareOptionsMenu(Menu menu);
}
