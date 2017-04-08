package example.tctctc.com.tybookreader.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by tctctc on 2017/4/3.
 * Function:
 */

public class BitmapUtils {
    //    public static Bitmap decodeFromResourceResize(Resources resources, int resId, int needWidth, int needHeight) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(resources, resId, options);
//
//        options.inSampleSize = countInSampleSize(options, needWidth, needHeight);
//
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(resources, resId, options);
//
//    }
//
    private static int countInSampleSize(BitmapFactory.Options options, int needWidth, int needHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width > needWidth || height > needHeight) {
            float scale = Math.max(Math.round((float) needWidth / (float) width), Math.round((float) needHeight / (float) height));
        }
        return inSampleSize;
    }


    public static Bitmap decodeFromResourceResize(Resources resources, int resId, int needWidth, int needHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);

        if (options.outWidth > needWidth && options.outHeight > needHeight) {
            options.inSampleSize = countInSampleSize(options, needWidth, needHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(resources, resId, options);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
            float scaleX = (float) needWidth / (float) bitmap.getWidth();
            float scaleY = (float)needHeight / (float)bitmap.getHeight();


            Log.d("aaa","scaleX:"+scaleX);
            Log.d("aaa","scaleY:"+scaleY);

            float scale = Math.max(scaleX, scaleY);
            Matrix matrix = new Matrix();
            // 缩放原图
            matrix.postScale(scale, scale);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    matrix, true);
        }

    }
}
