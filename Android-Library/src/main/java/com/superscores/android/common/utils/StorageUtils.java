package com.superscores.android.common.utils;

import java.io.File;

/**
 * Created by Pongpat on 1/29/15.
 */
public class StorageUtils {

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] allFiles = fileOrDirectory.listFiles();
            if (allFiles == null) {
                return;
            }

            for (File child : allFiles) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public static boolean isSDCardWritable() {
        return android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static boolean isSDCardReadable() {
        return isSDCardWritable() || isSDCardReadOnly();
    }

    public static boolean isSDCardReadOnly() {
        return android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}