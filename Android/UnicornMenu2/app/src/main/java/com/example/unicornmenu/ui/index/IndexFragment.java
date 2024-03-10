package com.example.unicornmenu.ui.index;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.unicornmenu.R;
import com.example.unicornmenu.databinding.FragmentIndexBinding;
import com.example.unicornmenu.ui.index.CircleDisplay.SelectionListener;

import java.util.Arrays;

public class IndexFragment extends Fragment implements SelectionListener{
    float[] GetDataCat = new float[41];
    private FragmentIndexBinding binding;
    private CircleDisplay mCircleDisplay;
    private Button _btnConnect = null;
    boolean buttonState = false;

    int counter;
    float[][] calibration = new float[8][2];
    float[][] calibrationDelta = new float[8][10];
    int DeltaCounter = 300;     //started calibration seconds
    float[] HighAverage = new float[8];
    float[] LowAverage = new float[8];
    float[] diffAverage = new float[8];

    float[] calibrationsend = new float[17];

    boolean getCali = false;
    float[] GetDataCali = new float[17];


    Handler handler;
    String discText;
    String discText2;
    TextView description;
    TextView description2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding= FragmentIndexBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mCircleDisplay = root.findViewById(R.id.circleDisplay);

        mCircleDisplay.setAnimDuration(0);
        mCircleDisplay.setValueWidthPercent(55f);
        mCircleDisplay.setFormatDigits(1);
        mCircleDisplay.setDimAlpha(80);
        //mCircleDisplay.setSelectionListener(this);
        mCircleDisplay.setTouchEnabled(false);//change
        mCircleDisplay.setUnit("S");
        mCircleDisplay.setStepSize(0.5f);
        mCircleDisplay.setDrawText(true);
        mCircleDisplay.setColor(0xff3399);
        _btnConnect = root.findViewById(R.id.btnConnect);
        _btnConnect.setOnClickListener(this::onClick);
//        mCircleDisplay.showValue(30f, 100f, true);
//        mCircleDisplay.showValue((float) (Math. random()) * 80, 100f, true);
        _btnConnect.setText("Start Calibration");
        description = root.findViewById(R.id.d);
        description.setText("1");
        description2 = root.findViewById(R.id.d2);
        description2.setText("2");


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
//                                System.out.print(GetDataCali[0]);
//                                System.out.println(GetDataCali[8]);
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
                    counter ++;
                    if(!buttonState){
                        mCircleDisplay.showValue(0, 90f, false);
                        DeltaCounter = 300;
                        counter = 0;
                    }
                    else{
                        mCircleDisplay.showValue(counter/10, 90f, false);
                    }

                    if(counter > 900) {
                        buttonState = false;
                        calibrationsend[16] = 999;    //config index
                        for(int i = 0; i < 8; i++) {
                            calibration[i][0] /= 600;
                            calibration[i][1] /= 600;
                        }


//                        System.out.print("cal.theta");
//                        System.out.println(calibration[0]);
//                        System.out.print("cal.beta");
//                        System.out.println(calibration[1]);

                    }
                    else if (counter > 300) {


                        for(int i = 0; i < 8; i++){
                            calibrationDelta[i][counter - DeltaCounter-1] = GetDataCat[i * 5 + 0];
                            //System.out.println(counter - DeltaCounter-1);
                            //calibration[0][0] += GetDataCat[i*5+0];
                            calibration[i][0] += diffAverage[i];
                            calibration[i][1] += GetDataCat[i*5+3];
                        }

                        if(counter - DeltaCounter > 9){
                            DeltaCounter = counter;
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
                            }

                            //System.out.println(diffAverage[0]);

                        }
                    }


                    for(int i = 0; i < 16; i++){
                        if(i < 8) calibrationsend[i] = calibration[i][0];
                        else calibrationsend[i] = calibration[i-8][1];
                    }

                    if(binding != null){
                        if(!getCali && calibrationsend[16] == 999) {
                            Bundle resultSendCali = new Bundle();
                            resultSendCali.putFloatArray("df2", calibrationsend);
                            getParentFragmentManager().setFragmentResult("dataFromBrainCali", resultSendCali);
                        }
                        else {
                            Bundle resultSendCali = new Bundle();
                            resultSendCali.putFloatArray("df2", GetDataCali);
                            getParentFragmentManager().setFragmentResult("dataFromBrainCali", resultSendCali);
                        }
                    }

                    discText = Float.toString(GetDataCali[0])+"  "+Float.toString(GetDataCali[1])
                            +"  "+Float.toString(GetDataCali[2])+"  "+Float.toString(GetDataCali[3])
                            +"  "+Float.toString(GetDataCali[4])+"  "+Float.toString(GetDataCali[5])
                            +"  "+Float.toString(GetDataCali[6])+"  "+Float.toString(GetDataCali[7]);
                    discText2 = Float.toString(GetDataCali[8])+"  "+Float.toString(GetDataCali[9])
                            +"  "+Float.toString(GetDataCali[10])+"  "+Float.toString(GetDataCali[11])
                            +"  "+Float.toString(GetDataCali[12])+"  "+Float.toString(GetDataCali[13])
                            +"  "+Float.toString(GetDataCali[14])+"  "+Float.toString(GetDataCali[15]);
                    handler.sendEmptyMessage(0);



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
            case R.id.btnConnect:
            {
                buttonState = !buttonState;
                getCali = false;
                if(!buttonState) {
                    _btnConnect.setText("Start Calibration");

                }
                else {
                    _btnConnect.setText("Cancel");
                    for(int i = 0; i < 8; i++) {
                        calibration[i][0] = 0;
                        calibration[i][1] = 0;
                    }
                    for(int i = 0; i < 16; i++){
                        calibrationsend[i] = 0;
                    }

                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSelectionUpdate(float val, float maxval) {
        Log.i("Main", "Selection update: " + val + ", max: " + maxval);
    }

    @Override
    public void onValueSelected(float val, float maxval) {
        Log.i("Main", "Selection complete: " + val + ", max: " + maxval);
    }
}
