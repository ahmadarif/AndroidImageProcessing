package com.ahmadarif.imageprocessing.process.chaincode;

import android.graphics.Point;

import java.util.List;

/**
 * Created by ARIF on 19-Sep-16.
 */
public class ChainCodeResult {
    public List<Integer> dir;
    public List<Point> points;

    public ChainCodeResult(List<Integer> dir, List<Point> points) {
        this.dir = dir;
        this.points = points;
    }

    @Override
    public String toString() {
        String str = "";
        for (int x : dir) str += x;
        return str;
    }
}
