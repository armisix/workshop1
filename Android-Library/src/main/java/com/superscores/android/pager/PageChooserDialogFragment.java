package com.superscores.android.pager;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.superscores.android.R;
import com.superscores.android.common.base.AbstractExtraConfigDialogFragment;

import java.util.ArrayList;

public class PageChooserDialogFragment extends AbstractExtraConfigDialogFragment {

    private static final String EXTRA_TAB_TITLES = "EXTRA_TAB_TITLES";
    private static final String EXTRA_TAB_INDEX = "EXTRA_TAB_INDEX";

    private OnPageSelectedListener mOnTabSelectedListener;
    private ArrayList<String> mTitles;
    private int mCurrentIndex;

    public interface OnPageSelectedListener {
        public void onTabDialogSelected(int index);
    }

    public static PageChooserDialogFragment newInstance(ArrayList<String> titles,
            int currentIndex) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_TAB_TITLES, titles);
        bundle.putInt(EXTRA_TAB_INDEX, currentIndex);
        PageChooserDialogFragment fragment = new PageChooserDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitles = bundle.getStringArrayList(EXTRA_TAB_TITLES);
            mCurrentIndex = bundle.getInt(EXTRA_TAB_INDEX, 0);
        }
        if (mTitles == null) {
            /* Titles do not available. Dismiss */
            dismiss();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TabTitlesDialogAdapter adapter = new TabTitlesDialogAdapter(getActivity(), mCurrentIndex);
        adapter.setData(mTitles);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(R.string.go_to);
        builder.adapter(adapter, mListCallback);
        builder.negativeText(R.string.dismiss);

        MaterialDialog dialog = builder.build();
        ListView listView = dialog.getListView();

        /* set first visible index, so user can easily see the current tab */
        int firstVisibleIndex = mCurrentIndex - 2;
        if (firstVisibleIndex < 0) {
            firstVisibleIndex = 0;
        }
        if (listView != null) {
            listView.setSelection(firstVisibleIndex);
        }

        return dialog;
    }

    public void registerOnTabSelectedListener(OnPageSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    private MaterialDialog.ListCallback mListCallback = new MaterialDialog.ListCallback() {
        @Override
        public void onSelection(MaterialDialog materialDialog, View view, int i,
                CharSequence charSequence) {
            if (mOnTabSelectedListener != null) {
                mOnTabSelectedListener.onTabDialogSelected(i);
            }
            dismiss();
        }
    };
}
