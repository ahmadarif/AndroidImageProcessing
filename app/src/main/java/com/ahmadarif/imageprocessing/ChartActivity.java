package com.ahmadarif.imageprocessing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmad Arif on 9/3/2016.
 */
public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.barchart)
    BarChart mBarChart;

    private static Map<Integer, Integer> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setTitle("Detail Colors");

        ButterKnife.bind(this);

        for (Map.Entry<Integer, Integer> entry : colors.entrySet())
        {
            int p = entry.getKey();
            int r = Color.red(p);
            int g = Color.green(p);
            int b = Color.blue(p);
            String str = new String(r + "," + g + "," + b);

            mBarChart.addBar(new BarModel(str, entry.getValue(), Color.rgb(r, g, b)));
        }

        mBarChart.startAnimation();
    }

    public static void start(Context context, Map<Integer, Integer> colors) {
        ChartActivity.colors = colors;

        Intent intent = new Intent(context, ChartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
