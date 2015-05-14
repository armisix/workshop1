package com.superscores.android.common.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

	public static String[] getFieldNames(Class<?> cls) {
		Field[] fields = cls.getFields();
		String[] names = new String[fields.length];
		for(int i = 0; i < fields.length; i++) {
			names[i] = fields[i].getName();
		}
		return names;
	}
}