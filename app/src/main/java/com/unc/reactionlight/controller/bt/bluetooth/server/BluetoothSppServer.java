package com.unc.reactionlight.controller.bt.bluetooth.server;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.unc.reactionlight.controller.bt.bluetooth.client.BluetoothConnector;
import com.unc.reactionlight.controller.bt.bluetooth.manager.BluetoothManager;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Rami on 16/06/2017.
 */
public class BluetoothSppServer extends BluetoothAbstractServer {

    private static final String TAG = BluetoothSppServer.class.getSimpleName();

    private UUID mUUID;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothConnector mBluetoothConnector;

    public BluetoothSppServer(BluetoothAdapter bluetoothAdapter, String uuiDappIdentifier, String adressMacClient, Activity activity, BluetoothManager.MessageMode messageMode) {
        super(bluetoothAdapter,uuiDappIdentifier,adressMacClient,activity,messageMode);
        mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }

    @Override
    public void waitForConnection() {

        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mClientAddress);

        while (mInputStream == null && CONTINUE_READ_WRITE) {
            mBluetoothConnector = new BluetoothConnector(mBluetoothDevice, false, mBluetoothAdapter, mUUID);

            try {
                mSocket = mBluetoothConnector.connect().getUnderlyingSocket();
                mInputStream = mSocket.getInputStream();
            } catch (IOException e1) {
                Log.e("", "===> mSocket IOException : "+ e1.getMessage());
                //EventBus.getDefault().post(new ClientConnectionFail(mServerAddress));
                e1.printStackTrace();
            }
        }

        if (mSocket == null) {
            Log.e("", "===> mSocket IS NULL");
            return;
        }
    }

    @Override
    public void intiObjReader() throws IOException {};

    @Override
    public void closeConnection() {
        super.closeConnection();
    }
}
