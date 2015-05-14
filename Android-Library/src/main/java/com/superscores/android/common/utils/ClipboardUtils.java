package com.superscores.android.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public class ClipboardUtils {

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void CopyText(@NonNull Context context, @StringRes int labelId,
            @NonNull CharSequence textToCopy) {
        if (Build.VERSION.SDK_INT < 11) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(textToCopy);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(context
                    .getString(labelId), textToCopy);
            clipboard.setPrimaryClip(clip);
        }
    }
}