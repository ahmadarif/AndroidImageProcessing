package com.ahmadarif.imageprocessing.process.basic;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageFilter {

    public static final int[][] filter9 = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };
    public static final int[][] filter16 = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
    };

    public static Bitmap getFilteredImage(Bitmap givenImage, int[][] filter, int iterationNum) {
        int count = 0;
        while (count < iterationNum) {
            for (int y = 1; y + 1 < givenImage.getHeight(); y++) {
                for (int x = 1; x + 1 < givenImage.getWidth(); x++) {
                    int tempColor = getFilteredValue(givenImage, y, x, filter);
                    givenImage.setPixel(x, y, tempColor);
                }
            }
            count++;
        }
        return givenImage;
    }

    private static int getFilteredValue(Bitmap givenImage, int y, int x, int[][] filter) {
        int r = 0, g = 0, b = 0;
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                r += (filter[1 + j][1 + k] * (Color.red(givenImage.getPixel(x + k, y + j))));
                g += (filter[1 + j][1 + k] * (Color.green(givenImage.getPixel(x + k, y + j))));
                b += (filter[1 + j][1 + k] * (Color.blue(givenImage.getPixel(x + k, y + j))));
            }

        }
        r = r / sum(filter);
        g = g / sum(filter);
        b = b / sum(filter);
        return Color.rgb(r, g, b);
    }

    private static int sum(int[][] filter) {
        int sum = 0;
        for (int y = 0; y < filter.length; y++) {
            for (int x = 0; x < filter[y].length; x++) {
                sum += filter[y][x];
            }
        }
        return sum;
    }
}