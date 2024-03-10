package com.example.tryingline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Context; 
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
//import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MSG_START = 1;

    public static final int LINE_NUMBER_1 = 0;
    public static final int LINE_NUMBER_2 = 1;
    public static final int LINE_NUMBER_3 = 2;


    public static void startActivity (Context context) {
        context.startActivity (new Intent(context, LineChartDemo.class));
    }   

    private DemoHandler mDemoHandler; 
    private Random mRandom = new Random(); 
    private DecimalFormat mDecimalFormat = new DecimalFormat("#.00");

    Button mBtnStart; 
    Button mBtnPause;
    CheckBox mCheckBox1;
    CheckBox mCheckBox2;
    CheckBox mCheckBox3;
    List<CheckBox> mCheckBoxList = new ArrayList<>();

    LineChart mLineChart; 
    LineData mLineData;
    XAxis mXAxis; 
    YAxis mLeftYAxis;  
    YAxis mRightYAxis; 
    Legend mLegend; 
    LimitLine mlimitline;

    List<Float> mList1 = new ArrayList<>();
    List<Float> mList2 = new ArrayList<>();
    List<Float> mList3 = new ArrayList<>();

    List<Entry> mEntries1 = new ArrayList<>();
    List<Entry> mEntries2 = new ArrayList<>();
    List<Entry> mEntries3 = new ArrayList<>();

    LineDataSet mLineDataSet1 = new LineDataSet(mEntries, "Line 1");
    LineDataSet mLineDataSet2 = new LineDataSet(mEntries, "Line 2");
    LineDataSet mLineDataSet3 = new LineDataSet(mEntries, "Line 3");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.demo_activity_line_chart);

        mDemoHandler = new DemoHandler(this); 
        initView();
        initLineChart();
    }

    public Float getRandom(Float seed){
        return Float.valueOf(mDecimalFormat.format(mRandom.nextFloat * seed));
    }
        
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

    public void initLineChart() { 
        mLineChart = findViewById(R.id.demo_linechart); 
        XAxis = mLineChart.getXAxis();
        mLeftYAxis = mLineChart.getAxisLeft(); 
        mRightYAxis = mLineChart.getAxisRight(); 
        mLegend = mLineChart.getLegend(); 
        mLineData = new LineData(); 
        mLineChart.setData(mLineData);

        setChartBasicAttr(mLineChart);
        setXYAxis(mLineChart, mXAxis, mLeftYAxis, mRightYAxis);
        initLine();
        createLegend (mLegend);
        setMarkerView(mLineChart);
    }
    
    void setChartBasicAttr(LineChart lineChart) {
        lineChart.setDrawGridBackground (false); 
        lineChart.setDrawBorders (true); 
        lineChart.setDragEnabled(true); 
        lineChart.setScaleEnabled(true); 
        lineChart.setTouchEnabled(true);
        
        lineChart.animateX(1500);
        lineChart.animateY(2500);
    }

    void setXYAxis (LineChart lineChart, XAxis xAxis, YAxis leftYAxis, YAxis rightYAxis) {
        xAxis.setPosition (XAxis. XAxisPosition. BOTTOM);
        xAxis.setAxisMinimum (0f);
        xAxis.setAxisMaximum (20);
        xAxis.setLabelCount (20, false);
        xAxis.setGranularity (1f);
        lineChart.setVisibleXRangeMaximum (5);
        leftYAxis.setAxisMinimum (0f);
        rightYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum (100f);
        rightYAxis.setAxisMaximum (100f);
        leftYAxis.setGranularity (1f);
        rightYAxis.setGranularity (1f);
        leftYAxis.setLabelCount(20);
        lineChart.setVisibleYRangeMaximum (30, YAxis.AxisDependency.LEFT);
        lineChart.setVisibleYRangeMaximum (30, YAxis.AxisDependency.RIGHT); 
        leftYAxis.setEnabled(false);
    }
    
    void initLine() {

        createLine(mList1, mEntries1, mLineDataSet1, LColor.Colors.RED.getColor(), mLineData, mLineChart); 
        createLine(mList2, mEntries2, mLineDataSet2, LColor.Colors.ORANGE.getColor(), mLineData, mLineChart); 
        createLine(mList3, mEntries3, mLineDataSet3, LColor.Colors.YELLOW.getColor(), mLineData, mLineChart);
        
        for (int i = 0; i < mLineData.getDataSetCount(); i++) {
            mLineChart.getLineData().getDataSets().get(i).setVisible(false);
        }
        showLine(LINE_NUMBER_1);
    }

    public void showLine(int index) { 
        mLineChart
                .getLineData()
                .getDataSets()
                .get(index)
                .setVisible(mCheckBoxList.get(index).isChecked());
        mLineChart.invalidate();
    }

    private void createLine(List<Float> dataList, list<Entry> entries, LineDataSet linedataSet, int color, LineData lineData, lineChart lineChart){
        for (int i = 0; i < dataList.size(); i++){
            Entry entry = new Entry(i, dataList.get(i));
            entries.add(entry);
        }

        initLineDataSet(lineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);

        if(lineData == null){
            lineData = new LineData();
            lineData.addDataSet(lineDataSet);
            lineChart.setData(lineData);
        }else{
            lineChart.getLineData().addDataSet(lineDataSet);
        }
        lineChart.invalidate();
    }
    
    private void initLineDataSet (LineDataSet lineDataSet, int color, LineDataSet. Mode mode){
        lineDataSet.setColor (color);
        lineDataSet.setCircleColor(color); 
        lineDataSet.setDrawCircleHole(false); 
        lineDataSet.setLineWidth (1f); 
        lineDataSet.setCircleRadius (3f); 
        lineDataSet.setValueTextSize(10f); 
        lineDataSet.setDrawFilled(true); 
        lineDataSet.setFormLineWidth (1f); 
        lineDataSet. setFormSize(15.f);
        if (mode == null) { 
             lineDataSet.setMode (LineDataSet.Mode.CUBIC_BEZIER); 
        } else { 
             lineDataSet.setMode (mode);
        }
    }

    public void addEntry(LineData lineData, LineChart lineChart, float yValues, int index) { 
        int xCount = lineData.getDataSetByIndex (index).getEntryCount(); 
    
        Entry entry = new Entry(xCount, yValues); 
        lineData.addEntry (entry, index); 
        lineData.notifyDataChanged(); 
        lineChart.notifyDataSetChanged();
        lineChart.moveViewToAnimated (xCount - 4, yValues, YAxis.AxisDependency. LEFT, 1000); 
        lineChart.invalidate();
    
    }

    public void addLine1Data(float yValues){
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_1);
    }
    public void addLine2Data(float yValues){
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_2);
    }
    public void addLine3Data(float yValues){
        addEntry(mLineData, mLineChart, yValues, LINE_NUMBER_3);
    }

    void sendStartAddEntry(){
        if(!mDemoHandler.hasMessages(MSG_START)){
            mDemoHandler.sendEmptyMessageDelayed(MSG_START, 1000);
        }
    }

    void sendPauseAddEntry() { 
        mDemoHandler.removeCallbacksAndMessages (null); 
    } 

    @Override 
    protected void onDestroy() { 
        super.onDestroy();
        mDemoHandler.removeCallbacksAndMessages (null); 
        mDemoHandler = null; 
        mLineChart.clearAllViewportJobs (); 
        mLineChart.removeAllViews InLayout(); 
        mLineChart.removeAllViews();
    }

    @Override 
    public void onClick(View view) { 
	    switch (view.getId()) { 
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
    
    @Override 
    public void handleMessage (Message msg) { 
	    super.handleMessage (msg); 
	    LineChartDemo lineChartDemo = mReference.get(); 
	    if (lineChartDemo == null) { 
		    return; 
	    } 
	    switch (msg.what) { 
		    case MSG_START: 
	 		    lineChartDemo.addLine1Data(lineChartDemo.getRandom (30f)); 
	 		    lineChartDemo.addLine2Data (lineChartDemo.getRandom (20f)); 
	 		    lineChartDemo.addLine3Data (lineChartDemo.getRandom (10f)); 
	 		    lineChartDemo. sendStartAddEntry(); 
			    break; 
		    default:
	    }
    }
        
        
}