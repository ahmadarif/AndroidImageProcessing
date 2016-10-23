package com.ahmadarif.imageprocessing.process.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by ARIF on 03-Oct-16.
 */

public class ImageUtils {

    public static int[][] getImageData(Bitmap bmp) {
        /* kamus */
        int[][] imageData = new int[bmp.getHeight()][bmp.getWidth()];

        /* algoritma */

        // ambil image data dengan nilai treshold
        for (int y = 0; y < bmp.getHeight(); y++) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                imageData[y][x] = bmp.getPixel(x, y);
            }
        }

        return imageData;
    }

    public static int[][] getImageDataTreshold(Bitmap bmp) {
        /* kamus */
        int[][] imageData = new int[bmp.getHeight()][bmp.getWidth()];

        /* algoritma */

        // ambil image data dengan nilai treshold
        for (int y = 0; y < bmp.getHeight(); y++) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                int p = bmp.getPixel(x, y);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                int tresh = (r + g + b) / 3;
                if (tresh < 128) {
                    imageData[y][x] = 1;
                } else {
                    imageData[y][x] = 0;
                }
            }
        }

        return imageData;
    }

    public static int[][] getImageDataTreshold(Bitmap bmp, int treshold) {
        /* kamus */
        int[][] imageData = new int[bmp.getHeight()][bmp.getWidth()];

        /* algoritma */

        // ambil image data dengan nilai treshold
        for (int y = 0; y < bmp.getHeight(); y++) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                int p = bmp.getPixel(x, y);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                int tresh = (r + g + b) / 3;
                if (tresh < treshold) {
                    imageData[y][x] = 1;
                } else {
                    imageData[y][x] = 0;
                }
            }
        }

        return imageData;
    }

}
