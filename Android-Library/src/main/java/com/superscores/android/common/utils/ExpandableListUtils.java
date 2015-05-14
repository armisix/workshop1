package com.superscores.android.common.utils;

import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by Pongpat on 2/11/15.
 */
public class ExpandableListUtils {

    public static void expandAllList(ExpandableListView expandableListView,
            BaseExpandableListAdapter adapter) {
        expandAllList(expandableListView, adapter, 0);
    }

    public static void expandAllList(ExpandableListView expandableListView,
            BaseExpandableListAdapter adapter, int placeHolderHeight) {

        if (expandableListView == null || expandableListView.getAdapter() == null || adapter ==
                null) {
            return;
        }

        int firstVisiblePosition = expandableListView.getFirstVisiblePosition();
        int expandCount = 0;
        long packedPosition;
        int scrollPosition;

        packedPosition = expandableListView.getExpandableListPosition(firstVisiblePosition);

        for (int i = 0; i < adapter.getGroupCount(); i++) {
            if (!expandableListView.isGroupExpanded(i)) {

                expandableListView.expandGroup(i);
                expandCount++;
            }
        }
        if (ExpandableListView.getPackedPositionType(packedPosition) == ExpandableListView
                .PACKED_POSITION_TYPE_NULL) {
            return;
        }

        if (expandCount > 0) {
            scrollPosition = expandableListView.getFlatListPosition(packedPosition);

            if (placeHolderHeight == 0) {
                expandableListView.setSelectionFromTop(scrollPosition, 0);
            } else {
                if (scrollPosition == 1) {
                    expandableListView.setSelectionFromTop(0, 0);
                } else {
                    expandableListView.setSelectionFromTop(scrollPosition, 0);
                }
            }
        }
    }

    public static void collapseAllList(ExpandableListView expandableListView,
            BaseExpandableListAdapter adapter) {
        collapseAllList(expandableListView, adapter, 0);
    }

    public static void collapseAllList(ExpandableListView expandableListView,
            BaseExpandableListAdapter adapter, int placeHolderHeight) {

        if (expandableListView == null || expandableListView.getAdapter() == null || adapter ==
                null) {
            return;
        }

        int firstVisiblePosition = expandableListView.getFirstVisiblePosition();
        int collapseCount = 0;
        long packedPosition;
        int scrollPosition;

        packedPosition = expandableListView.getExpandableListPosition(firstVisiblePosition);

        for (int i = 0; i < adapter.getGroupCount(); i++) {
            if (expandableListView.isGroupExpanded(i)) {
                expandableListView.collapseGroup(i);
                collapseCount++;
            }
        }

        if (ExpandableListView.getPackedPositionType(packedPosition) == ExpandableListView
                .PACKED_POSITION_TYPE_NULL) {
            return;
        }

        if (collapseCount > 0) {
            scrollPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

            if (placeHolderHeight == 0) {
                expandableListView.setSelectionFromTop(scrollPosition, 0);
            } else {
                if (scrollPosition != 0) {
                    scrollPosition++;
                    expandableListView.setSelectionFromTop(scrollPosition, 0);
                } else {
                    expandableListView.setSelectionFromTop(scrollPosition, 0);
                }
            }
        }
    }
}
