package com.example.crecimonstruo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class ImageUtils {

    public static Bitmap removeWhiteBackground(Bitmap source) {
        if (source == null) {
            return null;
        }

        int width = source.getWidth();
        int height = source.getHeight();

        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            if (isColorWhite(pixels[i])) {
                pixels[i] = Color.TRANSPARENT;
            }
        }

        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return resultBitmap;
    }

    private static boolean isColorWhite(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int threshold = 200;

        return red > threshold && green > threshold && blue > threshold;
    }
}
