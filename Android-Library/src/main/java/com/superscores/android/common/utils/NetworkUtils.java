package com.superscores.android.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

public class NetworkUtils {

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    private static NetworkInfo getNetworkInfo(Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager
                .TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager
                .TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(),
                info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    private static boolean isConnectionFast(int type, int subType) {

        boolean isFastNetwork = false;

        if (type == ConnectivityManager.TYPE_WIFI) {
            isFastNetwork = true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {

            if (subType == TelephonyManager.NETWORK_TYPE_1xRTT) {
                isFastNetwork = false; // ~ 50-100 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_CDMA) {
                isFastNetwork = false;// ~ 14-64 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                isFastNetwork = false;// ~ 50-100 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_EVDO_0) {
                isFastNetwork = true;// ~ 400-1000 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                isFastNetwork = true; // ~ 600-1400 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_GPRS) {
                isFastNetwork = false;// ~ 100 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_HSDPA) {
                isFastNetwork = true;// ~ 2-14 Mbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_HSPA) {
                isFastNetwork = true;// ~ 700-1700 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_HSUPA) {
                isFastNetwork = true;// ~ 1-23 Mbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS) {
                isFastNetwork = true;// ~ 400-7000 kbps
            } else if (Build.VERSION.SDK_INT >= 8 && subType == TelephonyManager
                    .NETWORK_TYPE_IDEN) {
                isFastNetwork = false;// ~25 kbps
            } else if (Build.VERSION.SDK_INT >= 9 && subType == TelephonyManager
                    .NETWORK_TYPE_EVDO_B) {
                isFastNetwork = true; // ~ 5 Mbps
            } else if (Build.VERSION.SDK_INT >= 11 && subType == TelephonyManager
                    .NETWORK_TYPE_LTE) {
                isFastNetwork = true;// ~ 10+ Mbps
            } else if (Build.VERSION.SDK_INT >= 11 && subType == TelephonyManager
                    .NETWORK_TYPE_EHRPD) {
                isFastNetwork = true;// ~ 1-2 Mbps
            } else if (Build.VERSION.SDK_INT >= 13 && subType == TelephonyManager
                    .NETWORK_TYPE_HSPAP) {
                isFastNetwork = true;// ~ 10-20 Mbps
            }
        }
        return isFastNetwork;
    }

    public static String getConnectionType(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isConnected()) {
            return getConnectionType(info.getType(), info.getSubtype());
        } else {
            return "Unknown";
        }
    }

    private static String getConnectionType(int type, int subType) {

        String networkType = "Unknown";

        if (type == ConnectivityManager.TYPE_WIFI) {
            networkType = "WiFi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {

            if (subType == TelephonyManager.NETWORK_TYPE_1xRTT) {
                networkType = "1xRTT"; // ~ 50-100 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_CDMA) {
                networkType = "CDMA";// ~ 14-64 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                networkType = "EDGE";// ~ 50-100 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_EVDO_0) {
                networkType = "EVDO_0";// ~ 400-1000 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                networkType = "EVDO_A"; // ~ 600-1400 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_GPRS) {
                networkType = "GPRS";// ~ 100 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_HSDPA) {
                networkType = "HSDPA";// ~ 2-14 Mbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_HSPA) {
                networkType = "HSPA";// ~ 700-1700 kbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_HSUPA) {
                networkType = "HSUPA";// ~ 1-23 Mbps
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS) {
                networkType = "UMTS";// ~ 400-7000 kbps
            } else if (Build.VERSION.SDK_INT >= 8 && subType == TelephonyManager
                    .NETWORK_TYPE_IDEN) {
                networkType = "IDEN";// ~25 kbps
            } else if (Build.VERSION.SDK_INT >= 9 && subType == TelephonyManager
                    .NETWORK_TYPE_EVDO_B) {
                networkType = "EVDO_B"; // ~ 5 Mbps
            } else if (Build.VERSION.SDK_INT >= 11 && subType == TelephonyManager
                    .NETWORK_TYPE_LTE) {
                networkType = "LTE";// ~ 10+ Mbps
            } else if (Build.VERSION.SDK_INT >= 11 && subType == TelephonyManager
                    .NETWORK_TYPE_EHRPD) {
                networkType = "EHRPD";// ~ 1-2 Mbps
            } else if (Build.VERSION.SDK_INT >= 13 && subType == TelephonyManager
                    .NETWORK_TYPE_HSPAP) {
                networkType = "HSPAP";// ~ 10-20 Mbps
            }
        }
        return networkType;
    }
}