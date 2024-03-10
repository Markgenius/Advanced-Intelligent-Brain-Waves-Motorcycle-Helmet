package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class MainActivity extends AppCompatActivity {

    //private LinearLayout mainLayout;
    LineChart mChart;
    LineData mLineData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = findViewById(R.id.mainLayout_);
        //create line chart
        mChart = new LineChart(this);
        //add to main layout
        //mChart.addView(mChart);

        //customize line chart
        //mChart.setDescription("");
        mChart.setNoDataText("No data for the moment");

        //enable value highlighting
        mChart.setHighlightPerDragEnabled(true);

        //enable touch gestures
        mChart.setTouchEnabled(true);

        //enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        //enable pinch zoom to avoid scaling x and y axis separately
        mChart.setPinchZoom(true);

        //alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        //work on data
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        //add data to line chart
        mChart.setData(data);

        //get legend object
        Legend i = mChart.getLegend();

        //customize legend
        i.setForm(Legend.LegendForm.LINE);
        i.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChart.getAxisLeft();
        yl.setTextColor(Color.WHITE);
        yl.setAxisMaxValue(120f);
        yl.setDrawGridLines(true);


        YAxis yl2 = mChart.getAxisRight();
        yl2.setEnabled(false);
    }
    private void addEntry(){
        LineData data = mChart.getData();

        if(data != null){
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            if(set == null){
                set = createSet();
                data.addDataSet(set);
            }

            data.addXValue("");
            data.addEntry(new Entry((float)Math.random()*75+60f, set.getEntryCount()),0);

            //notify chart data have changed
            mChart.notifyDataSetChanged();

            //limit number of visible entries
            mChart.setVisibleXRange(0,6);

            //scroll to the last entry
            mChart.moveViewToX(data.getXValCount()-7);
        }
    }

    //method to create set
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "SPL Db");
        set.setDrawFilled(true);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 177));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        return set;
    }
}




















