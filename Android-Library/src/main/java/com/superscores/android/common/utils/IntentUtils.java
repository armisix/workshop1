package com.superscores.android.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class IntentUtils {
	public static void startDialer(Context context, String phoneNumber,
			String errorMsg) {
		try {
			Intent dial = new Intent();
			dial.setAction(Intent.ACTION_DIAL);
			dial.setData(Uri.parse("tel:" + phoneNumber));
			context.startActivity(dial);
		} catch (Exception ex) {
			Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
		}
	}

	public static void startSmsIntent(Context context, String phoneNumber,
			String errorMsg) {
		try {
			Uri uri = Uri.parse("sms:" + phoneNumber);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.putExtra("address", phoneNumber);
			intent.setType("vnd.android-dir/mms-sms");
			context.startActivity(intent);
		} catch (Exception ex) {
			Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
		}
	}

	public static void startEmailIntent(Context context, String chooserTitle,
			String emailAddress, String subject, String body, String errorMsg) {
		try {
			Intent intent = new Intent(Intent.ACTION_SEND);
			// intent.setType("plain/text");
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_EMAIL,
					new String[] { emailAddress });
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT, body);
			context.startActivity(Intent.createChooser(intent, chooserTitle));
		} catch (Exception ex) {
			Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
		}
	}

	public static void startMapIntent(Context context, double latitude,
			double longitude, String label, String errorMsg) {
		Intent intent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?q="
						+ label + "@" + latitude + "," + longitude));
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
		}
	}

	public static void startWebIntent(Context context, String url,
			String errorMsg) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
		} catch (Exception ex) {
			Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
		}
	}
}
