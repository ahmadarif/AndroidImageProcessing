package com.ahmadarif.imageprocessing.process.basic;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class ImageProcessing {

    public static Bitmap grayscale(Bitmap bmp) {
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

                int gray = (int)(r * 0.299 + g * 0.587 + b * 0.114);

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

    public static int[] imageHistogram(Bitmap input) {
        int[] histogram = new int[256];

        for(int i=0; i<histogram.length; i++) histogram[i] = 0;

        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = Color.red(input.getPixel(i, j));
                histogram[red]++;
            }
        }

        return histogram;
    }

    // Get binary treshold using Otsu's method
    private static int otsuValue(Bitmap original) {
        int[] histogram = imageHistogram(grayscale(original));
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for(int i=0; i<256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;

            if(wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    /**
     * Melakukan proses binarization citra menggunakan Algoritma Otsu
     * @param original
     * @param usingAlpha
     * @return
     */
    public static Bitmap otsuThreshold(Bitmap original, boolean usingAlpha) {
        int red;
        int newPixel;

        int threshold = otsuValue(original);

        Bitmap binarized = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());

        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {

                // Get pixels
                red = Color.red(original.getPixel(i, j));
                int alpha = Color.alpha(original.getPixel(i, j));

                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }

                if (usingAlpha) {
                    binarized.setPixel(i, j, Color.argb(alpha, newPixel, newPixel, newPixel));
                } else {
                    binarized.setPixel(i, j, Color.rgb(newPixel, newPixel, newPixel));
                }
            }
        }

        return binarized;
    }
}