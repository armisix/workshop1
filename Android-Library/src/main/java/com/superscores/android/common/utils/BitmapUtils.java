package com.superscores.android.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public class BitmapUtils {

    private static final int BITMAP_PAINT_FLAGS = Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG |
            Paint.FILTER_BITMAP_FLAG;

    public static Bitmap decodeSampledBitmapSizeRes(@NonNull Context context,
            @DrawableRes int drawableId, @DimenRes int sizeRes) {
        int size = context.getResources()
                .getDimensionPixelSize(sizeRes);
        return decodeSampledBitmap(context, drawableId, size);
    }

    public static Bitmap decodeSampledBitmapSizeRes(@NonNull Context context,
            @DrawableRes int drawableId, @DimenRes int widthRes, @DimenRes int heightRes) {
        int width = context.getResources()
                .getDimensionPixelSize(widthRes);
        int height = context.getResources()
                .getDimensionPixelSize(heightRes);
        return decodeSampledBitmap(context, drawableId, width, height);
    }

    public static Bitmap decodeSampledBitmap(@NonNull Context context, @DrawableRes int drawableId,
            int reqSize) {
        return decodeSampledBitmap(context, drawableId, reqSize, reqSize, false);
    }

    public static Bitmap decodeSampledBitmap(@NonNull Context context, @DrawableRes int drawableId,
            int reqWidth, int reqHeight) {
        return decodeSampledBitmap(context, drawableId, reqWidth, reqHeight, false);
    }

    /**
     * @param context         context
     * @param drawableId      resource id to be decoded
     * @param reqWidth        require width
     * @param reqHeight       require height
     * @param targetShortEdge should decoded image has size larger or equal than short edge
     * @return decoded bitmap
     */
    public static Bitmap decodeSampledBitmap(@NonNull Context context, @DrawableRes int drawableId,
            int reqWidth, int reqHeight, boolean targetShortEdge) {

        if (context == null) {
            return null;
        }
        final Resources resources = context.getResources();

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        BitmapFactory.decodeResource(resources, drawableId, options);

        // Calculate inSampleSize
        int inSampleSize = 1;
        if (targetShortEdge) {
            int minSize = reqWidth > reqHeight ? reqHeight : reqWidth;
            inSampleSize = calculateInSampleSize(options, minSize);
        } else {
            inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        }
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inScaled = false;

        Bitmap resultBitmap;
        try {
            resultBitmap = BitmapFactory.decodeResource(resources, drawableId, options);
        } catch (OutOfMemoryError e1) {
            try {
                options.inSampleSize = inSampleSize * 2;
                resultBitmap = BitmapFactory.decodeResource(resources, drawableId, options);
            } catch (OutOfMemoryError e2) {
                resultBitmap = null;
            }
        }

        if (resultBitmap != null && (resultBitmap.getWidth() > reqWidth || resultBitmap.getHeight
                () > reqHeight)) {
            resultBitmap = Bitmap.createScaledBitmap(resultBitmap, reqWidth, reqHeight, false);
        }

        return resultBitmap;
    }

    public static Bitmap decodeSampledBitmap(@NonNull Context context, InputStream inputStream,
            int reqWidth, int reqHeight) {
        return decodeSampledBitmap(context, inputStream, reqWidth, reqHeight, false);
    }

    /**
     * @param context         context
     * @param inputStream     stream to be decoded
     * @param reqWidth        require width
     * @param reqHeight       require height
     * @param targetShortEdge should decoded image has size larger or equal than short edge
     * @return decoded bitmap
     */
    public static Bitmap decodeSampledBitmap(@NonNull Context context, InputStream inputStream,
            int reqWidth, int reqHeight, boolean targetShortEdge) {

        if (context == null || inputStream == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais;
        try {
            org.apache.commons.io.IOUtils.copy(inputStream, baos);
        } catch (IOException e) {
            return null;
        }
        byte[] bytes = baos.toByteArray();

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;

        bais = new ByteArrayInputStream(bytes);
        BitmapFactory.decodeStream(bais, null, options);

        // Calculate inSampleSize
        int inSampleSize = 1;
        if (targetShortEdge) {
            int minSize = reqWidth > reqHeight ? reqHeight : reqWidth;
            inSampleSize = calculateInSampleSize(options, minSize);
        } else {
            inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        }
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inScaled = false;

        Bitmap resultBitmap = null;
        try {
            // reset input stream
            bais = new ByteArrayInputStream(bytes);
            resultBitmap = BitmapFactory.decodeStream(bais, null, options);
        } catch (OutOfMemoryError e1) {
            try {
                // reset input stream
                bais = new ByteArrayInputStream(bytes);
                options.inSampleSize = inSampleSize * 2;
                resultBitmap = BitmapFactory.decodeStream(bais, null, options);
            } catch (OutOfMemoryError e2) {
                resultBitmap = null;
            }
        }
        return resultBitmap;
    }

    public static Bitmap readBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap resultBitmap = BitmapFactory.decodeFile(path, options);
        return resultBitmap;
    }

    public static Bitmap readBitmap(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        Bitmap resultBitmap = BitmapFactory.decodeFile(path, options);
        return resultBitmap;
    }

    public static Bitmap convert(@NonNull Bitmap bitmap, @NonNull Bitmap.Config config) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        if (config == null) {
            return bitmap;
        }

        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint(BITMAP_PAINT_FLAGS);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle();

        return convertedBitmap;
    }

    public static Bitmap scaleBitmapPreserveAspectRatio(Bitmap bitmap, int reqWidth,
            int reqHeight) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (reqWidth >= width && reqHeight >= height) {
            /* if source already smaller than or equal require width and height return source */
            return bitmap;
        }

        int destWidth = width;
        int destHeight = height;

        if (width >= reqWidth && height >= reqHeight) {
            /* both size are greater than require size */
            float scaleWidthRatio = reqWidth / (float) width;
            float scaleHeightRatio = reqHeight / (float) height;

            if (scaleHeightRatio < scaleWidthRatio) {
                destWidth = (int) (width * scaleHeightRatio);
                destHeight = (int) (height * scaleHeightRatio);
            } else {
                destWidth = (int) (width * scaleWidthRatio);
                destHeight = (int) (height * scaleWidthRatio);
            }
        } else if (width >= reqWidth) {
            /* width are greater than require width */
            float scaleRatio = reqWidth / (float) width;
            destWidth = reqWidth;
            destHeight = (int) (height * scaleRatio);
        } else if (height >= reqHeight) {
            /* height are greater than require height */
            float scaleRatio = reqHeight / (float) height;
            destWidth = (int) (width * scaleRatio);
            destHeight = reqHeight;
        }
        return Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
    }

    public static Drawable decodeSampledDrawableSizeRes(@NonNull Context context,
            @DrawableRes int drawableId, @DimenRes int sizeRes) {
        int size = context.getResources()
                .getDimensionPixelSize(sizeRes);
        return decodeSampledDrawable(context, drawableId, size, size);
    }

    public static Drawable decodeSampledDrawableSizeRes(@NonNull Context context,
            @DrawableRes int drawableId, @DimenRes int widthRes, @DimenRes int heightRes) {
        int width = context.getResources()
                .getDimensionPixelSize(widthRes);
        int height = context.getResources()
                .getDimensionPixelSize(heightRes);
        return decodeSampledDrawable(context, drawableId, width, height);
    }

    public static Drawable decodeSampledDrawable(@NonNull Context context,
            @DrawableRes int drawableId, int width, int height) {
        if (context == null) {
            return null;
        }
        Bitmap bitmap = decodeSampledBitmap(context, drawableId, width, height);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            /*
             * Calculate the largest inSampleSize value that is a power of 2 and keeps both height
             * and width larger than the requested height and width.
             */
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) >
                    reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqMinSize) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        final int shortSize = height > width ? width : height;

        int inSampleSize = 1;

        if (shortSize > reqMinSize) {
            final int halfShortSize = shortSize / 2;

            while ((halfShortSize / inSampleSize) > reqMinSize) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap createRoundBitmap(Bitmap source, int size) {
        return createRoundBitmap(source, size, 0, 0);
    }

    public static Bitmap createRoundBitmap(Bitmap source, int preferSize, int borderColor,
            int borderWidth) {

        int width = source.getWidth();
        int height = source.getHeight();

        int size = Math.min(width, height);
        float radius = size / 2.0f;

        int offsetX = (width - size) / 2;
        int offsetY = (height - size) / 2;

        /*
         * We're going to apply this paint eventually using a porter-duff xfer mode. This will allow
         * us to only overwrite certain pixels. BLACK is arbitrary. This could be any color that was
         * fully opaque (alpha = 255)
         */
        Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xferPaint.setColor(Color.BLACK);

         /* create result canvas */
        // Use ARGB_8888 since we're going to add alpha to the image.
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas resultCanvas = new Canvas(result);

        //draw mark
        resultCanvas.drawCircle(radius, radius, radius, xferPaint);

         /* Now we apply the 'magic sauce' to the paint */
        xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        /* draw result */
        resultCanvas.drawBitmap(source, new Rect(offsetX, offsetY, size + offsetX, size + offsetY),
                new Rect(0, 0, size, size), xferPaint);

        if (borderWidth > 0) {
            /* draw border */

            Paint borderPaint = new Paint();
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(borderColor);

            // Use ARGB_8888 since we're going to add alpha to the image.
            Bitmap border = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvasBorder = new Canvas(border);

            //draw mask that smaller than border
            canvasBorder.drawCircle(radius, radius, radius - borderWidth, borderPaint);

            borderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

            canvasBorder.drawCircle(radius, radius, radius, borderPaint);

            /* draw border to result bitmap */
            resultCanvas.drawBitmap(border, 0, 0, null);
        }
        return result;
    }
}