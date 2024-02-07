package com.imagefiletransformer.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.imagefiletransformer.constants.ScaleMode;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtility {
    private static String TAG = BitmapUtility.class.getSimpleName();
    public Context context;

    public BitmapUtility(Context context) {

        this.context = context;
    }
    
//    public Bitmap resizeGlide(Bitmap bitmap) throws ExecutionException, InterruptedException {
//        return Glide.with(context)
//                .asBitmap()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .load(bitmap)
//                .override(512, 512)
//                .centerInside()
//                .submit().get();
//    }

    public static Bitmap scalePreserveRatioM(Bitmap imageToScale, int destinationWidth, int destinationHeight) {
        if (destinationHeight > 0 && destinationWidth > 0 && imageToScale != null) {
            int width = imageToScale.getWidth();
            int height = imageToScale.getHeight();

            // Calculate the max changing amount and decide which dimension to use
            float widthRatio = (float) destinationWidth / (float) width;
            float heightRatio = (float) destinationHeight / (float) height;

            // Use the ratio that will fit the image into the desired sizes
            float minRatio = Math.min(widthRatio, heightRatio);
            int finalWidth = (int) Math.floor(width * minRatio);
            int finalHeight = (int) Math.floor(height * minRatio);

            // Scale given bitmap to fit into the desired area
            imageToScale = Bitmap.createScaledBitmap(imageToScale, finalWidth, finalHeight, true);

            // Create a bitmap with desired sizes
            Bitmap scaledImage = Bitmap.createBitmap(destinationWidth, destinationHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(scaledImage);

            // Draw background color
            Paint paint = new Paint();
            paint.setColor(Color.TRANSPARENT); // if transparent then encoder must read ALPHA
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

            // Calculate the ratios and decide which part will have empty areas (width or height)
            float ratioBitmap = (float) finalWidth / (float) finalHeight;
            float destinationRatio = (float) destinationWidth / (float) destinationHeight;
            float left = ratioBitmap >= destinationRatio ? 0 : (float) (destinationWidth - finalWidth) / 2;
            float top = ratioBitmap < destinationRatio ? 0 : (float) (destinationHeight - finalHeight) / 2;
            canvas.drawBitmap(imageToScale, left, top, null);

            return scaledImage;
        } else {
            return imageToScale;
        }
    }
    private static Bitmap fitCenter(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
    public static Bitmap crop(Bitmap img, int width, int height) {
        Log.v(TAG,"crop");

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        float scale = Math.max(width / (float) imgWidth, height / (float) imgHeight);

        int scaledWidth = (int) (imgWidth * scale);
        int scaledHeight = (int) (imgHeight * scale);

        Bitmap scaledImg = Bitmap.createScaledBitmap(img, scaledWidth, scaledHeight, true);

        int cropX = (scaledWidth - width) / 2;
        int cropY = (scaledHeight - height) / 2;

        return Bitmap.createBitmap(scaledImg, cropX, cropY, width, height);

    }
    public static Bitmap stretch(Bitmap img, int width, int height) {
        Log.v(TAG,"stretch");

        return Bitmap.createScaledBitmap(img, width, height, false);

    }


    public static Bitmap resizeBitmap(Bitmap bitmap, StaticOptions options) {

        if(options.WIDTH == 0){
            options.WIDTH = bitmap.getWidth();
        }
        if(options.HEIGHT == 0){
            options.HEIGHT = bitmap.getHeight();
        }


        ScaleMode scaleMode = ScaleMode.fromString(options.SCALE_MODE);
        Log.v(TAG,"Mode os "+scaleMode.getMode());

        return applyScaling(bitmap, scaleMode, options.WIDTH, options.HEIGHT);

    }

    private static Bitmap applyScaling(Bitmap bitmap, ScaleMode crop, int width, int height) {
        switch (crop) {
            case FIT_CENTER:
                return fitCenter(bitmap, width, height);
            case CROP:
                return crop(bitmap, width, height);
            case STRETCH:
                return stretch(bitmap, width, height);
            default:
                return fitCenter(bitmap, width, height);
        }
    }
    private static Bitmap.CompressFormat getCompressFormat(String format) {
        switch (format.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return Bitmap.CompressFormat.JPEG;
            case "png":
                return Bitmap.CompressFormat.PNG;
            case "webp":
                return Bitmap.CompressFormat.WEBP;
            default:
                // Default to JPEG for incorrect format
                return Bitmap.CompressFormat.JPEG;
        }
    }
    public static File saveBitmapToFile(Bitmap newBitmap, StaticOptions options) {
        Log.v(TAG,"start saveBitmapToFile : options.targetFormat:  "+options.TARGET_FORMAT);

        if (options.TARGET_FORMAT == null){
            Log.v(TAG,"return null bcz options.targetFormat:  "+options.TARGET_FORMAT);
            return null;
        }

        Bitmap.CompressFormat validFormat = getCompressFormat(options.TARGET_FORMAT);
        File file ;

        try{

            Log.v(TAG,"validFormat? : "+validFormat);
            file = FileUtility.createOutputFile(String.valueOf(validFormat));
        }catch (Exception e){
            Log.v(TAG,"strat 3 saveBitmapToFile Exception:  "+e);
            throw new RuntimeException(e);
        }


        try(FileOutputStream fos = new FileOutputStream(file)){

            newBitmap.compress(validFormat, options.QUALITY, fos);
        } catch (Exception e) {
            Log.w(TAG,"saveBitmapToFile : "+e.getMessage());
            throw new RuntimeException(TAG+":saveBitmapToFile "+e.getMessage());
        }

        return file;
    }
}
