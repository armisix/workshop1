/**
 * 
 */
package com.superscores.android.common.utils;

import android.text.SpannableString;

/**
 * @author Pongpat Ratanaamornpin
 * 
 */
public class TextUtils {

    /**
     * Text that going to add Span must be in format {(#d and #d)}, where d is positive integer from
     * 1, 2, 3, ..., etc.
     * 
     * @param input
     *            input string
     * @param spannableObj
     *            spannable object
     * @param flag
     *            spannable flag
     * @return processed string
     */
    public static SpannableString addSpannableObject(String input, Object[] spannableObj) {

        try {
            int[] firstIndex = new int[spannableObj.length];
            int[] lastIndex = new int[spannableObj.length];

            for (int i = 0; i < spannableObj.length; i++) {
                String startToken = "{(#" + (i + 1);
                String endToken = "#" + (i + 1) + ")}";

                firstIndex[i] = input.indexOf(startToken);
                input = input.replace(startToken, "");
                lastIndex[i] = input.indexOf(endToken);
                input = input.replace(endToken, "");
            }

            SpannableString spannableString = new SpannableString(input);
            for (int i = 0; i < spannableObj.length; i++) {
                spannableString.setSpan(spannableObj[i], firstIndex[i], lastIndex[i], 0);
            }
            return spannableString;
        } catch (IndexOutOfBoundsException e) {
            return new SpannableString(input);
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}