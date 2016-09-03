package com.ahmadarif.imageprocessing.process;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmad Arif on 9/3/2016.
 */
public class ImageProcessing {

    public static Bitmap convertToRed(Bitmap bmp) {
        /* kamus */
        Bitmap result;

        /* algoritma */

        result = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p) + 150;
                int g = 0;
                int b = 0;
                result.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }

        return result;
    }

    public static Bitmap convertToGreen(Bitmap bmp) {
        /* kamus */
        Bitmap result;

        /* algoritma */

        result = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = 0;
                int g = Color.green(p) + 150;
                int b = 0;
                result.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }

        return result;
    }

    public static Bitmap convertToBlue(Bitmap bmp) {
        /* kamus */
        Bitmap result;

        /* algoritma */

        result = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());

        for(int i = 0; i < bmp.getWidth(); i++) {
            for(int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = 0;
                int g = 0;
                int b = Color.blue(p) + 150;
                result.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }

        return result;
    }

    public static Bitmap grayscale(Bitmap bmp, int version) {
        /* kamus */
        Bitmap result;


        /* algoritma */

        result = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());

        for(int i = 0; i < bmp.getWidth(); i++) {
            for(int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                int gray;
                if (version == 1)
                    gray = (r + g + b) / 3;
                else
                    gray = (int)(r * 0.299 + g * 0.587 + b * 0.114);

                result.setPixel(i, j, Color.rgb(gray, gray, gray));
            }
        }

        return result;
    }

    public static Bitmap treshold(Bitmap bmp) {
        /* kamus */
        Bitmap result;


        /* algoritma */

        result = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());

        for(int i = 0; i < bmp.getWidth(); i++) {
            for(int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                // convert to treshold
                int tresh = (r + g + b) / 3;
                if (tresh < 128) {
                    tresh = 0;
                }
                else {
                    tresh = 255;
                }

                result.setPixel(i, j, Color.rgb(tresh, tresh, tresh));
            }
        }

        return result;
    }

    public static Map<Integer, Integer> countColor(Bitmap bmp) {
        /* kamus */
        Map<Integer, Integer> colors = new HashMap<>();

        /* algoritma */

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);

                if (!colors.containsKey(p)) {
                    colors.put(p, 1);
                } else {
                    colors.put(p, colors.get(p) + 1);
                }
            }
        }

        return colors;
    }
}
