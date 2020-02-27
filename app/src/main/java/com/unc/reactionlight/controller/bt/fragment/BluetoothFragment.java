package com.unc.reactionlight.controller.bt.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.unc.reactionlight.controller.bt.bluetooth.manager.BluetoothManager;
import com.unc.reactionlight.controller.bt.bus.BluetoothCommunicatorBytes;
import com.unc.reactionlight.controller.bt.bus.BluetoothCommunicatorObject;
import com.unc.reactionlight.controller.bt.bus.BluetoothCommunicatorString;
import com.unc.reactionlight.controller.bt.bus.BondedDevice;
import com.unc.reactionlight.controller.bt.bus.ClientConnectionFail;
import com.unc.reactionlight.controller.bt.bus.ClientConnectionSuccess;
import com.unc.reactionlight.controller.bt.bus.ServeurConnectionFail;
import com.unc.reactionlight.controller.bt.bus.ServeurConnectionSuccess;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Rami MARTIN on 13/04/2014.
 */
public abstract class BluetoothFragment extends Fragment {

    protected BluetoothManager mBluetoothManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBluetoothManager = new BluetoothManager(getActivity());
        checkBluetoothAviability();
        mBluetoothManager.setUUIDappIdentifier(setUUIDappIdentifier());
        mBluetoothManager.setNbrClientMax(myNbrClientMax());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        closeAllConnexion();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothManager.REQUEST_DISCOVERABLE_CODE) {
            if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_REFUSED) {
                getActivity().finish();
            } else if (resultCode == BluetoothManager.BLUETOOTH_REQUEST_ACCEPTED) {
                onBluetoothStartDiscovery();
            } else {
                getActivity().finish();
            }
        }
    }

    public void closeAllConnexion(){
        mBluetoothManager.closeAllConnexion();
    }

    public void checkBluetoothAviability(){
        if(!mBluetoothManager.checkBluetoothAviability()){
            onBluetoothNotAviable();
        }
    }

    public void setTimeDiscoverable(int timeInSec){
        mBluetoothManager.setTimeDiscoverable(timeInSec);
    }

    public void startDiscovery(){
        mBluetoothManager.startDiscovery();
    }

    public boolean isConnected(){
        return mBluetoothManager.isConnected;
    }

    public void scanAllBluetoothDevice(){
        mBluetoothManager.scanAllBluetoothDevice();
    }

    public void disconnectClient(){
        mBluetoothManager.disconnectClient(true);
    }

    public void disconnectServer(){
        mBluetoothManager.disconnectServer(true);
    }

    public void createServeur(String address){
        mBluetoothManager.createServeur(address,false);
    }

    public void createSppServeur(String address){
        mBluetoothManager.createServeur(address,true);
    }

    public void selectServerMode(){
        mBluetoothManager.selectServerMode();
    }
    public void selectSppServerMode(){
        mBluetoothManager.selectSppServerMode();
    }
    public void selectClientMode(){
        mBluetoothManager.selectClientMode();
    }

    public BluetoothManager.TypeBluetooth getTypeBluetooth(){
        return mBluetoothManager.mType;
    }

    public BluetoothManager.TypeBluetooth getBluetoothMode(){
        return mBluetoothManager.mType;
    }

    public void createClient(String addressMac){
        mBluetoothManager.createClient(addressMac);
    }

    public void setMessageMode(BluetoothManager.MessageMode messageMode){
        mBluetoothManager.setMessageMode(messageMode);
    }

    public void sendMessageStringToAll(String message){
        mBluetoothManager.sendStringMessageForAll(message);
    }
    public void sendMessageString(String adressMacTarget, String message){
        mBluetoothManager.sendStringMessage(adressMacTarget, message);
    }
    public void sendMessageObjectToAll(Object message){
        mBluetoothManager.sendObjectForAll(message);
    }
    public void sendMessageObject(String adressMacTarget, Object message){
        mBluetoothManager.sendObject(adressMacTarget, message);
    }
    public void sendMessageBytesForAll(byte[] message){
        mBluetoothManager.sendBytesForAll(message);
    }
    public void sendMessageBytes(String adressMacTarget, byte[] message){
        mBluetoothManager.sendBytes(adressMacTarget, message);
    }

    public abstract String setUUIDappIdentifier();

    public abstract int myNbrClientMax();
    @Subscribe
    public abstract void onBluetoothDeviceFound(BluetoothDevice device);
    @Subscribe
    public abstract void onClientConnectionSuccess();
    @Subscribe
    public abstract void onClientConnectionFail();
    @Subscribe
    public abstract void onServeurConnectionSuccess(String adrr);
    @Subscribe
    public abstract void onServeurConnectionFail();
    @Subscribe
    public abstract void onBluetoothStartDiscovery();
    @Subscribe
    public abstract void onBluetoothMsgStringReceived(String message);
    @Subscribe
    public abstract void onBluetoothMsgObjectReceived(Object message);
    @Subscribe
    public abstract void onBluetoothMsgBytesReceived(byte[] message);
    @Subscribe
    public abstract void onBluetoothNotAviable();
    @Subscribe
    public void onEventMainThread(BluetoothDevice device){
        if(!mBluetoothManager.isNbrMaxReached()){
            onBluetoothDeviceFound(device);
            if(mBluetoothManager.mType == BluetoothManager.TypeBluetooth.Server)
            createServeur(device.getAddress());
            else
                createSppServeur(device.getAddress());
        }
    }
    @Subscribe
    public void onEventMainThread(ClientConnectionSuccess event){
        mBluetoothManager.isConnected = true;
        onClientConnectionSuccess();
    }
    @Subscribe
    public void onEventMainThread(ClientConnectionFail event){
        mBluetoothManager.isConnected = false;
        onClientConnectionFail();
    }
    @Subscribe
    public void onEventMainThread(ServeurConnectionSuccess event){
        mBluetoothManager.isConnected = true;
        mBluetoothManager.onServerConnectionSuccess(event.mClientAdressConnected);
        onServeurConnectionSuccess(event.mClientAdressConnected);
    }
    @Subscribe
    public void onEventMainThread(ServeurConnectionFail event){
        mBluetoothManager.onServerConnectionFailed(event.mClientAdressConnectionFail);
        onServeurConnectionFail();
    }
    @Subscribe
    public void onEventMainThread(BluetoothCommunicatorString event){
        onBluetoothMsgStringReceived(event.mMessageReceive);
    }
    @Subscribe
    public void onEventMainThread(BluetoothCommunicatorObject event){
        onBluetoothMsgObjectReceived(event.mObject);
    }
    @Subscribe
    public void onEventMainThread(BluetoothCommunicatorBytes event){
        onBluetoothMsgBytesReceived(event.mBytesReceive);
    }
    @Subscribe
    public void onEventMainThread(BondedDevice event){
        //mActManager.sendMessage("BondedDevice");
    }
}
