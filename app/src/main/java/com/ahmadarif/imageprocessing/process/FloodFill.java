package com.ahmadarif.imageprocessing.process;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by ARIF on 03-Oct-16.
 */

public class FloodFill {

    public static Bitmap process(Bitmap bmp) {
        boolean[][] mark = new boolean[bmp.getHeight()][bmp.getWidth()];

        for (int row = 0; row < bmp.getHeight(); row++) {
            for (int col = 0; col < bmp.getWidth(); col++) {
                int p = bmp.getPixel(col, row);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int t = (r + g + b) / 3;
                if (t < 128) {
                    bmp.setPixel(col, row, Color.BLACK);
                } else {
                    bmp.setPixel(col, row, Color.WHITE);
                }
            }
        }

        //bmp.show();
        for (int row = 0; row < bmp.getHeight(); row++) {
            for (int col = 0; col < bmp.getWidth(); col++) {
                flood(bmp, mark, row, col, Color.BLACK, Color.RED);
            }
        }

        return bmp;
    }

    private static void flood(Bitmap img, boolean[][] mark, int row, int col, int srcColor, int tgtColor) {
        // make sure row and col are inside the image
        if (row < 0) return;
        if (col < 0) return;
        if (row >= img.getHeight()) return;
        if (col >= img.getWidth()) return;

        // make sure this pixel hasn't been visited yet
        if (mark[row][col]) return;

        // make sure this pixel is the right color to fill
        if (img.getPixel(col, row) != srcColor) return;

        // fill pixel with target color and mark it as visited
        img.setPixel(col, row, tgtColor);
        mark[row][col] = true;

        // animate
//        img.show();
//        sleep(25);

        // recursively fill surrounding pixels
        // (this is equivelant to depth-first search)
        flood(img, mark, row - 1, col, srcColor, tgtColor);
        flood(img, mark, row + 1, col, srcColor, tgtColor);
        flood(img, mark, row, col - 1, srcColor, tgtColor);
        flood(img, mark, row, col + 1, srcColor, tgtColor);
    }

    private static void sleep(int msec) {
        try {
            Thread.currentThread().sleep(msec);
        } catch (InterruptedException e) { }
    }
}
