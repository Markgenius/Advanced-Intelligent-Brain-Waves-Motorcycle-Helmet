package com.example.tryingline2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (LineChart) findViewById(R.id.linechart);

//        mchart.setOnChartGestureListener(MainActivity.this);
//        mchart.setOnChartValueSelectedListener(MainActivity.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        LimitLine upper_limit = new LimitLine(65f, "Danger");
        upper_limit.setLineWidth(4f);
        upper_limit.enableDashedLine(10f,20f,0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setTextSize(15f);
        LimitLine lower_limit = new LimitLine(35f, "Too Low");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f,10f,0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(15f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(25f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(1,50f));
        yValues.add(new Entry(2,70f));
        yValues.add(new Entry(3,30f));
        yValues.add(new Entry(4,50f));
        yValues.add(new Entry(5,40f));
        yValues.add(new Entry(6,65f));

        LineDataSet set1 = new LineDataSet(yValues, "Data set 1");


        set1.setFillAlpha(110);

        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextColor(Color.GREEN);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data);

        String[] values = new String[] {"Jan", "Feb", "Mar", "Apr", "May","Jun"};
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
    }

    public class MyXAxisValueFormatter extends IndexAxisValueFormatter {
        private  String[] mValues;
        public  MyXAxisValueFormatter(String[] values){
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis){
            return mValues[(int)value];
        }
    }
}