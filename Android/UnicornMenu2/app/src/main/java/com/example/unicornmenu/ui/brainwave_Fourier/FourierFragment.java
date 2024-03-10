package com.example.unicornmenu.ui.brainwave_Fourier;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.unicornmenu.R;
import com.example.unicornmenu.databinding.FragmentFourierBinding;
import com.example.unicornmenu.ui.brainwave.FFT;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unicornmenu.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;

public class FourierFragment extends Fragment{
    float[] GetData = new float[18];

    int NumOfLineChart = 8;
    int dataRange = 128;
    LineDataSet[] lineDataSet = new LineDataSet[NumOfLineChart];
    LineChart[] mLineChart = new LineChart[NumOfLineChart];
    LineData[] lineData = new LineData[NumOfLineChart];
    int bufferCounter = 0;
    int lineCounter = 0;
    Entry[] entry = new Entry[NumOfLineChart]; // create a point
    int buttonAddCounter = 0;

    boolean getCali = false;
    float[] GetDataCali = new float[17];

    private FragmentFourierBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FourierViewModel fourierViewModel = new ViewModelProvider(this).get(FourierViewModel.class);
        binding = FragmentFourierBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mLineChart[0] = (LineChart) root.findViewById(R.id.linechart1);
        mLineChart[1] = (LineChart) root.findViewById(R.id.linechart2);
        mLineChart[2] = (LineChart) root.findViewById(R.id.linechart3);
        mLineChart[3] = (LineChart) root.findViewById(R.id.linechart4);
        mLineChart[4] = (LineChart) root.findViewById(R.id.linechart5);
        mLineChart[5] = (LineChart) root.findViewById(R.id.linechart6);
        mLineChart[6] = (LineChart) root.findViewById(R.id.linechart7);
        mLineChart[7] = (LineChart) root.findViewById(R.id.linechart8);
        for(int i = 0; i < NumOfLineChart; i++){
            mLineChart[i].setDrawBorders(false); // show border
            mLineChart[i].getAxisLeft().setDrawLabels(true);
            mLineChart[i].getAxisRight().setDrawLabels(false);
            mLineChart[i].setDrawGridBackground(false);
            mLineChart[i].setSelected(false);
            mLineChart[i].setPinchZoom(false);
            if(i != 0) mLineChart[i].getXAxis().setDrawLabels(false);
            mLineChart[i].getLegend().setEnabled(false);
            mLineChart[i].setMaxVisibleValueCount(0);
            mLineChart[i].getDescription().setText("Brain"+String.valueOf(i+1));

            lineData[i] = new LineData();
            lineDataSet[i] = new LineDataSet(null, "Brain"+String.valueOf(i+1));
            lineDataSet[i].setDrawCircles(false);
            lineDataSet[i].setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet[i].setColor(Color.RED);
            lineData[i].addDataSet(lineDataSet[i]);
            mLineChart[i].setData(lineData[i]);
        }


        Thread t = new Thread(runnable);
        t.start();

        getParentFragmentManager().setFragmentResultListener("dataFromBrain", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if(!result.isEmpty() && result.size() == 1) {
                    GetData = result.getFloatArray("df1");
                }
                //result.clear();
            }
        }
        );

        getParentFragmentManager().setFragmentResultListener("dataFromBrainCali", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        if (!result.isEmpty() && !getCali) {
                            GetDataCali = result.getFloatArray("df2");
                            if(GetDataCali[16] == 999) getCali = true;
                            System.out.print("frag2 ");
                            System.out.print(GetDataCali[0]);
                            System.out.println(getCali);
                        }
                        //result.clear();
                    }
                }
        );

        return root;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            do{
                try {
                    if(binding != null && getCali){
                        Bundle resultSendCali = new Bundle();
                        resultSendCali.putFloatArray("df2", GetDataCali);
                        getParentFragmentManager().setFragmentResult("dataFromBrainCali", resultSendCali);
                    }

                    if (GetData[17] == 1) {
                        lineCounter++;
                        for (int i = 0; i < NumOfLineChart; i++) {
                            entry[i] = new Entry(lineCounter, GetData[i]);
                            lineData[i].addEntry(entry[i], 0); // Add entry to the polyline at the specified index
                            if (lineCounter > dataRange)
                                lineData[i].removeEntry(lineCounter - dataRange, 0);
                            mLineChart[i].setData(lineData[i]);
                            Thread.sleep(15);
                            showLine(0);
                        }



                    }
                    else Thread.sleep(15);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    };

    public void showLine(int index) {
        for(int i = 0; i < NumOfLineChart; i++){
            mLineChart[i]
                    .getLineData()
                    .getDataSets()
                    .get(index)
                    .setVisible(true);
            mLineChart[i].invalidate();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

