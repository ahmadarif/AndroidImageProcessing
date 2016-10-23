package com.ahmadarif.imageprocessing.process.mathscanner;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MathScanner {

    private static final String TAG = "MathScanner";
    private static final int MIN_WIDTH = 5;

    public static List<Bitmap> segment(Bitmap bmp) {
        List<Bitmap> result = new ArrayList<>();
        int firstX;
        int lastX;
        int lastScanX = 0;
        boolean available;
        boolean detected;

        do {
            available = false;
            detected = false;
            firstX = -1;
            lastX = -1;

            for (int x = lastScanX; x < bmp.getWidth() - 1; x++) {
                boolean isBlack = false;
                for (int y = 0; y < bmp.getHeight() - 1; y++) {
                    if (Color.red(bmp.getPixel(x, y)) == 0) {
                        // warna hitam pertama
                        if (firstX == -1) {
                            firstX = x - 2;
                            detected = true;
                        }

                        // beritahu kolom ke x terdapat warna hitam
                        isBlack = true;
                    }
                }

                // warna hitam terakhir
                if (detected && !isBlack) {
                    lastX = x + 2;
                    lastScanX = lastX;
                    available = true;
                    break;
                }
            }

            if (available) {
                if (firstX+MIN_WIDTH < lastX) {
                    Log.i(TAG, String.format("firstX = %d, lastX = %d", firstX, lastX));

                    Bitmap cropBitmap = Bitmap.createBitmap(bmp, firstX, 0, lastX-firstX, bmp.getHeight());
                    result.add(cropBitmap);
                }
            }
        } while (available && lastScanX < bmp.getWidth() - 1);

        return result;
    }

}