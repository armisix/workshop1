package com.example.varut.workshop1.volley;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Varut on 04/29/2015.
 */
public enum TestSingleton {
    INSTANCE;

    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue(Context context){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }
}
