package com.example.unicornmenu.ui.brainwave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

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

import gtec.java.unicorn.Unicorn;

import com.example.unicornmenu.databinding.FragmentBrainwaveBinding;


public class BrainwaveFragment extends Fragment implements View.OnClickListener {
    int bufferCounter = 0;
    int fourierN = 128;
    double[][] dataToFourier = new double[8][fourierN];
    double[][] im = new double[8][fourierN];
    double[][] Mag = new double[8][fourierN];
    float[][] MagCate = new float[8][5];
    boolean btSetup = false;
    int NumOfLineChart = 14;
    int dataRange = 256;
    LineDataSet[] lineDataSet = new LineDataSet[NumOfLineChart];
    LineChart[] mLineChart = new LineChart[NumOfLineChart];
    LineData[] lineData = new LineData[NumOfLineChart];

    Entry[] entry = new Entry[NumOfLineChart]; // create a point
    int NumOfBarChart = 8;
    int[] lineCounter = new int[NumOfBarChart];

    BarChart[] barChart = new BarChart[NumOfBarChart];
    BarDataSet[] barDataSet = new BarDataSet[NumOfBarChart];
    BarData[] barData = new BarData[NumOfBarChart];
    BarEntry[][] barEntry = new BarEntry[NumOfBarChart][fourierN / 2 + 1];
    ArrayList<BarEntry> entries1 = new ArrayList<>();
    ArrayList<BarEntry> entries2 = new ArrayList<>();
    ArrayList<BarEntry> entries3 = new ArrayList<>();
    ArrayList<BarEntry> entries4 = new ArrayList<>();
    ArrayList<BarEntry> entries5 = new ArrayList<>();
    ArrayList<BarEntry> entries6 = new ArrayList<>();
    ArrayList<BarEntry> entries7 = new ArrayList<>();
    ArrayList<BarEntry> entries8 = new ArrayList<>();
//    BarChart barChart;
//    BarDataSet barDataSet;
//    BarData barData = new BarData();
//    ArrayList<BarEntry> entries = new ArrayList<>();


    private String _btnConStr = "Connect";
    private String _btnDisconStr = "Disconnect";
    private Button _btnConnect = null;
    private Spinner _spnDevices = null;
    private TextView _tvState = null;
    private Unicorn _unicorn = null;
    private Thread _receiver;
    private boolean _receiverRunning = false;
    private Context _context = null;
    private  int _cnt = 0;

    FFT fft;
    Bundle result = new Bundle();      //send raw data to other fragments
    Bundle result2 = new Bundle();
    float[] OutData = new float[17+1];
    float[] OutDataCate = new float[8*5+1];

    boolean magDone = false;

    private static final int PermissionRequestCode = 1;
    float[] GetDataCali = new float[17];
    boolean getCali = false;


    private FragmentBrainwaveBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrainwaveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bufferCounter = 0;
        for(int i = 0; i < NumOfBarChart; i++) lineCounter[i] = 0;
        fourierN = 128;
        getCali = false;


//        barChart = root.findViewById(R.id.chart_bar1);

        barChart[0] = root.findViewById(R.id.chart_bar1);
        barChart[1] = root.findViewById(R.id.chart_bar2);
        barChart[2] = root.findViewById(R.id.chart_bar3);
        barChart[3] = root.findViewById(R.id.chart_bar4);
        barChart[4] = root.findViewById(R.id.chart_bar5);
        barChart[5] = root.findViewById(R.id.chart_bar6);
        barChart[6] = root.findViewById(R.id.chart_bar7);
        barChart[7] = root.findViewById(R.id.chart_bar8);

        for(int i = 0; i < barData.length; i++){
            barData[i] = new BarData();
        }

        getParentFragmentManager().setFragmentResultListener("dataFromBrainCali", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        if (!result.isEmpty() && !getCali) {
                            GetDataCali = result.getFloatArray("df2");
                            if(GetDataCali[16] == 999) getCali = true;
                            System.out.print("frag1 ");
                            System.out.print(GetDataCali[0]);
                            System.out.println(getCali);
                        }
                        //result.clear();
                    }
                }
        );

//        mLineChart[0] = (LineChart) root.findViewById(R.id.linechart1);
//        mLineChart[1] = (LineChart) root.findViewById(R.id.linechart2);
//        mLineChart[2] = (LineChart) root.findViewById(R.id.linechart3);
//        mLineChart[3] = (LineChart) root.findViewById(R.id.linechart4);
//        mLineChart[4] = (LineChart) root.findViewById(R.id.linechart5);
//        mLineChart[5] = (LineChart) root.findViewById(R.id.linechart6);
//        mLineChart[6] = (LineChart) root.findViewById(R.id.linechart7);
//        mLineChart[7] = (LineChart) root.findViewById(R.id.linechart8);
//        mLineChart[8] = (LineChart) root.findViewById(R.id.linechart9);
//        mLineChart[9] = (LineChart) root.findViewById(R.id.linechart10);
//        mLineChart[10] = (LineChart) root.findViewById(R.id.linechart11);
//        mLineChart[11] = (LineChart) root.findViewById(R.id.linechart12);
//        mLineChart[12] = (LineChart) root.findViewById(R.id.linechart13);
//        mLineChart[13] = (LineChart) root.findViewById(R.id.linechart14);
//        for(int i = 0; i < NumOfLineChart; i++){
//            mLineChart[i].setDrawBorders(false); // show border
//            mLineChart[i].getAxisLeft().setDrawLabels(true);
//            mLineChart[i].getAxisRight().setDrawLabels(false);
//            mLineChart[i].setDrawGridBackground(false);
//            mLineChart[i].setSelected(false);
//            mLineChart[i].setPinchZoom(false);
//            if(i != 0) mLineChart[i].getXAxis().setDrawLabels(false);
//            mLineChart[i].getLegend().setEnabled(false);
//            mLineChart[i].setMaxVisibleValueCount(0);
//            mLineChart[i].getDescription().setText("Brain"+String.valueOf(i+1));
//
//            lineData[i] = new LineData();
//            lineDataSet[i] = new LineDataSet(null, "Brain"+String.valueOf(i+1));
//            lineDataSet[i].setDrawCircles(false);
//            lineDataSet[i].setMode(LineDataSet.Mode.CUBIC_BEZIER);
//            lineDataSet[i].setColor(Color.RED);
//            lineData[i].addDataSet(lineDataSet[i]);
//            mLineChart[i].setData(lineData[i]);
//        }


        try {
            _context = root.getContext();
            _spnDevices = root.findViewById(R.id.spnDevices);
            _spnDevices.setBackgroundColor(Color.BLACK);
            _btnConnect = root.findViewById(R.id.btnConnect);
            _tvState = root.findViewById(R.id.textView);
            if(!btSetup) _btnConnect.setText(_btnConStr);
            else _btnConnect.setText(_btnDisconStr);
            _btnConnect.setOnClickListener(this);
        } catch (Exception ex) {
            Toast.makeText(_context, String.format("Could not initialize UI elements. %s", ex.getMessage()), Toast.LENGTH_SHORT).show();
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_SCAN}, PermissionRequestCode);
            } else {
                GetAvailableDevices();
            }
        } catch (Exception ex) {
            Toast.makeText(_context, String.format("Could not acquire available devices. %s", ex.getMessage()), Toast.LENGTH_SHORT).show();
        }



        Thread t = new Thread(runnable);
        t.start();
        return root;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            do{
                try {


                    result.putFloatArray("df1", OutData);
                    getParentFragmentManager().setFragmentResult("dataFromBrain", result);

                    if(magDone) {
                        magDone = false;
                        result2.putFloatArray("df1", OutDataCate);
                        getParentFragmentManager().setFragmentResult("dataFromBrainCate", result2);
                    }

                    if(binding != null && getCali){
                        Bundle resultSendCali = new Bundle();
                        resultSendCali.putFloatArray("df2", GetDataCali);
                        getParentFragmentManager().setFragmentResult("dataFromBrainCali", resultSendCali);
                    }

                    Thread.sleep(15);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionRequestCode:
                boolean permissionsGranted = true;
                for(int i = 0; i < grantResults.length;i++)
                {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        permissionsGranted = true;
                    }
                    else
                    {
                        permissionsGranted = false;
                        break;
                    }
                }

                if(!permissionsGranted)
                {
                    Toast.makeText(_context,String.format("Bluetooth permission not granted") ,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    GetAvailableDevices();
                }

                return;
        }
    }

    private void GetAvailableDevices()
    {
        try
        {
            //get available devices
            List<String> devices = Unicorn.GetAvailableDevices();

            //update ui
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(_context, android.R.layout.simple_list_item_1,devices);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            _spnDevices.setAdapter(adapter);
        }
        catch(Exception ex)
        {
            Toast t = Toast.makeText(_context,String.format("Could not detect available devices. %s", ex.getMessage()) ,Toast.LENGTH_SHORT);
        }
    }

    private void StartReceiver()
    {
        _receiverRunning = true;
        _receiver = new Thread(_doReceive);
        _receiver.setPriority(Thread.MIN_PRIORITY);
        _receiver.setDaemon(false);
        _receiver.start();
    }

    private void StopReceiver() throws Exception
    {
        _receiverRunning = false;
        _receiver.join(500);
    }

    private Runnable _doReceive = new Runnable()
    {

        @Override
        public void run()
        {
            while(_receiverRunning)
            {
                try
                {
                    float[] data = _unicorn.GetData();

                    //update main ui
                    _cnt++;
                    if(_cnt % Unicorn.SamplingRateInHz == 0)
                    {
                        Handler mainHandler = new Handler( _context.getMainLooper());
                        Runnable myRunnable = new Runnable() {

                            @Override
                            public void run()
                            {
                                for(int i = 0; i < 17; i++){
                                    OutData[i] = data[i];
                                }
                                OutData[17] = btSetup? 1:0;    //send the connection status


                                for(int i = 0; i < 8; i++){
                                    dataToFourier[i][bufferCounter] = data[i]/1000;
                                    im[i][bufferCounter] = 0;
                                }

                                bufferCounter ++;

                                    //int y = 1;
                                    //int y = 0;
                                    if (bufferCounter >= fourierN) {
                                        bufferCounter = 0;
                                        for (int y = 0; y < NumOfBarChart; y++) {
                                            fft = new FFT(fourierN);
                                            fft.fft(dataToFourier[y], im[y]);
                                            //System.out.print("Mag: [");
                                            for (int x = 0; x < fourierN; x++) {
                                                Mag[y][x] = Math.sqrt(Math.pow(dataToFourier[y][x], 2) + Math.pow(im[y][x], 2));
                                                //System.out.print(((int) (Mag[x] * 1000) / 1000.0) + " ");
                                            }
                                            //System.out.println("]");
                                            //}
                                        }
                                    }
                                for(int x = 0; x < 8; x++){
                                    for(int y = 0; y < 5; y++){
                                        MagCate[x][y] = 0;
                                    }
                                }
                                for(int x = 0; x < 8; x++){
                                    for (int y = 1; y < fourierN / 2 + 1; y++) {   //MAX VALUE
                                        if(y > 35) MagCate[x][4] = Math.max(MagCate[x][4], (float)Mag[x][y]);    //Gamma
                                        else if(y <= 35 && y >= 13) MagCate[x][3] = Math.max(MagCate[x][3], (float)Mag[x][y]);    //Beta
                                        else if(y <= 12 && y >= 8) MagCate[x][2] = Math.max(MagCate[x][2], (float)Mag[x][y]);     //Alpha
                                        else if(y < 8 && y >= 4) MagCate[x][1] = Math.max(MagCate[x][1], (float)Mag[x][y]);      //Theta
                                        else if(y < 4) MagCate[x][0] = Math.max(MagCate[x][0], (float)Mag[x][y]);          //Delta
                                    }
                                }
                                int encounter_ = 0;
                                for(int x = 0; x < 8; x++){
                                    for(int y = 0; y < 5; y++){
                                        OutDataCate[encounter_] = MagCate[x][y];
                                        encounter_ ++;
                                        if(x >= 7 && y >= 4) magDone = true;
                                    }
                                }
                                OutDataCate[40] = btSetup? 1:0;    //send the connection status


                                for (int y = 0; y < NumOfBarChart; y++) {
                                    lineCounter[y]++;
                                    //for(int x = 0; x < NumOfBarChart; x++) {
                                    //int x = 1;

                                    if (lineCounter[y] > 1) {
                                        //lineCounter = 2;
                                        if (y == 0) entries1.removeAll(entries1);
                                        else if (y == 1) entries2.removeAll(entries2);
                                        else if (y == 2) entries3.removeAll(entries3);
                                        else if (y == 3) entries4.removeAll(entries4);
                                        else if (y == 4) entries5.removeAll(entries5);
                                        else if (y == 5) entries6.removeAll(entries6);
                                        else if (y == 6) entries7.removeAll(entries7);
                                        else if (y == 7) entries8.removeAll(entries8);
                                        barData[y].removeDataSet(0);
                                    }
                                    //for (int i = 1; i < fourierN / 2 + 1; i++) {  //ignore 0hz
                                    for (int i = 0; i < 5; i++) {
                                        barEntry[y][i] = new BarEntry(i, (float) MagCate[y][i]);
                                        if (y == 0) entries1.add(barEntry[y][i]);
                                        else if (y == 1) entries2.add(barEntry[y][i]);
                                        else if (y == 2) entries3.add(barEntry[y][i]);
                                        else if (y == 3) entries4.add(barEntry[y][i]);
                                        else if (y == 4) entries5.add(barEntry[y][i]);
                                        else if (y == 5) entries6.add(barEntry[y][i]);
                                        else if (y == 6) entries7.add(barEntry[y][i]);
                                        else if (y == 7) entries8.add(barEntry[y][i]);

                                    }

                                    if (y == 0) barDataSet[y] = new BarDataSet(entries1, "bar");
                                    else if (y == 1) barDataSet[y] = new BarDataSet(entries2, "bar");
                                    else if (y == 2) barDataSet[y] = new BarDataSet(entries3, "bar");
                                    else if (y == 3) barDataSet[y] = new BarDataSet(entries4, "bar");
                                    else if (y == 4) barDataSet[y] = new BarDataSet(entries5, "bar");
                                    else if (y == 5) barDataSet[y] = new BarDataSet(entries6, "bar");
                                    else if (y == 6) barDataSet[y] = new BarDataSet(entries7, "bar");
                                    else if (y == 7) barDataSet[y] = new BarDataSet(entries8, "bar");
                                    barData[y].addDataSet(barDataSet[y]);
                                    barChart[y].setData(barData[y]);
                                    barChart[y].invalidate();
                                    //System.out.println("hihi");
                                }






//                                lineCounter ++;
//                                for(int i = 0; i < NumOfLineChart; i++){
//                                    entry[i] = new Entry(lineCounter, data[i]);
//                                    lineData[i].addEntry(entry[i], 0); // Add entry to the polyline at the specified index
//                                    if(lineCounter > dataRange) lineData[i].removeEntry(lineCounter-dataRange,0);
//                                    mLineChart[i].setData(lineData[i]);
//                                }
//                                showLine(0);



//                                for(int x = 0; x < NumOfBarChart; x++){
//                                    BarEntry barEntry[] = new BarEntry[Mag.length/2+1];
//                                    if(lineCounter > 1) {
//                                        entries.removeAll(entries);
//                                        barData[x].removeDataSet(0);
//                                    }
//                                    for(int i = 1; i < barEntry.length; i++){  //ignore 0hz
//                                        barEntry[i] = new BarEntry(i, (float) Mag[i]);
//                                        entries.add(barEntry[i]);
//                                    }
//
//                                    barDataSet[x] = new BarDataSet(entries, "bar");
//                                    barData[x].addDataSet(barDataSet[x]);
//                                    barChart[x].setData(barData[x]);
//                                    barChart[x].invalidate();
//                                }
//                                lineCounter ++;
//                                for(int x = 0; x < NumOfBarChart; x++) {
//                                    x = 0;
//                                    BarEntry barEntry[] = new BarEntry[Mag.length / 2 + 1];
//                                    if (lineCounter > 1) {
//                                        entries.removeAll(entries);
//                                        barData[x].removeDataSet(0);
//                                    }
//                                    for (int i = 1; i < barEntry.length; i++) {  //ignore 0hz
//                                        barEntry[i] = new BarEntry(i, (float) Mag[i]);
//                                        entries.add(barEntry[i]);
//                                    }
//
//                                    barDataSet[x] = new BarDataSet(entries, "bar");
//                                    barData[x].addDataSet(barDataSet[x]);
//                                    barChart[x].setData(barData[x]);
//                                    barChart[x].invalidate();
//                                }


                               // }



//                                BarEntry barEntry[] = new BarEntry[Mag.length/2+1];
//                                if(lineCounter > 1) {
//                                    entries.removeAll(entries);
//                                    barData.removeDataSet(0);
//                                }
//                                for(int i = 1; i < barEntry.length; i++){  //ignore 0hz
//                                    barEntry[i] = new BarEntry(i, (float) Mag[i]);
//                                    entries.add(barEntry[i]);
//                                }
//
//                                barDataSet = new BarDataSet(entries, "bar");
//                                barData.addDataSet(barDataSet);
//                                barChart.setData(barData);
//                                barChart.invalidate();



                            }
                        };
                        mainHandler.post(myRunnable);
                    }
                }
                catch (Exception ex)
                {
                    //disconnect and update main ui
                    btSetup = false;
                    Handler mainHandler = new Handler( _context.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run()
                        {
                            String message = _tvState.getText().toString();
                            message += String.format("Acquisition failed. %s\n", ex.getMessage());
                            _tvState.setText(message);
                            Disconnect();
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            }
        }
    };

    private void Connect()
    {
        _btnConnect.setEnabled(false);
        _spnDevices.setEnabled(false);
        String device = (String)_spnDevices.getSelectedItem();
        String message = "";
        try
        {
            //update ui message
            message += String.format("Connecting to %s...\n", device);
            _tvState.setText(message);

            //connect to device
            _unicorn = new Unicorn(device);
            _btnConnect.setText(_btnDisconStr);

            //update ui message
            message += "Connected.\n";
            message += "Starting data acquisition...\n";
            _tvState.setText(message);

            //start acquisition
            _unicorn.StartAcquisition();

            message += "Acquisition running.\n";
            _tvState.setText(message);
            //start receiving thread
            StartReceiver();
        }
        catch (Exception ex)
        {
            //close device
            btSetup = false;
            _unicorn = null;
            System.gc();
            System.runFinalization();

            _btnConnect.setText(_btnConStr);
            _spnDevices.setEnabled(true);

            //update ui message
            message += String.format("Could not start acquisition. %s", ex.getMessage());
            _tvState.setText(message);
        }
        _btnConnect.setEnabled(true);
    }

    private void Disconnect()
    {
        _btnConnect.setEnabled(false);
        String device = (String)_spnDevices.getSelectedItem();
        String message = _tvState.getText().toString();
        try
        {
            //update ui message
            message += "\nStopping data acquisition...\n";
            _tvState.setText(message);

            //stop receiving thread
            StopReceiver();

            //stop acquisition
            _unicorn.StopAcquisition();

            //update ui message
            message += String.format("Disconnecting from %s...\n", device);
            _tvState.setText(message);

            //close device
            _unicorn = null;
            System.gc();
            System.runFinalization();

            message += "Disconnected";
            _tvState.setText(message);

            _btnConnect.setText(_btnConStr);

        }
        catch (Exception ex)
        {
            //close device
            _unicorn = null;
            System.gc();
            System.runFinalization();

            _btnConnect.setText(_btnConStr);

            message += String.format("Could not stop acquisition. %s", ex.getMessage());
            _tvState.setText(message);
        }
        _spnDevices.setEnabled(true);
        _btnConnect.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnConnect:
            {
                if(_btnConnect.getText().equals(_btnConStr))
                {
                    if(!btSetup) {
                        btSetup = true;
                        Connect();

                    }
                }
                else
                {
                    btSetup = false;
                    Disconnect();
                }
                break;
            }
        }
    }
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
}
