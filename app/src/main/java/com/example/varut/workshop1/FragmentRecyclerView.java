package com.example.varut.workshop1;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.varut.workshop1.accounts.GenericAccountService;
import com.example.varut.workshop1.provider.MyContentObserver;
import com.example.varut.workshop1.provider.MyProvider;
import com.example.varut.workshop1.util.ImageModel;
import com.example.varut.workshop1.util.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varut on 04/28/2015.
 */
public class FragmentRecyclerView extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // Constants
    // The authority for the sync adapter's content provider
    private static final String PREF_SETUP_COMPLETE = "setup_complete";

    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 5L; // 5 minutes to sync
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;

    public static final int GRID_SPAN_COUNT = 2;

    public static final String MNG_GRID = "grid";
    public static final String MNG_LIST_VERTICAL = "listvertical";
    public static final String MNG_LIST_HORIZONTAL = "listhorizontal";

    private String layoutManagerType;
    List<ImageModel> images;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MyContentObserver myContentObserver;
    private Object mSyncObserverHandle;
    private Menu mOptionsMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // Create the dummy account
        CreateSyncAccount(getActivity());

        // Setup LayoutManager
        mLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        // Setup RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TriggerRefresh();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // ContentObserver ... Watch for data changes
        myContentObserver = new MyContentObserver(getActivity(), new Handler() );
        getActivity().getContentResolver().
                registerContentObserver(
                        MyProvider.CONTENT_URI, // URI
                        true,                   //
                        myContentObserver);     // Observer

        mSyncStatusObserver.onStatusChanged(0);
        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().getContentResolver().
                unregisterContentObserver(myContentObserver);

        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        this.mOptionsMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                TriggerRefresh();
                break;
            case R.id.menu_correction_grid:
                mOptionsMenu.findItem(R.id.menu_correction_view).setIcon(R.drawable.ic_action_view_as_grid);
                setLayoutManagerType(FragmentRecyclerView.MNG_GRID);
                mLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
                setRecyclerView();
                break;
            case R.id.menu_correction_list_v:
                mOptionsMenu.findItem(R.id.menu_correction_view).setIcon(R.drawable.ic_action_view_as_list_v);
                setLayoutManagerType(FragmentRecyclerView.MNG_LIST_VERTICAL);
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                setRecyclerView();
                break;
            case R.id.menu_correction_list_h:
                mOptionsMenu.findItem(R.id.menu_correction_view).setIcon(R.drawable.ic_action_view_as_list_h);
                setLayoutManagerType(FragmentRecyclerView.MNG_LIST_HORIZONTAL);
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                setRecyclerView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getLayoutManagerType() {
        return layoutManagerType;
    }

    public void setLayoutManagerType(String layoutManagerType) {
        this.layoutManagerType = layoutManagerType;
    }

    public void setRecyclerView(){
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getActivity(), images, getLayoutManagerType()); // 0 is Grid, 1 List Vertical, 2 List Horiozontal
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setDataToAdapter(Cursor cursor){
        images = queryImage(cursor);
        if(images != null){
            if(images.size() > 0){

                setRecyclerView();
            }
        }
    }

    public List<ImageModel> queryImage(Cursor c) {

        List<ImageModel> images = new ArrayList<ImageModel>();
        if (c.moveToFirst()) {
            do{
                ImageModel imageModel = new ImageModel(
                        c.getString(c.getColumnIndex(MyProvider.NAME)),
                        c.getString(c.getColumnIndex(MyProvider.SIZE)),
                        c.getString(c.getColumnIndex(MyProvider.DATE)),
                        c.getString(c.getColumnIndex(MyProvider.TYPE)));
                images.add(imageModel);

            } while (c.moveToNext());
            return images;
        }
        else return null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),  // Context
                MyProvider.CONTENT_URI, // URI
                null,                   // Projection
                null,                   // Selection
                null,                   // Selection args
                "name");                // Sort
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        setDataToAdapter(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Crfate a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
//            Toast.makeText(getActivity(), "onStatusChanged", Toast.LENGTH_SHORT).show();
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, MyProvider.AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, MyProvider.AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };

    public void setRefreshActionButtonState(boolean refreshing) {

//        Menu myMenu = mOptionsMenu;

        if (mOptionsMenu == null) {
            return;
        }

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.menu_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                MenuItemCompat.setActionView(refreshItem,R.layout.actionbar_indeterminate_progress);
            } else {
                // Disable ... actionBarProgress
                MenuItemCompat.setActionView(refreshItem,null);
                // Disable ... SwipeRefresh
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    }

    public static void CreateSyncAccount(Context context) {

        boolean newAccount = false;
        boolean setupComplete = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);

        // Create account, if it's missing. (Either first run, or user has deleted account.)
        Account account = GenericAccountService.GetAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(account, null, null)) {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, MyProvider.AUTHORITY, 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, MyProvider.AUTHORITY, true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(
                    account,
                    MyProvider.AUTHORITY,
                    new Bundle(),
                    SYNC_INTERVAL);
            newAccount = true;
        }

        // Schedule an initial sync if we detect problems with either our account or our local
        // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
        // the account list, so wee need to check both.)
        if (newAccount || !setupComplete) {
            TriggerRefresh();
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true).commit();
        }
    }

    public static void TriggerRefresh() {
        Bundle b = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                GenericAccountService.GetAccount(),      // Sync account
                MyProvider.AUTHORITY,                    // Content authority
                b);                                      // Extras
    }

}
