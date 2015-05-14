package com.superscores.android.widget.placeholder;

import android.support.annotation.NonNull;
import android.widget.BaseExpandableListAdapter;

public class ExpandableListPlaceholderObserver extends AbstractListPlaceholderObserver {

    private BaseExpandableListAdapter mAdapter;

    public ExpandableListPlaceholderObserver(
            @NonNull OnDataSetChangedListener onDataSetChangedListener,
            @NonNull BaseExpandableListAdapter adapter) {
        super(onDataSetChangedListener);

        mAdapter = adapter;
        mAdapter.registerDataSetObserver(this);
    }

    @Override
    public boolean isDataAvailable() {
        return mAdapter.getGroupCount() != 0;
    }
}