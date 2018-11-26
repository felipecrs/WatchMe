package com.readme.app.model.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

import androidx.room.TypeConverter;

public final class Converter {

    public static final int DEFAULT_BOOK_IMAGE_WIDTH = 128;
    public static final int DEFAULT_BOOK_IMAGE_HEIGHT = 256;

    @TypeConverter
    public static byte[] bitmapToByteArray(Bitmap image) {
        if(image == null) { return null; }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @TypeConverter
    public static Bitmap byteArrayToBitmap(byte[] image) {
        if(image == null) { return null; }
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap drawableToBitmap(Drawable image) {
        return ((BitmapDrawable)image).getBitmap();

    }


    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



}
