package com.jayam.impactapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;

import static com.jayam.impactapp.common.MfimoApp.getAppContext;

/**
 * Created by administrator_pc on 02-03-2020.
 */

public class ImageCompress

{
    private static final int BUFFER = 4096;//for zipper
//        private static final float maxHeight = 1280.0f;
//        private static final float maxWidth = 1280.0f;

    private static final float maxHeight = 816.0f;             //55746
    private static final float maxWidth = 612.0f;

        private static final DecimalFormat format = new DecimalFormat("#.##");
        private static final long MiB = 1024 * 1024;
        private static final long KiB = 1024;



        public String compressImage(File file,String imagePath) {
            Log.v("","abspath"+file.getAbsolutePath()+"/"+imagePath);
            Bitmap scaledBitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath()+"/"+imagePath, options);
            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            Log.v("","actualHeight"+actualHeight);
            Log.v("","actualWidth"+actualWidth);

            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }
            Log.v("","actualHeight11"+actualHeight);
            Log.v("","actualWidth11"+actualWidth);
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];
            try {
                bmp = BitmapFactory.decodeFile(file.getAbsolutePath()+"/"+imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }
            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            bmp.recycle();

            ExifInterface exif;
            try {
                exif = new ExifInterface(file.getAbsolutePath()+"/"+imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out;
            String filepath = getFilename(file.getAbsolutePath(),imagePath);

            Log.v("","filepath"+filepath);
            try {
                out = new FileOutputStream(filepath);
                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            return filepath;
        }

        private String getFilename(String imagePath,String path) {
            Log.v("","imagePath"+imagePath);
            Log.v("","path"+path);
            File mediaStorageDir = new File(imagePath+"/"+path);
            // Create the storage directory if it does not exist
            if (mediaStorageDir.exists()) mediaStorageDir.delete();

            {
                Log.v("","pathvariableexist");
            }
            String pathvariable=mediaStorageDir.getAbsolutePath();
            Log.v("","pathvariable"+pathvariable);
            return (mediaStorageDir.getAbsolutePath() );
        }

        public String getFileSize(File file) {
            final double length = file.length();
            Log.v("","filelength"+length);
            if (length > MiB) {
                return format.format(length / MiB) + " MiB";
            }
            if (length > KiB) {
                return format.format(length / KiB) + " KiB";
            }
            return format.format(length) + " B";
        }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        //new changess
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

}
