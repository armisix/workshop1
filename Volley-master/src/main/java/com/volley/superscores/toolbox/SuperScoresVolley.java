package com.volley.superscores.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.volley.Network;
import com.volley.RequestQueue;
import com.volley.toolbox.BasicNetwork;
import com.volley.toolbox.DiskBasedCache;
import com.volley.toolbox.HttpClientStack;
import com.volley.toolbox.HttpStack;

import java.io.File;

/**
 * Super Scores implementations of Volley
 *
 * Created by Pongpat on 3/11/15.
 */
public class SuperScoresVolley {

    /** Default on-disk cache directory. */
    private static final String DEFAULT_CACHE_DIR = "superscoresCached";

    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, new File(context.getCacheDir(), DEFAULT_CACHE_DIR));
    }

    public static RequestQueue newRequestQueue(Context context, File cacheFile) {

        final Context appContext = context.getApplicationContext();

        String userAgent = "com.superscores.android/0";
        try {
            String packageName = appContext.getPackageName();
            PackageInfo info = appContext.getPackageManager()
                    .getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        HttpStack stack;
        if (Build.VERSION.SDK_INT >= 9) {
            // stack = new HurlStack();
            stack = new OkHttpStack();
        } else {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            // See:
            // http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
        }

        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheFile), network);
        queue.start();

        return queue;
    }
}