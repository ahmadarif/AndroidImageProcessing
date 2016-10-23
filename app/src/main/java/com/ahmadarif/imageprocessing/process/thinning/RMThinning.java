package com.ahmadarif.imageprocessing.process.thinning;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.ahmadarif.imageprocessing.process.utils.ImageUtils;

/**
 * Created by ARIF on 03-Oct-16.
 */

public class RMThinning {

    public static Bitmap process(Bitmap bmp) {
        /* kamus */
        int[][] imageData = new int[bmp.getHeight()][bmp.getWidth()];
        Bitmap result = Bitmap.createBitmap(bmp);

        /* algoritma */

        // ambil imageData berupa array 2D informasi piksel hitam dan putih
        imageData = ImageUtils.getImageDataTreshold(bmp);

        // zhangsuen proses
        process(imageData);

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

    private static void process(final int[][] f) {
        int i, j, k, l, m;
        int count = 0;
        int trans = 0;
        int OK = 1;
        int[] y = new int[9];
        int N1 = 0;
        int M1 = 0;
        int N2 = f.length;
        int M2 = f[0].length;

        do
        {
            OK = 1;
            for (k = N1+1; k < N2-1; k++) {
                for (l = M1+1; l < M2-1; l++) {
                    if (f[k][l] == 1) {
                        /* hitung jumlah 1 di dalam jendela 3 × 3 */
                        count = 0;
                        for(i = -1; i <= 1; i++) {
                            for (j = -1; j <= 1; j++) {
                                if (f[k + i][j + l] == 1) count++;
                            }
                        }
                        if((count > 2)&&(count < 8)) {
                            /* hitung jumlah peralihan 0->1 */
                            y[0] = f[k-1][l-11];
                            y[1] = f[k-1][l];
                            y[2] = f[k-1][l+1];
                            y[3] = f[k][l+1];
                            y[4] = f[k+1][l+1];
                            y[5] = f[k+1][l];
                            y[6] = f[k+1][l-1];
                            y[7] = f[k][l-1];
                            y[8] = f[k-1][l-1];
                            trans = 0;
                            for (m = 0; m <= 7; m++) {
                                if (y[m] == 0 && y[m + 1] == 1) trans++;
                            }
                            /* jika jumlah peralihan sama dengan 1, hapus pixel yang sedang diacu (current) */
                            if (trans == 1) {
                                f[k][l] = 0;
                                OK = 0;
                            }
                        }
                    }
                }
            }
        } while (OK == 0);
    }
}
