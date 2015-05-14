package com.superscores.android.common.utils;

import android.widget.AbsListView;

public class ListViewUtils {

    /**
     * Check if the items in the list can be scrolled in a certain direction.
     * 
     * @param direction
     *            Negative to check scrolling up, positive to check scrolling down.
     * @return true if the list can be scrolled in the specified direction, false otherwise.
     */
    public static boolean canScrollList(AbsListView listView, int direction) {
        final int childCount = listView.getChildCount();
        if (childCount == 0) {
            return false;
        }

        final int firstPosition = listView.getFirstVisiblePosition();
        if (direction > 0) {
            final int lastBottom = listView.getChildAt(childCount - 1)
                                           .getBottom();
            final int lastPosition = firstPosition + childCount;
            return lastPosition < (listView.getChildCount() - 1)
                    || lastBottom > (listView.getHeight() - listView.getPaddingBottom());
        } else {
            final int firstTop = listView.getChildAt(0)
                                         .getTop();
            return firstPosition > 0 || firstTop < listView.getPaddingTop();
        }
    }

    public static boolean isListViewScrollable(AbsListView listView){
        boolean canScrollUp = ListViewUtils.canScrollList(listView, -1);
        boolean canScrollDown = ListViewUtils.canScrollList(listView, 1);
        return canScrollUp || canScrollDown;
    }
}
