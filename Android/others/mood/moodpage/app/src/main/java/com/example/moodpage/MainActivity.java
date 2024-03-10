package com.example.moodpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    // creating a variable for scatter chart
    private ScatterChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = findViewById(R.id.k);
        title.setTextSize(30);
        title.setText("MOOD CHART"); //set text for text view

        //add number here !!!!!!!!!!!!!!
        //float xvalue=4;
        Random x = new Random();

        int xvalue=x.nextInt(20);
        if(xvalue%2==0){
            xvalue=xvalue*(-1);
        }
        //float yvalue=-3;
        Random y = new Random();
        int yvalue=y.nextInt(20);
        if(yvalue%2==0){
            yvalue=yvalue*(-1);
        }
        // initializing our scatter chart.
        chart = findViewById(R.id.chart1);

        // below line is use to disable the description
        // of our scatter chart.
        chart.getDescription().setEnabled(false);

        // below line is use to draw grid background
        // and we are setting it to false.
        chart.setDrawGridBackground(false);

        // below line is use to set touch
        // enable for our chart.
        chart.setTouchEnabled(false);

        // below line is use to set maximum
        // highlight distance for our chart.
        chart.setMaxHighlightDistance(50f);

        // below line is use to set
        // dragging for our chart.
        chart.setDragEnabled(false);

        // below line is use to set scale
        // to our chart.
        chart.setScaleEnabled(false);

        // below line is use to set maximum
        // visible count to our chart.
        chart.setMaxVisibleValueCount(200);

        // below line is use to set
        // pinch zoom to our chart.
        chart.setPinchZoom(false);

        // below line we are getting
        // the legend of our chart.
     //   Legend l = chart.getLegend();

        // after getting our chart
        // we are setting our chart for vertical and horizontal
        // alignment to top, right and vertical.
      //  l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //l.setOrientation(Legend.LegendOrientation.VERTICAL);

        // below line is use for
        // setting draw inside to false.
        //l.setDrawInside(false);

        // below line is use to set
        // offset value for our legend.
     //   l.setXOffset(5f);

        // below line is use to get
        // y-axis of our chart.
        YAxis yl = chart.getAxisLeft();

        // below line is use to set
        // minimum axis to our y axis.
        yl.setAxisMinimum(-20f);
        yl.setAxisMaximum(20f);

        // below line is use to get axis
        // right of our chart
        chart.getAxisRight().setEnabled(false);
        //chart.getAxis().setEnabled(true);

        // below line is use to get
        // x axis of our chart.
        XAxis xl = chart.getXAxis();

        // below line is use to enable
        // drawing of grid lines.
        xl.setDrawGridLines(true);

        // below line is use to set
        // minimum axis to our x axis.
        xl.setAxisMinimum(-20f);
        xl.setAxisMaximum(20f);

        // in below line we are creating an array list
        // for each entry of our chart.
        // we will be representing three values in our charts.
        // below is the line where we are creating three
        // lines for our chart.
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        ArrayList<Entry> values3 = new ArrayList<>();

        // on below line we are adding data to our charts.
        //for (int i = 0; i < 11; i++) {
           // values1.add(new Entry(i, (i * 5)));
        //}

        values1.add(new Entry(xvalue,yvalue));

        // on below line we are adding
        // data to our value 2
        //for (int i = 11; i < 21; i++) {
          //  values2.add(new Entry(i, (i * 3)));
        //}

        // on below line we are adding
        // data to our 3rd value.
        //for (int i = 21; i < 31; i++) {
          //  values3.add(new Entry(i, (i * 4)));
        //}

        // create a data set and give it a type
        ScatterDataSet set1 = new ScatterDataSet(values1, "Your mood");

        // below line is use to set shape for our point on our graph.
        set1.setScatterShape(ScatterChart.ScatterShape.SQUARE);

        // below line is for setting color to our shape.
        set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);

        // below line is use to create a new point for our scattered chart.
        ScatterDataSet set2 = new ScatterDataSet(values2, "Point 2");

        // for this point we are setting our shape to circle
        set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);

        // below line is for setting color to our point in chart.
        set2.setScatterShapeHoleColor(ColorTemplate.COLORFUL_COLORS[3]);

        // below line is use to set hole
        // radius to our point in chart.
        set2.setScatterShapeHoleRadius(3f);

        // below line is use to set color to our set.
        set2.setColor(ColorTemplate.COLORFUL_COLORS[1]);



        // below line is use to set shape size
        // for our data set of the chart.
        set1.setScatterShapeSize(32f);
        set2.setScatterShapeSize(8f);

        // in below line we are creating a new array list for our data set.
        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();

        // in below line we are adding all
        // data sets to above array list.
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);

        // create a data object with the data sets
        ScatterData data = new ScatterData(dataSets);

        // below line is use to set data to our chart
        chart.setData(data);

        // at last we are calling
        // invalidate method on our chart.
        chart.invalidate();

        TextView subtitle = findViewById(R.id.sub);
        subtitle.setTextSize(20);
        subtitle.setText("Description:"); //set text for text view

        TextView description = findViewById(R.id.d);
        if(xvalue>0 && yvalue>0)
        {
            description.setText("1"); //set text for text view
        } else if (xvalue<0 && yvalue>0) {
            description.setText("2"); //set text for text view
        }else if (xvalue<0 && yvalue<0) {
            description.setText("3"); //set text for text view
        }else if (xvalue>0 && yvalue<0) {
            description.setText("4"); //set text for text view
        }else{
            description.setText("0"); //set text for text view
        }

    }
}