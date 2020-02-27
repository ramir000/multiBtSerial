package com.unc.reactionlight.service;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ToggleButton;


import com.unc.reactionlight.ClientInterface;
import com.unc.reactionlight.DeviceListAdapter;
import com.unc.reactionlight.R;
import com.unc.reactionlight.controller.bt.bluetooth.manager.BluetoothManager;
import com.unc.reactionlight.controller.bt.fragment.BluetoothFragment;

import org.greenrobot.eventbus.Subscribe;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BluetoothService extends BluetoothFragment {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "BlueTooth_Service";
    private ListView lvNewDevices;
    private ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    private DeviceListAdapter mDeviceListAdapter;
    private Context mContext;
    private ToggleButton btnStop;
    private ArrayList<ClientInterface> mBclientes = new ArrayList<>();
    private Activity mActivity;

    public BluetoothService() {
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMessageMode(BluetoothManager.MessageMode.Bytes);
    }

    public void serverType() {
        Log.d(TAG,"===> Start Server ! Your mac address : " + mBluetoothManager.getYourBtMacAddress());
        setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_120_SEC);
        selectSppServerMode();
    }

    public void stopServer() {
        mBTDevices = new ArrayList<>();
        mDeviceListAdapter = new DeviceListAdapter(mContext, R.layout.device_adapter_view, mBTDevices);
        btnStop.setChecked(false);
        disconnectClient();
    }

    public void startDiscovery() {
        super.mBluetoothManager.scanAllBluetoothDevice();
    }

    public void setStop(ToggleButton stop){
        this.btnStop = stop;
    }

    public void stopDiscovery() {
        super.mBluetoothManager.cancelDiscovery();
    }

    @Override
    public String setUUIDappIdentifier() {
        return "00001101-0000-1000-8000";
    }

    @Override
    public int myNbrClientMax() {
        return 7;
    }

    @Override
    public void onBluetoothDeviceFound(BluetoothDevice device) {
        mBTDevices.add(device);
        Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
        mDeviceListAdapter = new DeviceListAdapter(mContext, R.layout.device_adapter_view, mBTDevices);
        lvNewDevices.setAdapter(mDeviceListAdapter);
        btnStop.setChecked(true);
    }

    public ArrayList<BluetoothDevice> getmBTDevices() {
        return this.mBTDevices;
    }

    public void setListview(ListView lvNewDevices) {
        this.lvNewDevices = lvNewDevices;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    @Subscribe
    public void onClientConnectionSuccess() {

    }

    @Override
    @Subscribe
    public void onClientConnectionFail() {

    }

    @Override
    @Subscribe
    public void onServeurConnectionSuccess(String adrr) {
        int i = mBclientes.size();
        ClientInterface c = new ClientInterface(adrr,i,mActivity,this,mContext);
        ToggleButton mSend = c.getmSend();
        mActivity.runOnUiThread(c.mButton);
        mBclientes.add(c);
        Log.d(TAG, "===> Serveur Connexion success !");
    }

    @Override
    @Subscribe
    public void onServeurConnectionFail() {

    }

    @Override
    @Subscribe
    public void onBluetoothStartDiscovery() {
        Log.d(TAG, "===> Start discovering ! Your mac address : " + mBluetoothManager.getYourBtMacAddress());
    }

    @Override
    @Subscribe
    public void onBluetoothMsgStringReceived(String s) {
        Log.d(TAG, "===> onBluetoothMsgStringReceived(String s) : " + s);
    }

    @Override
    @Subscribe
    public void onBluetoothMsgObjectReceived(Object o) {
    }

    @Override
    @Subscribe
    public void onBluetoothMsgBytesReceived(byte[] bytes) {
        String str = new String(bytes, StandardCharsets.UTF_8);
        Log.d(TAG, "===> onBluetoothMsgStringReceived: " + str);
    }

    @Override
    @Subscribe
    public void onBluetoothNotAviable() {

    }

    @Override
    @Subscribe
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                // TODO stuff if u need
            }
        }
    }


}
