package com.superscores.android.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SystemUtils {

    private static final String SELECT_RUNTIME_PROPERTY = "persist.sys.dalvik.vm.lib";
    private static final String LIB_DALVIK = "libdvm.so";
    private static final String LIB_ART = "libart.so";
    private static final String LIB_ART_D = "libartd.so";

    public static String getCurrentRuntime() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get", String.class, String.class);
                if (get == null) {
                    return "Unknown 0x01";
                }
                try {
                    /* Assuming default is */
                    final String value = (String) get.invoke(systemProperties,
                            SELECT_RUNTIME_PROPERTY, "Dalvik (default)");
                    if (LIB_DALVIK.equals(value)) {
                        return "Dalvik";
                    } else if (LIB_ART.equals(value)) {
                        return "ART";
                    } else if (LIB_ART_D.equals(value)) {
                        return "ART debug build";
                    }

                    return value;
                } catch (IllegalAccessException e) {
                    return "Unknown 0x02";
                    // return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "Unknown 0x03";
                    // return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "Unknown 0x04";
                    // return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "Unknown 0x05";
                // return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "Unknown 0x06";
            // return "SystemProperties class is not found";
        }
    }

    public static CharSequence getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager());
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getVersionName(Context context) {
        String versionName;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "";
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = 0;
        }
        return versionCode;
    }

    public static int getApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDeviceID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceProductName() {
        return Build.PRODUCT;
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static int getOrientation(Context context) {
        return context.getResources()
                .getConfiguration().orientation;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources()
                .getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
