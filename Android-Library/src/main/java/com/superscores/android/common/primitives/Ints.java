package com.superscores.android.common.primitives;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities class for int
 *
 * Created by Pongpat on 4/16/15.
 */
public class Ints {

    public static List<Integer> asList(int... backedArray) {
        ArrayList<Integer> list = new ArrayList<>(backedArray.length);
        for (int value : backedArray) {
            list.add(value);
        }
        return list;
    }
}