package com.unc.reactionlight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.unc.reactionlight.service.BluetoothService;
import com.unc.reactionlight.service.CheckPermissions;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "MainActivity";
    Button btnEnableDisable_Discoverable;
    ToggleButton Stop;
    Fragment mBluetoothService;
    ListView lvNewDevices;


    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    public void disableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }

        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }

    public void onMessageEvent(BluetoothDevice device) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnDiscoverable_on_off);
        Stop = (ToggleButton) findViewById(R.id.Stop);
        Stop.setChecked(false);
        Stop.setText("Stop Server");
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        lvNewDevices.setOnItemClickListener(MainActivity.this);

        if (savedInstanceState == null) {
            mBluetoothService = new BluetoothService();
            ((BluetoothService) mBluetoothService).setListview(lvNewDevices);
            ((BluetoothService) mBluetoothService).setContext(this);
            ((BluetoothService) mBluetoothService).setStop(Stop);
            ((BluetoothService) mBluetoothService).setmActivity(this);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, mBluetoothService).commit();
        }
        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(this, CheckPermissions.class);
            startActivity(intent);
        }
    }

    public void btnEnableDisable_Discoverable(View view) {
        ((BluetoothService) mBluetoothService).startDiscovery();
    }

    public void btnstServer(View view) {
        ((BluetoothService) mBluetoothService).serverType();
    }

    public void btnstopServer(View view) {
        ((BluetoothService) mBluetoothService).stopDiscovery();
        ((BluetoothService) mBluetoothService).stopServer();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((BluetoothService) mBluetoothService).stopDiscovery();
        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = ((BluetoothService) mBluetoothService).getmBTDevices().get(i).getName();
        String deviceAddress = ((BluetoothService) mBluetoothService).getmBTDevices().get(i).getAddress();
        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);
        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // ((BluetoothService) mBluetoothService).createClient(deviceAddress);
        }
    }

    public void btnSend(View view) {
        if (((BluetoothService) mBluetoothService).isConnected()) {
            Log.d(TAG, "connected");
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
    }
}
