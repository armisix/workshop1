package com.superscores.android.common.utils;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataUtils {

    public static final String SEPARATOR = ";;";
    public static final char MAP_ITEM_SEPARATOR = '|';
    public static final char MAP_KEY_SEPARATOR = ':';
    public static final char MAP_ESCAPE_CHAR = '&';

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) return true;
        return false;
    }

    public static boolean isNan(double val) {
        return val != val;
    }

    @SuppressWarnings("rawtypes")
    public static String serializeCollection(Collection collection) {
        String dataString = TextUtils.join(SEPARATOR, collection);
        return dataString;
    }

    public static List<Integer> deserializeIntegerCollection(String dataString) {
        if (dataString == null) return new ArrayList<Integer>();

        String[] entries = dataString.split(SEPARATOR);
        if (entries == null || (entries.length == 1 && entries[0].length() == 0)) return new ArrayList<Integer>();

        Integer[] intEntries = new Integer[entries.length];
        for (int i = 0; i < entries.length; i++) {
            intEntries[i] = Integer.parseInt(entries[i]);
        }
        return Arrays.asList(intEntries);
    }

    public static List<String> deserializeStringCollection(String dataString) {
        if (dataString == null) return new ArrayList<String>();

        String[] entries = dataString.split(SEPARATOR);
        if (entries == null || (entries.length == 1 && entries[0].length() == 0)) return new ArrayList<String>();

        return Arrays.asList(entries);
    }

    @SuppressWarnings("rawtypes")
    public static String serializeMap(Map map) {
        String dataString = "";
        if (map == null) return "";
        Set keys = map.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Object value = map.get(key);
            dataString += "" + MAP_ITEM_SEPARATOR + key + MAP_KEY_SEPARATOR + value;
        }
        if (dataString.length() > 0) dataString = dataString.substring(1);
        return dataString;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, String> deserializeMapWithStringKey(String dataString) {
        if (dataString == null) return new HashMap<String, String>();

        HashMap<String, String> map = (HashMap<String, String>) StringToMap.toMap(dataString.toCharArray(), MAP_ITEM_SEPARATOR,
                MAP_KEY_SEPARATOR, MAP_ESCAPE_CHAR);
        if (map == null) return new HashMap<String, String>();

        return map;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<Integer, String> deserializeMapWithIntKey(String dataString) {
        if (dataString == null) return new HashMap<Integer, String>();

        HashMap<String, String> map = (HashMap<String, String>) StringToMap.toMap(dataString.toCharArray(), MAP_ITEM_SEPARATOR,
                MAP_KEY_SEPARATOR, MAP_ESCAPE_CHAR);
        if (map == null) return new HashMap<Integer, String>();

        HashMap<Integer, String> result = new HashMap<Integer, String>(map.size());
        Set<String> keys = map.keySet();
        for (String key : keys) {
            int intKey = Integer.parseInt(key);
            String value = map.get(key);
            result.put(intKey, value);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Long> deserializeLongMapWithStringKey(String dataString) {
        if (dataString == null) return new HashMap<String, Long>();

        HashMap<String, String> map = (HashMap<String, String>) StringToMap.toMap(dataString.toCharArray(), MAP_ITEM_SEPARATOR,
                MAP_KEY_SEPARATOR, MAP_ESCAPE_CHAR);
        if (map == null) return new HashMap<String, Long>();

        HashMap<String, Long> result = new HashMap<String, Long>(map.size());
        Set<String> keys = map.keySet();
        for (String key : keys) {
            Long value = -1L;
            try {
                value = Long.parseLong(map.get(key));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            result.put(key, value);
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static HashMap<Integer, Long> deserializeLongMapWithIntKey(String dataString) {
        if (dataString == null) return new HashMap<Integer, Long>();

        HashMap<String, String> map = (HashMap<String, String>) StringToMap.toMap(dataString.toCharArray(), MAP_ITEM_SEPARATOR,
                MAP_KEY_SEPARATOR, MAP_ESCAPE_CHAR);
        if (map == null) return new HashMap<Integer, Long>();

        HashMap<Integer, Long> result = new HashMap<Integer, Long>(map.size());
        Set<String> keys = map.keySet();
        for (String key : keys) {
            int intKey = Integer.parseInt(key);
            Long value = -1L;
            try {
                value = Long.parseLong(map.get(key));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            result.put(intKey, value);
        }
        return result;
    }

    public static String serializeSparseArray(SparseArray<String> sparseArray) {
        String dataString = "";
        if (sparseArray == null) return "";

        for (int i = 0; i < sparseArray.size(); i++) {
            dataString += "" + MAP_ITEM_SEPARATOR + sparseArray.keyAt(i) + MAP_KEY_SEPARATOR + sparseArray.get(sparseArray.keyAt(i));
        }

        if (dataString.length() > 0) dataString = dataString.substring(1);
        return dataString;
    }

    @SuppressWarnings("unchecked")
    public static SparseArray<String> deserializeSparseArray(String dataString) {
        if (dataString == null) return new SparseArray<String>();

        HashMap<String, String> map = (HashMap<String, String>) StringToMap.toMap(dataString.toCharArray(), MAP_ITEM_SEPARATOR,
                MAP_KEY_SEPARATOR, MAP_ESCAPE_CHAR);
        if (map == null) return new SparseArray<String>();

        SparseArray<String> result = new SparseArray<String>(map.size());
        Set<String> keys = map.keySet();
        for (String key : keys) {
            int intKey = Integer.parseInt(key);
            String value = map.get(key);
            result.put(intKey, value);
        }
        return result;
    }

    public static String serializeSparseIntArray(SparseIntArray sparseArray) {
        String dataString = "";
        if (sparseArray == null) return "";

        for (int i = 0; i < sparseArray.size(); i++) {
            dataString += "" + MAP_ITEM_SEPARATOR + sparseArray.keyAt(i) + MAP_KEY_SEPARATOR + sparseArray.get(sparseArray.keyAt(i));
        }

        if (dataString.length() > 0) dataString = dataString.substring(1);
        return dataString;
    }

    @SuppressWarnings("unchecked")
    public static SparseIntArray deserializeSparseIntArray(String dataString) {
        if (dataString == null) return new SparseIntArray();

        HashMap<String, String> map = (HashMap<String, String>) StringToMap.toMap(dataString.toCharArray(), MAP_ITEM_SEPARATOR,
                MAP_KEY_SEPARATOR, MAP_ESCAPE_CHAR);
        if (map == null) return new SparseIntArray();

        SparseIntArray result = new SparseIntArray(map.size());
        Set<String> keys = map.keySet();
        for (String key : keys) {
            int intKey = Integer.parseInt(key);
            int intValue = Integer.parseInt(map.get(key));
            result.put(intKey, intValue);
        }
        return result;
    }

}