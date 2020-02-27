package com.unc.reactionlight.controller.bt.bluetooth.server;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.util.Log;

import com.unc.reactionlight.controller.bt.bluetooth.manager.BluetoothManager;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Rami on 16/06/2017.
 */
public class BluetoothServer extends BluetoothAbstractServer {

    private static final String TAG = BluetoothServer.class.getSimpleName();

    private UUID mUUID;
    private BluetoothServerSocket mServerSocket;

    public BluetoothServer(BluetoothAdapter bluetoothAdapter, String uuiDappIdentifier, String adressMacClient, Activity activity, BluetoothManager.MessageMode messageMode) {
        super(bluetoothAdapter,uuiDappIdentifier,adressMacClient,activity,messageMode);
        mUUID = UUID.fromString(uuiDappIdentifier + "-" + mClientAddress.replace(":", ""));
    }

    @Override
    public void waitForConnection() {
        // NOTHING TO DO IN THE SERVER
    }

    @Override
    public void intiObjReader() throws IOException {
        mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BTISA",mUUID);
        mSocket = mServerSocket.accept();
        mInputStream = mSocket.getInputStream();
    }

    @Override
    public void closeConnection() {
        super.closeConnection();
        try {
            mServerSocket.close();
            mServerSocket = null;
        } catch (Exception e) {
            Log.e("", "===+++> closeConnection Exception e : "+e.getMessage());
        }
    }
}
