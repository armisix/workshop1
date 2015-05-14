package com.superscores.android.common.utils;

import android.content.Context;

import com.superscores.android.R;
import com.superscores.android.io.model.json.TimestampJsonModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class DateTimeUtils {

    public static final long MINUTE_MILLISECS = 60000;
    public static final long HOUR_MILLISECS = 3600000;
    public static final long DAY_MILLISECS = 86400000;

    public static final int DATE_FORMAT_SHORT = 0;
    public static final int DATE_FORMAT_MEDIUM = 1;
    public static final int DATE_FORMAT_LONG = 2;

    public static final String DATE_FORMAT_ISO_8601 = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT_ISO_8601 = "yyyy-MM-dd HH:mm:ss";

    public static final String DASH_SEPARATOR = "\u0020\u2014\u0020";

    private static final String DEFAULT_EMPTY_TIMESTAMP = "-";

    // /////////////////////////////////////////////////////////////////////////
    // convert timestamp
    // /////////////////////////////////////////////////////////////////////////

    public static String convertTimestampToString(TimestampJsonModel timestampObj,
            java.text.DateFormat dateFormat) {
        if (timestampObj == null) {
            return DEFAULT_EMPTY_TIMESTAMP;
        } else {
            return convertTimestampToString(timestampObj.getMilliSec(), dateFormat,
                    DEFAULT_EMPTY_TIMESTAMP);
        }
    }

    public static String convertTimestampToString(TimestampJsonModel timestampObj,
            java.text.DateFormat dateFormat, String defaultValue) {
        if (timestampObj == null) {
            return defaultValue;
        } else {
            return convertTimestampToString(timestampObj.getMilliSec(), dateFormat, defaultValue);
        }
    }

    public static String convertTimestampToString(long timestamp, java.text.DateFormat dateFormat) {
        return convertTimestampToString(timestamp, dateFormat, DEFAULT_EMPTY_TIMESTAMP);
    }

    public static String convertTimestampToString(long timestamp, java.text.DateFormat dateFormat,
            String defaultValue) {
        if (timestamp != TimestampJsonModel.EMPTY) {
            return dateFormat.format(new Date(timestamp));
        } else {
            return defaultValue;
        }
    }

    public static String convertTimestampToString(TimestampJsonModel timestampObj,
            SimpleDateFormat dateFormat) {
        if (timestampObj == null) {
            return DEFAULT_EMPTY_TIMESTAMP;
        } else {
            return convertTimestampToString(timestampObj.getMilliSec(), dateFormat,
                    DEFAULT_EMPTY_TIMESTAMP);
        }
    }

    public static String convertTimestampToString(TimestampJsonModel timestampObj,
            SimpleDateFormat dateFormat, String defaultValue) {
        if (timestampObj == null) {
            return defaultValue;
        } else {
            return convertTimestampToString(timestampObj.getMilliSec(), dateFormat, defaultValue);
        }
    }

    public static String convertTimestampToString(long timestamp, SimpleDateFormat dateFormat) {
        return convertTimestampToString(timestamp, dateFormat, DEFAULT_EMPTY_TIMESTAMP);
    }

    public static String convertTimestampToString(long timestamp, SimpleDateFormat dateFormat,
            String defaultValue) {
        if (timestamp != TimestampJsonModel.EMPTY) {
            return dateFormat.format(new Date(timestamp));
        } else {
            return defaultValue;
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // format string to string
    // /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    public static String convertStringToString(String dateTime, SimpleDateFormat sourceDateTime,
            java.text.DateFormat destinationDateFormat) {

        if (sourceDateTime == null) {
            return dateTime;
        }
        if (dateTime == null) {
            return "";
        }

        Date date = convertStringToDate(dateTime, sourceDateTime);
        return destinationDateFormat.format(date);
    }

    @SuppressWarnings("unused")
    public static String convertDateTime(String dateTime, SimpleDateFormat sourceDateTime,
            SimpleDateFormat destinationDateTime) {

        if (sourceDateTime == null || destinationDateTime == null) {
            return dateTime;
        }
        if (dateTime == null) {
            return "";
        }

        Date date = convertStringToDate(dateTime, sourceDateTime);

        return convertDateToString(date, destinationDateTime);
    }

    // /////////////////////////////////////////////////////////////////////////
    // format string to date
    // /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    public static Date convertStringToDate(String dateTime, java.text.DateFormat dateFormat) {
        return convertStringToDate(dateTime, (SimpleDateFormat) dateFormat);
    }

    public static Date convertStringToDate(String dateTime, SimpleDateFormat dateFormat) {
        if (dateTime == null || dateFormat == null) {
            return null;
        }

        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    // /////////////////////////////////////////////////////////////////////////
    // format date to string
    // /////////////////////////////////////////////////////////////////////////

    public static String convertDateToString(Date date, SimpleDateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return "";
        }
        return dateFormat.format(date);
    }

    public static String convertDateToString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return "";
        }
        return dateFormat.format(date);
    }

    // /////////////////////////////////////////////////////////////////////////
    // get format
    // /////////////////////////////////////////////////////////////////////////

    // /////////////////////////////////////////////////////////////////////////
    // get ISO date format
    // /////////////////////////////////////////////////////////////////////////

    public static SimpleDateFormat getISO8601DateFormat() {
        return getISO8601DateFormat(true);
    }

    public static SimpleDateFormat getISO8601DateFormat(boolean isLocalize) {
        // ISO 8601 number must be "Arabic numerals", the globally number system. So, we set Locale
        // to US
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_ISO_8601, Locale.US);
        if (isLocalize) {
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
        } else {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return simpleDateFormat;
    }

    public static SimpleDateFormat getISO8601DateTimeFormat() {
        return getISO8601DateTimeFormat(true);
    }

    public static SimpleDateFormat getISO8601DateTimeFormat(boolean isLocalize) {
        // ISO 8601 number must be "Arabic numerals", the globally number system. So, we set Locale
        // to US
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT_ISO_8601,
                Locale.US);
        if (isLocalize) {
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
        } else {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return simpleDateFormat;
    }

    // /////////////////////////////////////////////////////////////////////////
    // get date format
    // /////////////////////////////////////////////////////////////////////////

    public static DateFormat getDateFormat(Context context) {
        return getDateFormat(context, DATE_FORMAT_SHORT, true);
    }

    public static DateFormat getDateFormat(Context context, int style, boolean isLocalize) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        if (style == DATE_FORMAT_MEDIUM) {
            dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
        } else if (style == DATE_FORMAT_LONG) {
            dateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        }

        if (isLocalize) {
            dateFormat.setTimeZone(TimeZone.getDefault());
        } else {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return dateFormat;
    }

    @SuppressWarnings("unused")
    public static SimpleDateFormat getDateFormatWithDay(Context context, int style,
            boolean isLocalize) {
        return getDateFormatWithDay(context, style, isLocalize, " ");
    }

    public static SimpleDateFormat getDateFormatWithDay(Context context, int style,
            boolean isLocalize, String separator) {
        SimpleDateFormat dateFormat = (SimpleDateFormat) getDateFormat(context, style, isLocalize);
        dateFormat.applyPattern("EEE" + separator + dateFormat.toLocalizedPattern());
        return dateFormat;
    }

    public static SimpleDateFormat getDateTimeFormatWithDay(Context context, int style,
            boolean isLocalize, String separatorDay, String separatorTime) {
        SimpleDateFormat dateFormat = getDateTimeFormat(context, style, separatorTime, isLocalize);
        dateFormat.applyPattern("EEE" + separatorDay + dateFormat.toLocalizedPattern());
        return dateFormat;
    }

    // /////////////////////////////////////////////////////////////////////////
    // get date time format
    // /////////////////////////////////////////////////////////////////////////

    public static SimpleDateFormat getDateTimeFormat(Context context) {
        return getDateTimeFormat(context, DATE_FORMAT_SHORT, true);
    }

    public static SimpleDateFormat getDateTimeFormat(Context context, boolean isLocalize) {
        return getDateTimeFormat(context, DATE_FORMAT_SHORT);
    }

    public static SimpleDateFormat getDateTimeFormat(Context context, int style) {
        return getDateTimeFormat(context, style, ", ", true);
    }

    public static SimpleDateFormat getDateTimeFormat(Context context, int style,
            boolean isLocalize) {
        return getDateTimeFormat(context, style, ", ", isLocalize);
    }

    public static SimpleDateFormat getDateTimeFormat(Context context, int style, String separator) {
        return getDateTimeFormat(context, style, separator, true);
    }

    public static SimpleDateFormat getDateTimeFormat(Context context, int style, String separator,
            boolean isLocalize) {
        SimpleDateFormat tempDateFormat = (SimpleDateFormat) android.text.format.DateFormat
                .getDateFormat(
                context);
        if (style == DATE_FORMAT_MEDIUM) {
            tempDateFormat = (SimpleDateFormat) android.text.format.DateFormat.getMediumDateFormat(
                    context);
        } else if (style == DATE_FORMAT_LONG) {
            tempDateFormat = (SimpleDateFormat) android.text.format.DateFormat.getLongDateFormat(
                    context);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(tempDateFormat.toLocalizedPattern() +
                separator + "HH:mm", Locale.getDefault());
        if (isLocalize) {
            dateFormat.setTimeZone(TimeZone.getDefault());
        } else {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return dateFormat;
    }

    // /////////////////////////////////////////////////////////////////////////
    // get time format
    // /////////////////////////////////////////////////////////////////////////

    public static SimpleDateFormat getTimeFormat(Context context) {
        return getTimeFormat(context, true);
    }

    public static SimpleDateFormat getTimeFormat(Context context, boolean isLocalize) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        if (isLocalize) {
            dateFormat.setTimeZone(TimeZone.getDefault());
        } else {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return dateFormat;
    }

    // /////////////////////////////////////////////////////////////////////////
    // get custom format
    // /////////////////////////////////////////////////////////////////////////

    public static SimpleDateFormat getCustomFormat(String format) {
        return getCustomFormat(format, true);
    }

    public static SimpleDateFormat getCustomFormat(String format, boolean isLocalize) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        if (isLocalize) {
            dateFormat.setTimeZone(TimeZone.getDefault());
        } else {
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return dateFormat;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Misc.
    // /////////////////////////////////////////////////////////////////////////

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    public static long addDayToTimestamp(long timestamp, int days) {
        return timestamp + (days * 24 * 60 * 60 * 1000);
    }

    public static boolean isExpired(long lastTimestamp, long interval) {
        long currentTimestamp = getCurrentTimestamp();
        return (lastTimestamp >= currentTimestamp) || (currentTimestamp - lastTimestamp > interval);
    }

    public static String getTimeRange(Context context, long diffMilliSec) {
        return getTimeRange(context, diffMilliSec, 59, 23, true);
    }

    public static String getTimeRange(Context context, long diffMilliSec, boolean isShouldRoundUp) {
        return getTimeRange(context, diffMilliSec, 59, 23, isShouldRoundUp);
    }

    public static String getTimeRange(Context context, long diffMilliSec, int maxMinute,
            int maxHour, boolean isShouldRoundUp) {
        if (context == null) {
            return "";
        }

        String timePast = context.getString(R.string.just_now);
        int roundUpValue = isShouldRoundUp ? 1 : 0;

        // 60 * 1000
        int diffMin = (int) ((diffMilliSec / 60000) + roundUpValue);
        if (diffMin > 1 && diffMin <= maxMinute) {
            timePast = context.getResources()
                    .getQuantityString(R.plurals.minutes, diffMin, diffMin);
        } else {
            // 60 * 60 * 1000
            int diffHour = (int) ((diffMilliSec / 3600000) + roundUpValue);
            if (diffHour <= maxHour) {
                timePast = context.getResources()
                        .getQuantityString(R.plurals.hours, diffHour, diffHour);
            } else {
                // 60 * 60 * 24 * 1000
                int diffDay = (int) ((diffMilliSec / 86400000) + roundUpValue);
                timePast = context.getResources()
                        .getQuantityString(R.plurals.days, diffDay, diffDay);
            }
        }
        return timePast;
    }

    public static String getMatchTimeDiffString(long diffMilliSec) {
        String diffText;

        // 60 * 1000
        int diffMin = (int) (diffMilliSec / 60000);
        if (diffMin <= 90) {
            diffText = diffMin + " min";
        } else {
            // 60 * 60 * 1000
            int diffHour = (int) (diffMilliSec / 3600000);
            if (diffHour <= 72) {
                diffText = diffHour + " hr";
            } else {
                // 60 * 60 * 24 * 1000
                int diffDay = (int) (diffMilliSec / 86400000);
                diffText = diffDay + " day";
            }
        }
        return diffText;
    }

    public static String getUpdateTimeDiffString(Context context, long lastUpdate) {
        long t1 = lastUpdate;
        long t2 = getCurrentTimestamp();
        long diff = t2 - t1;

        if (diff <= 0) {
            return context.getString(R.string.just_now);
        } else {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTimeInMillis(t1);
            int y1 = calendar.get(Calendar.YEAR);
            int m1 = calendar.get(Calendar.MONTH);
            int d1 = calendar.get(Calendar.DAY_OF_YEAR);

            calendar.setTimeInMillis(t2);
            int y2 = calendar.get(Calendar.YEAR);
            int m2 = calendar.get(Calendar.MONTH);
            int d2 = calendar.get(Calendar.DAY_OF_YEAR);

            int diffYear = (d2 >= d1) ? (y2 - y1) : (y2 - y1 - 1);
            if (diffYear > 0) {
                return context.getString(R.string.sentence_update_over_year);
            }
            int diffMonth = diffYear * 12 + (m2 - m1);
            if (diffMonth > 0) {
                return context.getResources()
                        .getQuantityString(R.plurals.update_months_ago, diffMonth, diffMonth);
            }
            int diffDay = (int) TimeUnit.MILLISECONDS.toDays(diff);
            if (diffDay > 0) {
                return context.getResources()
                        .getQuantityString(R.plurals.update_days_ago, diffDay, diffDay);
            }
            int diffHour = (int) TimeUnit.MILLISECONDS.toHours(diff);
            if (diffHour > 0) {
                return context.getResources()
                        .getQuantityString(R.plurals.update_hours_ago, diffHour, diffHour);
            }
            int diffMin = (int) TimeUnit.MILLISECONDS.toMinutes(diff);
            if (diffMin > 0) {
                return context.getResources()
                        .getQuantityString(R.plurals.update_minutes_ago, diffMin, diffMin);
            }
            return context.getString(R.string.just_now);
        }
    }

    public static int getMonthsDifference(long t1, long t2) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(t1);
        int y1 = calendar.get(Calendar.YEAR);
        int m1 = calendar.get(Calendar.MONTH);

        calendar.setTimeInMillis(t2);
        int y2 = calendar.get(Calendar.YEAR);
        int m2 = calendar.get(Calendar.MONTH);

        return ((y2 - y1) * 12) + (m2 - m1);
    }
}