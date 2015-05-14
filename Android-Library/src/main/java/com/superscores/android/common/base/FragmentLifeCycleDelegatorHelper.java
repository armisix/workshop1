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

import java.util.ArrayList;

/**
 * This class delegate the life cycle to components. <p/> Created by Pongpat on 2/14/15.
 */
public class FragmentLifeCycleDelegatorHelper implements FragmentLifeCycle {

    private ArrayList<FragmentLifeCycle> mLifeCycleComponents;

    FragmentLifeCycleDelegatorHelper() {
    }

    /**
     * Add new helper component. This must be call onCreate().
     *
     * @param lifeCycleComponent the components to be added.
     */
    public void addLifeCycleComponent(FragmentLifeCycle lifeCycleComponent) {
        if (mLifeCycleComponents == null) {
            mLifeCycleComponents = new ArrayList<>();
        }
        mLifeCycleComponents.add(lifeCycleComponent);
    }

    @Override
    public void onAttach(Activity activity) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onAttach(activity);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onCreate(savedInstanceState);
            }
        }
    }

    @Override
    public void onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onCreateView(inflater, container, savedInstanceState);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onViewCreated(view, savedInstanceState);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onActivityCreated(savedInstanceState);
            }
        }
    }

    @Override
    public void onStart() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onStart();
            }
        }
    }

    @Override
    public void onResume() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onResume();
            }
        }
    }

    @Override
    public void onFirstResume() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onFirstResume();
            }
        }
    }

    @Override
    public void onSecondResume() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onSecondResume();
            }
        }
    }

    @Override
    public void onPause() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onPause();
            }
        }
    }

    @Override
    public void onStop() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onStop();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onDestroy();
            }
        }
    }

    @Override
    public void onDetach() {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onDetach();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onSaveInstanceState(outState);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onConfigurationChanged(newConfig);
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onPrepareOptionsMenu(menu);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onCreateOptionsMenu(menu, inflater);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mLifeCycleComponents != null) {
            for (FragmentLifeCycle component : mLifeCycleComponents) {
                component.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}