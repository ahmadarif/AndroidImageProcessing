package com.ahmadarif.imageprocessing.process.chaincode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.ahmadarif.imageprocessing.process.basic.ImageProcessing;

import java.util.ArrayList;
import java.util.List;

public class ChainCode {

    private static final String TAG = "ChainCode";

    public static ChainCodeResult singleObject(Bitmap bmp) {
        /* kamus */
        List<Integer> result = new ArrayList<>();
        List<Point> visited = new ArrayList<>();
        Point pointFirst = null;
        Point pointCurr = null;

        /* algoritma */

        // konversi bmp menjadi bmpTreshold
        bmp = ImageProcessing.treshold(bmp);

        // ambil pixel hitam pertama (titik awal dan akhir)
        for (int i = 0; i < bmp.getHeight(); i++) {
            for (int j = 0; j < bmp.getWidth(); j++) {
                if (Color.red(bmp.getPixel(j, i)) == 0) { // RGB : 0, 0, 0
                    pointFirst = new Point(j, i);
                    pointCurr = new Point(j, i);
                    visited.add(pointCurr);

//                    Log.i(TAG, "First : " + pointFirst.toString());
                    break;
                }
            }
            if (pointFirst != null) break;
        }

        // proses mengelilingi objek, sampai kembali lagi ke titik awal
        boolean isStop = false;
        while(!isStop) {
            Point[] points = new Point[8];
            points[0] = new Point(pointCurr.x + 1, pointCurr.y); // kanan
            points[1] = new Point(pointCurr.x + 1, pointCurr.y - 1); // kanan atas
            points[2] = new Point(pointCurr.x, pointCurr.y - 1); // atas
            points[3] = new Point(pointCurr.x - 1, pointCurr.y - 1); // kiri atas
            points[4] = new Point(pointCurr.x - 1, pointCurr.y); // kiri
            points[5] = new Point(pointCurr.x - 1, pointCurr.y + 1); // kiri bawah
            points[6] = new Point(pointCurr.x, pointCurr.y + 1); // bawah
            points[7] = new Point(pointCurr.x + 1, pointCurr.y + 1); // kanan bawah

            // cek calon pointCurrent yang baru
            for (int idx = 0; idx < points.length; idx++) {
                // cek apakah di luar ukuran gambar
                if ((points[idx].x >= 0 && points[idx].y >= 0) &&
                    (points[idx].x < bmp.getWidth() && points[idx].y < bmp.getHeight()) &&
                    isEdge(bmp, points[idx]) &&
                    !visited.contains(points[idx])
                ) {
                    visited.add(pointCurr);
                    pointCurr = points[idx];
                    result.add(direction(idx));
//                    Log.i(TAG, "Curr : " + pointCurr.toString());

                    break;
                }

                // proses berhenti ketika posisi selanjutnya sama dengan posisi pertama
                if (points[idx].equals(pointFirst)) {
                    pointCurr = points[idx];
                    result.add(direction(idx));
//                    Log.i(TAG, "Curr : " + pointCurr.toString());

                    isStop = true;
                    break;
                }
            }
        }

        return new ChainCodeResult(result, visited);
    }

    private static boolean isEdge(Bitmap bmp, Point p) {
        boolean containBlack = false;
        boolean containWhite = false;
        for (int y = p.y - 1; y <= p.y + 1; y++) {
            for (int x = p.x - 1; x <= p.x + 1; x++) {
                if (x < bmp.getWidth() && y < bmp.getHeight()) {
                    if (Color.red(bmp.getPixel(x, y)) == 255) {
                        containWhite = true;
                    }
                }
            }
        }
        if (Color.red(bmp.getPixel(p.x, p.y)) == 0) {
            containBlack = true;
        }
        return (containBlack && containWhite);
    }

    private static int direction(int idx) {
        switch (idx) {
            case 0 : return 0;
            case 1 : return 2;
            case 2 : return 4;
            case 3 : return 6;
            case 4 : return 1;
            case 5 : return 3;
            case 6 : return 5;
            default: return 7;
        }
    }


    // =============================================================================================
    // untuk memproses array 2D
    // =============================================================================================
    public static List<Integer> singleObject(int[][] image) {
        /* kamus */
        List<Integer> hasil = new ArrayList<>();

        /* algoritma */


        return hasil;
    }
}
