package com.example.myapplication4;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class MainActivity extends AppCompatActivity{
    int NumOfChart = 2;
    LineDataSet[] lineDataSet = new LineDataSet[NumOfChart];
    LineChart[] mLineChart = new LineChart[NumOfChart];
    LineData[] lineData = new LineData[NumOfChart];


    int buttonAddCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R. layout. activity_main);

        mLineChart[0] = (LineChart) findViewById(R.id.linechart1);
        mLineChart[1] = (LineChart) findViewById(R.id.linechart2);
        for(int i = 0; i < NumOfChart; i++){
            mLineChart[i].setDrawBorders(true); // show border
            mLineChart[i].getAxisLeft().setDrawLabels(false);
            mLineChart[i].getAxisRight().setDrawLabels(false);
            if(i != 0) mLineChart[i].getXAxis().setDrawLabels(false);
            mLineChart[i].getLegend().setEnabled(false);
            mLineChart[i].setMaxVisibleValueCount(0);
            mLineChart[i].getDescription().setText("Line"+String.valueOf(i+1));

            lineData[i] = new LineData();
            lineDataSet[i] = new LineDataSet(null, "Line"+String.valueOf(i+1));
            lineData[i].addDataSet(lineDataSet[i]);
            mLineChart[i].setData(lineData[i]);
        }
    }
    public void onBtnClick (View view){
        buttonAddCounter ++;
        Entry entry1 = new Entry(buttonAddCounter, (float) (Math. random()) * 80); // create a point
        lineData[0].addEntry(entry1, 0); // Add entry to the polyline at the specified index
        if(buttonAddCounter > 10) lineData[0].removeEntry(buttonAddCounter-10,0);
        mLineChart[0].setData(lineData[0]);

        Entry entry2 = new Entry(buttonAddCounter, (float) (Math. random()) * 80); // create a point
        lineData[1].addEntry(entry2, 0); // Add entry to the polyline at the specified index
        if(buttonAddCounter > 10) lineData[1].removeEntry(buttonAddCounter-10,0);
        mLineChart[1].setData(lineData[1]);

        showLine(0);
    }
    public void showLine(int index) {
        for(int i = 0; i < NumOfChart; i++){
            mLineChart[i]
                .getLineData()
                .getDataSets()
                .get(index)
                .setVisible(true);
            mLineChart[i].invalidate();
        }
    }
}