package com.example.varut.workshop1.provider;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.example.varut.workshop1.MainActivity;

/**
 * Created by Varut on 04/29/2015.
 */
public class MyContentObserver extends ContentObserver {

    Context mContext;

    public MyContentObserver(Handler handler) {
        super(handler);
    }
    public MyContentObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);

        Toast.makeText(mContext, "onChange : 1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {

        Toast.makeText(mContext, "selfChange : " + selfChange + "\n"+
                                " onChange : " + uri.toString(),
                Toast.LENGTH_SHORT).show();
    }
}
