package com.example.waiterapp.activity.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.waiterapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        final GraphView graph = (GraphView) findViewById(R.id.graf);
//        LineGraphSeries<DataPoint> bgseries= new LineGraphSeries<>(new DataPoint[]{
//
//                new DataPoint(0, 1000),
//                new DataPoint(1, 8000),
//                new DataPoint(2, 3500),
//                new DataPoint(3, 24000),
//                new DataPoint(4, 60000),
//                new DataPoint(5, 1300),
//                new DataPoint(6, 5300),
//                new DataPoint(7, 3500),
//                new DataPoint(8, 80000),
//                new DataPoint(9, 3500),
//                new DataPoint(10, 1300),
//                new DataPoint(11, 5300),
//                new DataPoint(12, 80000),
//                new DataPoint(13, 3500),
//                new DataPoint(14, 60000),
//                new DataPoint(15, 5300),
//                new DataPoint(16, 8000),
//                new DataPoint(17, 1000),
//                new DataPoint(18, 5300),
//                new DataPoint(19, 60000),
//                new DataPoint(20, 5300),
//                new DataPoint(21, 1300),
//                new DataPoint(22, 60000),
//                new DataPoint(23, 3500),
//                new DataPoint(24, 1300),
//                new DataPoint(25, 1000),
//                new DataPoint(26, 8000),
//                new DataPoint(27, 5300),
//                new DataPoint(28, 1300),
//                new DataPoint(29, 60000),
//                new DataPoint(30, 5300),
//                new DataPoint(31, 80000),
//        });
//
//        graph.addSeries(bgseries);
////        graph.setBackgroundColor(getResources().getColor(R.color.brownopa));
//        graph.getViewport().setScalable(true);
//        graph.getViewport().setScrollable(true);
////        graph.setTitle("نمودار رشد سرمایه");
//        graph.setTitleColor(getResources().getColor(R.color.browwn));
////        bgseries.setTitle("foo");
//        bgseries.setThickness(5);
//        bgseries.setDrawBackground(true);
//        bgseries.setColor(getResources().getColor(R.color.browwn));
////        bgseries.setBackgroundColor(Color.rgb(248,243,247));
//        bgseries.setBackgroundColor(getResources().getColor(R.color.brownopa));
//        bgseries.setAnimated(true);
//        bgseries.setColor(R.color.browwn);
////        graph.getLegendRenderer().setVisible(true);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        final GraphView graph = (GraphView) findViewById(R.id.graf);
        LineGraphSeries<DataPoint> bgseries= new LineGraphSeries<>(new DataPoint[]{

                new DataPoint(0, 1000),
                new DataPoint(1, 8000),
                new DataPoint(2, 3500),
                new DataPoint(3, 24000),
                new DataPoint(4, 60000),
                new DataPoint(5, 1300),
                new DataPoint(6, 5300),
                new DataPoint(7, 3500),
                new DataPoint(8, 80000),
                new DataPoint(9, 3500),
                new DataPoint(10, 1300),
                new DataPoint(11, 5300),
                new DataPoint(12, 80000),
                new DataPoint(13, 3500),
                new DataPoint(14, 60000),
                new DataPoint(15, 5300),
                new DataPoint(16, 8000),
                new DataPoint(17, 1000),
                new DataPoint(18, 5300),
                new DataPoint(19, 60000),
                new DataPoint(20, 5300),
                new DataPoint(21, 1300),
                new DataPoint(22, 60000),
                new DataPoint(23, 3500),
                new DataPoint(24, 1300),
                new DataPoint(25, 1000),
                new DataPoint(26, 8000),
                new DataPoint(27, 5300),
                new DataPoint(28, 1300),
                new DataPoint(29, 60000),
                new DataPoint(30, 5300),
                new DataPoint(31, 80000),
        });

        graph.addSeries(bgseries);
        graph.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
//        graph.setTitle("نمودار رشد سرمایه");
        graph.setTitleColor(getResources().getColor(android.R.color.white));
//        bgseries.setTitle("foo");
        bgseries.setThickness(5);
        bgseries.setDrawBackground(true);
        bgseries.setColor(Color.rgb(241,92,65));
        bgseries.setBackgroundColor(Color.rgb(248,243,247));
//        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    }
}