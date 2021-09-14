package com.smu.songgulsongul;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageUtil {

    public static native void maxSize2048(long inputImageAddress, long outputImageAddress);

    public static native void maxSizeCustom(long inputImageAddress, long outputImageAddress, int maxPixelSize);

    public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public static int[] ImagePointToImageView(ImageView imageView, int pointX, int pointY) {
        int[] point = new int[2];

        if (imageView == null || imageView.getDrawable() == null)
            return point;

        float[] imageValues = new float[9];
        imageView.getImageMatrix().getValues(imageValues);

        final float scaleX = imageValues[Matrix.MSCALE_X];
        final float scaleY = imageValues[Matrix.MSCALE_Y];

        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        point[0] = left + Math.round(pointX * scaleX);
        point[1] = top + Math.round(pointY * scaleY);

        point[0] = Math.max(0, point[0]);
        point[0] = Math.min(imgViewW, point[0]);
        point[1] = Math.max(0, point[1]);
        point[1] = Math.min(imgViewH, point[1]);

        return point;

    }

    public static int[] ImageViewPointToImage(ImageView imageView, int pointX, int pointY) {

        int[] point = new int[2];

        if (imageView == null || imageView.getDrawable() == null)
            return point;

        float[] imageValues = new float[9];
        imageView.getImageMatrix().getValues(imageValues);

        final float scaleX = imageValues[Matrix.MSCALE_X];
        final float scaleY = imageValues[Matrix.MSCALE_Y];

        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        point[0] = Math.round((pointX - left) / scaleX);
        point[1] = Math.round((pointY - top) / scaleY);


        point[0] = Math.max(0, point[0]);
        point[0] = Math.min(origW, point[0]);
        point[1] = Math.max(0, point[1]);
        point[1] = Math.min(origH, point[1]);

        return point;


    }

}
