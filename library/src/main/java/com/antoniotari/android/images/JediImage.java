package com.antoniotari.android.images;

import com.antoniotari.android.jedi.Log;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JediImage {
    //-----------------------------------------------------------------------------
    public static Drawable DrawableFromAsset(Context c, String imageName) {
        try {
            return Drawable.createFromStream(c.getAssets().open(imageName), null);
        } catch (Exception d) {
            return null;
        }
    }

    //-----------------------------------------------------------------------------
    public static Bitmap BitmapFromAsset(Context c, String imageName) {
        try {
            return drawableToBitmap(Drawable.createFromStream(c.getAssets().open(imageName), null));
        } catch (Exception d) {
            return null;
        }
    }

    //-----------------------------------------------------------------------------
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    //-----------------------------------------------------------------------------
    public static Bitmap ResizeBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    //-----------------------------------------------------------------------------
//	public static void DisplayImageFromUrl(Context context,ImageView img,String path)
//	{
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//		.cacheInMemory()
//		.cacheOnDisc()
//		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//		.displayer(new RoundedBitmapDisplayer(20))
//		.build();
//
//		// Load and display image
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//		.defaultDisplayImageOptions(options)
//		.build();
//		ImageLoader.getInstance().init(config);
//		ImageLoader.getInstance().displayImage(path, img);
//	}

    //-----------------------------------------------------------------------------
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.error("getBitmapFromURL", e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.error("getBitmapFromURL", e);
        }
        return null;
    }

    //-----------------------------------------------------------------------------
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //-----------------------------------------------------------------------------
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

        return inSampleSize;
    }

    //-----------------------------------------------------------------------------

    /**
     * @param sourceB      the source bitmap @Bitmap
     * @param size         the size to reduce the bitmap to
     * @param isTheMaxSize true: the passed size the max size, false: the passed size the min size
     *
     * @return the scaled bitmap
     */
    public static Bitmap scaleBitmap(Bitmap sourceB, final int size, final boolean isTheMaxSize) {
        int w = 0, h = 0;

        float reducedRatio = (float) sourceB.getWidth() / (float) size;
        w = size;
        h = Math.round((float) sourceB.getHeight() / reducedRatio);

        if (isTheMaxSize) {
            if (h > size) {
                reducedRatio = (float) sourceB.getHeight() / (float) size;
                h = size;
                w = Math.round((float) sourceB.getWidth() / reducedRatio);
            }
        } else {
            if (h < size) {
                reducedRatio = (float) sourceB.getHeight() / (float) size;
                h = size;
                w = Math.round((float) sourceB.getWidth() / reducedRatio);
            }
        }
        try {
            return Bitmap.createScaledBitmap(sourceB, w, h, true);
        } catch (Exception d) {
            return sourceB;
        }
    }

    /**
     * if the bitmap is too big it reduces it to the specified size
     *
     * @param sourceB the source bitmap @Bitmap
     * @param size    the size of the image in kbyte we want to obtain (1px=1kb)
     */
    public static Bitmap reduceBitmap(Bitmap sourceB, final int maxSize) {
        long actualSize = (long) sourceB.getWidth() * sourceB.getHeight();
        Log.debug("actual size " + actualSize);
        if (actualSize <= maxSize) {
            return sourceB;
        }
        int reducedSideSize = (int) Math.round(Math.sqrt((double) (actualSize - maxSize)));
        Log.debug("reducedSideSize " + reducedSideSize);

        return scaleBitmap(sourceB, reducedSideSize, true);
    }

    //-----------------------------------------------------------------------------
    public static Bitmap transparentBitmap(int w, int h) {
        Bitmap bp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        bp.eraseColor(android.graphics.Color.TRANSPARENT);
        return bp;
    }

    //-----------------------------------------------------------------------------
    public static Bitmap createColorBitmap(int w, int h, int color) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        return bitmap;
    }

    //-----------------------------------------------------------------------------
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

//	/**
//     * Uses RenderScript to Blur a Bitmap. From support Library.
//     * @param rs can use RenderScript.create(context). If blurring multiple times, pass the same instance of RenderScript.
//     * @param bitmap Your original Bitmap.
//     * @param blurRadius The radius which defines the amount to blur the image. Range is 0 - 25 inclusive.
//     */
//    @TargetApi(8)
//    public static void renderScriptBlur(RenderScript rs, Bitmap bitmap, float blurRadius) {
//
//        final Allocation input = Allocation.createFromBitmap(rs, bitmap); //use this constructor for best performance,
//        // because it uses USAGE_SHARED mode which reuses memory
//        final Allocation output = Allocation.createTyped(rs, input.getType());
//        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        script.setRadius(blurRadius);
//        script.setInput(input);
//        script.forEach(output);
//        output.copyTo(bitmap);
//    }

    /**
     * Uses RenderScript to Blur a Bitmap.
     *
     * @param rs         can use RenderScript.create(context). If blurring multiple times, pass the same instance of RenderScript.
     * @param bitmap     Your original Bitmap.
     * @param blurRadius The radius which defines the amount to blur the image. Range is 0 - 25 inclusive.
     */
    @TargetApi (17)
    public static void renderScriptBlur(RenderScript rs, Bitmap bitmap, float blurRadius) {
        final Allocation input = Allocation.createFromBitmap(rs, bitmap); //use this constructor for best performance,
        // because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(blurRadius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
    }

    /**
     * takes the screenshot of the passed view
     */
    public static Bitmap viewScreenShot(View v1) {
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        return bitmap;
        //BitmapDrawable bitmapDrawable = new BitmapDrawable(bm);
    }

    /**
     * takes a screenshot of the all activity
     */
    public static Bitmap activityScreenShot(Activity activity, boolean showNotificationBar) {
        View v1;
        if (showNotificationBar) {
            v1 = activity.getWindow().getDecorView().getRootView();
        } else {
            v1 = activity.findViewById(android.R.id.content);
        }
        return viewScreenShot(v1);
    }

    public static void addBitmapToIntent(Intent intent, Bitmap bitmap, String key) {
        ByteArrayOutputStream baosSCreen = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baosSCreen);
        byte[] screenByte = baosSCreen.toByteArray();
        intent.putExtra(key, screenByte);
    }

    public static void addBitmapToIntent(Intent intent, Bitmap bitmap, String key, int compression) {
        ByteArrayOutputStream baosSCreen = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compression, baosSCreen);
        byte[] screenByte = baosSCreen.toByteArray();
        intent.putExtra(key, screenByte);
    }

    /**
     * @param resizeRatio float value from 0.1 to 1.0
     */
    public static void addBitmapToIntent(Intent intent, Bitmap bitmapBig, String key, float resizeRatio, int compression) {
        Bitmap bitmap = ResizeBitmap(bitmapBig, Math.round((float) bitmapBig.getWidth() * resizeRatio)
                , Math.round((float) bitmapBig.getHeight() * resizeRatio));
        addBitmapToIntent(intent, bitmap, key, compression);
    }

    public static Bitmap retrieveBitmapFromIntent(Activity activity, String key) {
        Bitmap retBitmap = null;
        if (activity != null && activity.getIntent() != null && activity.getIntent().getExtras() != null) {
            Bundle extras = activity.getIntent().getExtras();
            try {
                byte[] b = extras.getByteArray(key);
                retBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            } catch (Exception e) {
            }
        }
        return retBitmap;
    }

    /**
     * This helper function can be used to convert your bitmap to any {@link Config}.[TYPE]. <br><br> Example: <br> You can convert to
     * Bitmap.Config.ARGB_8888 for {@link #renderScriptBlur(android.support.v8.renderscript.RenderScript, Bitmap, float)} to work.
     *
     * @param bitmap     your original Bitmap
     * @param bitmapType ex. Bitmap.Config.ARGB_8888
     *
     * @return the converted Bitmap
     */
    public static Bitmap convertBitmapType(Bitmap bitmap, Config bitmapType) {

        int numPixels = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmapType);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());

        return result;
    }
}
