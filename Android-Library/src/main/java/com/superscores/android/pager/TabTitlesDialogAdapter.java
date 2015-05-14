package com.superscores.android.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.superscores.android.R;

import java.util.ArrayList;

public class TabTitlesDialogAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mData;
    private int mCurrentIndex;

    public TabTitlesDialogAdapter(Context context, int currentIndex) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCurrentIndex = currentIndex;
    }

    public void setData(ArrayList<String> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TabTitleHolder viewHolder;
        if (convertView != null && convertView.getTag() instanceof TabTitleHolder) {
            viewHolder = (TabTitleHolder) convertView.getTag();
        } else {
            viewHolder = new TabTitleHolder();
            convertView = mLayoutInflater.inflate(R.layout.pager_tab_title_dialog, parent,
                    false);
            viewHolder.vTabTitlesIndicator = convertView.findViewById(R.id.vTabTitlesIndicator);
            viewHolder.tvTabTitlesDialog = (TextView) convertView.findViewById(R.id.tvTabTitlesDialog);
            convertView.setTag(viewHolder);
        }
        String title = mData.get(position);

        if (position == mCurrentIndex) {
            viewHolder.vTabTitlesIndicator.setVisibility(View.VISIBLE);
        } else {
            viewHolder.vTabTitlesIndicator.setVisibility(View.GONE);
        }
        viewHolder.tvTabTitlesDialog.setText(title);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    private static class TabTitleHolder {
        public View vTabTitlesIndicator;
        public TextView tvTabTitlesDialog;
    }
}