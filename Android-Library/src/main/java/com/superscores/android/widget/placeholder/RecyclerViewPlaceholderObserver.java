package com.superscores.android.widget.placeholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;

public class RecyclerViewPlaceholderObserver extends AdapterDataObserver implements
        PlaceholderDataObserver {

    private OnDataSetChangedListener mOnDataSetChangedListener;
    private RecyclerView.Adapter<?> mAdapter;

    public RecyclerViewPlaceholderObserver(
            @NonNull OnDataSetChangedListener onDataSetChangedListener,
            @NonNull RecyclerView.Adapter<?> adapter) {

        mOnDataSetChangedListener = onDataSetChangedListener;
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(this);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        mOnDataSetChangedListener.onDataSetChanged();
    }

    @Override
    public boolean isDataAvailable() {
        return mAdapter.getItemCount() != 0;
    }
}