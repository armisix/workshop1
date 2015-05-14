package com.superscores.android.common.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class AssetsUtils {
	private static final String TAG = "AssetUtils";
	private static final int BUFFER_SIZE = 65536;

	/**
	 * Reads all text from asset with specified path.
	 *
	 * @param assetPath
	 *            A path to asset which should be read.
	 * @return A string with asset content or empty string if asset is not
	 *         exists.
	 */
	public static String readAllText(Context context, String assetPath) {
		InputStream stream = null;
		try {
			stream = context.getAssets().open(assetPath);
			return readAllText(stream);
		} catch (IOException e) {
			Log.e(TAG, "Error on asset reading.", e);
		} finally {
			closeStream(stream);
		}
		return "";
	}

	private static String readAllText(InputStream stream) throws IOException {
		Reader reader = new InputStreamReader(stream);

		final char[] buffer = new char[BUFFER_SIZE];
		StringBuilder result = new StringBuilder();
		int read;
		while (true) {
			read = reader.read(buffer, 0, BUFFER_SIZE);
			if (read > 0) {
				result.append(buffer, 0, read);
				continue;
			}
			return result.toString();
		}
	}

	private static void closeStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				Log.e(TAG, "Error on asset reading.", e);
			}
		}
	}
}
