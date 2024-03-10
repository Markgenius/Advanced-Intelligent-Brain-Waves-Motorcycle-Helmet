package com.example.tryingline3;

import android. content. Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
//import com.liang.batterytestsystem.R;
//import com.liang.batterytestsystem.utils.LColor;
//import com.liang.liangutils.utils.LLogX;
//import com.liang.liangutils.view.LTitleView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartDemo extends AppCompatActivity implements View.OnClickListener {


    public static final int MSG_START = 1; // handler message, start adding points

    // polyline number
    public static final int LINE_NUMBER_1 = 0;
    public static final int LINE_NUMBER_2 = 1;
    public static final int LINE_NUMBER_3 = 2;

    /**
     * Function: start mode
     */
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, LineChartDemo.class));
    }

    private DemoHandler mDemoHandler; // Custom Handler
    private Random mRandom = new Random(); // randomly generate points
    private DecimalFormat mDecimalFormat = new DecimalFormat("#.00"); // Format floating-point numbers with two decimal places


    Button mBtnStart; // start adding points
    Button mBtnPause; // pause adding points
    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;
    List<CheckBox> mCheckBoxList = new ArrayList<>();

    LineChart mLineChart; // broken line table, collection of saved lines
    LineData mLineData; // Line collection, all discounts are stored in this collection in the form of an array
    XAxis mXAxis; //X axis
    YAxis mLeftYAxis; //Left Y axis
    YAxis mRightYAxis; //Right Y axis
    Legend mLegend; //Legend
    LimitLine mLimitline; //limit line

    // Y value data linked list
    List<Float> mList1 = new ArrayList<>();
    List<Float> mList2 = new ArrayList<>();
    List<Float> mList3 = new ArrayList<>();

    // Point data list required by Chart
    List<Entry> mEntries1 = new ArrayList<>();
    List<Entry> mEntries2 = new ArrayList<>();
    List<Entry> mEntries3 = new ArrayList<>();

    // LineDataSet: point collection, that is, a line
    LineDataSet mLineDataSet1 = new LineDataSet(mEntries1, "Polyline 1");
    LineDataSet mLineDataSet2 = new LineDataSet(mEntries2, "Polyline 2");
    LineDataSet mLineDataSet3 = new LineDataSet(mEntries3, "Polyline 3");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_line_chart);

        mDemoHandler = new DemoHandler(this);
        initView();
        initLineChart();
    }


    /**
     * Function: Generate random numbers (two decimal places)
     */
    public Float getRandom(Float seed) {
        return Float. valueOf(mDecimalFormat. format(mRandom. nextFloat() * seed));
    }

    /**
     * Function: Initialize basic controls, button, checkbox
     */
    public void initView() {
        mBtnStart = findViewById(R.id.demo_start);
        mBtnPause = findViewById(R.id.demo_pause);
        mCheckBox1 = findViewById(R.id.demo_checkbox1);
        mCheckBox2 = findViewById(R.id.demo_checkbox2);
        mCheckBox3 = findViewById(R.id.demo_checkbox3);
        mCheckBoxList.add(mCheckBox1);
        mCheckBoxList.add(mCheckBox2);
        mCheckBoxList.add(mCheckBox3);

        mBtnStart.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mCheckBox1.setOnClickListener(this);
        mCheckBox2.setOnClickListener(this);
        mCheckBox3.setOnClickListener(this);

    }

    /**
     * Function: Initialize LineChart
     */
    public void initLineChart() {
        mLineChart = findViewById(R.id.demo_linechart);
        mXAxis = mLineChart.getXAxis(); // Get the x-axis
        mLeftYAxis = mLineChart.getAxisLeft(); // Get the side Y axis
        mRightYAxis = mLineChart.getAxisRight(); // get the right Y axis
        mLegend = mLineChart.getLegend(); // get the legend
        mLineData = new LineData();
        mLineChart.setData(mLineData);

        // Set the basic properties of the icon
        setChartBasicAttr(mLineChart);

        // set the XY axis
        setXYAxis(mLineChart, mXAxis, mLeftYAxis, mRightYAxis);

        // add lines
        initLine();

        // set the legend
        createLegend(mLegend);

        // set MarkerView
        setMarkerView(mLineChart);
    }


    /**
     * Function: Set the basic properties of the icon
     */
    void setChartBasicAttr(LineChart lineChart) {
        /*Chart settings***/
        lineChart.setDrawGridBackground(false); //Whether to display grid lines
        lineChart.setDrawBorders(true); //Whether to display borders
        lineChart.setDragEnabled(true); //Whether it can be dragged
        lineChart.setScaleEnabled(true); // Can zoom
        lineChart.setTouchEnabled(true); //Whether there is a touch event
        //Set the XY axis animation effect
        //lineChart.animateY(2500);
        lineChart.animateX(1500);
    }

    /**
     * Function: set XY axis
     */
    void setXYAxis(LineChart lineChart, XAxis xAxis, YAxis leftYAxis, YAxis rightYAxis) {
        /***XY axis settings***/
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //X-axis setting display position is at the bottom
        xAxis.setAxisMinimum(0f); // Set the minimum value of the X axis
        xAxis.setAxisMaximum(20); // Set the maximum value of the X axis
        xAxis.setLabelCount(20, false); // Set the number of scales on the X-axis, the second parameter indicates whether to distribute evenly
        xAxis.setGranularity(1f); // Set the minimum interval between X-axis coordinates
        lineChart.setVisibleXRangeMaximum(5);//The maximum amount displayed on the x-axis coordinate line in the current statistical chart
        //Ensure that the Y axis starts from 0, otherwise it will move up a little
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(100f);
        rightYAxis.setAxisMaximum(100f);
        leftYAxis. setGranularity(1f);
        rightYAxis. setGranularity(1f);
        leftYAxis.setLabelCount(20);
        lineChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.LEFT);//The maximum amount displayed on the Y-axis coordinate line in the current statistical chart
        lineChart.setVisibleYRangeMaximum(30, YAxis.AxisDependency.RIGHT);//The maximum amount displayed on the Y-axis coordinate line in the current statistical chart
        leftYAxis. setEnabled(false);

// leftYAxis.setCenterAxisLabels(true);// Center the axis labels
// leftYAxis.setDrawZeroLine(true); // draw a line at the origin
// leftYAxis.setZeroLineColor(Color.RED);
// leftYAxis.setZeroLineWidth(1f);
    }

    /**
     * Function: Initialize the curves in the chart, add three curves, and display the first curve by default
     */
    void initLine() {

        createLine(mList1, mEntries1, mLineDataSet1, LColor.Colors.RED.getColor(), mLineData, mLineChart);
        createLine(mList2, mEntries2, mLineDataSet2, LColor.Colors.ORANGE.getColor(), mLineData, mLineChart);
        createLine(mList3, mEntries3, mLineDataSet3, LColor.Colors.YELLOW.getColor(), mLineData, mLineChart);


        // mLineData.getDataSetCount() Number of buses
        // mLineData.getEntryCount() total points
        // mLineData.getDataSetByIndex(index).getEntryCount() The total number of points of the polyline at the index index
        // After each curve is added to mLineData, it will be arranged from index 0
        for (int i = 0; i < mLineData.getDataSetCount(); i++) {
            mLineChart.getLineData().getDataSets().get(i).setVisible(false); //
        }
        showLine(LINE_NUMBER_1);
    }

    /**
     * Function: Show or hide the specified line according to the index
     */
    public void showLine(int index) {
        mLineChart
                .getLineData()
                .getDataSets()
                .get(index)
                .setVisible(mCheckBoxList.get(index).isChecked());
        mLineChart. invalidate();
    }

    /**
     * Function: dynamically create a curve
     */
    private void createLine(List<Float> dataList, List<Entry> entries, LineDataSet lineDataSet, int color, LineData lineData, LineChart lineChart) {
        for (int i = 0; i < dataList. size(); i++) {
            /**
             * You can view the Entry construction method here, and you can find that the value Entry(float x, float y) can be passed in
             * Drawable can also be passed in, Entry(float x, float y, Drawable icon) can set Drawable image display at the intersection of XY axis
             */
            Entry entry = new Entry(i, dataList.get(i));// Entry(x,y)
            entries. add(entry);
        }

        // Initialize the lines
        initLineDataSet(lineDataSet, color, LineDataSet. Mode. CUBIC_BEZIER);

        if (lineData == null) {
            lineData = new LineData();
            lineData. addDataSet(lineDataSet);
            lineChart.setData(lineData);
        } else {
            lineChart.getLineData().addDataSet(lineDataSet);
        }

        lineChart. invalidate();
    }


    /**
     * Curve initialization settings, a LineDataSet represents a curve
     *
     * @param lineDataSet line
     * @param color line color
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet. Mode mode) {
        lineDataSet.setColor(color); // Set the color of the curve
        lineDataSet.setCircleColor(color); // Set the color of the data point circle
        lineDataSet.setDrawCircleHole(false);// Set whether the circle point of the curve value is hollow
        lineDataSet.setLineWidth(1f); // Set the polyline width
        lineDataSet.setCircleRadius(3f); // Set the circle radius of the discount point
        lineDataSet.setValueTextSize(10f);

        lineDataSet.setDrawFilled(true); //Set line chart filling
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //Set the curve to be displayed as a smooth curve (if not set, it will default to a broken line)
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet. setMode(mode);
        }

    }


    /**
     * Function: create a legend
     */
    private void createLegend(Legend legend) {
        /***Print line legend label setting***/
        //Set the display type, LINE CIRCLE SQUARE EMPTY and many other ways, just check LegendForm
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend. setTextSize(12f);
        // display position lower left
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //Whether to draw in the chart
        legend. setDrawInside(false);
        legend. setEnabled(true);
    }


    /**
     * Set up a MarkerView that can display custom values ​​for the XY axis
     */
    public void setMarkerView(LineChart lineChart) {
        LineChartMarkViewDemo mv = new LineChartMarkViewDemo(this);
        mv.setChartView(lineChart);
        lineChart. setMarker(mv);
        lineChart. invalidate();
    }


    /**
     * Dynamically add data
     * The polylines stored in a LineChart are actually indexed from 0
     *
     * @param yValues ​​y value
     */
    public void addEntry(LineData lineData, LineChart lineChart, float yValues, int index) {

        // Get a polyline by index, and then get the number of current points on the polyline
        int xCount = lineData.getDataSetByIndex(index).getEntryCount();


        Entry entry = new Entry(xCount, yValues); // create a point
        lineData.addEntry(entry, index); // Add entry to the polyline at the specified index

        // notify data has changed
        lineData. notifyDataChanged();
        lineChart. notifyDataSetChanged();

        //Move yValues ​​to the position of the specified index
        lineChart.moveViewToAnimated(xCount - 4, yValues, YAxis.AxisDependency.LEFT, 1000);// TODO: 2019/5/4 memory leak, asynchronous to be fixed
        lineChart. invalidate();
    }


    /**
     * Function: add a point to the first polyline
     */
    public void addLine1Data(float yValues) {
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_1);
    }

    /**
     * Function: add a point to the second polyline
     */
    public void addLine2Data(float yValues) {
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_2);
    }

    /**
     * Function: add a point to the third polyline
     */
    public void addLine3Data(float yValues) {
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_3);
    }

    /**
     * Function: send start
     */
    void sendStartAddEntry() {
        if (!mDemoHandler.hasMessages(MSG_START)) { // Determine whether there is a message in the message queue, if not, send it
            mDemoHandler.sendEmptyMessageDelayed(MSG_START, 1000);
        }
    }

    /**
     * Function: Pause adding points, that is, remove all messages
     */
    void sendPauseAddEntry() {
        mDemoHandler. removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super. onDestroy();
        // clear message
        mDemoHandler. removeCallbacksAndMessages(null);
        mDemoHandler = null;

        // moveViewToAnimated moves to a certain point, there is a memory leak, it has not been fixed yet, I hope netizens can point to it
        mLineChart. clearAllViewportJobs();
        mLineChart. removeAllViewsInLayout();
        mLineChart. removeAllViews();
    }



    @Override
    public void onClick(View view) {
        switch (view. getId()) {
            case R.id.demo_start:
                sendStartAddEntry();
                break;
            case R.id.demo_pause:
                sendPauseAddEntry();
                break;
            case R.id.demo_checkbox1:
                showLine(LINE_NUMBER_1);
                break;
            case R.id.demo_checkbox2:
                showLine(LINE_NUMBER_2);
                break;
            case R.id.demo_checkbox3:
                showLine(LINE_NUMBER_3);
                break;
            default:
        }
    }



    /**
     * Function: Customize Handler to prevent memory leaks through weak references
     */
    private static class DemoHandler extends Handler {

        WeakReference<LineChartDemo> mReference;

        DemoHandler(LineChartDemo activity) {
            mReference = new WeakReference<>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            super. handleMessage(msg);
            LineChartDemo lineChartDemo = mReference. get();
            if (lineChartDemo == null) {
                return;
            }
            switch (msg. what) {
                case MSG_START:
                    lineChartDemo.addLine1Data(lineChartDemo.getRandom(30f));
                    lineChartDemo.addLine2Data(lineChartDemo.getRandom(20f));
                    lineChartDemo.addLine3Data(lineChartDemo.getRandom(10f));
                    lineChartDemo.sendStartAddEntry();
                    break;
                default:
            }
        }
    }
}