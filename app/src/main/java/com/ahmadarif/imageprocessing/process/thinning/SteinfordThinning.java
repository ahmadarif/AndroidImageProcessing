package com.ahmadarif.imageprocessing.process.thinning;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.ahmadarif.imageprocessing.process.utils.ImageUtils;

/**
 * Created by ARIF on 03-Oct-16.
 */

public class SteinfordThinning {

    public static Bitmap process(Bitmap bmp) {
        /* kamus */
        int[][] imageData = new int[bmp.getHeight()][bmp.getWidth()];
        Bitmap result = Bitmap.createBitmap(bmp);

        /* algoritma */

        // ambil imageData berupa array 2D informasi piksel hitam dan putih
        imageData = ImageUtils.getImageDataTreshold(bmp);

        // zhangsuen proses
//        process(imageData);

        // terapkan hasil imageData ke result
        for (int y = 0; y < bmp.getHeight(); y++) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                if (imageData[y][x] == 1) {
                    result.setPixel(x, y, Color.BLACK);
                } else {
                    result.setPixel(x, y, Color.WHITE);
                }
            }
        }

        return result;
    }

}
