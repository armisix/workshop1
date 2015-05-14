package com.superscores.android.widget.placeholder;

import android.support.annotation.NonNull;
import android.widget.BaseAdapter;

public class ListPlaceholderObserver extends AbstractListPlaceholderObserver {

    private BaseAdapter mAdapter;

    public ListPlaceholderObserver(@NonNull OnDataSetChangedListener listener,
            @NonNull BaseAdapter adapter) {
        super(listener);

        mAdapter = adapter;
        mAdapter.registerDataSetObserver(this);
    }

    @Override
    public boolean isDataAvailable() {
        return mAdapter.getCount() != 0;
    }
}