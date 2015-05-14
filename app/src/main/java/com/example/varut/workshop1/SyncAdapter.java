package com.example.varut.workshop1;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.example.varut.workshop1.model.JsonImages;
import com.example.varut.workshop1.model.JsonUploadResponse;
import com.example.varut.workshop1.parser.ImageParser;
import com.example.varut.workshop1.provider.MyProvider;
import com.google.gson.Gson;
import com.volley.Request;
import com.volley.superscores.NetworkResponseWrapper;
import com.volley.superscores.RequestBuilder;
import com.volley.superscores.Response;
import com.volley.superscores.parser.DataParser;
import com.volley.superscores.toolbox.DefaultRequestRetryPolicy;
import com.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.boye.httpclientandroidlib.protocol.HTTP;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String FEED_URL = "http://dev.ctrlyati.in.th/aupload/detail/";
    // Global variables
    // Define a variable to contain a content resolver instance
    Context mContext;
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    @SuppressLint("NewApi")
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContext = context;
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.i("Workshop1", "Beginning network synchronization");
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, FEED_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Toast.makeText(getContext(), "onResponse", Toast.LENGTH_SHORT).show();
//                        updateLocalFeedData(response.toString());
//                    }
//                }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        );
//        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

        HashMap<String, String> headers = new HashMap<>();
        headers.put(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
        headers.put("API-Version", "1.0");

        RequestBuilder requestBuilder = new RequestBuilder(Request.Method.GET, headers, FEED_URL);
        requestBuilder.setRetryPolicy(new DefaultRequestRetryPolicy());

        Request request = requestBuilder.build(getContext(),
                new ImageParser(), // Parser
                new Response.Listener<JsonUploadResponse>() {
                    @Override
                    public void onResponse(NetworkResponseWrapper<JsonUploadResponse> response) {
                        Toast.makeText(getContext(), "onResponse", Toast.LENGTH_SHORT).show();
                        updateLocalFeedData(response.getData().getData());
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(NetworkResponseWrapper errorResponse) {
                        Toast.makeText(getContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(getContext()).add(request);
//        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public void updateLocalFeedData(List<JsonImages> myData){

        for (int i = 0; i < myData.size(); i++) {
            JsonImages myObj = myData.get(i);

            if(!isDuplicate(myObj)){
                // Add a new student record
                ContentValues values = new ContentValues();
                values.put(MyProvider.NAME, myObj.getName());
                values.put(MyProvider.SIZE, myObj.getSize());
                values.put(MyProvider.DATE, myObj.getDate());
                values.put(MyProvider.TYPE, myObj.getType());

                Uri uri = mContentResolver.insert(MyProvider.CONTENT_URI, values);
//                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean isDuplicate(JsonImages myObj){
        boolean isDup = false;

        String name = myObj.getName();
        String size = myObj.getSize();
        String date = myObj.getDate();
        String type = myObj.getType();
        Cursor c = mContentResolver.query(MyProvider.CONTENT_URI, null, null, null, "name");
        if (c.moveToFirst()) {
            do{
                if(name.equals(c.getString(c.getColumnIndex(MyProvider.NAME))) &&
                        size.equals(c.getString(c.getColumnIndex(MyProvider.SIZE))) &&
                        date.equals(c.getString(c.getColumnIndex(MyProvider.DATE))) &&
                        type.equals(c.getString(c.getColumnIndex(MyProvider.TYPE))) ){
                    isDup = true;
                }
            } while (c.moveToNext());
        }
        c.close();

        return isDup;
    }

}
