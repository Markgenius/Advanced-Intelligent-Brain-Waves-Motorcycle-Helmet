package com.example.unicornmenu.ui.meter;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.unicornmenu.R;
import com.example.unicornmenu.databinding.FragmentMeterBinding;
import com.example.unicornmenu.ui.index.CircleDisplay;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

import com.example.unicornmenu.ui.brainwave.BrainwaveFragment;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class MeterFragment extends Fragment implements View.OnClickListener{

    private BrainwaveFragment _brain = null;
    float[] GetDataCat = new float[8*5+1];
    float[] GetDataCali = new float[17];

    private ScatterChart chart;
    ScatterDataSet set1;
    ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
    ScatterData data;
    TextView description;
    TextView description2;

    Handler handler;
    String discText;
    String discText2;

    boolean getCali = false;
    float xvalue, yvalue;

    float[] gammaDelta = new float[8];
    float gammaDeltaMean = 0;

    int deltaCounter = 0;

    float[][] calibrationDelta = new float[8][10];
    float[] HighAverage = new float[8];
    float[] LowAverage = new float[8];
    float[] diffAverage = new float[8];

    ImageView red,yellow,green,layout,alco;
    MediaPlayer usandthem,teenspirit,radiogaga,loveofmylife,fixyou,everybreath,drunk,beyond,allstar;

    private Button _btnMusic = null;
    boolean buttonstate = false;

    float y_1;
    float y_2;
    int safetyIndex = 1;    //0:green  1:yellow  2:disaster  3:Drunk
    float[] orginCali = {60f, 50f};    //JJ:39.841 42.859  Mark:10.9022 17.5243
    float drunkDelta, drunkCounter;
    float drunkCaliAvg;
    boolean drunkBrain;

    private FragmentMeterBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MeterViewModel meterViewModel = new ViewModelProvider(this).get(MeterViewModel.class);
        binding = FragmentMeterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        red = root.findViewById(R.id.red);
        yellow = root.findViewById(R.id.yellow);
        green = root.findViewById(R.id.green);
        layout = root.findViewById(R.id.layout);
        alco = root.findViewById(R.id.alco);
        red.setTransitionVisibility(View.INVISIBLE);
        yellow.setTransitionVisibility(View.INVISIBLE);
        green.setTransitionVisibility(View.INVISIBLE);
        layout.setTransitionVisibility(View.INVISIBLE);
        alco.setTransitionVisibility(View.INVISIBLE);

        usandthem = MediaPlayer.create(root.getContext(), R.raw.usandthem);
        teenspirit = MediaPlayer.create(root.getContext(), R.raw.teenspirit);
        radiogaga = MediaPlayer.create(root.getContext(), R.raw.radiogaga);
        loveofmylife = MediaPlayer.create(root.getContext(), R.raw.loveofmylife);
        fixyou = MediaPlayer.create(root.getContext(), R.raw.fixyou);
        everybreath = MediaPlayer.create(root.getContext(), R.raw.everybreath);
        drunk = MediaPlayer.create(root.getContext(), R.raw.drunk);
        beyond = MediaPlayer.create(root.getContext(), R.raw.beyond);
        allstar = MediaPlayer.create(root.getContext(), R.raw.allstar);

        _btnMusic = root.findViewById(R.id.musicButton);
        _btnMusic.setOnClickListener(this::onClick);
        _btnMusic.setText("Fix You");

        chart = root.findViewById(R.id.chart1);
        description = root.findViewById(R.id.d);
        description.setText(" ");
        description2 = root.findViewById(R.id.d2);
        description2.setText(" ");

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false);
        chart.setMaxHighlightDistance(50f);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setMaxVisibleValueCount(200);
        chart.setPinchZoom(false);

        YAxis yl = chart.getAxisLeft();
        yl.setAxisMinimum(-4f);
        yl.setAxisMaximum(4f);
        chart.getAxisRight().setEnabled(false);

        XAxis xl = chart.getXAxis();
        xl.setDrawGridLines(true);
        xl.setAxisMinimum(-4f);
        xl.setAxisMaximum(4f);

        handler = new Handler(){
            @NonNull
            public void handleMessage(Message msg){
                description.setText(discText);
                description2.setText(discText2);
            }
        };



        Thread t = new Thread(runnable);
        t.start();

        getParentFragmentManager().setFragmentResultListener("dataFromBrainCate", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if(!result.isEmpty() && result.size() == 1) {
                    GetDataCat = result.getFloatArray("df1");
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
        //sound.start();
        //drunk.start();
        usandthem.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        teenspirit.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        radiogaga.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        loveofmylife.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        fixyou.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        everybreath.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        drunk.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        beyond.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        allstar.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });



        return root;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            do{
                try {
//                    Random x = new Random();
//                    xvalue=x.nextInt(9);
//                    if(xvalue%2==0){
//                        xvalue=xvalue*(-1);
//                    }
//
//                    Random y = new Random();
//                    yvalue=y.nextInt(9);
//                    if(yvalue%2==0){
//                        yvalue=yvalue*(-1);
//                    }

                    gammaDeltaMean = 0;
                    for(int i = 0; i < 8; i++){
                        gammaDelta[i] = GetDataCat[i*5+3] - GetDataCali[i+8];

                        gammaDeltaMean += GetDataCat[i*5+3] - GetDataCali[i+8];
                        if (i==8) gammaDeltaMean /= 8;
                    }
                    xvalue = (float)gammaDeltaMean/15/1.5f *-1;
                    if(xvalue > 4) xvalue = 4;
                    else if(xvalue < -4) xvalue = -4;

///////////////////////////////////////////////////////////////
                    deltaCounter ++;
                    for(int i = 0; i < 8; i++) {
                        calibrationDelta[i][deltaCounter-1] = GetDataCat[i * 5 + 0];
                    }

                    if(deltaCounter > 9){
                        deltaCounter = 0;
                        for(int x = 0; x < 8; x++) {
                            LowAverage[x] = 0;
                            HighAverage[x] = 0;
                            diffAverage[x] = 0;
                        }
                        for(int i = 0; i < 8; i++){
                            Arrays.sort(calibrationDelta[i]);
                        }
                        for(int x = 0; x < 8; x++){
                            for(int y = 0; y < 3; y++){
                                LowAverage[x] += calibrationDelta[x][y];
                                HighAverage[x] += calibrationDelta[x][9-y];
                            }
                            HighAverage[x] /= 3;
                            LowAverage[x] /= 3;
                            diffAverage[x] = HighAverage[x] - LowAverage[x];
                            yvalue = (float)((diffAverage[x] - GetDataCali[x])-4)/5;
                            if(yvalue > 4) yvalue = 4;
                            else if(yvalue < -4) yvalue = -4;
                        }

                    }
                    drunkDelta = 0;
                    drunkCaliAvg = 0;
                    for(int i = 0; i < 2; i++){
                        //orginCali[i] = GetDataCali[i+8];
                        drunkDelta += GetDataCat[i*5+3] - orginCali[i];
                        drunkCaliAvg += orginCali[i];
                    }
                    drunkDelta /= 2;
                    drunkCaliAvg /= 2;
                    if(drunkDelta >= Math.log(drunkCaliAvg)*13){
                        drunkCounter++;
                    }
                    else drunkCounter --;

                    drunkBrain = drunkCounter > 3;
                    if(drunkCounter < 0) drunkCounter = 0;


//                    Random x = new Random();
//                    xvalue=x.nextInt(15);
//                    Random y = new Random();
//                    yvalue=y.nextInt(5);
//
//                    Random x = new Random();
//                    xvalue=x.nextInt(12)-10;
//                    Random y = new Random();
//                    yvalue=y.nextInt(20)-10;
//
//                    Random x = new Random();
//                    xvalue=x.nextInt(5)-20;
//                    Random y = new Random();
//                    yvalue=y.nextInt(5)-20;



                    ArrayList<Entry> values1 = new ArrayList<>();
                    values1.add(new Entry(xvalue,yvalue));
                    set1 = new ScatterDataSet(values1, "Your mood");
                    set1.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
                    set1.setColor(ColorTemplate.COLORFUL_COLORS[0]);
                    set1.setScatterShapeSize(64f);
                    dataSets.add(set1); // add the data sets
                    data = new ScatterData(set1);
                    chart.setData(data);
                    chart.invalidate();
                    dataSets.remove(0);

                    y_1 = 4/3*(xvalue-1);
                    y_2 = 8/3*(xvalue+2);
                    if(Math.pow(yvalue,2) <= y_1) safetyIndex = 0;
                    else if(Math.pow(yvalue,2) > y_1 && Math.pow(yvalue,2) <= y_2) safetyIndex = 1;
                    else safetyIndex = 2;

                    if(drunkBrain) safetyIndex = 99999;

                    if(safetyIndex == 0) {
                        red.setTransitionVisibility(View.INVISIBLE);
                        yellow.setTransitionVisibility(View.INVISIBLE);
                        green.setTransitionVisibility(View.VISIBLE);
                        layout.setTransitionVisibility(View.VISIBLE);
                        alco.setTransitionVisibility(View.INVISIBLE);
                    }
                    else if(safetyIndex == 1){
                        red.setTransitionVisibility(View.INVISIBLE);
                        yellow.setTransitionVisibility(View.VISIBLE);
                        green.setTransitionVisibility(View.INVISIBLE);
                        layout.setTransitionVisibility(View.VISIBLE);
                        alco.setTransitionVisibility(View.INVISIBLE);
                    }
                    else if(safetyIndex == 2){
                        red.setTransitionVisibility(View.VISIBLE);
                        yellow.setTransitionVisibility(View.INVISIBLE);
                        green.setTransitionVisibility(View.INVISIBLE);
                        layout.setTransitionVisibility(View.VISIBLE);
                        alco.setTransitionVisibility(View.INVISIBLE);
                    }
                    else {
                        red.setTransitionVisibility(View.INVISIBLE);
                        yellow.setTransitionVisibility(View.INVISIBLE);
                        green.setTransitionVisibility(View.INVISIBLE);
                        layout.setTransitionVisibility(View.INVISIBLE);
                        alco.setTransitionVisibility(View.VISIBLE);
                    }


                    discText = Float.toString(xvalue);
                    discText2 = Float.toString(yvalue);
                    handler.sendEmptyMessage(0);

                    if(binding != null && getCali){
                        Bundle resultSendCali = new Bundle();
                        resultSendCali.putFloatArray("df2", GetDataCali);
                        getParentFragmentManager().setFragmentResult("dataFromBrainCali", resultSendCali);
                    }



                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.musicButton:
            {
                if(!buttonstate) {
                    drunk.start();
                    _btnMusic.setText("Fixing");
                }
                else{
                    drunk.pause();
                    drunk.seekTo(0);
                    _btnMusic.setText("Fix You");
                }
                buttonstate = !buttonstate;

                break;
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

