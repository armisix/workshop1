package com.superscores.android.widget.placeholder;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;

public abstract class AbstractListPlaceholderObserver extends DataSetObserver implements
        PlaceholderDataObserver {

    private OnDataSetChangedListener mOnDataSetChangedListener;

    public AbstractListPlaceholderObserver(
            @NonNull OnDataSetChangedListener onDataSetChangedListener) {
        mOnDataSetChangedListener = onDataSetChangedListener;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        // data set changed
        mOnDataSetChangedListener.onDataSetChanged();
    }
}