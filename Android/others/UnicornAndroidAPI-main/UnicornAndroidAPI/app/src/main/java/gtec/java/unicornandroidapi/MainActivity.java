package gtec.java.unicornandroidapi;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import gtec.java.unicorn.Unicorn;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    int NumOfChart = 3;
    int dataRange = 100;
    LineDataSet[] lineDataSet = new LineDataSet[NumOfChart];
    LineChart[] mLineChart = new LineChart[NumOfChart];
    LineData[] lineData = new LineData[NumOfChart];

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

    private static final int PermissionRequestCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLineChart[0] = (LineChart) findViewById(R.id.linechart1);
        mLineChart[1] = (LineChart) findViewById(R.id.linechart2);
        mLineChart[2] = (LineChart) findViewById(R.id.linechart3);
        for(int i = 0; i < NumOfChart; i++){
            mLineChart[i].setDrawBorders(false); // show border
            mLineChart[i].getAxisLeft().setDrawLabels(false);
            mLineChart[i].getAxisRight().setDrawLabels(false);
            mLineChart[i].setDrawGridBackground(false);
            mLineChart[i].setSelected(false);
            mLineChart[i].setPinchZoom(false);
            if(i != 0) mLineChart[i].getXAxis().setDrawLabels(false);
            mLineChart[i].getLegend().setEnabled(false);
            mLineChart[i].setMaxVisibleValueCount(0);
            mLineChart[i].getDescription().setText("Line"+String.valueOf(i+1));

            lineData[i] = new LineData();
            lineDataSet[i] = new LineDataSet(null, "Line"+String.valueOf(i+1));
            lineDataSet[i].setDrawCircles(false);
            lineDataSet[i].setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet[i].setColor(Color.RED);
            lineData[i].addDataSet(lineDataSet[i]);
            mLineChart[i].setData(lineData[i]);
        }

        //get ui elements
        try
        {
            _context = this.getApplicationContext();
            _spnDevices = findViewById(R.id.spnDevices);
            _spnDevices.setBackgroundColor(Color.BLACK);
            _btnConnect = findViewById(R.id.btnConnect);
            _tvState = findViewById(R.id.textView);

            _btnConnect.setText(_btnConStr);
            _btnConnect.setOnClickListener(this);
        }
        catch(Exception ex)
        {
            Toast.makeText(_context,String.format("Could not initialize UI elements. %s", ex.getMessage()) ,Toast.LENGTH_SHORT).show();
        }

        //check if bluetooth is supported
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
        {
            Toast.makeText(this, "Bluetooth not supported.", Toast.LENGTH_SHORT).show();
        }

        //request runtime permission for new android devices
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_SCAN}, PermissionRequestCode);
            } else {
                GetAvailableDevices();
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(_context,String.format("Could not acquire available devices. %s", ex.getMessage()) ,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
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
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,devices);
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
        int counter_ = 0;
        int lineCounter = 0;
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
                                // String message = _tvState.getText().toString();
                                // message += ".";
                                // _tvState.setText(message);

                                String message2 = _tvState.getText().toString();
                                counter_++;
                                message2 += data[0]+" ,"+data[1]+" ,"+data[2]+" ,"+Integer.toString(counter_) +"\n";
                                ShowingLine(data);
                                if(counter_ >= 20){
                                    message2 = "";
                                    counter_ = 0;
                                }
                                _tvState.setText(message2);

                                lineCounter ++;
                                Entry entry1 = new Entry(lineCounter, data[0]); // create a point
                                lineData[0].addEntry(entry1, 0); // Add entry to the polyline at the specified index
                                if(lineCounter > dataRange) lineData[0].removeEntry(lineCounter-dataRange,0);
                                mLineChart[0].setData(lineData[0]);

                                Entry entry2 = new Entry(lineCounter, data[1]); // create a point
                                lineData[1].addEntry(entry2, 0); // Add entry to the polyline at the specified index
                                if(lineCounter > dataRange) lineData[1].removeEntry(lineCounter-dataRange,0);
                                mLineChart[1].setData(lineData[1]);

                                Entry entry3 = new Entry(lineCounter, data[2]); // create a point
                                lineData[2].addEntry(entry3, 0); // Add entry to the polyline at the specified index
                                if(lineCounter > dataRange) lineData[2].removeEntry(lineCounter-dataRange,0);
                                mLineChart[2].setData(lineData[2]);
                                showLine(0);
                                
                           }
                        };
                        mainHandler.post(myRunnable);
                    }
                }
                catch (Exception ex)
                {
                    //disconnect and update main ui
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

    private void ShowingLine(float[] data){

    }

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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnConnect:
            {
                if(_btnConnect.getText().equals(_btnConStr))
                {
                    Connect();
                }
                else
                {
                    Disconnect();
                }
                break;
            }
        }
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