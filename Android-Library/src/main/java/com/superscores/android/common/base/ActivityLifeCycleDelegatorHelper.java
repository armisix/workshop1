package com.superscores.android.common.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * This class delegate the life cycle to components. <p/> Created by Pongpat on 2/14/15.
 */
public class ActivityLifeCycleDelegatorHelper implements ActivityLifeCycle {

    private ArrayList<ActivityLifeCycle> mLifeCycleComponents;

    ActivityLifeCycleDelegatorHelper() {
    }

    /**
     * Add new helper component. This must be call onCreate().
     *
     * @param lifeCycleComponent the components to be added.
     */
    public void addLifeCycleComponent(ActivityLifeCycle lifeCycleComponent) {
        if (mLifeCycleComponents == null) {
            mLifeCycleComponents = new ArrayList<>();
        }
        mLifeCycleComponents.add(lifeCycleComponent);

        //if (lifeCycleComponents != null) {
        //    for (int i = 0; i < lifeCycleComponents.length; i++) {
        //        mLifeCycleComponents.add(lifeCycleComponents[i]);
        //        lifeCycleComponents[i].onCreate(savedInstanceState);
        //    }
        //}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onCreate(savedInstanceState);
            }
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onPostCreate(savedInstanceState);
            }
        }
    }

    @Override
    public void onRestart() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onRestart();
            }
        }
    }

    @Override
    public void onStart() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onStart();
            }
        }
    }

    @Override
    public void onResume() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onResume();
            }
        }
    }

    @Override
    public void onFirstResume() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onFirstResume();
            }
        }
    }

    @Override
    public void onSecondResume() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onSecondResume();
            }
        }
    }

    @Override
    public void onPostResume() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onPostResume();
            }
        }
    }

    @Override
    public void onResumeFragments() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onResumeFragments();
            }
        }
    }

    @Override
    public void onFirstResumeFragments() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onFirstResumeFragments();
            }
        }
    }

    @Override
    public void onSecondResumeFragments() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onSecondResumeFragments();
            }
        }
    }

    @Override
    public void onPause() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onPause();
            }
        }
    }

    @Override
    public void onStop() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onStop();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onDestroy();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onSaveInstanceState(outState);
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onRestoreInstanceState(savedInstanceState);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onConfigurationChanged(newConfig);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mLifeCycleComponents != null) {
            for (ActivityLifeCycle component : mLifeCycleComponents) {
                component.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}